package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgobacktologin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton);
        mgobacktologin=findViewById(R.id.gobacktologin);

        firebaseAuth= FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Your Email First",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Recovery Email Sent. Use that Email to Recove Your Password",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotpassword.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Email is Wrong or Account Does Not Exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}