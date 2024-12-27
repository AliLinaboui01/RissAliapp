package ali.org.rissali.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ali.org.rissali.Domain.Foods;
import ali.org.rissali.R;
import ali.org.rissali.adapter.FoodListAdapter;

public class ListFoodActivity extends BaseActivity {


    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_food);

        getIntentExtra();
        ProgressBar progressBar = findViewById(R.id.progressBarListFood);
        RecyclerView foodListView = findViewById(R.id.foodListView);
        TextView title = findViewById(R.id.titleTxListFood);
        ImageView img = findViewById(R.id.backBtn);
        initList(progressBar, foodListView , title, img);
    }

    private void initList(ProgressBar progressBar, RecyclerView foodListView, TextView title, ImageView back) {
        DatabaseReference myRef = database.getReference("Foods");
        progressBar.setVisibility(View.VISIBLE);
        title.setText(categoryName);
        back.setOnClickListener( v -> finish());
        ArrayList<Foods> list = new ArrayList<>();
        Query query;
        if(isSearch){
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');
        }else{
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                }
                if(!list.isEmpty()){
                    foodListView.setLayoutManager(new GridLayoutManager(ListFoodActivity.this, 2));
                    adapterListFood = new FoodListAdapter(list);
                    foodListView.setAdapter(adapterListFood);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);


    }
}