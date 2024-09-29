package com.ashish.signinsingup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView openReg,openForgetPass;
    private EditText logEmail, logPassword;
    protected FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        openReg = findViewById(R.id.openReg);
        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPass);
        Button loginBtn = findViewById(R.id.openForgetPass);

        auth = FirebaseAuth.getInstance();

        openReg.setOnClickListener(v -> openRegisterActivity());

        loginBtn.setOnClickListener(v -> startActivity(new Intent(Login.this, forgetpass.class)));

        if (auth.getCurrentUser() != null) {
            openMainActivity();
        }
    }
    private void validdateUserData(){
        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please provoide all fieldes", Toast.LENGTH_SHORT).show();

        } else {
            loginUser(email,password);
        }
    }
    private  void loginUser(String email,String password ){
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null){
                            if(user.isEmailVerified()){
                                openMainActivity();
                            }else {
                                Toast.makeText(this, "Email is not verified. Please verify your email", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail(user);
                            }

                        }
                    }else {
                        Toast.makeText(this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->{
                    Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        }
        private void sendVerificationEmail(FirebaseUser user ){
        user.sendEmailVerification()
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Verificaton email sent.Please check your inbox", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Faild to send verificaton email. ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void openMainActivity(){
        startActivity(new Intent(Login.this , MainActivity.class));
        finish();
    }
    private void openRegisterActivity(){
        startActivity(new Intent(Login.this,register.class));
        finish();
    }
}