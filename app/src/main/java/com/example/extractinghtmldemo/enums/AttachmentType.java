package com.example.extractinghtmldemo.enums;

import com.example.extractinghtmldemo.R;

public enum AttachmentType {

    DOCX,
    PDF,
    XLSX,
    UNKNOWN;

    public int getDrawableIdForStatus(){
        switch (this) {
            case DOCX:
                return R.drawable.docx_icon;
            case PDF:
                return R.drawable.pdf_icon;
            case XLSX:
                return R.drawable.xlsx_icon;
        }
        return R.drawable.default_file_icon;
    }
}
