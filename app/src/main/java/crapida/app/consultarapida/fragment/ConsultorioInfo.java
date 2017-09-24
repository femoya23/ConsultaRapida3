package crapida.app.consultarapida.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;

import crapida.app.consultarapida.R;
import crapida.app.consultarapida.Activity.Consultorio;
/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultorioInfo extends Fragment {


    public ConsultorioInfo() {
        // Required empty public constructor
    }


    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultorio_info, container, false);


    }



}
