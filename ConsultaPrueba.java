
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modelo.HibernateUtil;
import modelo.entidades.personas.maestra.Persona;
import modelo.entidades.polizas.financiamiento.Financiamiento;
import modelo.entidades.polizas.financiamiento.Giro;
import modelo.util.bean.BeanVO;
import modelo.util.ehts.BusinessKey;
import modelo.util.ehts.Method;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ConsultaPrueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        //        List l=s.createQuery(
        //                "SELECT p.nombreLargo, t.numero " +
        //                "FROM "+Persona.class.getName()+" p " +
        //                "LEFT JOIN p.telefonos t " +
        //                "WHERE p.auditoria.activo=true").list();
        //        for (Object object : l) {
        //            System.out.print(((Object[])object)[0]+" ");
        //            System.out.println(((Object[])object)[1]);
        //        }
        //        Transaction t = s.beginTransaction();
        //        Recibo r=(Recibo) s.createQuery("FROM "+Recibo.class.getName()).uniqueResult();
        //        r.getPoliza().setVehiculo(null);
        //        s.update(r);
        //        t.commit();
        //        //---------------
        //        List l = s.createQuery(
        //                "SELECT " +
        //                "r.poliza.asegurado.nombreLargo, t.numeroCompleto, r.primaTotal " +
        //                "FROM " + Recibo.class.getName() + " r " +
        //                "LEFT OUTER JOIN r.poliza.asegurado.telefonos t ")
        //                .list();
        //        for (Object object : l) {
        //            System.out.print(((Object[]) object)[0] + " ");
        //            System.out.print(((Object[]) object)[1] + " ");
        //            System.out.println(((Object[]) object)[2]);
        //        }
        //        List l=s.createQuery("SELECT t.numero FROM modelo.entidades.personas.maestra.Persona as p join p.telefonos t").list();
        //        for (Object object : l) {
        //            System.out.println(object);
        //        }
        //---
        //        List l = s.createQuery(
        //                "SELECT r.poliza.asegurado.rif.cedulaCompleta, r.poliza.asegurado.nombreLargo, r.poliza.cobrador.nombreLargo, r.poliza.compania.nombreCorto, r.poliza.ramoPoliza.nombreCorto, r.poliza.numero, r.vigenciaHasta, r.poliza.vehiculo.placa, r.poliza.vehiculo.marcaModelo.nombre, r.poliza.bienAseguradoDescrip, r.primaTotal, t.codigoArea.numero, t.numero "
        //                + "FROM " + Recibo.class.getName() + " r "
        //                + "LEFT OUTER JOIN r.poliza.asegurado.telefonos t ").list();
        //        for (Object object : l) {
        //            System.out.print(((Object[]) object)[0] + " ");
        //            System.out.print(((Object[]) object)[1] + " ");
        //            System.out.println(((Object[]) object)[2]);
        //        }
        //---------
        ////        s.createQuery(
        ////                "SELECT r.primaTotal, r.numero FROM modelo.entidades.polizas.recibos.maestra.Recibo as r"
        ////                ).list();
//        List l=s.createQuery(
//                "SELECT p.nombreLargo, t.numero " +
//                "FROM "+Persona.class.getName()+" p " +
//                "LEFT JOIN p.telefonos t " +
//                "WHERE p.auditoria.activo=true").list();
//        for (Object object : l) {
//            System.out.print(((Object[])object)[0]+" ");
//            System.out.println(((Object[])object)[1]);
//        }

//        Transaction t = s.beginTransaction();
//        Recibo r=(Recibo) s.createQuery("FROM "+Recibo.class.getName()).uniqueResult();
//        r.getPoliza().setVehiculo(null);
//        s.update(r);
//        t.commit();
//        //---------------
//        List l = s.createQuery(
//                "SELECT " +
//                "r.poliza.asegurado.nombreLargo, t.numeroCompleto, r.primaTotal " +
//                "FROM " + Recibo.class.getName() + " r " +
//                "LEFT OUTER JOIN r.poliza.asegurado.telefonos t ")
//                .list();
//        for (Object object : l) {
//            System.out.print(((Object[]) object)[0] + " ");
//            System.out.print(((Object[]) object)[1] + " ");
//            System.out.println(((Object[]) object)[2]);
//        }

//        List l=s.createQuery("SELECT t.numero FROM modelo.entidades.personas.maestra.Persona as p join p.telefonos t").list();
//        for (Object object : l) {
//            System.out.println(object);
//        }

        //---

