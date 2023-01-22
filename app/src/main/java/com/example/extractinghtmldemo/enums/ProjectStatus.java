package com.example.extractinghtmldemo.enums;

import com.example.extractinghtmldemo.R;

public enum ProjectStatus {

    IN_PROCESS("In_Process"),
    COMPLETED("Completed"),
    REVIEWED("Reviewed"),
    EDITED_AFTER_REVIEW("Edited_After_Review"),
    LOCKED("Locked");

    public final String label;
    ProjectStatus(String label){
        this.label = label;
    }

    public int getDrawableIdForStatus(){
        switch (this){
            case IN_PROCESS:
                return R.drawable.in_process_icon;

            case COMPLETED:
                return R.drawable.completed;

            case REVIEWED:
                return R.drawable.reviewed;

            case EDITED_AFTER_REVIEW:
                return R.drawable.edited_after_review;

            case LOCKED:
                return R.drawable.locked;
        }
        return 0;
    }

}
