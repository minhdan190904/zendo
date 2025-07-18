package com.gig.zendo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun TaskScreen(taskViewModel: TaskViewModel = viewModel()) {
    var newTask by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("📝 Task Manager", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        Row {
            TextField(
                value = newTask,
                onValueChange = { newTask = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("New task") }
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (newTask.isNotBlank()) {
                    taskViewModel.addTask(newTask.trim())
                    newTask = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(taskViewModel.tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { taskViewModel.toggleTask(task) }
                ) {
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { taskViewModel.toggleTask(task) }
                    )
                    Text(
                        text = task.title,
                        modifier = Modifier.padding(start = 8.dp),
                        style = if (task.completed)
                            TextStyle(textDecoration = TextDecoration.LineThrough)
                        else MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
