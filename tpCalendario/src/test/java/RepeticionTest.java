import junit.framework.TestResult;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class RepeticionTest {

    @Test
    public void calcularSiguienteRepeticion() {
        // Arrange

        // Determino una fecha inicial para las repeticiones
        var fechaInicial = LocalDateTime.parse("2023-03-01T12:00");

        // Creo los tres tipos de repeticion para el caso limite de una fecha
        var rep1 = new RepeticionComun(fechaInicial, LocalDateTime.parse("2023-04-24T23:59"), Frecuencia.MENSUAL);
        var rep2 = new RepeticionDiariaIntervalo(fechaInicial, LocalDateTime.parse("2023-04-24T23:59"), 3);
        var rep3 = new RepeticionSemanalDias(fechaInicial, LocalDateTime.parse("2023-04-24T23:59"), new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.FRIDAY});

        // Creo los tres tipos de repeticion para el caso limite de una cantidad
        var rep4 = new RepeticionComun(fechaInicial, 3, Frecuencia.MENSUAL);
        var rep5 = new RepeticionDiariaIntervalo(fechaInicial, 5, 3);
        var rep6 = new RepeticionSemanalDias(fechaInicial, 5, new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.FRIDAY});

        // Creo los tres tipos de repeticion para el caso sin limite
        var rep7 = new RepeticionComun(fechaInicial, Frecuencia.MENSUAL);
        var rep8 = new RepeticionDiariaIntervalo(fechaInicial,3);
        var rep9 = new RepeticionSemanalDias(fechaInicial, new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.FRIDAY});

        // Determino las fechas esperadas para ejecucion
        var repEsperada1 = LocalDateTime.parse("2023-04-01T12:00");
        var repEsperada2 = LocalDateTime.parse("2023-03-04T12:00");
        var repEsperada3 = LocalDateTime.parse("2023-03-03T12:00");

        var repEsperada4 = LocalDateTime.parse("2023-05-01T12:00");
        var repEsperada5 = LocalDateTime.parse("2023-03-07T12:00");
        var repEsperada6 = LocalDateTime.parse("2023-03-07T12:00");

        var repEsperada7 = LocalDateTime.parse("2023-06-01T12:00");
        var repEsperada8 = LocalDateTime.parse("2023-03-10T12:00");
        var repEsperada9 = LocalDateTime.parse("2023-03-10T12:00");

        // Act

        // Ejecuto para casos donde se espera una fecha resultante
        var repResultante1 = rep1.calcularSiguienteRepeticion(fechaInicial);
        var repResultante2 = rep2.calcularSiguienteRepeticion(fechaInicial);
        var repResultante3 = rep3.calcularSiguienteRepeticion(fechaInicial);

        var repResultante4 = rep4.calcularSiguienteRepeticion(repResultante1);
        var repResultante5 = rep5.calcularSiguienteRepeticion(repResultante2);
        var repResultante6 = rep6.calcularSiguienteRepeticion(repResultante3);

        var repResultante7 = rep7.calcularSiguienteRepeticion(repResultante4);
        var repResultante8 = rep8.calcularSiguienteRepeticion(repResultante5);
        var repResultante9 = rep9.calcularSiguienteRepeticion(repResultante6);

        // Ejecuto para casos donde se espera null o comprobar que los casos sin limite nunca dan null
        var repResultante10 = rep1.calcularSiguienteRepeticion(LocalDateTime.parse("2023-03-25T00:00"));
        var repResultante11 = rep2.calcularSiguienteRepeticion(LocalDateTime.parse("2023-04-22T00:00"));
        var repResultante12 = rep3.calcularSiguienteRepeticion(LocalDateTime.parse("2023-04-21T00:00"));
        var repResultante13 = rep4.calcularSiguienteRepeticion(LocalDateTime.parse("2023-04-01T12:01"));
        var repResultante14 = rep5.calcularSiguienteRepeticion(LocalDateTime.parse("2023-03-10T12:01"));
        var repResultante15 = rep6.calcularSiguienteRepeticion(LocalDateTime.parse("2023-03-14T12:01"));
        var repResultante16 = rep7.calcularSiguienteRepeticion(LocalDateTime.parse("2050-03-01T12:00"));
        var repResultante17 = rep8.calcularSiguienteRepeticion(LocalDateTime.parse("2050-03-01T12:00"));
        var repResultante18 = rep9.calcularSiguienteRepeticion(LocalDateTime.parse("2050-03-01T12:00"));

        // Assert

        assertEquals(repEsperada1, repResultante1);
        assertEquals(repEsperada2, repResultante2);
        assertEquals(repEsperada3, repResultante3);
        assertEquals(repEsperada4, repResultante4);
        assertEquals(repEsperada5, repResultante5);
        assertEquals(repEsperada6, repResultante6);
        assertEquals(repEsperada7, repResultante7);
        assertEquals(repEsperada8, repResultante8);
        assertEquals(repEsperada9, repResultante9);

        assertNull(repResultante10);
        assertNull(repResultante11);
        assertNull(repResultante12);
        assertNull(repResultante13);
        assertNull(repResultante14);
        assertNull(repResultante15);
        assertEquals(LocalDateTime.parse("2050-04-01T12:00"), repResultante16);
        assertEquals(LocalDateTime.parse("2050-03-04T12:00"), repResultante17);
        assertEquals(LocalDateTime.parse("2050-03-04T12:00"), repResultante18);
    }

    @Test
    public void calcularRepeticionesPorIntervalo() {
        // Arrange

        // Determino una fecha inicial para las repeticiones, una fecha inicial para el intervalo y tambien una fecha final
        var fechaInicial = LocalDateTime.parse("2023-03-01T12:00");
        var inicioIntervalo = LocalDateTime.parse("2023-04-01T00:00");
        var finIntervalo = LocalDateTime.parse("2023-04-30T23:59");

        // Creo los tres tipos de repeticion para casos que entren en el intervalo
        var rep1 = new RepeticionComun(fechaInicial, 10, Frecuencia.SEMANAL);
        var rep2 = new RepeticionDiariaIntervalo(fechaInicial, LocalDateTime.parse("2023-04-20T12:00"), 5);
        var rep3 = new RepeticionSemanalDias(fechaInicial, 13, new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.THURSDAY});

        // Creo los tres tipos para casos que no entren en el intervalo
        var rep4 = new RepeticionComun(fechaInicial, LocalDateTime.parse("2025-03-01T12:00"), Frecuencia.ANUAL);
        var rep5 = new RepeticionDiariaIntervalo(fechaInicial, LocalDateTime.parse("2023-03-31T12:00"), 10);
        var rep6 = new RepeticionSemanalDias(LocalDateTime.parse("2023-05-01T00:00"), 8, new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.SATURDAY});

        // Determino las fechas que se esperan esten dentro del intervalo para cada caso
        var fechasEsperadas1 = new LocalDateTime[] {LocalDateTime.parse("2023-04-05T12:00"), LocalDateTime.parse("2023-04-12T12:00"), LocalDateTime.parse("2023-04-19T12:00"), LocalDateTime.parse("2023-04-26T12:00")};
        var fechasEsperadas2 = new LocalDateTime[] {LocalDateTime.parse("2023-04-05T12:00"), LocalDateTime.parse("2023-04-10T12:00"), LocalDateTime.parse("2023-04-15T12:00"), LocalDateTime.parse("2023-04-20T12:00")};
        var fechasEsperadas3 = new LocalDateTime[] {LocalDateTime.parse("2023-04-04T12:00"), LocalDateTime.parse("2023-04-06T12:00"), LocalDateTime.parse("2023-04-11T12:00"), LocalDateTime.parse("2023-04-13T12:00")};

        // Act

        TreeSet<LocalDateTime> fechasResultantes1 = rep1.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
        TreeSet<LocalDateTime> fechasResultantes2 = rep2.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
        TreeSet<LocalDateTime> fechasResultantes3 = rep3.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);

        TreeSet<LocalDateTime> fechasResultantes4 = rep4.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
        TreeSet<LocalDateTime> fechasResultantes5 = rep5.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);
        TreeSet<LocalDateTime> fechasResultantes6 = rep6.calcularRepeticionesPorIntervalo(inicioIntervalo, finIntervalo);

        // Assert

        // Corroboro desde un principio que la cantidad de fechas sea la esperada
        assertEquals(4, fechasResultantes1.size());
        assertEquals(4, fechasResultantes2.size());
        assertEquals(4, fechasResultantes3.size());

        assertEquals(0, fechasResultantes4.size());
        assertEquals(0, fechasResultantes5.size());
        assertEquals(0, fechasResultantes6.size());


        // Corroboro que las fechas sean las esperadas
        for (int i = 0; i < 4; i++) {
            LocalDateTime fechaResultante1 = fechasResultantes1.first();
            LocalDateTime fechaResultante2 = fechasResultantes2.first();
            LocalDateTime fechaResultante3 = fechasResultantes3.first();

            assertEquals(fechasEsperadas1[i], fechaResultante1);
            assertEquals(fechasEsperadas2[i], fechaResultante2);
            assertEquals(fechasEsperadas3[i], fechaResultante3);

            fechasResultantes1.remove(fechaResultante1);
            fechasResultantes2.remove(fechaResultante2);
            fechasResultantes3.remove(fechaResultante3);
        }
    }

    @Test
    public void name() {

        var f = LocalDateTime.parse("2023-04-23T00:00");
        var d = Duration.ofMinutes(120);

        var tu = d.getUnits();

        var f2 = f.plus(d.getSeconds(), d.getUnits().get(0));
        System.out.println(f2);


    }
}