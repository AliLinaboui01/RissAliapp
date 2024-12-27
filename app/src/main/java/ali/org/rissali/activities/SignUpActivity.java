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
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import ali.org.rissali.Domain.User;
import ali.org.rissali.R;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_page); // Corrected layout name if needed
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));

        Button signUpBtn = findViewById(R.id.btnSignUp);
        TextView haveAccount = findViewById(R.id.haveAccount);
        ImageView showPassword = findViewById(R.id.showPassword);
        EditText passwordField = findViewById(R.id.inputPassword);

        signUp(signUpBtn);
        setupLoginRedirect(haveAccount);
        setupPasswordVisibilityToggle(showPassword, passwordField);
    }

    private void signUp(Button signUpBtn) {
        signUpBtn.setOnClickListener(v -> {
            EditText usernameEditText = findViewById(R.id.inputUsername);
            EditText emailEditText = findViewById(R.id.inputEmail);
            EditText passwordEditText = findViewById(R.id.inputPassword);

            String inputUsername = usernameEditText.getText().toString().trim();
            String inputEmail = emailEditText.getText().toString().trim();
            String inputPassword = passwordEditText.getText().toString().trim();

            if (inputUsername.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Username is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(inputEmail)) {
                Toast.makeText(SignUpActivity.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (inputPassword.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            if (firebaseUser != null) {
                                // Set the display name
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(inputUsername) // Set the username as display name
                                        .build();

                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                Log.i(TAG, "Display name updated");
                                                Toast.makeText(SignUpActivity.this,
                                                        "Registration successful! Please verify your email.",
                                                        Toast.LENGTH_LONG).show();

                                                // Redirect to login
                                                redirectToLogin();
                                            } else {
                                                Log.e(TAG, "Failed to update display name: " + profileTask.getException().getMessage());
                                                Toast.makeText(SignUpActivity.this,
                                                        "Failed to update display name.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                // Send email verification
                                firebaseUser.sendEmailVerification()
                                        .addOnCompleteListener(verificationTask -> {
                                            if (verificationTask.isSuccessful()) {
                                                Log.i(TAG, "Email verification sent");
                                            } else {
                                                Log.e(TAG, "Email verification failed: " + verificationTask.getException().getMessage());
                                            }
                                        });
                            }
                        } else {
                            Log.e(TAG, "Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage());
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to register user: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }


    private boolean isValidEmail(String inputEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return inputEmail.matches(emailPattern);
    }

    private void setupLoginRedirect(TextView haveAccount) {
        haveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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

    private void redirectToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
