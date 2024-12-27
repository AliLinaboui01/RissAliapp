package ali.org.rissali.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import ali.org.rissali.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));

        Button loginBtn = findViewById(R.id.loginBtn);
        TextView signUpBtn = findViewById(R.id.singUpBtn);
        ImageView showPassword = findViewById(R.id.showPassword);
        EditText passwordField = findViewById(R.id.passwordInput);

        login(loginBtn);
        redirectToSignUp(signUpBtn);
        setupPasswordVisibilityToggle(showPassword, passwordField);
    }

    private void login(Button loginBtn) {
        loginBtn.setOnClickListener(v -> {
            EditText email = findViewById(R.id.emailInput);
            EditText password = findViewById(R.id.passwordInput);

            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in email and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            if (firebaseUser != null) {
//                                fetchUserInfo(firebaseUser.getUid());
                            } else {
                                Toast.makeText(LoginActivity.this, "User authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Login failed: " + task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Failed to sign in: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void redirectToSignUp(TextView signUpBtn) {
        signUpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void setupPasswordVisibilityToggle(ImageView showPassword, EditText passwordField) {
        showPassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.ic_eye_off);
                } else {
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.ic_eye_on);
                }
                isPasswordVisible = !isPasswordVisible;
                passwordField.setSelection(passwordField.getText().length());
            }
        });
    }

//    private void fetchUserInfo(String userId) {
//        databaseReference.child(userId).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult().exists()) {
//                DataSnapshot snapshot = task.getResult();
//                String username = snapshot.child("username").getValue(String.class);
//                String email = snapshot.child("email").getValue(String.class);
//
//                // Redirect to the main activity with user data
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("username", username);
//                intent.putExtra("email", email);
//                startActivity(intent);
//                finish();
//
//                Log.i(TAG, "User data retrieved successfully: " + username + ", " + email);
//            } else {
//                Log.e(TAG, "Error fetching user data: " + task.getException());
//                Toast.makeText(LoginActivity.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
