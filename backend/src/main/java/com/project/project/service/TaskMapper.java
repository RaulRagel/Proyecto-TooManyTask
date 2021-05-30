package com.project.project.service;

import com.project.project.dto.HoursBagDTO;
import com.project.project.dto.TaskDTO;
import com.project.project.entity.Contract;
import com.project.project.entity.HoursBag;
import com.project.project.entity.Task;

import java.time.LocalDate;

public class TaskMapper {

    public static TaskDTO toDto(Task task) {
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(task.getId());
        taskDTO.setContractId(task.getContract().getId());

        taskDTO.setContractBN(
         "["+task.getContract().getBeneficiary()+"] "+task.getContract().getName()
        );

        taskDTO.setDescription(task.getDescription());

        taskDTO.setTitle(task.getTitle());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setInvestedTime(task.getInvestedTime());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setState(task.getState());

        //------Fecha de fin

        if(taskDTO.getState().equals("3")){ //si es finalizado, pues seteamos la fecha
            taskDTO.setFinishedAt(LocalDate.now());
        }else{
            taskDTO.setFinishedAt(null);
        }

        return taskDTO;
    }

    public static Task toEntity(TaskDTO taskDTO) {
        Task task = new Task();

        task.setId(taskDTO.getId());

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCreatedAt(taskDTO.getCreatedAt());
        task.setFinishedAt(taskDTO.getFinishedAt());
        task.setInvestedTime(taskDTO.getInvestedTime());
        task.setPriority(taskDTO.getPriority());
        task.setState(taskDTO.getState());

        Contract contract = new Contract();
        contract.setId(taskDTO.getContractId());

        task.setContract(contract);

        return task;
    }
}
