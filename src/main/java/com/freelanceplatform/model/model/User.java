package com.freelanceplatform.model.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serializable;

@DynamoDbBean
public class User implements Serializable {
    private static final long serialVersionUID = 4517148380012406558L;

    private String cognitoId;
    private String userId;
    private String email;
    private String firstname;
    private String middlename;
    private String lastname;
    private String telephone;
    private String preferredJobCategories;
    private String role;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

    public String getCognitoId() {
        return cognitoId;
    }

    public void setCognitoId(String cognitoId) {
        this.cognitoId = cognitoId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPreferredJobCategories() {
        return preferredJobCategories;
    }

    public void setPreferredJobCategories(String preferredJobCategories) {
        this.preferredJobCategories = preferredJobCategories;
    }
}