package com.example.geniuscop

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geniuscop.databinding.ActivityGameBinding
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {
    private var mediaPlayer1: MediaPlayer? = null
    private var mediaPlayer2: MediaPlayer? = null
    private var mediaPlayer3: MediaPlayer? = null
    private var mediaPlayer4: MediaPlayer? = null
    private lateinit var binding: ActivityGameBinding
    private val sequence = mutableListOf<Int>()
    private val playerMoves = mutableListOf<Int>()
    var round = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer1 = MediaPlayer.create(this, R.raw.meow)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.meow)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.meow)
        mediaPlayer4 = MediaPlayer.create(this, R.raw.meow)

        binding.btnStartRound.setOnClickListener {
            startGame()
        }

        binding.buttongreen.setOnClickListener {
            try {
                mediaPlayer1?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(0) }
        binding.buttonred.setOnClickListener {
            try {
                mediaPlayer2?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(1) }
        binding.buttonyellow.setOnClickListener {
            try {
                mediaPlayer3?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(2) }
        binding.buttonblue.setOnClickListener {
            try {
                mediaPlayer4?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(3) }

        binding.voltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer1?.release()
        mediaPlayer2?.release()
        mediaPlayer3?.release()
        mediaPlayer4?.release()
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
