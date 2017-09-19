package crapida.app.consultarapida.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import crapida.app.consultarapida.R;

public class perfil extends Activity {

    private DatabaseReference firebaseReferencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReferencia = firebaseReferencia.child("usuarios");

    private FirebaseAuth firebaseAuth;
    private EditText nome;
    private EditText dataNascimento;
    private EditText telefone;
    private EditText celular;
    private EditText convenio;
    private EditText plano;
    private Button salvar;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Recebe info do usuario logado





        //Referenciamento dos campos
        nome = (EditText) findViewById(R.id.nomeId);
        dataNascimento = (EditText) findViewById(R.id.datanascId);
        telefone = (EditText) findViewById(R.id.telefoneId);
        celular = (EditText) findViewById(R.id.celularId);
        convenio = (EditText) findViewById(R.id.convenioId);
        plano = (EditText) findViewById(R.id.planoId);
        salvar = (Button) findViewById(R.id.botaoSalvarId);

        //ação do botao
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

                //Instaciamento do construtor
                usuarioperfil usuario = new usuarioperfil();

                //Converte para String
                String textonome = nome.getText().toString();
                String textodatanascimento = dataNascimento.getText().toString();
                String textotelefone = telefone.getText().toString();
                String textocelular = celular.getText().toString();
                String textoconvenio = convenio.getText().toString();
                String textoplano = plano.getText().toString();

                //Envia para o objeto
                usuario.setNome(textonome);
                usuario.setDataNascimento(textodatanascimento);
                usuario.setTelefone(textotelefone);
                usuario.setCelular(textocelular);
                usuario.setConvenio(textoconvenio);
                usuario.setPlano(textoplano);

                //grava no banco

                usuarioReferencia.child(currentFirebaseUser.getUid()).setValue(usuario)
                        .addOnCompleteListener(perfil.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(perfil.this, "Perfil Alterado com Sucesso", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(perfil.this, "Falha ao alterar Perfil", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });


    }
}
