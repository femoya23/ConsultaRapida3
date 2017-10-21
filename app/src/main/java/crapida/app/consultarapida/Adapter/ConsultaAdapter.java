package crapida.app.consultarapida.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import crapida.app.consultarapida.Model.ConsultaAgendada;
import crapida.app.consultarapida.Model.ConsultasExibicao;
import crapida.app.consultarapida.R;

/**
 * Created by fmoya on 15/10/2017.
 */

public class ConsultaAdapter extends ArrayAdapter<ConsultasExibicao> {
    private final Context context;
    private final ArrayList<ConsultasExibicao> elementos;

    public ConsultaAdapter(Context context, ArrayList<ConsultasExibicao> elementos) {
        super(context, R.layout.linha, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha, parent, false);
        TextView nomeConsultorio = (TextView) rowView.findViewById(R.id.nome);
        TextView endereco = (TextView) rowView.findViewById(R.id.endereco);
        TextView cidade = (TextView) rowView.findViewById(R.id.cidadeestado);
        TextView data = (TextView) rowView.findViewById(R.id.dataHora);
        ImageView imagem = (ImageView) rowView.findViewById(R.id.imagem);
        nomeConsultorio.setText(elementos.get(position).getNome());
        endereco.setText(elementos.get(position).getEnd());
        cidade.setText(elementos.get(position).getCidade());
        data.setText(elementos.get(position).getDataHora());
        imagem.setImageResource(elementos.get(position).getImagem());
        return rowView;
    }

}