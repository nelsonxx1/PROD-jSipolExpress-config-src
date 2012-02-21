package dataBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelo.Dominios;
import modelo.HibernateUtil;
import modelo.entidades.Licencia;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.personas.maestra.Persona;
import modelo.entidades.personas.transac.TelefonoPersona;
import modelo.entidades.polizas.dominio.RamoPoliza;
import modelo.entidades.polizas.financiamiento.Giro;
import modelo.entidades.polizas.recibos.maestra.Recibo;
import modelo.entidades.sms.SMSPreEscrito;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc 
 */
public class SQL0706 {

    public SQL0706() {

        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
//            //mysql
//            //sqlUpdate(s,"UPDATE hibernate_sequence SET next_val = 3000");
//
            //hsqldb
            sqlUpdate(s, "ALTER SEQUENCE hibernate_sequence RESTART WITH 5000");
            hqlUpdate(s, "UPDATE " + Persona.class.getName() + " SET nombreCorto=RTRIM(LTRIM(nombreCorto)), nombreLargo=RTRIM(LTRIM(nombreLargo))");
            sqlUpdate(s, "ALTER TABLE \"PUBLIC\".\"RECIBO\" ALTER COLUMN \"BIENASEGURADODESCRIP\" varchar(2050);");
            //--------------

            //hqlUpdate(s, "UPDATE " + SMSMasa.class.getName() + " SET texto=upper(nombreLargo) WHERE nombreCorto=null OR length(nombreCorto)=0");

            hqlUpdate(s, "UPDATE " + Persona.class.getName() + " SET nombreCorto=upper(nombreLargo) WHERE nombreCorto=null OR length(nombreCorto)=0");
            hqlUpdate(s, "UPDATE " + RamoPoliza.class.getName() + " SET nombre=upper(nombre), nombreCorto=upper(nombreCorto)");
            hqlUpdate(s, "UPDATE " + Licencia.class.getName() + " SET blockSize=15");
            hqlUpdate(s, "UPDATE " + Giro.class.getName() + " SET estatus='" + Dominios.EstatusGiro.COBRADO_GIRO.name() + "' WHERE estatus='" + Dominios.EstatusGiro.PENDIENTE.name() + "' AND fechaCobro!=null");
            hqlUpdate(s, "UPDATE " + Giro.class.getName() + " SET estatus='" + Dominios.EstatusGiro.COBRADO_GIRO.name() + "' WHERE estatus='COBRADO'");
            try {
                Transaction t = s.beginTransaction();
                String hql = "UPDATE " + TelefonoPersona.class.getName() + " SET notificar=:valor WHERE notificar is null";
                String hql2 = "UPDATE " + TelefonoPersona.class.getName() + " SET notificar=:valor WHERE substring(numeroCompleto,1,3)='414' OR substring(numeroCompleto,1,3)='424' OR substring(numeroCompleto,1,3)='416' OR substring(numeroCompleto,1,3)='426' OR substring(numeroCompleto,1,3)='412'";
                System.out.println(s.createQuery(hql).setBoolean("valor", false).executeUpdate() + " - " + hql);
                System.out.println(s.createQuery(hql2).setBoolean("valor", true).executeUpdate() + " - " + hql);
                t.commit();
            } catch (Exception e) {
                System.out.println("----hql----");
                System.out.println("notificar");
                e.printStackTrace();
            }
            Transaction t = s.beginTransaction();
            //s.createQuery("DELETE FROM " + SMSMasa.class.getName()).executeUpdate();
            //s.createQuery("DELETE FROM " + SMSPreEscrito.class.getName()).executeUpdate();
            AuditoriaBasica a = new AuditoriaBasica(new Date(), "defaultData", true, true, false, true);
            ArrayList<SMSPreEscrito> list = new ArrayList<SMSPreEscrito>(0);
//            list.add(new SMSPreEscrito(Dominios.TipoSMS.GIRO, "GIRO VENCIDO", "Buen dia :nombre, EMPRESA le recuerda que su proximo giro vence el dia :fecha, por el monto de Bs :monto.", a));
//            list.add(new SMSPreEscrito(Dominios.TipoSMS.GIRO, "GIRO PENDIENTE", "Buen dia :nombre, EMPRESA le recuerda que ud tiene un giro que vence el dia :fecha, por el monto de :monto.", a));
//            list.add(new SMSPreEscrito(Dominios.TipoSMS.RENOVACION, "RENOVACION", "Buen dia :nombre, EMPRESA le recuerda que su poliza vence el día :fecha, por favor comunicarse lo mas pronto posible. TELF", a));
//            list.add(new SMSPreEscrito(Dominios.TipoSMS.BIENVENIDA, "BIENVENIDA", ":nombre, reciba una cordial bienvenida a nuestra organizacion en nombre de EMPRESA. Queremos que se sienta Seguro. Feliz dia", a));
//            list.add(new SMSPreEscrito(Dominios.TipoSMS.CUMPLEAYO, "CUMPLEAÑOS", "Feliz Cumpleaños :nombre, le desea EMPRESA en este día tan especial.", a));
//            list.add(new SMSPreEscrito(Dominios.TipoSMS.DOCUMENTO, "DOC. ANEXO POR VENCER", "Buen día :nombre, EMPRESA le recuerda que su :tipoDocumento vence el dia :fecha. Por favor, tramitelo con antelacion.", a));
            for (SMSPreEscrito o : list) {
                System.out.println(s.save(o));
            }
            List rs = s.createQuery("FROM " + Recibo.class.getName()).list();
            for (Object object : rs) {
                Recibo r = ((Recibo) object);
//                Distribucion ddd = new Distribucion(r.getPoliza().getCobrador(), r, r.getPorcComiCob(), a);
//                r.getDistribuciones().add(ddd);
//                if (r.getPorcComiCob() != null) {
//                    Distribucion ddd2 = new Distribucion(r.getPoliza().getProductor(), r, 100.0 - r.getPorcComiCob(), 100.0, 100.0, a);
//                    r.getDistribuciones().add(ddd2);
//                }
//                for (Distribucion distribucion : r.getDistribuciones()) {
//                    System.out.println(s.save(distribucion));
//                }
                if (r.getCobrador2() == null) {
                    r.setCobrador2(r.getPoliza().getCobrador());
                    s.update(r);
                } else {
                    //System.out.println("no nulo");
                }
            }
            t.commit();
            System.out.println("LISTO TODO SQL");
        } catch (Exception ex) {
            System.out.println("--comit---: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            s.close();
        }

    }

    public void hqlUpdate(Session s, String hql) {
        try {
            Transaction t = s.beginTransaction();
            System.out.println(s.createQuery(hql).executeUpdate() + " - " + hql);
            t.commit();
        } catch (Exception e) {
            System.out.println("----hql----");
            System.out.println(hql);
            e.printStackTrace();
        }
    }

    public void sqlUpdate(Session s, String sql) {
        try {
            Transaction t = s.beginTransaction();
            System.out.println(s.createSQLQuery(sql).executeUpdate() + " - " + sql);
            t.commit();
        } catch (Exception e) {
            System.out.println("----sql----");
            System.out.println(sql);
            e.printStackTrace();
        }
    }
}
