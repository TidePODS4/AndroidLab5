package com.example.lab5

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var callLogAdapter: CallLogAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            loadCallLog()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callLogAdapter = CallLogAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = callLogAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = callLogAdapter

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadCallLog()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
            }
        }
    }

    private fun loadCallLog() {
        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.let {
            while (it.moveToNext()) {
                val dateColumnIndex: Int = it.getColumnIndex(CallLog.Calls.DATE)
                val dateMillis: Long = it.getLong(dateColumnIndex)
                val instant: Instant = Instant.ofEpochMilli(dateMillis)
                val localDateTime: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                val formattedDate: String = localDateTime.format(formatter)

                val callLogItem = CallLogItem(
                    "Номер: " + it.getString(it.getColumnIndex(CallLog.Calls.NUMBER)),
                    "Дата: " + formattedDate,
                    "Длительность: " + it.getString(it.getColumnIndex(CallLog.Calls.DURATION))
                )
                callLogAdapter.addCallLog(callLogItem)
            }
        }
        cursor?.close()
    }
}


