package com.example.geniuscop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.geniuscop.database.PartidaDao
import com.example.geniuscop.databinding.FragmentProgressBinding
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firestore.v1.FirestoreGrpc.bindService

class ProgressFragment : Fragment() {
    private lateinit var partidaDao: PartidaDao
    private lateinit var binding: FragmentProgressBinding
    private lateinit var chart: LineChart
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
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)


        chart = findViewById< LineChart>(R.id.graph)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "meu-banco"
        ).build()
        partidaDao = db.partidaDao()

        lifecycleScope.launch {
            val partidas = partidaDao.getTodasPartidas()
            val entries = partidas.mapIndexed { index, partida ->
                Entry(index.toFloat(), partida.acertos.toFloat())
            }

            val dataSet = LineDataSet(entries, "Sequencia de acertos").apply {
                color = android.graphics.Color.BLUE
                valueTextColor = android.graphics.Color.BLACK
                lineWidth = 2f
                circleRadius = 4f
            }
            val lineData = LineData(dataSet)

            withContext(Dispatchers.Main){
                chart.data = lineData
                chart.description.text = "Progresso do paciente"
                chart.invalidate()
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