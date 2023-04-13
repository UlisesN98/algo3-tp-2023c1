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

    public void crearEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Integer[] efectoAlarmas) {
        var nuevoEvento = new Evento(titulo, descripcion, inicio, fin);
        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                nuevoEvento.agregarAlarma(inicioAlarmas[i], efectoAlarmas[i]);
            }
        }
        listaEventos.add(nuevoEvento);
    }

    public void modificarEvento(Evento evento, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        evento.setTitulo(nuevoTitulo);
        evento.setDescripcion(nuevaDescripcion);
        evento.setInicio(nuevoInicio);
        evento.setFin(nuevoFin);
    }

    public void eliminarEvento(Evento evento) {
        listaEventos.remove(evento);
    }

    public Tarea buscarTarea(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        for (Tarea tarea : listaTareas) {
            if (tarea.getTitulo().equals(titulo) && tarea.getDescripcion().equals(descripcion) && tarea.getInicio().equals(inicio) && tarea.getFin().equals(fin)) {
                return tarea;
            }
        }
        return null;
    }

    public void crearTarea(String titulo, String descripcion, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Integer[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, fin, fin);
        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                nuevaTarea.agregarAlarma(inicioAlarmas[i], efectoAlarmas[i]);
            }
        }
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

    public void cambiarEstadoTarea(Tarea tarea) {
        if (tarea.getCompletada()) {
            tarea.setCompletada(false);
        }
        tarea.setCompletada(true);
    }
}
