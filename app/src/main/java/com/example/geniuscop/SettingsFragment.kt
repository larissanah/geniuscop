package com.example.geniuscop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.geniuscop.databinding.FragmentSettingsBinding

class SettingsFragment : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        binding.voltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        //binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
        // salvar preferência de som
        //}
        //binding.switchDifficulty.setOnCheckedChangeListener { _, isChecked ->
        // salvar preferência de dificuldade
        //}
    }



    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

}