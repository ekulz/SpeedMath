package com.lhickin.speedmath;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CountDownFragment extends Fragment{


    private static TextView countDown;
    Fragment gameFrag = new GameFragment();
    CountDownTimer time = null;
    private long timeRemaining;
    Player player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);
        countDown = (TextView) view.findViewById(R.id.countDownText);

        //get bundles
        Bundle bundle = this.getArguments();
        player = (Player) bundle.getSerializable("player");


        //Implement the CountDownTimer
        if (savedInstanceState == null) {
            timeRemaining = 4000;
        }else {
            timeRemaining = (long) savedInstanceState.get("timeRemaining");
        }

        countDown();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeRemaining", timeRemaining);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

    public void countDown() {
        time = new CountDownTimer(timeRemaining, 1000) {

            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                countDown.setText("" + millisUntilFinished/1000);
            }

            public void onFinish() {
                Bundle args = new Bundle();
                args.putSerializable("player", player);
                //pass through the player instance
                gameFrag.setArguments(args);
                FragmentManager frag = getFragmentManager();
                frag.beginTransaction().replace(R.id.frame_container, gameFrag).commit();
            }
        };
        time.start();
    }

    public int dpToPixels(int dip) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
    }

}
