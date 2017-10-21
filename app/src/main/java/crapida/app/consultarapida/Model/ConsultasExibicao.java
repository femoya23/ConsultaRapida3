package crapida.app.consultarapida.Model;

/**
 * Created by fmoya on 17/10/2017.
 */

public class ConsultasExibicao {

    private String nome;
    private String end;
    private String cidade;
    private String dataHora;
    private int imagem;

    public ConsultasExibicao(String nome, String end, String cidade, String dataHora, int imagem) {
        this.nome = nome;
        this.end = end;
        this.cidade = cidade;
        this.dataHora = dataHora;
        this.imagem = imagem;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
