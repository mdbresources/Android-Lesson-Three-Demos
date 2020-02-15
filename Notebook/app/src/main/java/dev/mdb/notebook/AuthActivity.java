package dev.mdb.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtEmail;
    TextView txtPassword;

    Button btnLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btnLogin:
                login(email, password);
                break;
            case R.id.btnSignUp:
                signup(email, password);
                break;
        }
    }

    private void signup(String email, String password) {
        // Firebase will thrown an exception if passed in an empty or null string, so
        // we should check this before calling the firebase function.
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AuthActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If the sign up was successful, then go to the main activity.
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If the sign up was not successful, then display a toast saying
                            // that it failed, and remain on this screen.
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void login(String email, String password) {
        // Firebase will thrown an exception if passed in an empty or null string, so
        // we should check this before calling the firebase function.
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AuthActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Call the firebase login function.
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If the login was successful, then go to the main activity.
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If the login was not successful, then display a toast saying
                            // that it failed, and remain on this screen.
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
