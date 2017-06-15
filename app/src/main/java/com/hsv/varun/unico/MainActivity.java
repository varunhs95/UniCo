package com.hsv.varun.unico;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Random;

import static com.hsv.varun.unico.MyService.code;

public class MainActivity extends AppCompatActivity {
    ImageView qr_code_image;
    int min = 1000;
    int max = 9999;
    Random r = new Random();
    TextView qr_code;
    Switch activator;
    Button generate;
    RelativeLayout act_main;
    TextView clipContents;
    String pasteData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qr_code_image = (ImageView)findViewById(R.id.qr_code_image);
        qr_code = (TextView)findViewById(R.id.qr_code);
        activator = (Switch)findViewById(R.id.activator);
        act_main = (RelativeLayout)findViewById(R.id.activity_main);
        int i1 = r.nextInt(max - min + 1) + min;
        code = i1 + "";
        if(MyService.isStarted==false)
            activator.setChecked(false);
        else {
            make_qr();
            activator.setChecked(true);
        }
        activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    qr_code.setText("Generating Code... Please Wait..." + "");
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                synchronized (this){
                                    wait(100);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            make_qr();
                                            act_main.setBackgroundColor(0x9ccc65);
                                            // paste it here
                                            if(MyService.isStarted==false) {
                                                start(activator);
                                                clipContents = (TextView) findViewById(R.id.clipcontent_text);
                                                clipContents.setText("---null---");
                                            }
                                            else
                                                activator.setChecked(true);  //Check if any problems are caused
                                        }
                                    });
                                }
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }
                else{
                    qr_code_image.setImageBitmap(null);
                    qr_code.setText("");
                    // service stop
                    clipContents = (TextView)findViewById(R.id.clipcontent_text);
                    clipContents.setText("---null---");
                    stop(activator);
                    act_main.setBackgroundColor(0xffffff);

                }
            }
        });







        generate = (Button)findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_code_image.setImageBitmap(null);
                qr_code.setText("Generating Code... Please Wait");
                activator.setChecked(false);
                activator.setChecked(true);
            }
        });
        //to get contents from clipboard
        clipContents = (TextView)findViewById(R.id.clipcontent_text);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText() + "";
            clipContents.setText(pasteData);
        }catch(Exception e){
            Toast.makeText(this, "Empty ClipBoard", Toast.LENGTH_LONG).show();}

    }

    @Override
    protected void onResume() {
        super.onResume();
        clipContents = (TextView)findViewById(R.id.clipcontent_text);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        pasteData = item.getText() + "";
        clipContents.setText(pasteData);
    }

    public void start(View view)
    {

        startService(new Intent(getBaseContext(), MyService.class));
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }
    public void stop(View view)
    {
        stopService(new Intent(getBaseContext(), MyService.class));
    }



    Bitmap encodeAsBitmap(String str) throws WriterException{
        BitMatrix result;
        try{
            result = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE,500,500,null);
        }catch (IllegalArgumentException e){
            return null;
        }
        int[] pixels = new int[500*500];
        for(int i = 0;i < 500;i++){
            int offset = i*500;
            for(int j = 0;j < 500; j++){
                if(result.get(i,j)){
                    pixels[offset + j] = getResources().getColor(R.color.black);
                }
                else {
                    pixels[offset + j] = getResources().getColor(R.color.white);
                }
            }
        }
        Bitmap b = Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888);
        b.setPixels(pixels,0,500,0,0,500,500);
        return b;
    }

    public void make_qr(){
        Bitmap b = null;
        try {
            b = encodeAsBitmap(code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        qr_code_image.setImageBitmap(b);
        qr_code.setText("Code: " + code + "");
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