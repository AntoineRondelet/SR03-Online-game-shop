package com.gameshop.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
public class Purchase {
    private int id;
    private Date datePaid;
    private String username;
    private String paymentMode;
    private String paymentStatus;
    private Customer customerByUsername;
    private PaymentMode paymentModeByPaymentMode;
    private PaymentStatus paymentStatusByPaymentStatus;

    @Id
    @GenericGenerator(name="gen",strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date_paid", nullable = false)
    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    @Basic
    @Column(name = "username", nullable = false, length = -1)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "payment_mode", nullable = false, length = -1)
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Basic
    @Column(name = "payment_status", nullable = false, length = -1)
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        if (id != purchase.id) return false;
        if (datePaid != null ? !datePaid.equals(purchase.datePaid) : purchase.datePaid != null) return false;
        if (username != null ? !username.equals(purchase.username) : purchase.username != null) return false;
        if (paymentMode != null ? !paymentMode.equals(purchase.paymentMode) : purchase.paymentMode != null)
            return false;
        if (paymentStatus != null ? !paymentStatus.equals(purchase.paymentStatus) : purchase.paymentStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (datePaid != null ? datePaid.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (paymentMode != null ? paymentMode.hashCode() : 0);
        result = 31 * result + (paymentStatus != null ? paymentStatus.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false, insertable = false, updatable = false)
    public Customer getCustomerByUsername() {
        return customerByUsername;
    }

    public void setCustomerByUsername(Customer customerByUsername) {
        this.customerByUsername = customerByUsername;
    }

    @ManyToOne
    @JoinColumn(name = "payment_mode", referencedColumnName = "payment_mode", nullable = false, insertable = false, updatable = false)
    public PaymentMode getPaymentModeByPaymentMode() {
        return paymentModeByPaymentMode;
    }

    public void setPaymentModeByPaymentMode(PaymentMode paymentModeByPaymentMode) {
        this.paymentModeByPaymentMode = paymentModeByPaymentMode;
    }

    @ManyToOne
    @JoinColumn(name = "payment_status", referencedColumnName = "payment_status", nullable = false, insertable = false, updatable = false)
    public PaymentStatus getPaymentStatusByPaymentStatus() {
        return paymentStatusByPaymentStatus;
    }

    public void setPaymentStatusByPaymentStatus(PaymentStatus paymentStatusByPaymentStatus) {
        this.paymentStatusByPaymentStatus = paymentStatusByPaymentStatus;
    }
}
