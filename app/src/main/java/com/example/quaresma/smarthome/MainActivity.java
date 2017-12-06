package com.example.quaresma.smarthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Shared preferences
    public static final String LED1 = "LED1";
    public static final String LED2 = "LED2";
    public static final String LED3 = "LED3";
    private static final String ARCOND = "ARCOND";
    private static final String PRESENCA_BTN = "PRESENCA_BNT";
    private static final String TEMP = "TEMP";
    private static final String PREFS_NAME = "APP_PREFS";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference led1;
    private DatabaseReference led2;
    private DatabaseReference led3;
    private DatabaseReference presence;
    private DatabaseReference presenca_switch;
    private DatabaseReference temperatura;
    private DatabaseReference arCondicionado;

    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    Switch ledSala;
    Switch ledQuarto1;
    Switch ledQuarto2;
    Switch presenca_switchBtn;
    Switch ar_switch;

    TextView casaSeguraText;
    TextView casaWarnningText;
    ImageView casaSeguraImg;
    ImageView casaWarnningImg;

    TextView temperaturaText;

    //Serve de controle para as funções de ligar/desligar os comodos e ar-condicionado
    int cont;
    int cont2;
    int cont3;
    int cont4;
    int cont5;

    private void firebaseDatabaseInstance() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        led1 = mFirebaseDatabase.getReference().child("led");
        led2 = mFirebaseDatabase.getReference().child("led2");
        led3 = mFirebaseDatabase.getReference().child("led3");
        temperatura = mFirebaseDatabase.getReference().child("temperature");
        presence = mFirebaseDatabase.getReference().child("presence");
        presenca_switch = mFirebaseDatabase.getReference().child("presence_switch");
        arCondicionado = mFirebaseDatabase.getReference().child("arCondicionado");//Ventoinha
    }

    private void componentsInstances() {
        temperaturaText = (TextView) findViewById(R.id.temp_text);
        temperaturaText = new TextView(getBaseContext());

        ledSala = (Switch) findViewById(R.id.liga_sala);
        ledQuarto1 = (Switch) findViewById(R.id.switch_quarto1);
        ledQuarto2 = (Switch) findViewById(R.id.switch_quarto2);
        presenca_switchBtn = (Switch) findViewById(R.id.switch_presenca);
        ar_switch = (Switch) findViewById(R.id.switch_ar);

        ledSala = new Switch(getBaseContext());
        ledQuarto1 = new Switch(getBaseContext());
        ledQuarto2 = new Switch(getBaseContext());
        presenca_switchBtn = new Switch(getBaseContext());
        ar_switch = new Switch(getBaseContext());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabaseInstance();
        componentsInstances();

        startService(new Intent(this, MyService.class));

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                temperaturaText = (TextView) findViewById(R.id.temp_text);

                Double temp = dataSnapshot.getValue(Double.class);
                temperaturaText.setText(temp.intValue() + "");
                Log.i("TESTEAGAIN", temp + "");
                Log.i("TESTEAGAIN", temperaturaText + "");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        temperatura.addChildEventListener(childEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                componentsInstances();
                casaSeguraText = (TextView) findViewById(R.id.label_safe);
                casaWarnningText = (TextView) findViewById(R.id.label_warnning);

                casaSeguraImg = (ImageView) findViewById(R.id.safe_house);
                casaWarnningImg = (ImageView) findViewById(R.id.warnning);


                Boolean statusCasa = dataSnapshot.getValue(Boolean.class);

                if (statusCasa == true) {
                    casaSeguraText.setVisibility(View.GONE);
                    casaSeguraImg.setVisibility(View.GONE);
                    casaWarnningText.setVisibility(View.VISIBLE);
                    casaWarnningImg.setVisibility(View.VISIBLE);
                } else {
                    casaSeguraText.setVisibility(View.VISIBLE);
                    casaSeguraImg.setVisibility(View.VISIBLE);
                    casaWarnningText.setVisibility(View.GONE);
                    casaWarnningImg.setVisibility(View.GONE);
                }

                Log.i("TESTECASA", casaSeguraText + "");
                Log.i("TESTECASA", statusCasa + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        presence.addValueEventListener(valueEventListener);


        // Attach a listener to read the data at our posts reference

        led1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean statusLed1 = dataSnapshot.getValue(Boolean.class);

                if (statusLed1 == true)
                    ledSala.setEnabled(true);

                Log.i("TESTES", "teste4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TESTES", "teste3");
            }
        });

        led2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TESTES", "teste4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TESTES", "teste3");
            }
        });

        led3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TESTES", "teste4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TESTES", "teste3");
            }
        });

        presence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TESTES", "teste4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TESTES", "teste3");
            }
        });

        presenca_switch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TESTES", "teste4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TESTES", "teste3");
            }
        });

        arCondicionado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("TESTES", "teste4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TESTES", "teste3");
            }
        });

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                startActivity(new Intent(this, SobreActivity.class));
                break;
            }

        }
        return false;
    }

    public void ligarSala(View v) {

        if (ledSala.isEnabled()) {
            led1.setValue(true);
            cont++;
        }
        if (cont == 2) {
            led1.setValue(false);
            cont = 0;
        }

    }

    public void ligarQuarto1(View v) {

        if (ledQuarto1.isEnabled()) {
            led2.setValue(true);
            cont2++;
        }
        if (cont2 == 2) {
            led2.setValue(false);
            cont2 = 0;
        }

    }

    public void ligarQuarto2(View v) {

        if (ledSala.isEnabled()) {
            led3.setValue(true);
            cont3++;
        }
        if (cont3 == 2) {
            led3.setValue(false);
            cont3 = 0;
        }

    }

    public void ligarArCondicionado(View v) {

        if (ar_switch.isEnabled()) {
            arCondicionado.setValue(true);
            cont4++;
        }
        if (cont4 == 2) {
            arCondicionado.setValue(false);
            cont4 = 0;
        }

    }

    public void ligarPresenca(View v) {

        if (presenca_switchBtn.isEnabled()) {
            presenca_switch.setValue(true);
            cont5++;
        }
        if (cont5 == 2) {
            presenca_switch.setValue(false);
            cont5 = 0;
        }

    }

}
