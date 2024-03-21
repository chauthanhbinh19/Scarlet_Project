package com.example.scarlet.Data;

public class Customer {
    private String id;
    private String name;
    private String gender;
    private String date_of_birth;
    private String address;
    private String phone_number;
    private String email;
    private String username;
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Customer(){

    }
    public Customer(String id,String name, String gender,String date_of_birth,String address,String phone_number, String email, String username, String password){
        this.id=id;
        this.name=name;
        this.gender=gender;
        this.address=address;
        this.date_of_birth=date_of_birth;
        this.phone_number=phone_number;
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public String getId() {
        return id;
    }
    public String getName(){
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
