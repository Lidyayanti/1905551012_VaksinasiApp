package id.lidya.vaksinasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wajahatkarim3.easyvalidation.core.Validator;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import id.lidya.vaksinasi.MyRetrofit.ApiClient;
import id.winata.vaksinasi.MyRetrofit.ServerResponseObject.DeffaultServerResponse;
import id.winata.vaksinasi.MyRetrofit.ServerResponseObject.SpecificServerResponse.UserDataServerResponse;
import id.winata.vaksinasi.MyRetrofit.Services.AuthServices;
import id.winata.vaksinasi.MySharedPreference.SharedPreferencesContract;
import id.winata.vaksinasi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private MaterialCardView card;
    private ImageView decoration_1,decoration_2;
    private TextView btnRegister;
    private TextInputEditText etEmail,etPassword;
    private TextInputLayout tilEmail,tilPassword;
    private CircularProgressButton btnSubmit;

    private boolean validation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(SharedPreferencesContract.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initWidget();
        animIn();
        card.animate()
                .translationY(0)
                .setDuration(1000)
                .alpha(1.0f)
                .setListener(null);
    }

    protected void initWidget(){
        decoration_1 = findViewById(R.id.decoration_1);
        decoration_2 = findViewById(R.id.decoration_2);
        card = findViewById(R.id.card);
        etPassword = findViewById(R.id.password);
        etEmail = findViewById(R.id.email);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btnSubmit = findViewById(R.id.btnsubmit);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    animOut();
                    btnSubmit.startAnimation();
                    postLoginAPI(etEmail.getText().toString().trim(),etPassword.getText().toString().trim());
                }
            }
        });
    }

    private boolean checkInput() {
        validation = true;

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        new Validator(email).nonEmpty().validEmail().minLength(3).maxLength(50).addErrorCallback( it -> {
            tilEmail.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilEmail.setError("");
            return null;
        }).check();

        new Validator(password).nonEmpty().minLength(3).maxLength(50).addErrorCallback( it -> {
            tilPassword.setError(it);
            validation = false;
            return null;
        }).addSuccessCallback(()->{
            tilPassword.setError("");
            return null;
        }).check();

        return validation;
    }

    private void postLoginAPI(String email,String password){
        AuthServices services = ApiClient.getRetrofit().create(AuthServices.class);

        Call<UserDataServerResponse> call = services.login(email,password);

        call.enqueue(new Callback<UserDataServerResponse>() {
            @Override
            public void onResponse(Call<UserDataServerResponse> call, Response<UserDataServerResponse> response) {
                animIn();
                btnSubmit.revertAnimation();
                try{
                    assert response.body() != null;
                    if(response.body().status.matches(DeffaultServerResponse.SUCCESS)){
                        fillSharedPreferences(response.body().data);
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(response.body().status.matches(DeffaultServerResponse.FAIL)){
                        MaterialDialog mDialog = new MaterialDialog.Builder(LoginActivity.this)
                                .setTitle("Gagal Login")
                                .setMessage("Login Failure")
                                .setCancelable(false)
                                .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .build();
                        // Show Dialog
                        mDialog.show();

                        showErrorFromServer(response.body().data);
                    }else{
                        MaterialDialog mDialog = new MaterialDialog.Builder(LoginActivity.this)
                                .setTitle("Unknown Error")
                                .setMessage("Error tidak diketahui")
                                .setCancelable(false)
                                .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .build();
                        // Show Dialog
                        mDialog.show();
                    }
                }catch (AssertionError | NullPointerException e){
                    /*INIT MODAL*/
                    MaterialDialog mDialog = new MaterialDialog.Builder(LoginActivity.this)
                            .setTitle("Unkown Error")
                            .setMessage("Error tidak diketahui")
                            .setCancelable(false)
                            .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .build();
                    // Show Dialog
                    mDialog.show();
                }

            }

            @Override
            public void onFailure(Call<UserDataServerResponse> call, Throwable t) {
                animIn();
                btnSubmit.revertAnimation();
                /*INIT MODAL*/
                MaterialDialog mDialog = new MaterialDialog.Builder(LoginActivity.this)
                        .setTitle("Server Error")
                        .setMessage("Terjadi kesalahan pada server")
                        .setCancelable(false)
                        .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();
                // Show Dialog
                mDialog.show();
            }
        });
    }

    private void fillSharedPreferences(UserDataServerResponse.Data data) {
        try{
            editor.putString(SharedPreferencesContract.USER_NAMA,data.getName());
            editor.putString(SharedPreferencesContract.USER_USERNAME,data.getUsername());
            editor.putString(SharedPreferencesContract.USER_EMAIL,data.getEmail());
            editor.putString(SharedPreferencesContract.USER_PICTURE,data.getUser_picture());
            editor.putString(SharedPreferencesContract.USER_TOKEN,data.getUser_token());
            editor.apply();
        }catch (NullPointerException e){
            /*INIT MODAL*/
            MaterialDialog mDialog = new MaterialDialog.Builder(this)
                    .setTitle("Gagal !")
                    .setMessage("Gagal input credential user")
                    .setCancelable(false)
                    .setPositiveButton("OKE", new MaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    })
                    .build();
            // Show Dialog
            mDialog.show();
        }
    }

    protected void animIn(){
        decoration_1.animate()
                .translationX(0)
                .setDuration(1500);

        decoration_2.animate()
                .translationX(0)
                .setDuration(1500);
    }

    protected void animOut(){
        decoration_1.animate()
                .translationX(decoration_1.getWidth())
                .setDuration(700);

        decoration_2.animate()
                .translationX(-1*decoration_2.getWidth())
                .setDuration(700);
    }

    private void showErrorFromServer(UserDataServerResponse.Data data){
        if(!data.getEmail().matches("")){
            tilEmail.setError(data.getEmail());
        }

        if(!data.getPassword().matches("")){
            tilPassword.setError(data.getPassword());
        }

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
                        LoginActivity.super.onBackPressed();
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