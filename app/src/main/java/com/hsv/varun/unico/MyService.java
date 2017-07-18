package com.hsv.varun.unico;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

//FIRE BASE IMPORTS
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static android.content.ContentValues.TAG;


/*
INSERT COMMENT-IMAGE HERE
 */

public class MyService extends Service {
    public static boolean isStarted= false;
    private ClipboardManager myClipboard;
    public static String code = new String();
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ClipData myClip;
    String value;
    ClipboardManager.OnPrimaryClipChangedListener mPrimaryClipChangedListener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isStarted=true;
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        // Let it continue running until it is stopped.

        SharedPreferences sf=getSharedPreferences("Myfile", Context.MODE_PRIVATE);
        code=sf.getString("code","DEF");


        try {
            myRef = database.getInstance().getReference("code/" + code + "/content");  // REPLACE 1234 with code
            Log.d(TAG, myRef.toString());
        }catch(Exception e){   Toast.makeText(this,  "ERROR : " + e.toString(), Toast.LENGTH_SHORT).show();
        }
        // Read from the database

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Testing1: " + value);
                String text;
                if(value!=null) {
                    text = myClipboard.getPrimaryClip().getItemAt(0).getText().toString();

                    Log.d(TAG, "Testing2: " + text);
                    //CAREFUL, trying to convert value to a string from a clip object.
                    if (!(value.equals(text))) {
                        myClip = ClipData.newPlainText("text", value);
                        myClipboard.setPrimaryClip(myClip);
                        Log.d(TAG, "onDataChange: " + value);
                        callDataToast();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        mPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                Log.d("CHECKING ERROR STATE:", myClipboard.getPrimaryClip().toString());
                String text = myClipboard.getPrimaryClip().getItemAt(0).getText().toString(); //Some problem
                //firebase  push
                if(value!=null && !(value.equals(text))) {
                    callToast();
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("code/" + code + "/content");  // REPLACE 1234 with code

                    myRef.setValue(text);
                    callDataToast();
                    //ClipData clipData = myClipboard.getPrimaryClip();
                    Log.d("TESTING", "********** clip changed, clipData: " + text);
                }
                else if(value==null)
                    Log.d("No value", "Error, Firebase push Unsuccessful");
            }
        };


        ///////////DON'T EVEN THINK ABOUT MOVING THIS//////////////////
        String text = sf.getString("clip","DEF");
        myClip = ClipData.newPlainText("text", text);

        myClipboard.setPrimaryClip(myClip);
        myClipboard.addPrimaryClipChangedListener(mPrimaryClipChangedListener);
        ClipData abc = myClipboard.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        text = item.getText().toString();
        Toast.makeText(this, "Service Started Copied text is currently \n NULL " + text , Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("code/" + code + "/content");  // REPLACE 1234 with code
        myRef.setValue(text);
        ///////////DON'T EVEN THINK ABOUT MOVING THIS//////////////////
        return START_STICKY;
    }
    public void callDataToast()
    {
        Toast.makeText(this, "Server Change Detected", Toast.LENGTH_SHORT).show();
    }
    public void callToast()
    {
        Toast.makeText(this, "Clipboard Modified", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        myClip = ClipData.newPlainText("text", "");
        myClipboard.setPrimaryClip(myClip);
        myClipboard.removePrimaryClipChangedListener(mPrimaryClipChangedListener);
        Toast.makeText(this, "Service Destroyed, Clipboard cleared", Toast.LENGTH_SHORT).show();
        isStarted=false;
    }

}



