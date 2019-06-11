package com.example.bankapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;

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

import static com.example.bankapp.Helpers.Utility.showMessage;
import static com.example.bankapp.Helpers.Utility.updateUI;

public class TransferActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    //FirebaseUser currentUser;
    BottomNavigationView bottomNavigationView;
    Spinner fromSpinner, toSpinner, whenSpinner;
    ArrayList listFrom, listTo, listWhen;
    Button transferBtn, btnConfirm;
    EditText amount, regTransfer, accTransfer, nemIDpassword;
    TextView nemID, dialog_msg;
    HashMap userDetails;
    CurrentUser currentUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        regTransfer.setVisibility(View.INVISIBLE);
        accTransfer.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");
        loadBottomNav();





        addItemsOnSpinner(currentUser);
        onSelectListeners();





        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!amount.getText().toString().isEmpty()) {
                    transferData(currentUser);
                }else{
                    Utility.showMessage(getApplicationContext(), "Please enter amount.");
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
                        Utility.updateUI(TransferActivity.this, TransferActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_bills:
                        Utility.updateUI(TransferActivity.this, BillsActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_home:
                        Utility.updateUI(TransferActivity.this, HomeActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_menu:
                        Utility.updateUI(TransferActivity.this, SettingsActivity.class, currentUser);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerBottom);
    }

    private void onSelectListeners(){
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == listTo.size()-1){
                    regTransfer.setVisibility(View.VISIBLE);
                    accTransfer.setVisibility(View.VISIBLE);
                }else{
                    regTransfer.setVisibility(View.INVISIBLE);
                    accTransfer.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        whenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Utility.showMessage(getApplicationContext(), "Transfer set to Now");
                        break;
                    case 1:
                        Utility.showMessage(getApplicationContext(), "Transfer set to Monthly");
                        break;
                    case 2:
                        Utility.showMessage(getApplicationContext(), "Transfer set to Yearly");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void transferData(CurrentUser currentUser) {
        //Get selected "FROM" account and withdraw selected amount.
        final DocumentReference mDocRefFrom = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName() + "/Accounts/" + fromSpinner.getSelectedItem().toString());
        mDocRefFrom.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                        Double oldBalance = documentSnapshot.getDouble("balance");
                        Double amountChange = Double.parseDouble(amount.getText().toString());
                        Double newBalance = oldBalance - amountChange;
                        Map<String, Object> balanceSave = new HashMap<>();
                        balanceSave.put("balance", newBalance);
                        balanceSave.put("trans1", -amountChange);
                        mDocRefFrom.update(balanceSave);
                    }
                }

        });

        //Check if transferring "TO" Pension, if yes show Confirm dialog
        if(toSpinner.getSelectedItem().toString().equalsIgnoreCase("Pension")){
            buildAlertDialog("Are you sure? You cannot withdraw from a Pension account", currentUser);

        //Check if transferring "TO" External, if yes show Confirm dialog
        }else if(toSpinner.getSelectedItem().toString().equalsIgnoreCase("External")){
            buildAlertDialog("Confirm transfer to External account", currentUser);

        }else{
            //Get selected "TO" account and insert selected amount.
            final DocumentReference mDocRefTo = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName() + "/Accounts/" + toSpinner.getSelectedItem().toString());
            mDocRefFrom.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Double oldBalance = documentSnapshot.getDouble("balance");
                        Double amountChange = Double.parseDouble(amount.getText().toString());
                        Double newBalance = oldBalance + amountChange;
                        Map<String, Object> balanceSave = new HashMap<>();
                        balanceSave.put("balance", newBalance);
                        mDocRefTo.update(balanceSave);
                        Utility.showMessage(getApplicationContext(), "Successful Transfer");
                    }
                }
            });
        }
    }

    private void buildAlertDialog(String displayMsg, final CurrentUser currentUser) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransferActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);

        dialog_msg = mView.findViewById(R.id.dialog_msg);
        dialog_msg.setText(displayMsg);
        nemIDpassword = mView.findViewById(R.id.nemIDpassword);
        nemID = mView.findViewById(R.id.nemID);
        btnConfirm = mView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nemIDpassword.getText().toString().isEmpty()) {
                    showMessage(getApplicationContext(), "Please verify all fields");

                } else if (Long.parseLong(nemIDpassword.getText().toString()) == Long.parseLong(currentUser.getmNemID())){
                    showMessage(getApplicationContext(), "Transfer Complete");
                    updateUI(TransferActivity.this, HomeActivity.class, currentUser);

                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public void addItemsOnSpinner(CurrentUser currentUser) {
        listFrom = new ArrayList<>();
        listTo = new ArrayList<>();
        listWhen = new ArrayList();

        //Check if Accounts are currently available
        if(currentUser.getmBudget().equals("true")){
            listFrom.add("Budget");
            listTo.add("Budget");
        }
        if(currentUser.getmDefault().equals("true")){
            listFrom.add("Default");
            listTo.add("Default");
        }
        if(currentUser.getmBusiness().equals("true")) {
            listFrom.add("Business");
            listTo.add("Business");
        }
        if(currentUser.getmSavings().equals("true")){
            listFrom.add("Savings");
            listTo.add("Savings");
        }
        if(currentUser.getmPension().equals("true")){
            listTo.add("Pension");
            if(Integer.parseInt(currentUser.getmAge()) >= 77) {
                listFrom.add("Pension");
                }
            }

        listTo.add("External");
        listWhen.add("Now");
        listWhen.add("Monthly");
        listWhen.add("Yearly");
        createSpinner(listWhen, whenSpinner);
        createSpinner(listFrom, fromSpinner);
        createSpinner(listTo, toSpinner);

    }

    private void createSpinner(ArrayList spinnerData, Spinner spinner) {
        ArrayAdapter<String> dataAdapterTransfer = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerData);
        dataAdapterTransfer.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapterTransfer);

    }

    private void init() {
        bottomNavigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();

        fromSpinner = findViewById(R.id.spinnerFrom);
        toSpinner = findViewById(R.id.spinnerTo);
        whenSpinner = findViewById(R.id.spinnerWhen);
        transferBtn = findViewById(R.id.transferBtn);
        amount = findViewById(R.id.amountTransfer);
        regTransfer = findViewById(R.id.regTransfer);
        accTransfer = findViewById(R.id.accTransfer);



    }
}
