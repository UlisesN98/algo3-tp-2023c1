import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class Calendario {

    private LocalDateTime tiempoActual;
    private final ArrayList<Evento> listaEventos;
    private final ArrayList<Tarea> listaTareas;
    private final TreeSet<Alarma> listaAlarmas;

    public Calendario() {
        this.tiempoActual = LocalDateTime.now();
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
        this.listaAlarmas = new TreeSet<>(new Comparator<Alarma>() {
            @Override
            public int compare(Alarma o1, Alarma o2) {
                if (o1.getInicio().isBefore(o2.getInicio())){
                    return -1;
                }
                if (o1.getInicio().isAfter(o2.getInicio())){
                    return 1;
                }
                return 0;
            }
        });
    }

    public class ComparadorBusqueda implements Comparator<Actividad> {
        @Override
        public int compare(Actividad o1, Actividad o2){
            if (o1.getInicio().isBefore(o2.getInicio())){
                return -1;
            }
            if (o1.getInicio().isAfter(o2.getInicio())){
                return 1;
            }
            return 0;
        }
    }

    public void setTiempoActual(LocalDateTime tiempoActual) {this.tiempoActual = tiempoActual;}

    // METODOS DE BUSQUEDA
    public ArrayList<Evento> buscarEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var eventos = new ArrayList<Evento>();
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(titulo) && evento.getDescripcion().equals(descripcion) && evento.getInicio().equals(inicio) && evento.getFin().equals(fin)) {
                eventos.add(evento);
            }
        }
        return eventos;
    }

    public ArrayList<Tarea> buscarTarea(String titulo, String descripcion, LocalDateTime limite) {
        var tareas = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if (tarea.getTitulo().equals(titulo) && tarea.getDescripcion().equals(descripcion) && tarea.getFin().equals(limite)) {
                tareas.add(tarea);
            }
        }
        return tareas;
    }

    //public ArrayList<Evento> buscarEventoPorTitulo(String titulo){}
    //public ArrayList<Tarea> buscarTareaPorTitulo(String titulo){}
    //public ArrayList<Actividad> buscarPorTitulo(String titulo) {}

    public ArrayList<Evento> buscarEventoPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var eventosIntervalo = new ArrayList<Evento>();

        for (Evento evento : listaEventos) {
            if ((evento.getInicio().isAfter(inicioIntervalo) || evento.getInicio().isEqual(inicioIntervalo)) && evento.getInicio().isBefore(finIntervalo)) {
                eventosIntervalo.add(evento);
            } else if (evento.getFin().isAfter(inicioIntervalo) && evento.getFin().isBefore(finIntervalo)) {
                eventosIntervalo.add(evento);
            } else if (evento.getInicio().isBefore(inicioIntervalo) && evento.getFin().isAfter(finIntervalo)) {
                eventosIntervalo.add(evento);
            }

            if (evento.esRepetido()){
                TreeSet<LocalDateTime> instanciasRepetidas = evento.repeticionesPorIntervalo(inicioIntervalo, finIntervalo);
                for (LocalDateTime fecha : instanciasRepetidas){
                    Evento eventoCreado = new Evento(evento.getTitulo(), evento.getDescripcion(), evento.isDiaCompleto(), fecha, fecha.plusSeconds(evento.getDiferencia()), null);
                    eventosIntervalo.add(eventoCreado);
                }
            }
        }

        Collections.sort(eventosIntervalo, new ComparadorBusqueda());

        return eventosIntervalo;
    }

    public ArrayList<Tarea> buscarTareaPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var tareasIntervalo = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if ((tarea.getFin().isAfter(inicioIntervalo) || tarea.getFin().isEqual(inicioIntervalo)) && (tarea.getFin().isBefore(finIntervalo))) {
                tareasIntervalo.add(tarea);
            }
        }

        Collections.sort(tareasIntervalo, new ComparadorBusqueda());

        return tareasIntervalo;
    }

    public ArrayList<Actividad> buscarPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var listaActividades = new ArrayList<Actividad>();

        ArrayList<Evento> eventosIntervalo = buscarEventoPorIntervalo(inicioIntervalo, finIntervalo);
        ArrayList<Tarea> tareasIntervalo = buscarTareaPorIntervalo(inicioIntervalo, finIntervalo);

        listaActividades.addAll(eventosIntervalo);
        listaActividades.addAll(tareasIntervalo);

        Collections.sort(listaActividades, new ComparadorBusqueda());

        return listaActividades;
    }

    // METODOS DE CREACION
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);

        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                var nuevaAlarma = new Alarma(nuevoEvento, inicioAlarmas[i], efectoAlarmas[i]);
                nuevoEvento.agregarAlarma(nuevaAlarma);
                listaAlarmas.add(nuevaAlarma);
            }
        }

        listaEventos.add(nuevoEvento);
    }

    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite, limite);

        if (inicioAlarmas.length != 0) {
            for (int i = 0; i < inicioAlarmas.length; i++) {
                var nuevaAlarma = new Alarma(nuevaTarea, inicioAlarmas[i], efectoAlarmas[i]);
                nuevaTarea.agregarAlarma(nuevaAlarma);
                listaAlarmas.add(nuevaAlarma);
            }
        }

        listaTareas.add(nuevaTarea);
    }

    // METODOS DE MODIFICACION

    public void modificar(Actividad actividad, String nuevoTitulo, String nuevaDescripcion){
        if (nuevoTitulo != null){
            actividad.setTitulo(nuevoTitulo);
        }
        if (nuevaDescripcion != null){
            actividad.setDescripcion(nuevaDescripcion);
        }
    }

    public void modificar(Evento evento, LocalDateTime nuevoInicio, LocalDateTime nuevoFin){
        if (nuevoInicio != null){
            evento.setInicio(nuevoInicio);
        }
        if (nuevoFin != null){
            evento.setFin(nuevoFin);
        }
    }

    public void modificar(Tarea tarea, LocalDateTime nuevoLimite){
        tarea.setFin(nuevoLimite);
    }

    public void modificar(Evento evento, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        modificar(evento, nuevoTitulo, nuevaDescripcion);
        modificar(evento, nuevoInicio, nuevoFin);
    }

    public void modificar(Tarea tarea, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoLimite) {
        modificar(tarea, nuevoTitulo, nuevaDescripcion);
        modificar(tarea, nuevoLimite);
    }

    public void modificar(Evento evento, boolean diaCompleto) {
        evento.setDiaCompleto(diaCompleto);
    }

    public void modificar(Tarea tarea, boolean diaCompleto) {
        tarea.setDiaCompleto(diaCompleto);
    }

    public void modificar(Evento evento, Repeticion repeticion) {
        evento.setRepeticion(repeticion);
    }


    // METODOS DE ELIMINACION
    public void eliminarEvento(Evento evento) {
        for (Alarma alarma : evento.getListaAlarmas()) {
            listaAlarmas.remove(alarma);
        }
        listaEventos.remove(evento);
    }

    public void eliminarTarea(Tarea tarea) {
        for (Alarma alarma : tarea.getListaAlarmas()) {
            listaAlarmas.remove(alarma);
        }
        listaTareas.remove(tarea);
    }

    // METODOS DE COMPLETAR TAREA
    public boolean tareaEstaCompletada(Tarea tarea) {
        return tarea.isCompletada();
    }
    public void marcarTarea(Tarea tarea) {
        tarea.cambiarEstadoTarea();
    }

    // METODOS DE MODIFICACION RELATIVOS A ALARMAS
    public void agregarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(actividad, inicio, efecto);
        actividad.agregarAlarma(nuevaAlarma);
        listaAlarmas.add(nuevaAlarma);
    }

    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, LocalDateTime nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.modificarAlarma(alarma, nuevoInicio); //que ocurrira con el orden del treeset si hago esto, opcion 2 si modifico una alarma elimino la actual y creo una nueva con las caracteristicas modificadas
    }
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Efecto nuevoEfecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.modificarAlarma(alarma, nuevoEfecto); //lo mismo aca
    }

    public void eliminarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);
    }

    // METODOS RELATIVOS A LA EJECUCION DE LAS ALARMAS
    public Alarma obtenerProximaAlarma() {
        return listaAlarmas.first();
    }

    public boolean iniciaProximaAlarma () {
        return tiempoActual.equals(obtenerProximaAlarma().getInicio());
    }

    public Actividad dispararProximaAlarma() {
        Efecto efecto = obtenerProximaAlarma().getEfecto();
        Actividad actividad = obtenerProximaAlarma().getActividad();

        Alarma alarmaActual = obtenerProximaAlarma();
        actividad.eliminarAlarma(alarmaActual);
        listaAlarmas.remove(alarmaActual);

        return actividad;
    }
}