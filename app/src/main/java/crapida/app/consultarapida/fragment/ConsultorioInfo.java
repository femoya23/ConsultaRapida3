package crapida.app.consultarapida.fragment;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import crapida.app.consultarapida.Model.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import crapida.app.consultarapida.Model.ConsultaConsultorio;
import crapida.app.consultarapida.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultorioInfo extends Fragment{

    public ConsultorioInfo() {
        // Required empty public constructor
    }


    public String especialidadeSelect;
    public String estadoSelect;
    public String cidadeSelect;
    public String idNomeSelect;
    private DatabaseReference firebase;
    public String bdBairro;
    public String bdEnd;
    public String bdExperiencia;
    public String bdNome;
    public String bdNum;
    public String bdidNome;
    public TextView listaconvenio;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_consultorio_info, container, false);


        //Referencia Objeto

        //listaconvenio = (TextView) view.findViewById(R.id.conveniosId);

        //RECUPERA VALORES DO SHAREDPREFERENCES
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        idNomeSelect = sharedPreferences.getString("idnome","idnome");
        estadoSelect = sharedPreferences.getString("estado","estado");
        especialidadeSelect = sharedPreferences.getString("especialidade","especialidade");
        cidadeSelect = sharedPreferences.getString("cidade","cidade");

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

                ConsultaConsultorio consultaConsultorio = new ConsultaConsultorio();
                consultaConsultorio = dataSnapshot.getValue(ConsultaConsultorio.class);
                bdBairro = consultaConsultorio.getBairro();
                bdEnd = consultaConsultorio.getEnd();
                bdExperiencia = consultaConsultorio.getExperiencia();
                bdNome = consultaConsultorio.getNome();
                if(consultaConsultorio.getNum()!=null)
                bdNum = consultaConsultorio.getNum().toString();

                preencherDados(bdBairro, bdEnd, bdExperiencia, bdNome,bdNum);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
            });

        return inflater.inflate(R.layout.fragment_consultorio_info, container, false);
            }

    private void preencherDados (String bairro, String end, String experiencia, String nome, String num) {

        TextView nomeview = (TextView) getActivity().findViewById(R.id.nomeConsultorioInfoId);
        TextView enderecoview = (TextView) getActivity().findViewById(R.id.enderecoConsultorioInfoId);
        TextView experienciaview = (TextView) getActivity().findViewById(R.id.experienciaConsultorioInfoId);
        if(bairro==null)
            bairro="";
        else
            bairro = " - " + bairro;
        if(num==null)
            num="";
        else
            num=", " + num;

        String enderecoCompleto = end + num + bairro;
        try {
            nomeview.setText(nome);
        } finally {

        }
        try {
            enderecoview.setText(enderecoCompleto);
        }finally {

        }
        try {
            experienciaview.setText(experiencia);
        }finally {
        }


    }



}

