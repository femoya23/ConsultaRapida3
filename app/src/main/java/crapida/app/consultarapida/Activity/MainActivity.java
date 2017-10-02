package crapida.app.consultarapida.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import crapida.app.consultarapida.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private EditText senha;
    private Button botaocadastrar;
    private Button botaologin;
    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializando os componentes
        inicializarFirebase();
        inicializarBotaoFacebook();
        inicializarCallback();
        clicarBotaoFacebook();


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


    private void inicializarFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void inicializarBotaoFacebook() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile" );

    }

    private void inicializarCallback() {
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void clicarBotaoFacebook() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                alerta("Login cancelado.");
            }

            @Override
            public void onError(FacebookException error) {
                alerta("Erro no login com Facebook.");
            }
        });
    }

    private void firebaseLogin(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(MainActivity.this, TelaPrin.class);
                    startActivity(i);
                }else{
                    alerta("Erro de autenticação com Firebase.");
                }
            }
        });
    }

    private void alerta(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * teste pra finalizar e fazer o logoff do Facebook quando encerrar o app
     * não está funcionando pois quando ele sai da janela do app pra abrir a janela de confirmação do FB
     * ele não volta pro app, volta direto para a tela principal do Android
     *
    @Override
    protected void onDestroy(){
        super.onDestroy();
        logout();
    }

    private void logout() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        finish();
    }
     **/

}
