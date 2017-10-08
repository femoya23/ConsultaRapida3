package crapida.app.consultarapida.fragment;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import android.widget.Toast;


import java.util.ArrayList;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.ListadeDatas;
import crapida.app.consultarapida.Model.ListadeHoras;
import crapida.app.consultarapida.Model.Preferencias;
import crapida.app.consultarapida.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultorioAgendamento extends Fragment {

    public String especialidadeSelect;
    public String estadoSelect;
    public String cidadeSelect;
    public String idNomeSelect;
    private Spinner spDatas;
    private Spinner spHoras;
    private ArrayAdapter adapterDatas;
    private ArrayAdapter adapterHoras;
    private ArrayList<String> listDatas;
    private ArrayList<String> listHoras;
    private DatabaseReference firebase;
    private Button botaoagendar;
    private AlertDialog.Builder dialog;
    private String dataselecionada;
    private String horaselect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultorio_agendamento, container, false);

        //Recupera Dados da Seleção
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        idNomeSelect = sharedPreferences.getString("idnome","idnome");
        estadoSelect = sharedPreferences.getString("estado","estado");
        especialidadeSelect = sharedPreferences.getString("especialidade","especialidade");
        cidadeSelect = sharedPreferences.getString("cidade","cidade");

        //Instancia o Array List
        listDatas = new ArrayList<>();
        listHoras = new ArrayList<>();

        //Referencia objeto
        spDatas = (Spinner) view.findViewById(R.id.spDatas);
        spHoras = (Spinner) view.findViewById(R.id.spHora);
        botaoagendar = (Button) view.findViewById(R.id.btagendar);

        //Monta Spinner
        adapterDatas = new ArrayAdapter(getActivity(),R.layout.spinner_busca,listDatas);
        spDatas.setAdapter(adapterDatas);

        adapterHoras = new ArrayAdapter (getActivity(), R.layout.spinner_busca,listHoras);
        spHoras.setAdapter(adapterHoras);

        //recuperar especialidades do firebase especialidade
        Preferencias preferencias = new Preferencias(getActivity());
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("medicos")
                .child(especialidadeSelect)
                .child(estadoSelect)
                .child(cidadeSelect)
                .child(idNomeSelect)
                .child("Agenda");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDatas.clear();
                listDatas.add("Escolha uma Data");
                for(DataSnapshot dados: dataSnapshot.getChildren() ){
                    ListadeDatas espec = dados.getValue(ListadeDatas.class);
                    listDatas.add(espec.getDatas().toString());
                }
                adapterDatas.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        spDatas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                dataselecionada = (String) spDatas.getSelectedItem();
                dataselecionada = "D" + dataselecionada.substring(6,10) + dataselecionada.substring(3,5) + dataselecionada.substring(0,2);
                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("medicos")
                        .child(especialidadeSelect)
                        .child(estadoSelect)
                        .child(cidadeSelect)
                        .child(idNomeSelect)
                        .child("Agenda")
                        .child(dataselecionada)
                        .child("Horarios");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listHoras.clear();
                        listHoras.add("Escolha um Horário");
                        for(DataSnapshot dados: dataSnapshot.getChildren() ){
                            ListadeHoras listadeHoras = dados.getValue(ListadeHoras.class);
                            if(listadeHoras.getStatus().equals("1"))
                            listHoras.add(listadeHoras.getHora().toString());
                        }
                        adapterHoras.notifyDataSetChanged();
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

        botaoagendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                horaselect = spHoras.getSelectedItem().toString();
                horaselect = horaselect.substring(0,2)+horaselect.substring(3,5);

                //Criar Alert Dialog
                dialog = new AlertDialog.Builder(getActivity());

                //Configurar Alert Dialog
                dialog.setTitle("Confirmação de Consulta");
                dialog.setMessage("Deseja confirmar sua consulta no " + idNomeSelect+" para o dia " + spDatas.getSelectedItem()
                + " as " + spHoras.getSelectedItem() + "?");

                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Consulta não agendada", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MarcadordeConsultas(dataselecionada, horaselect);
                    }
                });

                dialog.create();
                dialog.show();

            }
        });






        return view;

    }

public boolean MarcadordeConsultas(String data, String hora){

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    firebase = ConfiguracaoFirebase.getFirebase()
            .child("medicos")
            .child(especialidadeSelect)
            .child(estadoSelect)
            .child(cidadeSelect)
            .child(idNomeSelect)
            .child("Agenda")
            .child(data)
            .child("Horarios")
            .child(hora);
    firebase.child("Status").setValue("2");
    firebase.child("Paciente").setValue(currentFirebaseUser.getUid());
    String idconsulta = data + hora;
    firebase = ConfiguracaoFirebase.getFirebase()
            .child("usuarios")
            .child(currentFirebaseUser.getUid())
            .child("Consultas")
            .child(idconsulta);
    firebase.child("especialidade").setValue(especialidadeSelect);
    firebase.child("idnome").setValue(idNomeSelect);
    firebase.child("Data").setValue(data);
    firebase.child("Hora").setValue(hora);
    firebase.child("Status").setValue("2");


return true;
}

}
