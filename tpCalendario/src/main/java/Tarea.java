import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tarea extends Actividad {

    private final boolean completada;

    public Tarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin, ArrayList<Alarma> listaAlarmas) {
        super(titulo, descripcion, inicio, fin, listaAlarmas);
        this.completada = false;
    }
}
