package com.acme.shared.domain.model.valueobjects;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Address() {
        this.street = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
        this.country = "";
    }

    public String getAddressAsString() {
        return street + ", " + city + ", " + state + ", " + zipCode + ", " + country;
    }


}
