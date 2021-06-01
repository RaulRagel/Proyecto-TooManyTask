package com.project.project.dto;

import javax.persistence.Column;
import java.time.LocalDate;

public class TaskDTO {

    private Long id;

    private String title;

    private LocalDate createdAt;

    private int investedTime;

    private String priority;

    private String state;

    private Long contractId;

    private String contractBN;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, LocalDate createdAt, int investedTime, String priority, String state, Long contractId, String contractBN) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.investedTime = investedTime;
        this.priority = priority;
        this.state = state;
        this.contractId = contractId;
        this.contractBN = contractBN;
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

    public int getInvestedTime() {
        return investedTime;
    }

    public void setInvestedTime(int investedTime) {
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
