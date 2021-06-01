package com.project.project.controller;

import com.project.project.dto.HoursBagDTO;
import com.project.project.dto.TaskDTO;
import com.project.project.service.HoursBagService;
import com.project.project.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getById(@PathVariable String taskId) {
        Long id = Long.parseLong(taskId);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO data) {
        return ResponseEntity.ok(taskService.create(data));
    }

    @PutMapping
    public ResponseEntity<TaskDTO> update(@RequestBody TaskDTO data) {
        if (data.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(taskService.update(data));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteById(@PathVariable String taskId) {

        Long id = Long.parseLong(taskId);

        taskService.deleteTaskById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {

        taskService.deleteTasks();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
