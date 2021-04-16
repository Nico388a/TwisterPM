package com.example.twisterpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    public void Register(View view) {
        EditText emailView = findViewById(R.id.MainMessageEditEmail);
        EditText passwordView = findViewById(R.id.MainMessageEditPassword);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if ("".equals(email)) {
            emailView.setError("No Email");
            return;
        }

        if ("".equals(password)) {
            passwordView.setError("No Password");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Apple", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "You are now registed.",
                            Toast.LENGTH_LONG).show();
                            Intent intentLogin = new Intent(MainActivity.this, MainMessageActivity.class);
                            startActivity(intentLogin);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Apple", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Login(View view) {
        EditText emailView = findViewById(R.id.MainMessageEditEmail);
        EditText passwordView = findViewById(R.id.MainMessageEditPassword);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if ("".equals(email)) {
            emailView.setError("No Email");
            return;
        }

        if ("".equals(password)) {
            passwordView.setError("No Password");
            return;
        }
        Log.d("Apple", email + password);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Apple", "Sign in");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Apple", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            TextView messageView = findViewById(R.id.MainActicityTextView);
                            messageView.setText("Welcome " + user);
                            Log.d("Apple", "User is Welcome");
                            //updateUI(user);
                            Intent intentLogin = new Intent(MainActivity.this, MainMessageActivity.class);
                            startActivity(intentLogin);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Apple", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT);
                            //updateUI(null);
                            TextView messageView = findViewById(R.id.MainActicityTextView);
                            messageView.setText("Sorry... " + task.getException().getMessage());
                        }
                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("Apple", "Current user: " + currentUser);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_Login:
                Intent intentLogin = new Intent(this, MainActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.action_Messages:
                Intent intentMessages = new Intent(this, MainMessageActivity.class);
                startActivity(intentMessages);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOutUser(View view) {
        Log.d("Apple", "Log Out");
        mAuth.signOut();
    }
}