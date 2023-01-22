package com.example.extractinghtmldemo.screens.ProjectDetailsScreen;

import com.example.extractinghtmldemo.enums.ProjectStatus;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String id;
    private String name;
    private String leader;
    private String year;
    private String type;

    private ProjectStatus projectStatus;
    private List<Phase> phasesList;
    private List<ExternalReference> referencesList;

    public Project(){

    }

    public Project(String name, String id) {
        this.name = name;
        this.id = id;
        this.phasesList = new ArrayList<>();
        this.referencesList = new ArrayList<>();
    }

    public Project(String name, String id, List<Phase> phasesList, List<ExternalReference> referencesList) {
        this.name = name;
        this.id = id;
        this.phasesList = phasesList;
        this.referencesList = referencesList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<Phase> getPhasesList() {
        return phasesList;
    }

    public void setPhasesList(List<Phase> phasesList) {
        this.phasesList = phasesList;
    }

    public List<ExternalReference> getReferencesList() {
        return referencesList;
    }

    public void setReferencesList(List<ExternalReference> referencesList) {
        this.referencesList = referencesList;
    }

    public boolean isLocked(){
        return projectStatus == ProjectStatus.LOCKED;
    }


}
