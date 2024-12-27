package ali.org.rissali.adapter;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;

import java.util.ArrayList;

import ali.org.rissali.Domain.Category;
import ali.org.rissali.R;
import ali.org.rissali.activities.ListFoodActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_category, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
        Category category = items.get(holder.getAdapterPosition()); // Safely use the updated position
        holder.titleTxt.setText(category.getName());

        int adapterPosition = holder.getAdapterPosition();
        switch (adapterPosition) {
            case 0:
                holder.pic.setBackgroundResource(R.drawable.cat_1_background);
                break;
            case 1:
                holder.pic.setBackgroundResource(R.drawable.cat_2_background);
                break;
            case 2:
                holder.pic.setBackgroundResource(R.drawable.cat_3_background);
                break;
            case 3:
                holder.pic.setBackgroundResource(R.drawable.cat_4_background);
                break;
            case 4:
                holder.pic.setBackgroundResource(R.drawable.cat_5_background);
                break;
            case 5:
                holder.pic.setBackgroundResource(R.drawable.cat_6_background);
                break;
            case 6:
                holder.pic.setBackgroundResource(R.drawable.cat_7_background);
                break;
            case 7:
                holder.pic.setBackgroundResource(R.drawable.cat_8_background);
                break;

        }
        Glide.with(context)
                .load(context.getResources().getIdentifier(category.getImagePath(), "drawable", context.getPackageName()))
                .into(holder.pic);
        // Safely handle item click
        holder.itemView.setOnClickListener(v -> {
            int currentAdapterPosition = holder.getAdapterPosition();
            if (currentAdapterPosition != RecyclerView.NO_POSITION) {
                Category clickedCategory = items.get(currentAdapterPosition);
                Intent intent = new Intent(context, ListFoodActivity.class);
                intent.putExtra("CategoryId", clickedCategory.getId());
                intent.putExtra("CategoryName", clickedCategory.getName());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.catNameTxt);
            pic = itemView.findViewById(R.id.imageCat);
        }
    }
}