package br.com.leucotron.desafio.model;

import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.leucotron.desafio.controller.EmptyFieldsException;

public class Person {

    private DatabaseReference newDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference personReference = newDatabaseReference.child("person");

    private String name;
    private String lastname;
    private String phoneNumber;
    private String email;
    private String skill;
    private String photoURL;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void addPerson(Person person){
        personReference.push().setValue(person);
    }


    public String getName() {
        return name;
    }


    public String getLastname() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getSkill() {
        return skill;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void checkNull(){
        if(name.equals("") || lastname.equals("") || phoneNumber.equals("")
                || email.equals("") || skill.equals("")){
            throw new EmptyFieldsException("Preencha os campos!");
        }
    }

}