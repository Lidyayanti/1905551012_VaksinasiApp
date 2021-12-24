package id.lidya.vaksinasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import id.winata.vaksinasi.MyViewModel.HomeActivityViewModel;
import id.winata.vaksinasi.R;

public class HomeActivity extends AppCompatActivity {
    private HomeActivityViewModel homeActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
    }
}