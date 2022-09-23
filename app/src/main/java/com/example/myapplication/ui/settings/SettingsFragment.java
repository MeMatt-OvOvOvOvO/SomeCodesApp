package com.example.myapplication.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.myapplication.databinding.FragmentSettingsBinding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    EditText editText;
    Button button;
    String textFromEditText;

    EditText eTWidth;
    EditText eTHeight;
    Button buttonProperties;
    Integer width;
    Integer height;


    public static String SHARED_PREFS = "sharedPrefs";
    public static String TEXT = "text";
    public static String INTWIDTH = "intwidth";
    public static String INTHEIGHT = "intheight";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editText = binding.editTextt;
        button = binding.butekSaveName;
        eTWidth = binding.editTextWidth;
        eTHeight = binding.editTextWidth;
        buttonProperties = binding.butekSaveProperties;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFromEditText = editText.getText().toString();
                textFromEditText = textFromEditText.replaceAll("\\s+", "");
                Log.d("TAG", textFromEditText);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(TEXT, textFromEditText);
                editor.commit();



                File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if(!textFromEditText.equals("default")){
                    File dir = new File(storageLoc, textFromEditText);
                    if(!dir.exists()){
                        dir.mkdir();
                    }
                }
            }
        });

        buttonProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                width = Integer.parseInt(eTWidth.getText().toString());
                height = Integer.parseInt(eTHeight.getText().toString());

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(INTWIDTH, width);
                editor.putInt(INTHEIGHT, height);
                editor.commit();

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
