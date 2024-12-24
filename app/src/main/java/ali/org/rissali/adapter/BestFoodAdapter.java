//package ali.org.rissali.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//
//import ali.org.rissali.Domain.Foods;
//import ali.org.rissali.R;
//
//
//public class BestFoodAdapter extends RecyclerView.Adapter<BestFoodAdapter.viewholder> {
//    ArrayList<Foods> items;
//    Context context;
//
//    @NonNull
//    @Override
//    public BestFoodAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_best_deal,parent, false);
//        return new viewholder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BestFoodAdapter.viewholder holder, int position) {
//        holder.titleTxt.setText(items.get(position).getTitle());
//        holder.priceTxt.setText("$" +items.get(position).getPrice());
//        holder.titleTxt.setText(items.get(position).getTimeValue()+ " min");
//        holder.starTxt.setText(""+ items.get(position).getStar());
//
//        Glide.with(context)
//                .load(items.get(position))
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class viewholder extends RecyclerView.ViewHolder {
//        TextView titleTxt, priceTxt, starTxt, timeTxt;
//        ImageView pic;
//        public viewholder(@NonNull View itemView) {
//            super(itemView);
//            titleTxt = itemView.findViewById(R.id.titleTxt);
//            priceTxt = itemView.findViewById(R.id.priceTxt);
//            starTxt = itemView.findViewById(R.id.starTxt);
//            timeTxt = itemView.findViewById(R.id.timetxt);
//            pic = itemView.findViewById(R.id.pic);
//        }
//    }
//}
