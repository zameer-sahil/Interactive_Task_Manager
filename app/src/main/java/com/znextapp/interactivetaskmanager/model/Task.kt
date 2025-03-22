package com.znextapp.interactivetaskmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: String,
    val dueDate: String?=null,
    var status: String? = null
)
