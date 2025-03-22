package com.znextapp.interactivetaskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.znextapp.interactivetaskmanager.enums.FilterOption
import com.znextapp.interactivetaskmanager.enums.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.znextapp.interactivetaskmanager.repository.TaskRepository
import com.znextapp.interactivetaskmanager.model.Task
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private var currentSort = SortOption.DUE_DATE
    private var currentFilter = FilterOption.ALL

    init {
        loadTasks()
    }

    fun getTaskById(taskId: Int): Flow<Task?> {
        return repository.getTaskById(taskId)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            loadTasks() // Refresh task list after insertion
        }
    }

    fun moveTask(fromIndex: Int, toIndex: Int) {
        val currentList = _tasks.value.toMutableList()
        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            val movedTask = currentList.removeAt(fromIndex)
            currentList.add(toIndex, movedTask)
            _tasks.value = currentList
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadTasks() // Refresh after update
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadTasks() // Refresh after deletion
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect { list ->
                val filteredTasks = list.filter { task ->
                    when (currentFilter) {
                        FilterOption.ALL -> true
                        FilterOption.COMPLETED -> task.status?.lowercase() == "completed"
                        FilterOption.PENDING -> task.status?.lowercase() == "pending"
                    }
                }
                _tasks.value = applySorting(filteredTasks)
            }
        }
    }
    fun restoreTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            loadTasks() // Refresh the list
        }
    }

    fun markTaskCompleted(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(status = "Completed")
            repository.updateTask(updatedTask)
            loadTasks() // Refresh the list
        }
    }

    fun markTaskPending(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(status = "Pending")
            repository.updateTask(updatedTask)
            loadTasks() // Refresh the list
        }
    }


    private fun applyFiltersAndSort(list: List<Task>): List<Task> {
        return list
            .filter {
                when (currentFilter) {
                    FilterOption.ALL -> true
                    FilterOption.COMPLETED -> it.status == "Completed"
                    FilterOption.PENDING -> it.status == "Pending"
                }
            }
            .sortedWith(compareBy<Task> {
                when (currentSort) {
                    SortOption.PRIORITY -> mapPriorityToInt(it.priority)
                    SortOption.DUE_DATE -> parseDate(it.dueDate.toString())
                    SortOption.ALPHABETICAL -> it.title.lowercase()
                }
            })
    }

    private fun mapPriorityToInt(priority: String): Int {
        return when (priority) {
            "Low" -> 1
            "Medium" -> 2
            "High" -> 3
            else -> 0
        }
    }

    private fun parseDate(dateStr: String): Long {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.parse(dateStr)?.time ?: Long.MAX_VALUE
        } catch (e: Exception) {
            Long.MAX_VALUE
        }
    }

    fun setSortOption(option: SortOption) {
        currentSort = option
        _tasks.value = applyFiltersAndSort(_tasks.value)
    }

    fun setFilterOption(option: FilterOption) {
        currentFilter = option
        loadTasks()
    }

    private fun applySorting(list: List<Task>): List<Task> {
        return list.sortedWith(compareBy<Task> {
            when (currentSort) {
                SortOption.PRIORITY -> mapPriorityToInt(it.priority)
                SortOption.DUE_DATE -> parseDate(it.dueDate ?: "")
                SortOption.ALPHABETICAL -> it.title.lowercase()
            }
        })
    }

}
