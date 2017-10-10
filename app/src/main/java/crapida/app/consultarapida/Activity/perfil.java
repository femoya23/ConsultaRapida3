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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.SimpleResource;
import com.facebook.login.LoginManager;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import crapida.app.consultarapida.Model.Cidade;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.Convenio;
import crapida.app.consultarapida.Model.DadosPerfil;
import crapida.app.consultarapida.Model.Estado;
import crapida.app.consultarapida.Model.Plano;
import crapida.app.consultarapida.R;

/**
 * Created by wsabo on 23/09/2017.
 */

public class perfil extends Activity {

    private Spinner spConvenio;
    private Spinner spPlano;
    private Spinner spSexo;
    private Spinner spEstado;
    private Spinner spCidade;

    private ArrayList<String> convenio;
    private ArrayList<String> plano;
    //private ArrayList<String> sexo;
    private ArrayList estado;
    private ArrayList cidade;

    private String[] sexo;

    private ArrayAdapter adapterConvenio;
    private ArrayAdapter adapterPlano;
    private ArrayAdapter adapterSexo;
    private ArrayAdapter adapterEstado;
    private ArrayAdapter adapterCidade;

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
    private Button botaoSalvar;

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
        sexo = new String[] {"Escolha seu sexo", "Feminino", "Masculino"};
        estado = new ArrayList<>();
        cidade = new ArrayList<>();

        //Referencia objeto
        spConvenio = (Spinner) findViewById(R.id.convenioId);
        spPlano = (Spinner) findViewById(R.id.planoId);
        spSexo = (Spinner) findViewById(R.id.sexoId);
        spEstado = (Spinner) findViewById(R.id.estadoId);
        spCidade = (Spinner) findViewById(R.id.cidadeId);
        nome = (EditText) findViewById(R.id.nomeId);
        email = (EditText) findViewById(R.id.emailId);
        celular = (EditText) findViewById(R.id.celularId);
        dataNascimento = (EditText) findViewById(R.id.datanascId);
        botaoSalvar = (Button) findViewById(R.id.botaoSalvarId);

        //Mask celular
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(celular,simpleMaskTelefone);
        celular.addTextChangedListener(maskTelefone);

        //Mask data Nascimento
        SimpleMaskFormatter simpleMaskData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskData = new MaskTextWatcher(dataNascimento,simpleMaskData);
        dataNascimento.addTextChangedListener(maskData);

        //Monta Spinner
        adapterConvenio = new ArrayAdapter (perfil.this, R.layout.spinner_busca,convenio);
        spConvenio.setAdapter(adapterConvenio);

        adapterPlano = new ArrayAdapter(perfil.this,R.layout.spinner_busca,plano);
        spPlano.setAdapter(adapterPlano);

        adapterSexo = new ArrayAdapter (perfil.this, R.layout.spinner_busca,sexo);
        spSexo.setAdapter(adapterSexo);

        adapterEstado = new ArrayAdapter(perfil.this, R.layout.spinner_busca,estado);
        spEstado.setAdapter(adapterEstado);

        adapterCidade = new ArrayAdapter(perfil.this, R.layout.spinner_busca, cidade);
        spCidade.setAdapter(adapterCidade);

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

        //recuperar estado do firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("filtros")
                .child("estado");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                estado.add("Escolha um estado");
                for(DataSnapshot dados: dataSnapshot.getChildren() ){
                    Estado est = dados.getValue(Estado.class);
                    estado.add(est.getNome());
                }
                adapterEstado.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String estadoSelecionado = (String) spEstado.getSelectedItem();

                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("filtros")
                        .child("cidade")
                        .child(estadoSelecionado);

                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cidade.clear();
                        cidade.add("Escolha uma cidade");
                        for(DataSnapshot dados: dataSnapshot.getChildren() ){
                            Cidade cid = dados.getValue(Cidade.class);
                            cidade.add(cid.getNome());
                        }
                        adapterCidade.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
            }
            );

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TestarPreenchimento()){
                    if(GravaPerfilBanco()) {
                        Toast.makeText(perfil.this, "Dados Gravados com Sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(perfil.this, "Erro ao Gravar os Dados", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private boolean TestarPreenchimento(){
        if(nome.getText() == null){
            Toast.makeText(perfil.this, "Necessário Preencher o Campo Nome", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dataNascimento.getText() == null){
            Toast.makeText(perfil.this, "Necessário Preencher o Campo Data de Nascimento", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spSexo.getSelectedItem().equals("Escolha seu sexo")){
            Toast.makeText(perfil.this, "Necessário Selecionar o Sexo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spEstado.getSelectedItem().equals("Escolha um estado")){
            Toast.makeText(perfil.this, "Necessário Selecionar o Estado", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spCidade.getSelectedItem().equals("Escolha uma cidade")){
            Toast.makeText(perfil.this, "Necessário Selecionar uma Cidade", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(celular.getText() == null){
            Toast.makeText(perfil.this, "Necessário Preencher o Campo Celular", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText() == null){
            Toast.makeText(perfil.this, "Necessário Preencher o Campo E-mail", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public boolean GravaPerfilBanco(){
        try {
            firebase = ConfiguracaoFirebase.getFirebase()
                    .child("usuarios")
                    .child(uiduser);
            firebase.child("nome").setValue(nome.getText().toString());
            firebase.child("dataNasc").setValue(dataNascimento.getText().toString());
            firebase.child("sexo").setValue(spSexo.getSelectedItem());
            firebase.child("estado").setValue(spEstado.getSelectedItem());
            firebase.child("cidade").setValue(spCidade.getSelectedItem());
            firebase.child("celular").setValue(celular.getText().toString());
            firebase.child("email").setValue(email.getText().toString());
            if (spConvenio.getSelectedItem().equals("Escolha um convênio"))
                firebase.child("convenio").setValue("Sem Convênio");
            else
                firebase.child("convenio").setValue(spConvenio.getSelectedItem());
            if (spPlano.getSelectedItem().equals("Escolha um plano"))
                firebase.child("plano").setValue("Sem Plano");
            else
                firebase.child("plano").setValue(spPlano.getSelectedItem());
            firebase.child("status").setValue(1);
            return true;
        }
        finally {
            return false;
        }
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

        Glide.with(perfil.this).load(this.firebaseUser.getPhotoUrl()).into(ivFoto);
    }

    private void inicializarComponentes() {
        ivFoto = (ImageView) findViewById(R.id.ivFoto);

        botaoLogoff = (Button) findViewById(R.id.botaoLogoff);
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
