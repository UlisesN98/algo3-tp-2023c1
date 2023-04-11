import java.time.LocalDateTime;
import java.util.ArrayList;

public class Actividad {

    private final Integer id;
    private final String titulo;
    private final String descripcion;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private final ArrayList<Alarma> listaAlarmas;

    public Actividad(Integer id, String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.inicio = inicio;
        this.fin = fin;
        this.listaAlarmas = new ArrayList<>();
    }
}
