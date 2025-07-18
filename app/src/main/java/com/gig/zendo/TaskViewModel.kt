package com.gig.zendo

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class TaskViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    init {
        listenToTasks()
    }

    private fun listenToTasks() {
        db.collection("tasks")
            .addSnapshotListener { snapshot, _ ->
                _tasks.clear()
                snapshot?.forEach { doc ->
                    doc.toObject(Task::class.java)?.let { _tasks.add(it) }
                }
            }
    }

    fun addTask(title: String) {
        val id = db.collection("tasks").document().id
        val task = Task(id, title, false)
        db.collection("tasks").document(id).set(task)
    }

    fun toggleTask(task: Task) {
        db.collection("tasks").document(task.id)
            .update("completed", !task.completed)
    }
}
