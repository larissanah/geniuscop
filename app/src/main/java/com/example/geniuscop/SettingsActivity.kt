package com.example.geniuscop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geniuscop.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}

