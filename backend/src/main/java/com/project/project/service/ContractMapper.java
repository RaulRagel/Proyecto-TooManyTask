package com.project.project.service;

import com.project.project.dto.ContractDTO;
import com.project.project.entity.Contract;

public class ContractMapper {

    public static ContractDTO toDto(Contract contract) {
        ContractDTO contractDTO = new ContractDTO();

        contractDTO.setId(contract.getId());

        contractDTO.setName(upperCaseFirst(contract.getName())); //convertimos siempre la primera en mayúscula
        contractDTO.setBeneficiary(upperCaseFirst(contract.getBeneficiary()));

        contractDTO.setCreatedAt(contract.getCreatedAt());

        if(contract.getHoursBagList() != null) contractDTO.setHourBags(contract.getHoursBagList().size());
        else contractDTO.setHourBags(0);

        double sum = 0;

        if(contract.getTaskList() != null){ //si no estaba vacía

            contractDTO.setTasks(contract.getTaskList().size()); //marcamos su numero de tareas

            for(int i=0;i<contract.getTaskList().size();i++){
                sum += contract.getTaskList().get(i).getHours();
            }

            contractDTO.setTotalHours(new Double(sum).longValue());
        }else{

            contractDTO.setTasks(0);
            contractDTO.setTotalHours(Long.parseLong("0"));
        }

        contractDTO.setPin(contract.isPin());

        return contractDTO;
    }

    public static Contract toEntity(ContractDTO contractDTO) {
        Contract contract = new Contract();

        contract.setId(contractDTO.getId());
        contract.setName(upperCaseFirst(contractDTO.getName()));
        contract.setBeneficiary(upperCaseFirst(contractDTO.getBeneficiary()));
        contract.setCreatedAt(contractDTO.getCreatedAt());
        contract.setPin(contractDTO.isPin());

        return contract;
    }

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
