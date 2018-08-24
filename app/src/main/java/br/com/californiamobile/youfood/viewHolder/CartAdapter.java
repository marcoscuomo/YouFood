package br.com.californiamobile.youfood.viewHolder;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.californiamobile.youfood.R;
import br.com.californiamobile.youfood.interfaces.ItemClickListener;
import br.com.californiamobile.youfood.model.Order;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(i).getQuantity(), Color.RED);

        cartViewHolder.imgCartCount.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        int price = (Integer.parseInt(listData.get(i).getPrice() )) * (Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHolder.txtPrice.setText(fmt.format(price));
        cartViewHolder.txtCartName.setText(listData.get(i).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}



class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtCartName, txtPrice;
    public ImageView imgCartCount;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCartName = itemView.findViewById(R.id.cart_item_name);
        txtPrice = itemView.findViewById(R.id.cart_item_price);
        imgCartCount = itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View view) {

    }
}
