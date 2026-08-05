package com.example.geniuscop

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.SwitchPreferenceCompat
import com.example.geniuscop.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
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
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(requireContext(), MusicService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

//        findPreference<SwitchPreferenceCompat>("modo")
//        findPreference<SwitchPreferenceCompat>("musica")
//        findPreference<SwitchPreferenceCompat>("efeitos")
//        findPreference<SwitchPreferenceCompat>("tema")

//        val btnAbrirMenu = binding.voltar
//        btnAbrirMenu.setOnClickListener {
//            if (drawerLayout.visibility == View.GONE) {
//                drawerLayout.visibility = View.VISIBLE
//            } else {
//                drawerLayout.visibility = View.GONE
//            }
//        }

        //binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
        // salvar preferência de som
        //}
        //binding.switchDifficulty.setOnCheckedChangeListener { _, isChecked ->
        // salvar preferência de dificuldade
        //}
    }



    override fun onStart() {
        super.onStart()
        val intent = Intent(requireContext(), MusicService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            requireContext().unbindService(serviceConnection)
            isBound = false
        }
    }

}