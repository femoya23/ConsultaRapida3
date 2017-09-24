package crapida.app.consultarapida.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.Convenio;
import crapida.app.consultarapida.Model.Espec;
import crapida.app.consultarapida.Model.Estado;
import crapida.app.consultarapida.Model.Plano;
import crapida.app.consultarapida.Model.Preferencias;
import crapida.app.consultarapida.R;

/**
 * Created by wsabo on 23/09/2017.
 */

public class perfil extends Activity {

    private Spinner spConvenio;
    private Spinner spPlano;
    private ArrayList<String> convenio;
    private ArrayList<String> plano;
    private ArrayAdapter adapterConvenio;
    private ArrayAdapter adapterPlano;
    private DatabaseReference firebase;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        //Instancia o Array List
        convenio = new ArrayList<>();
        plano = new ArrayList<>();

        //Referencia objeto
        spConvenio = (Spinner) findViewById(R.id.convenioId);
        spPlano = (Spinner) findViewById(R.id.planoId);

        //Monta Spinner
        adapterConvenio = new ArrayAdapter (perfil.this, R.layout.spinner_busca,convenio);
        spConvenio.setAdapter(adapterConvenio);

        adapterPlano = new ArrayAdapter(perfil.this,R.layout.spinner_busca,plano);
        spPlano.setAdapter(adapterPlano);


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


    }
}
