package crapida.app.consultarapida.Activity;


import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import crapida.app.consultarapida.Adapter.TabAdapter;
import crapida.app.consultarapida.R;
import crapida.app.consultarapida.helper.SlidingTabLayout;

public class TelaPrin extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_prin);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);


        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        //Configurar Adapter
        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );

        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager( viewPager );


    }


}
