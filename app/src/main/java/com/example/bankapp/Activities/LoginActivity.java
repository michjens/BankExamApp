package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bankapp.Helpers.CurrentUser;
import com.example.bankapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.example.bankapp.Helpers.Utility.*;



public class LoginActivity extends AppCompatActivity {

    EditText userMail, userPassword, nemIDpassword;
    TextView nemID;
    Button btnLogin, forgotPass, btnReg, btnConfirm;
    HashMap userDetails;
    private ProgressBar loginProg;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        loginProg.setVisibility(View.INVISIBLE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProg.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage(getApplicationContext(), "Please verify all fields");
                } else {
                    buildAlertDialog(mail, password);

            }}
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMail.getText().toString().isEmpty()) {
                    showMessage(getApplicationContext(), "Enter Email");
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(userMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showMessage(getApplicationContext(), "Email sent");
                            }
                        }
                    });
                }
            }
        });

    }

    private void buildAlertDialog(final String mail, final String password) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
        nemIDpassword = mView.findViewById(R.id.nemIDpassword);
        nemID = mView.findViewById(R.id.nemID);
        btnConfirm = mView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nemIDpassword.getText().toString().isEmpty()) {
                    showMessage(getApplicationContext(), "Please verify all fields");

                }else{
                    login(mail, password);
                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    private void login(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProg.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Users/" + mAuth.getCurrentUser().getDisplayName());
                    mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                if ((long) documentSnapshot.get(nemID.getText().toString()) == Long.parseLong(nemIDpassword.getText().toString())) {
                                    Intent successfulLogin = new Intent(getApplicationContext(), HomeActivity.class);

                                    CurrentUser currentUser = new CurrentUser(documentSnapshot.getString("name"),
                                            documentSnapshot.getString("age"),
                                            documentSnapshot.getString("Zipcode"),
                                            documentSnapshot.getString("mail"),
                                            documentSnapshot.getString("Budget"),
                                            documentSnapshot.getString("Default"),
                                            documentSnapshot.getString("Business"),
                                            documentSnapshot.getString("Savings"),
                                            documentSnapshot.getString("Pension"),
                                            documentSnapshot.get("5632").toString());


                                    successfulLogin.putExtra("currentUser", currentUser);
                                    startActivity(successfulLogin);
                                } else {
                                    showMessage(getApplicationContext(), "Verify NemID key");
                                }

                            }

                        }
                    });

                } else {
                    showMessage(getApplicationContext(), "Wrong Email, Password or NemID key");
                    loginProg.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void init() {
        userDetails = new HashMap<String, String>();
        userMail = findViewById(R.id.logMail);
        userPassword = findViewById(R.id.logPass);
        btnLogin = findViewById(R.id.logBtn);
        loginProg = findViewById(R.id.loginProg);
        mAuth = FirebaseAuth.getInstance();
        forgotPass = findViewById(R.id.loginForgot);
        btnReg = findViewById(R.id.btnReg);


    }
}
