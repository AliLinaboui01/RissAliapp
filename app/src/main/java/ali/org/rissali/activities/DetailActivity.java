package ali.org.rissali.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import ali.org.rissali.Domain.Foods;
import ali.org.rissali.R;
import ali.org.rissali.databinding.ActivityDetailBinding;
import ali.org.rissali.helpers.ManagementCart;

public class DetailActivity extends BaseActivity {

    private ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));

        getIntentExtra();
        setVariable();

    }

    private void setVariable() {
        managementCart = new ManagementCart(this);
        binding.backDetBtn.setOnClickListener(v -> finish());
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.picDet);
        binding.titleTxtDet.setText(object.getTitle());
        binding.priceTxtDet.setText("$"+object.getPrice());
        binding.timeDetTxt.setText(object.getTimeValue()+" min");
        binding.description.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + "Rating");
        binding.ratingBar.setRating((float)object.getStar());

        binding.plusBtn.setOnClickListener(v -> {
            num++;
            binding.numTxt.setText(num+ " ");
            binding.totalTxt.setText("$" +num * object.getPrice());
        });
        binding.minusBtn.setOnClickListener(v -> {
            if(num > 1){
                num--;
                binding.numTxt.setText(num+ " ");
                binding.totalTxt.setText("$" +num * object.getPrice());
            }

        });
        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managementCart.insertFood(object);
        });

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}