package com.project.project.service;

import com.project.project.dto.HoursBagDTO;
import com.project.project.dto.TaskDTO;
import com.project.project.entity.HoursBag;
import com.project.project.entity.Task;
import com.project.project.respository.HoursBagRepository;
import com.project.project.respository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    //private final ContractService contractService;

    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long idTask) {
        return TaskMapper.toDto(taskRepository.findById(idTask).isPresent() ?
                taskRepository.findById(idTask).get() : new Task());
    }

     public TaskDTO create(TaskDTO taskDTO) {
         taskDTO.setCreatedAt(LocalDate.now());

        return TaskMapper.toDto(taskRepository.save(TaskMapper.toEntity(taskDTO)));
    }

    public TaskDTO update(TaskDTO hoursBagDTO) {
        return TaskMapper.toDto(taskRepository.save(TaskMapper.toEntity(hoursBagDTO)));
    }

    public void deleteTaskById(Long idTask) {

        TaskDTO taskDTO = getTaskById(idTask);
        taskRepository.delete(TaskMapper.toEntity(taskDTO));
    }

    public void deleteTasks() {

        taskRepository.deleteAll();
    }
}
