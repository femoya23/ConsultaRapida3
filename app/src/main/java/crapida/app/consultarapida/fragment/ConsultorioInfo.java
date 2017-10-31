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
import com.google.firebase.storage.StorageReference;
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
    private StorageReference mStorageRef;
   // private TextView nomeview;
    private TextView enderecoview;
    private TextView experienciaview;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_consultorio_info, container, false);

        final TextView nomeview = (TextView)view.findViewById(R.id.nomeConsultorioInfoId);
        final TextView enderecoview = (TextView)view.findViewById(R.id.enderecoConsultorioInfoId);
        final TextView experienciaview = (TextView)view.findViewById(R.id.experienciaConsultorioInfoId);
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

                String endereco = preencherDados(bdBairro, bdEnd, bdNum);
                nomeview.setText(bdNome);
                enderecoview.setText(endereco);
                experienciaview.setText(bdExperiencia);




            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
            });

        return view;
            }


        private String preencherDados (String bairro, String end, String num) {
        if(bairro==null)
            bairro="";
        else
            bairro = " - " + bairro;
        if(num==null)
            num="";
        else
            num=", " + num;
        String enderecoCompleto = end + num + bairro;
        return enderecoCompleto;
    }



}

