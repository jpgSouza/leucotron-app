package br.com.leucotron.desafio.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.leucotron.desafio.R;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference credentialsReference = databaseReference.child("credentials");

    private TextInputEditText username;
    private TextInputEditText password;

    private Button loginButton;
    private Button loginWithGoogleButton;

    static final int GOOGLE_SIGN = 123;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        loginWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }



    }

    private void initComponents(){
        loginButton = findViewById(R.id.loginButtonId);
        loginWithGoogleButton = findViewById(R.id.googleButtonId);
        username = findViewById(R.id.loginId);
        password = findViewById(R.id.passwordId);
    }

    private void signInGoogle(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account);
                }

            }catch (ApiException e){
                e.printStackTrace();
            }
        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG:", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(),null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,task-> {
                    if(task.isSuccessful()){
                        Log.d("TAG", "signin sucess");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                        progressDialog = new ProgressDialog(LoginActivity.this);
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
                        pdCanceller.postDelayed(progressRunnable, 2000);

                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Email", email);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }else{
                        Log.w("TAG", "signin failure", task.getException());
                        Toast.makeText(this, "Signin Failed!", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });

    }

    private void updateUI(FirebaseUser user) {

        if(user != null){
            String name = user.getDisplayName();
            email = user.getEmail();

        }else{

        }

    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

    private void signin(){
        credentialsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        if(objSnapshot.child("username").getValue().toString().equals(username.getText().toString())
                                && objSnapshot.child("password").getValue().toString().equals(password.getText().toString())){
                            progressDialog = new ProgressDialog(LoginActivity.this);
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
                            pdCanceller.postDelayed(progressRunnable, 2000);

                            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
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