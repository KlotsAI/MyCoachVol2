package com.example.mycoach.Models;

public class User {
    private String name, email, pass, age, phone, sex, experience;
   // private String sex;

    public User(){}

    public User(String name, String email, String pass,  String age, String phone, String sex, String experience) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.age = age;
        this.phone = phone;
        this.sex = sex;
        this.experience = experience;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }


    public String getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }

    public String getExperience(){return experience;}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public void setAge(String age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSex(String sex) {this.sex = sex;}

    public void setExperience(String experience) {this.experience = experience;}
}
