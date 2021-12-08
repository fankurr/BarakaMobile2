package com.baraka.barakamobile.ui.usermanaje;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baraka.barakamobile.R;
import com.baraka.barakamobile.databinding.ActivityAddEditSplrBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorkerLogAdapter extends RecyclerView.Adapter<WorkerLogAdapter.WorkerLogViewHolder> {

    private List<WorkerLogViewModel> workerLogViewModelList;
    private Context context;
    private LayoutInflater layoutInflater;
    private static ArrayList<View> printView = new ArrayList<>();


    public WorkerLogAdapter(List<WorkerLogViewModel> workerLogViewModelList, Context context){
        this.workerLogViewModelList = workerLogViewModelList;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public WorkerLogAdapter.WorkerLogViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.card_worker_log, parent, false);
        WorkerLogAdapter.WorkerLogViewHolder workerLogViewHolder = new WorkerLogAdapter.WorkerLogViewHolder(view);

        return workerLogViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkerLogAdapter.WorkerLogViewHolder holder, int position) {
        holder.nameWorkerLog.setText(workerLogViewModelList.get(position).getNameUser());
        holder.loginWorkerLog.setText(workerLogViewModelList.get(position).getDatetimeLogin());
        holder.logoutWorkerLog.setText(workerLogViewModelList.get(position).getDatetimeLogout());

    }

    @Override
    public int getItemCount() {
        return workerLogViewModelList.size();
    }

    public class WorkerLogViewHolder extends RecyclerView.ViewHolder {

        TextView nameWorkerLog;
        TextView loginWorkerLog;
        TextView logoutWorkerLog;

        public WorkerLogViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameWorkerLog = itemView.findViewById(R.id.textViewNameLogWorker);
            loginWorkerLog = itemView.findViewById(R.id.textViewLoginLogWorker);
            logoutWorkerLog = itemView.findViewById(R.id.textViewLogoutLogWorker);
        }
    }


}
