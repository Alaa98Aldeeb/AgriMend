package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import maes.tech.intentanim.CustomIntent;

public class login_page extends AppCompatActivity {

    EditText editText1, editText2;
    Button btn;
    private FirebaseAuth mAuth;
    LoadingDialog loadingDialog;
    TextView register, resetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        editText1 = (EditText)findViewById(R.id.email);
        editText2 = (EditText)findViewById(R.id.pass);
        btn = (Button)findViewById(R.id.login);
        register = findViewById(R.id.createAccount);
        resetPass = findViewById(R.id.forgetPass);
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(login_page.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_page.this, signup_page.class));
                login_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_page.this, resetPassword.class));
                login_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = editText1.getText().toString();
                final String password = editText2.getText().toString();

                if(mail.isEmpty() || password.isEmpty()){
                    showMessage("Please verify all fields");

                }else{
                    signIn(mail, password);
                }
            }
        });
    }

    private void signIn(String mail, String password) {

        loadingDialog.startLoadingDialog();
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateUI();

                }else{
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    private void updateUI() {

        Intent home = new Intent(getApplicationContext(),home_news.class);
        loadingDialog.dismissDialog();
        startActivity(home);
        login_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
