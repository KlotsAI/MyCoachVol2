package com.example.mycoach.ui.traning_machines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainingMachinesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrainingMachinesViewModel() {
        mText = new MutableLiveData<>();
       // mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}