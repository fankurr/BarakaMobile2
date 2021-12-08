package com.baraka.barakamobile.ui.usermanaje;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baraka.barakamobile.R;

import java.util.ArrayList;
import java.util.List;

public class WorkerLogListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<WorkerLogViewModel> workerLogViewModelList = new ArrayList<>();

    public WorkerLogListAdapter(Activity activity, List<WorkerLogViewModel> workerLogViewModelList){
        this.activity = activity;
        this.workerLogViewModelList = workerLogViewModelList;
    }

    @Override
    public int getCount() {
        return workerLogViewModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return workerLogViewModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.card_worker_log_list, parent, false);

        TextView nameWorkerLog = (TextView) convertView.findViewById(R.id.textViewNameLogWorkerList);
        TextView loginWorkerLog = (TextView) convertView.findViewById(R.id.textViewLoginLogWorkerList);
        TextView logoutWorkerLog = (TextView) convertView.findViewById(R.id.textViewLogoutLogWorkerList);

        WorkerLogViewModel workerLogViewModel = workerLogViewModelList.get(position);

        nameWorkerLog.setText(workerLogViewModel.getNameUser());
        loginWorkerLog.setText(workerLogViewModel.getDatetimeLogin());
        logoutWorkerLog.setText(workerLogViewModel.getDatetimeLogout());

        return convertView;
    }
}
