package ali.org.rissali.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;


import ali.org.rissali.R;
import ali.org.rissali.databinding.LoginPageBinding;

public class LoginActivity extends BaseActivity {

    private LoginPageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));
        login();
        redirectToSignUp();
        setupPasswordVisibilityToggle();
    }

    private void login() {
        binding.loginBtn.setOnClickListener(v -> {
            String emailInput = binding.emailInput.getText().toString().trim();
            String passwordInput = binding.passwordInput.getText().toString().trim();

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

    private void redirectToSignUp() {
        binding.singUpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void setupPasswordVisibilityToggle() {
        binding.showPassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    binding.passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.showPassword.setImageResource(R.drawable.ic_eye_off);
                } else {
                    binding.passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.showPassword.setImageResource(R.drawable.ic_eye_on);
                }
                isPasswordVisible = !isPasswordVisible;
                binding.passwordInput.setSelection(binding.passwordInput.getText().length());
            }
        });
    }

}
