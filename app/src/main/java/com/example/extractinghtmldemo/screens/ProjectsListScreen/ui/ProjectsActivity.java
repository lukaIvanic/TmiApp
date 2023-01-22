package com.example.extractinghtmldemo.screens.ProjectsListScreen.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.extractinghtmldemo.networking.NetworkManager;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.Project;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui.activity.ProjectDetailsActivity;
import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.data.DataManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectsActivity extends AppCompatActivity {

    String searchText = "";
    ProgressBar projectsProgressBar;
    RecyclerView projectsRecyclerView;
    ProjectsRecyclerViewAdapter projectsRecyclerViewAdapter;


    List<Project> projects = new ArrayList<>();
    boolean filterLocked = false;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        setupUI();
        setupData();
    }


    private void setupUI() {
        setTitle("Repository");
        setupSearch();
        setupChips();
        setupRecyclerView();
    }

    private void setupData() {
        getRepository();
    }

    private void setupSearch() {
        SearchView projectsSearchView = findViewById(R.id.projectsSearchView);
        projectsSearchView.clearFocus();
        projectsSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                displayProjects();
                return false;
            }
        });
    }

    private void setupChips() {             
        Chip leaderChip = findViewById(R.id.leaderChip);
        leaderChip.setOnCheckedChangeListener((cb, b) ->
                projectsRecyclerViewAdapter.setShowLeader(leaderChip.isChecked()));

        Chip typeChip = findViewById(R.id.yearChip);
        typeChip.setOnCheckedChangeListener((cb, b) ->
                projectsRecyclerViewAdapter.setShowType(typeChip.isChecked()));

        Chip yearChip = findViewById(R.id.typeChip);
        yearChip.setOnCheckedChangeListener((cb, b) ->
                projectsRecyclerViewAdapter.setShowYear(yearChip.isChecked()));
    }

    private void setupRecyclerView() {
        projectsProgressBar = findViewById(R.id.projectsProgressBar);

        projectsRecyclerView = findViewById(R.id.projectsRecyclerView);
        projectsRecyclerViewAdapter = new ProjectsRecyclerViewAdapter(new ArrayList<>(), project -> rowClick(project));
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectsRecyclerView.setAdapter(projectsRecyclerViewAdapter);
    }

    private void displayProjects() {

        projectsRecyclerViewAdapter
                .setProjectList(projects.stream()
                        .filter(project -> !(project.isLocked() && filterLocked))
                        .filter(project -> project.getName().contains(searchText) | project.getType().contains(searchText)
                        | project.getLeader().contains(searchText) | project.getYear().contains(searchText))
                        .collect(Collectors.toList()));
        projectsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getRepository() {
        DataManager.getRepositoryProjects((repositoryName, projectsList, exception) -> {

            runOnUiThread(() -> projectsProgressBar.setVisibility(View.GONE));


            if (exception != null) {
                //TODO: Handle error
                Toast.makeText(this, "There was a problem with fetching the projects.", Toast.LENGTH_SHORT).show();
                return;
            }


            if (projectsList.isEmpty()) {
                showEmptyPopup();
                return;
            }

            projects = projectsList;

            runOnUiThread(() -> {
                displayProjects();

                setTitleToRepoName(repositoryName);

            });
        });
    }
    private void setTitleToRepoName(String name) {
        setTitle(name);
    }

    private void showEmptyPopup() {
        Snackbar.make(findViewById(R.id.projectsActivityLayout), "The repository has no projects.", Snackbar.LENGTH_INDEFINITE).show();
    }

    private void rowClick(Project project) {
        Intent intent = new Intent(ProjectsActivity.this, ProjectDetailsActivity.class);
        intent.putExtra("project_id", project.getId());
        intent.putExtra("project_name", project.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bar_menu_projects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle_locked_menu_item:
                item.setChecked(!item.isChecked());
                filterLocked = item.isChecked();
                displayProjects();
                break;
            case R.id.logout_menu_item:
                doLogout();
                break;
            default:
                break;
        }
        return true;
    }

    private void doLogout(){
        clearDataForLogout();
        finish();
    }

    private void clearDataForLogout(){
        NetworkManager.clearData();
        DataManager.clearData();
    }


    @Override
    public void onBackPressed() {
        clearDataForLogout();
        super.onBackPressed();
    }
}