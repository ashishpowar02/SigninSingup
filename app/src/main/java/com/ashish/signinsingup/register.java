package com.ashish.signinsingup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class register extends AppCompatActivity {

    private EditText regName,regEmail,regPassword;
    private Button register;
    private TextView openLog;
    private String name,email, pass;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.logEmail);
        regPassword = findViewById(R.id.regPass);
        register =findViewById(R.id.register);
        openLog = findViewById(R.id.openReg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        openLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });
    }
    private void openLogin(){
        startActivity(new Intent(register.this,Login.class));
        finish();
    }
    @Override
    protected void onStart(){
        if (auth.getCurrentUser() !=null){
            openMainActivity();
        }
    }
    private void openMainActivity(){
        startActivity(new Intent(this,MainActivity.class));
    finish();
    }
    private void validateData(){
        name =regName.getText().toString();
        email = regEmail.getText().toString();
        pass =regPassword.getText().toString();

        if (name.isEmpty()){
            regName.setError("Required Name");
            regName.requestFocus();
        }else if (email.isEmpty()){
            regEmail.setError("Required Email");
            regEmail.requestFocus();
        }else {
            createUser();
        }
    }
    private void createUser(){
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new View.OnClickListener<AuthResult>(){
            @Override
                    public void onComplete(@NonNull Task<AuthResult>task){
                if (task.isSuccessful()){
                    uploadUserData();
                }else {
                    Toast.makeText(register.this, "Error:"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new View.OnClickListener(){
            @Override
                    public void onFailure(@NonNull Exception e){
                Toast.makeText(register.this, "Error:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadUserData(){
        databaseReference = reference.child("users");
        String key = databaseReference.push().getKey();

        HashMap<String,String>user = new HashMap<>();
        user.put("key",key);
        user.put("name",name);
        user.put("email",email);
        user.put("status","no");

        databaseReference.child(key).setValue(user)
                .addOnCompleteListener(new View.OnClickListener<Void>(){
            @Override
                    public void onCompler(@NonNull Task<Void> task){
                if (task.isSuccessful()){
                    Toast.makeText(register.this, "User c reated", Toast.LENGTH_SHORT).show();
                openMainActivity();
                }else {
                    Toast.makeText(register.this, (CharSequence) task.getExcepation(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new View.OnClickListener(){
            @Override
            public void onFailure(@NonNull Exception e){
                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
                });
    }
}