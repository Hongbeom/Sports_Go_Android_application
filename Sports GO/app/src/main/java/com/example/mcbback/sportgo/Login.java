package com.example.mcbback.sportgo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    public static Activity LoginActivity;

    private Button btnRegist;
    private Button btnLogin;
    private EditText etUserEmail;
    private EditText etUserPw;
    private FirebaseAuth mAuth;
    private static final String TAG = Login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getApplicationContext(), Category.class);
            //Intent intent = new Intent(getApplicationContext(), Sample.class);
            startActivity(intent);
            finish();
        }
        LoginActivity = Login.this;
        btnRegist = (Button) findViewById(R.id.signupButton);
        btnLogin = (Button) findViewById(R.id.loginButton);
        etUserEmail = (EditText) findViewById(R.id.userEmail);
        etUserPw = (EditText) findViewById(R.id.userPassword);


        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = etUserEmail.getText().toString().trim();
                String userPw = etUserPw.getText().toString().trim();

                if(userEmail.length()==0){
                    Toast.makeText(Login.this, "Please, check the email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userPw.length()==0){
                    Toast.makeText(Login.this, "Please, check the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginProcess task = new LoginProcess();
                task.execute(userEmail, userPw);


            }
        });

    }
    private void signIn(String email, String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            logInSuccess();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed. Please check your account.",
                                    Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    }
                });
    }

    private void logInSuccess(){
        Intent intent = new Intent(getApplicationContext(), Category.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure to exit?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }
    private class LoginProcess extends AsyncTask<String, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Login.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Please wait...");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... args) {
            try {
                signIn(args[0], args[1]);
            } catch (Exception e) {
                Toast.makeText(Login.this,""+e,Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            asyncDialog.dismiss();
        }
    }



}
