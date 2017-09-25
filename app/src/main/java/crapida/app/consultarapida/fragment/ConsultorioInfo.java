package crapida.app.consultarapida.fragment;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import crapida.app.consultarapida.Model.ConsultaMedicos;
import crapida.app.consultarapida.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultorioInfo extends Fragment{

    public ConsultorioInfo() {
        // Required empty public constructor
    }


    private String especialidadeSelect;
    private String estadoSelect;
    private String cidadeSelect;
    private String idNomeSelect;
    private DatabaseReference firebase;
    public ConsultaMedicos consultaMedicos = new ConsultaMedicos();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //RECUPERA VALORES DO SHAREDPREFERENCES
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        idNomeSelect = sharedPreferences.getString("idnome","idnome");
        estadoSelect = sharedPreferences.getString("estado","estado");
        especialidadeSelect = sharedPreferences.getString("especialidade","especialidade");
        cidadeSelect = sharedPreferences.getString("cidade","cidade");

        //Instacia Obj ConsultaMedicos


        //Recupera Informações do Consultorio
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("medicos")
                .child(especialidadeSelect)
                .child(estadoSelect)
                .child(cidadeSelect)
                .child(idNomeSelect);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                consultaMedicos = dataSnapshot.getValue(ConsultaMedicos.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });









        return inflater.inflate(R.layout.fragment_consultorio_info, container, false);
    }





}

