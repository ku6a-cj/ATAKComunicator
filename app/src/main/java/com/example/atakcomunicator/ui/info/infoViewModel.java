package com.example.atakcomunicator.ui.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class infoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public infoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is info fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}