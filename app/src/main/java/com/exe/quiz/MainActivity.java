package com.exe.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionText;
    private TextView titleText;
    private Button restartButton;

    private int currentIndex = 0;
    private int correctAnswers = 0;
    private Question[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleText = findViewById(R.id.title_text);
        trueButton = findViewById(R.id.trueB);
        falseButton = findViewById(R.id.falseB);
        nextButton = findViewById(R.id.nextB);
        questionText = findViewById(R.id.question_text);
        restartButton = findViewById(R.id.restartB);
        restartButton.setVisibility(View.GONE);

        questions = new Question[]{
                new Question(R.string.quest1, true),
                new Question(R.string.quest2, false),
                new Question(R.string.quest3, true),
                new Question(R.string.quest4, false),
                new Question(R.string.quest5, false)
        };

        displayQuestion();
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex++;
                if (currentIndex < questions.length) {
                    displayQuestion();
                } else {
                    trueButton.setEnabled(false);
                    falseButton.setEnabled(false);
                    nextButton.setEnabled(false);
                    displayResult();
                }
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = 0;
                correctAnswers = 0;
                displayQuestion();
                trueButton.setEnabled(true);
                falseButton.setEnabled(true);
                nextButton.setEnabled(true);
                restartButton.setVisibility(View.GONE);
            }
        });

    }
    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (userAnswer == correctAnswer) {
            correctAnswers++;
            resultMessageId = R.string.correct_answer;
        } else {
            resultMessageId = R.string.incorrect_answer;
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void displayQuestion() {
            questionText.setText(questions[currentIndex].getQuestionId());
        }

        private void displayResult() {
            String resultMessage;
            if (correctAnswers >= 3) {
                resultMessage = "Gratulacje! Masz " + correctAnswers + " poprawnych odpowiedzi";
            } else {
                resultMessage = "Spróbuj jeszcze raz! Masz tylko " + correctAnswers + " poprawne odpowiedzi";
            }
            questionText.setText(resultMessage);
            restartButton.setVisibility(View.VISIBLE);
        }

    }
