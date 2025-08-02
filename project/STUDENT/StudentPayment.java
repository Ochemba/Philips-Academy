package com.se.philips.STUDENT;

/**
 * Represents a student's fee payment entry.
 */
public class StudentPayment {
    private final String matricId;
    private final String invoiceNumber;
    private final String paidDate;

    private double amountPaid;
    private double totalFee;
    private double balance;
    private String status;
    private String purpose;
    private boolean verified;

    public StudentPayment(String matricId, double amountPaid, double totalFee,
                          double balance, String status, String purpose,
                          boolean verified, String invoiceNumber, String paidDate) {
        this.matricId = matricId;
        this.amountPaid = amountPaid;
        this.totalFee = totalFee;
        this.balance = balance;
        this.status = status;
        this.purpose = purpose;
        this.verified = verified;
        this.invoiceNumber = invoiceNumber;
        this.paidDate = paidDate;
    }

    public String getMatricId() {
        return matricId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getPaidDate() {
        return paidDate;
    }
}
