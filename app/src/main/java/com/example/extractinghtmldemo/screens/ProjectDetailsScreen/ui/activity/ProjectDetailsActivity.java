package com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.extractinghtmldemo.networking.NetworkManager;
import com.example.extractinghtmldemo.screens.LoginScreen.ui.LoginActivity;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.ui.PhaseDetailsActivity;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;
import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.data.DataManager;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui.PhasesRecyclerViewAdapter;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui.ReferencesRecyclerViewAdapter;
import com.example.extractinghtmldemo.screens.ProjectsListScreen.ProjectMinimal;
import com.example.extractinghtmldemo.screens.ui_utils.TmiLinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;

public class ProjectDetailsActivity extends AppCompatActivity {

    ProjectMinimal projectMinimal;
    ConstraintLayout projectDetailsLayout;

    ProgressBar projectDetailsProgressBar;

    TextView projectDetailsNameTextView;
    RecyclerView phasesRecyclerView;
    PhasesRecyclerViewAdapter phasesRecyclerViewAdapter;

    TextView referencesTitleTextView;
    RecyclerView referencesRecyclerView;
    ReferencesRecyclerViewAdapter referencesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        setTitleToRepoName();

        projectMinimal = new ProjectMinimal(getProjectNameFromIntent(), getProjectIdFromIntent());

        projectDetailsLayout = findViewById(R.id.projectDetailsLayout);

        projectDetailsNameTextView = findViewById(R.id.project_details_name);
        projectDetailsNameTextView.setText(projectMinimal.getName());

        projectDetailsProgressBar = findViewById(R.id.projectDetailsProgressBar);

        referencesTitleTextView = findViewById(R.id.project_details_references_title);

        phasesRecyclerView = findViewById(R.id.phasesRecyclerView);
        phasesRecyclerViewAdapter = new PhasesRecyclerViewAdapter(phase -> phaseClicked(phase));
        phasesRecyclerView.setLayoutManager(new TmiLinearLayoutManager(this, false));
        phasesRecyclerView.setAdapter(phasesRecyclerViewAdapter);

        referencesRecyclerView = findViewById(R.id.referencesRecyclerView);
        referencesRecyclerViewAdapter = new ReferencesRecyclerViewAdapter();
        referencesRecyclerView.setLayoutManager(new TmiLinearLayoutManager(this, false));
        referencesRecyclerView.setAdapter(referencesRecyclerViewAdapter);

        DataManager.getProjectDetails(projectMinimal, (project, exception) -> {

            if(exception != null){
                return;
            }

            runOnUiThread(() -> {

                projectDetailsProgressBar.setVisibility(View.GONE);

                if (!project.getPhasesList().isEmpty()) {
                    phasesRecyclerViewAdapter.setPhasesList(project.getPhasesList());
                    phasesRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    showEmptyPopup();
                }



                if (!project.getReferencesList().isEmpty()) {
                    revealReferenceSection();
                    referencesRecyclerViewAdapter.setExternalReferences(project.getReferencesList());
                    referencesRecyclerViewAdapter.notifyDataSetChanged();
                }

            });

        });

    }

    private void setTitleToRepoName() {
        setTitle(DataManager.getRepositoryName());
    }

    private void revealReferenceSection() {
        referencesTitleTextView.setVisibility(View.VISIBLE);
        referencesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showEmptyPopup() {
        Snackbar.make(projectDetailsLayout, "This project is empty.", Snackbar.LENGTH_INDEFINITE).show();
    }

    private void phaseClicked(Phase phase) {
        Intent intent = new Intent(ProjectDetailsActivity.this, PhaseDetailsActivity.class);
        intent.putExtra("phase_id", phase.getPhaseId());
        intent.putExtra("phase_name",  projectMinimal.getName() + " > " + phase.getPhaseName());
        startActivity(intent);
    }

    private String getProjectIdFromIntent() {
        return getIntent().getExtras().getString("project_id");
    }

    private String getProjectNameFromIntent() {
        return getIntent().getExtras().getString("project_name");
    }

    private void clearDataForLogout(){
        NetworkManager.clearData();
        DataManager.clearData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bar_menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu_item:
                doLogout();

                break;
            default:
                break;
        }
        return true;
    }

    private void doLogout() {
        clearDataForLogout();
        Intent intent = new Intent(ProjectDetailsActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}