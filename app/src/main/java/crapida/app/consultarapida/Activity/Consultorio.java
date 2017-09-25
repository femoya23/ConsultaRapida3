package crapida.app.consultarapida.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import crapida.app.consultarapida.Adapter.TabAdapterConsultorio;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.R;
import crapida.app.consultarapida.helper.SlidingTabLayout;

public class Consultorio extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    public String nome;
    public String especialidade;
    public String estado;
    public String cidade;
    public String idnome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_prin);





        //Recebe Extras

        Bundle extra = getIntent().getExtras();
        nome = extra.getString("nome");
        idnome = extra.getString("idnome");
        especialidade = extra.getString("especialidade");
        estado = extra.getString("estado");
        cidade = extra.getString("cidade");


        //Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("idnome",idnome);
        editor.putString("especialidade", especialidade);
        editor.putString("estado",estado);
        editor.putString("cidade",cidade);
        editor.commit();


        //COnfiguração da Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nome);
        setSupportActionBar(toolbar);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configuração do Sliding
        TabAdapterConsultorio tabAdapter = new TabAdapterConsultorio( getSupportFragmentManager() );

        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager( viewPager );


        //Final Codigo novo


        //Configurar Adapter



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.action_settings:
                abrirPerfil();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void deslogarUsuario(){
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(Consultorio.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirPerfil(){
        Intent intent = new Intent(Consultorio.this,perfil.class);
        startActivity(intent);




    }


}

