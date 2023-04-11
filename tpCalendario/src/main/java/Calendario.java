import java.time.LocalDateTime;
import java.util.ArrayList;

public class Calendario {

    private Integer idEventos;
    private Integer idTareas;
    private final ArrayList<Evento> listaEventos;
    private final ArrayList<Tarea> listaTareas;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
    }

    public void crearEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevoEvento = new Evento(titulo, descripcion, inicio, fin);
        listaEventos.add(nuevoEvento);
    }

    public void modificarEvento() {

    }

    public void eliminarEvento() {

    }

    public void crearTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevaTarea = new Tarea(titulo, descripcion, inicio, fin);
        listaTareas.add(nuevaTarea);
    }

    public void modificarTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        
    }

    public void eliminarTarea() {

    }

}
