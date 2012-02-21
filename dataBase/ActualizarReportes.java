package dataBase;

import java.util.ArrayList;
import java.util.Date;
import modelo.Dominios;
import modelo.HibernateUtil;
import modelo.entidades.Reporte;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.polizas.recibos.maestra.Recibo;
import modelo.entidades.siniestros.maestra.Siniestro;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarReportes {

    public ActualizarReportes() {
        System.out.println("Actualizando Reportes");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        System.out.println(s.createQuery("DELETE FROM " + Reporte.class.getName()).executeUpdate());

        System.out.println("Viejos borrados ");

        AuditoriaBasica ab = new AuditoriaBasica(new Date(), "defaultdata", true);

        System.out.println("Creando nuevos reportes");


        ArrayList<Reporte> list = new ArrayList<Reporte>(0);
        //list.add(new Reporte(Dominios.CategoriaReporte.FINANCIAMIENTOS, 0, "FIN-D002", "Financiamientos", "Todas los Financiamientos", "FROM " + Financiamiento.class.getName(), "Carta 8½ x 11 Vertical"));


        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D001", "Todas las Personas x Nombre", "Todas las Personas", "FROM modelo.entidades.personas.maestra.Persona as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D002", "Todas las Personas con Telefono y Direccion", "Todas las Personas con sus Telefonos y Direcciones", "FROM modelo.entidades.personas.maestra.Persona as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D003", "Todas las Personas Naturales con Fecha de Nacimiento, Sexo, Telefono y Direccion.", "Personas segun su Tipo, con Telefonos y Direccions", "FROM modelo.entidades.personas.maestra.PersonaNatural as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D003", "Todas las Personas Naturales con Fecha de Nacimiento, Sexo, Telefono y Direccion.", "Personas segun su Tipo, con Telefonos y Direccions", "FROM modelo.entidades.personas.maestra.PersonaNatural as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical"));        

        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 0, "ZON-D001", "Recibos Cobrados x Zona con comision", "Recibos x Zona", "FROM "+Recibo.class.getName()+" as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.zona.id", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 0, "ZON-D002", "Recibos Cobrados x Cobrador con comision", "Recibos x Cobrador", "FROM "+Recibo.class.getName()+" as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.cobrador2.id", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 0, "ZON-D003", "Recibos Cobrados x Zona/Cobrador con comision", "Recibos x Zona y Cobrador", "FROM "+Recibo.class.getName()+" as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.zona.id, P.cobrador2.id", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 0, "ZON-D004", "Recibos Cobrados x Cobrador/Zona con comision", "Recibos x Cobrador y Zona", "FROM "+Recibo.class.getName()+" as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.cobrador2.id, P.zona.id", "Carta 8½ x 11 Vertical"));
        
        list.add(new Reporte(Dominios.CategoriaReporte.SINIESTROS, 0, "SIN-D001", "Siniestros", "Reporte Compañia/Estatus", "FROM "+Siniestro.class.getName()+" as P ORDER BY P.recibo.poliza.compania.nombreLargo, P.estatus", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.SINIESTROS, 0, "SIN-D002", "Siniestros con Comentarios", "Reporte Compañia/Estatus", "FROM "+Siniestro.class.getName()+" as P ORDER BY P.recibo.poliza.compania.nombreLargo, P.estatus", "Carta 8½ x 11 Vertical"));

        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 0, "COM-D011", "Recibos Cobrados x Cia./Ramo Comision y Bonos ", "Recibos Cobrados con Comision y Bonos", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.nombreLargo", "Extra Oficio 8½ x 14 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 1, "COM-R013", "Resumen Recibos (Cobrados) Compañia/Ramo Comision y Bonos", "(Cobrados) Resumen x Compañia/Ramo Comision y Bonos", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 1, "COM-R014", "Resumen Recibos (Cobrados) Compañia Comision y Bonos", "(Cobrados) Resumen x Compañia Comision y Bonos", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 1, "COM-R011", "Resumen Recibos (Pendientes) Compañia/Ramo Comision y Bonos", "(Pendientes) Resumen x Compañia/Ramo Comision y Bonos", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='PENDIENTE_COBRO' ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.COMISIONES, 1, "COM-R012", "Resumen Recibos (Pendientes) Compañia Comision y Bonos", "(Pendientes) Resumen x Compañia Comision y Bonos", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='PENDIENTE_COBRO' ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));

