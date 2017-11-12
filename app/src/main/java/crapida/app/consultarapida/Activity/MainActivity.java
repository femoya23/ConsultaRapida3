package crapida.app.consultarapida.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private EditText senha;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public DatabaseReference firebase;
    public FirebaseUser fbuser;
    public String uiduser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //inicializando os componentes
        inicializarFirebase();
        inicializarBotaoFacebook();
        inicializarCallback();
        clicarBotaoFacebook();
        firebaseAuth = firebaseAuth.getInstance();

        //Referencia Id com Variaveis
        email= (EditText) findViewById(R.id.loginId);
        senha= (EditText) findViewById(R.id.senhaId);
        Button botaocadastrar = (Button) findViewById(R.id.botaoCadastrarId);
        Button botaologin = (Button) findViewById(R.id.botaoLoginId);

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
                //Mensagem Cadastrando
                Toast.makeText(MainActivity.this, "Cadastrando...Aguarde", Toast.LENGTH_SHORT).show();

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

                                    fbuser = FirebaseAuth.getInstance().getCurrentUser() ;
                                    uiduser = fbuser.getUid();

                                    firebase = ConfiguracaoFirebase.getFirebase()
                                            .child("usuarios")
                                            .child(uiduser);
                                    firebase.child("nome").setValue("");
                                }else{
                                    Toast.makeText(MainActivity.this, "Erro ao Cadastrar o Usuário", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //método de requisição de dados do Facebook
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,gender,location");
        request.setParameters(parameters);
        request.executeAsync();




    }//final do método onCreate





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
                alerta("Erro no login com Facebook.erro A");
            }
        });
    }

    private void firebaseLogin(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(MainActivity.this, TelaPrin.class);
                    startActivity(i);
                    fbuser = FirebaseAuth.getInstance().getCurrentUser() ;
                    uiduser = fbuser.getUid();
                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(uiduser);
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    firebase.child("nome").setValue(currentFirebaseUser.getDisplayName());
                    firebase.child("email").setValue(currentFirebaseUser.getEmail());

                    finish();
                }else{
                    alerta("Erro de autenticação com Firebase.erro b");
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
