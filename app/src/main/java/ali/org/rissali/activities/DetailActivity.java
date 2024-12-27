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

public class DetailActivity extends BaseActivity {

    private Foods object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ImageView backBtn = findViewById(R.id.backDetBtn);
        TextView titleTxt = findViewById(R.id.titleTxtDet);
        TextView ratting = findViewById(R.id.rateTxt);
        ImageView picDet = findViewById(R.id.picDet);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView price = findViewById(R.id.priceTxtDet);
        TextView timeTxt = findViewById(R.id.timeDetTxt);
        TextView quantity = findViewById(R.id.numTxt);
        TextView total = findViewById(R.id.totalTxt);
        TextView description = findViewById(R.id.description);
        getIntentExtra();
        setVariable(backBtn, titleTxt, ratting, price, ratingBar,timeTxt, picDet, description);

    }

    private void setVariable(ImageView backBtn, TextView titleTxt, TextView ratting,TextView price, RatingBar ratingBar, TextView time, ImageView picDet, TextView description) {
        backBtn.setOnClickListener(v -> finish());
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(picDet);
        titleTxt.setText(object.getTitle());
        price.setText("$"+object.getPrice());
        time.setText(object.getTimeValue()+" min");
        description.setText(object.getDescription());
        ratting.setText(object.getStar() + "Rating");
        ratingBar.setRating((float)object.getStar());

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}