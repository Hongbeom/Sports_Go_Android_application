package com.example.mcbback.sportgo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private EditText etName;
    private RadioGroup rgSex;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText etEmail;
    private EditText etPhone;
    private Spinner etCity;
    private Spinner etGu;
    private Button signUpBtn;
    private Button cancelBtn;
    private Button idCheck;
    private DatabaseReference mDatabase;
    private boolean rightId;

    ArrayAdapter<CharSequence> add_city, add_gu;
    Login loginClass = (Login)Login.LoginActivity;
    String email = null;
    String pw = null;
    String name = null;
    String phone = null;
    String city = null;
    String gu = null;
    String sex = null;
    String uid = null;


    private void writeNewUser(String userId){
        try {
            Profile profile = new Profile(email, name, sex, phone, city, gu, "");

            mDatabase.child("users").child(userId).setValue(profile);
            Toast.makeText(Register.this,"Successful Sign Up!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Category.class);
            startActivity(intent);
            loginClass.finish();
            finish();
        }catch (Exception e){
            Toast.makeText(Register.this,""+e, Toast.LENGTH_LONG).show();

        }
    }

    private void signUp(String email, String pw){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });


    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfrim);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCity = (Spinner) findViewById(R.id.register_city);
        etGu = (Spinner) findViewById(R.id.register_gu);
        etName = (EditText) findViewById(R.id.etName);
        rgSex = (RadioGroup) findViewById(R.id.rgSex);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        etEmail = (EditText) findViewById(R.id.etEmail);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        idCheck = (Button) findViewById(R.id.idCheck);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // 비밀번호 일치 검사
        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = etPassword.getText().toString();
                String confirm = etPasswordConfirm.getText().toString();

                if( password.equals(confirm) ) {
                    etPassword.setBackgroundColor(Color.GREEN);
                    etPasswordConfirm.setBackgroundColor(Color.GREEN);
                } else {
                    etPassword.setBackgroundColor(Color.RED);
                    etPasswordConfirm.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add_city = ArrayAdapter.createFromResource(this, R.array.spinner_city, android.R.layout.simple_spinner_dropdown_item);

        etCity.setAdapter(add_city);
        etCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (add_city.getItem(i).equals("Seoul")) {
                    city = "Seoul";
                    add_gu = ArrayAdapter.createFromResource(Register.this, R.array.spinner_city_seoul, android.R.layout.simple_spinner_dropdown_item);
                    add_gu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    etGu.setAdapter(add_gu);
                    etGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int j, long id) {
                            gu = add_gu.getItem(j).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (add_city.getItem(i).equals("Incheon")) {
                    city = "Incheon";
                    add_gu = ArrayAdapter.createFromResource(Register.this, R.array.spinner_city_incheon, android.R.layout.simple_spinner_dropdown_item);
                    add_gu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    etGu.setAdapter(add_gu);
                    etGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            gu = add_gu.getItem(i).toString();
                        }
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                pw = etPassword.getText().toString().trim();
                name = etName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();



                sex = null;
                if(rbFemale.isChecked()){
                    sex = "Female";
                }else if(rbMale.isChecked()){
                    sex="Male";
                }else{
                    sex = null;
                }
                if(!rightId){
                    Toast.makeText(Register.this, "Please, Check the email for the duplication", Toast.LENGTH_SHORT).show();
                    idCheck.requestFocus();
                    return;
                }
                // 이름 확인
                if( etName.getText().toString().length() == 0 ) {
                    Toast.makeText(Register.this, "Email 을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }

                // 성별 확인
                if( sex == null ) {
                    Toast.makeText(Register.this, "Sex를 입력하세요!", Toast.LENGTH_SHORT).show();
                    rgSex.requestFocus();
                    return;
                }

                // 폰 입력 확인
                if( etPhone.getText().toString().length() == 0 ) {
                    Toast.makeText(Register.this, "PhoneNumber를 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPhone.requestFocus();
                    return;
                }


                // 이메일
                if( etEmail.getText().toString().length() == 0 ) {
                    Toast.makeText(Register.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    return;
                }

                // 비밀번호
                if( etPassword.getText().toString().length() == 0 ) {
                    Toast.makeText(Register.this, "passwoar를 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }

                // 비밀번호 확인
                if( etPasswordConfirm.getText().toString().length() == 0 ) {
                    Toast.makeText(Register.this, "password confirm을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPasswordConfirm.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()) ) {
                    Toast.makeText(Register.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etPasswordConfirm.setText("");
                    etPassword.requestFocus();
                    return;
                }

                SignUpProcess task = new SignUpProcess();
                task.execute(email, pw);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        idCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                if( etEmail.getText().toString().length() == 0 ) {
                    Toast.makeText(Register.this, "Email 을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etName.requestFocus();
                    return;
                }
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Profile tmp = snapshot.getValue(Profile.class);
                            if(tmp.email.equals(email) ){
                                Toast.makeText(Register.this, "Duplicated Email! Please, use other email address!", Toast.LENGTH_SHORT).show();
                                rightId = false;
                                return;
                            }

                        }
                        rightId = true;
                        Toast.makeText(Register.this, "Good Email", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
    private class SignUpProcess extends AsyncTask<String, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                Register.this);

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
                signUp(args[0], args[1]);
            } catch (Exception e) {
                Toast.makeText(Register.this,""+e,Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }







}
