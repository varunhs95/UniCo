package com.hsv.varun.unico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

/*
ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//Either
clipboard.setPrimaryClip(ClipData.newPlainText(text, text));
//or
ClipData clip = ClipData.newPlainText("message", msgText.getText());
clipboard.setPrimaryClip(clip);
 */