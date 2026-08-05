package com.example.geniuscop

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.geniuscop.database.PartidaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.geniuscop.databinding.FragmentGameBinding
import kotlin.jvm.java
import kotlin.time.Duration.Companion.milliseconds

class GameFragment : Fragment() {
    private var mediaPlayer1: MediaPlayer? = null
    private var mediaPlayer2: MediaPlayer? = null
    private var mediaPlayer3: MediaPlayer? = null
    private var mediaPlayer4: MediaPlayer? = null
    private lateinit var binding: FragmentGameBinding
    private val sequence = mutableListOf<Int>()
    private val playerMoves = mutableListOf<Int>()
    private lateinit var partidaDao: PartidaDao
    var round = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(requireContext(), MusicService::class.java)
        requireContext().stopService(intent)

        val db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "meu-banco"
        ).build()
        partidaDao = db.partidaDao()

        mediaPlayer1 = MediaPlayer.create(requireContext(), R.raw.taiko)
        mediaPlayer2 = MediaPlayer.create(requireContext(), R.raw.efect)
        mediaPlayer3 = MediaPlayer.create(requireContext(), R.raw.orch)
        mediaPlayer4 = MediaPlayer.create(requireContext(), R.raw.free)

        binding.btnStartRound.setOnClickListener { startGame() }
        binding.buttongreen.setOnClickListener { playerClick(0); mediaPlayer1?.start() }
        binding.buttonred.setOnClickListener { playerClick(1); mediaPlayer2?.start() }
        binding.buttonyellow.setOnClickListener { playerClick(2); mediaPlayer3?.start() }
        binding.buttonblue.setOnClickListener { playerClick(3); mediaPlayer4?.start() }

//        val btnAbrirMenu = binding.voltar
//        val navView = binding.nav_view
//        btnAbrirMenu.setOnClickListener {
//            if (navView.visibility == View.GONE) {
//                navView.visibility = View.VISIBLE
//            } else {
//                navView.visibility = View.GONE
//            }
//        }
    }
    private fun finalizarPartida(acertos: Int){
        lifecycleScope.launch {
            val partida = Partida(
                acertos = round,
                data = System.currentTimeMillis()
            )
            partidaDao.inserir(partida)
            val fragmentManager = GameoverFragment()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
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
                delay(300.milliseconds)
                binding.buttongreen.alpha = 1f
                binding.buttonred.alpha = 1f
                binding.buttonyellow.alpha = 1f
                binding.buttonblue.alpha = 1f
                delay(500.milliseconds)
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

    override fun onStop() {
        super.onStop()
        val intent = Intent(requireContext(), MusicService::class.java)
        requireContext().startService(intent)
    }


    fun gameOver() {
        binding.txtLevel.text = "Fim de jogo! Pontuação: $round"
        binding.btnStartRound.isEnabled = true
        finalizarPartida(round)

    }
}