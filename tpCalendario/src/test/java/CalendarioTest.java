import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CalendarioTest {

    @Test
    public void crearEvento() {
        var nuevoCalendario = new Calendario();
        String titulo = "Evento 1";
        String descripcion = "Primer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2023-04-03T14:00");
        LocalDateTime fin = LocalDateTime.parse("2023-04-24T17:00");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2023-04-24T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);
        Alarma alarmaEvento = nuevoEvento.buscarAlarma(inicioAlarmas[0], efectoAlarmas[0]);

        assertEquals(titulo, nuevoEvento.getTitulo());
        assertEquals(descripcion, nuevoEvento.getDescripcion());
        assertEquals(inicio, nuevoEvento.getInicio());
        assertEquals(fin, nuevoEvento.getFin());
        assertEquals(inicioAlarmas[0], alarmaEvento.getInicio());
        assertEquals(efectoAlarmas[0], alarmaEvento.getEfecto());
    }

    @Test
    public void modificarEvento() {
        var nuevoCalendario = new Calendario();

        String titulo1 = "Evento 1";
        String descripcion1 = "Primer evento a probar";
        LocalDateTime inicio1 = LocalDateTime.parse("2018-10-10T11:25");
        LocalDateTime fin1 = LocalDateTime.parse("2018-10-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-10-10T14:00")};
        Integer[] efectoAlarmas = {0};

        String titulo2 = "Evento 2";
        String descripcion2 = "Segundo evento a probar";
        LocalDateTime inicio2 = LocalDateTime.parse("2018-10-10T12:00");
        LocalDateTime fin2 = LocalDateTime.parse("2018-12-14T15:00");


        nuevoCalendario.crearEvento(titulo1, descripcion1, false, inicio1, fin1, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento = nuevoCalendario.buscarEvento(titulo1, descripcion1, inicio1, fin1);
        nuevoCalendario.modificarEvento(nuevoEvento, titulo2, descripcion2, inicio2, fin2);


        assertEquals(titulo2, nuevoEvento.getTitulo());
        assertEquals(descripcion2, nuevoEvento.getDescripcion());
        assertEquals(inicio2, nuevoEvento.getInicio());
        assertEquals(fin2, nuevoEvento.getFin());
    }

    @Test
    public void eliminarEvento() {
        var nuevoCalendario = new Calendario();
        String titulo = "Evento 3";
        String descripcion = "Tercer evento a probar";
        LocalDateTime inicio = LocalDateTime.parse("2018-04-10T11:25");
        LocalDateTime fin = LocalDateTime.parse("2018-07-10T14:25");
        LocalDateTime[] inicioAlarmas = {LocalDateTime.parse("2018-07-10T14:00")};
        Integer[] efectoAlarmas = {0};

        nuevoCalendario.crearEvento(titulo, descripcion, false, inicio, fin, inicioAlarmas, efectoAlarmas);
        Evento nuevoEvento1 = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);
        nuevoCalendario.eliminarEvento(nuevoEvento1);
        Evento nuevoEvento2 = nuevoCalendario.buscarEvento(titulo, descripcion, inicio, fin);

        assertNull(nuevoEvento2);
    }

    @Test
    public void crearTarea() {
    }

    @Test
    public void modificarTarea() {
    }

    @Test
    public void eliminarTarea() {
    }
}