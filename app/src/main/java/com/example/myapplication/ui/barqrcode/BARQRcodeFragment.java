package com.example.myapplication.ui.barqrcode;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.databinding.FragmentBarqrcodeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class BARQRcodeFragment extends Fragment {

    LinearLayout layout;
    private FragmentBarqrcodeBinding binding;
    private EditText scannedText;
    private Button buttonCopyToClipboard;
    private PreviewView previewView;

    public static String BUTEKCOLOR = "butek";
    public static String BACKCOLOR = "back";
    public static String SHARED_PREFS = "sharedPrefs";
    public String butekColorFromPrefs;
    public String backColorFromPrefs;

    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BARQRcodeViewModel BARQRcodeViewModel =
                new ViewModelProvider(this).get(BARQRcodeViewModel.class);

        binding = FragmentBarqrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        layout = binding.layout;
        scannedText = binding.scannedText;
        buttonCopyToClipboard = binding.copyToClip;
        previewView = binding.camera;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        butekColorFromPrefs = sharedPreferences.getString(BUTEKCOLOR, "");
        backColorFromPrefs = sharedPreferences.getString(BACKCOLOR, "");

        if (butekColorFromPrefs.isEmpty()) {
            Log.d("TAG", "pusto ");
        }else if(backColorFromPrefs.isEmpty()){
            Log.d("TAG", "pusto ");
        }else {
            layout.setBackgroundColor(Color.parseColor(backColorFromPrefs));
            buttonCopyToClipboard.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(butekColorFromPrefs)));
        }

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

        buttonCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", scannedText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });



        return root;
    }

    private void init(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    bindImageAnalysis(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },ContextCompat.getMainExecutor(getActivity()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            Toast.makeText(getActivity(), "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider){
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280,720)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getActivity()), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                Image mediaImage = image.getImage();
                if(mediaImage != null){
                    InputImage image2 = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

                    BarcodeScanner scanner = BarcodeScanning.getClient();
                    Task<List<Barcode>> result = scanner.process(image2);
                    result.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(@NonNull List<Barcode> barcodes) {
                            for(Barcode barcode : barcodes){
                                String getValue = barcode.getRawValue();
                                scannedText.setText(getValue);
                            }
                            image.close();
                            mediaImage.close();
                        }
                    });
                }
            }
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}