package com.lhickin.speedmath;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;



import java.io.IOException;

public class PlayerFragment extends Fragment {

    Fragment frag = new CountDownFragment();
    Player player;
    EditText nameInput;
    Button nameButton;
    MyDBHandler db;
    String playerName;
    ServerHelper serverHelper = new ServerHelper();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        nameInput = (EditText) view.findViewById(R.id.nameInput);
        nameButton = (Button) view.findViewById(R.id.nameButton);
        db = ((PlayGameActivity) getActivity()).getDbHandler();
        nameButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerName = nameInput.getText().toString();
                        if (!db.playerExists(playerName)) {
                            Log.e("PLAYER FRAGMENT onClick", "Player " + playerName + " does not exist.. adding new player");
                            player = new Player(playerName, playerName);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //add the player to the sql database
                                    db.addPlayer(player);
                                    //add the player to the server
                                    serverHelper.addNewPlayer(player);
                                }
                            }).start();

                        } else {
                            Log.e("PLAYER FRAGMENT onClick", "Player " + playerName + " already exists");
                            Context context = getActivity().getApplicationContext();
                            CharSequence text = "Welcome back " + playerName + "!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            player = new Player(playerName, playerName);
                        }
                        Bundle args = new Bundle();
                        args.putSerializable("player", player);
                        FragmentManager fragManager = getFragmentManager();
                        //pass through the player instance
                        frag.setArguments(args);
                        fragManager.beginTransaction().replace(R.id.frame_container, frag).commit();
                    }
                }
        );

        return view;
    }
}
