package com.exe.quiz;

import static android.util.Log.d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private static final String QUIZ_TAG = "MyActivity";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.exe.quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;

    private boolean answerWasShown = false;
    boolean correctAnswer;
    private Button hintButton;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        d(QUIZ_TAG, "wywołana metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        d(QUIZ_TAG, "wywołana metoda: onCreate");

        if (savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        titleText = findViewById(R.id.title_text);
        trueButton = findViewById(R.id.trueB);
        falseButton = findViewById(R.id.falseB);
        nextButton = findViewById(R.id.nextB);
        questionText = findViewById(R.id.question_text);
        restartButton = findViewById(R.id.restartB);
        restartButton.setVisibility(View.GONE);
        hintButton = findViewById(R.id.hintButton);

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
                disableAnswerButtons();
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                disableAnswerButtons();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex++;
                answerWasShown = false;
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

        hintButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            resultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
                        checkAnswer(true);
                    }
                }
            }
    );
    private void disableAnswerButtons (){
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
    }
    private void checkAnswer(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown) resultMessageId = R.string.answer_was_shown;
        else {
            if (userAnswer == correctAnswer) {
                correctAnswers++;
                resultMessageId = R.string.correct_answer;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
            Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayQuestion() {
            trueButton.setEnabled(true);
            falseButton.setEnabled(true);
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

    @Override
    protected void onStart() {
        super.onStart();
        d(QUIZ_TAG,"wywołana metoda: onStart");

    }
    @Override
    protected void onStop() {
        super.onStop();
        d(QUIZ_TAG,"wywołana metoda: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d(QUIZ_TAG,"wywołana metoda: onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        d(QUIZ_TAG,"wywołana metoda: onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        d(QUIZ_TAG,"wywołana metoda: onResume");
    }
}
