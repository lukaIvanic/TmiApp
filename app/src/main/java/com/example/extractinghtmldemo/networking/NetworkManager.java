package com.example.extractinghtmldemo.networking;

import static com.example.extractinghtmldemo.networking.NetworkManager.requestPhaseDetailsAsyncTask.*;
import static com.example.extractinghtmldemo.networking.NetworkManager.requestProjectDetailsAsyncTask.*;
import static com.example.extractinghtmldemo.networking.NetworkManager.requestStepDetailsAsyncTask.*;

import android.os.AsyncTask;
import android.util.Log;

import com.example.extractinghtmldemo.data.TmiPdfResponseListener;
import com.example.extractinghtmldemo.data.TmiHttpResponseListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkManager {
    private static String cachedCookie = "session=Tickmark+Info-user=<username>&Tickmark+Info-pw=<Password>";

    public static String getCachedCookie(){ return cachedCookie; }

    public static void clearData() {
        cachedCookie = "";
    }

    public static class requestProjectsAsyncTask extends AsyncTask<TmiHttpResponseListener, Void, Void> {
        private String cachedMainScreenHtml;
        private String username;
        private String password;

        public requestProjectsAsyncTask(String cachedMainScreenHtml){
            this.cachedMainScreenHtml = cachedMainScreenHtml;
        }
        public requestProjectsAsyncTask(String user, String pass) throws UnsupportedEncodingException {
            this.username = user;
            this.password = pass;   
            cachedCookie = "session=Tickmark+Info-user=" + URLEncoder.encode(user, StandardCharsets.UTF_8.toString()) +"&Tickmark+Info-pw=" + URLEncoder.encode(pass, StandardCharsets.UTF_8.toString());
        }

        @Override
        protected Void doInBackground(TmiHttpResponseListener... tmiResponseListeners) {
            if(cachedMainScreenHtml != null){
                tmiResponseListeners[0].tmiHttpRequest(cachedMainScreenHtml, null);
                return null;
            }
            requestProjects(username, password, tmiResponseListeners[0]);
            return null;
        }
        private void requestProjects(String username, String password, TmiHttpResponseListener tmiResponseListener) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "httpd_username=" + username + "&httpd_password=" + password + "&login=Login");
            Request request = new Request.Builder()
                    .url("https://tmi.tickmark-software.com/dologin.html")
                    .method("POST", body)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Accept-Language", "en-US,en;q=0.6")
                    .addHeader("Cache-Control", "max-age=0")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Origin", "https://tmi.tickmark-software.com")
                    .addHeader("Referer", "https://tmi.tickmark-software.com/")
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("Sec-GPC", "1")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Brave\";v=\"108\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("Cookie", cachedCookie)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    tmiResponseListener.tmiHttpRequest(responseBody, null);
                    Log.i("response length", responseBody.length() + "");
                } else {
                    Log.i("respnse", "body is null.");
                }
                response.close();
            } catch (IOException e) {
                tmiResponseListener.tmiHttpRequest(null, e);
                e.printStackTrace();
            }


        }
    }

    public static class requestProjectDetailsAsyncTask extends AsyncTask<RequestProjectDetailsParams, Void, Void>{

        public static class RequestProjectDetailsParams {
            String projectId;
            TmiHttpResponseListener tmiResponseListener;


            public RequestProjectDetailsParams(String id, TmiHttpResponseListener tmiResponseListener) {
                this.projectId = id;
                this.tmiResponseListener = tmiResponseListener;
            }
        }

        @Override
        protected Void doInBackground(RequestProjectDetailsParams... requestProjectDetailsParams) {
            requestProjectDetails(requestProjectDetailsParams[0].projectId, requestProjectDetailsParams[0].tmiResponseListener);
            return null;
        }

        private void requestProjectDetails(String projectId, TmiHttpResponseListener tmiResponseListener){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://tmi.tickmark-software.com/tmi" + projectId)
                    .method("GET", null)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Accept-Language", "en-US,en;q=0.6")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Cookie", cachedCookie)
                    .addHeader("Referer", "https://tmi.tickmark-software.com/tmi/")
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("Sec-GPC", "1")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Brave\";v=\"108\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    tmiResponseListener.tmiHttpRequest(responseBody, null);
                    Log.i("response length", responseBody.length() + "");
                } else {
                    Log.i("respnse", "body is null.");
                }
                response.close();
            } catch (IOException e) {
                tmiResponseListener.tmiHttpRequest(null, e);
                e.printStackTrace();
            }
        }
    }

    public static class requestPhaseDetailsAsyncTask extends AsyncTask<RequestPhaseDetailsParams, Void, Void>{

        public static class RequestPhaseDetailsParams {
            String phaseId;
            TmiHttpResponseListener tmiResponseListener;


            public RequestPhaseDetailsParams(String phaseId, TmiHttpResponseListener tmiResponseListener) {
                this.phaseId = phaseId;
                this.tmiResponseListener = tmiResponseListener;
            }
        }

        @Override
        protected Void doInBackground(RequestPhaseDetailsParams... requestPhaseDetailsParams) {
            requestPhaseDetails(requestPhaseDetailsParams[0].phaseId, requestPhaseDetailsParams[0].tmiResponseListener);
            return null;
        }

        private void requestPhaseDetails(String phaseId, TmiHttpResponseListener tmiResponseListener){

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://tmi.tickmark-software.com/tmi/" + phaseId + "/")
                    .method("GET", null)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Accept-Language", "en-US,en;q=0.7")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Cookie", cachedCookie)
                    .addHeader("If-Modified-Since", "Tue, 28 Dec 2021 09:56:15 GMT")
                    .addHeader("If-None-Match", "\"b5a-5d431d4fa1534-gzip\"")
                    .addHeader("Referer", "https://tmi.tickmark-software.com/tmi/tlnpqhxu/")
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("Sec-GPC", "1")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Brave\";v=\"108\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    tmiResponseListener.tmiHttpRequest(responseBody, null);
                } else {
                    throw new Exception("Empty html body.");
                }
                response.close();
            } catch (Exception e) {
                tmiResponseListener.tmiHttpRequest(null, e);
                e.printStackTrace();
            }
        }
    }

    public static class requestStepDetailsAsyncTask extends AsyncTask<RequestStepDetailsParams, Void, Void> {


        public static class RequestStepDetailsParams {
            String stepId;
            TmiHttpResponseListener tmiResponseListener;


            public RequestStepDetailsParams(String stepId, TmiHttpResponseListener tmiResponseListener) {
                this.stepId = stepId;
                this.tmiResponseListener = tmiResponseListener;
            }
        }

        @Override
        protected Void doInBackground(RequestStepDetailsParams... requestStepDetailsParams) {
            requestPhaseDetails(requestStepDetailsParams[0].stepId, requestStepDetailsParams[0].tmiResponseListener);
            return null;
        }

        private void requestPhaseDetails(String stepId, TmiHttpResponseListener tmiResponseListener){

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("https://tmi.tickmark-software.com/tmi/" + stepId + "/")
                    .method("GET", null)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Accept-Language", "en-US,en;q=0.8")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Cookie", cachedCookie)
                    .addHeader("Pragma", "no-cache")
                    .addHeader("Referer", "https://tmi.tickmark-software.com/tmi/" + stepId.substring(0, stepId.length()-10))
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("Sec-GPC", "1")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua", "\"Not_A Brand\";v=\"99\", \"Brave\";v=\"109\", \"Chromium\";v=\"109\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    tmiResponseListener.tmiHttpRequest(responseBody, null);
                } else {
                    throw new Exception("Empty html body.");
                }
                response.close();
            } catch (Exception e) {
                tmiResponseListener.tmiHttpRequest(null, e);
                e.printStackTrace();
            }
        }

    }


    public static class requestPDFAsyncTask extends AsyncTask<TmiPdfResponseListener, Void, Void>{

        String localPath;
        public requestPDFAsyncTask(String localPath) {
            this.localPath = localPath;
        }

        @Override
        protected Void doInBackground(TmiPdfResponseListener... tmiPdfResponseListeners) {
            requestPDF(tmiPdfResponseListeners[0], localPath);
            return null;
        }

        private void requestPDF(TmiPdfResponseListener tmiPdfResponseListener, String localPath){
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            String url = "https://tmi.tickmark-software.com/tmi/" + localPath;

            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Accept-Language", "en-US,en;q=0.6")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Cookie", cachedCookie)
                    .addHeader("Pragma", "no-cache")
                    .addHeader("Sec-Fetch-Dest", "document")
                    .addHeader("Sec-Fetch-Mode", "navigate")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-User", "?1")
                    .addHeader("Sec-GPC", "1")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Brave\";v=\"108\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .build();


            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {

//                    InputStream pdfInputStream = response.body().byteStream();

                    ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);

                    tmiPdfResponseListener.tmiPdfRequest(responseBody.byteStream(), null);
                } else {
                    throw new Exception("Problem with getting InputStream from url.");
                }
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
                tmiPdfResponseListener.tmiPdfRequest(null, e);
            }

        }
    }



}
