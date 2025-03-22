package com.znextapp.interactivetaskmanager.repository

import com.znextapp.interactivetaskmanager.appdao.TaskDao
import com.znextapp.interactivetaskmanager.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Int): Flow<Task?> = taskDao.getTaskById(taskId)

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}
