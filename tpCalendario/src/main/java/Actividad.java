import java.time.LocalDateTime;
import java.util.ArrayList;

public class Actividad {

    private final String titulo;
    private final String descripcion;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private final ArrayList<Alarma> listaAlarmas;

    public Actividad(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.inicio = inicio;
        this.fin = fin;
        this.listaAlarmas = new ArrayList<>();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public ArrayList<Alarma> getListaAlarmas() {
        return listaAlarmas;
    }
}
