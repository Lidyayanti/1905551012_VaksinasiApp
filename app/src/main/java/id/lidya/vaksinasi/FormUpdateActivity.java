package id.lidya.vaksinasi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wajahatkarim3.easyvalidation.core.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import id.lidya.vaksinasi.MyRoom.Database.MyDatabase;
import id.lidya.vaksinasi.MyRoom.Entity.FormVaksinasi;
import id.winata.vaksinasi.R;

public class FormUpdateActivity extends AppCompatActivity {
    private TextInputLayout tilNama,tilUsername,tilemail,tilPassword,tilPasswordConf,tilUsia;
    private TextInputEditText etNama,etUsername,etEmail,etPassword,etPasswordConf,etUsia;
    private SeekBar ratingAplikasi;
    private RadioGroup rgGender;
    private RadioButton rbLakiLaki, rbPerempuan;
    private CheckBox cbHamil, cbIspa, cbAlergi, cbJantung, cbGinjal;
    private TextView btnLogin, tilUserPict;
    private Button btnUserPic;
    private CircularProgressButton btnSubmit;
    private MaterialCardView card;
    private ImageView decoration_1,decoration_2;
    private CircleImageView circleUserPic;
    private boolean validation;

    private MyDatabase myDatabase;
    private Bitmap bitmap;
    private Uri imageUri;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int GALLERY_ADD_PROFILE = 1;
    private static final int MY_IMAGE_REQUEST = 1;
    private static final String USER_IMAGE_NAME = "user_picture";

    public static final String ID = "ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_update);
        this.myDatabase = MyDatabase.createDatabase(this);
        initWidget();
        animIn();
        fillWidget();
        card.animate()
                .translationY(0)
                .setDuration(1000)
                .alpha(1.0f)
                .setListener(null);
    }

    private void fillWidget() {
        FormVaksinasi formVaksinasi = null;
        try{
            Long id = getIntent().getLongExtra(ID,0);
            formVaksinasi = MyDatabase.createDatabase(this).daoFormVaksinasi().getFormVaksinasiById(id);
            assert formVaksinasi != null;

            etNama.setText(formVaksinasi.fullname);
            etUsername.setText(formVaksinasi.username);
            etEmail.setText(formVaksinasi.email);
            cbHamil.setChecked(formVaksinasi.hamil);
            cbIspa.setChecked(formVaksinasi.ispa);
            cbAlergi.setChecked(formVaksinasi.alergi);
            cbJantung.setChecked(formVaksinasi.jantung);
            cbGinjal.setChecked(formVaksinasi.ginjal);

            if (formVaksinasi.gender == "Laki-Laki"){
                rbLakiLaki.setChecked(true);
            }else{
                rbPerempuan.setChecked(true);
            }

            etUsia.setText(formVaksinasi.usia);
            etPassword.setText(formVaksinasi.password);
            etPasswordConf.setText(formVaksinasi.password);
            ratingAplikasi.setProgress(formVaksinasi.rating);

            try{
                circleUserPic.setImageBitmap(loadImageFromStorage(formVaksinasi.getImage()));
            }catch (Exception err){
                circleUserPic.setImageResource(R.drawable.user_placeholder);
            }

        }catch (AssertionError | Exception err){
            Log.e("ERROR",err.toString());
            finish();
        }

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
        rbLakiLaki = findViewById(R.id.radio_button_1);
        rbPerempuan = findViewById(R.id.radio_button_2);
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.startAnimation();
                if(validate()){
                    animOut();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animIn();

                            FormVaksinasi formVaksinasi = FormUpdateActivity.this.myDatabase.daoFormVaksinasi()
                                                                .getFormVaksinasiById(getIntent().getLongExtra(ID,0));

                            formVaksinasi.setFullname(etNama.getText().toString());
                            formVaksinasi.setUsername(etUsername.getText().toString());
                            formVaksinasi.setEmail(etEmail.getText().toString());

                            StringBuilder kebutuhan = new StringBuilder();

                            if(cbHamil.isChecked()){
                                kebutuhan.append("Sedang Hamil, ");
                                formVaksinasi.setHamil(cbHamil.isChecked());
                            }
                            if(cbIspa.isChecked()){
                                kebutuhan.append("Mengalami ISPA, ");
                                formVaksinasi.setIspa(cbIspa.isChecked());
                            }
                            if(cbAlergi.isChecked()){
                                kebutuhan.append("Memiliki Alergi, ");
                                formVaksinasi.setAlergi(cbAlergi.isChecked());
                            }
                            if(cbJantung.isChecked()){
                                kebutuhan.append("Meiliki Riwayat Penyakit Jantung, ");
                                formVaksinasi.setJantung(cbJantung.isChecked());
                            }
                            if(cbGinjal.isChecked()){
                                kebutuhan.append("Memiliki Riwayat Penyakit Ginjal");
                                formVaksinasi.setGinjal(cbGinjal.isChecked());
                            }

                            if(kebutuhan.toString().matches("")){
                                kebutuhan.append("Tidak ada kebutuhan khusus");
                            }

                            formVaksinasi.setKebutuhan(kebutuhan.toString());

                            RadioButton radioButton = findViewById(rgGender.getCheckedRadioButtonId());
                            formVaksinasi.setGender(radioButton.getText().toString());

                            formVaksinasi.setUsia(etUsia.getText().toString());
                            formVaksinasi.setPassword(etPassword.getText().toString());


                            if(imageUri != null && bitmap != null){
                                String path = saveToInternalStorage(bitmap);
                                formVaksinasi.setImage(path);
                            }

                            FormUpdateActivity.this.myDatabase.daoFormVaksinasi().updateFormVaksinasi(formVaksinasi);

                            btnSubmit.revertAnimation();
                            btnSubmit.stopAnimation();

                            finish();
                        }
                    },3000);
                }else{
                    btnSubmit.revertAnimation();
                    btnSubmit.stopAnimation();
                }
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

    private Bitmap loadImageFromStorage(String path){
        Bitmap bitmap = null;

        try {
            File file = new File(path);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }

    public boolean requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FormUpdateActivity.this,
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
}