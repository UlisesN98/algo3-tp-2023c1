import java.time.LocalDateTime;
import java.util.HashMap;

public class Calendario {

    private Integer idEventos;
    private Integer idTareas;
    private final HashMap<Integer,Evento> listaEventos;
    private final HashMap<Integer,Tarea> listaTareas;

    public Calendario() {
        this.idEventos = 0;
        this.idTareas = 0;
        this.listaEventos = new HashMap<>();
        this.listaTareas = new HashMap<>();
    }

    public void crearEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevoEvento = new Evento(this.idEventos, titulo, descripcion, inicio, fin);
        listaEventos.put(this.idEventos, nuevoEvento);
        this.idEventos += 1;
    }

    public void modificarEvento() {

    }

    public void eliminarEvento() {

    }

    public void crearTarea() {

    }

    public void modificarTarea() {

    }

    public void eliminarTarea() {

    }

}
