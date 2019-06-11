package com.example.bankapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bankapp.Activities.BillsActivity;
import com.example.bankapp.Activities.HomeActivity;
import com.example.bankapp.Activities.ProfileActivity;
import com.example.bankapp.Activities.TransferActivity;
import com.example.bankapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class Utility extends AppCompatActivity {



    public static void showMessage(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    public static void updateUI(Activity oldActivity, Class newActivity, CurrentUser currentUser){

        Intent update = new Intent(oldActivity, newActivity);
        update.putExtra("currentUser", currentUser);
        oldActivity.startActivity(update);
    }

   /* public void loadBottomNav(final Activity currentActivity, final CurrentUser currentUser){

        bottomNavigationView = findViewById(R.id.nav_view);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListenerBottom
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_transfer:
                        Intent transfer = new Intent(currentActivity, TransferActivity.class);
                        transfer.putExtra("currentUser", currentUser);
                        startActivity(transfer);
                        return true;
                    case R.id.nav_bills:
                        Intent bills = new Intent(currentActivity, BillsActivity.class);
                        bills.putExtra("currentUser", currentUser);
                        startActivity(bills);
                        return true;
                    case R.id.nav_home:
                        Intent home = new Intent(currentActivity, HomeActivity.class);
                        home.putExtra("currentUser", currentUser);
                        startActivity(home);
                        return true;
                    case R.id.nav_menu:
                        Intent menu = new Intent(currentActivity, ProfileActivity.class);
                        menu.putExtra("currentUser", currentUser);
                        startActivity(menu);
                        return true;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListenerBottom);
    }*/


}
