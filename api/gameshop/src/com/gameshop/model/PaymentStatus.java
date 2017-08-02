package com.gameshop.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
@Table(name = "payment_status", schema = "public", catalog = "sr03")
public class PaymentStatus {
    private String paymentStatus;
    private Collection<Purchase> purchasesByPaymentStatus;

    @Id
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

        PaymentStatus that = (PaymentStatus) o;

        if (paymentStatus != null ? !paymentStatus.equals(that.paymentStatus) : that.paymentStatus != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return paymentStatus != null ? paymentStatus.hashCode() : 0;
    }

    @OneToMany(mappedBy = "paymentStatusByPaymentStatus")
    public Collection<Purchase> getPurchasesByPaymentStatus() {
        return purchasesByPaymentStatus;
    }

    public void setPurchasesByPaymentStatus(Collection<Purchase> purchasesByPaymentStatus) {
        this.purchasesByPaymentStatus = purchasesByPaymentStatus;
    }
}
