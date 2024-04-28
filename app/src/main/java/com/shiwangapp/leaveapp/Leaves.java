package com.shiwangapp.leaveapp;

public class Leaves {
    String leaveType, status, requestDate, purpose, fromDate, tillDate, description;

    public Leaves(String leaveType, String status, String requestDate, String purpose, String fromDate, String tillDate, String description) {
        this.leaveType = leaveType;
        this.status = status;
        this.requestDate = requestDate;
        this.purpose = purpose;
        this.fromDate = fromDate;
        this.tillDate = tillDate;
        this.description = description;
    }

    public Leaves() {
    }

    public Leaves(String leaveType, String status, String requestDate, String purpose) {
        this.leaveType = leaveType;
        this.status = status;
        this.requestDate = requestDate;
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getTillDate() {
        return tillDate;
    }

    public void setTillDate(String tillDate) {
        this.tillDate = tillDate;
    }
}
