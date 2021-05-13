package com.selfowner.kryptomeet;

public class RegisterHelperClass {
    String Name;
    String Email;
    String Contact;
    String Password;
    RegisterHelperClass(){

    }

    public RegisterHelperClass(String name, String email, String contact, String password) {
        Name = name;
        Email = email;
        Contact = contact;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getContact() {
        return Contact;
    }

    public String getPassword() {
        return Password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
