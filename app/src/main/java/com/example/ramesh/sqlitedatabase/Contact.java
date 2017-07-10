package com.example.ramesh.sqlitedatabase;

class Contact {

    //private variables
    private String  _id;
    private String _name;
    private String _phone_number;

    // Empty constructor
    Contact(){

    }
    // constructor
    Contact(String id, String name, String _phone_number){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
    }

    // constructor
    Contact(String name, String _phone_number){
        this._name = name;
        this._phone_number = _phone_number;
    }
    // getting ID
    String getID(){
        return this._id;
    }

    // setting id
    void setID(String id){
        this._id = id;
    }

    // getting name
     String getName(){
        return this._name;
    }

    // setting name
    void setName(String name){
        this._name = name;
    }

    // getting phone number
    String getPhoneNumber(){
        return this._phone_number;
    }

    // setting phone number
    void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
}
