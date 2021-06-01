package com.project.project.service;

import com.project.project.dto.HoursBagDTO;
import com.project.project.dto.TaskDTO;
import com.project.project.entity.Contract;
import com.project.project.entity.HoursBag;
import com.project.project.entity.Task;

public class TaskMapper {

    public static TaskDTO toDto(Task task) {
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(task.getId());
        taskDTO.setContractId(task.getContract().getId());

        taskDTO.setContractBN(
         "["+task.getContract().getBeneficiary()+"] "+task.getContract().getName()
        );

        taskDTO.setTitle(task.getTitle());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setInvestedTime(task.getInvestedTime());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setState(task.getState());


        return taskDTO;
    }

    public static Task toEntity(TaskDTO taskDTO) {
        Task task = new Task();

        task.setId(taskDTO.getId());

        task.setTitle(taskDTO.getTitle());
        task.setCreatedAt(taskDTO.getCreatedAt());
        task.setInvestedTime(taskDTO.getInvestedTime());
        task.setPriority(taskDTO.getPriority());
        task.setState(taskDTO.getState());

        Contract contract = new Contract();
        contract.setId(taskDTO.getContractId());

        task.setContract(contract);

        return task;
    }
}
