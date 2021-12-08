package com.baraka.barakamobile.ui.product;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baraka.barakamobile.R;

import java.util.List;

public class AdapterSpinnerCatAddEdit extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<CateViewModel> cateViewModelList;

    public AdapterSpinnerCatAddEdit(Activity activity, List<CateViewModel> cateViewModelList){
        this.activity = activity;
        this.cateViewModelList = cateViewModelList;
    }

    @Override
    public int getCount() {
        return cateViewModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return cateViewModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_spinner_cat_add_edit, null);
        TextView category = (TextView) convertView.findViewById(R.id.listSpinnerCatAddEdit);

        CateViewModel cateViewModel;
        cateViewModel = cateViewModelList.get(position);

        category.setText(cateViewModel.getNameCat());


        return convertView;
    }
}
