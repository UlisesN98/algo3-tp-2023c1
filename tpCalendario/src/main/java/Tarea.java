import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tarea extends Actividad {

    private final boolean completada;

    public Tarea(Integer id, String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(id, titulo, descripcion, inicio, fin);
        this.completada = false;
    }
}
