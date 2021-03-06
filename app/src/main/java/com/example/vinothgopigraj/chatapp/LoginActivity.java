package com.example.vinothgopigraj.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail,loginPassword;
    private Button loginButton,signupButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView loginWithPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = (EditText) findViewById(R.id.emailLoginText);
        loginPassword = (EditText) findViewById(R.id.passwordLoginText);
        loginButton = (Button) findViewById(R.id.loginLoginButton);
        signupButton = (Button) findViewById(R.id.registerLoginButton);
        loginWithPhone = (TextView)findViewById(R.id.loginWithPhone);

        loginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this,LoginWithPhone.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    public void loginButtonClickedLogin(View view){
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && ! TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        checkUserExists();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Wrong email or password",Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }
    public void checkUserExists(){
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    Intent loginIntent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
