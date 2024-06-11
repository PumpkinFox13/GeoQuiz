package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var cheatButton: Button
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        cheatButton = findViewById(R.id.cheat_button)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)

        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            checkAskCompleted()
        }
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            checkAskCompleted()
        }

        updateQuestion()
        checkAskCompleted()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT)
        {
            quizViewModel.CheckCheat(data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        val isLastQuestion = quizViewModel.currentIndex == quizViewModel.questionSize - 1

        // Если это последний вопрос, скрываем кнопку "Next"
        nextButton.visibility = if (isLastQuestion) View.GONE else View.VISIBLE
        }

    private fun checkAnswer(userAnswer: Boolean) {
        if (checkAskCompleted()) return

        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.currentCorrectAnswerUp()
        } else {
            messageResId = R.string.incorrect_toast
        }
        if (quizViewModel.isCheater) messageResId = R.string.judgment_toast


        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        )
            .show()

        quizViewModel.currentQuestionUserResponse(userAnswer) //Запоминание ответа пользователя

        trueButton.isEnabled = false
        falseButton.isEnabled = false
        quizViewModel.currentResponseUp()
        if (checkAskCompleted()) return

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.currentCorrectAnswerUp()
        } else {
            messageResId = R.string.incorrect_toast
        }
        if (quizViewModel.isCheater) messageResId = R.string.judgment_toast

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()

        quizViewModel.currentQuestionUserResponse(userAnswer) // Запоминание ответа пользователя

        trueButton.isEnabled = false
        falseButton.isEnabled = false
        quizViewModel.currentResponseUp()

        // Проверяем, является ли текущий вопрос последним в игре
        if (quizViewModel.currentIndex == quizViewModel.questionSize - 1) {
            // Если это последний вопрос, показываем результаты
            checkQuizCompleted()
        }

    }

    private fun checkAskCompleted(): Boolean {
        checkQuizCompleted()
        if (quizViewModel.currentQuestionUserResponse == null) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            return false
        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            return true
        }
    }

    private fun checkQuizCompleted() {
        val quizSize = quizViewModel.questionSize
        if (quizViewModel.currentResponse == quizSize) {
            val msg =
                "Игра завершена. Правильных ответов ${quizViewModel.currentCorrectAnswer}/${quizSize}"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

}