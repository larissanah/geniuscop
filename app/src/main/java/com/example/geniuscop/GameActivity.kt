package com.example.geniuscop

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geniuscop.databinding.ActivityGameBinding
import kotlin.random.Random
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val sequence = mutableListOf<Int>()
    private val playerMoves = mutableListOf<Int>()
    var round = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartRound.setOnClickListener {
            startGame()
        }

        binding.buttongreen.setOnClickListener { playerClick(0) }
        binding.buttonred.setOnClickListener { playerClick(1) }
        binding.buttonyellow.setOnClickListener { playerClick(2) }
        binding.buttonblue.setOnClickListener { playerClick(3) }

        binding.voltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    fun showSequenceCoroutine() {
        GlobalScope.launch(Dispatchers.Main) {
            for (color in sequence) {
                when(color) {
                    0 -> binding.buttongreen.alpha = 0.5f
                    1 -> binding.buttonred.alpha = 0.5f
                    2 -> binding.buttonyellow.alpha = 0.5f
                    3 -> binding.buttonblue.alpha = 0.5f
                }
                delay(300)
                binding.buttongreen.alpha = 1f
                binding.buttonred.alpha = 1f
                binding.buttonyellow.alpha = 1f
                binding.buttonblue.alpha = 1f
                delay(500)
            }
        }
    }


    fun startGame() {
        binding.btnStartRound.isEnabled = false
        sequence.clear()
        round = 0
        nextRound()
    }

    fun nextRound() {
        round++
        sequence.add((0..3).random()) // 0=verde, 1=vermelho, 2=amarelo, 3=azul
        showSequenceCoroutine()
        playerMoves.clear()
        binding.txtLevel.text = "Rodada: $round"
    }

    fun playerClick(color: Int) {
        playerMoves.add(color)
        val index = playerMoves.size - 1
        if (playerMoves[index] != sequence[index]) {
            gameOver()
        } else if (playerMoves.size == sequence.size) {
            nextRound()
        }
    }

    fun gameOver() {
        binding.txtLevel.text = "Fim de jogo! Pontuação: $round"
        binding.btnStartRound.isEnabled = true
    }
}
