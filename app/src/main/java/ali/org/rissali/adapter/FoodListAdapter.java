package ali.org.rissali.adapter;

import android.content.Context;
import android.content.Intent;
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
import ali.org.rissali.activities.DetailActivity;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.viewholder> {

    ArrayList<Foods> items;
    Context context;

    public FoodListAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FoodListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_foods, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.viewholder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        holder.titleTxt.setText(items.get(adapterPosition).getTitle());
        holder.priceTxt.setText("$" +items.get(adapterPosition).getPrice());
        holder.timeTxt.setText(items.get(adapterPosition).getTimeValue()+ " min");
        holder.starTxt.setText(""+ items.get(adapterPosition).getStar());

        Glide.with(context)
                .load(items.get(adapterPosition).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(adapterPosition));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView titleTxt, priceTxt, timeTxt, starTxt;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.titleCard);
            priceTxt = itemView.findViewById(R.id.priceCard);
            timeTxt = itemView.findViewById(R.id.timeCard);
            starTxt = itemView.findViewById(R.id.starCard);
            pic = itemView.findViewById(R.id.imgCard);

        }
    }
}
