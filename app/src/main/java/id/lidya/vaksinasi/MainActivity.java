package id.lidya.vaksinasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import id.lidya.vaksinasi.MyFragment.Stepper.Step1Fragment;
import id.winata.vaksinasi.MySharedPreference.SharedPreferencesContract;
import id.winata.vaksinasi.R;

public class MainActivity extends AppCompatActivity {
    private Button btnNext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String FRAGMENT_STEP_1_TAG = "FRAGMENT_STEP_1_TAG";
    public static final String FRAGMENT_STEP_2_TAG = "FRAGMENT_STEP_2_TAG";
    public static final String FRAGMENT_STEP_3_TAG = "FRAGMENT_STEP_3_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(SharedPreferencesContract.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in,R.animator.slide_out);

        Fragment step_1_fragment = new Step1Fragment();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("FIRT_TIME_OPEN",true)){
                    fragmentTransaction.replace(R.id.mainactframelayout,step_1_fragment,FRAGMENT_STEP_1_TAG).commit();
                }else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        },3000);
    }

    @Override
    public void onBackPressed() {
        /*INIT MODAL*/
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Konfirmasi Keluar")
                .setMessage("Keluar Aplikasi ?")
                .setCancelable(false)
                .setAnimation(R.raw.people)
                .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("BACK", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        // Show Dialog
        mDialog.show();
    }
}