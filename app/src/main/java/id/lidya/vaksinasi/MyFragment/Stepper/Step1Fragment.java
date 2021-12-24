package id.lidya.vaksinasi.MyFragment.Stepper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import id.lidya.vaksinasi.MainActivity;
import id.winata.vaksinasi.R;

public class Step1Fragment extends Fragment {
    private Button btnNext;

    public Step1Fragment(){
        // HARUS KOSONG
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNext = view.findViewById(R.id.nextButton);
        Fragment step_2_fragment = new Step2Fragment();

        this.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in,R.animator.slide_out)
                        .replace(R.id.mainactframelayout,step_2_fragment,MainActivity.FRAGMENT_STEP_2_TAG)
                        .addToBackStack(MainActivity.FRAGMENT_STEP_2_TAG).commit();
            }
        });
    }
}