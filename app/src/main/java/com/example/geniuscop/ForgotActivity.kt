package com.example.geniuscop

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geniuscop.databinding.ActivityForgotBinding
import com.google.firebase.auth.FirebaseAuth


class ForgotActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityForgotBinding
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
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.irLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            if (validateEmail(binding.emailEt)){
                FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(
                        binding.emailEt.toString().trim())
                    .addOnSuccessListener {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        it.message?.Let
                    }
            } else{

            }

        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun resetPassword (view: View, email: String) {
        val email = binding.emailEt.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailEt.error = "Insert email Id"
            binding.emailEt.requestFocus()
        } else{
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Password reset link please check email",
                            Toast.LENGTH_SHORT,).show()
                    } else {
                        Toast.makeText(baseContext, task.exception.toString(),
                            Toast.LENGTH_SHORT,).show()
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}