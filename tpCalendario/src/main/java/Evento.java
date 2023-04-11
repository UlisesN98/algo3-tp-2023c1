import java.time.LocalDateTime;
import java.util.ArrayList;

public class Evento extends Actividad {

    public Evento(Integer id, String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(id, titulo, descripcion, inicio, fin);
    }
}
