import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class Calendario {

    private LocalDateTime tiempoActual;  // Fecha y hora actual
    private final ArrayList<Evento> listaEventos; // ArrayList que contiene Eventos
    private final ArrayList<Tarea> listaTareas; // ArrayList que contiene Tareas
    private final TreeSet<Alarma> listaAlarmas; // Treeset que contiena las Alarmas existentes ordenadas por orden aparicion

    public Calendario() {
        this.tiempoActual = LocalDateTime.now();
        this.listaEventos = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
        this.listaAlarmas = new TreeSet<>((o1, o2) -> {
            if (o1.getInicio().isBefore(o2.getInicio())) {
                return -1;
            }
            if (o1.getInicio().isAfter(o2.getInicio())) {
                return 1;
            }
            return 0;
        });
    }

    // Metodo requerido para hacer comparaciones que permitan ordenar Eventos y Tareas temporalmente
    public static class ComparadorBusqueda implements Comparator<Actividad> {
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

    // Metodo para cambiar arbitrariamente la fecha y hora actual del Calendario
    public void setTiempoActual(LocalDateTime tiempoActual) {this.tiempoActual = tiempoActual;}


    // Metodo que actualiza las fechas de un evento repetido en caso de que este ya haya ocurrido.
    public void actualizarEventosRepetidos() {
        for (Evento evento : listaEventos) {
            if (!evento.esRepetido()) {continue;}
            if (evento.getFin().isBefore(tiempoActual)) {
                evento.actualizarSiguienteRepeticion();
            }
        }
    }

    // METODOS DE BUSQUEDA

    // Busca Eventos a partir sus caracteristicas principales (titulo, descripcion, fecha de inicio y fecha de fin)
    // y devuelve un ArrayList con aquellos que las tienen
    public ArrayList<Evento> buscarEvento(String titulo, String descripcion, LocalDateTime inicio, LocalDateTime fin) {
        var eventos = new ArrayList<Evento>();
        for (Evento evento : listaEventos) {
            if (evento.getTitulo().equals(titulo) && evento.getDescripcion().equals(descripcion) && evento.getInicio().equals(inicio) && evento.getFin().equals(fin)) {
                eventos.add(evento);
            }
        }
        return eventos;
    }

    // Busca Tareas a partir sus caracteristicas principales (titulo, descripcion y fecha limite)
    // y devuelve un ArrayList con aquellas que las tienen
    public ArrayList<Tarea> buscarTarea(String titulo, String descripcion, LocalDateTime limite) {
        var tareas = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if (tarea.getTitulo().equals(titulo) && tarea.getDescripcion().equals(descripcion) && tarea.getFin().equals(limite)) {
                tareas.add(tarea);
            }
        }
        return tareas;
    }

    // Dado un intervalo de tiempo devuelve un ArrayList con los Eventos que ocurren en este.
    // El ArrayList esta ordenado.
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
                    Evento eventoCreado = new Evento(evento.getTitulo(), evento.getDescripcion(), evento.isDiaCompleto(), fecha, fecha.plusSeconds(ChronoUnit.SECONDS.between(evento.getInicio(), evento.getFin())), null);
                    eventosIntervalo.add(eventoCreado);
                }
            }
        }
        eventosIntervalo.sort(new ComparadorBusqueda());
        return eventosIntervalo;
    }

    // Dado un intervalo de tiempo devuelve un ArrayList con las Tareas que ocurren en este.
    // El ArrayList esta ordenado.
    public ArrayList<Tarea> buscarTareaPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var tareasIntervalo = new ArrayList<Tarea>();
        for (Tarea tarea : listaTareas) {
            if ((tarea.getFin().isAfter(inicioIntervalo) || tarea.getFin().isEqual(inicioIntervalo)) && (tarea.getFin().isBefore(finIntervalo))) {
                tareasIntervalo.add(tarea);
            }
        }
        tareasIntervalo.sort(new ComparadorBusqueda());
        return tareasIntervalo;
    }

    // Dado un intervalo de tiempo devuelve un ArrayList con los Eventos y Tareas que ocurren en este.
    // El ArrayList esta ordenado.
    public ArrayList<Actividad> buscarPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        var listaActividades = new ArrayList<Actividad>();

        ArrayList<Evento> eventosIntervalo = buscarEventoPorIntervalo(inicioIntervalo, finIntervalo);
        ArrayList<Tarea> tareasIntervalo = buscarTareaPorIntervalo(inicioIntervalo, finIntervalo);

        listaActividades.addAll(eventosIntervalo);
        listaActividades.addAll(tareasIntervalo);

        listaActividades.sort(new ComparadorBusqueda());
        return listaActividades;
    }


    // METODOS DE CREACION

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique la fecha y hora absoluta de sus alarmas, un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de que modo se repite. A partir de estas caracterisiticas se crea una
    // instancia de Evento que se guardara en la lista de Eventos. A su vez, guarda las alarmas del Evento en el treeset de Alarmas.
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

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo
    // un LocalDateTime que indique su fecha y hora de inicio, un LocalDateTime que indique su fecha y hora de fin, un array que
    // indique el intervalo de tiempo previo de sus alarmas , un array de la misma longitud que el anterior que indique el efecto de estas
    // alarmas y una instancia de Repeticion que indique de que modo se repite. A partir de estas caracterisiticas se crea una
    // instancia de Evento que se guardara en la lista de Eventos. A su vez, guarda las alarmas del Evento en el treeset de Alarmas.
    public void crearEvento(String titulo, String descripcion, boolean diaCompleto, LocalDateTime inicio, LocalDateTime fin, Duration[] inicioAlarmas, Efecto[] efectoAlarmas, Repeticion repeticion) {
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

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique la fecha y hora absoluta de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracterisiticas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas. A su vez, guarda las alarmas de la Tarea en el treeset de Alarmas.
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

    // Recibe un String que indique su titulo, un String que indique una descripcion, un boolean que indique si es de dia completo,
    // un LocalDateTime que indique su fecha y hora limite, un array que indique el intervalo de tiempo previo de sus alarmas, un array
    // de la misma longitud que el anterior que indique el efecto de estas alarmas. A partir de estas caracterisiticas se crea una
    // instancia de Tarea que se guardara en la lista de Tareas. A su vez, guarda las alarmas de la Tarea en el treeset de Alarmas.
    public void crearTarea(String titulo, String descripcion, boolean diaCompleto, LocalDateTime limite, Duration[] inicioAlarmas, Efecto[] efectoAlarmas) {
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

    // Recibe una instancia de Evento o Tarea y modifica su titulo y su descripcion, o unicamente uno de los dos en
    // caso de que uno de los parametros sea null.
    public void modificar(Actividad actividad, String nuevoTitulo, String nuevaDescripcion) {
        if (nuevoTitulo != null){
            actividad.setTitulo(nuevoTitulo);
        }
        if (nuevaDescripcion != null){
            actividad.setDescripcion(nuevaDescripcion);
        }
    }

    // Recibe una instancia de Evento y modifica su inicio y su fin, o unicamente uno de los dos en
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
        tarea.setFin(nuevoLimite);
    }

    // Recibe una instancia de Evento y modifica su titulo, descripcion, inicio y fin, o unicamente los parametros pasados distintos de null.
    public void modificar(Evento evento, String nuevoTitulo, String nuevaDescripcion, LocalDateTime nuevoInicio, LocalDateTime nuevoFin) {
        modificar(evento, nuevoTitulo, nuevaDescripcion);
        modificar(evento, nuevoInicio, nuevoFin);
    }

    // Recibe una instancia de Tarea y modifica su titulo, descripcion y limite, o unicamente los parametros pasados distintos de null.
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

    // Modifica la Repeticion de un Evento tras pasarle una nueva instancia de esta con las nuevas caracteristicas requeridas.
    public void modificar(Evento evento, Repeticion repeticion) {
        evento.setRepeticion(repeticion);
    }


    // METODOS DE ELIMINACION

    // Recibe una instancia de Evento y la quita de la lista de Eventos. A su vez, quita las alarmas del Evento del treeset de Alarmas.
    public void eliminarEvento(Evento evento) {
        for (Alarma alarma : evento.getListaAlarmas()) {
            listaAlarmas.remove(alarma);
        }
        listaEventos.remove(evento);
    }

    // Recibe una instancia de Tarea y la quita de la lista de Tareas. A su vez, quita las alarmas de la Tarea del treeset de Alarmas.
    public void eliminarTarea(Tarea tarea) {
        for (Alarma alarma : tarea.getListaAlarmas()) {
            listaAlarmas.remove(alarma);
        }
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
    // caracteristicas pasadas por parametro. Tambien la agrega al treeset de Alarmas.
    public void agregarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(actividad, inicio, efecto);
        actividad.agregarAlarma(nuevaAlarma);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe una instancia de Evento o Tarea y agrega le agrega una alarma con las
    // caracteristicas pasadas por parametro. Tambien la agrega al treeset de Alarmas.
    public void agregarAlarma(Actividad actividad, Duration inicio, Efecto efecto) {
        var nuevaAlarma = new Alarma(actividad, inicio, efecto);
        actividad.agregarAlarma(nuevaAlarma);
        listaAlarmas.add(nuevaAlarma);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, LocalDateTime nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);

        this.agregarAlarma(actividad, nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Duration nuevoInicio) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);

        this.agregarAlarma(actividad, nuevoInicio, efecto);
    }

    // Recibe una instancia de Evento o Tarea y modifica la alarma, que tiene las caracteristicas indicadas, con el parametro pasado.
    // No modifica la alarma en si, si no que crea una nueva.
    public void modificarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto, Efecto nuevoEfecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);

        this.agregarAlarma(actividad, inicio, nuevoEfecto);
    }

    // Recibe una instancia de Evento o Tarea y le elimina la alarma que tiene las caracteristicas indicadas.
    // Tambien la elimina del treeset de Alarmas.
    public void eliminarAlarma(Actividad actividad, LocalDateTime inicio, Efecto efecto) {
        Alarma alarma = actividad.buscarAlarma(inicio, efecto);
        actividad.eliminarAlarma(alarma);
        listaAlarmas.remove(alarma);
    }

    // Devuelve la siguiente alarma que deberia sonar. Devuelve null si no hay ninguna alarma.
    public Alarma obtenerProximaAlarma() {
        return listaAlarmas.isEmpty()? null : listaAlarmas.first();
    }

    // Metodo que indica en base al tiempo actual si debe sonar la proxima alarma.
    public boolean iniciaProximaAlarma () {
        return tiempoActual.equals(obtenerProximaAlarma().getInicio());
    }

    // Metodo que obtiene a partir de la alarma que debio activarse el Evento o Tarea correspondiente.
    // Si se trata de un Evento con repeticion, actualiza la alarma disparada a la de su repeticion.
    // Finalmente elimina la alarma disparada.
    public Actividad dispararProximaAlarma() {
        Alarma alarmaActual = obtenerProximaAlarma();
        Actividad actividad = alarmaActual.getActividad();

        actualizarAlarmaRepeticion(actividad, alarmaActual);

        actividad.eliminarAlarma(alarmaActual);
        listaAlarmas.remove(alarmaActual);
        return actividad;
    }

    // Metodo que se encarga de actualizar la alarma de un Evento dado el caso de que este deba repetirse.
    public void actualizarAlarmaRepeticion(Actividad actividad, Alarma alarma) {
        if (!listaEventos.contains(actividad)) { return;}

        int i = listaEventos.indexOf(actividad);
        Evento eventoAlarma = listaEventos.get(i);

        if (!eventoAlarma.esRepetido()) {return;}

        LocalDateTime fechaRepeticion = eventoAlarma.getSiguienteRepeticion();

        if (fechaRepeticion == null) {return;}

        LocalDateTime fechaSiguienteAlarma = eventoAlarma.getRepeticion().calcularSiguienteRepeticion(alarma.getInicio());

        var alarmaRepeticion = new Alarma(eventoAlarma, fechaSiguienteAlarma, alarma.getEfecto());

        eventoAlarma.agregarAlarma(alarmaRepeticion);
        listaAlarmas.add(alarmaRepeticion);
    }
}