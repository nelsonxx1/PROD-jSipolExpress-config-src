package dataBase;

import modelo.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author orlandobcrra
 */
public class SQLDROPSMS {

    public SQLDROPSMS() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            System.out.println("borrando");

            sqlUpdate(s, "DROP TABLE SMSAutomatico");

            sqlUpdate(s, "DROP TABLE SMSMasa_SMS");

            sqlUpdate(s, "DROP TABLE SMSMasa");

            sqlUpdate(s, "DROP TABLE SMS");

            sqlUpdate(s, "DROP TABLE SMSPreEscrito");

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
