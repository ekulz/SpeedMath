package com.lhickin.speedmath;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * http://stackoverflow.com/questions/8937380/how-to-set-id-of-dynamic-created-layout/8937477#8937477
 * for setId issue if present
 *
 * This entire file has the layouts programmatically implemented
 */


public class GameFragment extends Fragment {

    private int number1;
    private int number2;
    private int operator;

    private int actualAnswer;
    private int displayedAnswer;

    private int count;
    private int correctAnswers;
    private int timePenalty;

    private int rightOrWrong;
    private int error;

    private String finalEquationString;
    private Boolean theirAnswer;
    private Boolean correctAnswer;

    //timer
    private Timer timer;
    private TimerTask task;
    private static int secs = 0;
    private static int seconds;
    //private static int minutes;

    //views
    private static TextView finalEquation;
    private static Button correctButton;
    private static Button wrongButton;
    private static TextView questionNumber;
    private static TextView score;
    private static TextView penalty;

    //layouts and params
    private RelativeLayout relativeLayout;
    private RelativeLayout.LayoutParams layoutParams;
    private RelativeLayout.LayoutParams finalEquationParams;
    private RelativeLayout.LayoutParams correctButtonParams;
    private RelativeLayout.LayoutParams wrongButtonParams;
    private RelativeLayout.LayoutParams questionNumberParams;
    private RelativeLayout.LayoutParams scoreParams;
    private RelativeLayout.LayoutParams penaltyParams;

    //frag managers and fragments
    private FragmentManager fragManager;
    private Fragment postGameFrag;

    Handler handle = new Handler();
    Game game = new Game(null, null, null, new Date().getTime());
    Player player;
    MyDBHandler mdb;
    Equation currentEquation;
    ServerHelper serverHelper = new ServerHelper();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createView();
        makeTimer();
        //for resetting the timer
        if (savedInstanceState == null) {
            secs = 0;
            count = 1;
            generateNumbers();
        }
        //for setting the values to what they used to be when app orientation changes
        else {
            secs = (int) savedInstanceState.get("secs");
            count = (int) savedInstanceState.get("questionNumber");
            number1 = (int) savedInstanceState.get("number1");
            number2 = (int) savedInstanceState.get("number2");
            operator = (int) savedInstanceState.get("operator");
            displayedAnswer = (int) savedInstanceState.get("displayedAnswer");
            rightOrWrong = (int) savedInstanceState.get("rightOrWrong");
            error = (int) savedInstanceState.get("error");
            generateEquation();
        }

        //get bundles
        Bundle bundle = this.getArguments();
        player = (Player) bundle.getSerializable("player");

        mdb = ((PlayGameActivity) getActivity()).getDbHandler();

