package com.example.learnkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.learnkaro.databinding.ActivityInviteFriendBinding;

public class InviteFriend extends AppCompatActivity {

    ActivityInviteFriendBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}