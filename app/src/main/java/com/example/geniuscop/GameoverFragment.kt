package com.example.geniuscop

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.geniuscop.database.PartidaDao
import com.example.geniuscop.databinding.FragmentGameoverBinding
import kotlinx.coroutines.launch

class GameoverFragment : Fragment() {
    private lateinit var binding: FragmentGameoverBinding
    private lateinit var partidaDao: PartidaDao
    private var musicService: MusicService? = null
    private var isBound = false

   private val serviceConnection = object : ServiceConnection {
   override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                val localBinder = binder as MusicService.LocalBinder
                musicService = localBinder.getService()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                musicService = null
                isBound = false
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentGameoverBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val intent = Intent(requireContext(), MusicService::class.java)
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

            binding.tentardnv.setOnClickListener {

            }

//            val btnAbrirMenu = binding.voltar
//            btnAbrirMenu.setOnClickListener {
//                if (drawerLayout.visibility == View.GONE) {
//                    drawerLayout.visibility = View.VISIBLE
//                } else {
//                    drawerLayout.visibility = View.GONE
//                }
//            }
        }

        override fun onStart() {
            super.onStart()
            val intent = Intent(requireContext(), MusicService::class.java)
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            val db = Room.databaseBuilder(
                requireContext().applicationContext,
                AppDatabase::class.java,
                "meu-banco"
            ).build()
            partidaDao = db.partidaDao()
            lifecycleScope.launch {
                val partidas = partidaDao.getTodasPartidas()
                partidas.mapIndexed { index, partida ->
                binding.sequencia.text = partida.acertos.toString()
                }
            }

        }

        override fun onStop() {
            super.onStop()
            if (isBound) {
                requireContext().unbindService(serviceConnection)
                isBound = false
            }
        }
    }
