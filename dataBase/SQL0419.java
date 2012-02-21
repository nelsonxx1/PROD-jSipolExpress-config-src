package dataBase;

import java.util.List;
import modelo.Dominios;
import modelo.HibernateUtil;
import modelo.entidades.personas.maestra.Persona;
import modelo.entidades.personas.maestra.Rif;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class SQL0419 {

    public SQL0419() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction t = s.beginTransaction();
            hqlUpdate(s, "UPDATE modelo.entidades.personas.maestra.Persona SET nombreCorto=Ucase(nombreCorto), nombreLargo=Ucase(nombreLargo)");
            hqlUpdate(s, "UPDATE modelo.entidades.personas.maestra.PersonaNatural SET nombreCasada=Ucase(nombreCasada)");
            hqlUpdate(s, "UPDATE modelo.entidades.personas.maestra.PersonaNatural SET primerNombre=Ucase(primerNombre), primerApellido=Ucase(primerApellido)");
            hqlUpdate(s, "UPDATE modelo.entidades.personas.maestra.PersonaNatural SET segundoNombre=Ucase(segundoNombre), segundoApellido=Ucase(segundoApellido)");
            hqlUpdate(s, "UPDATE modelo.entidades.personas.maestra.PersonaNatural SET profecion=Ucase(profecion), ocupacion=Ucase(ocupacion)");
            hqlUpdate(s, "UPDATE modelo.entidades.personas.maestra.PersonaNatural SET ciudadNacimiento=Ucase(ciudadNacimiento), estadoNacimiento=Ucase(estadoNacimiento)");
            hqlUpdate(s, "UPDATE modelo.entidades.Reporte SET tipoPapel='Carta' WHERE tipoPapel is null");
            hqlUpdate(s, "UPDATE modelo.entidades.polizas.recibos.maestra.Recibo SET bono1=0 WHERE bono1 is null");
            hqlUpdate(s, "UPDATE modelo.entidades.polizas.recibos.maestra.Recibo SET bono2=0 WHERE bono2 is null");
            hqlUpdate(s, "UPDATE modelo.entidades.polizas.recibos.maestra.Recibo SET devolucion=false WHERE devolucion is null");
            hqlUpdate(s, "UPDATE modelo.entidades.polizas.financiamiento.Financiamiento SET girospagados=0, montopagado=0, montofinanciamiento=0 WHERE girospagados=null");
            updateRif(s);
            hqlUpdate(s, "update modelo.entidades.personas.transac.TelefonoPersona set numeros=str(numero)");
            sqlUpdate(s, "update recibo R set R.financiamiento_id=(select F.id from financiamiento F where F.recibo_id=R.id)");
            sqlUpdate(s, "ALTER TABLE TELEFONOPERSONA DROP \"NUMERO\"");
            sqlUpdate(s, "ALTER TABLE GIRO DROP \"NUMERO\"");
            sqlUpdate(s, "ALTER TABLE FINANCIAMIENTO ALTER COLUMN RECIBO_ID set null");
            sqlUpdate(s, "update financiamiento set recibo_id=null");
            System.out.println("LISTO");
            t.commit();
        } catch (Exception ex) {
            System.out.println("--comit---: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }

    public void hqlUpdate(Session s, String hql) {
        try {
            s.createQuery(hql).executeUpdate();
        } catch (Exception e) {
            System.out.println("----hql----");
            System.out.println(hql);
            e.printStackTrace();
        }
    }

    public void sqlUpdate(Session s, String sql) {
        try {
            s.createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            System.out.println("----sql----");
            System.out.println(sql);
            e.printStackTrace();
        }
    }

    public void updateRif(Session s) {
        try {
            List ps = s.createQuery("from modelo.entidades.personas.maestra.Persona").list();
            for (Object object : ps) {
                Persona p = (Persona) object;
                Rif r = p.getRif();
                String ss = String.valueOf(r.getNumeroCedula());
                if (ss.length() <= 8) {
                    StringBuilder sb = new StringBuilder("00000000");
                    sb.replace(8 - ss.length(), 8, ss);
                    if (r.getFinRif() != null) {
                        r.setRif(r.getTipoCedula().getIn() + sb.toString() + "-" + r.getFinRif());
                    } else {
                        r.setRif("" + (r.getTipoCedula()).getIn() + sb.toString());
                    }
                } else {
                    r.setNumeroCedula(Integer.parseInt(ss.substring(ss.length() - 8)));
                    if (r.getFinRif() != null) {
                        r.setRif("" + (r.getTipoCedula()).getIn() + r.getNumeroCedula() + "-" + r.getFinRif());
                    } else {
                        r.setRif("" + (r.getTipoCedula()).getIn() + r.getNumeroCedula());
                    }
                }
                p.setRif(r);
                s.update(p);
            }
        } catch (Exception e) {
            System.out.println("----updateRif----");
            e.printStackTrace();
        }
    }
}
