package com.example.bankapp.Activities;
import android.content.Intent;
import com.example.bankapp.Helpers.CurrentUser;
import com.example.bankapp.Helpers.Utility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bankapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView acc1, acc2, acc3, acc4, acc5, acc1amount, acc2amount, acc3amount, acc4amount, acc5amount;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    ArrayList accountNames, accountAmounts, arrayNames;
    HashMap activeAccounts, userDetails;
    ImageView bankCph, bankOdense;
    TextView myBank;
    CurrentUser currentUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        bankCph.setVisibility(View.INVISIBLE);
        bankOdense.setVisibility(View.INVISIBLE);
        myBank.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("currentUser");
        loadActiveAccounts(currentUser);
        loadBottomNav();



        for(int i = 0; i < arrayNames.size(); i++){
            loadAccountBalance(arrayNames.get(i).toString(), currentUser);
        }




    }

    private void loadBottomNav() {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListenerBottom
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_transfer:
                        Utility.updateUI(HomeActivity.this, TransferActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_bills:
                        Utility.updateUI(HomeActivity.this, BillsActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_home:
                        Utility.updateUI(HomeActivity.this, HomeActivity.class, currentUser);
                        item.setChecked(true);
                        break;
                    case R.id.nav_menu:
                        Utility.updateUI(HomeActivity.this, SettingsActivity.class, currentUser);
                        item.setChecked(true);
                        break;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerBottom);

    }


    private void loadAccountBalance(final String accountName, CurrentUser currentUser) {

            final DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName() + "/Accounts/" + accountName);
            mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        if(accountName.equals("Budget")){
                            acc1amount.setText(documentSnapshot.getDouble("balance").toString());
                        }else if(accountName.equals("Default")){
                            acc2amount.setText(documentSnapshot.getDouble("balance").toString());
                        }else if(accountName.equals("Business")){
                            acc3amount.setText(documentSnapshot.getDouble("balance").toString());
                        }else if(accountName.equals("Savings")){
                            acc4amount.setText(documentSnapshot.getDouble("balance").toString());
                        }else if(accountName.equals("Pension"))
                            acc5amount.setText(documentSnapshot.getDouble("balance").toString());

                    }}

            });
        }


    private void loadActiveAccounts(final CurrentUser currentUser) {

        DocumentReference mDocRef = FirebaseFirestore.getInstance().document("Users/" + currentUser.getmName());
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    //Determine bank branch
                    if(Integer.parseInt(currentUser.getmZipcode()) >= 5000){
                        loadCity("Odense");
                    }else if(Integer.parseInt(currentUser.getmZipcode()) <= 4999){
                        loadCity("Copenhagen");
                    }

                    //Iterate through Accounts and hide/show depending on True/False in DB.
                    for (int j = 0; j < accountNames.size(); j++) {
                        TextView accViews = (TextView) accountNames.get(j);
                        TextView accAmountViews = (TextView) accountAmounts.get(j);

                        if (documentSnapshot.getString(arrayNames.get(j).toString()).equalsIgnoreCase("false")) {
                            accViews.setVisibility(View.INVISIBLE);
                            accAmountViews.setVisibility(View.INVISIBLE);
                        } else {
                            accViews.setVisibility(View.VISIBLE);
                            accAmountViews.setVisibility(View.VISIBLE);
                        }



                    }



                }
            }
        });


    }

    private void loadCity(String city) {
        if(city.equalsIgnoreCase("Copenhagen")){
            myBank.setText("Your bank is located in " + city);
            myBank.setVisibility(View.VISIBLE);
            bankCph.setVisibility(View.VISIBLE);
        }else if(city.equalsIgnoreCase("Odense")){
            myBank.setText("Your bank is located in " + city);
            myBank.setVisibility(View.VISIBLE);
            bankOdense.setVisibility(View.VISIBLE);
        }else{
            myBank.setText("Unable to determine your location");
            myBank.setVisibility(View.VISIBLE);
        }

    }

    private void init() {
        accountNames = new ArrayList<>(Arrays.asList(acc1 = findViewById(R.id.acc1), acc2 = findViewById(R.id.acc2), acc3 = findViewById(R.id.acc3), acc4 = findViewById(R.id.acc4), acc5 = findViewById(R.id.acc5)));
        accountAmounts = new ArrayList<>(Arrays.asList(acc1amount = findViewById(R.id.acc1amount),acc2amount = findViewById(R.id.acc2amount), acc3amount = findViewById(R.id.acc3amount), acc4amount = findViewById(R.id.acc4amount), acc5amount = findViewById(R.id.acc5amount)));
        arrayNames = new ArrayList<String>();
        activeAccounts = new HashMap<String, String>();
        userDetails = new HashMap<String, String>();

        arrayNames.add(acc1.getText().toString());
        arrayNames.add(acc2.getText().toString());
        arrayNames.add(acc3.getText().toString());
        arrayNames.add(acc4.getText().toString());
        arrayNames.add(acc5.getText().toString());
        acc1.setOnClickListener(this);
        acc2.setOnClickListener(this);
        acc3.setOnClickListener(this);
        acc4.setOnClickListener(this);
        acc5.setOnClickListener(this);
        acc1amount.setOnClickListener(this);
        acc2amount.setOnClickListener(this);
        acc3amount.setOnClickListener(this);
        acc4amount.setOnClickListener(this);
        acc5amount.setOnClickListener(this);
        bottomNavigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        bankCph = findViewById(R.id.bankCph);
        bankOdense = findViewById(R.id.bankOdense);
        myBank = findViewById(R.id.myBank);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.acc1:
            case R.id.acc1amount:
                Utility.updateUI(HomeActivity.this, Acc1Activity.class, currentUser);
                break;
            case R.id.acc2:
            case R.id.acc2amount:
                Utility.updateUI(HomeActivity.this, Acc1Activity.class, currentUser);
                break;
            case R.id.acc3:
            case R.id.acc3amount:
                Utility.updateUI(HomeActivity.this, Acc1Activity.class, currentUser);
                break;
            case R.id.acc4:
            case R.id.acc4amount:
                Utility.updateUI(HomeActivity.this, Acc1Activity.class, currentUser);
                break;
            case R.id.acc5:
            case R.id.acc5amount:
                Utility.updateUI(HomeActivity.this, Acc1Activity.class, currentUser);
                break;



        }
    }
}
