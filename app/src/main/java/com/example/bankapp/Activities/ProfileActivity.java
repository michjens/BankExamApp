package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.bankapp.Helpers.CurrentUser;
import com.example.bankapp.Helpers.Utility;
import com.example.bankapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    CurrentUser currentUser;
    EditText name, mail, address, age, pass1, pass2;
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        loadBottomNav();
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");

        loadData();


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editData();
            }
        });
    }


    private void loadData() {
        name.setText(currentUser.getmName());
        mail.setText(currentUser.getmMail());
        address.setText(currentUser.getmZipcode());
        age.setText(currentUser.getmAge());


    }



    private void editData(){
        final DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName());
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String newName = name.getText().toString();
                    String newMail = mail.getText().toString();
                    String newAddress = address.getText().toString();
                    String newAge = age.getText().toString();
                    Map<String, Object> userEdits = new HashMap<>();

                    userEdits.put("name", newName);
                    userEdits.put("mail", newMail);
                    userEdits.put("age", newAge);
                    userEdits.put("Zipcode", newAddress);
                    currentUser.setmName(newName);
                    currentUser.setmMail(newMail);
                    currentUser.setmZipcode(newAddress);
                    currentUser.setmAge(newAge);
                    mDocRef.update(userEdits).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utility.showMessage(getApplicationContext(), "User details successfully updated");
                        }
                    });
                }
            }
        });
    }

    private void loadBottomNav() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListenerBottom
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_transfer:
                        Utility.updateUI(ProfileActivity.this, TransferActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_bills:
                        Utility.updateUI(ProfileActivity.this, BillsActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_home:
                        Utility.updateUI(ProfileActivity.this, HomeActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_menu:
                        Utility.updateUI(ProfileActivity.this, SettingsActivity.class, currentUser);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerBottom);
    }


    private void init(){
        bottomNavigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.editName);
        mail = findViewById(R.id.editMail);
        address = findViewById(R.id.editAddress);
        age = findViewById(R.id.editAge);
        editBtn = findViewById(R.id.editBtn);


    }
}
