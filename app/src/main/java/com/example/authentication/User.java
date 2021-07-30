package com.example.authentication;

public class User {
    public String email,firstName,middleName,lastName,nativePlace;
    public User(){

    }
    public User(String email,String firstName,String middleName,String lastName,String nativePlace){
      this.email=email;
      this.firstName=firstName;
      this.middleName=middleName;
      this.lastName=lastName;
      this.nativePlace=nativePlace;
    }

}
