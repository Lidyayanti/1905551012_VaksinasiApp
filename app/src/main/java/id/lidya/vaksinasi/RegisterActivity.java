package id.lidya.vaksinasi;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wajahatkarim3.easyvalidation.core.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import id.lidya.vaksinasi.MyRetrofit.ApiClient;
import id.winata.vaksinasi.MyRetrofit.ServerResponseObject.DeffaultServerResponse;
import id.winata.vaksinasi.MyRetrofit.ServerResponseObject.SpecificServerResponse.UserDataServerResponse;
import id.winata.vaksinasi.MyRetrofit.Services.AuthServices;
import id.winata.vaksinasi.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout tilNama,tilUsername,tilemail,tilPassword,tilPasswordConf,tilUsia;
    private TextInputEditText etNama,etUsername,etEmail,etPassword,etPasswordConf,etUsia;
    private SeekBar ratingAplikasi;
    private RadioGroup rgGender;
    private CheckBox cbHamil, cbIspa, cbAlergi, cbJantung, cbGinjal;
    private TextView btnLogin, tilUserPict;
    private Button btnUserPic;
    private CircularProgressButton btnSubmit;
    private MaterialCardView card;
    private ImageView decoration_1,decoration_2;
    private CircleImageView circleUserPic;
    private boolean validation;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int GALLERY_ADD_PROFILE = 1;
    private static final int MY_IMAGE_REQUEST = 1;
    private static final String USER_IMAGE_NAME = "user_picture";

    private Bitmap bitmap;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidget();
        animIn();

        card.animate()
                .translationY(0)
                .setDuration(1000)
                .alpha(1.0f)
                .setListener(null);
    }

    private void initWidget() {
        tilNama = findViewById(R.id.tilNama);
        tilUsername = findViewById(R.id.tilUsername);
        tilemail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilPasswordConf = findViewById(R.id.tilPasswordConfirm);
        tilUserPict = findViewById(R.id.tilUserPict);
        tilUsia = findViewById(R.id.tilUsia);
        etNama = findViewById(R.id.nama);
        etUsername = findViewById(R.id.username);
        etUsia = findViewById(R.id.usia);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etPasswordConf = findViewById(R.id.passwordconfirm);
        ratingAplikasi = findViewById(R.id.ratingaplikasi);
        rgGender = findViewById(R.id.rgGender);
        cbHamil = findViewById(R.id.cbHamil);
        cbIspa = findViewById(R.id.cbIspa);
        cbAlergi = findViewById(R.id.cbAlergi);
        cbJantung = findViewById(R.id.cbJantung);
        cbGinjal = findViewById(R.id.cbGinjal);
        circleUserPic = findViewById(R.id.ivUserPicture);

        decoration_1 = findViewById(R.id.decoration_1);
        decoration_2 = findViewById(R.id.decoration_2);

        card = findViewById(R.id.card);

        btnSubmit = findViewById(R.id.btnsubmit);
        btnUserPic = findViewById(R.id.btnUserPic);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = requestRead();
                if(result){
                    pickFile();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.startAnimation();
                if(validate()){
                    animOut();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RegisterActivity.this,FormResultActivity.class);

                            animIn();
                            StringBuilder kebutuhan = new StringBuilder();

                            if(cbHamil.isChecked()){
                                kebutuhan.append("Sedang Hamil, ");
                                intent.putExtra(FormResultActivity.HAMIL,true);
                            }
                            if(cbIspa.isChecked()){
                                kebutuhan.append("Mengalami ISPA, ");
                                intent.putExtra(FormResultActivity.ISPA,true);
                            }
                            if(cbAlergi.isChecked()){
                                kebutuhan.append("Memiliki Alergi, ");
                                intent.putExtra(FormResultActivity.ALERGI,true);
                            }
                            if(cbJantung.isChecked()){
                                kebutuhan.append("Meiliki Riwayat Penyakit Jantung, ");
                                intent.putExtra(FormResultActivity.JANTUNG,true);
                            }
                            if(cbGinjal.isChecked()){
                                kebutuhan.append("Memiliki Riwayat Penyakit Ginjal");
                                intent.putExtra(FormResultActivity.GINJAL,true);
                            }

                            if(kebutuhan.toString().matches("")){
                                kebutuhan.append("Tidak ada kebutuhan khusus");
                            }


                            if(imageUri != null && bitmap != null){
                                String path = saveToInternalStorage(bitmap);
                                intent.putExtra(FormResultActivity.IMAGE,path);
                            }

                            RadioButton selected = findViewById(rgGender.getCheckedRadioButtonId());
                            String gender = selected.getText().toString();

                            intent.putExtra(FormResultActivity.NAMA,etNama.getText().toString());
                            intent.putExtra(FormResultActivity.USERNAME,etUsername.getText().toString());
                            intent.putExtra(FormResultActivity.EMAIL, etEmail.getText().toString());
                            intent.putExtra(FormResultActivity.USIA,etUsia.getText().toString());
                            intent.putExtra(FormResultActivity.KEBUTUHAN,kebutuhan.toString());
                            intent.putExtra(FormResultActivity.GENDER,gender);
                            intent.putExtra(FormResultActivity.RATING,ratingAplikasi.getProgress());
                            intent.putExtra(FormResultActivity.PASSWORD,etPassword.getText().toString());
                            startActivity(intent);

                            btnSubmit.revertAnimation();
                            btnSubmit.stopAnimation();
                        }
                    },3000);
                }else{
                    btnSubmit.revertAnimation();
                    btnSubmit.stopAnimation();
                }
            }
        });
    }

    private boolean validate(){

        validation = true;

        String nama = etNama.getText().toString();
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String password_confirmation = etPasswordConf.getText().toString();
        String usia = etUsia.getText().toString();

        new Validator(nama).nonEmpty().minLength(3).maxLength(30).addErrorCallback((it)->{
            tilNama.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilNama.setError("");
            return null;
        }).check();

        new Validator(username).nonEmpty().minLength(3).maxLength(20).addErrorCallback((it)->{
            tilUsername.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilUsername.setError("");
            return null;
        }).check();

        new Validator(usia).nonEmpty().greaterThan(0).addErrorCallback((it)->{
            tilUsia.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilUsia.setError("");
            return null;
        }).check();

        new Validator(email).nonEmpty().validEmail().minLength(3).maxLength(50).addErrorCallback( it -> {
            tilemail.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilemail.setError("");
            return null;
        }).check();

        new Validator(password).nonEmpty().minLength(3).maxLength(50).textEqualTo(password_confirmation).addErrorCallback( it -> {
            tilPassword.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilPassword.setError("");
            return null;
        }).check();

        new Validator(password_confirmation).nonEmpty().minLength(3).maxLength(50).textEqualTo(password).addErrorCallback(it -> {
            tilPasswordConf.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilPasswordConf.setError("");
            return null;
        }).check();

        return validation;
    }

    private void animIn(){
        decoration_1.animate()
                .translationX(0)
                .setDuration(1500);

        decoration_2.animate()
                .translationX(0)
                .setDuration(1500);
    }

    private void animOut(){
        decoration_1.animate()
                .translationX(decoration_1.getWidth())
                .setDuration(500);

        decoration_2.animate()
                .translationX(-1*decoration_2.getWidth())
                .setDuration(500);
    }

    public boolean requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void pickFile() {
        Intent fileintent = new Intent(Intent.ACTION_PICK);
        fileintent.setType("image/*");
        try {
            startActivityForResult(fileintent,GALLERY_ADD_PROFILE);
        } catch (ActivityNotFoundException e) {
            Log.e("ERROR",e.toString());
            Toast.makeText(this, "NO STORAGE DETECTED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_IMAGE_REQUEST && resultCode == -1) {
            try {
                imageUri = data.getData();
                circleUserPic.setImageURI(imageUri);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "TIDAK BISA MENGGUNAKAN IMAGE TERSEBUT", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", MODE_PRIVATE);

        // GENERATE RANDOM NAME
        String generatedString = UUID.randomUUID()+".jpg";

        // Create imageDir
        File mypath=new File(directory,generatedString);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath()+"/"+generatedString;
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
                        RegisterActivity.super.onBackPressed();
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