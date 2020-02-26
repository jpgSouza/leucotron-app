
package br.com.leucotron.desafio.view;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import br.com.leucotron.desafio.R;
import br.com.leucotron.desafio.model.Person;

public class ListCurriculumActivity extends AppCompatActivity {

    //Event buttons
    private Button deleteButton;
    private Button editButton;

    //Firebase reference
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference personListReference = databaseReference.child("person");

    //List resource
    private ArrayList<Person> person = new ArrayList<>();
    private Person p;
    private ListView curriculumList;
    private String selectedPerson;

    //Button animaton
    private Animation fade;

    //Aux variable
    private String nameAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_curriculum);

        initComponents();

        recoveryData();

        fade = AnimationUtils.loadAnimation(this,R.anim.animation);

        deleteButton.setAlpha(0);
        editButton.setAlpha(0);

        curriculumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedPerson = (String) parent.getItemAtPosition(position);

                nameAux = selectedPerson;

                deleteButton.setAlpha(1);
                deleteButton.startAnimation(fade);

                editButton.setAlpha(1);
                editButton.startAnimation(fade);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePerson();
                        deleteButton.setAlpha(0);
                        editButton.setAlpha(0);
                    }
                });

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListCurriculumActivity.this,EditActivity.class);
                        Bundle name = new Bundle();
                        name.putString("Name", nameAux);
                        intent.putExtras(name);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

        curriculumList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder phoneAlert  = new AlertDialog.Builder(parent.getContext());
                phoneAlert.setTitle("Phone Call");
                phoneAlert.setMessage("Deseja telefonar para essa pessoa?");
                phoneAlert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPerson = (String) parent.getItemAtPosition(position);

                        nameAux = selectedPerson;

                        recoveryPhoneNumber();
                    }
                });

                phoneAlert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                phoneAlert.create().show();

                return true;
            }
        });

    }

    class MyAdapter extends ArrayAdapter<String>{
        private Context context;
        private String rNames[];
        private String rEmails[];
        private String rPhones[];
        private String rSkills[];
        private String rImagesURL[];

        MyAdapter(Context context, String names[], String email[], String phone[], String skills[], String images[]){
            super(context, R.layout.list_row, names);
            this.context = context;
            this.rNames = names;
            this.rEmails = email;
            this.rPhones = phone;
            this.rSkills = skills;
            this.rImagesURL = images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_row, parent, false);
            TextView names = row.findViewById(R.id.namesListId);
            TextView email = row.findViewById(R.id.emailListId);
            TextView phoneNumber = row.findViewById(R.id.phoneListId);
            TextView skills = row.findViewById(R.id.skillListId);
            ImageView image = row.findViewById(R.id.listImageId);

            names.setText(rNames[position]);
            email.setText(rEmails[position]);
            phoneNumber.setText(rPhones[position]);
            skills.setText(rSkills[position]);

            Glide.with(getApplicationContext()).load(rImagesURL[position]).into(image);

            return row;
        }
    }

    public void initComponents(){
        curriculumList = findViewById(R.id.listViewId);
        deleteButton = findViewById(R.id.deleteButtonId);
        editButton = findViewById(R.id.editButtonId);

    }

    public void recoveryData(){
        personListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    person.clear();
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        p = objSnapshot.getValue(Person.class);
                        person.add(p);
                        String[] names = new String[person.size()];
                        String[] email = new String[person.size()];
                        String[] phone = new String[person.size()];
                        String[] skills = new String[person.size()];
                        String[] images = new String[person.size()];

                        for(int i = 0; i<person.size(); i++){
                            names[i] = person.get(i).getName();
                            email[i] = person.get(i).getEmail();
                            phone[i] = person.get(i).getPhoneNumber();
                            skills[i] = person.get(i).getSkill();
                            images[i] = person.get(i).getPhotoURL();
                        }
                        MyAdapter adapter = new MyAdapter(getApplicationContext(), names, email,phone,skills,images);
                        curriculumList.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recoveryPhoneNumber(){
        personListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        p = objSnapshot.getValue(Person.class);
                        if(p.getName().equals(nameAux)){
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+p.getPhoneNumber()));

                            if (ActivityCompat.checkSelfPermission(ListCurriculumActivity.this,
                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            }
                            startActivity(callIntent);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deletePerson(){
        personListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Person per = objSnapshot.getValue(Person.class);
                        if(per.getName().equals(nameAux)){
                            personListReference.child(objSnapshot.getKey()).removeValue();
                            recoveryData();
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