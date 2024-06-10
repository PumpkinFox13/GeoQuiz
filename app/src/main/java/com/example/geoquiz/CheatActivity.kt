package com.example.geoquiz

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView
    private var cheatAttempts = 0

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)

        apiLevelTextView.text = getString(R.string.api_level, Build.VERSION.SDK_INT)

        showAnswerButton.setOnClickListener {
            if (cheatAttempts < 3) {
                // Показать подсказку
                answerTextView.text = getString(R.string.correct_answer, QuizViewModel().currentQuestionAnswer)
                showAnswerButton.isEnabled = false
                cheatAttempts++
            } else {
                // Предупредить пользователя о достижении лимита подсказок
                Toast.makeText(this, "Вы исчерпали лимит подсказок", Toast.LENGTH_SHORT).show()
            }
        }
    }
}