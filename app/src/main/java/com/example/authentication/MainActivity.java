package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mFirstName,mMiddleName,mLastName,mEmail,mPassword,mPhone,mNativePlace;
    Button mSignUpBtn,mlogin;
    TextView mDate;
    ProgressBar mProgressBar;

    Calendar myCalendar = Calendar.getInstance();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mSignUpBtn=findViewById(R.id.signup);
        mSignUpBtn.setOnClickListener(this);


        mEmail=findViewById(R.id.email);
        mFirstName=findViewById(R.id.firstName);
        mMiddleName=findViewById(R.id.middleName);
        mLastName=findViewById(R.id.lastName);
        mPhone=findViewById(R.id.editTextPhone);
        mNativePlace=findViewById(R.id.nativePlace);
        mPassword=findViewById(R.id.pass);
        mProgressBar=findViewById(R.id.progressBar);
        mlogin=findViewById(R.id.login);
        mDate=findViewById(R.id.editTextDate);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });




        DatePickerDialog.OnDateSetListener date = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };
        mDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(MainActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });

    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onClick(View v) {

        final String email = mEmail.getText().toString().trim();
        final String firstName = mFirstName.getText().toString();
        final String middleName = mMiddleName.getText().toString();
        final String lastName = mLastName.getText().toString();
        final String phone    = mPhone.getText().toString();
        final String nativePlace    = mNativePlace.getText().toString();
        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(firstName)){
            mFirstName.setError("First Name is Required.");
            return;
        }
        if(TextUtils.isEmpty(lastName)){
            mLastName.setError("Last Name is Required.");
            return;
        }
        if(TextUtils.isEmpty(phone)){
            mPhone.setError("Phone Number is Required.");
            return;
        }
        if(TextUtils.isEmpty(nativePlace)){
            mNativePlace.setError("Details are Required.");
            return;
        }
        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is Required.");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please provide vaild email");
            return;
        }
        if(password.length() < 6){
            mPassword.setError("Password Must be >= 6 Characters");
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this,home.class));
                    User user = new User(email, firstName, middleName, lastName, nativePlace);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "User has been registered successfully.", Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to register.", Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Failed to register.", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }

        });

}

    }


