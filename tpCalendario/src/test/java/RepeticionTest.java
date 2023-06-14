import calendario.RepeticionComun;
import calendario.RepeticionDiariaIntervalo;
import calendario.RepeticionSemanalDias;
import calendario.Frecuencia;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class RepeticionTest {

    @Test
    public void calcularSiguienteRepeticion() {
        // Arrange

        // Determino una fecha inicial para las repeticiones y otras para ejecutar pruebas
        var fechaInicial = LocalDateTime.parse("2023-03-01T12:00");
        var fechaPrevia = LocalDateTime.parse("2023-02-20T15:00");
        var fechaPosterior = LocalDateTime.parse("2023-03-03T00:00");
        var fechaPosterior2 = LocalDateTime.parse("2023-04-01T00:00");
        var fechaNula = LocalDateTime.parse("2023-04-24T16:00");
        var fechaPosterior3 = LocalDateTime.parse("2023-03-11T00:00");
        var fechaNula2 = LocalDateTime.parse("2023-05-02T16:00");
        var fechaPosterior4 = LocalDateTime.parse("2023-07-01T00:00");

        // Creo los tres tipos de repeticion para el caso límite de una fecha
        var rep1 = new RepeticionComun(fechaInicial, LocalDateTime.parse("2023-04-24T23:59"), Frecuencia.MENSUAL);
        var rep2 = new RepeticionDiariaIntervalo(fechaInicial, LocalDateTime.parse("2023-04-24T23:59"), 3);
        var dias = new HashSet<DayOfWeek>();
        dias.add(DayOfWeek.TUESDAY);
        dias.add(DayOfWeek.FRIDAY);
        var rep3 = new RepeticionSemanalDias(fechaInicial, LocalDateTime.parse("2023-04-24T23:59"), dias);

        // Creo los tres tipos de repeticion para el caso límite de una cantidad
        var rep4 = new RepeticionComun(fechaInicial, 3, Frecuencia.MENSUAL);
        var rep5 = new RepeticionDiariaIntervalo(fechaInicial, 5, 3);
        var rep6 = new RepeticionSemanalDias(fechaInicial, 5, dias);

        // Creo los tres tipos de repeticion para el caso sin límite
        var rep7 = new RepeticionComun(fechaInicial, Frecuencia.MENSUAL);
        var rep8 = new RepeticionDiariaIntervalo(fechaInicial,3);
        var rep9 = new RepeticionSemanalDias(fechaInicial, dias);

        // Determino las fechas esperadas tras la ejecución
        var repEsperada1 = LocalDateTime.parse("2023-04-01T12:00");
        var repEsperada2 = LocalDateTime.parse("2023-03-04T12:00");
        var repEsperada3 = LocalDateTime.parse("2023-03-03T12:00");
        var repEsperada4 = LocalDateTime.parse("2023-04-03T12:00");
        var repEsperada5 = LocalDateTime.parse("2023-04-04T12:00");
        var repEsperada6 = LocalDateTime.parse("2023-03-13T12:00");
        var repEsperada7 = LocalDateTime.parse("2023-03-14T12:00");
        var repEsperada8 = LocalDateTime.parse("2023-07-01T12:00");
        var repEsperada9 = LocalDateTime.parse("2023-07-02T12:00");
        var repEsperada10 = LocalDateTime.parse("2023-07-04T12:00");

        // Ejecuto para casos donde se espera una fecha resultante
        var repResultante1 = rep1.calcularSiguienteRepeticion(fechaInicial);
        var repResultante2 = rep2.calcularSiguienteRepeticion(fechaInicial);
        var repResultante3 = rep3.calcularSiguienteRepeticion(fechaInicial);

        var repResultante4 = rep1.calcularSiguienteRepeticion(fechaPrevia);
        var repResultante5 = rep2.calcularSiguienteRepeticion(fechaPrevia);
        var repResultante6 = rep3.calcularSiguienteRepeticion(fechaPrevia);

        var repResultante7 = rep1.calcularSiguienteRepeticion(fechaPosterior);
        var repResultante8 = rep2.calcularSiguienteRepeticion(fechaPosterior);
        var repResultante9 = rep3.calcularSiguienteRepeticion(fechaPosterior);

        var repResultante10 = rep1.calcularSiguienteRepeticion(fechaPosterior2);
        var repResultante11 = rep2.calcularSiguienteRepeticion(fechaPosterior2);
        var repResultante12 = rep3.calcularSiguienteRepeticion(fechaPosterior2);

        // Ejecuto para casos donde se espera null
        var repResultante13 = rep1.calcularSiguienteRepeticion(fechaNula);
        var repResultante14 = rep2.calcularSiguienteRepeticion(fechaNula);
        var repResultante15 = rep3.calcularSiguienteRepeticion(fechaNula);

        // Ejecuto para casos donde se espera una fecha resultante
        var repResultante16 = rep4.calcularSiguienteRepeticion(fechaInicial);
        var repResultante17 = rep5.calcularSiguienteRepeticion(fechaInicial);
        var repResultante18 = rep6.calcularSiguienteRepeticion(fechaInicial);

        var repResultante19 = rep4.calcularSiguienteRepeticion(fechaPrevia);
        var repResultante20 = rep5.calcularSiguienteRepeticion(fechaPrevia);
        var repResultante21 = rep6.calcularSiguienteRepeticion(fechaPrevia);

        var repResultante22 = rep4.calcularSiguienteRepeticion(fechaPosterior);
        var repResultante23 = rep5.calcularSiguienteRepeticion(fechaPosterior);
        var repResultante24 = rep6.calcularSiguienteRepeticion(fechaPosterior);

        var repResultante25 = rep4.calcularSiguienteRepeticion(fechaPosterior3);
        var repResultante26 = rep5.calcularSiguienteRepeticion(fechaPosterior3);
        var repResultante27 = rep6.calcularSiguienteRepeticion(fechaPosterior3);

        // Ejecuto para casos donde se espera null
        var repResultante28 = rep4.calcularSiguienteRepeticion(fechaNula2);
        var repResultante29 = rep5.calcularSiguienteRepeticion(fechaNula2);
        var repResultante30 = rep6.calcularSiguienteRepeticion(fechaNula2);

        // Ejecuto para casos donde se espera una fecha resultante
        var repResultante31 = rep7.calcularSiguienteRepeticion(fechaPosterior4);
        var repResultante32 = rep8.calcularSiguienteRepeticion(fechaPosterior4);
        var repResultante33 = rep9.calcularSiguienteRepeticion(fechaPosterior4);

        // Assert

        assertEquals(fechaInicial, repResultante1);
        assertEquals(fechaInicial, repResultante2);
        assertEquals(repEsperada3, repResultante3);

        assertEquals(fechaInicial, repResultante4);
        assertEquals(fechaInicial, repResultante5);
        assertEquals(repEsperada3, repResultante6);

        assertEquals(repEsperada1, repResultante7);
        assertEquals(repEsperada2, repResultante8);
        assertEquals(repEsperada3, repResultante9);

        assertEquals(repEsperada1, repResultante10);
        assertEquals(repEsperada4, repResultante11);
        assertEquals(repEsperada5, repResultante12);

        assertNull(repResultante13);
        assertNull(repResultante14);
        assertNull(repResultante15);

        assertEquals(fechaInicial, repResultante16);
        assertEquals(fechaInicial, repResultante17);
        assertEquals(repEsperada3, repResultante18);

        assertEquals(fechaInicial, repResultante19);
        assertEquals(fechaInicial, repResultante20);
        assertEquals(repEsperada3, repResultante21);

        assertEquals(repEsperada1, repResultante22);
        assertEquals(repEsperada2, repResultante23);
        assertEquals(repEsperada3, repResultante24);

        assertEquals(repEsperada1, repResultante25);
        assertEquals(repEsperada6, repResultante26);
        assertEquals(repEsperada7, repResultante27);

        assertNull(repResultante28);
        assertNull(repResultante29);
        assertNull(repResultante30);

        assertEquals(repEsperada8, repResultante31);
        assertEquals(repEsperada9, repResultante32);
        assertEquals(repEsperada10, repResultante33);
    }

    @Test
    public void calcularRepeticionesPorIntervalo() {
        // Arrange

        // Determino una fecha inicial para las repeticiones, una fecha inicial para el intervalo y también una fecha final
        var fechaInicial = LocalDateTime.parse("2023-03-01T12:00");
        var inicioIntervalo = LocalDateTime.parse("2023-04-01T00:00");
        var finIntervalo = LocalDateTime.parse("2023-04-30T23:59");

        // Creo los tres tipos de repeticion para casos que entren en el intervalo
        var rep1 = new RepeticionComun(fechaInicial, 10, Frecuencia.SEMANAL);
        var rep2 = new RepeticionDiariaIntervalo(fechaInicial, LocalDateTime.parse("2023-04-20T12:00"), 5);
        var dias1 = new HashSet<DayOfWeek>();
        dias1.add(DayOfWeek.TUESDAY);
        dias1.add(DayOfWeek.THURSDAY);
        var rep3 = new RepeticionSemanalDias(fechaInicial, 13, dias1);

        // Creo los tres tipos para casos que no entren en el intervalo
        var rep4 = new RepeticionComun(fechaInicial, LocalDateTime.parse("2025-03-01T12:00"), Frecuencia.ANUAL);
        var rep5 = new RepeticionDiariaIntervalo(fechaInicial, LocalDateTime.parse("2023-03-31T12:00"), 10);
        var dias2 = new HashSet<DayOfWeek>();
        dias2.add(DayOfWeek.MONDAY);
        dias2.add(DayOfWeek.SATURDAY);
        var rep6 = new RepeticionSemanalDias(LocalDateTime.parse("2023-05-01T00:00"), 8, dias2);

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
}