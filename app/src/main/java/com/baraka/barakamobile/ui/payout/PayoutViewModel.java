package com.baraka.barakamobile.ui.payout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PayoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PayoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pengeluaran");
    }

    public LiveData<String> getText() {
        return mText;
    }
}