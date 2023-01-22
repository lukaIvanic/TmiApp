package com.example.extractinghtmldemo.screens.StepDetailsScreen.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.enums.AttachmentType;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Attachment;

import java.util.ArrayList;
import java.util.List;

public class AttachmentsRecyclerViewAdapter extends RecyclerView.Adapter<AttachmentsRecyclerViewAdapter.AttachmentsViewHolder> {


    List<Attachment> attachmentList;
    RowClickListener rowClickListener;

    public AttachmentsRecyclerViewAdapter(RowClickListener rowClickListener) {
        this.attachmentList = new ArrayList<>();
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onRowClick(Attachment attachment);

        void onDownloadClick(Attachment attachment);
    }

    public class AttachmentsViewHolder extends RecyclerView.ViewHolder {

        ImageView attachmentTypeImageView;
        TextView attachmentNameTextView;
        ImageView attachmentDownloadImageView;

        public AttachmentsViewHolder(@NonNull View itemView, RowClickListener rowClickListener) {
            super(itemView);
            this.attachmentTypeImageView = itemView.findViewById(R.id.attachmentTypeIcon);
            this.attachmentNameTextView = itemView.findViewById(R.id.attachmentRowName);
            this.attachmentDownloadImageView = itemView.findViewById(R.id.stepAttachmentDownloadIcon);

            itemView.setOnClickListener(view -> rowClickListener.onRowClick(getItem(getAdapterPosition())));

            attachmentDownloadImageView.setOnClickListener(view -> rowClickListener.onDownloadClick(getItem(getAdapterPosition())));

        }
    }

    @NonNull
    @Override
    public AttachmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attachment, parent, false);
        return new AttachmentsViewHolder(row, rowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsViewHolder holder, int position) {
        final int i = holder.getAdapterPosition();
        Attachment attachment = getItem(i);
        if (attachment == null) {
            holder.attachmentNameTextView.setText(R.string.adapter_warning_phases);
            return;
        }
        if (attachment.getAttachmentType() != AttachmentType.UNKNOWN) {
            holder.attachmentTypeImageView.setImageResource(attachment.getAttachmentType().getDrawableIdForStatus());
        }
        holder.attachmentNameTextView.setText("      " + attachment.getAttachmentName());

    }

    private Attachment getItem(int i) {
        return (i < attachmentList.size()) ? attachmentList.get(i) : null;
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

}
