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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talenttakeaways.kristaljewels.beans.User;


public class SignupActivity extends AppCompatActivity
{
    EditText email,number,password,name;
    TextView loginLink;
    Button registerButton;
    ProgressDialog pd;

    //Firebase stuff
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;

    //user entered data
    String userName, userEmail, userPassword, userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);

        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        //register button and login link
        loginLink = (TextView) findViewById(R.id.login_link);
        registerButton = (Button) findViewById(R.id.signup_button);

        //User entered details
        name = (EditText) findViewById(R.id.usrusr);
        password = (EditText) findViewById(R.id.pswrdd);
        email = (EditText) findViewById(R.id.mail);
        number = (EditText) findViewById(R.id.mobphone);

        //action for register button
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //show the ProgressDialog
                pd.setMessage("Registering..");
                pd.setCanceledOnTouchOutside(false);

                //get the text entered by user
                userName = name.getText().toString().trim();
                userEmail = email.getText().toString().trim();
                userPassword = password.getText().toString().trim();
                userNumber = number.getText().toString().trim();

                //validate user inputs
                if(!validateForm()){return;}
                pd.show();

                createUser(userEmail, userPassword);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    public void createUser(String email, String password) {
        // creates a user using email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserDetails(mAuth.getCurrentUser());
                        } else {
                            pd.dismiss();
                            showToast("Registration Failed !");
                        }
                    }
                });
    }

    public void addUserDetails(FirebaseUser user) {
        // This function gets the Uid of the created user and created a new entry
        // in the databse under the Uid of the user
        String userId = user.getUid();  //gets the Uid
        dbReference = FirebaseDatabase.getInstance().getReference();

        //use the container class to hold the values entered by the user
        User myUser = new User(userName, userEmail, userPassword, userNumber, "false");

        // what happens here is we get a reference to the database/users/'usesrId'
        //then we add the user details under that structure
        dbReference.child("users").child(userId).setValue(myUser).
                addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //dismiss the progress dialog
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            //If user is added to database
                            showToast("Registration Successful!");
                            finish();
                            startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                        } else {
                            //if it fails
                            showToast("Registration Failed!");
                        }
                    }
                });
    }

    //performs validation on user data and returns a boolean
    public boolean validateForm() {
        //validate if the data entered by user is valid nor not
        if (TextUtils.isEmpty(userName)) {
            name.setError("Required");
            return false;
        }
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Required");
            return false;
        } else if (!userEmail.contains("@")) {
            email.setError("Enter a valid email");
            return false;
        }
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Required");
            return false;
        } else if (userPassword.length() < 8) {
            password.setError("Minimum 8 characters");
            return false;
        }
        if (TextUtils.isEmpty(userNumber)) {
            number.setError("Required");
            return false;
        }
        return true;
    }

    public void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
