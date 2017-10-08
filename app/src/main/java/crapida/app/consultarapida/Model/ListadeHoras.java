package crapida.app.consultarapida.Model;

/**
 * Created by Fernando on 30/09/2017.
 */

public class ListadeHoras {

    private String Hora;
    private String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ListadeHoras() {
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }
}
