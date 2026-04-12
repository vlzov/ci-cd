package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // Явная инициализация сервиса
        taskService = new TaskServiceImpl();
    }

    @Test
    void createTask_ShouldAssignIdAndTimestamps() {
        TaskRequest request = new TaskRequest("Test Task", "Description");

        Task created = taskService.createTask(request);

        assertNotNull(created.getId());
        assertEquals("Test Task", created.getTitle());
        assertEquals("Description", created.getDescription());
        assertFalse(created.isCompleted());
        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
    }

    @Test
    void getAllTasks_ShouldReturnAllCreatedTasks() {
        TaskRequest request1 = new TaskRequest("Task 1", "Desc 1");
        TaskRequest request2 = new TaskRequest("Task 2", "Desc 2");

        taskService.createTask(request1);
        taskService.createTask(request2);

        assertEquals(2, taskService.getAllTasks().size());
    }

    @Test
    void getTaskById_WhenExists_ShouldReturnTask() {
        TaskRequest request = new TaskRequest("Task", "Desc");
        Task created = taskService.createTask(request);

        Task found = taskService.getTaskById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(created.getTitle(), found.getTitle());
    }

    @Test
    void getTaskById_WhenNotExists_ShouldThrowException() {
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999L));
    }

    @Test
    void updateTask_ShouldModifyExistingTask() {
        TaskRequest createRequest = new TaskRequest("Old Title", "Old Desc");
        Task created = taskService.createTask(createRequest);

        TaskRequest updateRequest = new TaskRequest("New Title", "New Desc");
        Task updated = taskService.updateTask(created.getId(), updateRequest);

        assertEquals("New Title", updated.getTitle());
        assertEquals("New Desc", updated.getDescription());
        assertNotEquals(created.getUpdatedAt(), updated.getUpdatedAt());
    }

    @Test
    void deleteTask_ShouldRemoveTask() {
        TaskRequest request = new TaskRequest("Task to delete", "Desc");
        Task created = taskService.createTask(request);

        taskService.deleteTask(created.getId());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(created.getId()));
    }
}