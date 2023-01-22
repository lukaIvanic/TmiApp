package com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ExternalReference;

import java.util.ArrayList;
import java.util.List;
public class ReferencesRecyclerViewAdapter extends RecyclerView.Adapter<ReferencesRecyclerViewAdapter.ReferencesViewHolder> {

    List<ExternalReference> externalReferences = new ArrayList<>();

    public class ReferencesViewHolder extends RecyclerView.ViewHolder {
        TextView referenceTextView;
        public ReferencesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.referenceTextView = itemView.findViewById(R.id.phaseRowName);
        }
    }

    @NonNull
    @Override
    public ReferencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reference, parent, false);
        return new ReferencesViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferencesViewHolder holder, int position) {
        final int i = holder.getAdapterPosition();
        ExternalReference externalReference = getItem(i);
        if(externalReference == null) {
            holder.referenceTextView.setText(R.string.adapter_warning_phases);
            return;
        }
        holder.referenceTextView.setText(Html.fromHtml(externalReference.getText(), HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private ExternalReference getItem(int i){
        return (i < externalReferences.size()) ? externalReferences.get(i) : null;
    }

    @Override
    public int getItemCount() {
        return externalReferences.size();
    }

    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

}
