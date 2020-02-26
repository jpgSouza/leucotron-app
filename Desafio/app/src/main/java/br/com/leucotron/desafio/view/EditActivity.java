package br.com.leucotron.desafio.view;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.leucotron.desafio.R;
import br.com.leucotron.desafio.controller.Mask;
import br.com.leucotron.desafio.model.Person;

public class EditActivity extends AppCompatActivity {

    //Pop up
    private ProgressDialog progressDialog;

    //Firebase reference
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference personListReference = databaseReference.child("person");

    //Aux variables
    private String name;
    private String key;

    //Input components
    private TextInputEditText nameEditField;
    private TextInputEditText lastanameEditField;
    private TextInputEditText emailEditFiedl;
    private TextInputEditText phoneEditField;
    private TextInputEditText skillsEditField;

    //Event buttons
    private Button editButton;
    private Button cancelButton;

    Person person = new Person();
    Mask mask = new Mask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            name = bundle.getString("Name");
        }

        mask.phoneMask(phoneEditField);

        initComponents();

        recoveryFields();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFields();
                progressDialog = new ProgressDialog(EditActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.edit_progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 2000);

                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(EditActivity.this, ListCurriculumActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, ListCurriculumActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void initComponents(){
        nameEditField = findViewById(R.id.nameEditId);
        lastanameEditField = findViewById(R.id.lastNameEditId);
        emailEditFiedl = findViewById(R.id.emailEditId);
        phoneEditField = findViewById(R.id.phoneEditId);
        skillsEditField = findViewById(R.id.skillsEditId);

        editButton = findViewById(R.id.editButton2Id);
        cancelButton = findViewById(R.id.cancelButtonId);
    }

    public void recoveryFields(){
        personListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Person p = objSnapshot.getValue(Person.class);
                        if(name.equals(p.getName())){
                            nameEditField.setText(p.getName());
                            lastanameEditField.setText(p.getLastname());
                            emailEditFiedl.setText(p.getEmail());
                            phoneEditField.setText(p.getPhoneNumber());
                            skillsEditField.setText(p.getSkill());
                            key = objSnapshot.getKey();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void editFields(){
        person.setName(nameEditField.getText().toString());
        person.setLastname(lastanameEditField.getText().toString());
        person.setEmail(emailEditFiedl.getText().toString());
        person.setPhoneNumber(phoneEditField.getText().toString());
        person.setSkill(skillsEditField.getText().toString());
        personListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        if(key.equals(objSnapshot.getKey())){
                            personListReference.child(objSnapshot.getKey()).setValue(person);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}