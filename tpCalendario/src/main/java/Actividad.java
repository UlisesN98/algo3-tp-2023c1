import java.time.LocalDateTime;
import java.util.ArrayList;

public class Actividad {

    private final String titulo;
    private final String descripcion;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private final ArrayList<Alarma> listaAlarmas;

    public Actividad(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin, ArrayList<Alarma> listaAlarmas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.inicio = inicio;
        this.fin = fin;
        this.listaAlarmas = new ArrayList<>();
    }
}
