package crapida.app.consultarapida;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class filtroBuscaActivity extends Activity {

    private Spinner spespecialidade;
    private Spinner spestado;
    private Spinner spcidade;
    private ArrayAdapter adapterespecialidade;
    private ArrayAdapter adapterestado;
    private ArrayAdapter adaptercidade;
    private ArrayList<String> especialidade;
    private ArrayList<String> estados;
    private ArrayList<String> cidades;
    private ArrayList<String> nome;
    private ArrayList<String> end;
    private DatabaseReference firebase;
    private Button botaopesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_busca);


        //Instancia o Array List
        especialidade = new ArrayList<>();
        estados = new ArrayList<>();
        cidades = new ArrayList<>();
        nome = new ArrayList<>();
        end = new ArrayList<>();

        //Referencia objeto
        spespecialidade = (Spinner) findViewById(R.id.spespecialidade);
        spestado = (Spinner) findViewById(R.id.spestado);
        spcidade = (Spinner) findViewById(R.id.spcidade);
        botaopesquisar = (Button) findViewById(R.id.btpesquisar);

        //Monta Spinner
        adapterespecialidade = new ArrayAdapter (this, R.layout.spinner_busca,especialidade);
        spespecialidade.setAdapter(adapterespecialidade);

        adapterestado = new ArrayAdapter (this, R.layout.spinner_busca,estados);
        spestado.setAdapter(adapterestado);

        adaptercidade = new ArrayAdapter (this, R.layout.spinner_busca,cidades);
        spcidade.setAdapter(adaptercidade);



        //recuperar especialidades do firebase especialidade
        Preferencias preferencias = new Preferencias(this);
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("filtros")
                .child("especialidade");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                especialidade.add("Escolha uma especialidade");
                for(DataSnapshot dados: dataSnapshot.getChildren() ){
                    Espec espec = dados.getValue(Espec.class);
                    especialidade.add(espec.getNome());
                }
                adapterespecialidade.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

            spespecialidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("filtros")
                        .child("estado");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        estados.clear();
                        estados.add("Escolha um estado");
                        for(DataSnapshot dados: dataSnapshot.getChildren() ){
                            Estado estado = dados.getValue(Estado.class);
                            estados.add(estado.getNome());
                        }
                        adapterestado.notifyDataSetChanged();
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

            spestado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    String estadoselecionado = (String) spestado.getSelectedItem();


                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("filtros")
                            .child("cidade")
                            .child(estadoselecionado);
                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            cidades.clear();
                            cidades.add("Escolha uma cidade");
                            for(DataSnapshot dados: dataSnapshot.getChildren() ){
                                Cidade cidade = dados.getValue(Cidade.class);
                                cidades.add(cidade.getNome());
                            }
                            adaptercidade.notifyDataSetChanged();
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


        botaopesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String especialidadeselecionado = (String) spespecialidade.getSelectedItem();
                String estadoselecionado = (String) spestado.getSelectedItem();
                String cidadeselecionado = (String) spcidade.getSelectedItem();

                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("medicos")
                        .child(especialidadeselecionado)
                        .child(estadoselecionado)
                        .child(cidadeselecionado);


                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Limpar lista
                        nome.clear();
                        end.clear();
                        //Listar contatos
                        for (DataSnapshot dados: dataSnapshot.getChildren() ){
                            ConsultaMedicos consultaMedicos = dados.getValue( ConsultaMedicos.class );
                            nome.add(consultaMedicos.getNome());
                            end.add(consultaMedicos.getEnd());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                Intent intent = new Intent(filtroBuscaActivity.this, Results.class);
                intent.putExtra("nome", nome);
                intent.putExtra("end", end);


                startActivity( intent);
            }
        });



    }
}
