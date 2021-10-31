package com.example.learnkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.learnkaro.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.ContentViewCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSendPasswordLink.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                binding.edtEmail.setError("Required");
                binding.edtEmail.requestFocus();
            }else {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Link Send", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                    alert.setTitle("Password Reset Link");
                                    alert.setMessage("Link has been send to your email.");
                                    alert.setPositiveButton("Open Mail", (dialogInterface, i) -> {
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    });
                                    alert.setNegativeButton("OK", (dialogInterface, i) -> {
                                        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    });
                                    alert.show();
                                } else {
//                                    Toast.makeText(ForgetPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    Snackbar snackbar = Snackbar.make(
                                                    view,
                                                    "Error",
                                                    Snackbar.LENGTH_INDEFINITE);

                                            snackbar.setAction(
                                                    "OK",

                                                    // If the Undo button
                                                    // is pressed, show
                                                    // the message using Toast
                                                    view1 -> snackbar.dismiss());

                                    snackbar.show();
                                }
                            }
                        });

            }
        });
    }
}