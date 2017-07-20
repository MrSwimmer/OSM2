package pro.bignerdranch.android.osmpro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import pro.bignerdranch.android.osmpro.database.ExportImportDB;

import com.bignerdranch.android.osmpro.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Севастьян on 08.03.2017.
 */

public class NoteSettings extends AppCompatActivity {
    private static final String DIALOG_DATE = "DialogDate";
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mButton;
    private TextView mMark;
    private TextView Email;
    private TextView mEnterRegButton;
    private String str = null;
    private TextView mVer;
    private AdView mAdView;
    public static boolean SmartScore=true;
    private Switch mSwitch;
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_note);
        mSwitch = (Switch) findViewById(R.id.switch1);
        if(SmartScore){
            mSwitch.setChecked(true);
        }
        else{
            mSwitch.setChecked(false);
        }
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int str;
                int strbut;
                if(SmartScore){
                    str = R.string.textforoff;
                    strbut = R.string.textbuttonforoff;

                }
                else{
                    str = R.string.textforon;
                    strbut = R.string.textbuttonforon;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteSettings.this);
                builder.setTitle("SmartScore")
                        .setMessage(str)
                        .setPositiveButton(strbut, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SmartScore=!SmartScore;
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                mSwitch.setChecked(SmartScore);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
//        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
//        serviceIntent.setPackage("com.android.vending");
//        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        mAuth = FirebaseAuth.getInstance();
        Email = (TextView) findViewById(R.id.email);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Log.i("LOG", email);
                    mEnterRegButton.setText("Вход выполнен!");
                    Email.setText(email);
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3781095842244998/9275756860");
        mAdView = (AdView) findViewById(R.id.adViewtwo);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mMark = (TextView) findViewById(R.id.buttonMark);
        mEnterRegButton = (TextView) findViewById(R.id.EnterRegBut);
        mEnterRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NoteSettings.this, ExportImportDB.class);
                startActivity(i);
            }
        });
        mButton = (TextView) findViewById(R.id.buttonDel);
        mMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.bignerdranch.android.OSMpro"));
                startActivity(intent);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteSettings.this);
                builder.setTitle("Удаление всех записей")
                        .setMessage("Вы действительно хотите удалить все записи?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteLab.get(NoteSettings.this).delAllNote();
                        Toast.makeText(NoteSettings.this,
                                "Все записи удалены",
                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        mVer = (TextView) findViewById(R.id.buttonVer);
        mVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteSettings.this);
                builder.setTitle("О версии").setMessage("Что нового в этой версии?\nСделали красиво, добавлена функция SmartScore - обучающаяся нейросеть, которая позволяет давать оценку состоянию организма более качественно.\n" +
                        "Что ожидать в следующих версиях?\nFull bugfix").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