//        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "COM-D020", "Comision de Recibos Pagados por Zona y Cobrador", "Recibos Pagados por Zona y Cobrador", "FROM " + Distribucion.class.getName() + " as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.recibo.zona.nombre, P.cobrador.id, P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "COM-D021", "Comision y Bono de Recibos Cobrados por Zona y Cobrador", "Comision y Bono de Recibos Cobrados por Zona y Cobrador", "FROM " + Distribucion.class.getName() + " as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.recibo.zona.nombre, P.cobrador.id, P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));

        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "DIS-D020", "Comision de recibos pagados por Grupo y Cobrador", "Recibos Pagados por Zona y Cobrador", "FROM modelo.entidades.polizas.recibos.maestra.Distribucion as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.recibo.poliza.grupoPoliza.nombre, P.cobrador.id, P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "DIS-D021", "Comision y Bonos de Recibos Cobrados por Grupo y Cobrador ", "Comision y Bono de Recibos Cobrados por Grupo y Cobrador", "FROM modelo.entidades.polizas.recibos.maestra.Distribucion as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.recibo.poliza.grupoPoliza.nombre, P.cobrador.id, P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "DIS-D022", "Comision de recibos pagados por Grupo y Cobrador", "Comision de Recibos Pagados por Cobrador y Grupo", "FROM modelo.entidades.polizas.recibos.maestra.Distribucion as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.cobrador.id, P.recibo.poliza.grupoPoliza.nombre, P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "DIS-D023", "Comision y Bonos de Recibos Cobrados por Grupo y Cobrador ", "Comision y Bono de Recibos Cobrados por Cobrador y Grupo", "FROM modelo.entidades.polizas.recibos.maestra.Distribucion as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.cobrador.id, P.recibo.poliza.grupoPoliza.nombre ,P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "DIS-D024", "Comision, Bono1, Bono2 de recibos cobrados por Grupo y Cobrador", "Comision, Bono1 y Bono2 de Recibos Cobrados por Grupo y Cobrador", "FROM modelo.entidades.polizas.recibos.maestra.Distribucion as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.recibo.poliza.grupoPoliza.nombre, P.cobrador.id, P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "DIS-D025", "Comision, Bono1, Bono2 de recibos cobrados por Cobrador y Grupo", "Comision, Bono1 y Bono2 de Recibos Cobrados por Cobrador y Grupo", "FROM modelo.entidades.polizas.recibos.maestra.Distribucion as P WHERE P.recibo.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.cobrador.id, P.recibo.poliza.grupoPoliza.nombre ,P.recibo.fechaPagoComision", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.DISTRIBUCION, 0, "SMS-D001", "Mensajes", "Mensajes Enviados", "FROM modelo.entidades.sms.SMS as P WHERE P.estatus IN('ENVIADO') ORDER BY P.auditoria.fechaInsert", "Carta 8½ x 11 Horizontal"));

        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo", "Recibos Cobrados", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D015", "Recibos Cobrados + Observaciones x Cia./Ramo", "Recibos Cobrados", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo ENERO", "Recibos Cobrados ENERO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-01-01' AND P.fechaCobro<'2010-02-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo FEBRERO", "Recibos Cobrados FEBRERO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-02-01' AND P.fechaCobro<'2010-03-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo MARZO", "Recibos Cobrados MARZO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-03-01' AND P.fechaCobro<'2010-04-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo ABRIL", "Recibos Cobrados ABRIL", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-04-01' AND P.fechaCobro<'2010-05-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo MAYO", "Recibos Cobrados MAYO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-05-01' AND P.fechaCobro<'2010-06-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo ENERO", "Recibos Cobrados ENERO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-01-01' AND P.fechaCobro<'2010-02-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo JUNIO", "Recibos Cobrados JUNIO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-06-01' AND P.fechaCobro<'2010-07-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo JULIO", "Recibos Cobrados JULIO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-07-01' AND P.fechaCobro<'2010-08-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo AGOSTO", "Recibos Cobrados AGOSTO", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-08-01' AND P.fechaCobro<'2010-09-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo SEPTIEMBRE", "Recibos Cobrados SEPTIEMBRE", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-09-01' AND P.fechaCobro<'2010-10-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo OCTUBRE", "Recibos Cobrados OCTUBRE", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-10-01' AND P.fechaCobro<'2010-11-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo NOVIENBRE", "Recibos Cobrados NOVIENBRE", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-11-01' AND P.fechaCobro<'2010-12-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
