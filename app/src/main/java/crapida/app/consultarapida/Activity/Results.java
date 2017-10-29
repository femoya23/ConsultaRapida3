package crapida.app.consultarapida.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.ConsultaMedicos;
import crapida.app.consultarapida.R;


public class Results extends AppCompatActivity {

    private String especialidade;
    private String estado;
    private String cidade;
    private ArrayList nome;
    private ArrayList idnome;
    private ArrayList end;
    private DatabaseReference firebase;
    public String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    private ListView resultsListView;
    private int tamanholista = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        FirebaseApp.initializeApp(this);

        //Recupera dados da pesquisa
        Bundle extra = getIntent().getExtras();
        idnome = extra.getStringArrayList("idnome");
        especialidade = extra.getString("especialidade");
        cidade = extra.getString("cidade");
        estado = extra.getString("estado");

        nome = new ArrayList();
        idnome = new ArrayList();
        end = new ArrayList();

            recuperarDados();

            //construirListView();
                resultsListView = (ListView) findViewById(R.id.results_listview);
        //Evento ao clicar no item da lista
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nomeSelect = (String) nome.get(position);
                String idnomeSelect = (String) idnome.get(position);
                Intent intent = new Intent(Results.this, Consultorio.class );
                intent.putExtra("nome", nomeSelect);
                intent.putExtra("idnome", idnomeSelect);
                intent.putExtra("especialidade", especialidade);
                intent.putExtra("estado", estado);
                intent.putExtra("cidade", cidade);
                startActivity(intent);
                killActivity();
            }
        });
    }
    public void killActivity(){
                finish();
    }

    public void recuperarDados(){

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("medicos")
                .child(especialidade)
                .child(estado)
                .child(cidade);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                //nome.clear();
                //end.clear();
                //idnome.clear();
                //Listar contatos
                try{
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    ConsultaMedicos consultaMedicos = dados.getValue( ConsultaMedicos.class );
                    nome.add(consultaMedicos.getNome());
                    end.add(consultaMedicos.getEnd());
                    idnome.add(consultaMedicos.getIdNome());
                    tamanholista = end.size();
                }}finally {
                    construirListView();
                }
            }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                    }
        );
    }

    public void construirListView(){
        LinkedHashMap nameAddresses = new LinkedHashMap();
        for (int i=0;i<tamanholista; i++){
            nameAddresses.put(nome.get(i), end.get(i));
        }
        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});
        Iterator it = nameAddresses.entrySet().iterator();
        while (it.hasNext())
        {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }
        resultsListView.setAdapter(adapter);
    }
}
