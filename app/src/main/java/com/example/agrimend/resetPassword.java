package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import maes.tech.intentanim.CustomIntent;

public class resetPassword extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetPassword.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    EditText emailAddress;
    Button send;
    FirebaseAuth mAuth;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailAddress = findViewById(R.id.emailTxt);
        send = findViewById(R.id.sendReset);
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(resetPassword.this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAddress.getText().toString();
                loadingDialog.startLoadingDialog();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loadingDialog.dismissDialog();
                            Toast.makeText(resetPassword.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(resetPassword.this,login_page.class));
                            CustomIntent.customType(resetPassword.this, "left-to-right");
                        }
                    }
                });
            }
        });
    }
}
