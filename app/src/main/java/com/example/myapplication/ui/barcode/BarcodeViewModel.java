package com.example.myapplication.ui.barcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarcodeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BarcodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is barcode fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}