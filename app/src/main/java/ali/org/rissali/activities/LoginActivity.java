package ali.org.rissali.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.firebase.auth.AuthResult;

import ali.org.rissali.R;

public class LoginActivity extends BaseActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        progressBar = findViewById(R.id.progressBar);

        Button loginBtn = findViewById(R.id.loginBtn);
        login(loginBtn);



        TextView singUpBtn = findViewById(R.id.singUpBtn);
        singUp(singUpBtn);

        //LinearLayout singIbWithGoogle = findViewById(R.id.singIbWithGoogle);

        ImageView showPassword = findViewById(R.id.showPassword);
        EditText passwordField = findViewById(R.id.passwordInput);
        showPassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.ic_eye_off);
                } else {
                    // Show password
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.ic_eye_on);
                }
                isPasswordVisible = !isPasswordVisible;
                passwordField.setSelection(passwordField.getText().length());
            }
        });

    }

    private void login(Button loginBtn){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = findViewById(R.id.emailInput);
                String emailInput = email.getText().toString();
                EditText password = findViewById(R.id.passwordInput);
                String passwordInput = password.getText().toString();

                if(!emailInput.isEmpty() && !passwordInput.isEmpty()){

                    mAuth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        showLoading(true);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "failed to sing in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                    );
                }else{
                    Toast.makeText(LoginActivity.this, "please fill email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loginWithGoogle(){

    }
    private void singUp(TextView singUpBtn){
        singUpBtn.setOnClickListener(v ->{
            Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
            startActivity(intent);
        });
    }
    private void performLogin() {
        // Simulate a delay for login
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            showLoading(false); // Hide the ProgressBar after login
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        }, 3000); // Simulate a 2-second loading time
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
