import java.time.LocalDateTime;
import java.util.ArrayList;

public class Actividad {

    private String titulo;
    private String descripcion;
    private LocalDateTime inicio;
    private LocalDateTime fin;
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
