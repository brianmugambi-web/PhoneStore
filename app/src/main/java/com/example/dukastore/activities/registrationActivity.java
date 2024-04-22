package com.example.dukastore.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dukastore.R;
import com.example.dukastore.models.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registrationActivity extends AppCompatActivity {

    Button SignUp;
    EditText name,email,password,confirmPassword;
    TextView signIn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;

    SharedPreferences sharedPreferences;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private static final String TAG = "RegisterActivity";



    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressBar=findViewById(R.id.registration_progressbar_id);
        progressBar.setVisibility(View.GONE);

        SignUp=findViewById(R.id.button_reg);
        name=findViewById(R.id.name_reg);
        email=findViewById(R.id.email_reg);
        password=findViewById(R.id.password_id_reg);
        signIn=findViewById(R.id.back_login);
        confirmPassword=findViewById(R.id.confirm_password_id);

        sharedPreferences=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);

        boolean isFirstTime=sharedPreferences.getBoolean("firstTime",true);
        if(isFirstTime){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();

            Intent intent=new Intent(registrationActivity.this,OnBoardingActivity.class);
            startActivity(intent);
            finish();

        }




        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registrationActivity.this, LoginActivity.class));
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=(password.getRight()-password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        togglePasswordVisibility();
                        return true;
                    }

                }
                return false;

            }
        });
        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT=2;
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=(confirmPassword.getRight()-confirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        toggleConfirmPasswordVisibility();
                        return true;
                    }

                }

                return false;
            }
        });

    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_vpn_key_24, 0, R.drawable.eye_icon, 0);
        } else {
            confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_vpn_key_24, 0, R.drawable.eye_icon, 0);
            confirmPassword.setSelection(confirmPassword.getText().length());
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_vpn_key_24, 0, R.drawable.eye_icon, 0);
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT);
            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_vpn_key_24, 0, R.drawable.eye_icon, 0);
            password.setSelection(password.getText().length());
        }
        isPasswordVisible = !isPasswordVisible;
    }


    private void createUser() {

        String userName=name.getText().toString();
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();
        String confirmPass=confirmPassword.getText().toString();

        // Regular expression patterns for email and password validation
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Pattern passwordPattern = Pattern.compile("^.{8,}$");
        Pattern confirmPattern=Pattern.compile("^.{8,}$");
        Pattern namePattern = Pattern.compile("^[a-zA-Z']+(([',. -][a-zA-Z ])?[a-zA-Z']*)*$");

// Matcher objects to match user input with patterns
        Matcher emailMatcher = emailPattern.matcher(userEmail);
        Matcher passwordMatcher = passwordPattern.matcher(userPassword);
        Matcher confirmMatcher=confirmPattern.matcher(confirmPass);
        Matcher nameMatcher = namePattern.matcher(userName);

// Check if name is empty or doesn't match pattern
        if (TextUtils.isEmpty(userName) || !nameMatcher.matches()) {
            if (!userName.matches("^[a-zA-Z']+(([',. -][a-zA-Z ])?[a-zA-Z']*)*$")) {
                Toast.makeText(this, "Invalid name format. Please enter a valid name", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Name field cannot be empty", Toast.LENGTH_SHORT).show();
            }
            return;
        }

// Check if email is empty or doesn't match pattern
        if (TextUtils.isEmpty(userEmail) || !emailMatcher.matches()) {
            if (!userEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                Toast.makeText(this, "Invalid email format. Please enter a valid email address", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Email field cannot be empty", Toast.LENGTH_SHORT).show();
            }
            return;
        }
// Check if password is empty or doesn't match pattern
        if (TextUtils.isEmpty(userPassword) || !passwordMatcher.matches()) {
            if (!userPassword.matches(".*[A-Z].*")) {
                Toast.makeText(this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
            } else if (!userPassword.matches(".*[a-z].*")) {
                Toast.makeText(this, "Password must contain at least one lowercase letter", Toast.LENGTH_SHORT).show();
            } else if (!userPassword.matches(".*\\d.*")) {
                Toast.makeText(this, "Password must contain at least one numeric digit", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (!confirmMatcher.matches() || !confirmPass.equals(userPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }



        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            userModel uModel=new userModel(userName,userEmail,userPassword);
                            String id=task.getResult().getUser().getUid();
                            database.getReference().child("users").child(id).setValue(uModel);
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(registrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(registrationActivity.this,LoginActivity.class));
                        }else{
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(registrationActivity.this,"registration failed ,try again,Try registering with a different email",Toast.LENGTH_SHORT).show();
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                email.setError("your email is invalid or already in use.Kindly use another email");
                                email.requestFocus();
                                progressBar.setVisibility(View.GONE);


                            }catch(FirebaseAuthUserCollisionException e){
                                email.setError("User is already registered with this email.Use another email");
                                email.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            }catch(Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(registrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }
                });

    }
}