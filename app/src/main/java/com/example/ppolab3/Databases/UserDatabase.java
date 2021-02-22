package com.example.ppolab3.Databases;

public class UserDatabase {
   public UserDatabase(){}
   public UserDatabase(String user_email,String user_name,boolean isGravatar,String user_id){
        this.isGravatar=isGravatar;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_id = user_id;
    }
  public String user_name,user_email,user_id;
  public boolean isGravatar;
}
