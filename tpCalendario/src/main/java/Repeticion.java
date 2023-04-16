import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Repeticion  {

    private LocalDateTime inicio;       //fecha en la que se comienza el primer evento
    private String tipo;                //que tipo de repeticion es (diaria, semanal, mensual, anual)
    private Integer frecuencia;         //cada cuanto se repite (cada 2 dias, cada 3 meses, etc.)
    private DayOfWeek[] dias;           //si es semanal y se especifican dias, lista de los dias
    private String tipoFinalizacion;    //como termina (nunca, despues de cierta cantidad, despues de una fecha)
    private String finalizacion;        //si termina despues de una cantidad es un numero, si termina despues de una fecha es una fecha en formato LocalDateTime

    public Repeticion(LocalDateTime inicio, String tipo, Integer frecuencia, String tipoFinalizacion, String finalizacion, DayOfWeek[] dias) {
        this.inicio = inicio;
        this.tipo = tipo;
        this.frecuencia = frecuencia;
        this.dias = dias;
        this.tipoFinalizacion = tipoFinalizacion;
        this.finalizacion = finalizacion;
    }

    //metodo prototipo para poder entender la logica de las repeticiones, no necesariamente se usara en el calendario
    public ArrayList<LocalDateTime> obtenerRepeticiones(int limite) {
        var repeticiones = new ArrayList<LocalDateTime>();              //lista de fechas donde se repite
        LocalDateTime repeticion = inicio;                              //fecha inicial

        //dependiendo del tipo de repeticion se calcula distinto, mas o menos
        if (tipo.equals("diaria")) {
            obtenerDiarias(repeticiones, repeticion, limite);
        }
        if (this.tipo.equals("semanal")) {
            obtenerSemanales(repeticiones, repeticion, limite);
        }
        if (this.tipo.equals("mensual")) {
            obtenerMensuales(repeticiones, repeticion, limite);
        }
        if (this.tipo.equals("anual")) {
            obtenerAnuales(repeticiones, repeticion, limite);
        }
        return repeticiones;
    }

    public void obtenerDiarias(ArrayList<LocalDateTime> repeticiones, LocalDateTime repeticion, int limite) {
        if (tipoFinalizacion.equals("nunca")) {                 //caso donde la repeticion es infinita
            for (int i = 0; i < limite; i++) {                  //se iterara hasta un limite
                repeticiones.add(repeticion);
                repeticion = repeticion.plusDays(frecuencia);   //tras agregar una repeticon aumenta la fecha en la cantidad definida por la frecuencia
            }
        }
        if (tipoFinalizacion.equals("cantidad")) {                          //caso donde la repeticion la determina una cantidad
            for (int i = 0; i < Integer.parseInt(finalizacion); i++) {      //itero hasta dicha cantidad, la paso de string a int
                repeticiones.add(repeticion);
                repeticion = repeticion.plusDays(frecuencia);
            }
        }
        if (tipoFinalizacion.equals("fecha")) {                                     //caso donde la repeticion la determina una fecha
            while (repeticion.isBefore(LocalDateTime.parse(finalizacion))) {        //itero hasta alcanzar la fecha, paso de string a LocalDateTime
                repeticiones.add(repeticion);
                repeticion = repeticion.plusDays(frecuencia);
            }
        }
    }

    public void obtenerSemanales(ArrayList<LocalDateTime> repeticiones, LocalDateTime repeticion, int limite) {
        if (tipoFinalizacion.equals("nunca")) {
            for (int i = 0; i < limite; i++) {
                calcularSemanales(repeticiones, repeticion);        //metodo extra para calcular los dos casos posibles
                repeticion = repeticion.plusWeeks(frecuencia);      //aumento le fecha en la cantidad de semanas definidas en el intervalo, es lo mismo para los dos casos
            }
        }
        if (tipoFinalizacion.equals("cantidad")) {
            for (int i = 0; i < Integer.parseInt(finalizacion); i++) {
                calcularSemanales(repeticiones, repeticion);
                repeticion = repeticion.plusWeeks(frecuencia);
            }
        }
        if (tipoFinalizacion.equals("fecha")) {
            while (repeticion.isBefore(LocalDateTime.parse(finalizacion))) {
                calcularSemanales(repeticiones, repeticion);
                repeticion = repeticion.plusWeeks(frecuencia);
            }
        }
    }

    public void calcularSemanales(ArrayList<LocalDateTime> repeticiones, LocalDateTime repeticion) {
        if (dias.length != 0) {                                 //si tengo una lista de dias
            for (DayOfWeek diaRep : dias) {                     //itero la lista de dias
                DayOfWeek dia = repeticion.getDayOfWeek();      //obtengo que dia es la fecha inicial o la fecha tras aumentar una semana
                int resta = diaRep.getValue() - dia.getValue(); //DayOfWeek le otorga un numero a cada dia -> lunes = 1, ..., domingo = 7
                if (resta >= 0) {
                    repeticiones.add(repeticion.plusDays(resta));               //si el resultado de la resta es positivo sumo los dias que indica
                } else {
                    repeticiones.add(repeticion.plusDays(resta).plusWeeks(1));  //si el resultado es negativo le sumo una semana para contrarrestar la resta
                }
            }
        }
        else {
            repeticiones.add(repeticion); //si no hay lista de dias se comporta como el metodo de repeticiones diarias
        }
        //esta funcion no esta completa, no contempla si un dia se pasa de la fecha limite, ni de la cantidad de repeticiones limite o el limite indicado
        //dependiendo el orden de la lista de dias es como agrega las fechas, pudiendo no agregarlas en orden cronologico
    }

    //misma logica que las diarias pero aumento un mes
    public void obtenerMensuales(ArrayList<LocalDateTime> repeticiones, LocalDateTime repeticion, int limite) {
        if (tipoFinalizacion.equals("nunca")) {
            for (int i = 0; i < limite; i++) {
                repeticiones.add(repeticion);
                repeticion = repeticion.plusMonths(frecuencia);
            }
        }
        if (tipoFinalizacion.equals("cantidad")) {
            for (int i = 0; i < Integer.parseInt(finalizacion); i++) {
                repeticiones.add(repeticion);
                repeticion = repeticion.plusMonths(frecuencia);
            }
        }
        if (tipoFinalizacion.equals("fecha")) {
            while (repeticion.isBefore(LocalDateTime.parse(finalizacion))) {
                repeticiones.add(repeticion);
                repeticion = repeticion.plusMonths(frecuencia);
            }
        }
    }

    //misma logica que las diarias pero aumenta un año
    public void obtenerAnuales(ArrayList<LocalDateTime> repeticiones, LocalDateTime repeticion, int limite) {
        if (tipoFinalizacion.equals("nunca")) {
            for (int i = 0; i < limite; i++) {
                repeticiones.add(repeticion);
                repeticion = repeticion.plusYears(frecuencia);
            }
        }
        if (tipoFinalizacion.equals("cantidad")) {
            for (int i = 0; i < Integer.parseInt(finalizacion); i++) {
                repeticiones.add(repeticion);
                repeticion = repeticion.plusYears(frecuencia);
            }
        }
        if (tipoFinalizacion.equals("fecha")) {
            while (repeticion.isBefore(LocalDateTime.parse(finalizacion))) {
                repeticiones.add(repeticion);
                repeticion = repeticion.plusYears(frecuencia);
            }
        }
    }
}