//        List l = s.createQuery(
//                "SELECT r.poliza.asegurado.rif.cedulaCompleta, r.poliza.asegurado.nombreLargo, r.poliza.cobrador.nombreLargo, r.poliza.compania.nombreCorto, r.poliza.ramoPoliza.nombreCorto, r.poliza.numero, r.vigenciaHasta, r.poliza.vehiculo.placa, r.poliza.vehiculo.marcaModelo.nombre, r.poliza.bienAseguradoDescrip, r.primaTotal, t.codigoArea.numero, t.numero "
//                + "FROM " + Recibo.class.getName() + " r "
//                + "LEFT OUTER JOIN r.poliza.asegurado.telefonos t ").list();
//        for (Object object : l) {
//            System.out.print(((Object[]) object)[0] + " ");
//            System.out.print(((Object[]) object)[1] + " ");
//            System.out.println(((Object[]) object)[2]);
//        }

        //---------
////        s.createQuery(
////                "SELECT r.primaTotal, r.numero FROM modelo.entidades.polizas.recibos.maestra.Recibo as r"
////                ).list();


//
//        List l=s.createQuery("SELECT p as r2 FROM modelo.entidades.personas.maestra.PersonaNatural as p inner join p.tiposPersona t WHERE 'ASE' = t.idPropio").list();
//        for (Object object : l) {
//            Object par[]=(Object[])object;
//            System.out.println(par[0]);
//            Persona p=(Persona)object;
//            System.out.println(p.getNombreLargo());
//            System.out.println(p.getRif().getCedulaCompleta());
//        }

        //
        //List l = s.createQuery("SELECT p.nombreLargo, month(d.fechaVencimiento) FROM " + Persona.class.getName() + " as p join p.documentos d WHERE d.fechaVencimiento=:fecha").setDate("fecha", new Date(2010-1900,6,5)).list();
//        List l = s.createQuery("SELECT F FROM modelo.entidades.polizas.financiamiento.Financiamiento F LEFT JOIN F.recibos R WHERE R.numero='75891'").list();
//        System.out.println("**");
//        for (Object o : l) {
//            System.out.println(((Financiamiento)o).getNumeroFF());
//        }
        //System.out.println(s.createQuery("DELETE FROM "+Distribucion.class.getName()).executeUpdate());

//
//        Query q = s.createQuery("SELECT DISTINCT C FROM modelo.entidades.personas.maestra.Persona C LEFT JOIN C.tiposPersona T WHERE T.idPropio = 'SEG' AND C.auditoria.activo=? AND C.rif.cedulaCompleta like ?  ORDER BY C.rif.cedulaCompleta ASC");
//        q=q.setBoolean(0, true);
//        q=q.setString(1, "J%");
//        List l = q.list();
//        System.out.println("**");
//        for (Object o : l) {
//            System.out.println(((Persona) o).getNombreCorto());
//        }
        Query q = s.createQuery("SELECT f.pagador, g, r.poliza.productor.nombreCorto FROM " + Financiamiento.class.getName() + " as f join f.giros g join f.recibos r WHERE g.fechaVencimiento=?").setDate(0, new Date());
        List l = q.list();
        System.out.println("lista: " + l.size());
        System.out.println("**");
        Set<datoGiro> set = new HashSet<datoGiro>(0);
        for (Object object : l) {
            Object ob[] = (Object[]) object;
            set.add(new datoGiro((Persona)ob[0],(Giro)ob[1],(String)ob[2]));
        }


        System.out.println("set: " + set.size());
//        for (Object o : l) {
//            System.out.println("***");
//            Object ob[] = (Object[]) o;
//            Persona pagador = (Persona) ob[0];
//            Giro giro = (Giro) ob[1];
//
//            System.out.println(giro.getId());
//            System.out.println(ob[2]);
//
//            System.out.println("***");
//        }
//        System.out.println(s.createQuery("UPDATE " + Persona.class.getName() + " SET nombreCorto=upper(nombreLargo) WHERE nombreCorto=null OR length(nombreCorto)=0").executeUpdate());
//        System.out.println(s.createQuery("UPDATE " + Persona.class.getName() + " SET nombreCorto=RTRIM(LTRIM(nombreCorto)), nombreLargo=RTRIM(LTRIM(nombreLargo))").executeUpdate());
        t.commit();
        s.close();
    }
}

class datoGiro extends BeanVO {

    @BusinessKey(include = Method.ALL)
    public Persona pagador;
    @BusinessKey(include = Method.ALL)
    public Giro grio;
    @BusinessKey(include = Method.ALL)
    public String productor;

    public datoGiro(Persona pagador, Giro grio, String productor) {
        this.pagador = pagador;
        this.grio = grio;
        this.productor = productor;
    }
}
