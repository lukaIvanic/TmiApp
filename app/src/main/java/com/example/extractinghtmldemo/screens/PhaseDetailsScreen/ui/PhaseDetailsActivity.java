package com.example.extractinghtmldemo.screens.PhaseDetailsScreen.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.data.DataManager;
import com.example.extractinghtmldemo.networking.NetworkManager;
import com.example.extractinghtmldemo.screens.LoginScreen.ui.LoginActivity;
import com.example.extractinghtmldemo.screens.PhaseDetailsScreen.Phase;
import com.example.extractinghtmldemo.screens.ProjectDetailsScreen.ui.ReferencesRecyclerViewAdapter;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.Step;
import com.example.extractinghtmldemo.screens.StepDetailsScreen.ui.StepDetailsActivity;
import com.example.extractinghtmldemo.screens.ui_utils.TmiLinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.pdfview.PDFView;

public class PhaseDetailsActivity extends AppCompatActivity {

    ConstraintLayout phaseDetailsLayout;

    Phase phaseMinimal;
    TextView phaseNameTextView;

    LinearLayout pdfLayout;
    CardView pdfCardView;
    PDFView pdfView;

    ProgressBar phaseDetailsProgressBar;

    RecyclerView resourcesRecyclerView;
    StepsRecyclerViewAdapter stepsRecyclerViewAdapter;

    TextView referencesTitleTextView;
    RecyclerView phaseReferencesRecyclerView;
    ReferencesRecyclerViewAdapter phasesReferencesRecyclerViewAdapter;

    boolean isPdfEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_details);

        phaseDetailsLayout = findViewById(R.id.phaseDetailsLayout);


        phaseMinimal = new Phase(getPhaseNameFromIntent(), getPhaseIdFromIntent());

        setTitleToRepoName();

        phaseNameTextView = findViewById(R.id.phaseDetailsName);
        phaseNameTextView.setText(phaseMinimal.getPhaseName());

        phaseDetailsProgressBar = findViewById(R.id.phaseDetailsProgressBar);

        resourcesRecyclerView = findViewById(R.id.phaseResourcesList);
        stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(step -> {
            gotoStepDetailsScreen(step);
        });
        resourcesRecyclerView.setLayoutManager(new TmiLinearLayoutManager(this, false));
        resourcesRecyclerView.setAdapter(stepsRecyclerViewAdapter);

        pdfLayout = findViewById(R.id.phaseDetailsPdfLayout);
        pdfCardView = findViewById(R.id.phaseDetailsPdfCardView);
        pdfView = findViewById(R.id.phaseDetailsPdfView);

        referencesTitleTextView = findViewById(R.id.phaseDetailsReferencesTitle);
        phaseReferencesRecyclerView = findViewById(R.id.phaseReferencesRecyclerView);
        phasesReferencesRecyclerViewAdapter = new ReferencesRecyclerViewAdapter();
        phaseReferencesRecyclerView.setLayoutManager(new TmiLinearLayoutManager(this, false));
        phaseReferencesRecyclerView.setAdapter(phasesReferencesRecyclerViewAdapter);


        String firstPart = phaseMinimal.getPhaseId().split("/")[0];
        String secondPart = phaseMinimal.getPhaseId().split("/")[1];
        String localPath = firstPart + "/" + secondPart + "/" + secondPart + ".docx.pdf";

        DataManager.getPDF(localPath, false, this, (pdfFile, exception) -> {

            if (exception != null) {
                //TODO: handle error
                return;
            }

            isPdfEmpty = false;

            runOnUiThread(() -> {
//
//                try {
//                    // Check if PDF is empty by length of document
//                    if (pdfInputStream.available() < 1150) {
//                        pdfView.setVisibility(View.GONE);
//                        isPdfEmpty = true;
//                        return;
//                    }
//                } catch (Exception ignored) {
//                }

                pdfView.fromFile(pdfFile)
                        .show();

                revealPdfSection();

            });
        });


        DataManager.getPhaseDetails(phaseMinimal.getPhaseId(), phaseMinimal.getPhaseName(), (phase, exception) -> {

            if (exception != null) {
                //TODO: handle error
                return;
            }

            runOnUiThread(() -> {

                phaseDetailsProgressBar.setVisibility(View.GONE);

                stepsRecyclerViewAdapter.setStepsList(phase.getStepsList());
                stepsRecyclerViewAdapter.notifyDataSetChanged();


                if (!phase.getReferenceList().isEmpty()) {
                    showReferenceSection();
                    phasesReferencesRecyclerViewAdapter.setExternalReferences(phase.getReferenceList());
                    phasesReferencesRecyclerViewAdapter.notifyDataSetChanged();
                }

                if (!isPdfEmpty)
                    revealPdfSection();

                if(phase.getStepsList().isEmpty() && isPdfEmpty)
                    showEmptyPopup();

            });

        });

    }

    private void showEmptyPopup() {
        Snackbar.make(phaseDetailsLayout, "This phase is empty.", Snackbar.LENGTH_INDEFINITE).show();
    }


    private void gotoStepDetailsScreen(Step step) {
        Intent intent = new Intent(PhaseDetailsActivity.this, StepDetailsActivity.class);
        intent.putExtra("stepName", phaseMinimal.getPhaseName() + " > " + step.getStepName());
        intent.putExtra("stepId", step.getStepId());
        startActivity(intent);
    }

    private void setTitleToRepoName() {
        setTitle(DataManager.getRepositoryName());
    }

    private void showReferenceSection() {
        referencesTitleTextView.setVisibility(View.VISIBLE);
        phaseReferencesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void revealPdfSection() {
        pdfLayout.setVisibility(View.VISIBLE);
        pdfCardView.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.VISIBLE);
    }


    private String getPhaseIdFromIntent() {
        return getIntent().getExtras().getString("phase_id");
    }

    private String getPhaseNameFromIntent() {
        return getIntent().getExtras().getString("phase_name");
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
        Intent intent = new Intent(PhaseDetailsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}