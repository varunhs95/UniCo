package com.hsv.varun.unico;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Random;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import static com.hsv.varun.unico.MyService.code;


        /*

──────▄▀▀▄────────────────▄▀▀▄────
─────▐▒▒▒▒▌──────────────▌▒▒▒▒▌───
─────▌▒▒▒▒▐─────────────▐▒▒▒▒▒▐───
────▐▒▒▒▒▒▒▌─▄▄▄▀▀▀▀▄▄▄─▌▒▒▒▒▒▒▌──
───▄▌▒▒▒▒▒▒▒▀▒▒▒▒▒▒▒▒▒▒▀▒▒▒▒▒▒▐───
─▄▀▒▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▌───
▐▒▒▒▌▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▐───
▌▒▒▌▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▌──
▒▒▐▒▒▒▒▒▒▒▒▒▄▀▀▀▀▄▒▒▒▒▒▄▀▀▀▀▄▒▒▐──
▒▒▌▒▒▒▒▒▒▒▒▐▌─▄▄─▐▌▒▒▒▐▌─▄▄─▐▌▒▒▌─
▒▐▒▒▒▒▒▒▒▒▒▐▌▐█▄▌▐▌▒▒▒▐▌▐█▄▌▐▌▒▒▐─
▒▌▒▒▒▒▒▒▒▒▒▐▌─▀▀─▐▌▒▒▒▐▌─▀▀─▐▌▒▒▒▌
▒▌▒▒▒▒▒▒▒▒▒▒▀▄▄▄▄▀▒▒▒▒▒▀▄▄▄▄▀▒▒▒▒▐
▒▌▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▄▄▄▒▒▒▒▒▒▒▒▒▒▒▐
▒▌▒▒▒▒▒▒▒▒▒▒▒▒▀▒▀▒▒▒▀▒▒▒▀▒▀▒▒▒▒▒▒▐
▒▌▒▒▒▒▒▒▒▒▒▒▒▒▒▀▒▒▒▄▀▄▒▒▒▀▒▒▒▒▒▒▒▐
▒▐▒▒▒▒▒▒▒▒▒▒▀▄▒▒▒▄▀▒▒▒▀▄▒▒▒▄▀▒▒▒▒▐
▒▓▌▒▒▒▒▒▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒▐
▒▓▓▌▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▌
▒▒▓▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▌─
▒▒▓▓▀▀▄▄▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▐──
▒▒▒▓▓▓▓▓▀▀▄▄▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▄▄▀▀▒▌─
▒▒▒▒▒▓▓▓▓▓▓▓▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▒▒▒▒▒▐─
▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▌
▒▒▒▒▒▒▒█▒█▒█▀▒█▀█▒█▒▒▒█▒█▒█▒▒▒▒▒▒▐
▒▒▒▒▒▒▒█▀█▒█▀▒█▄█▒▀█▒█▀▒▀▀█▒▒▒▒▒▒▐
▒▒▒▒▒▒▒▀▒▀▒▀▀▒▀▒▀▒▒▒▀▒▒▒▀▀▀▒▒▒▒▒▒▐
█▀▄▒█▀▄▒█▀▒█▀█▒▀█▀▒█▒█▒█▒█▄▒█▒▄▀▀▐
█▀▄▒█▀▄▒█▀▒█▄█▒▒█▒▒█▀█▒█▒█▀██▒█▒█▐
▀▀▒▒▀▒▀▒▀▀▒▀▒▀▒▒▀▒▒▀▒▀▒▀▒▀▒▒▀▒▒▀▀▐
▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▐

         */





public class MainActivity extends AppCompatActivity {
    final int transparent = 0x0106000d;
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


        qr_code_image = (ImageView) findViewById(R.id.qr_code_image);
        qr_code = (TextView) findViewById(R.id.qr_code);
        activator = (Switch) findViewById(R.id.activator);
        act_main = (RelativeLayout) findViewById(R.id.activity_main);


