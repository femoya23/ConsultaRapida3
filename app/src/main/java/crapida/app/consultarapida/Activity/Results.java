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
import java.util.List;
import java.util.Map;
import com.google.firebase.database.DatabaseReference;
import crapida.app.consultarapida.R;


public class Results extends AppCompatActivity {

    private String especialidade;
    private String estado;
    private String cidade;
    private ArrayList nome;
    private ArrayList idnome;
    public String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        //Recupera dados da pesquisa
        Bundle extra = getIntent().getExtras();
        nome = extra.getStringArrayList("nome");
        ArrayList end = extra.getStringArrayList("end");
        idnome = extra.getStringArrayList("idnome");
        especialidade = extra.getString("especialidade");
        cidade = extra.getString("cidade");
        estado = extra.getString("estado");

        ListView resultsListView = (ListView) findViewById(R.id.results_listview);

        HashMap<Object, Object> nameAddresses = new HashMap<>();


        for (int i=0;i<end.size(); i++){
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


            }
        });




    }
}
