package com.example.atakcomunicator.ui.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApiViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ApiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("To jest fragment do dodawania znacznikow");
    }

    public LiveData<String> getText() {
        return mText;
    }
}