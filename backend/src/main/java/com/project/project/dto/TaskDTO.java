package com.project.project.dto;

import java.time.LocalDate;

public class TaskDTO {

    private Long id;

    private String title;

    private String description;

    private LocalDate createdAt;

    private LocalDate finishedAt;

    private double investedTime; //es la suma de horas y minutos

    private double hours;

    private double minutes;

    private String priority;

    private String state;

    private Long contractId;

    private String contractBN;

    private boolean pin;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, LocalDate createdAt, LocalDate finishedAt, int investedTime, String priority, String state, Long contractId, String contractBN) {

        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.investedTime = investedTime;
        this.priority = priority;
        this.state = state;
        this.contractId = contractId;
        this.contractBN = contractBN;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getMinutes() {
        return minutes;
    }

    public void setMinutes(double minutes) {
        this.minutes = minutes;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDate finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getInvestedTime() {
        return investedTime;
    }

    public void setInvestedTime(double investedTime) {
        this.investedTime = investedTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractBN() {
        return contractBN;
    }

    public void setContractBN(String contractBN) {
        this.contractBN = contractBN;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
