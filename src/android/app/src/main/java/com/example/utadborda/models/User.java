package com.example.utadborda.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Date dateOfBirth;
    private List<String> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setUserRoles(List<String> userRoles) {
        this.roles = userRoles;
    }

    public void addUserRole(String userRole) {
        this.roles.add(userRole);
    }
}
