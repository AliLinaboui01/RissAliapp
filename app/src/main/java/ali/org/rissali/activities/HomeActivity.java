package ali.org.rissali.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ali.org.rissali.R;

public class HomeActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
    }
}
