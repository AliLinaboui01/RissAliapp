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

public class HomeActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        TextView usernameTxt = findViewById(R.id.usernameTxt);
        setUsername(usernameTxt);
        ImageView logoutBtn = findViewById(R.id.logoutBtn);
        ProgressBar progressBar = findViewById(R.id.progressBarBestDates);
        ProgressBar categoryProgressBar = findViewById(R.id.categoryProgressBar);

        RecyclerView bestDatesView = findViewById(R.id.bestDatesView);
        RecyclerView categoryView = findViewById(R.id.seasonView);

        ImageView searchBtn = findViewById(R.id.searchBtn);
        EditText searchText = findViewById(R.id.searchEditText);

        logout(logoutBtn);
        initLocation();
        initUsername(usernameTxt);
        initTime();
        initPrice();
        initBestFood(progressBar, bestDatesView);
        initCategory(categoryProgressBar, categoryView);
        setVariable(searchBtn, searchText);
    }

    private void setVariable(ImageView searchBtn, EditText searchText) {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString();
                if(!text.isEmpty()){
                    Intent intent = new Intent(HomeActivity.this, ListFoodActivity.class);
                    intent.putExtra("text" ,text);
                    intent.putExtra("isSearch", true);
                    startActivity(intent);
                }
            }
        });
    }

    private void initUsername(TextView usernameTxt) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = database.getReference("Users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get the user object
                        User currentUser = snapshot.getValue(User.class);
                        if (currentUser != null) {
                            // Set the username in the TextView
                            usernameTxt.setText(currentUser.getUsername());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error retrieving user data: " + error.getMessage());
                }
            });
        }
    }

    private void initCategory(ProgressBar categoryProgressBar, RecyclerView categoryView) {
        DatabaseReference myRef = database.getReference("Category");
        categoryProgressBar.setVisibility(View.VISIBLE);
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
                        categoryView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 4));
                        CategoryAdapter adapter = new CategoryAdapter(list);
                        categoryView.setAdapter(adapter);
                    } else {
                        Log.e("CategoryDebug", "No categories found in database");
                    }
                } else {
                    Log.e("CategoryDebug", "Category snapshot doesn't exist");
                }
                categoryProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CategoryDebug", "Database error: " + error.getMessage());
                categoryProgressBar.setVisibility(View.GONE);
            }
        });
    }
    private void initBestFood(ProgressBar progressBar, RecyclerView bestDatesView) {
        DatabaseReference myRef = database.getReference("Foods");
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<Foods> foods = new ArrayList<>();
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        foods.add(issue.getValue(Foods.class));
                    }
                    if (!foods.isEmpty()) {
                        bestDatesView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        RecyclerView.Adapter<BestFoodAdapter.viewholder> adapter = new BestFoodAdapter(foods);
                        bestDatesView.setAdapter(adapter);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logout(ImageView logoutBtn){
        logoutBtn.setOnClickListener(new View.OnClickListener() {
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
    private void setUsername(TextView usernameTxt){
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
