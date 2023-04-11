import java.time.LocalDateTime;
import java.util.ArrayList;

public class Evento extends Actividad {

    public Evento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        super(titulo, descripcion, inicio, fin);
    }
}
