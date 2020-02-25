package br.com.leucotron.desafio.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.leucotron.desafio.R;
import br.com.leucotron.desafio.model.Person;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference searchReference = databaseReference.child("person");

    private EditText searchField;
    private ImageButton searchButton;

    private RecyclerView searchListResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initComponents();

        searchListResult.setHasFixedSize(true);
        searchListResult.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchName = searchField.getText().toString();

                searchPersonInFirebase(searchName);
            }
        });

    }

    public void initComponents(){
        searchField = findViewById(R.id.searchFieldId);
        searchButton = findViewById(R.id.searchButtonId);
        searchListResult = findViewById(R.id.searchListId);
    }

    public void searchPersonInFirebase(String searchName){

        Query firebaseSearch = searchReference.orderByChild("name").startAt(searchName).endAt(searchName + "\uf8ff");

        FirebaseRecyclerAdapter<Person, MyAdapterHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Person, MyAdapterHolder>(
                Person.class,
                R.layout.list_row,
                MyAdapterHolder.class,
                firebaseSearch
        ) {
            @Override
            protected void populateViewHolder(MyAdapterHolder myAdapterHolder, Person model, int i) {

                myAdapterHolder.recoveryData(SearchActivity.this, model.getName(), model.getEmail(), model.getPhoneNumber(),model.getSkill());

            }
        };

        searchListResult.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MyAdapterHolder extends RecyclerView.ViewHolder{

        View view;

        public MyAdapterHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

        }

        public void recoveryData(Context context, String name, String email, String phone, String skill){
            TextView personName = view.findViewById(R.id.namesListId);
            TextView personEmail = view.findViewById(R.id.emailListId);
            TextView personPhone = view.findViewById(R.id.phoneListId);
            TextView personSkill = view.findViewById(R.id.skillListId);

            personName.setText(name);
            personEmail.setText(email);
            personPhone.setText(phone);
            personSkill.setText(skill);

        }

    }

}
