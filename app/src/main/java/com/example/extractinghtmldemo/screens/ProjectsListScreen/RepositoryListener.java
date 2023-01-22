package com.example.extractinghtmldemo.screens.ProjectsListScreen;

import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.Project;

import java.util.List;


public interface RepositoryListener {

    void getProjects(String repositoryName, List<Project> projectsList, Exception exception);

}
