package com.example.extractinghtmldemo.data;

import static com.example.extractinghtmldemo.Utils.TmiFileUtils.*;
import static com.example.extractinghtmldemo.networking.NetworkManager.requestPhaseDetailsAsyncTask.*;
import static com.example.extractinghtmldemo.networking.NetworkManager.requestProjectDetailsAsyncTask.*;

import android.content.Context;
import android.os.FileUtils;

import com.example.extractinghtmldemo.Utils.TmiFileUtils;
import com.example.extractinghtmldemo.screens.LoginScreen.LoginListener;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.PdfFileListener;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.PhaseDetailsListener;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ExternalReference;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ProjectDetailsListener;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.Project;
import com.example.extractinghtmldemo.screens.ProjectsListScreen.ProjectMinimal;
import com.example.extractinghtmldemo.screens.ProjectsListScreen.RepositoryListener;
import com.example.extractinghtmldemo.Utils.RegexUtils;
import com.example.extractinghtmldemo.networking.NetworkManager;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Step;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.StepDetailsListener;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static String repositoryName = "Repository";
    private static String cachedRepositoryScreenHtml;

    public static void clearData() {
        repositoryName = "Repository";
        cachedRepositoryScreenHtml = null;
    }

    public static String getRepositoryName() {
        return repositoryName;
    }

    public static void attemptLogin(String username, String password, LoginListener loginListener) {
        try {
            new NetworkManager.requestProjectsAsyncTask(username, password).execute((html, exception) -> {
                if (exception != null) {
                    loginListener.loginRequest(false, exception);
                    return;
                }

                if (html.contains("dologin.html")) {
                    loginListener.loginRequest(false, null);
                } else {
                    loginListener.loginRequest(true, null);
                    cachedRepositoryScreenHtml = html;
                }

            });
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            loginListener.loginRequest(false, unsupportedEncodingException);
        }
    }

    public static void getRepositoryProjects(RepositoryListener repositoryListener) {
        new NetworkManager.requestProjectsAsyncTask(cachedRepositoryScreenHtml).execute((html, exception) -> {
            if (exception != null) {
                repositoryListener.getProjects(null, null, exception);
                return;
            }


            List<Project> projects = RegexUtils.getProjects(html);
            repositoryName = RegexUtils.getRepositoryName(html);

            repositoryListener.getProjects(repositoryName, projects, null);
        });

    }

    public static void getProjectDetails(ProjectMinimal projectMinimal, ProjectDetailsListener projectDetailsListener) {
        new NetworkManager.requestProjectDetailsAsyncTask()
                .execute(new RequestProjectDetailsParams(
                        projectMinimal.getId(),
                        (html, exception) -> {
                            if (exception != null) {
                                projectDetailsListener.getProjectDetails(null, exception);
                                return;
                            }

                            Project project = RegexUtils.getProjectDetails(html);
                            project.setId(projectMinimal.getId());
                            project.setName(projectMinimal.getName());

                            projectDetailsListener.getProjectDetails(project, null);

                        }));
    }

    public static void getPhaseDetails(String phaseId, String phaseName, PhaseDetailsListener phaseDetailsListener) {
        new NetworkManager
                .requestPhaseDetailsAsyncTask()
                .execute(new RequestPhaseDetailsParams(
                        phaseId,
                        (html, exception) -> {
                            if (exception != null) {
                                phaseDetailsListener.getPhaseDetails(null, exception);
                                return;
                            }

                            Phase phase = RegexUtils.getPhaseDetails(html);
                            phase.setPhaseId(phaseId);
                            phase.setPhaseName(phaseName);

                            phaseDetailsListener.getPhaseDetails(phase, null);

                        }));
    }

    public static void getStepDetails(String stepId, StepDetailsListener stepDetailsListener) {
        new NetworkManager
                .requestStepDetailsAsyncTask()
                .execute(new NetworkManager.requestStepDetailsAsyncTask.RequestStepDetailsParams(stepId, (html, exception) -> {

                    if (exception != null) {
                        stepDetailsListener.getStepDetails(null, exception);
                    }


                    Step step = RegexUtils.getStepDetails(html);
                    stepDetailsListener.getStepDetails(step, null);

                }));
    }

    public static void getPDF(String localPath, boolean isDownload, Context context, PdfFileListener pdfFileListener) {


        new NetworkManager
                .requestPDFAsyncTask(localPath)
                .execute((pdfInputStream, exception) -> {
                    if (exception != null) {
                        pdfFileListener.getPdfFile(null, exception);
                        return;
                    }


                    File tempFile;

                    if(isDownload){
                        tempFile = new File(context.getCacheDir(), TmiFileUtils.removeForbiddenCharacters(localPath));
                    } else{
                        tempFile = new File(context.getCacheDir(), "temp.pdf");
                    }

                    if (copyInputStreamToFile(pdfInputStream, tempFile, isDownload)) {
                        pdfFileListener.getPdfFile(tempFile, null);
                    } else {
                        pdfFileListener.getPdfFile(null, new Exception("Pdf not available."));
                    }


                });
    }


}
