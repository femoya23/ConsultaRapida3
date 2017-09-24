package crapida.app.consultarapida.Model;

/**
 * Created by wsabo on 23/09/2017.
 */

public class ConsultorioSelect {

    public String especialidade;
    public String cidade;
    public String estado;
    public String nomeselect;

    public ConsultorioSelect() {
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNomeselect() {
        return nomeselect;
    }

    public void setNomeselect(String nomeselect) {
        this.nomeselect = nomeselect;
    }
}
