package com.example.geoquiz

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class CheatActivity : AppCompatActivity() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView
    private lateinit var returnButton: Button
    private var cheatAttempts = 0

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)
        returnButton = findViewById(R.id.return_button)

        apiLevelTextView.text = getString(R.string.api_level, Build.VERSION.SDK_INT)

        showAnswerButton.setOnClickListener {
            if (cheatAttempts < 3) {
                answerTextView.text = getString(R.string.correct_answer, quizViewModel.currentQuestionAnswer)
                showAnswerButton.isEnabled = false
                cheatAttempts++
            } else {
                Toast.makeText(this, getString(R.string.cheat_attempts_limit_reached), Toast.LENGTH_SHORT).show()
                showAnswerButton.isEnabled = false
            }
        }

        returnButton.setOnClickListener {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("cheat_attempts", cheatAttempts)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cheatAttempts = savedInstanceState.getInt("cheat_attempts")
        if (cheatAttempts >= 3) {
            showAnswerButton.isEnabled = false
        }
    }
}