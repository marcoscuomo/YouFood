package br.com.californiamobile.youfood.activitys;

import android.app.ProgressDialog;
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
import br.com.californiamobile.youfood.model.Usuario;
import info.hoang8f.widget.FButton;

public class SignUpActivity extends AppCompatActivity {

    //Atributos
    private MaterialEditText edtNome, edtTelefone, edtSenha;
    private FButton btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //FindViewByIds
        edtNome = findViewById(R.id.signup_edtName);
        edtTelefone = findViewById(R.id.signup_edtFone);
        edtSenha = findViewById(R.id.signup_edtSenha);
        btnSignUp = findViewById(R.id.signup_btnSignUp);

        //Iniciando o Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tabelaUsuario = database.getReference("User");


        //Evendo de clique do botao
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Barra de carregamento
                final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Por favor aguarde...");
                mDialog.show();

                //Recuperando e tratando valores da tabela usuario
                tabelaUsuario.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Verificando se o telefone ja esta cadastrado
                        if(dataSnapshot.child(edtTelefone.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Telefone ja cadastrado", Toast.LENGTH_SHORT).show();
                        }else{
                            mDialog.dismiss();

                            //Criando um objeto para cria o usuario
                            Usuario usuario = new Usuario(edtNome.getText().toString(), edtSenha.getText().toString());

                            //Inserindo o objeto ao Firebase
                            tabelaUsuario.child(edtTelefone.getText().toString()).setValue(usuario);
                            Toast.makeText(SignUpActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            finish();

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
