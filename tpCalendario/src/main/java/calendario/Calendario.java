package calendario;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Calendario implements Serializable {

    private final List<Evento> listaEventos;
    private final List<Tarea> listaTareas;

    public Calendario() {
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
    }

    // Actualiza la repeticion actual de los eventos repetidos si esta
    // queda obsoleta de acuerdo a la fecha pasada por parametro
    public void actualizarEventosRepetidos(LocalDateTime fechaActual) {
        for (Evento evento : listaEventos) {
            if (!evento.esRepetido()) { continue; }

            if (evento.finRepeticionEsAnterior(fechaActual)) {
                evento.actualizarRepeticionActual(fechaActual);
            }
        }
    }

    // METODOS DE BUSQUEDA

    // Dado un intervalo de tiempo devuelve una Lista con los Eventos que ocurren en este.
    // La Lista está ordenado.
    public List<Evento> buscarEventoPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var eventosIntervalo = new ArrayList<Evento>();

        for (Evento evento : listaEventos) {
            if (evento.esRepetido()){
                TreeSet<LocalDateTime> instanciasRepetidas = evento.repeticionesPorIntervalo(inicioIntervalo, finIntervalo);
                for (LocalDateTime fecha : instanciasRepetidas){
                    EventoRepetido eventoCreado = evento.crearRepeticion(fecha);
                    eventosIntervalo.add(eventoCreado);
                }
            } else {
                if ((evento.getInicio().isAfter(inicioIntervalo) || evento.getInicio().isEqual(inicioIntervalo)) && evento.getInicio().isBefore(finIntervalo)) {
                    eventosIntervalo.add(evento);
                } else if (evento.getFin().isAfter(inicioIntervalo) && evento.getFin().isBefore(finIntervalo)) {
                    eventosIntervalo.add(evento);
                } else if (evento.getInicio().isBefore(inicioIntervalo) && evento.getFin().isAfter(finIntervalo)) {
                    eventosIntervalo.add(evento);
                }
            }
        }
        eventosIntervalo.sort(new Actividad.ComparadorActividad());
        return eventosIntervalo;
    }

    // Dado un intervalo de tiempo devuelve una Lista con las Tareas que ocurren en este.
    // La Lista está ordenado.
    public List<Tarea> buscarTareaPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var tareasIntervalo = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if ((tarea.getInicio().isAfter(inicioIntervalo) || tarea.getInicio().isEqual(inicioIntervalo)) && (tarea.getInicio().isBefore(finIntervalo))) {
                tareasIntervalo.add(tarea);
            }
        }
        tareasIntervalo.sort(new Actividad.ComparadorActividad());
        return tareasIntervalo;
    }

    // Dado un intervalo de tiempo devuelve una Lista con los Eventos y Tareas que ocurren en este.
    // La Lista está ordenado.
    public List<Actividad> buscarPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var listaActividades = new ArrayList<Actividad>();

        List<Evento> eventosIntervalo = buscarEventoPorIntervalo(inicioIntervalo, finIntervalo);
        List<Tarea> tareasIntervalo = buscarTareaPorIntervalo(inicioIntervalo, finIntervalo);

        listaActividades.addAll(eventosIntervalo);
        listaActividades.addAll(tareasIntervalo);

        listaActividades.sort(new Actividad.ComparadorActividad());
        return listaActividades;
    }

    // METODOS DE CREACION

    // Recibe un String que indique su título, un String que indique una descripcion, un boolean que indique si es de día completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique la fecha y hora absoluta de sus alarmas, un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de qué modo se repite. A partir de estas caracteristicas se crea una
    // instancia de Evento que se guardara en la lista de Eventos.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        nuevoEvento.agregarAlarmas(inicioAlarmas, efectoAlarmas);
        nuevoEvento.setRepeticionActual();
        listaEventos.add(nuevoEvento);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique el intervalo de tiempo previo de sus alarmas, un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de qué modo se repite. A partir de estas caracteristicas se crea una
    // instancia de Evento que se guardara en la lista de Eventos.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Duration[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
        var nuevoEvento = new Evento(titulo, descripcion, diaCompleto, inicio, fin, repeticion);
        nuevoEvento.agregarAlarmas(inicioAlarmas, efectoAlarmas);
        nuevoEvento.setRepeticionActual();
        listaEventos.add(nuevoEvento);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique la fecha y hora absoluta de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracteristicas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, LocalDateTime[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite);
        nuevaTarea.agregarAlarmas(inicioAlarmas, efectoAlarmas);
        listaTareas.add(nuevaTarea);
    }

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique el intervalo de tiempo previo de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracteristicas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, Duration[] inicioAlarmas, Efecto[] efectoAlarmas) {
        var nuevaTarea = new Tarea(titulo, descripcion, diaCompleto, limite);
        nuevaTarea.agregarAlarmas(inicioAlarmas, efectoAlarmas);
        listaTareas.add(nuevaTarea);
    }

    // METODOS DE MODIFICACION

    // Recibe una instancia de Evento o Tarea y modifica su titulo y su descripcion, o únicamente uno de los dos en
    // caso de que uno de los parametros sea null.
    public void modificar(Actividad actividad, String nuevoTitulo, String nuevaDescripcion) {
        if (nuevoTitulo != null){
            actividad.setTitulo(nuevoTitulo);
        }
        if (nuevaDescripcion != null){
            actividad.setDescripcion(nuevaDescripcion);
        }
    }

    // Recibe una instancia de Evento y modifica su inicio y su fin, o únicamente uno de los dos en
    // caso de que uno de los parametros sea null.
    public void modificar(Evento evento, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        if (nuevoInicio != null){
            evento.setInicio(nuevoInicio);
        }
        if (nuevoFin != null){
            evento.setFin(nuevoFin);
        }
    }

    // Recibe una instancia de Tarea y modifica su limite.
    public void modificar(Tarea tarea, LocalDateTime nuevoLimite){
        if (nuevoLimite != null){
            tarea.setInicio(nuevoLimite);
        }
    }

    // Recibe una instancia de Evento y modifica su titulo, descripcion, inicio y fin, o únicamente los parametros pasados distintos de null.
    public void modificar(Evento evento, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        modificar(evento, nuevoTitulo, nuevaDescripcion);
        modificar(evento, nuevoInicio, nuevoFin);
    }

    // Recibe una instancia de Tarea y modifica su titulo, descripcion y limite, o únicamente los parametros pasados distintos de null.
    public void modificar(Tarea tarea, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoLimite) {
        modificar(tarea, nuevoTitulo, nuevaDescripcion);
        modificar(tarea, nuevoLimite);
    }

    // Recibe una instancia de Evento y segun el parametro pasado cambia su estado de dia completo.
    public void modificar(Evento evento, boolean diaCompleto) {
        evento.setDiaCompleto(diaCompleto);
    }

    // Recibe una instancia de Tarea y segun el parametro pasado cambia su estado de dia completo.
    public void modificar(Tarea tarea, boolean diaCompleto) {
        tarea.setDiaCompleto(diaCompleto);
    }

    // METODOS DE ELIMINACION

    // Recibe una instancia de Evento y la quita de la lista de Eventos.
    public void eliminarEvento(Evento evento) {
        if (evento.esOriginal()) {
            listaEventos.remove(evento);
        } else {
            var eventoRep = (EventoRepetido) evento;
            listaEventos.remove(eventoRep.getEventoOriginal());
        }
    }

    // Recibe una instancia de Tarea y la quita de la lista de Tareas.
    public void eliminarTarea(Tarea tarea) {
        listaTareas.remove(tarea);
    }


    // METODOS DE COMPLETAR TAREA

    // Recibe una instancia de Tarea y devuelve un boolean que indica si esta completa o no.
    public boolean tareaEstaCompletada(Tarea tarea) {
        return tarea.isCompletada();
    }

    // Recibe una instancia de Tarea y dependiendo de su estado la cambia a completa o incompleta.
    public void marcarTarea(Tarea tarea) {
        tarea.cambiarEstadoTarea();
    }


    // METODOS RELATIVOS A ALARMAS

    // Recibe una instancia de Evento o Tarea y agrega le agrega una alarma con las
    // caracteristicas pasadas por parametro.
    public void agregarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        actividad.agregarAlarma(inicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y agrega le agrega una alarma con las
    // caracteristicas pasadas por parametro.
    public void agregarAlarma(Actividad actividad, Duration inicio, Efecto efecto) {
        actividad.agregarAlarma(inicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, LocalDateTime nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        actividad.agregarAlarma(nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Duration nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        actividad.agregarAlarma(nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Efecto nuevoEfecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        actividad.agregarAlarma(inicio, nuevoEfecto);
    }

    // Recibe una instancia de Evento o Tarea y le elimina la alarma que tiene las caracteristicas indicadas.
    public void eliminarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
    }

    // Devuelve la siguiente alarma que deberia sonar basandose en la fecha pasada.
    // Devuelve null si no hay ninguna alarma.
    public Alarma obtenerProximaAlarma(LocalDateTime tiempoActual) {
        Alarma proximaAlarma = null;
        for (Evento evento : listaEventos) {
            if (evento.esRepetido() && evento.tieneSiguienteRepeticion()) {
                proximaAlarma = actualizarProximaAlarma(evento.getRepeticionActual(), proximaAlarma, tiempoActual);
            } else {
                proximaAlarma = actualizarProximaAlarma(evento, proximaAlarma, tiempoActual);
            }
        }
        for (Tarea tarea : listaTareas) {
            proximaAlarma = actualizarProximaAlarma(tarea, proximaAlarma, tiempoActual);
        }
        return proximaAlarma;
    }

    private Alarma actualizarProximaAlarma(Actividad actividad, Alarma proximaAlarma, LocalDateTime tiempoActual) {
        Alarma alarma = actividad.obtenerProximaAlarma(tiempoActual);
        if (alarma == null) {
            return proximaAlarma;
        }
        if (proximaAlarma == null) {
            return alarma;
        }
        if (alarma.esAnterior(proximaAlarma)) {
            return alarma;
        }
        return proximaAlarma;
    }

    // Metodo que muestra, basándose en una fecha y hora indicada,
    // si debe sonar la proxima alarma.
    public boolean iniciaProximaAlarma (LocalDateTime tiempoActual) {
        return obtenerProximaAlarma(tiempoActual) != null && tiempoActual.equals(obtenerProximaAlarma(tiempoActual).getInicio());
    }

    // METODOS DE SERIALIZACION

    public void serializar(OutputStream os) throws IOException {
        ObjectOutputStream objectOutStream = new ObjectOutputStream(os);
        objectOutStream.writeObject(this);
        objectOutStream.flush();
    }

    public static Calendario deserializar(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInStream = new ObjectInputStream(is);
        return (Calendario) objectInStream.readObject();
    }
}
