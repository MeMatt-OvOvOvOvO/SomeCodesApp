package com.example.myapplication.ui.settings;

import static android.graphics.Color.*;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Random;

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

    RelativeLayout layout;

    Button bTheme1;
    Button bTheme2;
    Button bTheme3;

    public static String SHARED_PREFS = "sharedPrefs";
    public static String TEXT = "text";
    public static String INTWIDTH = "intwidth";
    public static String INTHEIGHT = "intheight";

    public static String BUTEKCOLOR = "butek";
    public static String BACKCOLOR = "back";

    public String butekColorFromPrefs;
    public String backColorFromPrefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        layout = binding.layout;

        editText = binding.editTextt;
        button = binding.butekSaveName;
        eTWidth = binding.editTextWidth;
        eTHeight = binding.editTextHeight;
        buttonProperties = binding.butekSaveProperties;
        bTheme1 = binding.butekTheme1;
        bTheme2 = binding.butekTheme2;
        bTheme3 = binding.butekTheme3;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        butekColorFromPrefs = sharedPreferences.getString(BUTEKCOLOR, "");
        backColorFromPrefs = sharedPreferences.getString(BACKCOLOR, "");

        Log.d("TAG", butekColorFromPrefs);
        Log.d("TAG", backColorFromPrefs);

        if (butekColorFromPrefs.isEmpty()) {
            Log.d("TAG", "pusto ");
        }else if(backColorFromPrefs.isEmpty()){
            Log.d("TAG", "pusto ");
        }else {
            layout.setBackgroundColor(Color.parseColor(backColorFromPrefs));
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(butekColorFromPrefs)));
            buttonProperties.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(butekColorFromPrefs)));
        }


        bTheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(BUTEKCOLOR, "#D213F2"); //rozowy
                editor.putString(BACKCOLOR, "#0FA2DB"); //niebieski 1
                editor.commit();

                layout.setBackgroundColor(Color.parseColor("#0FA2DB"));
                button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D213F2")));
                buttonProperties.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D213F2")));
            }
        });

        bTheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(BUTEKCOLOR, "#5C2554");
                editor.putString(BACKCOLOR, "#224B5C");
                editor.commit();

                layout.setBackgroundColor(Color.parseColor("#224B5C"));
                button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5C2554")));
                buttonProperties.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5C2554")));
            }
        });

        bTheme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(BUTEKCOLOR, "#7D26A8");
                editor.putString(BACKCOLOR, "#0B7CA8");
                editor.commit();

                layout.setBackgroundColor(Color.parseColor("#0B7CA8"));
                button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7D26A8")));
                buttonProperties.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7D26A8")));
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFromEditText = editText.getText().toString();
                textFromEditText = textFromEditText.replaceAll("\\s+", "");
                Log.d("TAG", textFromEditText);
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
