package com.project.project.dto;

import java.time.LocalDate;

public class HoursBagDTO {

    private Long id;

    private LocalDate initDate;

    private LocalDate endDate;

    private Long hours;

    private LocalDate createdAt;

    private Long contractId;

    private String contractBN;

    public HoursBagDTO() {
    }

    public HoursBagDTO(Long id, LocalDate initDate, LocalDate endDate, Long hours, LocalDate createdAt, Long contractId, String contractBN) { //
        this.id = id;
        this.initDate = initDate;
        this.endDate = endDate;
        this.hours = hours;
        this.createdAt = createdAt;
        this.contractId = contractId;
        this.contractBN = contractBN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
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
}
