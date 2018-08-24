package br.com.californiamobile.youfood.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.californiamobile.youfood.R;
import br.com.californiamobile.youfood.interfaces.ItemClickListener;
import br.com.californiamobile.youfood.model.Food;
import br.com.californiamobile.youfood.viewHolder.FoodViewHolder;

public class FoodListActivity extends AppCompatActivity {

    //Atributos
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private String categoryId = "";

    //Configurcao RecyclerView
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    //Configuracao do Search
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> sugestaoList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        //FindViewByIds


        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        //RecyclerView
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Pegando a intent
        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId != null){
            loadListFood(categoryId);
        }

        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Pesquisar prato");
        //materialSearchBar.setSpeechMode(false); ja definimos no xml
        loadSugestoes();
        materialSearchBar.setLastSuggestions(sugestaoList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> sugestao = new ArrayList<>();
                for(String search:sugestaoList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        sugestao.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(sugestao);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(FoodListActivity.this, "" + local.getName(), Toast.LENGTH_SHORT).show();

                        //Start new Activity
                        Intent foodDetailIntent = new Intent (FoodListActivity.this, FoodDetailActivity.class);
                        //Enviando o id para a nova Activity
                        foodDetailIntent.putExtra("FoodId", searchAdapter.getRef(position).getKey());
                        startActivity(foodDetailIntent);

                    }
                });

            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSugestoes() {

        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapShot:dataSnapshot.getChildren() ){

                            Food item = postSnapShot.getValue(Food.class);
                            sugestaoList.add(item.getName());

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(FoodListActivity.this, "" + local.getName(), Toast.LENGTH_SHORT).show();

                        //Start new Activity
                        Intent foodDetailIntent = new Intent (FoodListActivity.this, FoodDetailActivity.class);
                        //Enviando o id para a nova Activity
                        foodDetailIntent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetailIntent);

                    }
                });
            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);

    }
}
