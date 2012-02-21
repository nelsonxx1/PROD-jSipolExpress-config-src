package dataBase;

import java.util.List;
import modelo.Dominios.EstadoSiniestro;
import modelo.HibernateUtil;
import modelo.entidades.Usuario;
import modelo.entidades.polizas.financiamiento.Financiamiento;
import modelo.entidades.polizas.financiamiento.LNFinanciamiento;
import modelo.entidades.siniestros.maestra.Siniestro;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author orlandobcrra
 */
public class SQL1021 {

    public SQL1021() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction t = s.beginTransaction();
            List<Financiamiento> l = s.createQuery("FROM " + Financiamiento.class.getName()).list();

            for (Financiamiento financiamiento : l) {
                LNFinanciamiento.validarEstatusFinanciamiento(financiamiento);
                s.update(financiamiento);
            }
            t.commit();
            System.out.println("Financiamientos actualizados");

        } catch (Exception ex) {
            System.out.println("--financiamientos---: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            s.close();
        }

        s = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction t = s.beginTransaction();
            List<Siniestro> l = s.createQuery("FROM " + Siniestro.class.getName()).list();

            for (Siniestro siniestro : l) {
                siniestro.setEstado(EstadoSiniestro.ABIERTO);
                s.update(siniestro);
            }
            t.commit();
            System.out.println("Siniestros actualizados");

        } catch (Exception ex) {
            System.out.println("--siniestros---: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            s.close();
        }

        s = HibernateUtil.getSessionFactory().openSession();

        try {
            Transaction t = s.beginTransaction();
            List<Usuario> l = s.createQuery("FROM " + Usuario.class.getName()).list();

            for (Usuario usuario : l) {
                if (usuario.getUserName().compareTo("bcrra") == 0) {
                    usuario.setSuperusuario(Boolean.TRUE);
                    usuario.setModificarPermisos(Boolean.TRUE);
                } else {
                    usuario.setSuperusuario(Boolean.FALSE);
                    if (usuario.getAdministraUsuarios()) {
                        usuario.setModificarPermisos(Boolean.TRUE);
                    } else {
                        usuario.setModificarPermisos(Boolean.FALSE);
                    }
                }
                s.update(usuario);
            }
            t.commit();
            System.out.println("Usuario actualizado");

        } catch (Exception ex) {
            System.out.println("--usuario---: " + ex.getMessage());
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
