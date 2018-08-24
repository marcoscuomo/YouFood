package br.com.californiamobile.youfood.activitys;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import info.hoang8f.widget.FButton;
import br.com.californiamobile.youfood.R;

public class MainActivity extends AppCompatActivity {

    //Atributos
    private FButton btnSignUp, btnSignIn;
    private TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FindViewByIds
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSlogan = findViewById(R.id.txtSlogan);

        //Alterando a font do slogan
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        //Criando eventos nos botoes
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, SignInActivity.class));

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, SignUpActivity.class));

            }
        });

    }
}
