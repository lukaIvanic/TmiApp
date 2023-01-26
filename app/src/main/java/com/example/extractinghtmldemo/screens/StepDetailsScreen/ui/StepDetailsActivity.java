package com.example.extractinghtmldemo.screens.StepDetailsScreen.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.data.DataManager;
import com.example.extractinghtmldemo.networking.NetworkManager;
import com.example.extractinghtmldemo.screens.LoginScreen.ui.LoginActivity;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui.ReferencesRecyclerViewAdapter;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Attachment;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Step;
import com.example.extractinghtmldemo.screens.ui_utils.TmiLinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.pdfview.PDFView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StepDetailsActivity extends AppCompatActivity {


    ConstraintLayout stepDetailsLayout;
    Step step;
    TextView stepNameTextView;

    LinearLayout pdfLayout;
    CardView pdfCardView;
    PDFView stepDetailsPdfView;

    ProgressBar stepDetailsProgressBar;

    TextView attachmentTitleTextView;
    RecyclerView attachmentsRecyclerView;
    AttachmentsRecyclerViewAdapter attachmentsAdapter;

    TextView referencesTitleTextView;
    RecyclerView stepReferencesRecyclerView;
    ReferencesRecyclerViewAdapter stepReferencesAdapter;

    boolean isPdfEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        step = new Step(getStepIdFromIntent(), getStepNameFromIntent());

        setupUI();

        setupData();

    }

    private void setupUI() {

        setTitleToRepoName();

        stepDetailsLayout = findViewById(R.id.stepDetailsLayout);

        stepNameTextView = findViewById(R.id.stepDetailsName);
        stepNameTextView.setText(step.getStepName());

        stepDetailsProgressBar = findViewById(R.id.stepDetailsProgressBar);

        attachmentTitleTextView = findViewById(R.id.stepDetailsAttachmentTitle);

        attachmentsRecyclerView = findViewById(R.id.stepAttachmentRecyclerView);
        attachmentsAdapter = new AttachmentsRecyclerViewAdapter(new AttachmentsRecyclerViewAdapter.RowClickListener() {
            @Override
            public void onRowClick(Attachment attachment) {
                openAttachmentFileInExternalApp(attachment);
            }

            @Override
            public void onDownloadClick(Attachment attachment) {

                downloadAttachment(attachment);
            }
        });
        attachmentsRecyclerView.setAdapter(attachmentsAdapter);
        attachmentsRecyclerView.setLayoutManager(new TmiLinearLayoutManager(this, false));

        referencesTitleTextView = findViewById(R.id.stepDetailsReferencesTitle);

        stepReferencesRecyclerView = findViewById(R.id.stepReferencesRecyclerView);
        stepReferencesAdapter = new ReferencesRecyclerViewAdapter();
        stepReferencesRecyclerView.setAdapter(stepReferencesAdapter);
        stepReferencesRecyclerView.setLayoutManager(new TmiLinearLayoutManager(this, false));


        pdfLayout = findViewById(R.id.stepDetailsPdfLayout);
        pdfCardView = findViewById(R.id.stepDetailsPdfCardView);
        stepDetailsPdfView = findViewById(R.id.stepDetailsPdfView);

    }

    private void setTitleToRepoName() {
        setTitle(DataManager.getRepositoryName());
    }

    private void setupData() {
        getStepDetails();
        getPDF();
    }


    private void getStepDetails() {
        DataManager.getStepDetails(step.getStepId(), (step, exception) -> {
            if (exception != null) {
                //TODO: handle error
                runOnUiThread(() -> Toast.makeText(StepDetailsActivity.this, "Exception with getting step details", Toast.LENGTH_SHORT).show());
            }
            runOnUiThread(() -> {
                stepDetailsProgressBar.setVisibility(View.GONE);

                if (step.getStepAttachmentList().size() > 0) {
                    revealAttachmentsSection();
                    attachmentsAdapter.setAttachmentList(step.getStepAttachmentList());
                    attachmentsAdapter.notifyDataSetChanged();
                }

                if (step.getExternalReferencesList().size() > 0) {
                    revealExternalReferencesSection();
                    stepReferencesAdapter.setExternalReferences(step.getExternalReferencesList());
                    stepReferencesAdapter.notifyDataSetChanged();
                }

                if (!isPdfEmpty)
                    revealPdfSection();

                if (step.getStepAttachmentList().isEmpty() && isPdfEmpty)
                    showEmptyPopup();


            });

        });
    }

    private void getPDF() {

        String firstPart = step.getStepId().split("/")[0];
        String secondPart = step.getStepId().split("/")[1];
        String thirdPart = step.getStepId().split("/")[2];
        String localPath = firstPart + "/" + secondPart + "/" + thirdPart + "/" + thirdPart + ".docx.pdf";

        DataManager.getPDF(localPath, false, this, (pdfFile, exception) -> {
            if (exception != null) {
                //TODO: handle error
                return;
            }

            isPdfEmpty = false;

            runOnUiThread(() -> {

                stepDetailsPdfView
                        .fromFile(pdfFile)
                        .show();

                revealPdfSection();
            });
        });
    }

    private void openAttachmentFileInExternalApp(Attachment attachment) {

        DataManager.getPDF(attachment.getAttachmentUrlLink().substring(39), true, this, (pdfFile, exception) -> {

            if (exception != null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });

                //TODO handle exception
                return;
            }

            isPdfEmpty = false;

            runOnUiThread(() -> {
                        openFileInExternalApp(pdfFile);
                        revealPdfSection();
                    }
            );

        });

    }


    private void openFileInExternalApp(File file) {

        String type = "*/*";

        if (file.getName().endsWith(".pdf")) {
            type = "application/pdf";
        } else if (file.getName().endsWith(".docx")) {
            type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (file.getName().endsWith(".doc")) {
            type = "application/msword";
        } else if (file.getName().endsWith(".xls")) {
            type = "application/vnd.ms-excel";
        } else if(file.getName().endsWith("xlsx")){
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }

        Uri uriPdfPath = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent fileOpenIntent = new Intent(Intent.ACTION_VIEW);
        fileOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fileOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
        fileOpenIntent.setDataAndType(uriPdfPath, type);
        fileOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivity(fileOpenIntent);
    }

    private String getAttachmentFileUrlPath(String attachmentName, boolean doEncoding) throws UnsupportedEncodingException {

        String firstPart = step.getStepId().split("/")[0];
        String secondPart = step.getStepId().split("/")[1];
        String thirdPart = step.getStepId().split("/")[2];
        String name = attachmentName;

        if (doEncoding) {
            name = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        }

        return firstPart + "/" + secondPart + "/" + thirdPart + "/" + name;
    }


    private void downloadAttachment(Attachment attachment) {

        if (attachment.getAttachmentUrlLink() == null) {
            Toast.makeText(this, "File name is not supported. ", Toast.LENGTH_SHORT).show();
            return;
        }

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(attachment.getAttachmentUrlLink());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.addRequestHeader("Cookie", NetworkManager.getCachedCookie());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        manager.enqueue(request);
    }


    private void showEmptyPopup() {
        Snackbar.make(stepDetailsLayout, "This step is empty.", Snackbar.LENGTH_INDEFINITE).show();
    }

    private void revealAttachmentsSection() {
        attachmentTitleTextView.setVisibility(View.VISIBLE);
        attachmentsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void revealExternalReferencesSection() {
        referencesTitleTextView.setVisibility(View.VISIBLE);
        stepReferencesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void revealPdfSection() {
        pdfLayout.setVisibility(View.VISIBLE);
        pdfCardView.setVisibility(View.VISIBLE);
        stepDetailsPdfView.setVisibility(View.VISIBLE);
    }

    private String getStepIdFromIntent() {
        return getIntent().getExtras().getString("stepId");
    }

    private String getStepNameFromIntent() {
        return getIntent().getExtras().getString("stepName");
    }


    private void clearDataForLogout() {
        NetworkManager.clearData();
        DataManager.clearData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bar_menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu_item:
                doLogout();

                break;
            default:
                break;
        }
        return true;
    }

    private void doLogout() {
        clearDataForLogout();
        Intent intent = new Intent(StepDetailsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}