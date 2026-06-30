package com.example.geniuscop

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.geniuscop.database.PartidaDao
import com.example.geniuscop.databinding.ActivityGameBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.jvm.java


class GameActivity : AppCompatActivity() {
    private var mediaPlayer1: MediaPlayer? = null
    private var mediaPlayer2: MediaPlayer? = null
    private var mediaPlayer3: MediaPlayer? = null
    private var mediaPlayer4: MediaPlayer? = null
    private lateinit var binding: ActivityGameBinding
    private val sequence = mutableListOf<Int>()
    private val playerMoves = mutableListOf<Int>()
    private lateinit var partidaDao: PartidaDao
    var round = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MusicService::class.java)
        stopService(intent)


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "meu-banco"
        ).build()
        partidaDao = db.partidaDao()

        mediaPlayer1 = MediaPlayer.create(this, R.raw.meow)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.meow)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.meow)
        mediaPlayer4 = MediaPlayer.create(this, R.raw.meow)

        binding.btnStartRound.setOnClickListener {
            startGame()
        }

        binding.buttongreen.setOnClickListener {
            binding.buttongreen.alpha = 0.5f
            try {
                mediaPlayer1?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(0) }
        binding.buttonred.setOnClickListener {
            binding.buttonred.alpha = 0.5f
            try {
                mediaPlayer2?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(1) }
        binding.buttonyellow.setOnClickListener {
            binding.buttonyellow.alpha = 0.5f
            try {
                mediaPlayer3?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            playerClick(2) }
        binding.buttonblue.setOnClickListener {
            binding.buttonblue.alpha = 0.5f
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
    private fun finalizarPartida(acertos: Int){
        lifecycleScope.launch {
            val partida = Partida(
               acertos = round,
                data = System.currentTimeMillis()
            )
            partidaDao.inserir(partida)
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
        lifecycleScope.launch(Dispatchers.Main) {
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
        finalizarPartida(round)
    }
}