        if (MyService.isStarted == false) {
            ClipData myClip;
            ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            myClip = ClipData.newPlainText("text", "-!-null-!-");
            myClipboard.setPrimaryClip(myClip);
            int i1 = r.nextInt(max - min + 1) + min;
            code = i1 + "";
            activator.setChecked(false);
            SharedPreferences st = getSharedPreferences("Myfile", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = st.edit();
            edit.putString("code", code);
            edit.putString("clip", "-!-null-!-");
            edit.commit();
        } else {
            SharedPreferences sf = getSharedPreferences("Myfile", Context.MODE_PRIVATE);
            code = sf.getString("code", "DEF");
            make_qr();
            activator.setChecked(true);
        }
        activator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    qr_code.setText("Generating Code... Please Wait..." + "");
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(50);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            make_qr();
                                            act_main.setBackgroundColor(0x9ccc65);
                                            // paste it here
                                            if (MyService.isStarted == false) {
                                                start(activator);
                                                clipContents = (TextView) findViewById(R.id.clipcontent_text);
                                                clipContents.setText("-!-null-!-");
                                            } else
                                                activator.setChecked(true);  //Check if any problems are caused
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                } else {
                    qr_code_image.setImageBitmap(null);
                    qr_code.setText("");
                    // service stop
                    clipContents = (TextView) findViewById(R.id.clipcontent_text);
                    clipContents.setText("---null---");
                    stop(activator);
                    act_main.setBackgroundColor(0xffffff);

                }
            }
        });


        generate = (Button) findViewById(R.id.generate);
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
        clipContents = (TextView) findViewById(R.id.clipcontent_text);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText() + "";
            clipContents.setText(pasteData);
        } catch (Exception e) {
            Toast.makeText(this, "Empty ClipBoard", Toast.LENGTH_LONG).show();
        }

    }

    // Just for modularity sake
    protected void File_Write() {
        SharedPreferences sf = getSharedPreferences("Myfile", Context.MODE_PRIVATE);
        code = sf.getString("code", "DEF");
        make_qr();
        activator.setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MyService.isStarted == true) {
            File_Write();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clipContents = (TextView) findViewById(R.id.clipcontent_text);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        pasteData = item.getText() + "";
        clipContents.setText(pasteData);
        if (MyService.isStarted == true) {
            File_Write();
        }
    }

    public void start(View view) {


        startService(new Intent(getBaseContext(), MyService.class));

        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }

    public void stop(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 500, 500, null);
        } catch (IllegalArgumentException e) {
            return null;
        }
        int[] pixels = new int[500 * 500];
        for (int i = 0; i < 500; i++) {
            int offset = i * 500;
            for (int j = 0; j < 500; j++) {
                if (result.get(i, j)) {
                    pixels[offset + j] = ContextCompat.getColor(getBaseContext(), R.color.black);
                } else {

                    pixels[offset + j] = 0;
                }
            }
        }
        Bitmap b = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        b.setPixels(pixels, 0, 500, 0, 0, 500, 500);
        return b;
    }

    public void make_qr() {
//        InputStream is;
//        is = getResources().openRawResource(R.drawable.loading);
//
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        Bitmap bm;
//
//        opts.inJustDecodeBounds = true;
//        bm = BitmapFactory.decodeStream(is, null, opts);
//
//        Bitmap b = bm;
        Bitmap b = null;
        qr_code_image.setImageBitmap(b);
        try {
            b = encodeAsBitmap(code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        qr_code_image.setImageBitmap(b);
        qr_code.setText("Code: " + code + "");
    }

//REMOVE BELOW COMMENt IF YOU ARE TRYING THE SWIPE FEATURE AGAIN
/*
    public class OnSwipeTouchListener implements OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }

    public void call_Toast(String text){
        Toast.makeText(this,"Swipe Detected",Toast.LENGTH_SHORT);
    }
*/
//END OF COMMENT

}



/*
ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//Either
clipboard.setPrimaryClip(ClipData.newPlainText(text, text));
//or
ClipData clip = ClipData.newPlainText("message", msgText.getText());
clipboard.setPrimaryClip(clip);
 */