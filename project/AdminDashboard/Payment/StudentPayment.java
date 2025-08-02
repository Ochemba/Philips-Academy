package com.se.philips.AdminDashboard.Payment;

public class StudentPayment {
    private final String matricId;
    private final String studentName;
    private final String invoiceNumber;
    private final double amountPaid;
    private final String paymentStatus;
    private final String paymentDate;

    public StudentPayment( String matricId, String studentName, String invoiceNumber, String balance, double amountPaid, String paymentStatus, String paymentDate) {

        this.matricId = matricId;
        this.studentName = studentName;
        this.invoiceNumber = invoiceNumber;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public String getMatricId() { return matricId; }
    public String getStudentName() { return studentName; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public double getAmountPaid() { return amountPaid; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getPaymentDate() { return paymentDate; }
}
