package br.com.californiamobile.youfood.activitys;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import br.com.californiamobile.youfood.R;
import br.com.californiamobile.youfood.database.Database;
import br.com.californiamobile.youfood.model.Food;
import br.com.californiamobile.youfood.model.Order;

public class FoodDetailActivity extends AppCompatActivity {

    //Atributos
    private TextView food_name, food_price, food_description;
    private ImageView food_image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnCart;
    private ElegantNumberButton numberButton;
    private String foodId = "";
    private FirebaseDatabase database;
    private DatabaseReference foods;
    private Food currentFood;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //FindViewByIds
        numberButton            = findViewById(R.id.number_button);
        btnCart                 = findViewById(R.id.btnCart);
        food_description        = findViewById(R.id.food_description);
        food_name               = findViewById(R.id.food_name);
        food_price              = findViewById(R.id.food_price);
        food_image              = findViewById(R.id.img_food);
        collapsingToolbarLayout = findViewById(R.id.collapsing);

        //Botao de add ao carrinho
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(FoodDetailActivity.this).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));

                Toast.makeText(FoodDetailActivity.this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();
            }
        });

        //Collapsing
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandeAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        //Pegando o ID da intent
        if(getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty()){
            getDetailFood(foodId);
        }




    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Setando a imagem
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
