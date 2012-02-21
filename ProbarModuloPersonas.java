
import java.util.Date;
import modelo.Dominios;
import modelo.HibernateUtil;
import modelo.entidades.Observacion;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.Usuario;
import modelo.entidades.personas.dominio.TipoActividadEconomica;
import modelo.entidades.personas.dominio.TipoCapacidadEconomica;
import modelo.entidades.personas.dominio.TipoCuentaBancaria;
import modelo.entidades.personas.dominio.TipoDireccion;
import modelo.entidades.personas.dominio.TipoTelefono2;
import modelo.entidades.personas.dominio.TipoPersona;
import modelo.entidades.personas.maestra.PersonaJuridica;
import modelo.entidades.personas.maestra.PersonaNatural;
import modelo.entidades.personas.maestra.Rif;
import modelo.entidades.personas.transac.CuentaBancariaPersona;
import modelo.entidades.personas.transac.DireccionPersona;
import modelo.entidades.personas.transac.TelefonoPersona;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 * Clase Principal para probar todo el modelado de Personas
 * @version 1.0 22/05/2009
 * @since JDK 1.5
 * @author Nelson Moncada
 * @author Orlando Becerra
 */
public class ProbarModuloPersonas {

    private static Session s;
    private static Transaction tx;

    public ProbarModuloPersonas() {

        s = HibernateUtil.getSessionFactory().openSession();
        tx = s.beginTransaction();

        TipoActividadEconomica tipActiv;
        TipoCapacidadEconomica tipCapac;
        TipoCuentaBancaria tipCuent;
        TipoDireccion tipDirec;
        TipoTelefono2 tipTelef;
        TipoPersona tipPerso;
        PersonaJuridica perJurid;
        PersonaNatural perNatur;
        CuentaBancariaPersona cuentaBanc;
        DireccionPersona direccion;
        //DocumentoPersona documento;
        Observacion observac;
        TelefonoPersona telefono;

        Usuario u = new Usuario("Nelson Moncada", "Nelson", new byte[]{}, null, false, false, false);
        s.save(u);

        tx.commit();
        tx = s.beginTransaction();

        AuditoriaBasica ab = new AuditoriaBasica(new Date(), u.getUserName(), true);

        tipActiv = new TipoActividadEconomica("Agricultura", ab);
        tipCapac = new TipoCapacidadEconomica("poca plata", ab);
        tipCuent = new TipoCuentaBancaria("Corriente", ab);
        tipDirec = new TipoDireccion("Casa", ab);
        tipTelef = new TipoTelefono2("casaaaaaaaaaa", ab);
        tipPerso = new TipoPersona("12", "Contratante", false, ab);

        s.save(tipActiv);
        s.save(tipCapac);
        s.save(tipCuent);
        s.save(tipDirec);
        s.save(tipTelef);
        s.save(tipPerso);

        perJurid = new PersonaJuridica(new Rif(Dominios.TipoCedula.JURIDICO, 123), Dominios.Ranking.A, Dominios.TipoContribuyente.FORMAL, new Date(), true, tipCapac, tipActiv, new Date(), "123456", "Uno", 9876.5, 456.1, 789.123456789);
        perJurid.setNombreCorto("corto");
        perJurid.setNombreLargo("largo");

        s.save(perJurid);

        perNatur = new PersonaNatural(new Rif(Dominios.TipoCedula.VENEZOLANO, 18256939), Dominios.TipoNombre.DESCONOCIDO,Dominios.Ranking.A, Dominios.TipoContribuyente.FORMAL, new Date(), false, "Becerra", "Perez", "Orlando", null, null, Dominios.Sexo.MASCULINO, Dominios.EstadoCivil.SOLTERO, new Date(), "SC", "tachira", "Agricultor", "Vago");
        perNatur.setNombreCorto("corto");
        perNatur.setNombreLargo("largo");
        observac = new Observacion("Observacion Ojala funciones", ab);
        cuentaBanc = new CuentaBancariaPersona("numc", "ob", perJurid, tipCuent, ab);
        direccion = new DireccionPersona("direcc", "Nueva obser", tipDirec, ab);
        //documento = new DocumentoPersona("C:/Mis Documentos", "ob", tipDocum, ab);
        telefono = new TelefonoPersona("1213", "sino me voy a dormir", tipTelef, ab);

        perNatur.getTiposPersona().add(tipPerso);
        //tipPerso.getPersonas().add(perNatur);

        perNatur.setActividadEconomica(tipActiv);

        perNatur.setCapacidadEconomica(tipCapac);


        //s.save(perNatur);
        //tx.commit();
        //tx = s.beginTransaction();

        perNatur.getObservaciones().add(observac);

        perNatur.getCuentasBancarias().add(cuentaBanc);

        perNatur.getTelefonos().add(telefono);

        perNatur.getDirecciones().add(direccion);

        //perNatur.getDocumentos().add(documento);

        s.save(perNatur);

        tx.commit();

        tx = s.beginTransaction();
        s.delete(perNatur);
        tx.commit();
        s.close();


    }

    public static void main(String[] args) {
        new ProbarModuloPersonas();
    }
}
