package br.com.leucotron.desafio.view;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.leucotron.desafio.R;
import br.com.leucotron.desafio.model.Person;

public class ListCurriculumActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference personListReference = databaseReference.child("person");

    private ArrayList<Person> person = new ArrayList<>();
    private Person p;

    private ListView curriculumList;
    String names[] = {""};
    //String names[] = {"João", "Matheus", "Gustavo", "João Paulo", "Bender", "Giovani"};
    String infos[] = {"Java", "C#", "C++","MySQL", "Medicina","Eng.Civil","Ensino médio"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_curriculum);

        initComponents();

        recoveryData(names,infos);

        //MyAdapter adapter = new MyAdapter(this, names, infos);
        //curriculumList.setAdapter(adapter);

        curriculumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Toast.makeText(ListCurriculumActivity.this,"Clicou", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    class MyAdapter extends ArrayAdapter<String>{
        private Context context;
        private String rNames[];
        private String rEmails[];
        private String rPhones[];
        private String rSkills[];

        MyAdapter(Context context, String names[], String email[], String phone[], String skills[]){
            super(context, R.layout.list_row, names);
            this.context = context;
            this.rNames = names;
            this.rEmails = email;
            this.rPhones = phone;
            this.rSkills = skills;
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

            names.setText(rNames[position]);
            email.setText(rEmails[position]);
            phoneNumber.setText(rPhones[position]);
            skills.setText(rSkills[position]);

            return row;
        }
    }

    public void initComponents(){
        curriculumList = findViewById(R.id.listViewId);
    }

    public void recoveryData(String names[], String email[]){
        personListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        p = objSnapshot.getValue(Person.class);
                        person.add(p);
                        String[] names = new String[person.size()];
                        String[] email = new String[person.size()];
                        String[] phone = new String[person.size()];
                        String[] skills = new String[person.size()];

                        for(int i = 0; i<person.size(); i++){
                            names[i] = person.get(i).getName();
                            email[i] = person.get(i).getEmail();
                            phone[i] = person.get(i).getPhoneNumber();
                            skills[i] = person.get(i).getSkill();
                        }
                        MyAdapter adapter = new MyAdapter(getApplicationContext(), names, email,phone,skills);
                        curriculumList.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
