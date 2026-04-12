package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task createTask(TaskRequest request);
    Task updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);
}