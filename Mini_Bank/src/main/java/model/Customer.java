package model;

import java.io.Serializable;
import model.annotation.MaxLength;

public class Customer implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;
    private static long customerCounter = 101;
    private final String customerId;
    @MaxLength(10)
    private String name;
    private final String email, mobile;
    private final Address address;

    public Customer(String name, String email, String mobile, Address address) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address == null ? null : new Address(address.line, address.city, address.pincode);
    }

    private static synchronized String generateCustomerId() {
        return "CUST" + customerCounter++;
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
        return address == null ? null : new Address(address.line, address.city, address.pincode);
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public static class Address implements Serializable {

        private static final long serialVersionUID = 1L;
        private final String line, city, pincode;

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
}
