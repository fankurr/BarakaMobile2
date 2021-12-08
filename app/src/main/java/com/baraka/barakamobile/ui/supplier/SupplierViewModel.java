package com.baraka.barakamobile.ui.supplier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SupplierViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SupplierViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Supplier");
    }

    public LiveData<String> getText() {
        return mText;
    }
}