//        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D012", "Recibos Cobrados x Cia./Ramo DICIEMBRE", "Recibos Cobrados DICIEMBRE", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.fechaCobro>='2010-12-01' AND P.fechaCobro<'2011-01-01' AND P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D011", "Recibos Pendientes x Cia./Ramo", "Recibos Pendientes", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='PENDIENTE_COBRO' ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D013", "Recibos Anulados x Cia./Ramo", "Recibos Anulados", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='ANULADO' ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.nombreLargo", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 1, "REC-R011", "Resumen Recibos (Pendientes) Compañia/Ramo", "(Pendientes) Resumen x Compañia/Ramo", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='PENDIENTE_COBRO' ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 1, "REC-R012", "Resumen Recibos (Pendientes) Compañia", "(Pendientes) Resumen x Compañia", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='PENDIENTE_COBRO' ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 1, "REC-R013", "Resumen Recibos (Cobrados) Compañia/Ramo", "(Cobrados) Resumen x Compañia/Ramo", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 1, "REC-R014", "Resumen Recibos (Cobrados) Compañia", "(Cobrados) Resumen x Compañia", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 1, "REC-R015", "Resumen Recibos (Anulados) Compañia/Ramo", "(Anulados) Resumen x Compañia/Ramo", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='ANULADO' ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 1, "REC-R016", "Resumen Recibos (Anulados) Compañia", "(Anulados) Resumen x Compañia", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus='ANULADO' ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.RECIBOS, 0, "REC-D021", "Recibos Cobrados x Cia./Ramo S/Comision", "Recibos Cobrados Sin Comision", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.nombreLargo"));

        //list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REN-D001", "Renovaciones", "Renovaciones con telefonos y direcciones", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.anulado!='TRUE' ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.nombreLargo"));

        list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REN-D004", "Renovaciones", "Renovaciones con telefonos", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('ANULADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.vigenciaHasta"));
        list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REN-D001", "Renovaciones", "Renovaciones con telefonos y direcciones", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('ANULADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.vigenciaHasta"));        
        list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REN-D002", "Renovaciones con Observaciones del Recibo", "Renovaciones con telefonos, direcciones y observaciones", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('ANULADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.vigenciaHasta"));
        list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REN-D003", "Renovaciones con Observaciones de la Poliza", "Renovaciones con telefonos, direcciones y observaciones", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('ANULADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.vigenciaHasta"));

        list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REC-D001", "Renovacion/Pólizas x Cia./Ramo", "Sin Dirección/Tlfs.", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('ANULADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre"));
        list.add(new Reporte(Dominios.CategoriaReporte.RENOVACIONES, 0, "REC-D002", "Renovacion/Pólizas Cia./Ramo Con Dirección", "Con Direccions y Telefonos del Asegurado", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('ANULADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre"));

        list.add(new Reporte(Dominios.CategoriaReporte.DEVOLUCIONES, 0, "DEV-D001", "Devoluciones x Cia./Ramo", "Recibos con devoluciones de Prima", "SELECT P FROM modelo.entidades.polizas.recibos.maestra.Recibo as P right join P.devoluciones WHERE P.estatus IN('COBRADO_PENDIENTE_COMISION','COBRADO') ORDER BY P.poliza.compania.nombreLargo, P.poliza.ramoPoliza.nombre, P.asegurado.nombreLargo", "Carta 8½ x 11 Horizontal"));

        //list.add(new Reporte(Dominios.CategoriaReporte.FINANCIAMIENTOS.toString(), 1, "FIN-D001", "DETALLE DE CONTRATOS PENDIENTES", "CONTRATOS PENDIENTES x COMPAÑIA", "FROM modelo.entidades.polizas.financiamiento.Financiamiento as P ","Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.FINANCIAMIENTOS, 0, "FIN-D011", "FINANCIAMIENTOS CON GIROS", "GIROS COMPAÑIA", "FROM modelo.entidades.polizas.financiamiento.Financiamiento AS P WHERE P.anuladoF!='TRUE'", "Carta 8½ x 11 Vertical"));
        list.add(new Reporte(Dominios.CategoriaReporte.FINANCIAMIENTOS, 0, "FIN-D012", "FINANCIAMIENTOS CON GIROS PENDIENTES", "GIROS PENDIENTES x COMPAÑIA", "FROM modelo.entidades.polizas.financiamiento.Financiamiento AS P WHERE P.estatus IN('PENDIENTE') ", "Carta 8½ x 11 Vertical"));

        list.add(new Reporte(Dominios.CategoriaReporte.SUPERINTENDENDENCIA, 0, "SUP-D001", "RECIBOS DE PRIMAS PENDIENTES DE COBRO AL: CIERRE DEL EJERCICIO ECONOMICO", "Recibos Pendientes", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('PENDIENTE_COBRO') ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.SUPERINTENDENDENCIA, 0, "SUP-D002", "ESTADO DEMOSTRATIVO DE LAS PRIMAS COBRADAS DURANTE EL EJERCICIO ECONOMICO", "Recibos Cobrados", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('PENDIENTE_COBRO','ANULADO') ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.SUPERINTENDENDENCIA, 0, "SUP-S001", "TABLA DE PRIMAS PENDIENTES DE COBRO AL: CIERRE DEL EJERCICIO ECONOMICO", "Recibos Pendientes", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus IN('PENDIENTE_COBRO') ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        list.add(new Reporte(Dominios.CategoriaReporte.SUPERINTENDENDENCIA, 0, "SUP-S001", "TABLA DEMOSTRATIVO DE LAS PRIMAS COBRADAS DURANTE EL EJERCICIO ECONOMICO", "Recibos Cobrados", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P WHERE P.estatus NOT IN('PENDIENTE_COBRO','ANULADO') ORDER BY P.poliza.compania.nombreLargo", "Carta 8½ x 11 Horizontal"));
        //list.add(new Reporte(Dominios.CategoriaReporte.SUPERINTENDENDENCIA.toString(), 0, "SUP-D003", "RECIBOS ANULADOS", "RECIBOS ANULADOS", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P ORDER BY P.poliza.compania.nombreLargo","Carta 8½ x 11 Horizontal"));
        //list.add(new Reporte(Dominios.CategoriaReporte.SUPERINTENDENDENCIA.toString(), 0, "SUP-D004", "DEVOLUCIONES DE PRIMAS", "Devoluciones de Prima", "FROM modelo.entidades.polizas.recibos.maestra.Recibo as P ORDER BY P.poliza.compania.nombreLargo"));
        for (Reporte o : list) {
            s.saveOrUpdate(o);
        }
//        System.out.println("antes del comit");

        tx.commit();
        System.out.println("Reporets Nuevos Creados");
        s.close();
        //System.out.println("close");
        System.out.println("Listo");
        
        System.out.println("Fin de Actualizacion de reportes");
    }

    public static void main(String[] args) {
        new ActualizarReportes();
    }
}
