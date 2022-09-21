package com.example.myapplication.ui.barqrcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentBarqrcodeBinding;
import com.journeyapps.barcodescanner.CaptureActivity;


public class BARQRcodeFragment extends Fragment {

    private FragmentBarqrcodeBinding binding;
    Button btscan;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BARQRcodeViewModel BARQRcodeViewModel =
                new ViewModelProvider(this).get(BARQRcodeViewModel.class);

        binding = FragmentBarqrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btscan = binding.idscan;
        btscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }

            private void scanCode() {


            }
            
        });





        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}