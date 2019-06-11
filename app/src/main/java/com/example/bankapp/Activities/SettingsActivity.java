package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankapp.Helpers.CurrentUser;
import com.example.bankapp.Helpers.Utility;
import com.example.bankapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;

    ImageView businessCheck, savingsCheck, pensionCheck;
    TextView businessApply, savingsApply, pensionApply;
    Button userSettings;
    CurrentUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");
        loadBottomNav();
        setVisibility(currentUser);
        setOnClicks();


    }





    private void setOnClicks() {
        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userSettings = new Intent(getApplicationContext(), ProfileActivity.class);
                userSettings.putExtra("currentUser", currentUser);
                startActivity(userSettings);
            }
        });

        businessCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForAccount("Business");
            }
        });

        savingsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForAccount("Savings");
            }
        });

        pensionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForAccount("Pension");
            }
        });

    }

    private void setVisibility(CurrentUser currentUser) {
        if(currentUser.getmBusiness().equals("true")){
            businessApply.setVisibility(View.INVISIBLE);
            businessCheck.setVisibility(View.INVISIBLE);
        }
        if(currentUser.getmSavings().equals("true")){
            savingsApply.setVisibility(View.INVISIBLE);
            savingsCheck.setVisibility(View.INVISIBLE);
        }
        if(currentUser.getmPension().equals("true")){
            pensionApply.setVisibility(View.INVISIBLE);
            pensionCheck.setVisibility(View.INVISIBLE);
        }

    }

    private void loadBottomNav() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListenerBottom
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_transfer:
                        Utility.updateUI(SettingsActivity.this, TransferActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_bills:
                        Utility.updateUI(SettingsActivity.this, BillsActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_home:
                        Utility.updateUI(SettingsActivity.this, HomeActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_menu:
                        Utility.updateUI(SettingsActivity.this, SettingsActivity.class, currentUser);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerBottom);

    }

    private void applyForAccount(final String accountType) {
        final DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName());
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if (accountType.equals("Business")){
                        currentUser.setmBusiness("true");
                    }else if(accountType.equals("Savings")) {
                        currentUser.setmSavings("true");
                    }else if(accountType.equals("Pension")){
                        currentUser.setmPension("true");
                    }

                    Map<String, Object> applyForAccount = new HashMap<>();
                    applyForAccount.put(accountType, "true");
                    mDocRef.update(applyForAccount);
                    Utility.showMessage(getApplicationContext(), "Application send.");
                }
            }
        });
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.nav_view);
        businessCheck = findViewById(R.id.businessCheck);
        savingsCheck = findViewById(R.id.savingsCheck);
        pensionCheck = findViewById(R.id.pensionCheck);
        businessApply = findViewById(R.id.businessApply);
        savingsApply = findViewById(R.id.savingsApply);
        pensionApply = findViewById(R.id.pensionApply);
        userSettings = findViewById(R.id.userSettings);

        mAuth = FirebaseAuth.getInstance();


    }



}
