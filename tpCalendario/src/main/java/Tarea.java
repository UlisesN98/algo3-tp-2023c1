import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tarea extends Actividad {

    private final boolean completada;

    public Tarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
        this.completada = false;
    }

    public boolean isCompletada() {
        return completada;
    }
}
