package model;

import java.io.Serial;
import java.io.Serializable;
import model.annotation.Id;

public class Customer implements Cloneable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static long customerCounter = 100;

    @Id
    private final String customerId;
    private String name;
    private String email;
    private String mobile;
    private Address address;

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public Customer(String name, Address address) {
        this(name, "", "");
        this.address = address;
    }

    private static String generateCustomerId() {
        return "CUST" + (++customerCounter);
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public Address getAddress() {
        return address;
    }

    public static class Address implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }
    }

    @Override
    public Customer clone() {

        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
