package com.example.myapplication.ui.barcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentBarcodeBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BarcodeFragment extends Fragment {

    private FragmentBarcodeBinding binding;
    Button butek;
    EditText text;
    Button butekSave;
    ImageView imageView;
    public static long currentTimeMillis;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BarcodeViewModel galleryViewModel =
                new ViewModelProvider(this).get(BarcodeViewModel.class);

        binding = FragmentBarcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        butek = binding.butek;
        text = binding.editText;
        butekSave = binding.butekSave;
        imageView = binding.iamgevieww;
        butek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BitMatrix bitMatrix = new MultiFormatWriter().encode(text.getText().toString(), BarcodeFormat.CODE_128, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    ((ImageView) imageView).setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        butekSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.buildDrawingCache();

                Bitmap bmp = imageView.getDrawingCache();

                File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);
                currentTimeMillis = System.currentTimeMillis();
                File file = new File(storageLoc, currentTimeMillis + ".jpg");

                try{
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