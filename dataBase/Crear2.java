package dataBase;

import controlador.licencia.Equipo;
import java.util.Calendar;
import java.util.Date;
import modelo.Dominios;
import modelo.entidades.Empresa;
import modelo.entidades.Licencia;
import modelo.entidades.Usuario;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.personas.maestra.PersonaNatural;
import modelo.entidades.personas.maestra.Rif;

/**
 *
 * @author bc
 */
public class Crear2 extends Crear {

    public Crear2() {
    }

    @Override
    public void create() {
        empresa();
        licencia();
        byte[] sha256 = {31, -35, -28, -37, -15, -21, 90, 22, 46, 71, -99, 89, 72, -57, 101, -112, 86, -114, 28, -47, -89, -23, 93, 19, 73, 32, -17, -76, 88, 89, -47, -68};
        byte[] md5 = {-112, 30, 11, 103, -81, 109, -46, 47, -8, 94, 79, -3, -17, -78, 54, 26};
        Usuario u = new Usuario("Orlando Becerra", "bcrra", md5, new AuditoriaBasica(), false, true, true);
        s.save(u);
        super.create();
        personas2();
    }

    private void empresa() {
        Empresa empresa = new Empresa("V18256939-1", "NombreEmpresa", "nombreEmpresajSipolee@gmail.com", "jsipoleeSoporte@gmail.com", "/jSipolEE/DocDigital", "./Reportes");
        s.save(empresa);
    }

    private void licencia() {
        Calendar c = Calendar.getInstance();
        c.set(2010, 11, 31);
        //Licencia licencia = new Licencia("bc-4427d1d1eeff", "MI CASA AP", Equipo.encodeText("6898-8EF5"), "vfw:Microsoft WDM Image Capture (Win32):0", true, new Date(), c.getTime(), false);
        Licencia licencia2 = new Licencia("orlandobcrra-pc", "portatil", Equipo.encodeText("2400-AB67"), "vfw:Microsoft WDM Image Capture (Win32):0", true, new Date(), c.getTime(), false,15);
        //s.save(licencia);
        s.save(licencia2);
    }

    public void personas2() {
        PersonaNatural contratante1 = new PersonaNatural();
        contratante1.setRif(new Rif(Dominios.TipoCedula.VENEZOLANO, 18256940));
        contratante1.setPrimerApellido("Contratante");
        contratante1.setPrimerNombre("Asegurado");
        dataPersonaNatural(contratante1, auditoriaActivo);
        contratante1.getTiposPersona().add(tpContratante);
        contratante1.getTiposPersona().add(tpAsegurado);
        s.save(contratante1);

        PersonaNatural productor = new PersonaNatural();
        productor.setRif(new Rif(Dominios.TipoCedula.VENEZOLANO, 5686631));
        productor.setPrimerApellido("Productor");
        productor.setPrimerNombre("Cobrador");
        dataPersonaNatural(productor, auditoriaActivo);
        productor.getTiposPersona().add(tpProductor);
        productor.getTiposPersona().add(tpCobrador);
        s.save(productor);
    }

    public static void main(String[] args) {
        try {
            Crear bd = new Crear2();
            bd.open();
            bd.create();
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



