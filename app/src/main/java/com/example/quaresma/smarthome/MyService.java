package com.example.quaresma.smarthome;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.id;

public class MyService extends Service {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference presence;
    private ValueEventListener valueEventListener;


    public MyService() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        presence = mFirebaseDatabase.getReference().child("presence");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean statusCasa = dataSnapshot.getValue(Boolean.class);
                if(statusCasa == true) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("msg","ALERTA");
                    String contentTitle = "Sua casa foi invadida";
                    String contentText = "Ligue já para polícia";
                    NotificationUtil.create(getBaseContext(), intent,contentTitle,contentText,id);

                    try{
                        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone toque = RingtoneManager.getRingtone(getBaseContext(), som);
                        toque.play();
                    }
                    catch(Exception e){}
                    Log.i("SERVICO",  statusCasa+ "");
                }else {
                    Log.i("SERVICO",  statusCasa+ "");
                }
                //Log.i("TESTECASA", casaSeguraText + "");
                Log.i("TESTECASA", statusCasa + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        presence.addValueEventListener(valueEventListener);
        return super.onStartCommand(intent, flags, startId);
    }
}
