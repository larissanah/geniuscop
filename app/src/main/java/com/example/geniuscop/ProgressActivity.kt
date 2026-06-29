package com.example.geniuscop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.geniuscop.database.SequenceDao
import com.example.geniuscop.databinding.ActivityProgressBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java
import kotlin.collections.Map.Entry
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class ProgressActivity : AppCompatActivity() {
    private lateinit var sequenceDao: SequenceDao
    private lateinit var binding: ActivityProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "meu-banco"
        ).build()
        sequenceDao = db.sequenceDao()

        GlobalScope.launch {
            val partidas = sequenceDao.getTodasPartidas()
            val entries = partidas.mapIndexed { index, partida ->
                Entry(index.toFloat(), partida.acertos.toFloat())
            }

            val dataSet = LineDataSet(entries, "Sequencia de acertos")
            val lineData = LineData(dataSet)

            withContext(Dispatchers.Main){
                binding.graph.data = lineData
                binding.graph.invalidate()
            }
        }


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

