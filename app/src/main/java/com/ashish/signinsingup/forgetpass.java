package com.ashish.signinsingup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import kotlinx.coroutines.scheduling.Task;

public class forgetpass extends AppCompatActivity {
    private Button forgetBtn;
    private EditText txtEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgetpass);

        auth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.forEmail);
        forgetBtn = findViewById(R.id.forgetBtn);

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserData();
            }
        });

    }

    private void validateUserData() {
        String email = txtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            txtEmail.setError("Email is required");

        } else {
            forgetPass(email);

        }
    }

    private void forgetPass(String email) {

        auth.sendPasswordResetEmaiil(email)
                .addOncompleteListerner(new View.OnClickListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(forgetpass.this, "Check your email", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(forgetpass.this, Login.class));
                                                    finish();
                                                } else {
                                                    String errorMessage = task.getExcption().getMessage();
                                                    Toast.makeText(forgetpass.this, "Error:" + errorMessage, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                );
    }
}