package com.example.scarlet.Data;

public class Address {
    private String customerId;
    private String street;
    private String ward;
    private String district;
    private String province;
    private String postalCode;
    private String lat;
    private String lng;
    private String addtionalInfo;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(String addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    public Address() {
    }

    public Address(String customerId, String street, String ward, String district, String province, String postalCode, String lat, String lng, String addtionalInfo) {
        this.customerId = customerId;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.province = province;
        this.postalCode = postalCode;
        this.lat = lat;
        this.lng = lng;
        this.addtionalInfo = addtionalInfo;
    }
}
