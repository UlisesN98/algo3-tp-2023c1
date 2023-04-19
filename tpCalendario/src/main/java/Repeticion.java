import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class Repeticion {

    private LocalDateTime inicio;
    private TipoRepeticion tipoRep; //ENUM
    private Integer frecuencia;
    private DayOfWeek[] dias;
    private TipoFinalizacion tipoFin; //ENUM
    private String finalizacion;

    public Repeticion(LocalDateTime inicio, TipoRepeticion tipoRep, Integer frecuencia, TipoFinalizacion tipoFin, String finalizacion, DayOfWeek[] dias) {
        this.inicio = inicio;
        this.tipoRep = tipoRep;
        this.frecuencia = frecuencia;
        this.dias = dias;
        this.tipoFin = tipoFin;
        this.finalizacion = finalizacion;
    }

    //falta agregarle el caso de los dias de la semana, y esta muy fea e inentendible, y si uso la funcion que obtiene todas las repeticiones y me quedo solo las que me sirven?
    public TreeSet<LocalDateTime> obtenerRepeticionesPorIntervalo(LocalDateTime inicioIntervalo, LocalDateTime finIntervalo) {
        LocalDateTime repeticion = inicio;
        var repeticiones = new TreeSet<LocalDateTime>(); //el treeset los guarda ordenados, puede que especificamente aca no sirva pero en una funcion de abajo si
        if (inicio.isAfter(finIntervalo)) {
            return repeticiones;
        }
        if (tipoFin.equals(TipoFinalizacion.FECHA)) {
            if (LocalDateTime.parse(finalizacion).isBefore(inicioIntervalo)) {
                return repeticiones;
            }
            else {
                while (repeticion.isBefore(LocalDateTime.parse(finalizacion)) && repeticion.isBefore(finIntervalo)) {
                    if (repeticion.isAfter(inicioIntervalo) || repeticion.equals(inicioIntervalo)) {
                        repeticiones.add(repeticion);
                    }
                    repeticion = sumarTiempo(repeticion);
                }
                return repeticiones;
            }
        }
        if (tipoFin.equals(TipoFinalizacion.CANTIDAD)) {
            if (obtenerUltimaRepeticion(inicio).isBefore(inicioIntervalo)) {
                return repeticiones;
            }
            else {
                for (int i = 0; i < Integer.parseInt(finalizacion); i++) {
                    if (repeticion.isAfter(inicioIntervalo) || repeticion.equals(inicioIntervalo)) {
                        repeticiones.add(repeticion);
                    }
                    repeticion = sumarTiempo(repeticion);
                    if (repeticion.isAfter(finIntervalo)) {
                        break;
                    }
                }
                return repeticiones;
            }
        }
        else {
            while (repeticion.isBefore(finIntervalo)) {
                if (repeticion.isAfter(inicioIntervalo) || repeticion.equals(inicioIntervalo)) {
                    repeticiones.add(repeticion);
                }
                repeticion = sumarTiempo(repeticion);
            }
            return repeticiones;
        }
    }

    //te obtiene la ultima repeticion de los que tienen cantidad limite
    public LocalDateTime obtenerUltimaRepeticion(LocalDateTime fecha){
        if (tipoRep.equals(TipoRepeticion.DIARIA)) {
            return fecha.plusDays((long) frecuencia * Integer.parseInt(finalizacion));
        }
        if (this.tipoRep.equals(TipoRepeticion.SEMANAL)) {
            return fecha.plusWeeks((long) frecuencia * Integer.parseInt(finalizacion));
        }
        if (this.tipoRep.equals(TipoRepeticion.MENSUAL)) {
            return fecha.plusMonths((long) frecuencia * Integer.parseInt(finalizacion));
        }
        else {
            return fecha.plusYears((long) frecuencia * Integer.parseInt(finalizacion));
        }
    }

    //esto te obtiene todas las repeticiones
    public TreeSet<LocalDateTime> obtenerRepeticiones(int limite) {
        var repeticiones = new TreeSet<LocalDateTime>();
        LocalDateTime repeticion = inicio;

        if (dias.length == 0) {
            agregarRepeticiones(repeticiones, repeticion, limite); //para agregar las que respetan una intervalo
        } else {
            agregarRepeticionesDS(repeticiones, repeticion, limite); //para agregar las que son dias de le semana
        }

        return repeticiones;
    }

    public void agregarRepeticiones(TreeSet<LocalDateTime> repeticiones, LocalDateTime repeticion, int limite) {
        if (tipoFin.equals(TipoFinalizacion.INFINITA)) {
            for (int i = 0; i < limite; i++) {
                repeticiones.add(repeticion);
                repeticion = sumarTiempo(repeticion);
            }
        }
        if (tipoFin.equals(TipoFinalizacion.CANTIDAD)) {
            for (int i = 0; i < Integer.parseInt(finalizacion); i++) {
                repeticiones.add(repeticion);
                repeticion = sumarTiempo(repeticion);
            }
        }
        if (tipoFin.equals(TipoFinalizacion.FECHA)) {
            while (repeticion.isBefore(LocalDateTime.parse(finalizacion))) {
                repeticiones.add(repeticion);
                repeticion = sumarTiempo(repeticion);
            }
        }
    }

    public LocalDateTime sumarTiempo(LocalDateTime repeticion){
        if (tipoRep.equals(TipoRepeticion.DIARIA)) {
            return repeticion.plusDays(frecuencia);
        }
        if (this.tipoRep.equals(TipoRepeticion.SEMANAL)) {
            return repeticion.plusWeeks(frecuencia);
        }
        if (this.tipoRep.equals(TipoRepeticion.MENSUAL)) {
            return repeticion.plusMonths(frecuencia);
        }
        else {
            return repeticion.plusYears(frecuencia);
        }
    }

    public void agregarRepeticionesDS(TreeSet<LocalDateTime> repeticiones, LocalDateTime repeticion, int limite) {
        if (tipoFin.equals(TipoFinalizacion.INFINITA)) {
            for (int i = 0; i < limite;) {
                TreeSet<LocalDateTime> repSemanalas = calcularSemanales(repeticion);
                for (LocalDateTime r: repSemanalas) { // esto es una mierda pero es lo primero que se me ocurrio para respetar la condicion de corte
                    if (i < limite) {
                        repeticiones.add(r);
                        i++;
                    }
                }
                repeticion = repeticion.plusWeeks(frecuencia);
            }
        }
        if (tipoFin.equals(TipoFinalizacion.CANTIDAD)) {
            for (int i = 0; i < Integer.parseInt(finalizacion);) {
                TreeSet<LocalDateTime> repSemanales = calcularSemanales(repeticion);
                for (LocalDateTime r: repSemanales) { // esto es una mierda pero es lo primero que se me ocurrio para respetar la condicion de corte
                    if (i < Integer.parseInt(finalizacion)) {
                        repeticiones.add(r);
                        i++;
                    }
                }
                repeticion = repeticion.plusWeeks(frecuencia);
            }
        }
        if (tipoFin.equals(TipoFinalizacion.FECHA)) {
            while (repeticion.isBefore(LocalDateTime.parse(finalizacion))) {
                TreeSet<LocalDateTime> repSemanales = calcularSemanales(repeticion);
                for (LocalDateTime r: repSemanales) { // esto es una mierda pero es lo primero que se me ocurrio para respetar la condicion de corte
                    if (r.isBefore(LocalDateTime.parse((finalizacion)))) {
                        repeticiones.add(r);
                    }
                }
                repeticion = repeticion.plusWeeks(frecuencia);
            }
        }
    }

    public TreeSet<LocalDateTime> calcularSemanales(LocalDateTime repeticion) {
        var repeticiones = new TreeSet<LocalDateTime>();
        for (DayOfWeek diaRep : dias) {
                DayOfWeek dia = repeticion.getDayOfWeek();
                int resta = diaRep.getValue() - dia.getValue();
                if (resta >= 0) {
                    repeticiones.add(repeticion.plusDays(resta));
                } else {
                    repeticiones.add(repeticion.plusDays(resta).plusWeeks(1));
                }
            }
        return repeticiones;
    }
}

