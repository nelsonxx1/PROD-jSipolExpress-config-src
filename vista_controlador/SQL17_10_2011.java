/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vista_controlador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modelo.HibernateUtil;
import modelo.entidades.personas.maestra.Persona;
import modelo.entidades.personas.transac.TelefonoPersona;
import modelo.entidades.sms.SMS;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author NELSON
 */
class SQL17_10_2011 {

    public SQL17_10_2011() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            System.out.println("ACTUALIZANDO PERSONAS");

            mensajes(s);

            System.out.println("PERSONAS ACTUALIZADAS");

            System.out.println("ACTUALIZACION 17-10-2011 LISTA");
        } catch (Exception ex) {
            System.out.println("error --comit---: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }

    public void mensajes(Session s) {
        try {
            Transaction t = s.beginTransaction();

            Criteria sql = s.createCriteria(Persona.class);
            List<Persona> f = sql.list();

            int contM = 0;
            for (Persona persona : f) {

                Set<TelefonoPersona> numsTelefonos = persona.getTelefonos();
                List<SMS> smss = new ArrayList<SMS>(0);
                for (TelefonoPersona telefonoPersona : numsTelefonos) {
                    Criteria sqlSms = s.createCriteria(SMS.class);
                    sqlSms.add(Restrictions.like("numero", "%" + telefonoPersona.getNumeroCompleto() + "%"));
                    smss = sqlSms.list();

                    boolean band = false;

                    for (SMS sms : smss) {
                        for (SMS aux : persona.getSmss()) {
                            if (aux.getId() == sms.getId()) {
                                band = true;
                                break;
                            }
                        }
                        if (!band) {
                            persona.getSmss().add(sms);
                        }
                        band=false;
                    }
                }
                contM += smss.size();
                persona.setSmss(smss);
                s.update(persona);
            }
            System.out.println(contM + " Mensajes");
            t.commit();
        } catch (Exception e) {
            System.out.println("----Personas----");
            e.printStackTrace();
        }
    }
}
