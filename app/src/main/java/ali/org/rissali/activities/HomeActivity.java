package ali.org.rissali.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ali.org.rissali.Domain.Category;
import ali.org.rissali.Domain.Foods;
import ali.org.rissali.Domain.Location;
import ali.org.rissali.Domain.Price;
import ali.org.rissali.Domain.Time;
import ali.org.rissali.Domain.User;
import ali.org.rissali.R;
import ali.org.rissali.adapter.BestFoodAdapter;
import ali.org.rissali.adapter.CategoryAdapter;
import ali.org.rissali.databinding.HomePageBinding;

public class HomeActivity extends BaseActivity{

    private HomePageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));
        setUsername();

        logout();
        initLocation();
        initUsername();
        initTime();
        initPrice();
        initBestFood();
        initCategory();
        setVariable();

        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
    }

    private void setVariable() {
        binding.searchBtn.setOnClickListener(v -> {
            String text = binding.searchEditText.getText().toString();
            if(!text.isEmpty()){
                Intent intent = new Intent(HomeActivity.this, ListFoodActivity.class);
                intent.putExtra("text" ,text);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });
    }

    private void initUsername() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Get user details from the FirebaseUser object
            String displayName = user.getDisplayName(); // This might be null if not set during registration
            String email = user.getEmail(); // Email is usually available

            // Display the email or a placeholder if displayName is null
            String usernameToShow = displayName != null ? displayName : email;

            binding.usernameTxt.setText(usernameToShow);
        } else {
            Log.e(TAG, "No authenticated user found.");
        }
    }


    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.categoryProgressBar.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("CategoryDebug", "onDataChange triggered");
                if (snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        Category category = issue.getValue(Category.class);
                        if (category != null) {
                            Log.d("CategoryDebug", "Found category: " + category.getName()
                                    + ", image: " + category.getImagePath());
                            list.add(category);
                        }
                    }
                    if (!list.isEmpty()) {
                        Log.d("CategoryDebug", "Setting up RecyclerView with " + list.size() + " categories");
                        binding.seasonView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 4));
                        CategoryAdapter adapter = new CategoryAdapter(list);
                        binding.seasonView.setAdapter(adapter);
                    } else {
                        Log.e("CategoryDebug", "No categories found in database");
                    }
                } else {
                    Log.e("CategoryDebug", "Category snapshot doesn't exist");
                }
                binding.categoryProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CategoryDebug", "Database error: " + error.getMessage());
                binding.categoryProgressBar.setVisibility(View.GONE);
            }
        });
    }
    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarBestDates.setVisibility(View.VISIBLE);
        ArrayList<Foods> foods = new ArrayList<>();

        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null) {
                            Log.d("FoodsDebug", "Food: " + food.getTitle() + ", ImagePath: " + food.getImagePath());
                            foods.add(food);
                        }
                    }

                    if (!foods.isEmpty()) {
                        Log.d("FoodsDebug", "Setting up RecyclerView with " + foods.size() + " items");
                        binding.bestDatesView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        BestFoodAdapter adapter = new BestFoodAdapter(foods);
                        binding.bestDatesView.setAdapter(adapter);
                        binding.bestDatesView.setVisibility(View.VISIBLE); // Ensure RecyclerView is visible
                    } else {
                        Log.e("FoodsDebug", "No best foods found.");
                    }
                } else {
                    Log.e("FoodsDebug", "Foods snapshot doesn't exist");
                }
                binding.progressBarBestDates.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FoodsDebug", "Database error: " + error.getMessage());
                binding.progressBarBestDates.setVisibility(View.GONE);
            }
        });
    }


    public void logout(){
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                // Redirect user to the login screen or any other screen
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setUsername(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            user.getDisplayName();
            System.out.println(user.getEmail());
            System.out.println(user.getDisplayName());
        }

    }
    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Location location = issue.getValue(Location.class);
                        if (location != null) {
                            list.add(location);
                        }
                    }
                    Spinner locationSpinner = findViewById(R.id.spinnerLocation);
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(
                            HomeActivity.this,
                            R.layout.sp_item,
                            list
                    );
                    locationSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initTime() {
        DatabaseReference myRef = database.getReference("Time");
        ArrayList<Time> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Time time = issue.getValue(Time.class);
                        if (time != null) {
                            list.add(time);
                        }
                    }
                    Spinner timeSpinner = findViewById(R.id.spinnerTime);
                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(
                            HomeActivity.this,
                            R.layout.sp_item,
                            list
                    );
                    timeSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initPrice() {
        DatabaseReference myRef = database.getReference("Price");
        ArrayList<Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Price price = issue.getValue(Price.class);
                        if (price != null) {
                            list.add(price);
                        }
                    }
                    Spinner priceSpinner = findViewById(R.id.spinnerDollar);
                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(
                            HomeActivity.this,
                            R.layout.sp_item,
                            list
                    );
                    priceSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
