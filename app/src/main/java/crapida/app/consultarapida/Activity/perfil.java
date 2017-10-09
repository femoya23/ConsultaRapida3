package crapida.app.consultarapida.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.Convenio;
import crapida.app.consultarapida.Model.DadosPerfil;
import crapida.app.consultarapida.Model.Plano;
import crapida.app.consultarapida.R;

/**
 * Created by wsabo on 23/09/2017.
 */

public class perfil extends Activity {

    private Spinner spConvenio;
    private Spinner spPlano;
    private Spinner spSexo;

    private ArrayList<String> convenio;
    private ArrayList<String> plano;
    //private ArrayList<String> sexo;

    private String[] sexo;

    private ArrayAdapter adapterConvenio;
    private ArrayAdapter adapterPlano;
    private ArrayAdapter adapterSexo;

    private DatabaseReference firebase;
    //teste de exibição de foto e dados do Facebook
    private ImageView ivFoto;
    private TextView tvEmail;
    private TextView tvId;
    private Button botaoLogoff;

    //Infos coletadas do Facebook
    private TextView fbId;
    private TextView fbName;
    private TextView fbFirstName;
    private TextView fbLastName;
    private TextView fbGender;
    private TextView fbLocale;
    private TextView fbTimezone;
    private TextView fbUpdatedTime;
    private TextView fbVerified;

    private EditText nome;
    private EditText email;
    private EditText celular;
    private EditText dataNascimento;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseUser fbuser;
    private String uiduser;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        fbuser = FirebaseAuth.getInstance().getCurrentUser() ;
        uiduser = fbuser.getUid();
        //inicializar componentes
        inicializarComponentes();
        inicializarFirebase();
        clicarBotaoLogout();
        preencherCampos();





        //Instancia o Array List
        convenio = new ArrayList<>();
        plano = new ArrayList<>();
        sexo = new String[] {"Escolha seu sexo", "Feminino", "Masculino", "Outro"};

        //Referencia objeto
        spConvenio = (Spinner) findViewById(R.id.convenioId);
        spPlano = (Spinner) findViewById(R.id.planoId);
        spSexo = (Spinner) findViewById(R.id.sexoId);
        nome = (EditText) findViewById(R.id.nomeId);
        email = (EditText) findViewById(R.id.emailId);
        celular = (EditText) findViewById(R.id.celularId);
        dataNascimento = (EditText) findViewById(R.id.datanascId);


        //Monta Spinner
        adapterConvenio = new ArrayAdapter (perfil.this, R.layout.spinner_busca,convenio);
        spConvenio.setAdapter(adapterConvenio);

        adapterPlano = new ArrayAdapter(perfil.this,R.layout.spinner_busca,plano);
        spPlano.setAdapter(adapterPlano);

        adapterSexo = new ArrayAdapter (perfil.this, R.layout.spinner_busca,sexo);
        spSexo.setAdapter(adapterSexo);


        //recuperar convenio do firebase convenios
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("filtros")
                .child("convenios");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                convenio.add("Escolha um convênio");
                for(DataSnapshot dados: dataSnapshot.getChildren() ){
                    Convenio conv = dados.getValue(Convenio.class);
                    convenio.add(conv.getNome());
                }
                adapterConvenio.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        spConvenio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String convenioSelecionado = (String) spConvenio.getSelectedItem();

                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("filtros")
                        .child("convenios")
                        .child(convenioSelecionado)
                        .child("Planos");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        plano.clear();
                        plano.add("Escolha um plano");
                        for(DataSnapshot dados: dataSnapshot.getChildren() ){
                            Plano plan = dados.getValue(Plano.class);
                            plano.add(plan.getNome());
                        }
                        adapterPlano.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        /**
        //recuperar sexo do firebase
        spSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("filtros")
                        .child("sexo");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sexo.clear();
                        sexo.add("Escolha seu sexo");
                        for(DataSnapshot dados: dataSnapshot.getChildren() ){
                            Sexo sex = dados.getValue(Sexo.class);
                            sexo.add(sex.getNome());
                        }
                        adapterSexo.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });*/


    }

    private void preencherCampos(){
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        try {
            firebase = ConfiguracaoFirebase.getFirebase()
                    .child("usuarios")
                    .child(uiduser);
        }finally {
            firebase = ConfiguracaoFirebase.getFirebase()
                    .child("usuarios");
            firebase.child(uiduser).child("nome").setValue("");
        }


        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getChildren();
                DadosPerfil dadosPerfil = dataSnapshot.getValue(DadosPerfil.class);
                if(dadosPerfil.getNome()!= null) {
                    nome.setText(dadosPerfil.getNome());
                }else{
                    if(currentFirebaseUser.getEmail()!=null)
                        nome.setText(currentFirebaseUser.getDisplayName());
                }
                if(dadosPerfil.getCelular()!= null)
                    celular.setText(dadosPerfil.getCelular());
                if(dadosPerfil.getDataNasc()!= null)
                    dataNascimento.setText(dadosPerfil.getDataNasc());
                if(dadosPerfil.getEmail()!= null) {
                    email.setText(dadosPerfil.getEmail());
                }else {
                    if (currentFirebaseUser.getEmail()!=null)
                        email.setText(currentFirebaseUser.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }



    private void clicarBotaoLogout() {
        botaoLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent i = new Intent(perfil.this, MainActivity.class);
        startActivity(i);
    }

    private void inicializarFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser !=null){
                    exibirDados(firebaseUser);
                }else{
                    finish();
                }
            }
        };
    }

    private void exibirDados(FirebaseUser firebaseUser) {
        tvEmail.setText(this.firebaseUser.getEmail());
        tvId.setText(this.firebaseUser.getUid());
        Glide.with(perfil.this).load(this.firebaseUser.getPhotoUrl()).into(ivFoto);

        //exibir dados coletados do FB
        fbName.setText(this.firebaseUser.getDisplayName());
        fbId.setText(this.firebaseUser.getProviderId());

    }

    private void inicializarComponentes() {
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvId = (TextView) findViewById(R.id.tvId);
        botaoLogoff = (Button) findViewById(R.id.botaoLogoff);
        fbId = (TextView) findViewById(R.id.tvFbId);
        fbName = (TextView) findViewById(R.id.tvFbName);
        fbFirstName = (TextView) findViewById(R.id.tvFbFirstName);
        fbLastName = (TextView) findViewById(R.id.tvFbLastName);
        fbGender = (TextView) findViewById(R.id.tvFbGender);
        fbLocale = (TextView) findViewById(R.id.tvFbLocale);
        fbTimezone = (TextView) findViewById(R.id.tvFbTimezone);
        fbUpdatedTime = (TextView) findViewById(R.id.tvFbUpdatedTime);
        fbVerified = (TextView) findViewById(R.id.tvFbVerified);

    }

    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        LoginManager.getInstance().logOut();
    }
}
