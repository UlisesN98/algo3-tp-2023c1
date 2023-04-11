import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Calendario {

    private Integer sizeListaEventos;
    private Integer sizeListaTareas;
    private final ArrayList<Evento> listaEventos;
    private final ArrayList<Tarea> listaTareas;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.sizeListaEventos = 0;
        this.listaTareas = new ArrayList<>();
        this.sizeListaTareas = 0;
    }

    public void crearEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevoEvento = new Evento(titulo, descripcion, inicio, fin);
        listaEventos.add(nuevoEvento);
        sizeListaEventos += 1;
    }

    public void modificarEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        for (int i = 0; i < sizeListaEventos; i++){
            if Objects.equals(listaEventos.get(i).getTitulo(), titulo) {
                var eventoModificado = new Evento(titulo, descripcion, inicio, fin);
                listaEventos.set(i, eventoModificado);
            }
        }
    }

    public void eliminarEvento(String titulo) {
        for (int i = 0; i < sizeListaEventos; i++){
            if Objects.equals(listaEventos.get(i).getTitulo(), titulo) {
                listaEventos.remove(i);
                sizeListaEventos -= 1;
            }
        }
    }

    public void crearTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevaTarea = new Tarea(titulo, descripcion, inicio, fin);
        listaTareas.add(nuevaTarea);
        sizeListaTareas += 1;
    }

    public void modificarTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        for (int i = 0; i < sizeListaTareas; i++){
            if Objects.equals(listaTareas.get(i).getTitulo(), titulo) {
                var tareaModificada = new Tarea(titulo, descripcion, inicio, fin);
                listaTareas.set(i, tareaModificada);
            }
        }
    }

    public void eliminarTarea(String titulo) {
        for (int i = 0; i < sizeListaTareas; i++){
            if Objects.equals(listaEventos.get(i).getTitulo(), titulo) {
                listaTareas.remove(i);
                sizeListaTareas -= 1;
            }
        }
    }

}
