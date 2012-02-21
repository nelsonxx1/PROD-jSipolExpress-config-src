package dataBase;

import java.util.ArrayList;
import java.util.Date;
import modelo.HibernateUtil;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.personas.dominio.TipoPersona;
import modelo.entidades.sms.SMSPreEscrito;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author orlandobcrra
 */
public class SQL0909 {

    public SQL0909() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction t = s.beginTransaction();
            AuditoriaBasica a = new AuditoriaBasica(new Date(), "defaultData", true, true, false, true);
            s.save(new TipoPersona("IVA", "CONTRIBUYENTE", true, a));
            s.save(new TipoPersona("RAZ", "RAZON SOCIAL", true, a));
            ArrayList<SMSPreEscrito> list = new ArrayList<SMSPreEscrito>(0);
            list.add(new SMSPreEscrito("BIENVENIDA", ":nombre, reciba una cordial bienvenida a nuestra organizacion en nombre de EMPRESA. Queremos que se sienta Seguro. Feliz dia", a));
            list.add(new SMSPreEscrito("GIRO PENDIENTE", "Buen dia :nombre, EMPRESA le recuerda que su proximo giro vence el dia :fecha, por el monto de Bs :monto.", a));
            list.add(new SMSPreEscrito("GIRO VENCIDO", "Buen dia :nombre, EMPRESA le recuerda que ud tiene un giro vencido del dia :fecha, por el monto de :monto.", a));
            list.add(new SMSPreEscrito("RENOVACION", "Buen dia :nombre, EMPRESA le recuerda que su poliza vence el día :fecha, por favor comunicarse lo mas pronto posible. TELF", a));
            list.add(new SMSPreEscrito("CUMPLEAÑOS", "Feliz Cumpleaños :nombre, le desea EMPRESA en este día tan especial.", a));
            list.add(new SMSPreEscrito("DOC. ANEXO POR VENCER", "Buen día :nombre, EMPRESA le recuerda que su :tipoDocumento vence el dia :fecha. Por favor, tramitelo con antelacion.", a));
            for (SMSPreEscrito o : list) {
                System.out.println(s.save(o));
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
