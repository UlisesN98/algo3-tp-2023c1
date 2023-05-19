import org.junit.Test;

import java.io.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CalendarioTest {

    @Test
    public void crearEvento() { // Crea un evento, se le pide al calendario dicho evento y su alarma y se corrobora que sus parametros sean correctos.
        // Arramge

        var nuevoCalendario = new Calendario();

        // Determino los atributos para dos eventos distintos con pero que poseen un mismo horario y una alarma una hora antes
        String titulo1 = "Evento 1";
        String descripcion1 = "Primer evento a probar";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-04-24T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas1 = {LocalDateTime.parse("2023-04-24T13:00")};
        Efecto[] efectoAlarmas1 = {Efecto.NOTIFICACION};

        String titulo2 = "Evento 2";
        String descripcion2 = "Segundo evento a probar";
        LocalDateTime inicio2 = LocalDateTime.parse("2023-04-24T14:00");
        LocalDateTime fin2 = LocalDateTime.parse("2023-04-24T17:00");
        Duration[] inicioAlarmas2 = {Duration.ofHours(1)};
        Efecto[] efectoAlarmas2 = {Efecto.MAIL};

        // Act

        // Creo cada evento con un metodo de creacion distinto
        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas1, efectoAlarmas1, null);
        nuevoCalendario.crearEvento(titulo2, descripcion2, false, inicio2, fin2, inicioAlarmas2, efectoAlarmas2, null);

        // Busco en el calendario cada evento
        List<Evento> nuevaListaEventos1 = nuevoCalendario.buscarEventos(titulo1, descripcion1, inicio1, fin1);
        Evento eventoBuscado1 = nuevaListaEventos1.get(0);
        Alarma alarmaEvento1 = eventoBuscado1.buscarAlarma(inicioAlarmas1[0], efectoAlarmas1[0]);

        List<Evento> nuevaListaEventos2 = nuevoCalendario.buscarEventos(titulo2, descripcion2, inicio2, fin2);
        Evento eventoBuscado2 = nuevaListaEventos2.get(0);
        // Como para buscar necesito la hora de la alarma, no un intervalo, y ambos tienen una alarma a la misma hora, uso la hora del primer evento
        Alarma alarmaEvento2 = eventoBuscado2.buscarAlarma(inicioAlarmas1[0], efectoAlarmas2[0]);

        // Assert

        assertEquals(titulo1, eventoBuscado1.getTitulo());
        assertEquals(descripcion1, eventoBuscado1.getDescripcion());
        assertEquals(inicio1, eventoBuscado1.getInicio());
        assertEquals(fin1, eventoBuscado1.getFin());
        assertEquals(inicioAlarmas1[0], alarmaEvento1.getInicio());
        assertEquals(efectoAlarmas1[0], alarmaEvento1.getEfecto());

        assertEquals(titulo2, eventoBuscado2.getTitulo());
        assertEquals(descripcion2, eventoBuscado2.getDescripcion());
        assertEquals(inicio2, eventoBuscado2.getInicio());
        assertEquals(fin2, eventoBuscado2.getFin());
        assertEquals(inicioAlarmas1[0], alarmaEvento2.getInicio());
        assertEquals(efectoAlarmas2[0], alarmaEvento2.getEfecto());
    }

    @Test
    public void modificarEvento() { // Crea un evento en el calendario, lo busca y luego lo modifica con nuevos parametros.
        // Arrange

        var nuevoCalendario = new Calendario();

        // Caracteristicas iniciales del evento
        String titulo1 = "Evento 1";
        String descripcion1 = "Primer evento a probar";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Caracteristicas nuevas del evento
        String titulo2 = "Evento 2";
        String descripcion2 = "Segundo evento a probar";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T12:00");
        LocalDateTime fin2 = LocalDateTime.parse("2018-12-14T15:00");

        // Act

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);

        List<Evento> nuevaListaEventos1 = nuevoCalendario.buscarEventos(titulo1, descripcion1, inicio1, fin1);
        Evento eventoBuscado1 = nuevaListaEventos1.get(0);

        // Modifico el evento
        nuevoCalendario.modificar(eventoBuscado1, titulo2, descripcion2, inicio2, fin2);

        // Vuelvo a buscarlo
        List<Evento> nuevaListaEventos2 = nuevoCalendario.buscarEventos(titulo2, descripcion2, inicio2, fin2);
        Evento eventoBuscado2 = nuevaListaEventos2.get(0);

        // Assert

        // Chequeo que los atributos cambiaron y que el evento sigue siendo el mismo
        assertEquals(titulo2, eventoBuscado1.getTitulo());
        assertEquals(descripcion2, eventoBuscado1.getDescripcion());
        assertEquals(inicio2, eventoBuscado1.getInicio());
        assertEquals(fin2, eventoBuscado1.getFin());
        assertEquals(eventoBuscado1, eventoBuscado2);
    }

    @Test
    public void eliminarEvento() { // Crea un evento, lo busca, elimina y luego chequea que no persista en el calendario.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo = "Evento 3";
        String descripcion = "Tercer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2018-04-10T11:25");
        LocalDateTime fin = LocalDateTime.parse("2018-07-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas, null);
        List<Evento> nuevaListaEventos1 = nuevoCalendario.buscarEventos(titulo, descripcion, inicio, fin);
        Evento eventoBuscado1 = nuevaListaEventos1.get(0);

        // Elimino el evento
        nuevoCalendario.eliminarEvento(eventoBuscado1);

        // Lo busco con el objetivo de recibir una lista vacia
        List<Evento> listaEventosPostBorrado = nuevoCalendario.buscarEventos(titulo, descripcion, inicio, fin);

        // Assert

        // Chequeo que tanto el evento como su alarma ya no estan en el calendario
        assertEquals(listaEventosPostBorrado.size(), 0);
        assertNull(nuevoCalendario.obtenerProximaAlarma());
    }

    @Test
    public void crearTarea() { // Crea una tarea, se le pide al calendario dicho evento y su alarma y se corrobora que sus parametros sean correctos.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 1";
        String descripcion = "Primer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Tarea 2";
        String descripcion2 = "Segunda tarea a probar";
        LocalDateTime limite2 = LocalDateTime.parse("2023-04-24T17:00");
        Duration[] inicioAlarmas2 = {Duration.ofHours(3)};
        Efecto[] efectoAlarmas2 = {Efecto.SONIDO};

        // Act

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite2, inicioAlarmas2, efectoAlarmas2);

        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareas(titulo, descripcion, limite);
        Tarea tareaBuscada = nuevaListaTareas.get(0);
        Alarma alarmaTarea = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        List<Tarea> nuevaListaTareas2 = nuevoCalendario.buscarTareas(titulo2, descripcion2, limite2);
        Tarea tareaBuscada2 = nuevaListaTareas2.get(0);
        Alarma alarmaTarea2 = tareaBuscada2.buscarAlarma(inicioAlarmas[0], efectoAlarmas2[0]);

        // Assert

        assertEquals(titulo, tareaBuscada.getTitulo());
        assertEquals(descripcion, tareaBuscada.getDescripcion());
        assertEquals(limite, tareaBuscada.getInicio());
        assertEquals(inicioAlarmas[0], alarmaTarea.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTarea.getEfecto());

        assertEquals(titulo2, tareaBuscada2.getTitulo());
        assertEquals(descripcion2, tareaBuscada2.getDescripcion());
        assertEquals(limite2, tareaBuscada2.getInicio());
        assertEquals(inicioAlarmas[0], alarmaTarea2.getInicio());
        assertEquals(efectoAlarmas2[0], alarmaTarea2.getEfecto());
    }

    @Test
    public void modificarTarea() { // Crea una tarea, la busca y modifica en base a nuevos parametros y chequea que sean correctos al finalizar.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 1";
        String descripcion = "Primer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Tarea 2";
        String descripcion2 = "Segundo tarea a probar";
        LocalDateTime limite2 = LocalDateTime.parse("2023-04-24T18:00");

        // Act

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareas(titulo, descripcion, limite);
        Tarea tareaBuscada = nuevaListaTareas.get(0);

        nuevoCalendario.modificar(tareaBuscada, titulo2, descripcion2, limite2);

        List<Tarea> nuevaListaTareas2 = nuevoCalendario.buscarTareas(titulo2, descripcion2, limite2);
        Tarea tareaBuscada2 = nuevaListaTareas2.get(0);

        // Assert

        assertEquals(titulo2, tareaBuscada.getTitulo());
        assertEquals(descripcion2, tareaBuscada.getDescripcion());
        assertEquals(limite2, tareaBuscada.getInicio());
        assertEquals(tareaBuscada, tareaBuscada2);
    }

    @Test
    public void eliminarTarea() { // Crea una tarea, la busca, elimina y luego chequea que no persista en el calendario.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo = "Tarea 3";
        String descripcion = "Tercer tarea a probar";
        LocalDateTime limite = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(titulo, descripcion, false, limite, inicioAlarmas, efectoAlarmas);
        List<Tarea> nuevaListaTareas1 = nuevoCalendario.buscarTareas(titulo, descripcion, limite);
        Tarea tareaBuscada1 = nuevaListaTareas1.get(0);

        nuevoCalendario.eliminarTarea(tareaBuscada1);

        List<Tarea> listaTareasPostBorrado = nuevoCalendario.buscarTareas(titulo, descripcion, limite);

        // Assert

        assertEquals(listaTareasPostBorrado.size(), 0);
        assertNull(nuevoCalendario.obtenerProximaAlarma());
    }

    @Test
    public void crearEventosIdenticos() { // Crea 2 eventos con los mismos parametros, luego los busca en base a dichos parametros y chequea que no son el mismo evento
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);
        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);

        List<Evento> nuevaListaEventos = nuevoCalendario.buscarEventos(titulo1, descripcion1, inicio1, fin1);

        // Assert

        assertEquals(2, nuevaListaEventos.size());
        assertNotEquals(nuevaListaEventos.get(0), nuevaListaEventos.get(1));
        assertEquals(nuevaListaEventos.get(0).getTitulo(), nuevaListaEventos.get(1).getTitulo());
        assertEquals(nuevaListaEventos.get(0).getInicio(), nuevaListaEventos.get(1).getInicio());
        assertEquals(nuevaListaEventos.get(0).getListaAlarmas().first().getInicio(), nuevaListaEventos.get(1).getListaAlarmas().first().getInicio());
    }

    @Test
    public void crearTareasIdenticas() { // Crea 2 tareas con los mismos parametros, luego los busca en base a dichos parametros y chequea que no sean la misma tarea
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);

        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareas(titulo1, descripcion1, limite1);

        // Assert

        assertEquals(2, nuevaListaTareas.size());
        assertNotEquals(nuevaListaTareas.get(0), nuevaListaTareas.get(1));
        assertEquals(nuevaListaTareas.get(0).getTitulo(), nuevaListaTareas.get(1).getTitulo());
        assertEquals(nuevaListaTareas.get(0).getInicio(), nuevaListaTareas.get(1).getInicio());
        assertEquals(nuevaListaTareas.get(0).getListaAlarmas().first().getInicio(), nuevaListaTareas.get(1).getListaAlarmas().first().getInicio());
    }

    @Test
    public void crearAlarma(){ // Crea un evento y tarea en el calendario con una alarma dada, luego busca esas actividades y les agrega una alarma nueva a cada uno
        // Arrange

        var nuevoCalendario = new Calendario();

        String titulo1 = "Tarea/Evento A";
        String descripcion1 = "Desc tarea/evento A";

        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T15:25");
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T15:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T20:25");

        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T10:25")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Parametros de las nuevas alarmas, ambas ocurren a la misma hora
        Duration inicioAlarmasNuevo1 = Duration.ofHours(4);
        LocalDateTime inicioAlarmasNuevo2 = LocalDateTime.parse("2018-10-10T11:25");
        Efecto efectoAlarmaNuevo = Efecto.SONIDO;

        // Act

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas, null);

        List<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTareas(titulo1, descripcion1, limite1);
        List<Evento> nuevaListaEvento1 = nuevoCalendario.buscarEventos(titulo1, descripcion1, inicio1, fin1);

        Tarea tareaBuscada = nuevaListaTarea1.get(0);
        Evento eventoBuscado = nuevaListaEvento1.get(0);

        nuevoCalendario.agregarAlarma(tareaBuscada, inicioAlarmasNuevo1, efectoAlarmaNuevo);
        nuevoCalendario.agregarAlarma(eventoBuscado, inicioAlarmasNuevo2, efectoAlarmaNuevo);

        Alarma alarmaTareaOriginal = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaTareaNueva = tareaBuscada.buscarAlarma(inicioAlarmasNuevo2, efectoAlarmaNuevo);

        Alarma alarmaEventoOriginal = eventoBuscado.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaEventoNueva = eventoBuscado.buscarAlarma(inicioAlarmasNuevo2, efectoAlarmaNuevo);

        // Assert

        assertEquals(inicioAlarmas[0], alarmaTareaOriginal.getInicio());
        assertEquals(efectoAlarmas[0], alarmaTareaOriginal.getEfecto());

        assertEquals(inicioAlarmas[0], alarmaEventoOriginal.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEventoOriginal.getEfecto());

        assertEquals(inicioAlarmasNuevo2, alarmaTareaNueva.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaTareaNueva.getEfecto());

        assertEquals(inicioAlarmasNuevo2, alarmaEventoNueva.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaEventoNueva.getEfecto());
    }

    @Test
    public void modificarAlarma() { // Crea una tarea y la busca en el calendario para luego cambiarle laS alarmas con nuevos parametros.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:00");

        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T10:00"), LocalDateTime.parse("2018-10-10T09:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION, Efecto.MAIL};

        LocalDateTime inicioAlarmasNuevo1 = LocalDateTime.parse("2018-10-10T08:00");
        Duration inicioAlarmasNuevo2 = Duration.ofHours(6);
        Efecto efectoAlarmaNuevo = Efecto.SONIDO;

        // Act

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        List<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTareas(titulo1, descripcion1, limite1);
        Tarea tareaBuscada = nuevaListaTarea1.get(0);

        // Modifico las alarmas con distintos parametros
        nuevoCalendario.modificarAlarma(tareaBuscada, inicioAlarmas[0], efectoAlarmas[0], inicioAlarmasNuevo1);
        nuevoCalendario.modificarAlarma(tareaBuscada, inicioAlarmasNuevo1, efectoAlarmas[0], efectoAlarmaNuevo);

        nuevoCalendario.modificarAlarma(tareaBuscada, inicioAlarmas[1], efectoAlarmas[1], inicioAlarmasNuevo2);
        nuevoCalendario.modificarAlarma(tareaBuscada, LocalDateTime.parse("2018-10-10T05:00"), efectoAlarmas[1], efectoAlarmaNuevo);

        // Busco las alarmas por los atributos viejos y nuevos para luego corroborar el cambio
        Alarma alarmaTarea1 = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
        Alarma alarmaTarea2 = tareaBuscada.buscarAlarma(inicioAlarmasNuevo1, efectoAlarmaNuevo);

        Alarma alarmaTarea3 = tareaBuscada.buscarAlarma(inicioAlarmas[1], efectoAlarmas[1]);
        Alarma alarmaTarea4 = tareaBuscada.buscarAlarma(LocalDateTime.parse("2018-10-10T05:00"), efectoAlarmaNuevo);

        // Assert

        // Chequeo que tanto los nuevos atributos como que ya no existan alarmas con las caracteristicas viejas
        assertNull(alarmaTarea1);
        assertEquals(inicioAlarmasNuevo1, alarmaTarea2.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaTarea2.getEfecto());

        assertNull(alarmaTarea3);
        assertEquals(LocalDateTime.parse("2018-10-10T05:00"), alarmaTarea4.getInicio());
        assertEquals(efectoAlarmaNuevo, alarmaTarea4.getEfecto());
    }

    @Test
    public void eliminarAlarma() { // Crea una tarea y la busca en el calendario para eliminar su alarma y luego chequear que dicha alarma efectivamente dejo de existir.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        List<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTareas(titulo1, descripcion1, limite1);
        Tarea tareaBuscada = nuevaListaTarea1.get(0);

        nuevoCalendario.eliminarAlarma(tareaBuscada, inicioAlarmas[0], efectoAlarmas[0]);

        Alarma alarmaTareaPostBorrado = tareaBuscada.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        // Assert

        // Chequeo que la alarma no figure en ningun lado
        assertNull(alarmaTareaPostBorrado);
        assertNull(nuevoCalendario.obtenerProximaAlarma());
    }
    @Test
    public void tareaCompletaIncompleta(){ // Crea una tarea y corrobora si su comportamiento de TareaCompleta es el esperado (Inicia en false, es true luego de ser marcada).
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act & Assert

        nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1, inicioAlarmas, efectoAlarmas);
        List<Tarea> nuevaListaTarea1 = nuevoCalendario.buscarTareas(titulo1, descripcion1, limite1);
        Tarea tareaBuscada = nuevaListaTarea1.get(0);

        // La tarea comienza incompleta
        assertFalse(nuevoCalendario.tareaEstaCompletada(tareaBuscada));

        // Marco la tarea como completada
        nuevoCalendario.marcarTarea(tareaBuscada);

        // La tarea esta completa
        assertTrue(nuevoCalendario.tareaEstaCompletada(tareaBuscada));
    }

    @Test
    public void crearTareasMasa(){ // Crea 1000 tareas con limites distintos y corrobora que esten en el calendario como corresponde.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1.plusMinutes(i), inicioAlarmas, efectoAlarmas);
        }

        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Tarea tareaActual = nuevaListaTareas.get(i);
            Alarma alarmaActual = tareaActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo1, tareaActual.getTitulo());
            assertEquals(descripcion1, tareaActual.getDescripcion());
            assertEquals(limite1.plusMinutes(i), tareaActual.getInicio());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void crearEventosMasa(){ // Crea 1000 eventos con inicio y fin diferentes, corroborando que esten agregados de manera correcta al calendario.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1.plusMinutes(i), fin1.plusMinutes(i), inicioAlarmas, efectoAlarmas, null);
        }

        List<Evento> nuevaListaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Evento eventoActual = nuevaListaEventos.get(i);
            Alarma alarmaActual = eventoActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo1, eventoActual.getTitulo());
            assertEquals(descripcion1, eventoActual.getDescripcion());
            assertEquals(inicio1.plusMinutes(i), eventoActual.getInicio());
            assertEquals(fin1.plusMinutes(i), eventoActual.getFin());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void modificarTareasMasa(){ // Crea 1000 tareas, las modifica y corrobora que sus parametros sean correctos.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Tarea B";
        String descripcion2 = "Desc. tarea B";
        LocalDateTime limite2 = LocalDateTime.parse("2018-10-10T21:25");

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1.plusMinutes(i), inicioAlarmas, efectoAlarmas);
        }

        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Tarea tareaActual = nuevaListaTareas.get(i);
            nuevoCalendario.modificar(tareaActual, titulo2, descripcion2, limite2.plusMinutes(i));
            Alarma alarmaActual = tareaActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo2, tareaActual.getTitulo());
            assertEquals(descripcion2, tareaActual.getDescripcion());
            assertEquals(limite2.plusMinutes(i), tareaActual.getInicio());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void modificarEventosMasa(){ // Crea 1000 eventos, los modifica y corrobora que sus parametros sean correctos.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        String titulo2 = "Evento B";
        String descripcion2 = "Desc. evento B";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T21:25");
        LocalDateTime fin2 = LocalDateTime.parse("2018-10-10T23:25");

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1.plusMinutes(i), fin1.plusMinutes(i), inicioAlarmas, efectoAlarmas, null);
        }

        List<Evento> nuevaListaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Evento eventoActual = nuevaListaEventos.get(i);
            nuevoCalendario.modificar(eventoActual, titulo2, descripcion2, inicio2.plusMinutes(i), fin2.plusMinutes(i));
            Alarma alarmaActual = eventoActual.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);
            assertEquals(titulo2, eventoActual.getTitulo());
            assertEquals(descripcion2, eventoActual.getDescripcion());
            assertEquals(inicio2.plusMinutes(i), eventoActual.getInicio());
            assertEquals(fin2.plusMinutes(i), eventoActual.getFin());
            assertEquals(inicioAlarmas[0], alarmaActual.getInicio());
            assertEquals(efectoAlarmas[0], alarmaActual.getEfecto());
        }
    }

    @Test
    public void eliminarTareasMasa(){ // Crea 1000 tareas, luego las elimina y luego chequea que efectivamente el calendario no tiene ninguna de esas 1000 tareas al final.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Tarea A";
        String descripcion1 = "Desc. tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearTarea(titulo1, descripcion1, false, limite1.plusMinutes(i), inicioAlarmas, efectoAlarmas);
        }

        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Tarea tareaActual = nuevaListaTareas.get(i);
            nuevoCalendario.eliminarAlarma(tareaActual, inicioAlarmas[0], efectoAlarmas[0]);
            nuevoCalendario.eliminarTarea(tareaActual);
        }

        List<Tarea> listaTareasPostBorrado = nuevoCalendario.buscarTareaPorIntervalo(limite1, limite1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            assertEquals(listaTareasPostBorrado.size(), 0);
        }
    }

    @Test
    public void eliminarEventosMasa(){ // Crea 1000 eventos, los elimina y luego chequea que el calendario no tiene ninguno de esos 1000 eventos al final.
        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        for (int i = 0; i < 1000; i++){
            nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1.plusMinutes(i), fin1.plusMinutes(i), inicioAlarmas, efectoAlarmas, null);
        }

        List<Evento> nuevaListaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            Evento eventoActual = nuevaListaEventos.get(i);
            nuevoCalendario.eliminarAlarma(eventoActual, inicioAlarmas[0], efectoAlarmas[0]);
            nuevoCalendario.eliminarEvento(eventoActual);
        }

        List<Evento> listaEventosPostBorrado = nuevoCalendario.buscarEventoPorIntervalo(inicio1, fin1.plusMinutes(1000));

        for (int i = 0; i < 1000; i++){
            assertEquals(listaEventosPostBorrado.size(), 0);
        }
    }

    @Test
    public void testProximaDisparoAlarma(){ // Chequea manualmente con un evento y una tarea que las alarmas se dispararian en el orden esperado.
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Evento A";
        String descripcion1 = "Desc. evento A";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2018-10-10T14:00")};

        String titulo2 = "Tarea A";
        String descripcion2 = "Desc. Tarea A";
        LocalDateTime limite1 = LocalDateTime.parse("2018-10-10T18:25");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2018-10-10T14:30")};

        // Alarmas que seran agregadas luego
        LocalDateTime inicioAlarmaTarea2 = LocalDateTime.parse("2018-10-10T15:30");

        LocalDateTime inicioAlarmaEvento2 = LocalDateTime.parse("2018-10-10T15:00");
        LocalDateTime inicioAlarmaEvento3 = LocalDateTime.parse("2018-10-10T16:00");

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act & Assert

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, null); // Evento creado con su primera alarma.
        List<Evento> nuevaListaEventos = nuevoCalendario.buscarEventos(titulo1, descripcion1, inicio1, fin1);
        Evento eventoBuscado = nuevaListaEventos.get(0);

        nuevoCalendario.agregarAlarma(eventoBuscado, inicioAlarmaEvento2, efectoAlarmas[0]); // Se agrega alarma a evento con tiempo = inicioAlarmaEvento2
        nuevoCalendario.agregarAlarma(eventoBuscado, inicioAlarmaEvento3, efectoAlarmas[0]); // Se agrega alarma a evento con tiempo = inicioAlarmaEvento3

        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite1, inicioAlarmasTarea, efectoAlarmas); // Tarea creada con su alarma original
        List<Tarea> nuevaListaTareas = nuevoCalendario.buscarTareas(titulo2, descripcion2, limite1);
        Tarea tareaBuscada = nuevaListaTareas.get(0);

        nuevoCalendario.agregarAlarma(tareaBuscada, inicioAlarmaTarea2, efectoAlarmas[0]); // Se agrega alarma a tarea con tiempo = inicioAlarmaTarea2

        Alarma alarmaEvento1 = eventoBuscado.buscarAlarma(inicioAlarmasEvento[0], efectoAlarmas[0]);
        Alarma alarmaEvento2 = eventoBuscado.buscarAlarma(inicioAlarmaEvento2, efectoAlarmas[0]);
        Alarma alarmaEvento3 = eventoBuscado.buscarAlarma(inicioAlarmaEvento3, efectoAlarmas[0]);

        Alarma alarmaTarea1 = tareaBuscada.buscarAlarma(inicioAlarmasTarea[0], efectoAlarmas[0]);
        Alarma alarmaTarea2 = tareaBuscada.buscarAlarma(inicioAlarmaTarea2, efectoAlarmas[0]); // Alarmas buscadas y traidas a variables para comprobar

        assertEquals(alarmaEvento1, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaEvento1, nuevoCalendario.obtenerProximaAlarma()); // A proposito, chequea que la proxima alarma siga siendo la misma a pesar de que no cambio nada.

        assertFalse(nuevoCalendario.iniciaProximaAlarma(LocalDateTime.parse("2018-10-10T12:00")));

        assertEquals(nuevoCalendario.dispararProximaAlarma(), eventoBuscado); // Primera alarma es removida al ejecutar .dispararProximaAlarma()

        assertEquals(alarmaTarea1, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaTarea1, nuevoCalendario.obtenerProximaAlarma());

        assertFalse(nuevoCalendario.iniciaProximaAlarma(LocalDateTime.parse("2018-10-10T12:00")));

        assertEquals(nuevoCalendario.dispararProximaAlarma(), tareaBuscada); // Segunda alarma removida.

        assertEquals(alarmaEvento2, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaEvento2, nuevoCalendario.obtenerProximaAlarma());

        assertFalse(nuevoCalendario.iniciaProximaAlarma(LocalDateTime.parse("2018-10-10T12:00")));

        assertEquals(nuevoCalendario.dispararProximaAlarma(), eventoBuscado); // Tercera removida.

        assertEquals(alarmaTarea2, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaTarea2, nuevoCalendario.obtenerProximaAlarma());

        assertFalse(nuevoCalendario.iniciaProximaAlarma(LocalDateTime.parse("2018-10-10T12:00")));

        assertEquals(nuevoCalendario.dispararProximaAlarma(), tareaBuscada); // Cuarta removida.

        assertEquals(alarmaEvento3, nuevoCalendario.obtenerProximaAlarma());
        assertEquals(alarmaEvento3, nuevoCalendario.obtenerProximaAlarma());

        assertTrue(nuevoCalendario.iniciaProximaAlarma(LocalDateTime.parse("2018-10-10T16:00"))); // Chequeo de que el tiempo del calendario y la alarma que viene coinciden

        assertEquals(nuevoCalendario.dispararProximaAlarma(), eventoBuscado); // Quinta y ultima alarma removida.

        assertNull(eventoBuscado.buscarAlarma(inicioAlarmaEvento2, efectoAlarmas[0]));
        assertNull(tareaBuscada.buscarAlarma(inicioAlarmaTarea2, efectoAlarmas[0]));
    }

    // Crea un evento que se repite y una tarea. Se usa el metodo de buscarActividadesPorIntervalo
    // y se corrobora que dicha busqueda de intervalo devuelve la lista de actividades ordenada de
    // manera ascendente segun las fechas.
    @Test
    public void testBusquedaActividadesMixtasConReps(){
        // Arrange

        var nuevoCalendario = new Calendario();
        String titulo1 = "Algoritmos 3";
        String descripcion1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-13T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};

        var dias = new HashSet<DayOfWeek>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.THURSDAY);
        Repeticion repeticionEvento = new RepeticionSemanalDias(inicio1, LocalDateTime.parse("2023-06-29T17:00"), dias); // Se repite todos los Lunes y Jueves

        String titulo2 = "Entrega TP";
        String descripcion2 = "Fecha limite entrega TP Etapa 1";
        LocalDateTime limite1 = LocalDateTime.parse("2023-04-24T23:59");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-04-24T22:00")};

        LocalDateTime inicioIntervaloBuscar = LocalDateTime.parse("2023-04-24T00:00"); // Inicio intervalo a buscar
        LocalDateTime finIntervaloBuscar = LocalDateTime.parse("2023-05-07T00:00"); // Fin intervalo a buscar

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(titulo2, descripcion2, false, limite1, inicioAlarmasTarea, efectoAlarmas);
        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, repeticionEvento);

        List<Actividad> nuevaListaActividades = nuevoCalendario.buscarPorIntervalo(inicioIntervaloBuscar, finIntervaloBuscar); // Intervalo buscado

        assertEquals(nuevaListaActividades.size(), 5); // Deberia tener 5 actividades dentro de este intervalo

        Evento eventoPrimera = (Evento) nuevaListaActividades.get(0);
        Tarea tareaPrimera = (Tarea) nuevaListaActividades.get(1);
        Evento eventoSegunda = (Evento) nuevaListaActividades.get(2);
        Evento eventoTercera = (Evento) nuevaListaActividades.get(3);
        Evento eventoCuarta = (Evento) nuevaListaActividades.get(4);

        // Assert

        assertEquals(eventoPrimera.getTitulo(), titulo1);
        assertEquals(eventoPrimera.getDescripcion(), descripcion1);
        assertEquals(eventoPrimera.getInicio(), LocalDateTime.parse("2023-04-24T14:00"));
        assertEquals(eventoPrimera.getFin(), LocalDateTime.parse("2023-04-24T17:00"));

        assertEquals(tareaPrimera.getTitulo(), titulo2);
        assertEquals(tareaPrimera.getDescripcion(), descripcion2);
        assertEquals(tareaPrimera.getInicio(), limite1);

        assertEquals(eventoSegunda.getTitulo(), titulo1);
        assertEquals(eventoSegunda.getDescripcion(), descripcion1);
        assertEquals(eventoSegunda.getInicio(), LocalDateTime.parse("2023-04-27T14:00"));
        assertEquals(eventoSegunda.getFin(), LocalDateTime.parse("2023-04-27T17:00"));

        assertEquals(eventoTercera.getTitulo(), titulo1);
        assertEquals(eventoTercera.getDescripcion(), descripcion1);
        assertEquals(eventoTercera.getInicio(), LocalDateTime.parse("2023-05-01T14:00"));
        assertEquals(eventoTercera.getFin(), LocalDateTime.parse("2023-05-01T17:00"));

        assertEquals(eventoCuarta.getTitulo(), titulo1);
        assertEquals(eventoCuarta.getDescripcion(), descripcion1);
        assertEquals(eventoCuarta.getInicio(), LocalDateTime.parse("2023-05-04T14:00"));
        assertEquals(eventoCuarta.getFin(), LocalDateTime.parse("2023-05-04T17:00"));
    }

    // Crea 2 tareas y 2 eventos en el calendario (1 evento se repite, el otro no) y los busca con los metodos
    // buscarTareaPorIntervalo y buscarEventoPorIntervalo respectivamente, chequeando que devuelven las listas esperadas.
    @Test
    public void testBusquedaMezclada() {
        // Arrange

        var nuevoCalendario = new Calendario();
        String tituloEvento1 = "Algoritmos 3";
        String descripcionEvento1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-13T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};

        var dias = new HashSet<DayOfWeek>();
        dias.add(DayOfWeek.MONDAY);
        dias.add(DayOfWeek.THURSDAY);
        Repeticion repeticionEvento = new RepeticionSemanalDias(inicio1, LocalDateTime.parse("2023-06-29T17:00"), dias);

        String tituloEvento2 = "Torneo Local";
        String descripcionEvento2 = "Cosas";
        LocalDateTime inicio2 = LocalDateTime.parse("2023-04-29T12:00");
        LocalDateTime fin2 = LocalDateTime.parse("2023-04-29T18:00");

        String tituloTarea1 = "Entrega TP";
        String descripcionTarea1 = "Fecha limite entrega TP Etapa 1";
        LocalDateTime limite1 = LocalDateTime.parse("2023-04-24T23:59");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-04-24T22:00")};

        String tituloTarea2 = "Entrega Informe";
        String descripcionTarea2 = "Informe de trabajo";
        LocalDateTime limite2 = LocalDateTime.parse("2023-04-26T23:59");

        LocalDateTime inicioIntervaloBuscar = LocalDateTime.parse("2023-04-24T00:00");
        LocalDateTime finIntervaloBuscar = LocalDateTime.parse("2023-05-02T00:00");

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(tituloTarea2, descripcionTarea2, false, limite2, inicioAlarmasTarea, efectoAlarmas);
        nuevoCalendario.crearEvento(tituloEvento2, descripcionEvento2, false, inicio2, fin2, inicioAlarmasEvento, efectoAlarmas, null);
        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, false, limite1, inicioAlarmasTarea, efectoAlarmas);
        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, repeticionEvento);

        List<Evento> listaEventos = nuevoCalendario.buscarEventoPorIntervalo(inicioIntervaloBuscar, finIntervaloBuscar);
        List<Tarea> listaTareas = nuevoCalendario.buscarTareaPorIntervalo(inicioIntervaloBuscar, finIntervaloBuscar);
        // Ayuda memoria, eventos: evento1, evento1, evento2, evento1
        // tareas: tarea1, tarea2

        assertEquals(listaEventos.size(), 4); // Se esperan 4 eventos dentro del intervalo dado, debido al evento repetido
        assertEquals(listaTareas.size(), 2); // Se esperan 2 tareas dentro del intervalo dado

        Evento primerEventoLista = listaEventos.get(0);
        Evento segundoEventoLista = listaEventos.get(1);
        Evento tercerEventoLista = listaEventos.get(2);
        Evento cuartoEventoLista = listaEventos.get(3);

        Tarea primerTareaLista = listaTareas.get(0);
        Tarea segundaTareaLista = listaTareas.get(1);

        // Assert

        assertEquals(primerEventoLista.getTitulo(), tituloEvento1);
        assertEquals(primerEventoLista.getDescripcion(), descripcionEvento1);
        assertEquals(primerEventoLista.getInicio(), LocalDateTime.parse("2023-04-24T14:00"));
        assertEquals(primerEventoLista.getFin(), LocalDateTime.parse("2023-04-24T17:00"));

        assertEquals(segundoEventoLista.getTitulo(), tituloEvento1);
        assertEquals(segundoEventoLista.getDescripcion(), descripcionEvento1);
        assertEquals(segundoEventoLista.getInicio(), LocalDateTime.parse("2023-04-27T14:00"));
        assertEquals(segundoEventoLista.getFin(), LocalDateTime.parse("2023-04-27T17:00"));

        assertEquals(tercerEventoLista.getTitulo(), tituloEvento2);
        assertEquals(tercerEventoLista.getDescripcion(), descripcionEvento2);
        assertEquals(tercerEventoLista.getInicio(), LocalDateTime.parse("2023-04-29T12:00"));
        assertEquals(tercerEventoLista.getFin(), LocalDateTime.parse("2023-04-29T18:00"));

        assertEquals(cuartoEventoLista.getTitulo(), tituloEvento1);
        assertEquals(cuartoEventoLista.getDescripcion(), descripcionEvento1);
        assertEquals(cuartoEventoLista.getInicio(), LocalDateTime.parse("2023-05-01T14:00"));
        assertEquals(cuartoEventoLista.getFin(), LocalDateTime.parse("2023-05-01T17:00"));

        assertEquals(primerTareaLista.getTitulo(), tituloTarea1);
        assertEquals(primerTareaLista.getDescripcion(), descripcionTarea1);
        assertEquals(primerTareaLista.getInicio(), limite1);

        assertEquals(segundaTareaLista.getTitulo(), tituloTarea2);
        assertEquals(segundaTareaLista.getDescripcion(), descripcionTarea2);
        assertEquals(segundaTareaLista.getInicio(), limite2);
    }

    @Test
    public void modificarRepeticion() {
        // Arrange

        var nuevoCalendario = new Calendario();

        String titulo1 = "Evento";
        String descripcion1 = "Evento a probar";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-04-24T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas1 = {LocalDateTime.parse("2023-04-24T13:00")};
        Efecto[] efectoAlarmas1 = {Efecto.NOTIFICACION};
        Repeticion repeticion1 = new RepeticionDiariaIntervalo(inicio1, 5, 3);

        Repeticion repeticion2 = new RepeticionComun(inicio1, Frecuencia.SEMANAL);

        // Act

        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas1, efectoAlarmas1, repeticion1);
        List<Evento> listaEventos = nuevoCalendario.buscarEventos(titulo1, descripcion1, inicio1, fin1);
        Evento eventoBuscado = listaEventos.get(0);

        LocalDateTime siguienteRepeticion1 = eventoBuscado.getSiguienteRepeticion();

        // Modifico la repeticion
        nuevoCalendario.modificar(eventoBuscado, repeticion2);
        LocalDateTime siguienteRepeticion2 = eventoBuscado.getSiguienteRepeticion();

        nuevoCalendario.modificar(eventoBuscado, null);
        LocalDateTime siguienteRepeticion3 = eventoBuscado.getSiguienteRepeticion();

        // Assert

        // Chequeo que la siguiente repeticion sea correcta y se actualice
        assertEquals(LocalDateTime.parse("2023-04-27T14:00"), siguienteRepeticion1);
        assertEquals(LocalDateTime.parse("2023-05-01T14:00"), siguienteRepeticion2);
        assertNull(siguienteRepeticion3);
    }

    // Crea un evento con un dado inicio y fin pero indicando que es de dia completo y chequea que sus valores de inicio y fin son los
    // que corresponden al ser marcado como dia completo
    @Test
    public void testEventoDiaCompleto(){
        // Arrange

        var nuevoCalendario = new Calendario();
        String tituloEvento1 = "Algoritmos 3";
        String descripcionEvento1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-16T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, true, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, null);
        List<Evento> listaEventos = nuevoCalendario.buscarEventoPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Evento eventoBuscado = listaEventos.get(0);

        // Assert

        assertEquals(LocalDateTime.parse("2023-03-13T00:00"), eventoBuscado.getInicio());
        assertEquals(LocalDateTime.parse("2023-03-17T00:00"), eventoBuscado.getFin());
    }

    // Crea un evento con un dado inicio y fin, marcada como NO de dia completo, y luego se la modifica para ser de dia
    // completo, chequeando que su inicio y fin son los que corresponden luego de ser marcada como de dia completo
    @Test
    public void testEventoConvertidoDiaCompleto(){
        // Arrange

        var nuevoCalendario = new Calendario();
        String tituloEvento1 = "Algoritmos 3";
        String descripcionEvento1 = "Clase de Algo3";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-03-16T17:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, null);
        List<Evento> listaEventos = nuevoCalendario.buscarEventoPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Evento eventoBuscado = listaEventos.get(0);
        nuevoCalendario.modificar(eventoBuscado, true);

        // Assert

        assertEquals(eventoBuscado.getInicio(), LocalDateTime.parse("2023-03-13T00:00"));
        assertEquals(eventoBuscado.getFin(), LocalDateTime.parse("2023-03-17T00:00"));
    }

    // Crea una tarea con un limite dado y marcada como de dia completo, chequeando que su limite es el
    // que corresponde si es de dia completo
    @Test
    public void testTareaDiaCompleto(){
        // Arrange

        var nuevoCalendario = new Calendario();
        String tituloTarea1 = "Algoritmos 3";
        String descripcionTarea1 = "Clase de Algo3";
        LocalDateTime limite1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, true, limite1, inicioAlarmasTarea, efectoAlarmas);
        List<Tarea> listaTareas = nuevoCalendario.buscarTareaPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Tarea tareaBuscada = listaTareas.get(0);

        // Assert

        assertEquals(tareaBuscada.getInicio(), LocalDateTime.parse("2023-03-14T00:00"));
    }

    // Crea una tarea con un limite dado y marcada como NO de dia completo y luego se la modifica para que sea de dia completo
    // chequeando que su limite es el que corresponde para una tarea de dia completo
    @Test
    public void testTareaConvertidaDiaCompleto(){
        // Arrange

        var nuevoCalendario = new Calendario();
        String tituloTarea1 = "Algoritmos 3";
        String descripcionTarea1 = "Clase de Algo3";
        LocalDateTime limite1 = LocalDateTime.parse("2023-03-13T14:00");
        LocalDateTime[] inicioAlarmasTarea = {LocalDateTime.parse("2023-03-13T13:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION};

        // Act

        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, false, limite1, inicioAlarmasTarea, efectoAlarmas);
        List<Tarea> listaTareas = nuevoCalendario.buscarTareaPorIntervalo(LocalDateTime.parse("2023-03-11T12:00"), LocalDateTime.parse("2023-03-20T12:00"));
        Tarea tareaBuscada = listaTareas.get(0);
        nuevoCalendario.modificar(tareaBuscada, true);
        nuevoCalendario.modificar(tareaBuscada, LocalDateTime.parse("2023-06-15T18:00")); // Le cambio el fin a modo de chequear que esta fecha se adaptara al dia completo

        // Assert

        assertEquals(tareaBuscada.getInicio(), LocalDateTime.parse("2023-06-16T00:00"));
    }

    // Chequea que si un evento se repite sus alarmas acompañen esa repeticion
    @Test
    public void testAlarmasEventosRepetidos() {
        // Arrange

        var nuevoCalendario = new Calendario();

        String tituloEvento1 = "Estructuras y Organizaciones";
        String descripcionEvento1 = "Clase de EyO";
        LocalDateTime inicio1 = LocalDateTime.parse("2023-04-21T18:00");
        LocalDateTime fin1 = LocalDateTime.parse("2023-04-21T22:00");
        LocalDateTime[] inicioAlarmasEvento = {LocalDateTime.parse("2023-04-21T16:00"), LocalDateTime.parse("2023-04-21T17:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION, Efecto.SONIDO};
        var repeticion1 = new RepeticionComun(inicio1, 3, Frecuencia.SEMANAL);

        var inicioEsperado1 = LocalDateTime.parse("2023-04-21T16:00");
        var inicioEsperado2 = LocalDateTime.parse("2023-04-21T17:00");
        var inicioEsperado3 = LocalDateTime.parse("2023-04-28T16:00");
        var inicioEsperado4 = LocalDateTime.parse("2023-04-28T17:00");
        var inicioEsperado5 = LocalDateTime.parse("2023-05-05T16:00");
        var inicioEsperado6 = LocalDateTime.parse("2023-05-05T17:00");

        var inicioEventoEsp1 = LocalDateTime.parse("2023-04-21T18:00");
        var inicioEventoEsp2 = LocalDateTime.parse("2023-04-28T18:00");
        var inicioEventoEsp3 = LocalDateTime.parse("2023-05-05T18:00");

        // Act

        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicio1, fin1, inicioAlarmasEvento, efectoAlarmas, repeticion1);

        // Primera ocurrencia del evento
        Alarma alarma1 = nuevoCalendario.obtenerProximaAlarma(); // Obtengo y disparo la primer alarma
        Actividad evento1 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp1, evento1.getInicio()); // Chequeo que las fechas del evento de la alarma que sono sean las esperadas

        Alarma alarma2 = nuevoCalendario.obtenerProximaAlarma(); // Obtengo y disparo la segunda alarma
        Actividad evento2 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp1, evento2.getInicio()); // Chequeo que las fechas del evento de la alarma que sono sean las esperadas

        // Actualizo las fechas de los eventos
        nuevoCalendario.actualizarEventosRepetidos(LocalDateTime.parse("2023-04-21T22:01"));

        // Segunda ocurrencia del evento
        Alarma alarma3 = nuevoCalendario.obtenerProximaAlarma(); // Obtengo y disparo la tercer alarma
        Actividad evento3 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp2, evento3.getInicio());  // Chequeo que las fechas del evento de la alarma que sono sean las esperadas

        Alarma alarma4 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento4 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp2, evento4.getInicio());

        nuevoCalendario.actualizarEventosRepetidos(LocalDateTime.parse("2023-04-28T22:01"));

        // Tercera ocurrencia
        Alarma alarma5 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento5 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp3, evento5.getInicio());

        Alarma alarma6 = nuevoCalendario.obtenerProximaAlarma();
        Actividad evento6 = nuevoCalendario.dispararProximaAlarma();

        assertEquals(inicioEventoEsp3, evento6.getInicio());

        nuevoCalendario.actualizarEventosRepetidos(LocalDateTime.parse("2023-05-05T22:01"));

        // Chequeo que ya no haya alarmas
        Alarma alarma7 = nuevoCalendario.obtenerProximaAlarma();

        // Assert

        // Chequeo que cada alarma haya sido la esperada
        assertEquals(inicioEsperado1, alarma1.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma1.getEfecto());

        assertEquals(inicioEsperado2, alarma2.getInicio());
        assertEquals(Efecto.SONIDO, alarma2.getEfecto());

        assertEquals(inicioEsperado3, alarma3.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma3.getEfecto());

        assertEquals(inicioEsperado4, alarma4.getInicio());
        assertEquals(Efecto.SONIDO, alarma4.getEfecto());

        assertEquals(inicioEsperado5, alarma5.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma5.getEfecto());

        assertEquals(inicioEsperado6, alarma6.getInicio());
        assertEquals(Efecto.SONIDO, alarma6.getEfecto());

        assertNull(alarma7);
    }

    // Chequea que el calendario pueda guardar su estado y recuperarlo
    @Test
    public void testPersistencia() throws IOException, ClassNotFoundException {
        // Arrange

        Calendario nuevoCalendario = new Calendario();
        nuevoCalendario.crearTarea("Nueva tarea", "descripcion tarea", false, LocalDateTime.parse("2023-05-12T19:00"), new LocalDateTime[] {LocalDateTime.parse("2023-05-12T18:00")}, new Efecto[] {Efecto.SONIDO});
        nuevoCalendario.crearEvento("Nuevo evento", "descripcion evento", true, LocalDateTime.parse("2023-05-12T19:00"), LocalDateTime.parse("2023-05-12T20:00"), new Duration[] {Duration.ofHours(4)}, new Efecto[] {Efecto.NOTIFICACION}, new RepeticionComun(LocalDateTime.parse("2023-05-12T00:00"), 2, Frecuencia.SEMANAL));

        String tituloEvento2 = "Estructuras y Organizaciones";
        String descripcionEvento2 = "Clase de EyO";
        LocalDateTime inicioEv2 = LocalDateTime.parse("2023-06-12T19:00");
        LocalDateTime finEv2 = LocalDateTime.parse("2023-06-12T23:00");
        LocalDateTime[] inicioAlarmasEvento2 = {LocalDateTime.parse("2023-06-12T14:00"), LocalDateTime.parse("2023-06-12T15:00")};
        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION, Efecto.SONIDO};
        var repeticion1 = new RepeticionComun(inicioEv2, 1, Frecuencia.SEMANAL);

        nuevoCalendario.crearEvento(tituloEvento2, descripcionEvento2, false, inicioEv2, finEv2, inicioAlarmasEvento2, efectoAlarmas, repeticion1);
        // Act

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        nuevoCalendario.serializar(bytes);
        Calendario calendarioDeserializado = Calendario.deserializar(new ByteArrayInputStream(bytes.toByteArray()));

        var listaTareas = calendarioDeserializado.buscarTareas("Nueva tarea", "descripcion tarea", LocalDateTime.parse("2023-05-12T19:00"));
        Tarea tarea = listaTareas.get(0);
        var listaEventos = calendarioDeserializado.buscarEventoPorIntervalo(LocalDateTime.parse("2023-01-01T00:00"), LocalDateTime.parse("2023-12-29T00:00"));
        Evento evento1 = listaEventos.get(0);
        Evento evento2 = listaEventos.get(3); // Este es evento 2, indices 1 y 2 son repeticiones semanales de evento1

        // Assert

        assertEquals(1, listaTareas.size());
        assertEquals("Nueva tarea", tarea.getTitulo());
        assertEquals("descripcion tarea", tarea.getDescripcion());
        assertEquals(LocalDateTime.parse("2023-05-12T19:00"), tarea.getInicio());

        assertEquals(5, listaEventos.size()); // Eventos originales + sus repeticiones
        assertEquals("Nuevo evento", evento1.getTitulo());
        assertEquals("descripcion evento", evento1.getDescripcion());
        assertEquals(LocalDateTime.parse("2023-05-12T00:00"), evento1.getInicio());
        assertEquals(LocalDateTime.parse("2023-05-13T00:00"), evento1.getFin());

        assertEquals(tituloEvento2, evento2.getTitulo());
        assertEquals(descripcionEvento2, evento2.getDescripcion());
        assertEquals(inicioEv2, evento2.getInicio());
        assertEquals(finEv2, evento2.getFin());

    }

    // Chequea que el calendario pueda guardar su estado y recuperarlo mediante la generacion de un archivo
    @Test
    public void testPersistenciaArchivo() throws IOException, ClassNotFoundException {
        // Arrange

        Calendario nuevoCalendario = new Calendario();
        nuevoCalendario.crearTarea("Nueva tarea", "descripcion tarea", false, LocalDateTime.parse("2023-05-12T19:00"), new LocalDateTime[] {LocalDateTime.parse("2023-05-12T18:00")}, new Efecto[] {Efecto.SONIDO});
        nuevoCalendario.crearEvento("Nuevo evento", "descripcion evento", true, LocalDateTime.parse("2023-05-12T19:00"), LocalDateTime.parse("2023-05-12T20:00"), new Duration[] {Duration.ofHours(4)}, new Efecto[] {Efecto.NOTIFICACION}, new RepeticionComun(LocalDateTime.parse("2023-05-12T00:00"), 5, Frecuencia.SEMANAL));

        // Act

        var archivo1 = new BufferedOutputStream(new FileOutputStream("calendario"));
        nuevoCalendario.serializar(archivo1);

        var archivo2 = new BufferedInputStream(new FileInputStream("calendario"));
        Calendario calendarioDeserializado = Calendario.deserializar(archivo2);

        var listaTareas = calendarioDeserializado.buscarTareas("Nueva tarea", "descripcion tarea", LocalDateTime.parse("2023-05-12T19:00"));
        Tarea tarea = listaTareas.get(0);

        var listaEventos = calendarioDeserializado.buscarEventoPorIntervalo(LocalDateTime.parse("2023-01-01T00:00"), LocalDateTime.parse("2023-12-12T00:00")); // Evento original + sus repeticiones
        Evento evento1 = listaEventos.get(0);

        // Assert

        assertEquals(1, listaTareas.size());
        assertEquals("Nueva tarea", tarea.getTitulo());
        assertEquals(LocalDateTime.parse("2023-05-12T19:00"), tarea.getInicio());

        assertEquals(6, listaEventos.size()); // Eventos originales + sus repeticiones
        assertEquals("Nuevo evento", evento1.getTitulo());
        assertEquals("descripcion evento", evento1.getDescripcion());
        assertEquals(LocalDateTime.parse("2023-05-12T00:00"), evento1.getInicio());
        assertEquals(LocalDateTime.parse("2023-05-13T00:00"), evento1.getFin());

    }

    // Chequea que el calendario mantenga el comportamiento esperado tras guardar y recuperar su estado varias veces
    @Test
    public void testPersistenciaComportamiento() throws IOException, ClassNotFoundException {
        // Arrange

        String tituloEvento1 = "Estructuras y Organizaciones";
        String descripcionEvento1 = "Clase de EyO";
        LocalDateTime inicioEv1 = LocalDateTime.parse("2023-06-12T19:00");
        LocalDateTime finEv1 = LocalDateTime.parse("2023-06-12T23:00");
        LocalDateTime[] inicioAlarmasEvento1 = {LocalDateTime.parse("2023-06-12T14:00"), LocalDateTime.parse("2023-06-12T15:00")};

        String tituloEvento2 = "Estructuras y Organizaciones 35";
        String descripcionEvento2 = "NO Clase de EyO";
        LocalDateTime inicioEv2 = LocalDateTime.parse("2023-10-12T19:00");
        LocalDateTime finEv2 = LocalDateTime.parse("2023-10-12T23:00");
        LocalDateTime[] inicioAlarmasEvento2 = {LocalDateTime.parse("2023-10-12T12:00"), LocalDateTime.parse("2023-10-12T14:00")};

        String tituloTarea1 = "TP 40";
        String descripcionTarea1 = "Arrangements";
        LocalDateTime limiteTa1 = LocalDateTime.parse("2023-03-12T15:00");
        LocalDateTime[] inicioAlarmasTarea1 = {LocalDateTime.parse("2023-03-12T10:00"), LocalDateTime.parse("2023-03-12T11:00")};

        Efecto[] efectoAlarmas = {Efecto.NOTIFICACION, Efecto.SONIDO};

        Calendario nuevoCalendario = new Calendario();
        nuevoCalendario.crearEvento(tituloEvento1, descripcionEvento1, false, inicioEv1, finEv1, inicioAlarmasEvento1, efectoAlarmas, null);
        nuevoCalendario.crearEvento(tituloEvento2, descripcionEvento2, false, inicioEv2, finEv2, inicioAlarmasEvento2, efectoAlarmas, null);
        nuevoCalendario.crearTarea(tituloTarea1, descripcionTarea1, false, limiteTa1, inicioAlarmasTarea1, efectoAlarmas);

        // Act

        var archivo1 = new BufferedOutputStream(new FileOutputStream("calendario"));
        nuevoCalendario.serializar(archivo1);

        var archivo2 = new BufferedInputStream(new FileInputStream("calendario"));
        Calendario calendarioDeserializado = Calendario.deserializar(archivo2);

        Alarma alarma1 = calendarioDeserializado.obtenerProximaAlarma(); // La alarma mas temprana es la de tarea1
        Actividad actividadDeAlarma1 = calendarioDeserializado.dispararProximaAlarma(); // Deberia ser tarea1

        Alarma alarma2 = calendarioDeserializado.obtenerProximaAlarma(); // La alarma que sigue es la segunda de tarea1
        Actividad actividadDeAlarma2 = calendarioDeserializado.dispararProximaAlarma(); // Deberia ser tarea1

        Alarma alarma3 = calendarioDeserializado.obtenerProximaAlarma(); // La alarma que sigue es la primera de Ev1 original
        Actividad actividadDeAlarma3 = calendarioDeserializado.dispararProximaAlarma(); // Deberia ser evento1

        Alarma alarma4 = calendarioDeserializado.obtenerProximaAlarma(); // La alarma que sigue es la segunda de Ev1 original
        Actividad actividadDeAlarma4 = calendarioDeserializado.dispararProximaAlarma(); // Deberia ser evento1

        Alarma alarma5 = calendarioDeserializado.obtenerProximaAlarma(); // La alarma que sigue es la primera de Ev2 original (y unico)
        Actividad actividadDeAlarma5 = calendarioDeserializado.dispararProximaAlarma(); // Deberia ser evento2

        Alarma alarma6 = calendarioDeserializado.obtenerProximaAlarma(); // La alarma que sigue es la primera de Ev2 original (y unico)
        Actividad actividadDeAlarma6 = calendarioDeserializado.dispararProximaAlarma(); // Deberia ser evento2

        var listaTareas = calendarioDeserializado.buscarTareaPorIntervalo(LocalDateTime.parse("2023-03-01T00:00"), LocalDateTime.parse("2023-03-20T00:00"));
        Tarea tarea = listaTareas.get(0);

        var listaEventos = calendarioDeserializado.buscarEventoPorIntervalo(LocalDateTime.parse("2023-01-01T00:00"), LocalDateTime.parse("2023-12-12T00:00")); // Ev1, Ev1Rep, Ev2
        Evento evento1 = listaEventos.get(0);
        Evento evento2 = listaEventos.get(1);

        // Assert
        // Arrancando por las alarmas...
        assertEquals(inicioAlarmasTarea1[0], alarma1.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma1.getEfecto());

        assertEquals(inicioAlarmasTarea1[1], alarma2.getInicio());
        assertEquals(Efecto.SONIDO, alarma2.getEfecto());

        assertEquals(inicioAlarmasEvento1[0], alarma3.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma3.getEfecto());

        assertEquals(inicioAlarmasEvento1[1], alarma4.getInicio());
        assertEquals(Efecto.SONIDO, alarma4.getEfecto());

        assertEquals(inicioAlarmasEvento2[0], alarma5.getInicio());
        assertEquals(Efecto.NOTIFICACION, alarma5.getEfecto());

        assertEquals(inicioAlarmasEvento2[1], alarma6.getInicio());
        assertEquals(Efecto.SONIDO, alarma6.getEfecto());

        // Luego todas las instancias recuperadas de eventos y tareas...
        assertEquals(tarea.getTitulo(), tituloTarea1);
        assertEquals(tarea.getDescripcion(), descripcionTarea1);
        assertEquals(tarea.getInicio(), limiteTa1);

        assertEquals(evento1.getTitulo(), tituloEvento1);
        assertEquals(evento1.getDescripcion(), descripcionEvento1);
        assertEquals(evento1.getInicio(), inicioEv1);
        assertEquals(evento1.getFin(), finEv1);

        assertEquals(evento2.getTitulo(), tituloEvento2);
        assertEquals(evento2.getDescripcion(), descripcionEvento2);
        assertEquals(evento2.getInicio(), inicioEv2);
        assertEquals(evento2.getFin(), finEv2);

        assertEquals(actividadDeAlarma1.getTitulo(), tarea.getTitulo());
        assertEquals(actividadDeAlarma2.getTitulo(), tarea.getTitulo());

        assertEquals(actividadDeAlarma3.getTitulo(), evento1.getTitulo());
        assertEquals(actividadDeAlarma4.getTitulo(), evento1.getTitulo());

        assertEquals(actividadDeAlarma5.getTitulo(), evento2.getTitulo());
        assertEquals(actividadDeAlarma6.getTitulo(), evento2.getTitulo());
    }
}