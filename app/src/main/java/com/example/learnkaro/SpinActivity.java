package com.example.learnkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.learnkaro.databinding.ActivitySpinBinding;

public class SpinActivity extends AppCompatActivity {

    ActivitySpinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Spin2Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SpinActivity.this, HomeFragment.class));
            }
        });

        binding.Spin2Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SpinActivity.this, WalletFragment.class));
            }
        });
    }



}