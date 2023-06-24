import calendario.Calendario;

import java.io.*;

public class Estado {

    public static void guardar(Calendario calendario, String ruta) {
        try {
            var estado = new BufferedOutputStream(new FileOutputStream(ruta));
            calendario.serializar(estado);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Calendario recuperar(String ruta) {
        Calendario c;
        try {
            var estado = new BufferedInputStream(new FileInputStream(ruta));
            c = Calendario.deserializar(estado);
        } catch (FileNotFoundException e) {
            c = new Calendario();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return c;
    }
}
