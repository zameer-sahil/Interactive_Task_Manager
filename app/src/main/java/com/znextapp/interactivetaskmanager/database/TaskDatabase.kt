package com.znextapp.interactivetaskmanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.znextapp.interactivetaskmanager.appdao.TaskDao
import com.znextapp.interactivetaskmanager.model.Task

@Database(entities = [Task::class], version = 4, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
