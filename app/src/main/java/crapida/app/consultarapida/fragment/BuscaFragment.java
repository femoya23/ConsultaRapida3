package crapida.app.consultarapida.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;

import crapida.app.consultarapida.Model.Cidade;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.ConsultaMedicos;
import crapida.app.consultarapida.Model.Espec;
import crapida.app.consultarapida.Model.Estado;
import crapida.app.consultarapida.Model.Preferencias;
import crapida.app.consultarapida.R;
import crapida.app.consultarapida.Activity.Results;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscaFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_busca, container, false);


        //Instancia o Array List
        especialidade = new ArrayList<>();
        estados = new ArrayList<>();
        cidades = new ArrayList<>();
        nome = new ArrayList<>();
        end = new ArrayList<>();

        //Referencia objeto
        spespecialidade = (Spinner) view.findViewById(R.id.spespecialidade);
        spestado = (Spinner) view.findViewById(R.id.spestado);
        spcidade = (Spinner) view.findViewById(R.id.spcidade);
        botaopesquisar = (Button) view.findViewById(R.id.btpesquisar);

        //Monta Spinner
        adapterespecialidade = new ArrayAdapter(getActivity(),R.layout.spinner_busca,especialidade);
        spespecialidade.setAdapter(adapterespecialidade);

        adapterestado = new ArrayAdapter (getActivity(), R.layout.spinner_busca,estados);
        spestado.setAdapter(adapterestado);

        adaptercidade = new ArrayAdapter (getActivity(), R.layout.spinner_busca,cidades);
        spcidade.setAdapter(adaptercidade);



        //recuperar especialidades do firebase especialidade
        Preferencias preferencias = new Preferencias(getActivity());
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

                Intent intent = new Intent(getActivity(), Results.class);
                intent.putExtra("nome", nome);
                intent.putExtra("end", end);


                startActivity( intent);
            }
        });

        return view;

    }
}