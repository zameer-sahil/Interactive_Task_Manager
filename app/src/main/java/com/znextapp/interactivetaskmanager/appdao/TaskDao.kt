package com.znextapp.interactivetaskmanager.appdao

import androidx.room.*
import com.znextapp.interactivetaskmanager.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :taskId LIMIT 1")
    fun getTaskById(taskId: Int): Flow<Task?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}
