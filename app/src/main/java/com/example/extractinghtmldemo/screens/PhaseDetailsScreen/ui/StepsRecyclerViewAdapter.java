package com.example.extractinghtmldemo.screens.PhaseDetailsScreen.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.StepsViewHolder> {


    List<Step> stepsList;
    RowClickListener rowClickListener;

    public StepsRecyclerViewAdapter(RowClickListener rowClickListener) {
        this.stepsList = new ArrayList<>();
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onRowClick(Step step);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        ImageView stepStatusIconImageView;
        TextView stepNameTextView;
        ImageView stepHasAttachmentImageView;
        public StepsViewHolder(@NonNull View itemView, RowClickListener rowClickListener) {
            super(itemView);
            this.stepStatusIconImageView = itemView.findViewById(R.id.stepStatusIcon);
            this.stepNameTextView = itemView.findViewById(R.id.stepRowName);
            this.stepHasAttachmentImageView = itemView.findViewById(R.id.stepHasAttachmentIcon);
            if(rowClickListener != null)
                itemView.setOnClickListener(view -> rowClickListener.onRowClick(getItem(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_step, parent, false);
        return new StepsViewHolder(row, rowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        final int i = holder.getAdapterPosition();
        Step step = getItem(i);
        if(step == null) {
            holder.stepNameTextView.setText(R.string.adapter_warning_phases);
            return;
        }
        holder.stepStatusIconImageView.setImageResource(step.getStatus().getDrawableIdForStatus());
        holder.stepNameTextView.setText("      " + step.getStepName());
        if(step.hasAttachment()){
            holder.stepHasAttachmentImageView.setVisibility(View.VISIBLE);
        }

    }

    private Step getItem(int i){
        return (i < stepsList.size()) ? stepsList.get(i) : null;
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public void setStepsList(List<Step> stepsList) {
        this.stepsList = stepsList;
    }
}
