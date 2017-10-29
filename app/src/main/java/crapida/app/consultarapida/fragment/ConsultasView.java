package crapida.app.consultarapida.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import crapida.app.consultarapida.Adapter.ConsultaAdapter;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import crapida.app.consultarapida.Model.ConsultaAgendada;
import crapida.app.consultarapida.Model.ConsultaConsultorio;
import crapida.app.consultarapida.Model.ConsultasExibicao;
import crapida.app.consultarapida.R;

import static crapida.app.consultarapida.R.id.nome;
import static crapida.app.consultarapida.R.id.spDatas;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultasView extends Fragment {

    public DatabaseReference firebase;
    public DatabaseReference firebase2;
    private ArrayList nomeConsultorio;
    private ArrayList idnome;
    private ArrayList cidade;
    private ArrayList data;
    private ArrayList hora;
    private ArrayList endereco;
    private ArrayList estado;
    private ArrayList Status;
    private ArrayList especialidade;
    private ConsultaConsultorio consultaConsultorio;
    private ConsultaAgendada consultaAgendada;
    private String endcomp;
    private String dataHora;
    private ArrayList<ConsultasExibicao> consultas;
    private ListView lista;
    private AlertDialog.Builder dialog;
    public ConsultasView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_consultas_view, container, false);

        nomeConsultorio = new ArrayList<>();
        idnome = new ArrayList<>();
        data = new ArrayList<>();
        hora = new ArrayList<>();
        endereco = new ArrayList<>();
        estado = new ArrayList<>();
        Status = new ArrayList<>();
        especialidade = new ArrayList<>();
        cidade = new ArrayList<>();
        consultaConsultorio = new ConsultaConsultorio();
        consultas = new ArrayList<>();
        lista = (ListView) view.findViewById(R.id.consultasId);

        adicionarConsultas();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog = new AlertDialog.Builder(getActivity());

                //Configurar Alert Dialog
                dialog.setTitle(consultas.get(position).getDataHora());
                if(consultas.get(position).getImagem()==R.mipmap.ic_agendado)
                dialog.setMessage("Sua consulta no consultório " + consultas.get(position).getNome() +
                 " ainda não foi confirmada. Para cancelar sua consulta clique no botão cancelar.");
                if(consultas.get(position).getImagem()==R.mipmap.ic_confirmada)
                    dialog.setMessage("Sua consulta no consultório " + consultas.get(position).getNome() +
                            " está confirmada. Para cancelar sua consulta clique no botão cancelar.");
                dialog.setNegativeButton("Cancelar Consulta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setTitle("Confirmação");
                        dialog.setMessage("Tem certeza que deseja cancelar sua consulta?");
                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelarConsulta(especialidade.get(position).toString(),
                                        estado.get(position).toString(),cidade.get(position).toString()
                                        ,data.get(position).toString(), hora.get(position).toString(), idnome.get(position).toString());
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }
                });
                dialog.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.create();
                dialog.show();
            }
        });
        return view;
    }

    private void adicionarConsultas() {
        //final int consults = 0;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child(currentFirebaseUser.getUid())
                .child("Consultas");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar lista
                consultas.clear();
                idnome.clear();
                estado.clear();
                cidade.clear();
                hora.clear();
                data.clear();
                especialidade.clear();
                //Listar contatos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                consultaAgendada = dados.getValue(ConsultaAgendada.class);
                idnome.add(consultaAgendada.getIdnome());
                estado.add(consultaAgendada.getEstado());
                cidade.add(consultaAgendada.getCidade());
                String dataAjustada = consultaAgendada.getData().substring(7,9) + "/" +
                        consultaAgendada.getData().substring(5,7) + "/" + consultaAgendada.getData().substring(1,5);
                String horaAjustada = consultaAgendada.getHora().substring(0,2) + ":" +
                        consultaAgendada.getHora().substring(2,4);
                dataHora = dataAjustada + " - " + horaAjustada;
                hora.add(horaAjustada);
                data.add(dataAjustada);
                especialidade.add(consultaAgendada.getEspecialidade());
                    ConsultasExibicao e;
                    if(consultaAgendada.getStatus().equals("2")) {
                        e = new ConsultasExibicao(consultaAgendada.getNome(),
                                consultaAgendada.getEndcomp(),
                                consultaAgendada.getCidade(),
                                dataHora, R.mipmap.ic_agendado);
                    }else{
                        e = new ConsultasExibicao(consultaAgendada.getNome(),
                                consultaAgendada.getEndcomp(),
                                consultaAgendada.getCidade(),
                                dataHora, R.mipmap.ic_confirmada);
                    }
                consultas.add(e);
               }

                criarAdapter();
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }
    public void criarAdapter(){
        ArrayAdapter adapter = new ConsultaAdapter(getActivity(), consultas);
        lista.setAdapter(adapter);
    }

    public void cancelarConsulta(String espec, String estadoCanc, String cidadeCanc, String dataCanc, String horaCanc, String idnomeCanc ){
        String dataAjust = transformarData(dataCanc);
        String horaAjust = transformarHora(horaCanc);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("medicos")
                .child(espec)
                .child(estadoCanc)
                .child(cidadeCanc)
                .child(idnomeCanc)
                .child("Agenda")
                .child(dataAjust)
                .child("Horarios")
                .child(horaAjust);
        firebase.child("Status").setValue("1");
        firebase.child("Paciente").setValue("");
        dataCanc = dataAjust+horaAjust;
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child(currentFirebaseUser.getUid())
                .child("Consultas");
        firebase.child(dataCanc).removeValue();
    }
    public String transformarData (String data){
        data = "D" + data.substring(6,10) + data.substring(3,5) + data.substring(0,2);
        return data;
    }

    public String transformarHora(String hora){
        hora = hora.substring(0,2) + hora.substring(3,5);
        return hora;
    }
}
