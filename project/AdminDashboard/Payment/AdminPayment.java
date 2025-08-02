package com.se.philips.AdminDashboard.Payment;

public class AdminPayment {
    private int id;
    private String titleName;
    private String invoiceNumber;
    private double amountPaid;
    private String paymentPurpose;
    private String paymentDate;

    public AdminPayment(int id, String adminName, String invoiceNumber, double amountPaid, String paymentPurpose, String paymentDate) {
        this.id = id;
        this.titleName = adminName;
        this.invoiceNumber = invoiceNumber;
        this.amountPaid = amountPaid;
        this.paymentPurpose = paymentPurpose;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
