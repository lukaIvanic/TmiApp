package com.example.extractinghtmldemo.data;

import java.io.InputStream;

public interface TmiPdfResponseListener {
    void tmiPdfRequest(InputStream pdfInputStream, Exception exception);

}