        buttonListeners();
        fragManager = getFragmentManager();
        return relativeLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void buttonListeners() {
        correctButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        correctButtonIsPushed();
                    }
                }
        );
        wrongButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        wrongButtonIsPushed();
                    }
                }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("secs", secs);
        outState.putInt("questionNumber", count);
        outState.putInt("number1", number1);
        outState.putInt("number2", number2);
        outState.putInt("operator", operator);
        outState.putInt("displayedAnswer", displayedAnswer);
        outState.putInt("rightOrWrong", rightOrWrong);
        outState.putInt("error", error);
    }

    public void makeTimer() {
        timer = new Timer();
        task = new myTimerTask();
        timer.schedule(task, 0, 1000);
    }

    private class myTimerTask extends TimerTask {
        @Override
        public void run() {
            secs++;
            updateTime.sendEmptyMessage(0);
        }
    }

    private static Handler updateTime = new Handler() {
        @Override
        public void handleMessage(Message m) {
            seconds = secs;
            //minutes = (secs%60)/60;
            score.setText(String.format("%d seconds have elapsed", seconds));
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        task.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.cancel();
    }

    public void createView() {
        //relative layout and the params
        relativeLayout = new RelativeLayout(getActivity());
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        //equation and question number TextView and the params
        finalEquation = new TextView(getActivity());
        finalEquationParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        questionNumber = new TextView(getActivity());
        //questionNumber.setId(1);
        questionNumberParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        score = new TextView(getActivity());
        scoreParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        penalty = new TextView(getActivity());
        penaltyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        //Correct and Incorrect buttons and their params
        correctButton = new Button(getActivity());
        wrongButton = new Button(getActivity());
        correctButtonParams = new RelativeLayout.LayoutParams(dpToPixels(125), dpToPixels(125));
        wrongButtonParams = new RelativeLayout.LayoutParams(dpToPixels(125), dpToPixels(125));
        correctButton.setText("Correct");
        wrongButton.setText("Wrong");
        correctButton.setTextColor(Color.parseColor("#37474F"));
        wrongButton.setTextColor(Color.parseColor("#37474F"));

        //align everything/make it pretty
        correctButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        correctButtonParams.setMargins(dpToPixels(50),0,0,dpToPixels(50));
        wrongButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        wrongButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        wrongButtonParams.setMargins(0, 0,dpToPixels(50),dpToPixels(50));
        finalEquationParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        finalEquationParams.setMargins(0, dpToPixels(180), 0, 0);
        finalEquation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        finalEquation.setTextColor(Color.WHITE);
        questionNumberParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //questionNumber.setTextSize(pixelsToSp(getActivity(), 40));
        questionNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        questionNumber.setTextColor(Color.WHITE);
        questionNumber.setTypeface(null, Typeface.ITALIC);
        scoreParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        scoreParams.setMargins(0, dpToPixels(20),0, 0);
        //score.setTextSize(pixelsToSp(getActivity(), 40));
        score.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        score.setTextColor(Color.WHITE);
        score.setTypeface(null, Typeface.ITALIC);
        penaltyParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        penaltyParams.setMargins(0, dpToPixels(40),0,0);
        //penalty.setTextSize(pixelsToSp(getActivity(), 40));
        penalty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        penalty.setTypeface(null, Typeface.ITALIC);

        //set the params for everything!
        relativeLayout.setLayoutParams(layoutParams);
        finalEquation.setLayoutParams(finalEquationParams);
        correctButton.setLayoutParams(correctButtonParams);
        wrongButton.setLayoutParams(wrongButtonParams);
        questionNumber.setLayoutParams(questionNumberParams);
        score.setLayoutParams(scoreParams);
        penalty.setLayoutParams(penaltyParams);

        //add buttons/text views etc
        relativeLayout.addView(finalEquation);
        relativeLayout.addView(correctButton);
        relativeLayout.addView(wrongButton);
        relativeLayout.addView(questionNumber);
        relativeLayout.addView(score);
        relativeLayout.addView(penalty);
    }

    public void generateNumbers() {
        //randomly generate 2 numbers and an operator
        number1 = (int) (Math.random() * 10) + 1;
        number2 = (int) (Math.random() * 10) + 1;
        operator = (int) (Math.random() * 4) + 1;
        //50% chance whether the displayed answer will be right or wrong
        rightOrWrong = (int) (Math.random() * 2) + 1;
        //calculate the offset of displayed answer for a wrong equation (Error)
        error = (int) (Math.random() * 4) + 1;
        generateEquation();
    }

    public void generateEquation() {
        StringBuilder equation = new StringBuilder();
        //append the first number
        equation.append(number1);
        //generate/append the operator and calculate the real answer
        if (operator == 1) {
            equation.append(" + ");
            actualAnswer = number1 + number2;
        } else if (operator == 2) {
            equation.append(" - ");
            actualAnswer = number1 - number2;
        } else if (operator == 3) {
            equation.append(" x ");
            actualAnswer = number1 * number2;
        } else if (operator == 4) {
            if ((number1%number2==0) && (number1>number2)) {
                actualAnswer = number1 / number2;
            } else {
                generateNumbers();
                return;
            }
            equation.append(" / ");

        }
        //append the second number and the equals sign
        equation.append(number2 + " = ");

        //we will display the correct answer for the equation
        if (rightOrWrong == 1) {
            displayedAnswer = actualAnswer;
            equation.append(displayedAnswer);
        }
        //we will display an incorrect answer for the equation
        //need to calculate error (-2, -1, +1, +2)
        else {
            if (error == 1) {
                displayedAnswer = actualAnswer - 1;
            } else if (error == 2) {
                displayedAnswer = actualAnswer - 2;
            }else if (error == 3) {
                displayedAnswer = actualAnswer + 1;
            }else {
                displayedAnswer = actualAnswer + 2;
            }
            //append the displayed answer with error
            equation.append(displayedAnswer);

        }
        questionNumber.setText("You have answered " + count + " out of 20 questions");
        finalEquationString = equation.toString();
        finalEquation.setText(finalEquationString);
    }




    //method which does the checking whether the equation was correct or not when yes button is pushed
    //will update score and count and then generate numbers
    public void correctButtonIsPushed() {
        if (actualAnswer == displayedAnswer) {
            correctAnswers++;
            correctAnswer = true;
            penalty.setTextColor(Color.GREEN);
            penalty.setText("Answer correct!");
            penalty.postDelayed(new Runnable() {
                public void run() {
                    penalty.setText("");
                }
            }, 500);
        }else {
            correctAnswer = false;
            secs+=3;
            penalty.setTextColor(Color.RED);
            penalty.setText("A 3 second penalty has been applied");
            penalty.postDelayed(new Runnable() {
                public void run() {
                    penalty.setText("");
                }
            }, 500);
        }
        theirAnswer = true;
        afterButtonPushed();
    }

    //method which does the checking whether the equation was correct or not when no button is pushed
    //updates score, counts, then regenerates the numbers
    public void wrongButtonIsPushed() {
        if (actualAnswer != displayedAnswer) {
            correctAnswers++;
            correctAnswer = true;
            penalty.setTextColor(Color.GREEN);
            penalty.setText("Answer correct!");
            penalty.postDelayed(new Runnable() {
                public void run() {
                    penalty.setText("");
                }
            }, 500);
        }else {
            correctAnswer = false;
            secs+=3;
            penalty.setTextColor(Color.RED);
            penalty.setText("A 3 second penalty has been applied");
            penalty.postDelayed(new Runnable() {
                public void run() {
                    penalty.setText("");
                }
            }, 500);
        }
        theirAnswer = false;
        afterButtonPushed();
    }

    public void afterButtonPushed() {
        if (count < 20) {
            currentEquation = new Equation(count, finalEquationString, theirAnswer, correctAnswer);
            //add equation to game object
            game.addEquation(currentEquation);
            //add equation to sql database
            mdb.addEquation(currentEquation);
            count++;
            generateNumbers();
        }else {
            currentEquation = new Equation(count, finalEquationString, theirAnswer, correctAnswer);
            //add equation to game object and set objects
            game.addEquation(currentEquation);
            game.setScore(secs);
            game.setTimeInSeconds(secs);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //add game to database
                    serverHelper.newGame(game, player);
                }
            }).start();
            //add equation to sql database
            mdb.addEquation(currentEquation);

            timePenalty = (20 - correctAnswers) * 3;
            Bundle args = new Bundle();
            args.putInt("Correct Answers", correctAnswers);
            args.putInt("Penalty", timePenalty);
            args.putInt("Elapsed Time", secs);
            if (game != null) {
                args.putSerializable("game", game);
                Log.e("game", "is valid");
            }else{
                Log.e("game", "is null");
            }
            //test objects
            Log.e("GAME OBJECT TEST", "" + game.toString());
            Log.e("EQUATIONS TEST", "" + game.getEquations());
            //add the game to sql database
            mdb.addGame(game, player);
            postGameFrag = new PostGameFragment();
            postGameFrag.setArguments(args);
            fragManager.beginTransaction().replace(R.id.frame_container, postGameFrag).commit();
        }
    }


    public int dpToPixels(int dip) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }




}



