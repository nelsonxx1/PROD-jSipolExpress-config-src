
import vista_controlador.ConfigFrame;
import vista_controlador.Logon;

/**
 *
 * @author bc
 */
public class jSipolConfig {

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    ConfigFrame cf = new ConfigFrame();
                    new Logon(cf, true).setVisible(true);
                    cf.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
