package com.asm.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.asm.quizme.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Admob ads Loading
        // getAds();



        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in... ");
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass;

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();

                dialog.show();

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        //show hide password eye
       /* ImageView imageViewShowHidePwd = findViewById(R.id.imageViewShowHidePwd);
        imageViewShowHidePwd.setImageResource(R.drawable.hidden);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.passwordBox.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    binding.passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.hidden);
                }else {
                    binding.passwordBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.show);

                }
            }
        });*/

        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();

            }
        });

        binding.forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   // Toast.makeText(LoginActivity.this, "Please enter Email Id and then click Forgot Password", Toast.LENGTH_SHORT).show();


                    String email = binding.emailBox.getText().toString();
                    dialog.setMessage("Sending Mail...");
                    dialog.show();

                    auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.cancel();
                                    Toast.makeText(LoginActivity.this, "Email Send.. Check Your Mail Box", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.cancel();
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

        });

    }

   /* private void getAds() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ads");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String app_id = snapshot.child("app_id").getValue(String.class);
                String banner = snapshot.child("banner").getValue(String.class);
                String interstitial = snapshot.child("interstitial").getValue(String.class);

                Ads.banner = banner;
                Ads.interstitial = interstitial;
                Ads.appId = app_id;

                //App id change
                try { ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                    Bundle bundle = applicationInfo.metaData;
                    applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", Pref.getPref(Ads.appId,LoginActivity.this));
                    String apiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                    Log.d("AppID", "The saved id is " + Pref.getPref(Ads.appId,LoginActivity.this));
                    Log.d("AppID", "The saved id is " + apiKey); }

                catch (PackageManager.NameNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}