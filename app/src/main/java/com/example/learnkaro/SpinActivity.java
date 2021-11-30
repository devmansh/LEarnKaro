package com.example.learnkaro;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learnkaro.databinding.ActivitySpinBinding;

public class SpinActivity extends AppCompatActivity {

    ActivitySpinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Spin2Home.setOnClickListener(view -> startActivity(new Intent(SpinActivity.this, HomeFragment.class)));

        binding.Spin2Wallet.setOnClickListener(view -> startActivity(new Intent(SpinActivity.this, WalletFragment.class)));
    }



}