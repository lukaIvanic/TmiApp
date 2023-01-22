package com.example.extractinghtmldemo.screens.LoginScreen.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.extractinghtmldemo.R;
import com.example.extractinghtmldemo.data.DataManager;
import com.example.extractinghtmldemo.screens.LoginScreen.LoginListener;
import com.example.extractinghtmldemo.screens.ProjectsListScreen.ui.ProjectsActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText usernameEditText;
    TextInputEditText passwordEditText;
    Button loginButton;
    Button dummyLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton = findViewById(R.id.loginButton);
        dummyLoginButton = findViewById(R.id.dummyLoginButton);

        loginButton.setOnClickListener(view -> login());

        dummyLoginButton.setOnClickListener(view -> {
            usernameEditText.setText("auditmanager@pRepo");
            passwordEditText.setText("Dummy1");
            login();
        });

    }

    private void login() {

        disableButtons();

        String username = Objects.requireNonNull(usernameEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        DataManager.attemptLogin(username, password, (success, exception) -> {
            if (exception != null) {
                //TODO: handle error
                runOnUiThread(() -> enableButton());
                return;
            }

            if (success) {
                gotoProjectsActivity();
            } else {
                runOnUiThread(() -> {
                    enableButton();
                    Toast.makeText(this, "Wrong credentials.", Toast.LENGTH_SHORT).show();
                });
            }

        });


    }

    private void gotoProjectsActivity() {
        Intent intent = new Intent(LoginActivity.this, ProjectsActivity.class);
        startActivity(intent);
    }

    private void disableButtons() {
        loginButton.setEnabled(false);
        dummyLoginButton.setEnabled(false);
    }

    private void enableButton() {
        loginButton.setEnabled(true);
        dummyLoginButton.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableButton();
    }

    @Override
    public void onBackPressed() {

    }
}