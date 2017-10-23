package crapida.app.consultarapida.Activity;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import crapida.app.consultarapida.Adapter.TabAdapter;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.R;
import crapida.app.consultarapida.helper.SlidingTabLayout;

public class TelaPrin extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_prin);

        FirebaseApp.initializeApp(this);
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);

        //COnfiguração da Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Consulta Rápida");
        setSupportActionBar(toolbar);


        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );

        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager( viewPager );


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
                finish();
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
        LoginManager.getInstance().logOut();
        //Intent intent = new Intent(TelaPrin.this, MainActivity.class);
        //startActivity(intent);
        //finish();
    }

    public void abrirPerfil(){
        Intent intent = new Intent(TelaPrin.this,perfil.class);
        startActivity(intent);
    }
}
