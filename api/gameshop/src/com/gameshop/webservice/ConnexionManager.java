package com.gameshop.webservice;

import com.gameshop.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class ConnexionManager {
    private static List<Customer> customers = new ArrayList<>();

    static List<Customer> getCustomers() {
        return customers;
    }

    static void addCustomers(Customer customer) {
        List<Customer> customers = getCustomers();
        customers.add(customer);
    }

    static boolean isConnected(Customer customer) {
        List<Customer> customers = getCustomers();
        return customers.contains(customer);
    }

    static boolean deleteCustomer(Customer customer) {
        return customers.remove(customer);
    }
}
