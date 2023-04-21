import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class RepeticionTest {

    @Test
    public void calcularSiguienteRepeticion() {
        // Arrange
        var fechaInicial1 = LocalDateTime.parse("2023-03-01T12:00");
        var fechaInicial2 = LocalDateTime.parse("2023-04-22T00:00");

        var rep1 = new RepeticionComun(fechaInicial1, Frecuencia.MENSUAL, Finalizacion.FECHA, "2023-04-24T23:59");
        var rep2 = new RepeticionDiariaIntervalo(fechaInicial1, 3, Finalizacion.FECHA, "2023-04-24T23:59");
        var rep3 = new RepeticionSemanalDias(fechaInicial1, new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.FRIDAY}, Finalizacion.FECHA, "2023-04-24T23:59");

        var repEsperada1 = LocalDateTime.parse("2023-04-01T12:00");
        var repEsperada2 = LocalDateTime.parse("2023-03-04T12:00");
        var repEsperada3 = LocalDateTime.parse("2023-03-03T12:00");

        // Act
        var repResultante1 = rep1.calcularSiguienteRepeticion(fechaInicial1);
        var repResultante2 = rep2.calcularSiguienteRepeticion(fechaInicial1);
        var repResultante3 = rep3.calcularSiguienteRepeticion(fechaInicial1);

        var repResultante4 = rep1.calcularSiguienteRepeticion(fechaInicial2);
        var repResultante5 = rep2.calcularSiguienteRepeticion(fechaInicial2);
        var repResultante6 = rep3.calcularSiguienteRepeticion(fechaInicial2);

        // Assert
        assertEquals(repEsperada1, repResultante1);
        assertEquals(repEsperada2, repResultante2);
        assertEquals(repEsperada3, repResultante3);

        assertNull(repResultante4);
        assertNull(repResultante5);
        assertNull(repResultante6);
    }

    @Test
    public void calcularRepeticionesPorIntervalo() {
    }

    @Test
    public void superoLimite() {

        // Arrange
        var fechaInicial = LocalDateTime.parse("2023-03-01T12:00");

        var rep1 = new RepeticionComun(fechaInicial, Frecuencia.SEMANAL, Finalizacion.CANTIDAD, "3");
        var rep2 = new RepeticionDiariaIntervalo(fechaInicial, 4, Finalizacion.CANTIDAD, "3");
        var rep3 = new RepeticionSemanalDias(fechaInicial, new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY}, Finalizacion.CANTIDAD, "2");

        var fechaErronea1 = LocalDateTime.parse("2023-03-22T12:01");
        var fechaErronea2 = LocalDateTime.parse("2023-03-13T12:01");
        var fechaErronea3 = LocalDateTime.parse("2023-03-04T12:00");

        // Act
        var resultado1 = rep1.superoLimite(fechaErronea1);
        var resultado2 = rep2.superoLimite(fechaErronea2);
        var resultado3 = rep3.superoLimite(fechaErronea3);

        // Assert
        assertTrue(resultado1);
        assertTrue(resultado2);
        assertTrue(resultado3);
    }
}