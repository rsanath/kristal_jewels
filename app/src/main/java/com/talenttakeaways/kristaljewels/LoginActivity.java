package com.talenttakeaways.kristaljewels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talenttakeaways.kristaljewels.beans.User;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;
import com.talenttakeaways.kristaljewels.others.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.talenttakeaways.kristaljewels.others.CommonFunctions.getDismissDialog;
import static com.talenttakeaways.kristaljewels.others.CommonFunctions.showToast;

public class LoginActivity extends AppCompatActivity {
    Activity context = this;

    @BindView(R.id.forgot_password_link) TextView forgotPassLink;
    @BindView(R.id.login_button) Button loginButton;
    @BindView(R.id.signup_link) TextView signupLink;
    @BindView(R.id.login_email) EditText inputEmail;
    @BindView(R.id.login_password) EditText inputPassword;
    MaterialDialog loading;

    //strings
    private String email, password;

    //firebase stuff
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(context);

        mAuth = FirebaseAuth.getInstance();
        loading = CommonFunctions.getLoadingDialog(context, R.string.loading,R.string.please_wait).build();

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
                loading.show();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                if (!validateForm()) {
                    loading.dismiss();
                    return;
                }
                validateUser(email, password);
            }
        });

        forgotPassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetLink();
            }
        });
    }

    public void validateUser(String email, String password) {
        //firebase method to signin the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserDetails(mAuth);
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            loading.dismiss();
                            showToast(context, getString(R.string.login_fail_message));
                        }
                    }
                });
    }

    public void saveUserDetails(FirebaseAuth mAuth) {
        final String uId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference(Constants.users).child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        CommonFunctions.setCurrentUser(context, user);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void sendPasswordResetLink() {
        new MaterialDialog.Builder(this)
                .title(R.string.forgot_pass_title)
                .content(R.string.forgot_pass_content)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input("Email Id", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        loading.show();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(input.toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loading.dismiss();
                                        if (task.isSuccessful()) {

                                            getDismissDialog(context, R.string.success,
                                                    R.string.reset_email_sent_content).show();
                                        } else {
                                            showToast(context, getString(R.string.try_later));
                                        }
                                    }
                                });
                    }
                }).show();
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
