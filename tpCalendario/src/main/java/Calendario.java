import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Calendario {

    private final ArrayList<Evento> listaEventos;
    private final ArrayList<Tarea> listaTareas;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
    }

    public Evento buscarEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(titulo) && evento.getDescripcion().equals(descripcion) && evento.getInicio().equals(inicio) && evento.getFin().equals(fin)) {
                return evento;
            }
        }
        return null;
    }

    public void crearEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevoEvento = new Evento(titulo, descripcion, inicio, fin);
        listaEventos.add(nuevoEvento);
    }

    public void modificarEvento(Evento evento, String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        evento.setTitulo(titulo);
        evento.setDescripcion(descripcion);
        evento.setInicio(inicio);
        evento.setFin(fin);
    }

    public void eliminarEvento(Evento evento) {
        listaEventos.remove(evento);
    }

    public void crearTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var nuevaTarea = new Tarea(titulo, descripcion, inicio, fin);
        listaTareas.add(nuevaTarea);
    }

    public void modificarTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        for (int i = 0; i < listaTareas.size(); i++){
            if (Objects.equals(listaTareas.get(i).getTitulo(), titulo)) {
                var tareaModificada = new Tarea(titulo, descripcion, inicio, fin);
                listaTareas.set(i, tareaModificada);
            }
        }
    }

    public void eliminarTarea(String titulo) {
        for (int i = 0; i < listaTareas.size(); i++){
            if (Objects.equals(listaEventos.get(i).getTitulo(), titulo)) {
                listaTareas.remove(i);
                i -= 1;
            }
        }
    }

}
