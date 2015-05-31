package com.lhickin.speedmath;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class PlayGameActivity extends Activity {

    MyDBHandler dbHandler;
    Fragment frag = new PlayerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dbHandler = new MyDBHandler(this, null, null, 1);
        if (dbHandler!=null) Log.e("DBHANDLER", "not null");
        //fragment manager for swapping the fragments in
        if (savedInstanceState ==  null) {
            FragmentManager fragManager = getFragmentManager();
            fragManager.beginTransaction().replace(R.id.frame_container, frag).commit();
        }

    }

    @Override
    public void onBackPressed() {
        Intent aboutIntent = new Intent(PlayGameActivity.this, MainActivity.class);
        startActivity(aboutIntent);
    }

    public MyDBHandler getDbHandler() {
        return this.dbHandler;
    }


}
