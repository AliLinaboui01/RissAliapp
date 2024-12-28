package ali.org.rissali.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ali.org.rissali.R;
import ali.org.rissali.databinding.LandingPageBinding;

public class MainActivity extends BaseActivity {

    private LandingPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));

        login();
        signup();

    }

    private void signup() {
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to SignUpActivity if the user clicks on Sign Up
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        binding.loginButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                // If the user is logged in, go to HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                // If the user is not logged in, go to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
