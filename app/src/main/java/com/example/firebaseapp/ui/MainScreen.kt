package com.example.firebaseapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val messages = viewModel.messages.collectAsState().value

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crashlytics Test")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                throw RuntimeException("Force Crash")
            }) {
                Text("Force Crash")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val millis = System.currentTimeMillis()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                viewModel.sendMessage("Send at ${dateFormat.format(Date(millis))}")
            }) {
                Text("Write to Database")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Database Contents:")
            LazyColumn {
                items(messages) {
                    Text(it, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}