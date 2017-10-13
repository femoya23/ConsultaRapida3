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

    private int cidadeConfirmada;


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
                        if (cidadeConfirmada == 1) preencherCidade();
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


    }//final do método onCreate

    private boolean TestarPreenchimento(){
        if(nome.getText() == null){
            alerta("Necessário preencher o campo Nome");
            return false;
        }
        if(dataNascimento.getText() == null){
            alerta("Necessário preencher o campo Data de Nascimento");
            return false;
        }
        if(spSexo.getSelectedItem().equals("Escolha seu sexo")){
            alerta("Necessário preencher o campo Sexo");
            return false;
        }
        if(spEstado.getSelectedItem().equals("Escolha um estado")){
            alerta("Necessário preencher o campo Estado");
            return false;
        }
        if(spCidade.getSelectedItem().equals("Escolha uma cidade")){
            alerta("Necessário preencher o campo Cidade");
            return false;
        }
        if(celular.getText() == null){
            alerta("Necessário preencher o campo Celular");
            return false;
        }
        if(email.getText() == null){
            alerta("Necessário preencher o campo e-mail");
            return false;
        }
        return true;
    }

    private void alerta(String s) {
        Toast.makeText(perfil.this, s, Toast.LENGTH_SHORT).show();
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
        catch (ArrayIndexOutOfBoundsException e){
            alerta(e.toString());
            return false;
        }
    }


    private void preencherCampos(){
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    firebase = ConfiguracaoFirebase.getFirebase()
                    .child("usuarios")
                    .child(uiduser);
            //Toast.makeText(perfil.this, "recuperou", Toast.LENGTH_SHORT).show();


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

                //setar no spinner Sexo o valor do Firebase
                if(dadosPerfil.getSexo()!= null) {

                    int posicaoSexo = 0;
                    while (posicaoSexo <= sexo.length){

                        //String a = sexo[posicaoSexo];
                        //String b = dadosPerfil.getSexo();

                        if (sexo[posicaoSexo].equals(dadosPerfil.getSexo())){
                            spSexo.setSelection(posicaoSexo);
                            posicaoSexo = sexo.length+1;

                        } else{
                            posicaoSexo++;
                        }
                    }
                }

                //setar no spinner Estado o valor do Firebase
                if(dadosPerfil.getEstado()!= null) {

                    int posicaoEstado = 0;

                    while (posicaoEstado <= estado.size()){

                        if (estado.get(posicaoEstado).equals(dadosPerfil.getEstado())){
                            spEstado.setSelection(posicaoEstado);
                            int teste = cidade.size();
                            posicaoEstado = estado.size()+1;
                            cidadeConfirmada = 1;

                        } else{
                            posicaoEstado++;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void preencherCidade() {
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child(uiduser);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getChildren();
                DadosPerfil dadosPerfil = dataSnapshot.getValue(DadosPerfil.class);

                if(dadosPerfil.getCidade()!= null) {

                    int posicaoCidade = 0;
                    while (posicaoCidade <= cidade.size()){

                        if (cidade.get(posicaoCidade).equals(dadosPerfil.getCidade())){
                            spCidade.setSelection(posicaoCidade);
                            posicaoCidade = cidade.size()+1;

                        } else{
                            posicaoCidade++;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        cidadeConfirmada=0;
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
