package com.project.project.service;

import com.project.project.dto.ContractBTDTO;
import com.project.project.entity.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractBTMapper {

    public static ContractBTDTO toDto(Contract contract) {
        ContractBTDTO contractBTDTO = new ContractBTDTO();

        contractBTDTO.setId(contract.getId());

        contractBTDTO.setName(upperCaseFirst(contract.getName()));  //convertimos siempre la primera en mayúscula
        contractBTDTO.setBeneficiary(upperCaseFirst(contract.getBeneficiary()));

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
        double dec = 0;

        if(contractBTDTO.getTaskList() != null)
        for (int i = 0; i < contractBTDTO.getTaskList().size(); i++) {
            dec += contractBTDTO.getTaskList().get(i).getInvestedTime();
        }
        dec = Math.round(dec); //redondeamos las horas invertidas a la alza

        contractBTDTO.setTotalInvested(new Double(dec).longValue()); //variable que mostramos

        //-----------CALCULO HORAS RESTANTES
        contractBTDTO.setTotalInvestedAux(new Double(dec).longValue()); //variable que usamos para los cálculos de horas restantes

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

        contractBTDTO.setWarningList(getWarnings(contractBTDTO));

        if(contractBTDTO.getWarningList().isEmpty()){
            contractBTDTO.setWarnings(0);
        }else{
            contractBTDTO.setWarnings(contractBTDTO.getWarningList().size());
        }

        //----------MARCADO COMO FAVORITO

        contractBTDTO.setPin(contract.isPin());

        return contractBTDTO;
    }

    private static List<String> getWarnings(ContractBTDTO contractBTDTO) {

        List<String> warningList = new ArrayList<>();
        List<Integer> warningsID = new ArrayList<>();

        contractBTDTO.setWarningImportance(0); //empezamos en 0, si no pasamos por ningún aviso se mantendrá en 0

        //IMPORTANCIA BAJA

        if(contractBTDTO.getHourBagsList().isEmpty()){
            warningList.add("No hay bolsas de horas en el servicio");
            contractBTDTO.setWarningImportance(1);
            warningsID.add(1);
        }
        if(contractBTDTO.getTaskList().isEmpty()){
            warningList.add("No hay tareas en el servicio");
            contractBTDTO.setWarningImportance(1);
            warningsID.add(2);
        }else{
            int warningsPendiente = 0;
            int tareasImportantes = 0;

            for (int i = 0; i < contractBTDTO.getTaskList().size(); i++) {

                if(contractBTDTO.getTaskList().get(i).getState().equals("1") && contractBTDTO.getTaskList().get(i).getInvestedTime() > 0){ //estado pendiente y tiempo > 0

                    warningsPendiente++;
                }
                if(contractBTDTO.getTaskList().get(i).getPriority().equals("1")){ //prioridad alta

                    tareasImportantes++;
                }
            }
            if(warningsPendiente > 0){
                warningList.add("("+warningsPendiente+") Has invertido tiempo a algunas tareas que mantienen el estado 'Pendiente'");
                contractBTDTO.setWarningImportance(1);
                warningsID.add(3);
            }

        //IMPORTANCIA MEDIA

            if(tareasImportantes > 5) {
                warningList.add("Se están acumulando las tareas importantes ("+tareasImportantes+")");
                contractBTDTO.setWarningImportance(2);
                warningsID.add(4);
            }
        }

        if(contractBTDTO.getAvailableHours() < 5){

            if(contractBTDTO.getAvailableHours() != 0) {

                warningList.add("Se están acabando las horas disponibles ("+contractBTDTO.getAvailableHours()+"h)");
                contractBTDTO.setWarningImportance(2);
                warningsID.add(5);

        //IMPORTANCIA ALTA

            }else {
                warningList.add("No quedan horas disponibles");
                contractBTDTO.setWarningImportance(3);
                warningsID.add(6);
            }
        }

        if(contractBTDTO.getTotalInvested()>contractBTDTO.getTotalHours()) {
            warningList.add("Has invertido "+(contractBTDTO.getTotalInvested()-contractBTDTO.getTotalHours())+" horas más de las que tienes");
            contractBTDTO.setWarningImportance(3);
            warningsID.add(7);
        }

        //DESPUES DE RECORRER LOS AVISOS, RELLENAMOS LA LISTA FILTRADA CON SÓLO LOS AVISOS DE SU TIPO
        List<String> warningListFiltered = new ArrayList<>();

        switch (contractBTDTO.getWarningImportance()){
            case 0:
                warningListFiltered.add("0");
                break;
            case 1:
                if(warningsID.contains(1))  warningListFiltered.add("No hay bolsas de horas en el servicio");
                if(warningsID.contains(2)) warningListFiltered.add("No hay bolsas de horas en el servicio");
                if(warningsID.contains(3)) warningListFiltered.add("Has invertido tiempo a algunas tareas que mantienen el estado 'Pendiente'");
                break;
            case 2:
                if(warningsID.contains(4)) warningListFiltered.add("Se están acumulando las tareas importantes");
                if(warningsID.contains(5)) warningListFiltered.add("Se están acabando las horas disponibles");
                break;
            case 3:
                if(warningsID.contains(6)) warningListFiltered.add("No quedan horas disponibles");
                if(warningsID.contains(7)) warningListFiltered.add("Has invertido más horas de las que tienes");
                break;
        }

        contractBTDTO.setWarningListFiltered(warningListFiltered);

        return warningList;
    }

    /* Primero las menos importantes. Si el método llega a las últimas, quiere decir que habrá avisos importantes
        Valor de warningImportance:

        1:
        (x)Has invertido tiempo a algunas tareas que mantienen el estado 'Pendiente'
        No hay bolsas de horas en el servicio
        No hay tareas en el servicio

        2:
        Se están acumulando las tareas importantes (x)
        Se están acabando las horas disponibles (xh)

        3:
        No quedan horas disponibles
        Has invertido (x) horas más de las que tienes
     */

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

}
