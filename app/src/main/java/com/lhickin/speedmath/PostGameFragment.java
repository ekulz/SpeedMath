package com.lhickin.speedmath;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class PostGameFragment extends Fragment implements Serializable{

    private static TextView correctAnswers;
    private static TextView timePenalty;
    private static TextView goodGame;
    private static TextView elapsedTime;
    private static Button mainMenuButton;
    private static Button playAgainButton;

    private Game game;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_postgame, container, false);

        //link text views
        correctAnswers = (TextView) view.findViewById(R.id.numCorrect);
        timePenalty = (TextView) view.findViewById(R.id.timePenalty);
        elapsedTime = (TextView) view.findViewById(R.id.elapsedTime);
        goodGame = (TextView) view.findViewById(R.id.goodGame);

        //bundle and args that were passed through
        Bundle bundle = this.getArguments();
        int bundleCorrect = bundle.getInt("Correct Answers");
        int bundlePenalty = bundle.getInt("Penalty");
        int bundleElapsedTime = bundle.getInt("Elapsed Time");
        game = (Game) bundle.getSerializable("game");


        //check for final score and display appropriate message
        if (bundleCorrect > 17)
            goodGame.setText("Excellent!");
        else if (bundleCorrect > 14 && bundleCorrect < 18 )
            goodGame.setText("Well done!");
        else
            goodGame.setText("You can do better...");

        //set the text views
        correctAnswers.setText("You got " + bundleCorrect + " answers correct!");
        timePenalty.setText("Received a " + bundlePenalty + " second penalty");
        elapsedTime.setText("So your final time was " + bundleElapsedTime + " seconds");

        //set listeners for the main menu and play again buttons
        mainMenuButton = (Button) view.findViewById(R.id.returnHome);
        mainMenuButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent menuIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        startActivity(menuIntent);
                    }
                }
        );
        playAgainButton = (Button) view.findViewById(R.id.playAgain);
        playAgainButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent retryIntent = new Intent(getActivity().getApplicationContext(), PlayGameActivity.class);
                        startActivity(retryIntent);
                    }
                }
        );
        return view;

    }

    public void onBackPressed() {
        return;
    }
}
