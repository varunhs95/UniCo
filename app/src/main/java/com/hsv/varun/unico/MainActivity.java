package com.hsv.varun.unico;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView qr_code_image;
    TextView qr_code;
    Switch activator;
    Button generate;
    String code = new String();
    int min = 1000;
    int max = 9999;
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qr_code_image = (ImageView)findViewById(R.id.qr_code_image);
        qr_code = (TextView)findViewById(R.id.qr_code);
        activator = (Switch)findViewById(R.id.activator);
        activator.setChecked(false);
        activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    qr_code.setText("Generating Code... Please Wait..." + "");
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int i1 = r.nextInt(max - min + 1) + min;
                            code = i1 + "";
                            try{
                                synchronized (this){
                                    wait(100);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                Bitmap b = null;
                                                b = encodeAsBitmap(code);
                                                qr_code_image.setImageBitmap(b);
                                                qr_code.setText("Code:" + code + "");
                                            }catch (WriterException e){
                                                e.printStackTrace();
                                            }
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
                }
            }
        });
        generate = (Button)findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activator.setChecked(true);
            }
        });
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
}

/*
ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//Either
clipboard.setPrimaryClip(ClipData.newPlainText(text, text));
//or
ClipData clip = ClipData.newPlainText("message", msgText.getText());
clipboard.setPrimaryClip(clip);
 */