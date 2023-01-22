package com.example.extractinghtmldemo.screens.ProjectsListScreen.ui;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.Project;

import java.util.List;

public class ProjectsRecyclerViewAdapter extends RecyclerView.Adapter<ProjectsRecyclerViewAdapter.ProjectViewHolder> {

    List<Project> projectList;
    RowClickListener rowClickListener;

    private boolean showLeader = true;
    private boolean showType = true;
    private boolean showYear = true;

    public ProjectsRecyclerViewAdapter(List<Project> projectList, RowClickListener rowClickListener) {
        this.projectList = projectList;
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onRowClick(Project project);
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        ImageView statusIconImageView;
        TextView projectNameTextView;
        TextView projectLeaderTextView;
        TextView projectYearTextView;
        TextView projectTypeTextView;
        public ProjectViewHolder(@NonNull View itemView, RowClickListener rowClickListener) {
            super(itemView);
            this.statusIconImageView = itemView.findViewById(R.id.statusIcon);
            this.projectNameTextView = itemView.findViewById(R.id.projectRowName);
            this.projectLeaderTextView = itemView.findViewById(R.id.projectRowLeader);
            this.projectYearTextView = itemView.findViewById(R.id.projectRowYear);
            this.projectTypeTextView = itemView.findViewById(R.id.projectRowType);
            itemView.setOnClickListener(view -> rowClickListener.onRowClick(getItem(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public ProjectsRecyclerViewAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_project, parent, false);
        return new ProjectViewHolder(row, rowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsRecyclerViewAdapter.ProjectViewHolder holder, int position) {
        Project project = getItem(holder.getAdapterPosition());

        if(project == null) {
            holder.projectNameTextView.setText(R.string.adapter_warning);
            return;
        }


        holder.projectNameTextView.setText(getHtmlText(project.getName(), "name"));

        holder.projectYearTextView.setVisibility((showYear) ? View.VISIBLE : View.GONE);
        holder.projectYearTextView.setText(getHtmlText(project.getYear(), "year"));

        holder.projectLeaderTextView.setVisibility((showLeader) ? View.VISIBLE : View.GONE);
        holder.projectLeaderTextView.setText(getHtmlText(project.getLeader(), "leader"));

        holder.projectTypeTextView.setVisibility((showType) ? View.VISIBLE : View.GONE);
        holder.projectTypeTextView.setText(getHtmlText(project.getType(), "type"));


        if (project.getProjectStatus() != null)
            holder.statusIconImageView.setImageResource(project.getProjectStatus().getDrawableIdForStatus());


    }

    private Spanned getHtmlText(String mainPart, String secondaryPart){
        String s = mainPart + "<span style=\"color:#f8f8f8;\"> <i> <small>\t\t" + secondaryPart + "</small> </i> </span>";
        return Html.fromHtml(s, HtmlCompat.FROM_HTML_MODE_LEGACY);
    }

    private Project getItem(int i){
        return (i < projectList.size()) ? projectList.get(i) : null;
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public void setShowLeader(boolean showLeader) {
        this.showLeader = showLeader;
        notifyDataSetChanged();
    }

    public void setShowType(boolean showType) {
        this.showType = showType;
        notifyDataSetChanged();
    }

    public void setShowYear(boolean showYear) {
        this.showYear = showYear;
        notifyDataSetChanged();
    }
}
