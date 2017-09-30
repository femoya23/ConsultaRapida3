package crapida.app.consultarapida.Model;

/**
 * Created by Fernando on 24/09/2017.
 */

public class ConsultaConsultorio {

    private String Bairro;
    private String End;
    private String Experiencia;
    private String Nome;
    private Long Num;
    private String idNome;

    public ConsultaConsultorio() {
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public String getExperiencia() {
        return Experiencia;
    }

    public void setExperiencia(String experiencia) {
        Experiencia = experiencia;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Long getNum() {
        return Num;
    }

    public void setNum(Long num) {
        Num = num;
    }

    public String getIdNome() {
        return idNome;
    }

    public void setIdNome(String idNome) {
        this.idNome = idNome;
    }
}
