package com.hw.firebase_practice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.hw.firebase_practice.FBRef;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.play.core.integrity.t;
import com.hw.firebase_practice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    EditText eTMail, eTPass;
    TextView tVMsg;
    Button btnCreateUser;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        init();
        setContentView(binding.getRoot());
    }
    private void init()
    {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        eTMail = binding.eTMail;
        eTPass = binding.eTPass;
        tVMsg = binding.tVMsg;
        btnCreateUser = binding.btnCreateUser;
        btnCreateUser.setOnClickListener(view -> createUser(view));
    }
    public void createUser(View view)
    {
        String email = eTMail.getText().toString();
        String pass = eTPass.getText().toString();
        if(email.isEmpty() || pass.isEmpty())
        {
            tVMsg.setText("Please fill all fields");
        }
        else
        {
            Log.i("MainActivity","mail: " + email + " pass: " + pass);
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Connecting");
            pd.setMessage("Creating user");
            pd.show();
            System.out.println("mail: " + email + " pass: " + pass);
            FBRef.refAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                Log.i("MainActivity", "createUserWithEmailAndPassword: success");
                                FirebaseUser user = FBRef.refAuth.getCurrentUser();
                                tVMsg.setText("User created successfuly\nUid: " + user.getUid());
                            }
                            else
                            {
                                Exception exp = task.getException();
                                if (exp instanceof FirebaseAuthInvalidUserException) {
                                    tVMsg.setText("Invalid email address.");
                                } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                                    tVMsg.setText("Password too weak.");
                                } else if (exp instanceof FirebaseAuthUserCollisionException) {
                                    tVMsg.setText("User already exists.");
                                } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                    tVMsg.setText("General authentication failure.");
                                } else if (exp instanceof FirebaseNetworkException) {
                                    tVMsg.setText("Network error. Please check your connection.");
                                } else {
                                    tVMsg.setText("An error occurred. Please try again later.");
                                }
                            }
                        }
                    });
        }
    }
}