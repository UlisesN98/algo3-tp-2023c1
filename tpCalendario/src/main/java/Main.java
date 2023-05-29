import calendario.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {
        @Override
        public void start(Stage stage) throws Exception {

            Calendario calendario;
            try {
                var archivo = new BufferedInputStream(new FileInputStream("calendario"));
                calendario = Calendario.deserializar(archivo);
            }
            catch (IOException error) {
                calendario = new Calendario();
            }

            Vista vista = new Vista(stage, calendario);
            Controlador controlador = new Controlador(calendario, vista);

            controlador.iniciar();
        }

        public static void main(String[] args) {
            launch();
        }

    }

