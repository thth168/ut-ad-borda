package com.example.utadborda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.util.Log.*;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.back_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //TODO: IMPLEMENT
                Log.d(TAG, "Onclick");
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        //TODO: IMPLEMENT 
    }
}
