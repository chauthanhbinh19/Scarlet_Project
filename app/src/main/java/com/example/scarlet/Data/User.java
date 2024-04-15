package com.example.scarlet.Data;

public class User {
    private String uid;
    private String first_name;
    private String last_name;
    private String gender;
    private String date_of_birth;
    private String phone_number;
    private String email;
    private int point;
    private String membershipId;
    private String avatar_img;
    private boolean isCustomer;
    private boolean isEmployee;
    private String key;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }

    public User(){

    }

    public User(String uid, String first_name, String last_name, String gender, String date_of_birth, String phone_number, String email, int point, String membershipId, String avatar_img, boolean isCustomer, boolean isEmployee) {
        this.uid = uid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.email = email;
        this.point = point;
        this.membershipId = membershipId;
        this.avatar_img = avatar_img;
        this.isCustomer=isCustomer;
        this.isEmployee=isEmployee;
    }
    public User(String uid, String first_name, String last_name, String gender, String date_of_birth, String phone_number, String email, int point, String membershipId, String avatar_img,String key) {
        this.uid = uid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.email = email;
        this.point = point;
        this.membershipId = membershipId;
        this.avatar_img = avatar_img;
        this.key=key;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }


    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar_img() {
        return avatar_img;
    }
}
