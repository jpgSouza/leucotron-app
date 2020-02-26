package br.com.leucotron.desafio.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.leucotron.desafio.R;
import br.com.leucotron.desafio.controller.Mask;
import br.com.leucotron.desafio.model.Person;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("Images");

    Person person = new Person();
    Mask mask = new Mask();

    private TextInputEditText nameField;
    private TextInputEditText lastnameField;
    private TextInputEditText phoneNumberField;
    private TextInputEditText emailField;
    private TextInputEditText skillsField;
    private ImageView photoField;

    private String imageURL;
    private Uri imguri;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        mask.phoneMask(phoneNumberField);

        photoField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoUpload();

                delay();
                inputRecovery(person);

                cleanFields();
            }
        });

    }


    public void initComponents(){
        nameField = findViewById(R.id.nameId);
        lastnameField = findViewById(R.id.lastNameId);
        phoneNumberField = findViewById(R.id.phoneNumberId);
        emailField = findViewById(R.id.emailId);
        skillsField = findViewById(R.id.skillsId);
        registerButton = findViewById(R.id.registerButtonId);
        photoField = findViewById(R.id.photoProfileId);
    }

    public void inputRecovery(Person person){
        person.setName(nameField.getText().toString());
        person.setLastname(lastnameField.getText().toString());
        person.setPhoneNumber(phoneNumberField.getText().toString());
        person.setEmail(emailField.getText().toString());
        person.setSkill(skillsField.getText().toString());

    }

    public void cleanFields(){
        nameField.setText("");
        lastnameField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        skillsField.setText("");
        photoField.setImageResource(R.drawable.ic_person);
    }

    public void Filechooser(){
        Intent intent = new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();
            photoField.setImageURI(imguri);

        }

    }

    public String getExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void photoUpload(){
        StorageReference storageReference = mStorageReference.child(System.currentTimeMillis()
                + "." + getExtention(imguri));


        storageReference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageURL = uri.toString();
                                person.setPhotoURL(imageURL);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void delay(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.login_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressDialog.dismiss();
                person.addPerson(person);
            }
        });
    }

}