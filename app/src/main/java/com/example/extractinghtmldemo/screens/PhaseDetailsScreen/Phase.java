package com.example.extractinghtmldemo.screens.PhaseDetailsScreen;

import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ExternalReference;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Step;

import java.util.ArrayList;
import java.util.List;

public class Phase {
    private String phaseId;
    private String phaseName;
    private boolean hasAttachment;
    private List<Step> stepsList = new ArrayList<>();
    private List<ExternalReference> referenceList = new ArrayList<>();

    public Phase(){

    }
    public Phase(String phaseName, String phaseId) {
        this.phaseName = phaseName;
        this.phaseId = phaseId;
    }

    public Phase(String phaseName, String phaseId, List<Step> stepsList, List<ExternalReference> referenceList) {
        this.phaseName = phaseName;
        this.phaseId = phaseId;
        this.stepsList = stepsList;
        this.referenceList = referenceList;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public boolean hasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public List<Step> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Step> stepsList) {
        this.stepsList = stepsList;
    }

    public List<ExternalReference> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<ExternalReference> referenceList) {
        this.referenceList = referenceList;
    }
}
