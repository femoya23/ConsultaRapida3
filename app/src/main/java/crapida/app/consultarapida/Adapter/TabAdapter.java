package crapida.app.consultarapida.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import crapida.app.consultarapida.fragment.BuscaFragment;
import crapida.app.consultarapida.fragment.Consultas;

/**
 * Created by Fernando on 18/09/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"BUSCA","CONSULTAS","TESTE"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch ( position ){
            case 0 :
                fragment = new BuscaFragment();
                break;
            case 1 :
                fragment = new Consultas();
                break;
            case 2 :
                fragment = new BuscaFragment();
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
