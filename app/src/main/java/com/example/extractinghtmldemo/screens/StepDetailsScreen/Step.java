package com.example.extractinghtmldemo.screens.StepDetailsScreen;

import com.example.extractinghtmldemo.enums.ProjectStatus;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ExternalReference;

import java.util.List;

public class Step {
    private String stepName;
    private String stepId;
    private ProjectStatus status;
    private boolean hasAttachment;
    private List<Attachment> stepAttachmentList;
    private List<ExternalReference> externalReferencesList;

    public Step(){

    }

    public Step(String stepId, String stepName){
        this.stepId = stepId;
        this.stepName = stepName;
    }

    public Step(List<Attachment> stepAttachmentList, List<ExternalReference> externalReferencesList) {
        this.stepAttachmentList = stepAttachmentList;
        this.externalReferencesList = externalReferencesList;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public boolean hasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public List<Attachment> getStepAttachmentList() {
        return stepAttachmentList;
    }

    public void setStepAttachmentList(List<Attachment> stepAttachmentList) {
        this.stepAttachmentList = stepAttachmentList;
    }

    public List<ExternalReference> getExternalReferencesList() {
        return externalReferencesList;
    }

    public void setExternalReferencesList(List<ExternalReference> externalReferencesList) {
        this.externalReferencesList = externalReferencesList;
    }
}
