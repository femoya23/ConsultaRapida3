package crapida.app.consultarapida.Model;

/**
 * Created by fmoya on 29/10/2017.
 */

public class gravaAgendamento {
    private String especialidade;
    private String idnome;
    private String Data;
    private String Hora;
    private String Estado;
    private String Cidade;
    private String Status;
    private String endcomp;
    private String Nome;

    public gravaAgendamento() {
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getIdnome() {
        return idnome;
    }

    public void setIdnome(String idnome) {
        this.idnome = idnome;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEndcomp() {
        return endcomp;
    }

    public void setEndcomp(String endcomp) {
        this.endcomp = endcomp;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}
