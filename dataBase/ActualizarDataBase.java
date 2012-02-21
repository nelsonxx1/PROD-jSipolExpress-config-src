package dataBase;


import modelo.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarDataBase {

    private Session s;
    private Transaction tx;

    public ActualizarDataBase() {
        s = HibernateUtil.getAnnotationConfiguration().
                setProperty("hibernate.hbm2ddl.auto", "update").
                buildSessionFactory().openSession();
        tx = s.beginTransaction();
        tx.commit();
        s.close();
    }

    public static void main(String[] args) {
        new ActualizarDataBase();
    }
}
