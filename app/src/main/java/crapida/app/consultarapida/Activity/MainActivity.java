package crapida.app.consultarapida.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import crapida.app.consultarapida.R;

public class MainActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private EditText senha;
    private Button botaocadastrar;
    private Button botaologin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        //Referencia Id com Variaveis
        email= (EditText) findViewById(R.id.loginId);
        senha= (EditText) findViewById(R.id.senhaId);
        botaocadastrar= (Button) findViewById(R.id.botaoCadastrarId);
        botaologin= (Button) findViewById(R.id.botaoLoginId);

        //Ação Botao Login
        botaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //recebe dados

                String textoemail = email.getText().toString();
                String textosenha = senha.getText().toString();

                //Login

                firebaseAuth.signInWithEmailAndPassword(textoemail,textosenha)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //testa login

                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Usuário logado com Sucesso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, TelaPrin.class));
                                }else{
                                    Toast.makeText(MainActivity.this, "Erro ao logar o Usuário", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //Ação Botao Cadastro
        botaocadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //recebe dados

                String textoemail = email.getText().toString();
                String textosenha = senha.getText().toString();

                //Cadastro


               firebaseAuth.createUserWithEmailAndPassword(textoemail,textosenha)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //testa cadastro

                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Usuário Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, TelaPrin.class));
                                }else{
                                    Toast.makeText(MainActivity.this, "Erro ao Cadastrar o Usuário", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }
}
