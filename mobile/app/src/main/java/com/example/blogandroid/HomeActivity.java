package com.example.blogandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blogandroid.apiservice.APIService;
import com.example.blogandroid.databinding.ActivityHomeBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding homeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        View viewRoot = homeBinding.getRoot();
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(viewRoot);
        homeBinding.logoutButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Do you want to logout?");
            builder.setTitle("Logout alert!");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                logoutUser();
                Intent intent = new Intent(HomeActivity.this, FragmentReplacerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void logoutUser() {
        APIService.apiService.logoutUser().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Logout successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "CRITICAL: SERVER ERROR!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}