package crapida.app.consultarapida.Model;

/**
 * Created by Fernando on 10/09/2017.
 */

public class ConsultaMedicos {

    private String Nome;

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    private String End;

    private String idNome;

    private String bairro;
    private String experiencia;
    private String num;

    public ConsultaMedicos() {
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIdNome() {
        return idNome;
    }

    public void setIdNome(String idNome) {
        this.idNome = idNome;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}
