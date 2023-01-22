package com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;
import com.example.extractinghtmldemo.R;

import java.util.ArrayList;
import java.util.List;

public class PhasesRecyclerViewAdapter extends RecyclerView.Adapter<PhasesRecyclerViewAdapter.PhasesViewHolder> {

    List<Phase> phasesList;
    RowClickListener rowClickListener;

    public PhasesRecyclerViewAdapter(RowClickListener rowClickListener) {
        this.phasesList = new ArrayList<>();
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onRowClick(Phase phase);
    }

    public class PhasesViewHolder extends RecyclerView.ViewHolder {

        ImageView phaseStatusIconImageView;
        TextView phaseNameTextView;
        ImageView phaseHasAttachmentImageView;
        public PhasesViewHolder(@NonNull View itemView, RowClickListener rowClickListener) {
            super(itemView);
            this.phaseStatusIconImageView = itemView.findViewById(R.id.phaseStatusIcon);
            this.phaseNameTextView = itemView.findViewById(R.id.phaseRowName);
            this.phaseHasAttachmentImageView = itemView.findViewById(R.id.phaseHasAttachmentIcon);
            if(rowClickListener != null)
                itemView.setOnClickListener(view -> rowClickListener.onRowClick(getItem(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public PhasesRecyclerViewAdapter.PhasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_phase, parent, false);
        return new PhasesViewHolder(row, rowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhasesViewHolder holder, int position) {
        final int i = holder.getAdapterPosition();
        Phase phase = getItem(i);
        if(phase == null) {
            holder.phaseNameTextView.setText(R.string.adapter_warning_phases);
            return;
        }
        holder.phaseNameTextView.setText("      " + phase.getPhaseName());
        holder.phaseStatusIconImageView.setImageResource(R.drawable.in_process_icon);
        if(phase.hasAttachment()){
            holder.phaseHasAttachmentImageView.setVisibility(View.VISIBLE);
        }else {
            holder.phaseHasAttachmentImageView.setVisibility(View.GONE);
        }

    }

    private Phase getItem(int i){
        return (i < phasesList.size()) ? phasesList.get(i) : null;
    }

    @Override
    public int getItemCount() {
        return phasesList.size();
    }

    public void setPhasesList(List<Phase> phasesList) {
        this.phasesList = phasesList;
    }

}
