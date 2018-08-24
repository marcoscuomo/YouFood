package br.com.californiamobile.youfood.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import br.com.californiamobile.youfood.R;
import br.com.californiamobile.youfood.common.Common;
import br.com.californiamobile.youfood.model.Usuario;
import info.hoang8f.widget.FButton;

public class SignInActivity extends AppCompatActivity {

    //Atributos

    MaterialEditText edtFone, edtSenha;
    FButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //FindViewByIds
        edtFone = findViewById(R.id.signin_edtFone);
        edtSenha = findViewById(R.id.signin_edtSenha);
        btnSignIn = findViewById(R.id.signin_btnSignIn);

        //Iniciando o Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tabelaUsuario = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Barra de carregamentp
                final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Por favor aguarde...");
                mDialog.show();

                tabelaUsuario.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Verificando se o usuario nao existe no banco de dados
                        if(dataSnapshot.child(edtFone.getText().toString()).exists()){
                            //Pegar informacoes do Usuario
                            mDialog.dismiss();
                            final Usuario usuario = dataSnapshot.child(edtFone.getText().toString()).getValue(Usuario.class);
                            usuario.setPhone(edtFone.getText().toString()); //add fone posteriormente
                            if(usuario.getPassword().equals(edtSenha.getText().toString())){

                                Common.currentUser = usuario;
                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                finish();


                                //Toast.makeText(SignInActivity.this, "Senha ok", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignInActivity.this, "Senha invalida", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Usuario nao existe", Toast.LENGTH_SHORT).show();
                        }




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
