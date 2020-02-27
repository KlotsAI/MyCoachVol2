package com.example.mycoach.ui.theory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TheoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TheoryViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}