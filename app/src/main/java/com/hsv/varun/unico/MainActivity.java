package com.hsv.varun.unico;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int i1 = r.nextInt(max - min + 1) + min;
                            code = i1 + "";
                            try{
                                synchronized (this){
                                    wait(500);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                Bitmap b = null;
                                                b = encodeAsBitmap(code);
                                                qr_code_image.setImageBitmap(b);
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
            }
        });
    }

    Bitmap encodeAsBitmap(String str) throws WriterException{
        BitMatrix result;
        try{
            result = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE,100,100,null);
        }catch (IllegalArgumentException e){
            return null;
        }
        int[] pixels = new int[100*100];
        for(int i = 0;i < 100;i++){
            int offset = i*100;
            for(int j = 0;j < 100; j++){
                if(result.get(i,j)){
                    pixels[offset + j] = getResources().getColor(R.color.black);
                }
                else {
                    pixels[offset + j] = getResources().getColor(R.color.white);
                }
            }
        }
        Bitmap b = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
        b.setPixels(pixels,0,500,0,0,100,100);
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