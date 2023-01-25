package com.asm.quizme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asm.quizme.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class walletFragment extends Fragment {


    public walletFragment() {

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

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       user = documentSnapshot.toObject(User.class);
                      binding.currentCoins.setText(String.valueOf(user.getCoins()));

                    }
                });

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getCoins() > 50000){
                    String uid = FirebaseAuth.getInstance().getUid();
                    String payPal = binding.emailBox.getText().toString();
                    String coins = binding.currentCoins.getText().toString();

                    WithdrawRequest request = new WithdrawRequest(uid, payPal, user.getName(), coins);
                    database.collection("withdraws").document(uid).set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                           // String coins = Integer.toString(30);
                            //CoinsUpdated change = new CoinsUpdated(coins);

                           // database.collection("users").document(uid).set(change);
                            /*database.collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    user = documentSnapshot.toObject(User.class);
                                    binding.currentCoins.setText(String.valueOf(30));
                                }
                            });*/



                           // database.collection("users").document(uid).set(request);
                            // database.collection("withdraws").document(uid).set(coins);
                            Toast.makeText(getContext(), "Request send successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(getContext(), "You need more coins to get withdraw", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return binding.getRoot();
    }
}