package com.example.myapplication.ui.barqrcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BARQRcodeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BARQRcodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is BARQRcode fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}