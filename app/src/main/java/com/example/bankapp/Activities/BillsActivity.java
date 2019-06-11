package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bankapp.Helpers.CurrentUser;
import com.example.bankapp.Helpers.Utility;
import com.example.bankapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class BillsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Spinner fromSpinner, whenSpinner;
    ArrayList listFrom, listWhen;
    FirebaseAuth mAuth;
    CurrentUser currentUser;
    EditText amountBills, regBill, accBill;
    Button billsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        init();
        addItemsOnSpinner();
        loadBottomNav();
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");




        billsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amountBills.getText().toString().isEmpty() || !regBill.getText().toString().isEmpty() || !accBill.getText().toString().isEmpty()){
                payBills();
                }
            }
        });

        whenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Utility.showMessage(getApplicationContext(), "Payment set to Now");
                        break;
                    case 1:
                        Utility.showMessage(getApplicationContext(), "Payment set to Monthly.");
                        break;
                    case 2:
                        Utility.showMessage(getApplicationContext(), "Payment set to Yearly.");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                        Utility.updateUI(BillsActivity.this, TransferActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_bills:
                        Utility.updateUI(BillsActivity.this, BillsActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_home:
                        Utility.updateUI(BillsActivity.this, HomeActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_menu:
                        Utility.updateUI(BillsActivity.this, SettingsActivity.class, currentUser);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerBottom);
    }

    private void payBills() {
        final DocumentReference mDocRefFrom = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName() + "/Accounts/" + fromSpinner.getSelectedItem().toString());
        mDocRefFrom.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Double oldBalance = documentSnapshot.getDouble("balance");
                    Double amountChange = Double.parseDouble(amountBills.getText().toString());
                    Double newBalance = oldBalance - amountChange;
                    Map<String, Object> balanceSave = new HashMap<>();
                    balanceSave.put("balance", newBalance);
                    balanceSave.put("trans1", -amountChange);

                    if(whenSpinner.getSelectedItem().toString().equalsIgnoreCase("Now")){
                        mDocRefFrom.update(balanceSave);
                        Utility.showMessage(getApplicationContext(), "Reg:" + regBill.getText().toString() + "\n" + "Account:" + accBill.getText().toString() + "\n" + "Transaction complete.");
                    }if(whenSpinner.getSelectedItem().toString().equalsIgnoreCase("Monthly")){
                        try {
                            Thread.sleep(3000);
                            mDocRefFrom.update(balanceSave);
                            Utility.showMessage(getApplicationContext(), "Reg:" + regBill.getText().toString() + "\n" + "Account:" + accBill.getText().toString() + "\n" + "Transaction complete.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }



                }
            }
        });
    }

    private void addItemsOnSpinner() {
        listFrom = new ArrayList<>();
        listWhen = new ArrayList<>();
        listFrom.add("Budget");
        listFrom.add("Default");
        listWhen.add("Now");
        listWhen.add("Monthly");
        listWhen.add("Yearly");
        createSpinner(listFrom, fromSpinner);
        createSpinner(listWhen, whenSpinner);

    }

    private void createSpinner(ArrayList spinnerData, Spinner spinner) {
        ArrayAdapter<String> dataAdapterTransfer = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerData);
        dataAdapterTransfer.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapterTransfer);

    }

    private void init() {
        bottomNavigationView = findViewById(R.id.nav_view);
        fromSpinner = findViewById(R.id.spinnerFrom);
        whenSpinner = findViewById(R.id.spinnerWhen);
        amountBills = findViewById(R.id.amountBill);
        mAuth = FirebaseAuth.getInstance();
        billsBtn = findViewById(R.id.billsBtn);
        regBill = findViewById(R.id.regBill);
        accBill = findViewById(R.id.accBill);

    }
}
