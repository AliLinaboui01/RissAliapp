package ali.org.rissali.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import ali.org.rissali.Domain.Foods;
import ali.org.rissali.R;
import ali.org.rissali.helpers.ChangeNumberItemsListener;
import ali.org.rissali.helpers.ManagementCart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagementCart managementCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        Foods food = list.get(holder.getAdapterPosition());
        int adapterPosition = holder.getAdapterPosition();
        holder.titleTxt.setText(food.getTitle());
        holder.priceEachItem.setText(food.getPrice()+"");
        holder.priceOneItemTxt.setText(food.getNumberInCart() + "*$" + (
                food.getNumberInCart() * food.getPrice()
                )) ;
        holder.num.setText(+food.getNumberInCart()+"");
        Glide.with(holder.itemView.getContext())
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.plusBtn.setOnClickListener(v -> managementCart.plusNumberItem(list, adapterPosition, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
        holder.minusBtn.setOnClickListener(v -> managementCart.minusNumberItem(list, adapterPosition, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView titleTxt, priceOneItemTxt, priceEachItem, plusBtn, minusBtn, num;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleCartView);
            priceOneItemTxt = itemView.findViewById(R.id.pricecartView);
            plusBtn = itemView.findViewById(R.id.plusCartBtn);
            minusBtn = itemView.findViewById(R.id.minusCartBtn);
            num = itemView.findViewById(R.id.numItemCartView);
            priceEachItem = itemView.findViewById(R.id.PriceEachCartView);
            pic = itemView.findViewById(R.id.imgCartView);
        }
    }
}
