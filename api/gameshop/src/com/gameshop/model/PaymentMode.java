package com.gameshop.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
@Table(name = "payment_mode", schema = "public", catalog = "sr03")
public class PaymentMode {
    private String paymentMode;
    private Collection<Purchase> purchasesByPaymentMode;

    @Id
    @Column(name = "payment_mode", nullable = false, length = -1)
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentMode that = (PaymentMode) o;

        if (paymentMode != null ? !paymentMode.equals(that.paymentMode) : that.paymentMode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return paymentMode != null ? paymentMode.hashCode() : 0;
    }

    @OneToMany(mappedBy = "paymentModeByPaymentMode")
    public Collection<Purchase> getPurchasesByPaymentMode() {
        return purchasesByPaymentMode;
    }

    public void setPurchasesByPaymentMode(Collection<Purchase> purchasesByPaymentMode) {
        this.purchasesByPaymentMode = purchasesByPaymentMode;
    }
}
