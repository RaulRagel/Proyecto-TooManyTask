package com.project.project.dto;

import java.time.LocalDate;

public class ContractDTO {

    private Long id;

    private String name;

    private String beneficiary;

    private LocalDate createdAt;

    private int tasks;

    private Long totalHours;

    private int hourBags;


    private boolean pin;

    public ContractDTO() {
    }

    public ContractDTO(Long id, String name, String beneficiary, LocalDate createdAt, int tasks, Long totalHours, int hourBags, boolean pin) {
        this.id = id;
        this.name = name;
        this.beneficiary = beneficiary;
        this.createdAt = createdAt;
        this.tasks = tasks;
        this.totalHours = totalHours;
        this.hourBags = hourBags;
        this.pin = pin;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }

    public Long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Long totalHours) {
        this.totalHours = totalHours;
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
}
