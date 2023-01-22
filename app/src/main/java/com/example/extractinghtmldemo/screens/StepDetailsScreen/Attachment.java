package com.example.extractinghtmldemo.screens.StepDetailsScreen;

import com.example.extractinghtmldemo.enums.AttachmentType;

public class Attachment {

    private String attachmentName;
    private String attachmentUrlLink;
    private AttachmentType attachmentType;

    public Attachment(){

    }

    public Attachment(String attachmentName, String attachmentUrlLink, AttachmentType attachmentType) {
        this.attachmentName = attachmentName;
        this.attachmentUrlLink = attachmentUrlLink;
        this.attachmentType = attachmentType;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentUrlLink() {
        return attachmentUrlLink;
    }

    public void setAttachmentUrlLink(String attachmentUrlLink) {
        this.attachmentUrlLink = attachmentUrlLink;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }
}
