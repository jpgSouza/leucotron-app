package br.com.leucotron.desafio.view;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    //Pop Up
    private ProgressDialog progressDialog;

    //Google dashboard infos
    private TextView email;
    private ImageView profilePic;

    //Dashboard components
    private ImageView aboutButton;
    private ImageView registerButton;
    private ImageView curriculumButton;
    private ImageView logoutButton;
    private ImageView searchButton;

    //Logout config
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    //Aux variables for google signIn
    private String googleEmail;
    private String googlePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initComponents();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Receiving infos from another Activity
        Bundle emailBundle = getIntent().getExtras();
        Bundle photoBundle = getIntent().getExtras();
        if(emailBundle != null){
            googleEmail = emailBundle.getString("Email");
        }
        if(photoBundle != null){
            googlePhoto = photoBundle.getString("URL");
        }

        //Showing Google profile photo
        Glide.with(this).load(googlePhoto).into(profilePic);

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

        email.setText(googleEmail);

        //Dashboard components events
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        curriculumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListCurriculumActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogout();
            }
        });

    }

    public void initComponents(){
        aboutButton = findViewById(R.id.aboutSquareId);
        registerButton = findViewById(R.id.registerSquareId);
        curriculumButton = findViewById(R.id.listSquareId);
        email = findViewById(R.id.dashBoardEmailId);
        logoutButton = findViewById(R.id.logoutSquareId);
        searchButton = findViewById(R.id.searchSquareId);
        profilePic = findViewById(R.id.dashboardPictureId);
    }

    public void googleLogout(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

    private void updateUI(FirebaseUser user) {

        if(user != null){

        }else{

            progressDialog = new ProgressDialog(DashboardActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.logout_progress_dialog);
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
                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }

}