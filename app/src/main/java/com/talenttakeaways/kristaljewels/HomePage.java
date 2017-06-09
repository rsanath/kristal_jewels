package com.talenttakeaways.kristaljewels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by sanath on 09/06/17.
 */

public class HomePage extends AppCompatActivity {
    Button logoutButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        logoutButton = (Button) findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                Toast.makeText(getApplicationContext(), "Logging you out..", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                finish();
            }
        });

    }
}
