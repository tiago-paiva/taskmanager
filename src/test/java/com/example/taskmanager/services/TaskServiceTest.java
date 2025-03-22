package com.example.taskmanager.services;

import com.example.taskmanager.models.Task;
import com.example.taskmanager.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task exempleTask;

    @BeforeEach
    void setUp() {
        exempleTask = new Task(1L, "taskTitle", "taskDescription", false);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(exempleTask, exempleTask));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        long taskId = exempleTask.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(exempleTask));

        Optional<Task> foundTask = taskService.getTaskById(taskId);

        assertTrue(foundTask.isPresent());
        assertEquals(exempleTask.getTitle(), foundTask.get().getTitle());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(exempleTask)).thenReturn(exempleTask);

        Task createdTask = taskService.createTask(exempleTask);

        assertNotNull(createdTask);
        assertEquals(exempleTask.getTitle(), createdTask.getTitle());
        verify(taskRepository, times(1)).save(exempleTask);
    }

    @Test
    void testUpdateTask() {
        long taskId = exempleTask.getId();
        String newTaskTitle = "Task Updated";
        String newTaskDescription = "New description";

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(exempleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(exempleTask);

        Task updatedTask = taskService.updateTask(taskId, new Task(null, newTaskTitle, newTaskDescription, true));

        assertNotNull(updatedTask);
        assertEquals(newTaskTitle, updatedTask.getTitle());
        assertEquals(newTaskDescription, updatedTask.getDescription());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testDeleteTask() {
        long taskId = exempleTask.getId();

        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

}
