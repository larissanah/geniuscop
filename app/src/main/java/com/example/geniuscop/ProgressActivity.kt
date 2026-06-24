package com.example.geniuscop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geniuscop.databinding.ActivityProgressBinding

class ProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.voltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

//        val series = LineGraphSeries(
//            arrayOf(
//                DataPoint(1.0, 2.0),
//                DataPoint(2.0, 4.0),
//                DataPoint(3.0, 6.0),
//                DataPoint(4.0, 3.0)
//            )
//        )
        //binding.graph.addSeries(series)
    }
}

