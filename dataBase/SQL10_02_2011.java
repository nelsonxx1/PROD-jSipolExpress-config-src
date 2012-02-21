package dataBase;

import java.util.List;
import modelo.HibernateUtil;
import modelo.entidades.polizas.financiamiento.Financiamiento;
import modelo.entidades.polizas.financiamiento.LNFinanciamiento;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author orlandobcrra
 */
public class SQL10_02_2011 {

    public SQL10_02_2011() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            System.out.println("ACTUALIZANDO FINANCIAMIENTOS");

            financiamientos(s);

            System.out.println("FINANCIAMIENTOS ACTUALIZADOS");

            System.out.println("ACTUALIZACION 10-02-2011 LISTA");
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

    public void financiamientos(Session s) {
        try {
            Transaction t = s.beginTransaction();
            
            Query q=s.createQuery("FROM "+Financiamiento.class.getName());
            List<Financiamiento> f= q.list();
            
            for (Financiamiento financiamiento : f) {
                LNFinanciamiento.actulizarMontoFinanciamiento(financiamiento);
                s.update(financiamiento);
            }
            t.commit();
        } catch (Exception e) {
            System.out.println("----financiamiestos----");
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
