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

public class AdapterSpinnerSplrAddEdit extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSpinnerSplr> splrDataSpinner;

    public AdapterSpinnerSplrAddEdit(Activity activity, List<DataSpinnerSplr> splrDataSpinner){
        this.activity = activity;
        this.splrDataSpinner = splrDataSpinner;
    }

    @Override
    public int getCount() {
        return splrDataSpinner.size();
    }

    @Override
    public Object getItem(int location) {
        return splrDataSpinner.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_spinner_splr_add_edit, null);
        TextView supplier = (TextView) convertView.findViewById(R.id.listSpinnerSplrAddEdit);

        DataSpinnerSplr dataSpinnerSplr;
        dataSpinnerSplr = splrDataSpinner.get(position);

        supplier.setText(dataSpinnerSplr.getNameSplr());


        return convertView;
    }
}
