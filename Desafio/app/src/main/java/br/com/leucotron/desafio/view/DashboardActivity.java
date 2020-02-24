package br.com.leucotron.desafio.view;

import androidx.appcompat.app.AppCompatActivity;
import br.com.leucotron.desafio.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DashboardActivity extends AppCompatActivity {

    private ImageView aboutButton;
    private ImageView registerButton;
    private ImageView curriculumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initComponents();

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

    }



    public void initComponents(){
        aboutButton = findViewById(R.id.aboutSquareId);
        registerButton = findViewById(R.id.registerSquareId);
        curriculumButton = findViewById(R.id.listSquareId);
    }
}
