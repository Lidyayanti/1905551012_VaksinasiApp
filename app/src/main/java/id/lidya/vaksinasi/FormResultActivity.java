package id.lidya.vaksinasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.lidya.vaksinasi.MyAdapter.FormVaksinasiAdapter;
import id.lidya.vaksinasi.MyRoom.Database.MyDatabase;
import id.lidya.vaksinasi.MyRoom.Entity.FormVaksinasi;
import id.winata.vaksinasi.R;

public class FormResultActivity extends AppCompatActivity {
    private TextInputEditText etNama,etUsername,etEmail,etUsia,etGender,etKebutuhan,etRatingAplikasi;
    private CircleImageView circleImageView;
    private MyDatabase myDatabase;
    private RecyclerView recyclerViewFormVaksinasi;
    private FormVaksinasiAdapter formVaksinasiAdapter;

    public static final String BITMAP = "BITMAP";
    public static final String NAMA = "NAMA";
    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";
    public static final String KEBUTUHAN = "KEBUTUHAN";
    public static final String HAMIL = "HAMIL";
    public static final String ISPA = "ISPA";
    public static final String ALERGI = "ALERGI";
    public static final String JANTUNG = "JANTUNG";
    public static final String GINJAL = "GINJAL";
    public static final String GENDER = "GENDER";
    public static final String USIA = "USIA";
    public static final String RATING = "RATING";
    public static final String PASSWORD = "PASSWORD";
    public static final String IMAGE = "IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_result);

        this.myDatabase = MyDatabase.createDatabase(this);

        initWidget();

        insertFormVaksinasi();

    }

    private void initWidget() {
        this.recyclerViewFormVaksinasi = findViewById(R.id.rvFromVaksinasi);
        this.recyclerViewFormVaksinasi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void insertFormVaksinasi() {
        FormVaksinasi formVaksinasi = new FormVaksinasi();
        formVaksinasi.setFullname(getIntent().getStringExtra(NAMA));
        formVaksinasi.setUsername(getIntent().getStringExtra(USERNAME));
        formVaksinasi.setEmail(getIntent().getStringExtra(EMAIL));
        formVaksinasi.setKebutuhan(getIntent().getStringExtra(KEBUTUHAN));
        formVaksinasi.setHamil(getIntent().getBooleanExtra(HAMIL,false));
        formVaksinasi.setIspa(getIntent().getBooleanExtra(ISPA,false));
        formVaksinasi.setAlergi(getIntent().getBooleanExtra(ALERGI,false));
        formVaksinasi.setJantung(getIntent().getBooleanExtra(JANTUNG,false));
        formVaksinasi.setGinjal(getIntent().getBooleanExtra(GINJAL,false));
        formVaksinasi.setGender(getIntent().getStringExtra(GENDER));
        formVaksinasi.setUsia(getIntent().getStringExtra(USIA));
        formVaksinasi.setRating(getIntent().getIntExtra(RATING,0));
        formVaksinasi.setPassword(getIntent().getStringExtra(PASSWORD));
        formVaksinasi.setImage(getIntent().getStringExtra(IMAGE));

        this.myDatabase.daoFormVaksinasi().insertFormVaksinasi(formVaksinasi);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView(){
        if(this.formVaksinasiAdapter == null){
            Toast.makeText(this, "PERUBAHAN 1", Toast.LENGTH_SHORT).show();
            formVaksinasiAdapter = new FormVaksinasiAdapter(this.myDatabase.daoFormVaksinasi().getAllFormVaksinasi());
            recyclerViewFormVaksinasi.setAdapter(formVaksinasiAdapter);
        }else{
            Toast.makeText(this, "PERUBAHAN 2", Toast.LENGTH_SHORT).show();
            formVaksinasiAdapter.listFormVaksinasi = this.myDatabase.daoFormVaksinasi().getAllFormVaksinasi();
            formVaksinasiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "ACTIVITY START", Toast.LENGTH_SHORT).show();
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "ACTIVITY RESUME", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "ACTIVITY STOP", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "ACTIVITY DESTROY", Toast.LENGTH_SHORT).show();
    }
}