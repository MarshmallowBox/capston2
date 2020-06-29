package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HelpPopupActivity extends Activity {


    Button close;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_popup);
        close = findViewById(R.id.help_popup_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}