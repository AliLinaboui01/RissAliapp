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
import androidx.appcompat.app.AppCompatActivity;

import ali.org.rissali.R;

public class SingUpActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_page);
        Button singUpBtn = findViewById(R.id.btnSignUp);
        singUp(singUpBtn);
        TextView haveAccount = findViewById(R.id.haveAccount);
        login(haveAccount);

        ImageView showPassword = findViewById(R.id.showPassword);
        EditText passwordField = findViewById(R.id.inputPassword);
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

    private void singUp(Button singUpBtn){
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = findViewById(R.id.inputUsername);
                String inputUsername = usernameEditText.getText().toString();
                EditText emailEditText = findViewById(R.id.inputEmail);
                EditText passwordEditText = findViewById(R.id.inputPassword);
                String inputEmail = emailEditText.getText().toString();
                String inputPassword = passwordEditText.getText().toString();
                if(!isValidEmail(inputEmail) ){
                    Toast.makeText(SingUpActivity.this, "you need to enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(inputPassword.length() < 6 ){
                    Toast.makeText(SingUpActivity.this, "the password must be 6 character", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener(SingUpActivity.this , task ->{
                    if (task.isSuccessful()){
                        Log.i(TAG , "OnComplete");
                        Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Log.e(TAG , "failed");
                        Toast.makeText(SingUpActivity.this, "Failed To Authenticate!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private boolean isValidEmail(String inputEmail) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return inputEmail.matches(emailPattern);
    }
    private void login(TextView haveAccount){
        haveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
