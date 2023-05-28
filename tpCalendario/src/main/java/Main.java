import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
        @Override
        public void start(Stage stage) throws Exception {

            var label = new Label("Calendario");
            var scene = new Scene(new VBox(label), 640, 480);
            stage.setScene(scene);
            stage.show();
        }
    }

