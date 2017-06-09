package com.talenttakeaways.kristaljewels;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    TextView signupLink;
    Button loginButton;

    //strings
    private String email, password;

    //firebase stuff
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        signupLink = (TextView) findViewById(R.id.signup_link);
        inputEmail = (EditText) findViewById(R.id.login_email);
        inputPassword = (EditText) findViewById(R.id.login_password);

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                if(!validateForm()){return;}
                validateUser(email, password);
            }
        });
    }

    public void validateUser(String email, String password) {
        //get an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //firebase method to signin the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showToast("Logged In Succesfully!");
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        } else {
                            showToast("Login Failed :(");
                        }
                    }
                });
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
    }

    public boolean validateForm() {
        //validate if the data entered by user is valid nor not
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Required");
            return false;
        } else if (!email.contains("@")) {
            inputEmail.setError("Enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Required");
            return false;
        } else if (password.length() < 8) {
            inputPassword.setError("Password should be more than 4");
            return false;
        }
        return true;
    }
}
