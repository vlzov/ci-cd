package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskServiceImpl implements TaskService {

    private final Map<Long, Task> taskStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskStore.values());
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = taskStore.get(id);
        if (task == null) {
            throw new TaskNotFoundException("Задача с id " + id + " не найдена");
        }
        return task;
    }

    @Override
    public Task createTask(TaskRequest request) {
        Long id = idGenerator.getAndIncrement();
        LocalDateTime now = LocalDateTime.now();

        Task task = new Task();
        task.setId(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        taskStore.put(id, task);
        return task;
    }

    @Override
    public Task updateTask(Long id, TaskRequest request) {
        Task existingTask = getTaskById(id);

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setUpdatedAt(LocalDateTime.now());

        taskStore.put(id, existingTask);
        return existingTask;
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskStore.remove(id);
    }
}