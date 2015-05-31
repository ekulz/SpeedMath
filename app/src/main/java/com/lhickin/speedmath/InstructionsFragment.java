package com.lhickin.speedmath;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InstructionsFragment extends Fragment {

    Fragment frag;
    FragmentManager fragManager;
    private final String textToDisplay = "<body><h1>How To Play:</h1><p>After the game begins, calculate the given equation and tap the button which you think is correct.</p><p>Each time you answer a question incorrectly, 3 seconds will be added to your total time.</p><p>Aim for the quickest time possible, with no errors! Good luck!</p></body>";
    Button arithmeticButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instructions, container, false);
        frag = new OnlineHelpFragment();
        fragManager = getFragmentManager();

        TextView htmlTextView = (TextView)view.findViewById(R.id.htmlText);
        htmlTextView.setText(Html.fromHtml(textToDisplay));

        arithmeticButton = (Button) view.findViewById(R.id.arithmeticHelpButton);
        arithmeticButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragManager.beginTransaction().replace(R.id.frame_container, frag).commit();
                    }
                }
        );

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
