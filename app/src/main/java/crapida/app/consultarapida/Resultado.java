package crapida.app.consultarapida;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Resultado extends Activity {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> pesquisa;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        //Instânciar objetos
        pesquisa = new ArrayList<>();

        //Monta listview e adapter
        listView = (ListView) findViewById(R.id.lv_pesquisa);
        adapter = new ArrayAdapter(this,
                R.layout.spinner_busca,
                pesquisa
        );
        listView.setAdapter( adapter );


        //Recupera dados da pesquisa

        Bundle extra = getIntent().getExtras();
        String especialidadeselect = extra.getString("especialidade");
        String estadoselect = extra.getString("estado");
        String cidadeselect = extra.getString("cidade");

        //Recuperar contatos do firebase

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("medicos")
                .child( especialidadeselect )
                .child(estadoselect)
                .child(cidadeselect);

        //Listener para recuperar contatos
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                pesquisa.clear();

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    ConsultaMedicos consultaMedicos = dados.getValue( ConsultaMedicos.class );
                    pesquisa.add(consultaMedicos.getNome());


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








    }
}
