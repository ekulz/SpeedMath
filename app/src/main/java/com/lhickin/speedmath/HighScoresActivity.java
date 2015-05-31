package com.lhickin.speedmath;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HighScoresActivity extends AppCompatActivity {

    TextView highScoreText;
    TextView highScoreTextScores;
    MyDBHandler dbHandler;
    String dbString;
    String dbString2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        getSupportActionBar().setTitle("High Scores");
        highScoreText = (TextView) findViewById(R.id.highscoreText);
        highScoreTextScores = (TextView) findViewById(R.id.highscoreTextScore);
        dbHandler = new MyDBHandler(this, null, null, 1);
        highScoreText.setText("Loading highscores may have failed, try again...");
        printDatabase();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //print database

    public void printDatabase() {
        dbString = dbHandler.databaseToStringNames();
        dbString2 = dbHandler.databaseToStringScores();
        highScoreText.setText(dbString);
        highScoreTextScores.setText(dbString2);
        Log.e("printDatabase()", "method finished");
    }

}
