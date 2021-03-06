package com.example.learnkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnkaro.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question> questions;
    Question question;
    int index = 0;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    AdRequest adRequest = new AdRequest.Builder().build();
    binding.adView2.loadAd(adRequest);

    binding.adView2.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            //any logics can be applied
            Toast.makeText(QuizActivity.this, "Ad is loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Toast.makeText(QuizActivity.this, "Ad is opened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            binding.adView2.loadAd(adRequest);
        }

        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }
    });


    questions = new ArrayList<>();
    database = FirebaseFirestore.getInstance();

    final String catId = getIntent().getStringExtra("catId");

    Random random = new Random();
    final int rand = random.nextInt(12);

    database.collection("categories")
            .document(catId)
            .collection("questions")
            .whereGreaterThanOrEqualTo("index", rand)
            .orderBy("index")
            .limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.getDocuments().size() < 5){
                database.collection("categories")
                        .document(catId)
                        .collection("questions")
                        .whereLessThanOrEqualTo("index", rand)
                        .orderBy("index")
                        .limit(5).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots1){
                                    Question question = snapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                            setNextQuestion();
                            });
            } else {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Question question = snapshot.toObject(Question.class);
                    questions.add(question);
                }
            }
            });


    resetTimer();


    }

    void resetTimer(){
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l/1000));

            }

            @Override
            public void onFinish() {
                // TODO document why this method is empty
            }
        };
    }

    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion(){
        resetTimer();
        if (timer != null)
            timer.cancel();
        timer.start();
        if (index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

        }
    }


    void checkAnswer(TextView textview){
        String selectedAnswer = textview.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            textview.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textview.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }

    }


    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick (View view){
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if (timer != null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;
            case R.id.nextBtn:
                reset();
                if (index <= questions.size()){
                    index++;
                    setNextQuestion();

                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    //Toast.makeText(this, "Quiz is Over.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.QuitBtn:
                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }
}