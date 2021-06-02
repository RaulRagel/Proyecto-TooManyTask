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

        taskDTO.setTitle(upperCaseFirst(task.getTitle()));

        if(!task.getDescription().equals("")){
            taskDTO.setDescription(upperCaseFirst(task.getDescription()));
        }else{
            taskDTO.setDescription(task.getDescription());
        }

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

        taskDTO.setPin(task.isPin());

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
        task.setPin(taskDTO.isPin());

        Contract contract = new Contract();
        contract.setId(taskDTO.getContractId());

        task.setContract(contract);

        return task;
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
