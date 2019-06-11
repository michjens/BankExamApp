package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.bankapp.Helpers.CurrentUser;
import com.example.bankapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.bankapp.Helpers.Utility.*;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userPassword2, userName, userAge, userZipcode;
    private ProgressBar loadingProg;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    CurrentUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        loadingProg.setVisibility(View.INVISIBLE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerBtn.setVisibility(View.INVISIBLE);
                loadingProg.setVisibility(View.VISIBLE);
                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String age = userAge.getText().toString();
                final String zipcode = userZipcode.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();
                DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Users/" + name);


                if(name.isEmpty() || email.isEmpty() || password.isEmpty()|| password2.isEmpty() || !password.equals(password2)){
                    showMessage(getApplicationContext(),"Please verify all fields");
                    registerBtn.setVisibility(View.VISIBLE);
                    loadingProg.setVisibility(View.INVISIBLE);
                }else{
                    currentUser.setmName(name);
                    currentUser.setmMail(email);
                    currentUser.setmAge(age);
                    currentUser.setmZipcode(zipcode);
                    currentUser.setmBudget("true");
                    currentUser.setmDefault("true");
                    currentUser.setmBusiness("false");
                    currentUser.setmSavings("false");
                    currentUser.setmPension("false");
                    createUserAcc(email, name, password);
                    Map<String, Object> userSave = new HashMap<>();
                    userSave.put("name", name);
                    userSave.put("email", email);
                    userSave.put("age", age);
                    userSave.put("Zipcode", zipcode);
                    userSave.put("Budget", "true");
                    userSave.put("Default", "true");
                    userSave.put("Business", "false");
                    userSave.put("Savings", "false");
                    userSave.put("Pension", "false");

                    mDocRef.set(userSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMessage(getApplicationContext(), "Data has been saved");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage(getApplicationContext(), "Data was not saved");
                        }
                    });
                }

            }
        });


    }

    private void createUserAcc(String email, final String name, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    showMessage(getApplicationContext(),"User registration complete");
                    updateInfo(name, mAuth.getCurrentUser());


                }else{
                    showMessage(getApplicationContext(),"User registration failed " + "\n" + task.getException().getMessage());
                    registerBtn.setVisibility(View.VISIBLE);
                    loadingProg.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateInfo(String name, final FirebaseUser userReg) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        userReg.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    showMessage(getApplicationContext(), "User registration complete");
                    updateUI(RegisterActivity.this, HomeActivity.class, currentUser);



                }
            }
        });

    }


    private void init() {
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPass);
        userPassword2 = findViewById(R.id.regPass2);
        userName = findViewById(R.id.regName);
        userAge = findViewById(R.id.regAge);
        userZipcode = findViewById(R.id.regZipcode);
        loadingProg = findViewById(R.id.progressBar);
        registerBtn = findViewById(R.id.regBtn);
        mAuth = FirebaseAuth.getInstance();



    }
}
