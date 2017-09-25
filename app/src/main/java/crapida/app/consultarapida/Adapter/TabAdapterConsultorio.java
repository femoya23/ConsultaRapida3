package crapida.app.consultarapida.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import crapida.app.consultarapida.fragment.BuscaFragment;
import crapida.app.consultarapida.fragment.Consultas;
import crapida.app.consultarapida.fragment.ConsultorioAgendamento;
import crapida.app.consultarapida.fragment.ConsultorioAvaliacao;
import crapida.app.consultarapida.fragment.ConsultorioInfo;

/**
 * Created by Fernando on 18/09/2017.
 */

public class TabAdapterConsultorio extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"INFORMAÇÕES","AGENDAMENTO","AVALIAÇÕES"};



    public TabAdapterConsultorio(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch ( position ){
            case 0 :
                fragment = new ConsultorioInfo();
                break;
            case 1 :
                fragment = new ConsultorioAgendamento();
                break;
            case 2 :
                fragment = new ConsultorioAvaliacao();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}



