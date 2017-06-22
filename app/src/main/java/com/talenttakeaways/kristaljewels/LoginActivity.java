package com.talenttakeaways.kristaljewels;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talenttakeaways.kristaljewels.beans.User;

public class LoginActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    TextView signupLink;
    Button loginButton;
    ProgressDialog pd;

    //strings
    private String email, password;

    //firebase stuff
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
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
                //show the progress dialog
                pd.setMessage("Logging In..");
                pd.setCancelable(false);
                pd.show();

                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                if(!validateForm()){
                    pd.dismiss();
                    return;
                }
                validateUser(email, password);
            }
        });
    }

    public void validateUser(String email, String password) {
        //firebase method to signin the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            showToast("Login Failed :(");
                        }
                    }
                });
    }

    public void isAdmin(FirebaseAuth mAuth){
        final boolean[] re = new boolean[1];
        String uId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
                db.getReference("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userDetails : dataSnapshot.getChildren()) {
                            User user = userDetails.getValue(User.class);
                            showToast(user.isAdmin);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
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
