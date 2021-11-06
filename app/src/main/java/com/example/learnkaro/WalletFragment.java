package com.example.learnkaro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.learnkaro.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class WalletFragment extends Fragment {


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        binding = FragmentWalletBinding.inflate(inflater, container,false);

        database = FirebaseFirestore.getInstance();

        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.currentCoins.setText(String.valueOf(user.getCoins()));

                //other way
                //binding.currentCoins.setText(user.getCoins() + "");
            }
        });

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getCoins() > 35000) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String payPal = binding.emailBox.getText().toString();
                    //String paytm = binding.paytmphone.getText().toString();

                    if (payPal.isEmpty()){
                        //binding.emailBox.setError("Required");
                        //binding.emailBox.requestFocus();
                        Snackbar snackbar = Snackbar.make(
                                view,
                                "Error !! Please provide either PayPal Email OR Paytm Number",
                                Snackbar.LENGTH_INDEFINITE);

                        snackbar.setAction(
                                "OK",

                                // If the Undo button
                                // is pressed, show
                                // the message using Toast
                                view1 -> snackbar.dismiss());

                        snackbar.show();
                    }

                    else {
                        withdrawRequest request = new withdrawRequest(uid, payPal, user.getName());
                    database.collection("withdraws")
                            .document(uid)
                            .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Request Sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                } else {
                    Toast.makeText(getContext(), "You need more points to get withdraw.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return binding.getRoot();
    }
}