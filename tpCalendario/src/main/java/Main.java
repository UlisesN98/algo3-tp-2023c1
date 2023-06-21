import calendario.Calendario;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private final Calendario calendario;
    private final String ruta;

    public Main() {
        ruta = "calendario";
        calendario = Persistir.recuperarEstado(ruta);
        Persistir.guardarEstado(calendario, ruta);
    }

    @Override
    public void start(Stage stage) {
        Actualizar actualizar = new Actualizar(calendario, ruta);
        Mostrar mostrar = new Mostrar(calendario, actualizar, stage);
        Alarmar alarmar = new Alarmar(calendario, ruta);
        mostrar.mostrarVistaActividades();
    }
}