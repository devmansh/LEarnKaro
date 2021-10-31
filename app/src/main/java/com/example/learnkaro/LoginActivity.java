package com.example.learnkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.learnkaro.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(view -> {
            showProgressBar();
            String email, pass;
            email = binding.emailBox.getText().toString();
            pass = binding.passwordBox.getText().toString();

            if (email.isEmpty()) {
                binding.emailBox.setError("Required");
                binding.emailBox.requestFocus();
                hideProgressBar();
            }else if (pass.isEmpty()) {
                binding.passwordBox.setError("Required");
                binding.passwordBox.requestFocus();
                hideProgressBar();
            }else {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        hideProgressBar();
                        if (task.getException() != null) {
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

        binding.createNewUser.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, signupActivity.class)));
    }

    private void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layout.setAlpha(0.3f);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    private void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
        binding.layout.setAlpha(1f);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}