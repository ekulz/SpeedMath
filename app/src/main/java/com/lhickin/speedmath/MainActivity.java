package com.lhickin.speedmath;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //find id's
        Button playButton = (Button)findViewById(R.id.playButton);
        Button instructionsButton = (Button)findViewById(R.id.instructionsButton);
        Button highScoresButton = (Button)findViewById(R.id.highScoresButton);
        Button aboutButton = (Button)findViewById(R.id.aboutButton);

        //play listener
        playButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent playIntent = new Intent(MainActivity.this, PlayGameActivity.class);
                        startActivity(playIntent);
                    }
                }
        );

        //instructions listener
        instructionsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent instructionsIntent = new Intent(MainActivity.this, InstructionsActivity.class);
                        startActivity(instructionsIntent);
                    }
                }
        );

        //high scores listener
        highScoresButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent highScoresIntent = new Intent(MainActivity.this, HighScoresActivity.class);
                        startActivity(highScoresIntent);
                    }
                }
        );

        //about listener
        aboutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                    }
                }
        );

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
