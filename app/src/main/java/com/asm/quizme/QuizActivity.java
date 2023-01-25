package com.asm.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.asm.quizme.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
   // InterstitialAd mInterstitialAd;
  // private InterstitialAd mInterstitialAd;

    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //loadInter();
        //Ad loading

       /* AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        super.onAdLoaded(mInterstitialAd);
                        mInterstitialAd = interstitialAd;
                        // Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        // Log.d(TAG, loadAdError.toString());
                        super.onAdFailedToLoad(loadAdError);
                        mInterstitialAd = null;
                    }
                });*/

       /* mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                //Log.d(TAG, "Ad was clicked.");
                super.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
               // Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
               // Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
               // Log.d(TAG, "Ad recorded an impression.");
                super.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
               // Log.d(TAG, "Ad showed fullscreen content.");
                super.onAdShowedFullScreenContent();
            }
        });*/



        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        final String catId = getIntent().getStringExtra("catId");

        Random random = new Random();
       final int rand = random.nextInt(50);

        database.collection("categories")
                .document(catId).collection("questions")
                .whereGreaterThanOrEqualTo("index", rand).orderBy("index")
                .limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     if (queryDocumentSnapshots.getDocuments().size() <= 10) {
                         database.collection("categories")
                                 .document(catId).collection("questions")
                                 .whereLessThanOrEqualTo("index", rand).orderBy("index")
                                 .limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                     @Override
                                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                         for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                             Question question = snapshot.toObject(Question.class);
                                             questions.add(question);
                                         }
                                         setNextQuestion();
                                     }
                                 });

                     }else {
                         for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                             Question question = snapshot.toObject(Question.class);
                             questions.add(question);
                         }
                         setNextQuestion();

                     }
                    }
                });

                resetTimer();


    }

    void resetTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l / 1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }


    void showAnswer(){
        if (question.getAnswer().equals(binding.option1.getText().toString())) {
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if (question.getAnswer().equals(binding.option2.getText().toString())) {
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if (question.getAnswer().equals(binding.option3.getText().toString())) {
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else if (question.getAnswer().equals(binding.option4.getText().toString())) {
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
        }

    }

    void setNextQuestion(){
        if (timer != null)
            timer.cancel();

        timer.start();
        if (index < questions.size()){
            binding.quetionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }

    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(question.getAnswer())){
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }

    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.option1:
            case R.id.option2:
            case R.id.option3:
            case R.id.option4:
                if (timer != null)
                    timer.cancel();
              TextView selected = (TextView) view;
              checkAnswer(selected);
              break;

            case R.id.nextbtn:
                if (index < questions.size() - 1) {
                    reset();
                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();


                    Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.quitbtn:
                /*if (mInterstitialAd != null){
                    mInterstitialAd.show(QuizActivity.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();

                            mInterstitialAd = null;
                           // loadInter();
                        }
                    });
                }else{
                    Toast.makeText(this, "Ad Not Load...", Toast.LENGTH_SHORT).show();
                }*/
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.fifty_fifty_lifeLine:
                Toast.makeText(this, "This field is under development", Toast.LENGTH_SHORT).show();
                break;

            case R.id.audiansPoll_lifeLine:
                Toast.makeText(this, "This field is under development", Toast.LENGTH_SHORT).show();
                break;


        }
    }

   /* private void loadInter()
    {
               AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                       // Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                       // Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

    }*/


}