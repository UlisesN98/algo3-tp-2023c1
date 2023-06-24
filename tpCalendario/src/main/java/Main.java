import calendario.Calendario;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private final Calendario calendario;
    private final String ruta;

    public Main() {
        ruta = "calendario";
        calendario = Estado.recuperar(ruta);
        Estado.guardar(calendario, ruta);
    }

    @Override
    public void start(Stage stage) {
        Actualizador actualizador = new Actualizador(calendario, ruta);
        VistaActividades vistaActividades = new VistaActividades(calendario, actualizador, stage);
        ControladorAlarma controladorAlarma = new ControladorAlarma(calendario, ruta);
        vistaActividades.mostrar();
    }
}