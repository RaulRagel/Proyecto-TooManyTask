package com.project.project.dto;

import java.time.LocalDate;
import java.util.List;

public class ContractBTDTO {

    private Long id;

    private String name;

    private String beneficiary;

    private LocalDate createdAt;

    private List<TaskDTO> taskList;
    private int tasks;

    private Long totalHours; //total de horas del contrato (suma de horas de bolsas)
    private Long totalInvested; //total horas invertidas (suma horas invertidas en tareas)
    private Long totalInvestedAux; //variable que se inicializa a totalInvested pero se va consumiendo
    private Long availableHours; //total horas que nos quedan (suma de horas restantes)

    private List<HoursBagDTO> hourBagsList;
    private int hourBags;

    private List<String> warningList;
    private int warnings;

    private boolean pin;

    public ContractBTDTO() {
    }

    public ContractBTDTO(Long id, String name, String beneficiary, LocalDate createdAt, List<TaskDTO> taskList, int tasks, Long totalHours, Long totalInvested, Long totalInvestedAux, Long availableHours, List<HoursBagDTO> hourBagsList, int hourBags, List<String> warningList, int warnings, boolean pin) {
        this.id = id;
        this.name = name;
        this.beneficiary = beneficiary;
        this.createdAt = createdAt;
        this.taskList = taskList;
        this.tasks = tasks;
        this.totalHours = totalHours;
        this.totalInvested = totalInvested;
        this.totalInvestedAux = totalInvestedAux;
        this.availableHours = availableHours;
        this.hourBagsList = hourBagsList;
        this.hourBags = hourBags;
        this.warningList = warningList;
        this.warnings = warnings;
        this.pin = pin;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getHourBags() {
        return hourBags;
    }

    public void setHourBags(int hourBags) {
        this.hourBags = hourBags;
    }

    public List<String> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<String> warningList) {
        this.warningList = warningList;
    }

    public Long getTotalInvestedAux() {
        return totalInvestedAux;
    }

    public void setTotalInvestedAux(Long totalInvestedAux) {
        this.totalInvestedAux = totalInvestedAux;
    }

    public Long getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(Long totalInvested) {
        this.totalInvested = totalInvested;
    }

    public Long getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(Long availableHours) {
        this.availableHours = availableHours;
    }

    public Long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Long totalHours) {
        this.totalHours = totalHours;
    }

    public List<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDTO> taskList) {
        this.taskList = taskList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<HoursBagDTO> getHourBagsList() {
        return hourBagsList;
    }

    public void setHourBagsList(List<HoursBagDTO> hourBagsList) {
        this.hourBagsList = hourBagsList;
    }


}
