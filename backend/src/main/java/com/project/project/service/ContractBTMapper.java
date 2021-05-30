package com.project.project.service;

import com.project.project.dto.ContractBTDTO;
import com.project.project.dto.ContractDTO;
import com.project.project.entity.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractBTMapper {

    public static ContractBTDTO toDto(Contract contract) {
        ContractBTDTO contractBTDTO = new ContractBTDTO();

        contractBTDTO.setId(contract.getId());
        contractBTDTO.setName(contract.getName());
        contractBTDTO.setBeneficiary(contract.getBeneficiary());
        contractBTDTO.setCreatedAt(contract.getCreatedAt());

        //-------------LISTA DE BOLSAS DE HORAS
        if(contract.getHoursBagList() != null){
            contractBTDTO.setHourBagsList(contract.getHoursBagList().stream().map(HoursBagMapper::toDto)
                    .collect(Collectors.toList()));

            contractBTDTO.setHourBags(contractBTDTO.getHourBagsList().size());
        }else{
            contractBTDTO.setHourBags(0);
        }

        //-------------LISTA DE TAREAS
        if(contract.getTaskList() != null){
            contractBTDTO.setTaskList(contract.getTaskList().stream().map(TaskMapper::toDto)
                    .collect(Collectors.toList()));

            contractBTDTO.setTasks(contractBTDTO.getTaskList().size());
        }else{
            contractBTDTO.setTasks(0);
        }


        //-------------CALCULO DE HORAS TOTALES
        int sum = 0;

        if(contractBTDTO.getHourBagsList() != null)
            for (int i = 0; i < contractBTDTO.getHourBagsList().size(); i++) {
                sum += contractBTDTO.getHourBagsList().get(i).getHours();
            }

        contractBTDTO.setTotalHours(Long.parseLong(String.valueOf(sum)));

        //-------------CALCULO TIEMPO INVERTIDO
        sum = 0;

        if(contractBTDTO.getTaskList() != null)
        for (int i = 0; i < contractBTDTO.getTaskList().size(); i++) {
            sum += contractBTDTO.getTaskList().get(i).getInvestedTime();
        }

        contractBTDTO.setTotalInvested(Long.parseLong(String.valueOf(sum))); //variable que mostramos

        //-----------CALCULO HORAS RESTANTES
        contractBTDTO.setTotalInvestedAux(Long.parseLong(String.valueOf(sum))); //variable que usamos para los cálculos de horas restantes

        boolean puedeConsumir = true;

        if(contractBTDTO.getHourBagsList() != null)
        for (int i = 0; i < contractBTDTO.getHourBagsList().size(); i++) {

            contractBTDTO.getHourBagsList().get(i).setRemaining(contractBTDTO.getHourBagsList().get(i).getHours());

            if(contractBTDTO.getTotalInvestedAux() >= contractBTDTO.getHourBagsList().get(i).getHours() && puedeConsumir){

                contractBTDTO.setTotalInvestedAux(contractBTDTO.getTotalInvestedAux() - contractBTDTO.getHourBagsList().get(i).getHours()); //consumo horas (total-horas)
                contractBTDTO.getHourBagsList().get(i).setRemaining(Long.parseLong("0")); //ya no quedan horas
                puedeConsumir = true; //podremos consumir de la siguiente

            }else if(puedeConsumir){

                contractBTDTO.getHourBagsList().get(i).setRemaining(contractBTDTO.getHourBagsList().get(i).getHours()-contractBTDTO.getTotalInvestedAux()); //todavía quedan horas
                contractBTDTO.setTotalInvestedAux(contractBTDTO.getHourBagsList().get(i).getHours()-contractBTDTO.getTotalInvestedAux()); //reflejamos el calculo
                puedeConsumir = false; //no podremos consumir de la siguiente
            }
        }

        //-----------CALCULO DE HORAS DISPONIBLES
        sum = 0;

        if(contractBTDTO.getHourBagsList() != null)
            for (int i = 0; i < contractBTDTO.getHourBagsList().size(); i++) {
                sum += contractBTDTO.getHourBagsList().get(i).getRemaining();
            }

        contractBTDTO.setAvailableHours(Long.parseLong(String.valueOf(sum))); //variable que mostramos

        //-----------LISTA DE AVISOS

        contractBTDTO.setWarningList(comprobarWarnings(contractBTDTO));

        if(contractBTDTO.getWarningList().isEmpty()){
            contractBTDTO.setWarnings(0);
        }else{
            contractBTDTO.setWarnings(contractBTDTO.getWarningList().size());
        }

        return contractBTDTO;
    }

    private static List<String> comprobarWarnings(ContractBTDTO contractBTDTO) {

        List<String> warningList = new ArrayList<>();

        if(contractBTDTO.getHourBagsList().isEmpty()) warningList.add("No hay bolsas de horas en el servicio");

        if(contractBTDTO.getAvailableHours() < 5) warningList.add("Se están acabando las horas disponibles");

        if(contractBTDTO.getAvailableHours() == 0) warningList.add("No quedan horas disponibles");

        if(contractBTDTO.getTaskList().isEmpty()) warningList.add("No hay tareas en el servicio");

        if(!contractBTDTO.getTaskList().isEmpty()){
            int tareasImportantes = 0;

            for (int i = 0; i < contractBTDTO.getTaskList().size(); i++) {
                if(contractBTDTO.getTaskList().get(i).getPriority().equals("1")){ //prioridad alta
                    tareasImportantes++;
                }

                if(contractBTDTO.getTaskList().get(i).getState().equals("1") && contractBTDTO.getTaskList().get(i).getInvestedTime() > 0){ //estado pendiente y tiempo > 0
                    warningList.add("Has invertido tiempo a alguna tareas pero no has cambiado su estado");
                }
            }

            if(tareasImportantes > 9) warningList.add("Se están acumulando las tareas importantes");
        }



        return warningList;
    }

    /*
        1  No hay bolsas de horas en el servicio

        2  Se están acabando las horas disponibles

        3  No quedan horas disponibles

        4 No hay tareas en el servicio

        5 Se están acumulando las tareas importantes

        6 Has invertido tiempo a alguna tareas pero no has cambiado su estado
     */


}
