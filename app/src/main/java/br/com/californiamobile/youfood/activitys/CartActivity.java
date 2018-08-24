package br.com.californiamobile.youfood.activitys;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import br.com.californiamobile.youfood.R;
import br.com.californiamobile.youfood.common.Common;
import br.com.californiamobile.youfood.model.Order;
import br.com.californiamobile.youfood.model.Request;
import br.com.californiamobile.youfood.viewHolder.CartAdapter;
import br.com.californiamobile.youfood.database.Database;
import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity {

    //Atributos
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private TextView txtTotalPrice;
    private FButton btnPlace;
    private List<Order> cart = new ArrayList<>();
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //FindViewByIds
        recyclerView  = findViewById(R.id.listCart);
        txtTotalPrice = findViewById(R.id.cart_txtTotal);
        btnPlace      = findViewById(R.id.cart_btnPlaceOrder);


        //RecyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Botao de fechar o pedido
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showAlertDialog();

            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("Mais um passo!");
        alertDialog.setMessage("Informe seu endereco: ");


        final EditText edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress); // Adicionando edit text ao alert dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_24dp);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Criando um novo pedido
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                //Submit ao Firebase
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //Delete carrinho
                new Database(CartActivity.this).cleanCart();
                Toast.makeText(CartActivity.this, "Obrigado! Pedido enviado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        //Calculando preco total
        int total = 0;
        for(Order order:cart){
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
        }


    }
}
