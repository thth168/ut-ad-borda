package com.example.utadborda;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import static android.util.Log.*;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int x = 6;
        int y = 4;
        int c = x + y;
        mButton = (Button) findViewById(R.id.login_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //TODO: IMPLEMENT onCreate
                Log.d(TAG, "Onclick");
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        //TODO: IMPLEMENT onStop
    }
}
