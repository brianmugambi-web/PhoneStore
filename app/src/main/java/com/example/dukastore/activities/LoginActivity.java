package com.example.dukastore.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dukastore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText email,password;
    TextView signIn;
    FirebaseAuth auth;

    ProgressBar progressBar;
    TextView forgotPassword;
    private boolean isPasswordVisible = false;
    private static final String TAG = "LoginActivity";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.GONE);
        email=findViewById(R.id.email_id_login);
        password=findViewById(R.id.password_id_login);
        signIn=findViewById(R.id.back_login);
        loginBtn=findViewById(R.id.button_reg);
        forgotPassword=findViewById(R.id.forgot_password);


        signIn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, registrationActivity.class)));

        loginBtn.setOnClickListener(v -> {
           loginUser();
           progressBar.setVisibility(View.VISIBLE);
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

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox1=dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);

                AlertDialog dialog=builder.create();

                dialogView.findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String userEmail=emailBox1.getText().toString();

                        if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(LoginActivity.this,"Enter your registered email id",Toast.LENGTH_SHORT).show();

                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Check Your email",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(LoginActivity.this,"Unable to send ,failed",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                });
                dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow()!=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();;

            }
        });

    }


    private void togglePasswordVisibility() {
        if(isPasswordVisible){
            password.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible=false;
            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_vpn_key_24, 0, R.drawable.ic_icon, 0);
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT);
            isPasswordVisible=true;
            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_vpn_key_24,0,R.drawable.ic_icon,0);
            password.setSelection(password.getText().length());
        }
    }


    private void loginUser() {

        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();


// Regular expression patterns for email and password validation
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");

        // Matcher objects to match user input with patterns
        Matcher emailMatcher = emailPattern.matcher(userEmail);
        Matcher passwordMatcher = passwordPattern.matcher(userPassword);


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


//        login user

        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         Toast.makeText(LoginActivity.this,"Login successfully",Toast.LENGTH_SHORT).show();
                         progressBar.setVisibility(View.GONE);
                         startActivity(new Intent(LoginActivity.this, MainActivity.class));
                     }else{
//                         progressBar.setVisibility(View.GONE);
//                         Toast.makeText(LoginActivity.this,"Error"+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                         try{
                             throw task.getException();

                         }catch (FirebaseAuthInvalidUserException e){
                             email.setError("User does not exist or is no longer valid.Please register again");
                             email.requestFocus();
                             progressBar.setVisibility(View.GONE);
                         }catch(FirebaseAuthInvalidCredentialsException e){
                             email.setError("Invalid credentials.Kindly ,check and re-enter.");
                             email.requestFocus();
                             progressBar.setVisibility(View.GONE);
                         }catch(Exception e){
                             Log.e(TAG,e.getMessage());
                             Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                             progressBar.setVisibility(View.GONE);

                         }
                     }
                    }
                });
    }
}