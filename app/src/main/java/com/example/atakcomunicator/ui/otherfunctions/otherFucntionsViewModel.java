package com.example.atakcomunicator.ui.otherfunctions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class otherFucntionsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public otherFucntionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is other functions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}