package com.example.myapplication.ui.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentQrcodeBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRcodeFragment extends Fragment {

    private FragmentQrcodeBinding binding;
    Button butek;
    Button butekSave;
    EditText text;
    ImageView imageView;
    public static long currentTimeMillis;
    public static String SHARED_PREFS = "sharedPrefs";
    public String pathFromPrefs;
    public static String TEXT = "text";
    RelativeLayout layout;
    public static String BUTEKCOLOR = "butek";
    public static String BACKCOLOR = "back";

    public String butekColorFromPrefs;
    public String backColorFromPrefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRcodeViewModel qRcodeViewModel =
                new ViewModelProvider(this).get(QRcodeViewModel.class);

        binding = FragmentQrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        layout = binding.layout;
        butek = binding.butek;
        text = binding.editText;
        butekSave = binding.butekSave;
        imageView = binding.iamgevieww;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pathFromPrefs = sharedPreferences.getString(TEXT, "");
        butekColorFromPrefs = sharedPreferences.getString(BUTEKCOLOR, "");
        backColorFromPrefs = sharedPreferences.getString(BACKCOLOR, "");

        if (butekColorFromPrefs.isEmpty()) {
            Log.d("TAG", "pusto ");
        }else if(backColorFromPrefs.isEmpty()){
            Log.d("TAG", "pusto ");
        }else {
            layout.setBackgroundColor(Color.parseColor(backColorFromPrefs));
            butek.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(butekColorFromPrefs)));
            butekSave.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(butekColorFromPrefs)));
        }

        Log.d("TAG", pathFromPrefs);

        QRCodeWriter writer = new QRCodeWriter();
        butek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.getText().toString().isEmpty()){
                    Log.d("TAG", "pusty text");
                }else {
                    try {
                        BitMatrix bitMatrix = writer.encode(text.getText().toString(), BarcodeFormat.QR_CODE, 512, 512);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        imageView.setImageBitmap(bmp);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        butekSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageView.getDrawable() == null){
                    Log.d("TAG", "brak obrazka");
                }else {
                    imageView.buildDrawingCache();

                    Bitmap bmp = imageView.getDrawingCache();


                    if (pathFromPrefs.isEmpty() || pathFromPrefs.equals("default")) {
                        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);
                        currentTimeMillis = System.currentTimeMillis();
                        File file = new File(storageLoc, currentTimeMillis + ".jpg");

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();

                            scanFile(getContext(), Uri.fromFile(file));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/" + pathFromPrefs); //context.getExternalFilesDir(null);
                        currentTimeMillis = System.currentTimeMillis();
                        File file = new File(storageLoc, currentTimeMillis + ".jpg");

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();

                            scanFile(getContext(), Uri.fromFile(file));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        return root;
    }
    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}