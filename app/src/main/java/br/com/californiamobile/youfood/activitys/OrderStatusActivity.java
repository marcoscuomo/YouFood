package br.com.californiamobile.youfood.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import br.com.californiamobile.youfood.R;
import br.com.californiamobile.youfood.common.Common;
import br.com.californiamobile.youfood.model.Request;
import br.com.californiamobile.youfood.viewHolder.OrderViewHolder;

public class OrderStatusActivity extends AppCompatActivity {

    //Atributos
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //FindViewByIds
        recyclerView = findViewById(R.id.listOrders);

        //RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        loadOrders(Common.currentUser.getPhone());
        //String phone = "11974570739";
        //loadOrders(phone);
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone")
                .equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {

        if(status.equals("0")){
            return "Pedido Recebido";
        }else if(status.equals("1")){
            return "Pedido enviado";
        }else {
            return "Pedido Entregue";
        }
    }
}
