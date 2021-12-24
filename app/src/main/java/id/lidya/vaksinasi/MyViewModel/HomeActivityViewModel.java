package id.winata.vaksinasi.MyViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import id.winata.vaksinasi.MySharedPreference.SharedPreferencesContract;

public class HomeActivityViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> username = new MutableLiveData<>();
    public final MutableLiveData<String> email  = new MutableLiveData<>();

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(SharedPreferencesContract.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        refreshData();
    }

    public void refreshData() throws NullPointerException{
        name.setValue(sharedPreferences.getString(SharedPreferencesContract.USER_NAMA,"Uknown"));
        username.setValue(sharedPreferences.getString(SharedPreferencesContract.USER_USERNAME,"Uknown"));
        email.setValue(sharedPreferences.getString(SharedPreferencesContract.USER_EMAIL,"Uknown"));
    }
}
