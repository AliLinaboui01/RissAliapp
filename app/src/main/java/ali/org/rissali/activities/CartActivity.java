package ali.org.rissali.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ali.org.rissali.R;
import ali.org.rissali.adapter.CartAdapter;
import ali.org.rissali.databinding.ActivityCartBinding;
import ali.org.rissali.databinding.ActivityDetailBinding;
import ali.org.rissali.helpers.ChangeNumberItemsListener;
import ali.org.rissali.helpers.ManagementCart;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    private double tax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.main));
        managementCart = new ManagementCart(this);
        setVariable();
        calculateCart();
        initList();
        binding.backBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initList() {
        if(managementCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cartRecycler.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managementCart.getListCart(), this, () -> calculateCart());
        binding.cartRecycler.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02; //percent 2% Tax
        double delivery = 10; // 10 Dollar
        tax = Math.round(managementCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;
        binding.subTotal.setText("$"+ itemTotal);
        binding.totalTaxTxt.setText("$"+ tax);
        binding.deliveryTxt.setText("$"+ delivery);
        binding.total.setText("$"+ total);

    }

    private void setVariable() {
        binding.backBtnCart.setOnClickListener(v -> finish());
    }
}