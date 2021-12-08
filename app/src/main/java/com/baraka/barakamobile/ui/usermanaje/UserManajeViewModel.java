package com.baraka.barakamobile.ui.usermanaje;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserManajeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserManajeViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("Manajemen Karyawan");
    }

    public LiveData<String> getText(){
        return mText;
    }
}