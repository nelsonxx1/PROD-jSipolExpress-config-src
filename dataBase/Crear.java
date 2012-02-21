package dataBase;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import modelo.Dominios;
import modelo.HibernateUtil;
import modelo.entidades.CalendarioBancario;
import modelo.entidades.Estado;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.Usuario;
import modelo.entidades.defaultData.DefaultData;
import modelo.entidades.personas.dominio.TipoActividadEconomica;
import modelo.entidades.personas.dominio.TipoCapacidadEconomica;
import modelo.entidades.personas.dominio.TipoCodigoArea;
import modelo.entidades.personas.dominio.TipoCuentaBancaria;
import modelo.entidades.personas.dominio.TipoDireccion;
import modelo.entidades.personas.dominio.TipoPersona;
import modelo.entidades.personas.dominio.TipoTelefono2;
import modelo.entidades.personas.maestra.Persona;
import modelo.entidades.personas.maestra.PersonaJuridica;
import modelo.entidades.personas.maestra.PersonaNatural;
import modelo.entidades.personas.maestra.Rif;
import modelo.entidades.polizas.dominio.GrupoPoliza;
import modelo.entidades.polizas.dominio.RamoPoliza;
import modelo.entidades.siniestros.dominio.CausaSiniestro;
import modelo.entidades.siniestros.dominio.DetalleCausaSiniestro;
import modelo.entidades.vehiculos.dominio.ClasificacionVehiculo;
import modelo.entidades.vehiculos.dominio.MarcaVehiculo;
import modelo.entidades.vehiculos.dominio.ModeloVehiculo;
import modelo.entidades.vehiculos.dominio.TipoColor;
import modelo.entidades.vehiculos.maestra.Vehiculo;
import modelo.entidades.TipoDocumento;
import modelo.entidades.ValoresEstandares;
import modelo.entidades.caja.dominio.TipoDocumentoPago;
import modelo.entidades.caja.dominio.TipoIngresoCaja;
import modelo.entidades.caja.dominio.TipoMovimiento;
import modelo.entidades.caja.dominio.TipoTarjetaCredito;
import modelo.entidades.cuentasPorPagar.dominio.TipoEnriquecimiento;
import modelo.entidades.cuentasPorPagar.dominio.TipoEnriquecimientoDetalle;
import modelo.entidades.facturacion.DetalleLiquidacion;
import modelo.entidades.ordenes.dominio.TipoOrden;
import modelo.entidades.personas.maestra.LNPersonaNatural;
import modelo.entidades.polizas.recibos.dominio.TipoZona;
import modelo.entidades.sms.SMSPreEscrito;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 * @author Orlando Becerra
 * @author Enrique Becerra
 */
public class Crear {

    protected Session s;
    protected Usuario userDD;
    protected Transaction tx;
    protected DefaultData defaultData;
    protected TipoPersona tpCompania;
    protected TipoPersona tpFinanciadora;
    protected TipoPersona tpBanco;
    protected TipoPersona tpProveedor;
    protected TipoPersona tpClinica;
    protected TipoPersona tpTaxi;
    protected TipoPersona tpTaller;
    protected TipoPersona tpAsegurado;
    protected TipoPersona tpContratante;
    protected TipoPersona tpProductor;
    protected TipoPersona tpCobrador;
    protected AuditoriaBasica auditoriaActivo;
    protected AuditoriaBasica auditoriaInactivo;

    public Crear() {
    }

    public void open() {
        s = HibernateUtil.getAnnotationConfiguration().
                //setProperty("hibernate.show_sql", "true").
                //setProperty("hibernate.format_sql", "true").
                setProperty("hibernate.hbm2ddl.auto", "create-drop").
                buildSessionFactory().openSession();
        tx = s.beginTransaction();
    }

//    public void open2() {
//        s = HibernateUtil.getSessionFactory().openSession();
//        tx = s.beginTransaction();
//    }
    public void close() {
        tx.commit();
        s.close();
    }

    public void create() {
        open();
        defaultData = new DefaultData();

        datosPorDefecto();
        //open2();
        s.save(defaultData.persona);
        s.save(defaultData.vehiculo);
        s.save(defaultData.poliza);
        s.save(defaultData.recibo);
        close();
//        s.save(defaultData.siniestro);
    }

    private void datosPorDefecto() {
        // open2();
        userDD = new Usuario("defaultdata", "defaultdata", new byte[]{}, null, false, false, false);
        s.save(userDD);
        Date d = new Date();
        auditoriaActivo = new AuditoriaBasica(d, userDD.getUserName(), true);
        auditoriaInactivo = new AuditoriaBasica(d, userDD.getUserName(), false);
        tiposSMSPreEscrito(auditoriaActivo);
        tiposPersona(auditoriaActivo, auditoriaInactivo);
        tiposActividadEconomica(auditoriaActivo);
        tipoCuentaBancaria(auditoriaActivo);
        tiposDocumento(auditoriaActivo);
        tiposDireccion(auditoriaActivo);
        tiposTelefono(auditoriaActivo);
        tiposCodigoArea(auditoriaActivo);
        tiposCapacidadEconomica(auditoriaActivo);
        marcaModeloVehiculo(auditoriaActivo);
        clasificacionVehiculo(auditoriaActivo);
        colores(auditoriaActivo);
        estados(auditoriaActivo);
        personasDefault(auditoriaActivo);
        tiposRamoPoliza(auditoriaActivo);
        tiposGrupoPoliza(auditoriaActivo);
        vehiculoDefault(auditoriaInactivo);
        causasSiniestro(auditoriaActivo);
        //tipoIngresoCaja(auditoriaActivo);
        tipoDocumentosPago(auditoriaActivo);
        //tipoTarjetaCredito(auditoriaActivo);
        //tipoOrdenDePago(auditoriaActivo);
        //tipoEnriquecimiento(auditoriaActivo);
        tipoZona(auditoriaActivo);
        valoresEstandares(auditoriaActivo);
        calendarioBancario(auditoriaActivo);
        detalleLiquidacion(auditoriaActivo);

        //close();
        //open2();
        //new ActualizarReportes(s, tx).reportes(auditoriaActivo);
        //close();
    }

    private void tipoCuentaBancaria(AuditoriaBasica a) {
        ArrayList<TipoCuentaBancaria> list = new ArrayList<TipoCuentaBancaria>(0);
        list.add(new TipoCuentaBancaria("AHORRO", a));
        list.add(new TipoCuentaBancaria("CORRIENTE", a));
        list.add(new TipoCuentaBancaria("TARJETA DE CREDITO", a));
        list.add(new TipoCuentaBancaria("TARJETA DE DEBITO", a));
        for (TipoCuentaBancaria o : list) {
            s.save(o);
        }
    }

    private void tiposDocumento(AuditoriaBasica a) {
        ArrayList<TipoDocumento> list = new ArrayList<TipoDocumento>(0);
        list.add(new TipoDocumento("CEDULA DE IDENTIDAD", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("CERTIFICADO MEDICO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("LICENCIA DE CONDUCIR", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("PARTIDA DE NACIMIENTO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("FOTO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("FOTO DEL VEHICULO", Dominios.Modulos.VEHICULOS, a));
        list.add(new TipoDocumento("TITULO DE PROPIEDAD", Dominios.Modulos.VEHICULOS, a));
        list.add(new TipoDocumento("TRIMESTRES", Dominios.Modulos.VEHICULOS, a));
        list.add(new TipoDocumento("CUADRO RECIBO", Dominios.Modulos.RECIBOS, a));
        list.add(new TipoDocumento("ANEXO", Dominios.Modulos.RECIBOS, a));
        list.add(new TipoDocumento("DEVOLUCION", Dominios.Modulos.RECIBOS, a));
        list.add(new TipoDocumento("ANEXO", Dominios.Modulos.SINIESTROS, a));
        list.add(new TipoDocumento("FOTO VEHICULO", Dominios.Modulos.SINIESTROS, a));
        list.add(new TipoDocumento("CONTRATO", Dominios.Modulos.FINANCIAMIENTOS, a));
        list.add(new TipoDocumento("COMPROBANTE PAGO GIRO", Dominios.Modulos.FINANCIAMIENTOS, a));
        list.add(new TipoDocumento("COMPROBANTE PAGO INICIAL", Dominios.Modulos.FINANCIAMIENTOS, a));
        list.add(new TipoDocumento("ANEXO", Dominios.Modulos.COMUNICADOS, a));
        for (TipoDocumento o : list) {
            s.save(o);
        }
    }

    private void tiposSMSPreEscrito(AuditoriaBasica a) {
        ArrayList<SMSPreEscrito> list = new ArrayList<SMSPreEscrito>(0);
        list.add(new SMSPreEscrito("GIRO PENDIENTE", "Buen dia :nombre, EMPRESA le recuerda que su proximo giro vence el dia :fecha, por el monto de Bs :monto.", a));
        list.add(new SMSPreEscrito("GIRO VENCIDO", "Buen dia :nombre, EMPRESA le recuerda que ud tiene un giro vencido del dia :fecha, por el monto de :monto.", a));
        list.add(new SMSPreEscrito("RENOVACION", "Buen dia :nombre, EMPRESA le recuerda que su poliza vence el día :fecha, por favor comunicarse lo mas pronto posible. TELF", a));
        list.add(new SMSPreEscrito("BIENVENIDA", ":nombre, reciba una cordial bienvenida a nuestra organizacion en nombre de EMPRESA. Queremos que se sienta Seguro. Feliz dia", a));
        list.add(new SMSPreEscrito("CUMPLEAÑOS", "Feliz Cumpleaños :nombre, le desea EMPRESA en este día tan especial.", a));
        list.add(new SMSPreEscrito("DOC. ANEXO POR VENCER", "Buen día :nombre, EMPRESA le recuerda que su :tipoDocumento vence el dia :fecha. Por favor, tramitelo con antelacion.", a));
        for (SMSPreEscrito o : list) {
            s.save(o);
        }
    }

    private void tiposCapacidadEconomica(AuditoriaBasica a) {
        ArrayList<TipoCapacidadEconomica> list = new ArrayList<TipoCapacidadEconomica>(0);
        TipoCapacidadEconomica cap = new TipoCapacidadEconomica("Desconocido", a);
        list.add(cap);
        list.add(new TipoCapacidadEconomica("HASTA 5 Mil Bs", a));
        list.add(new TipoCapacidadEconomica("DESDE 10 Mil HASTA 20 MIL Bs", a));
        list.add(new TipoCapacidadEconomica("DESDE 20 Mil HASTA 30 MIL Bs", a));
        list.add(new TipoCapacidadEconomica("MAS DE 30 MIL Bs", a));
        for (TipoCapacidadEconomica o : list) {
            s.save(o);
        }
        defaultData.persona.setCapacidadEconomica(cap);
    }

    private void tiposDireccion(AuditoriaBasica a) {
        ArrayList<TipoDireccion> list = new ArrayList<TipoDireccion>(0);
        TipoDireccion dir = new TipoDireccion("COBRO", a);
        list.add(dir);
        list.add(new TipoDireccion("PPRINCIPAL", a));
        list.add(new TipoDireccion("HABITACION", a));
        list.add(new TipoDireccion("TRABAJO", a));
        list.add(new TipoDireccion("SUCURSAL", a));
        for (TipoDireccion o : list) {
            s.save(o);
        }
        defaultData.persona.setDireccion(dir);
    }

    private void tiposTelefono(AuditoriaBasica a) {
        ArrayList<TipoTelefono2> list = new ArrayList<TipoTelefono2>(0);
        TipoTelefono2 tel = new TipoTelefono2("COBRO", a);
        list.add(tel);
        list.add(new TipoTelefono2("PRINCIPAL", a));
        list.add(new TipoTelefono2("HABITACION", a));
        list.add(new TipoTelefono2("TRABAJO", a));
        list.add(new TipoTelefono2("SUCURSAL", a));
        list.add(new TipoTelefono2("MOVIL", a));
        for (TipoTelefono2 o : list) {
            s.save(o);
        }
        defaultData.persona.setTelefono(tel);
    }

    private void tiposGrupoPoliza(AuditoriaBasica a) {
        ArrayList<GrupoPoliza> list = new ArrayList<GrupoPoliza>(0);
        GrupoPoliza g = new GrupoPoliza("NINGUNO", a);
        list.add(g);
        for (GrupoPoliza o : list) {
            s.save(o);
        }
        defaultData.poliza.setGrupoPoliza(g);
    }

    private void tiposRamoPoliza(AuditoriaBasica a) {
        ArrayList<RamoPoliza> list = new ArrayList<RamoPoliza>(0);
        list.add(new RamoPoliza("AUTOMOVIL CASCO", "CASCO", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.VEHICULO, a));
        list.add(new RamoPoliza("AUTOMOVIL CASCO COLECTIVO", "CASCO COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.VEHICULO, a));
        list.add(new RamoPoliza("RESPONSABILIDAD CIVIL VEHICULAR", "RCV", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.VEHICULO, a));
        list.add(new RamoPoliza("RESPONSABILIDAD CIVIL VEHICULAR COLECTIVO", "RCV COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.VEHICULO, a));
        //    list.add(new RamoPoliza("APOV Colectivo", "APOV Col.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("HOSPITALIZACION CIRUJIA Y MATERNIDAD INDIVIDUAL", "HCM IND.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("HOSPITALIZACION CIRUJIA Y MATERNIDAD COLECTIVO", "HCM COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("HOSPITALIZACION CIRUJIA Y MATERNIDAD INTERNACIONAL", "HCM INT.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("ACCIDENTES PERSONALES INDIVIDUAL", "AP IND.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("ACCIDENTES PERSONALES COLECTIVO", "AP COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("ACCIDENTES PERSONALES ESCOLARES", "AP ESC.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("VIDA INDIVIDUAL", "VIDA IND.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("VIDA COLECTIVO", "VIDA COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("GASTOS FUNERARIOS INDIVIDUAL", "GTS. FUN. IND.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("GASTOS FUNERARIOS COLECTIVO", "GTS. FUN. COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("ODONTOLOGIA", "ODONTOLOGIA", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("RESPONSABILIDAD CIVIL GENERAL", "RCG", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        list.add(new RamoPoliza("INCENDIO", "INCENDIO", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        list.add(new RamoPoliza("ROBO", " ROBO", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        list.add(new RamoPoliza("TRANSPORTE TERRESTRE", "TRANS. TERR.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        list.add(new RamoPoliza("FIANZA", "FIANZA", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.FIANZA, a));
        list.add(new RamoPoliza("RESPONSABILIDAD EMPRESARIAL", "RE", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        list.add(new RamoPoliza("RESPONSABILIDAD CIVIL INTERNACIONAL", "RC INT.", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("RESPONSABILIDAD CIVIL INTERNACIONAL Colectivo", "RC INT. COL.", Dominios.TipoRamo.COLECTIVO, Dominios.RamoContable.PERSONA, a));
        list.add(new RamoPoliza("HOGAR", "HOGAR", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        list.add(new RamoPoliza("TERREMOTO", "TERREMOTO", Dominios.TipoRamo.INDIVIDUAL, Dominios.RamoContable.RAMOS_GENERALES, a));
        //list.add(new RamoPoliza("Fidelidad", "Fidelidad", 0, 0, a));
        //list.add(new RamoPoliza("Casco Embarcación", "Casco Nav", 0, 2, a));
        //list.add(new RamoPoliza("Seguro Especial", "Seg.Espec", 0, 2, a));
        //list.add(new RamoPoliza("Exceso Dorada", "Exc.Dorad", 0, 0, a));
        //list.add(new RamoPoliza("Responsabilidad Patronal", "Resp.Patr", 0, 2, a));
        //list.add(new RamoPoliza("Accidentes de Trabajo", "Acc.Traba", 0, 0, a));
        //list.add(new RamoPoliza("Enfermedades Graves Multinacional", "Enf. Grav", 0, 0, a));
        //list.add(new RamoPoliza("Integrado Vida y Accidentes Pers.", "Vida/Acci", 0, 0, a));
        //list.add(new RamoPoliza("RESP.CIVIL EN EXCESO DE RCV", "RCV.EXCES", 0, 1, a));
        //list.add(new RamoPoliza("Combinado Empresarial", "Comb/Empr", 0, 2, a));
        //list.add(new RamoPoliza("Vida Ahorro", "Vida Ah.", 0, 0, a));
        //list.add(new RamoPoliza("P.F.P.", "P.F.P.", 0, 0, a));
        //list.add(new RamoPoliza("Savi", "Savi", 0, 0, a));
        //list.add(new RamoPoliza("Combinado Residencial", "Comb. Res.", 0, 2, a));
        //list.add(new RamoPoliza("Equipo Electrónico", "Eq. Elect.", 0, 2, a));
        //list.add(new RamoPoliza("Rotura de Maquinaria", "Rot. Maquin.", 0, 2, a));
        //list.add(new RamoPoliza("Dinero y Valores", "Din./Valores", 0, 2, a));
        //list.add(new RamoPoliza("Riesgos Especiales", "Ries. Esp.", 0, 2, a));
        //list.add(new RamoPoliza("Riesgos Diversos", "Ries. Div.", 0, 2, a));
        //list.add(new RamoPoliza("Oficina", "Oficina", 0, 2, a));
        //list.add(new RamoPoliza("Negocio", "Negocio", 0, 2, a));
        //list.add(new RamoPoliza("Sofiintegral", "Sof-Integ", 0, 0, a));
        //list.add(new RamoPoliza("Combinado Protección", "Comb/Prot", 0, 0, a));
        //list.add(new RamoPoliza("Vidrios y Cristales", "Vidrios/C", 0, 2, a));
        //list.add(new RamoPoliza("Avisos Luminosos", "Avisos", 0, 2, a));
        //list.add(new RamoPoliza("Seg Industria y Comercio", "Indus/Com", 0, 2, a));
        //list.add(new RamoPoliza("Vida Dolares", "VIDA $", 0, 0, a));

//        Collections.sort(list, new Comparator() {
//
//            public int compare(Object o1, Object o2) {
//                RamoPoliza r1 = (RamoPoliza) o1;
//                RamoPoliza r2 = (RamoPoliza) o2;
//                return r1.getNombre().compareTo(r2.getNombre());
//            }
//        });

        for (RamoPoliza o : list) {
            s.save(o);
        }
    }

    private void tiposCodigoArea(AuditoriaBasica a) {
        ArrayList<TipoCodigoArea> list = new ArrayList<TipoCodigoArea>(0);
        list.add(new TipoCodigoArea("MOVISTAR 414", 414, a));
        list.add(new TipoCodigoArea("MOVISTAR 424", 424, a));
        list.add(new TipoCodigoArea("MOVILNET 416", 416, a));
        list.add(new TipoCodigoArea("MOVILNET 426", 426, a));
        list.add(new TipoCodigoArea("DIGITEL 412", 412, a));
        list.add(new TipoCodigoArea("TA S/CTBAL. 276", 276, a));
        // list.add(new TipoCodigoArea("AMAZONAS 248", 248, a));
        // list.add(new TipoCodigoArea("AMAZONAS 296", 296, a));
        // list.add(new TipoCodigoArea("ANZUATEGUI 281", 281, a));
        // list.add(new TipoCodigoArea("ANZUATEGUI 282", 282, a));
        // list.add(new TipoCodigoArea("ANZUATEGUI 283", 283, a));
        // list.add(new TipoCodigoArea("ANZUATEGUI 285", 285, a));
        // list.add(new TipoCodigoArea("ANZUATEGUI 292", 292, a));
        // list.add(new TipoCodigoArea("Apure 240", 240, a));
        // list.add(new TipoCodigoArea("Apure 247", 247, a));
        // list.add(new TipoCodigoArea("Apure 278", 278, a));
        // list.add(new TipoCodigoArea("Aragua 243", 243, a));
        // list.add(new TipoCodigoArea("Aragua 244", 244, a));
        // list.add(new TipoCodigoArea("Aragua 246", 246, a));
        // list.add(new TipoCodigoArea("Barinas 240", 240, a));
        // list.add(new TipoCodigoArea("Barinas 247, 247, a));
        // list.add(new TipoCodigoArea("Barinas 258", 258, a));
        // list.add(new TipoCodigoArea("Barinas 273", 273, a));
        // list.add(new TipoCodigoArea("Bolivar 284", 284, a));
        // list.add(new TipoCodigoArea("Bolivar 285", 285, a));
        // list.add(new TipoCodigoArea("Bolivar 286", 286, a));
        // list.add(new TipoCodigoArea("Bolivar 288", 288, a));
        // list.add(new TipoCodigoArea("Bolivar 289", 289, a));
        // list.add(new TipoCodigoArea("Carabobo 241", 241, a));
        // list.add(new TipoCodigoArea("Carabobo 242", 242, a));
        // list.add(new TipoCodigoArea("Carabobo 243", 243, a));
        // list.add(new TipoCodigoArea("Carabobo 245", 245, a));
        // list.add(new TipoCodigoArea("Carabobo 249", 249, a));
        // list.add(new TipoCodigoArea("Cojedes 258", 258, a));
        // list.add(new TipoCodigoArea("Delta Macuro 287", 287, a));
        // list.add(new TipoCodigoArea("Falcon 259", 259, a));
        // list.add(new TipoCodigoArea("Falcon 268", 268, a));
        // list.add(new TipoCodigoArea("Falcon 269", 269, a));
        // list.add(new TipoCodigoArea("Falcon 279", 279, a));
        // list.add(new TipoCodigoArea("Caracas 212", 212, a));
        // list.add(new TipoCodigoArea("Caracas 237", 237, a));
        // list.add(new TipoCodigoArea("Guarico 235", 235, a));
        // list.add(new TipoCodigoArea("Guarico 238", 238, a));
        // list.add(new TipoCodigoArea("Guarico 246", 246, a));
        // list.add(new TipoCodigoArea("Guarico 247", 247, a));
        // list.add(new TipoCodigoArea("Lara 251", 251, a));
        // list.add(new TipoCodigoArea("Lara 252", 252, a));
        // list.add(new TipoCodigoArea("Lara 253", 253, a));
        // list.add(new TipoCodigoArea("Merida 271", 271, a));
        // list.add(new TipoCodigoArea("Merida 274", 274, a));
        // list.add(new TipoCodigoArea("Merida 275", 275, a));
        // list.add(new TipoCodigoArea("Merida 277", 277, a));
        // list.add(new TipoCodigoArea("Miranda 212", 212, a));
        // list.add(new TipoCodigoArea("Miranda 234", 234, a));
        // list.add(new TipoCodigoArea("Miranda 239", 239, a));
        // list.add(new TipoCodigoArea("Monagas 291", 291, a));
        // list.add(new TipoCodigoArea("Monagas 292", 292, a));
        // list.add(new TipoCodigoArea("Monagas 286", 286, a));
        // list.add(new TipoCodigoArea("Monagas 287", 287, a));
        // list.add(new TipoCodigoArea("Nueva Esparta 295", 295, a));
        // list.add(new TipoCodigoArea("Portuguesa 255", 255, a));
        // list.add(new TipoCodigoArea("Portuguesa 256", 256, a));
        // list.add(new TipoCodigoArea("Portuguesa 257", 257, a));
        // list.add(new TipoCodigoArea("Sucre 294", 294, a));
        // list.add(new TipoCodigoArea("Sucre 293", 293, a));
        // list.add(new TipoCodigoArea("Tachira 270", 270, a));
        // list.add(new TipoCodigoArea("Tachira 275", 275, a));
        // list.add(new TipoCodigoArea("Tachira 276", 276, a));
        // list.add(new TipoCodigoArea("Tachira 277", 277, a));
        // list.add(new TipoCodigoArea("Trujillo 271", 272, a));
        // list.add(new TipoCodigoArea("Vargas 212", 212, a));
        // list.add(new TipoCodigoArea("Yaracuy 251", 251, a));
        // list.add(new TipoCodigoArea("Yaracuy 253", 253, a));
        // list.add(new TipoCodigoArea("Yaracuy 254", 254, a));
        // list.add(new TipoCodigoArea("Zulia 260", 260, a));
        // list.add(new TipoCodigoArea("Zulia 261", 261, a));
        // list.add(new TipoCodigoArea("Zulia 262", 262, a));
        // list.add(new TipoCodigoArea("Zulia 263", 263, a));
        // list.add(new TipoCodigoArea("Zulia 264", 264, a));
        // list.add(new TipoCodigoArea("Zulia 265", 265, a));
        // list.add(new TipoCodigoArea("Zulia 266", 266, a));
        // list.add(new TipoCodigoArea("Zulia 267", 267, a));
        // list.add(new TipoCodigoArea("Zulia 271", 271, a));
        // list.add(new TipoCodigoArea("Zulia 275", 275, a));

        for (TipoCodigoArea o : list) {
            s.save(o);
        }
    }

    private void tiposActividadEconomica(AuditoriaBasica a) {
        ArrayList<TipoActividadEconomica> list = new ArrayList<TipoActividadEconomica>(0);
        TipoActividadEconomica act = new TipoActividadEconomica("Desconocido", a);
        list.add(act);
        list.add(new TipoActividadEconomica("CONCEJO COMUNAL", a));
        list.add(new TipoActividadEconomica("AMA DE CASA", a));
        list.add(new TipoActividadEconomica("AGRICULTURA", a));
        list.add(new TipoActividadEconomica("AUTONOMO/PROPIETARIO", a));
        list.add(new TipoActividadEconomica("COMERCIANTE/ARTESANO", a));
        list.add(new TipoActividadEconomica("CONSULTOR/SERV.PROFESIONALES", a));
        list.add(new TipoActividadEconomica("CONTABILIDAD/FINANZAS", a));
        list.add(new TipoActividadEconomica("DIRECCION EJECUTIVA", a));
        list.add(new TipoActividadEconomica("ESTUDIANTE", a));
        list.add(new TipoActividadEconomica("VENTAS/MERCADOTECNIA/PUBLICIDA", a));
        list.add(new TipoActividadEconomica("GOBIERNO/EJERCITO", a));
        list.add(new TipoActividadEconomica("INFORMATICA", a));
        list.add(new TipoActividadEconomica("PESCA", a));
        list.add(new TipoActividadEconomica("SIN EMPLEO/INACTIVIDAD TEMPORAL", a));
        list.add(new TipoActividadEconomica("TRANSPORTE", a));
        list.add(new TipoActividadEconomica("TURISMO", a));
        list.add(new TipoActividadEconomica("MINERIA", a));
        list.add(new TipoActividadEconomica("INDUSTRIA", a));
        list.add(new TipoActividadEconomica("PESCA", a));
        for (TipoActividadEconomica o : list) {
            s.save(o);
        }
        defaultData.persona.setActividadEconomica(act);
    }

    private void tiposPersona(AuditoriaBasica a, AuditoriaBasica a2) {
        ArrayList<TipoPersona> list = new ArrayList<TipoPersona>(0);
        tpAsegurado = new TipoPersona("ASE", "ASEGURADO", true, a);
        list.add(tpAsegurado);
        tpContratante = new TipoPersona("CON", "CONTRATANTE", true, a);
        list.add(tpContratante);
        tpProductor = new TipoPersona("PRD", "PRODUCTOR", true, a);
        list.add(tpProductor);
        tpCobrador = new TipoPersona("COB", "COBRADOR", true, a);
        list.add(tpCobrador);
        list.add(new TipoPersona("IVA", "CONTRIBUYENTE", false, a2));
        list.add(new TipoPersona("RAZ", "RAZON SOCIAL", false, a2));
        list.add(new TipoPersona("EJE", "EJECUTIVO", true, a));
        tpCompania = new TipoPersona("SEG", "COMPAÑIA DE SEGUROS", true, a);
        list.add(tpCompania);
        tpFinanciadora = new TipoPersona("FIN", "FINANCIADORA", true, a);
        list.add(tpFinanciadora);
        tpBanco = new TipoPersona("BAN", "BANCO", true, a);
        list.add(tpBanco);
        list.add(new TipoPersona("BEN", "BENEFICIARIO", true, a));
        tpTaller = new TipoPersona("TAL", "TALLER", false, a);
        list.add(tpTaller);
        list.add(new TipoPersona("TER", "TERCERO", false, a2));
        list.add(new TipoPersona("EMP", "EMPLEADO", false, a2));
        list.add(new TipoPersona("ABG", "ABOGADO", false, a2));
        list.add(new TipoPersona("MED", "MEDICO", false, a2));
        tpProveedor = new TipoPersona("PRV", "PROVEEDOR", true, a);
        list.add(tpProveedor);
        tpClinica = new TipoPersona("CLI", "CLINICA", false, a2);
        list.add(tpClinica);
        list.add(new TipoPersona("FIA", "FIADOR", false, a2));
        list.add(new TipoPersona("PAG", "PAGADOR", true, a));
        list.add(new TipoPersona("FAR", "FARMACIA", false, a2));
        tpTaxi = new TipoPersona("TXA", "TAXIS", false, a2);
        list.add(tpTaxi);
        list.add(new TipoPersona("TCL", "TRANSPORTE COLECTIVO", false, a2));
        list.add(new TipoPersona("RES", "RESTAURANTE", false, a2));
        list.add(new TipoPersona("PRO", "EMPRESA", true, a));
        for (TipoPersona o : list) {
            s.save(o);
        }
    }

    private void clasificacionVehiculo(AuditoriaBasica a) {
        ArrayList<ClasificacionVehiculo> list = new ArrayList<ClasificacionVehiculo>(0);
        ClasificacionVehiculo clasificacionVehiculo = new ClasificacionVehiculo("Desconocido", a);
        list.add(clasificacionVehiculo);
        list.add(new ClasificacionVehiculo("PARTICULAR", a));
        list.add(new ClasificacionVehiculo("PICK UP - RUSTICO", a));
        list.add(new ClasificacionVehiculo("CAMION 350", a));
        list.add(new ClasificacionVehiculo("CAMION 750", a));
        list.add(new ClasificacionVehiculo("CAMION 8 A 12 TONELADAS", a));
        list.add(new ClasificacionVehiculo("CAMION +12 TONELADAS", a));
        list.add(new ClasificacionVehiculo("TAXI TODO TIPO", a));
        for (ClasificacionVehiculo o : list) {
            s.save(o);
        }
        defaultData.vehiculo.setClasificacionVehiculo(clasificacionVehiculo);
    }

    private void estados(AuditoriaBasica a) {
        ArrayList<Estado> list = new ArrayList<Estado>(0);
        list.add(new Estado("TACHIRA", a));
        list.add(new Estado("DISTRITO FEDERAL", a));
        list.add(new Estado("ANZUATEGUI", a));
        list.add(new Estado("APURE", a));
        list.add(new Estado("ARAGUA", a));
        list.add(new Estado("BARINAS", a));
        list.add(new Estado("BOLIVAR", a));
        list.add(new Estado("CARABOBO", a));
        list.add(new Estado("COJEDES", a));
        list.add(new Estado("FALCON", a));
        list.add(new Estado("GUARICO", a));
        list.add(new Estado("LARA", a));
        list.add(new Estado("MERIDA", a));
        list.add(new Estado("MIRANDA", a));
        list.add(new Estado("MONAGAS", a));
        list.add(new Estado("NUEVA ESPARTA", a));
        list.add(new Estado("PORTUGUEZA", a));
        list.add(new Estado("SUCRE", a));
        list.add(new Estado("TRUJILLO", a));
        list.add(new Estado("YARACUY", a));
        list.add(new Estado("ZULIA", a));
        list.add(new Estado("MONAGAS", a));
        list.add(new Estado("VARGAS", a));
        list.add(new Estado("ANTILLAS", a));
        list.add(new Estado("TERRITORIO DEL TAMACURO", a));
        list.add(new Estado("OTRO PAIS", a));
        for (Estado o : list) {
            s.save(o);
        }
    }

    private void dataPersona(Persona p, AuditoriaBasica a) {
        p.setAuditoria(a);
        p.setCapacidadEconomica(defaultData.persona.getCapacidadEconomica());
        p.setActividadEconomica(defaultData.persona.getActividadEconomica());
        p.setTipoContribuyente(Dominios.TipoContribuyente.DESCONOCIDO);
        p.setRanking(Dominios.Ranking.B);
    }

    protected void dataPersonaNatural(PersonaNatural pn, AuditoriaBasica a) {
        pn.setTipoNombre(Dominios.TipoNombre.DESCONOCIDO);
        pn.setSexo(Dominios.Sexo.DESCONOCIDO);
        pn.setEstadoCivil(Dominios.EstadoCivil.DESCONOCIDO);
        LNPersonaNatural.generarNombres(pn);
        dataPersona(pn, a);
    }

    private void personasDefault(AuditoriaBasica a) {

        //Persona Natural

        //*************************
        //Persona Juridica Bancos
        PersonaJuridica bcomercantil = new PersonaJuridica();
        bcomercantil.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2961, 0));
        bcomercantil.setNombreLargo("MERCANTIL, C.A. BANCO UNIVERSAL");
        bcomercantil.setNombreCorto("MERCANTIL");
        bcomercantil.setWeb("http://http://www.bancomercantil.com");
        bcomercantil.getTiposPersona().add(tpBanco);
        dataPersona(bcomercantil, a);
        s.save(bcomercantil);

        PersonaJuridica bcobicentenario = new PersonaJuridica();
        bcobicentenario.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20009148, 7));
        bcobicentenario.setNombreLargo("BANCO BICENTENARIO, C.A. BANCO UNIVERSAL");
        bcobicentenario.setNombreCorto("BICENTENARIO");
        bcobicentenario.setWeb("http://www.bicentenariobu.com/");
        bcobicentenario.getTiposPersona().add(tpBanco);
        dataPersona(bcobicentenario, a);
        s.save(bcobicentenario);

        PersonaJuridica bcovenezuela = new PersonaJuridica();
        bcovenezuela.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2948, 2));
        bcovenezuela.setNombreLargo("BANCO DE VENEZUELA, S.A BANCO UNIVERSAL");
        bcovenezuela.setNombreCorto("VENEZUELA");
        bcovenezuela.setWeb("http://www.bancodevenezuela.com/");
        bcovenezuela.getTiposPersona().add(tpBanco);
        dataPersona(bcovenezuela, a);
        s.save(bcovenezuela);

        PersonaJuridica bcotesoro = new PersonaJuridica();
        bcotesoro.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20005187, 6));
        bcotesoro.setNombreLargo("BANCO DEL TESORO, C.A. BANCO UNIVERSA");
        bcotesoro.setNombreCorto("TESORO");
        bcotesoro.setWeb("http://www.bt.gob.ve/");
        bcotesoro.getTiposPersona().add(tpBanco);
        dataPersona(bcotesoro, a);
        s.save(bcotesoro);

        PersonaJuridica bcobanesco = new PersonaJuridica();
        bcobanesco.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7013380, 5));
        bcobanesco.setNombreLargo("BANESCO BANCO UNIVERSAL, C.A.");
        bcobanesco.setNombreCorto("BANESCO");
        bcobanesco.setWeb("http://www.banesco.com/");
        bcobanesco.getTiposPersona().add(tpBanco);
        dataPersona(bcobanesco, a);
        s.save(bcobanesco);

        PersonaJuridica bcosofitasa = new PersonaJuridica();
        bcosofitasa.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 9028384, 6));
        bcosofitasa.setNombreLargo("BANCO SOFITASA BANCO UNIVERSAL, C. A.");
        bcosofitasa.setNombreCorto("SOFITASA");
        bcosofitasa.setWeb("http://www.sofitasa.com/index.asp");
        bcosofitasa.getTiposPersona().add(tpBanco);
        dataPersona(bcosofitasa, a);
        s.save(bcosofitasa);

        PersonaJuridica bcobod = new PersonaJuridica();
        bcobod.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30061946, 0));
        bcobod.setNombreLargo("BANCO OCCIDENTAL DE DESCUENTO, BANCO UNIVERSAL, C.A. ");
        bcobod.setNombreCorto("BOD");
        bcobod.setWeb("http://www.bodinternet.com/Red_de_Oficinas.asp");
        bcobod.getTiposPersona().add(tpBanco);
        dataPersona(bcobod, a);
        s.save(bcobod);

        PersonaJuridica bcoexterior = new PersonaJuridica();
        bcoexterior.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2950, 4));
        bcoexterior.setNombreLargo("BANCO EXTERIOR,C.A, BANCO UNIVERSAL");
        bcoexterior.setNombreCorto("EXTERIOR");
        bcoexterior.setWeb("http://www.bancoexterior.com/");
        bcoexterior.getTiposPersona().add(tpBanco);
        dataPersona(bcoexterior, a);
        s.save(bcoexterior);

        PersonaJuridica bcocoro = new PersonaJuridica();
        bcocoro.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7000173, 9));
        bcocoro.setNombreLargo("BANCORO C.A. BANCO UNIVERSAL REGIONAL");
        bcocoro.setNombreCorto("BANCORO");
        bcocoro.setWeb("http://www.bancoro.com/");
        bcocoro.getTiposPersona().add(tpBanco);
        dataPersona(bcocoro, a);
        s.save(bcocoro);

        PersonaJuridica bcobfc = new PersonaJuridica();
        bcobfc.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30778189, 0));
        bcobfc.setNombreLargo("BFC BANCO FONDO COMUN, C.A. BANCO UNIVERSAL");
        bcobfc.setNombreCorto("BFC");
        bcobfc.setWeb("http://www.bfc.com.ve/");
        bcobfc.getTiposPersona().add(tpBanco);
        dataPersona(bcobfc, a);
        s.save(bcobfc);

        PersonaJuridica bcofederal = new PersonaJuridica();
        bcofederal.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 8511576, 5));
        bcofederal.setNombreLargo("BANCO FEDERAL, C.A.");
        bcofederal.setNombreCorto("FEDERAL");
        bcofederal.setWeb("http://www.bancofederal.com/");
        bcofederal.getTiposPersona().add(tpBanco);
        dataPersona(bcofederal, a);
        s.save(bcofederal);

        PersonaJuridica bcoprovincial = new PersonaJuridica();
        bcoprovincial.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2967, 9));
        bcoprovincial.setNombreLargo("BANCO PROVINCIAL, S.A. BANCO UNIVERSAL");
        bcoprovincial.setNombreCorto("PROVINCIAL");
        bcoprovincial.setWeb("https://www.provincial.com/");
        bcoprovincial.getTiposPersona().add(tpBanco);
        dataPersona(bcoprovincial, a);
        s.save(bcoprovincial);

        PersonaJuridica bcoguayana = new PersonaJuridica();
        bcoguayana.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2941, 5));
        bcoguayana.setNombreLargo("BANCO GUAYANA C.A.");
        bcoguayana.setNombreCorto("GUAYANA");
        bcoguayana.setWeb("http://www.bancoguayana.net/");
        bcoguayana.getTiposPersona().add(tpBanco);
        dataPersona(bcoguayana, a);
        s.save(bcoguayana);

        PersonaJuridica bcoiv = new PersonaJuridica();
        bcoiv.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2957, 1));
        bcoiv.setNombreLargo("BANCO INDUSTRIAL DE VENEZUELA C A");
        bcoiv.setNombreCorto("INDUSTRIAL");
        bcoiv.setWeb("http://www.biv.com.ve//");
        bcoiv.getTiposPersona().add(tpBanco);
        dataPersona(bcoiv, a);
        s.save(bcoiv);

        //*************************
        //Persona Juridica Prestacion de servicios
        PersonaJuridica servmovilnet = new PersonaJuridica();
        servmovilnet.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30000493, 7));
        servmovilnet.setNombreLargo("TELECOMUNICACIONES MOVILNET, C.A.");
        servmovilnet.setNombreCorto("MOVILNET");
        servmovilnet.setWeb("http://www.movilnet.com.ve/sitio/");
        servmovilnet.getTiposPersona().add(tpProveedor);
        dataPersona(servmovilnet, a);
        s.save(servmovilnet);

        PersonaJuridica servcantv = new PersonaJuridica();
        servcantv.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30186298, 8));
        servcantv.setNombreLargo("CANTV.NET, C.A.");
        servcantv.setNombreCorto("CANTV");
        servcantv.setWeb("http://www.cantv.net/");
        servcantv.getTiposPersona().add(tpProveedor);
        dataPersona(servcantv, a);
        s.save(servcantv);

        PersonaJuridica servmovistar = new PersonaJuridica();
        servmovistar.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 343994, 0));
        servmovistar.setNombreLargo("TELCEL, C.A.");
        servmovistar.setNombreCorto("MOVISTAR");
        servmovistar.setWeb("http://www.movistar.com.ve/");
        servmovistar.getTiposPersona().add(tpProveedor);
        dataPersona(servmovistar, a);
        s.save(servmovistar);

        PersonaJuridica servdigitel = new PersonaJuridica();
        servdigitel.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30468971, 3));
        servdigitel.setNombreLargo("CORPORACION DIGITEL, C.A. ");
        servdigitel.setNombreCorto("DIGITEL");
        servdigitel.setWeb("http://www.digitel.com.ve/");
        servdigitel.getTiposPersona().add(tpProveedor);
        dataPersona(servdigitel, a);
        s.save(servdigitel);

        // pendiente revisar rif seniat
        PersonaJuridica servcadafe = new PersonaJuridica();
        servcadafe.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 4366, 3));
        servcadafe.setNombreLargo("CADAFE");
        servcadafe.setNombreCorto("CADAFE");
        servcadafe.setWeb("http://www.google.com");
        servcadafe.getTiposPersona().add(tpProveedor);
        dataPersona(servcadafe, a);
        s.save(servcadafe);

        // pendiente revisar rif seniat
        PersonaJuridica servhso = new PersonaJuridica();
        servhso.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20008085, 0));
        servhso.setNombreLargo("HIDROSUROESTE");
        servhso.setNombreCorto("HIDROSUROESTE");
        servhso.setWeb("http://www.google.com");
        servhso.getTiposPersona().add(tpProveedor);
        dataPersona(servhso, a);
        s.save(servhso);

        PersonaJuridica servseniat = new PersonaJuridica();
        servseniat.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20000303, 0));
        servseniat.setNombreLargo("SERVICIO NACIONAL INTEGRADO DE ADMINISTRACION ADUANERA Y TRIBUTARIA");
        servseniat.setNombreCorto("SENIAT");
        servseniat.setWeb("http://www.seniat.gob.ve/");
        servseniat.getTiposPersona().add(tpProveedor);
        dataPersona(servseniat, a);
        s.save(servseniat);

        PersonaJuridica servdirectv = new PersonaJuridica();
        servdirectv.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30259700, 5));
        servdirectv.setNombreLargo("GALAXY ENTERTAINMENT DE VENEZUELA, C.A.");
        servdirectv.setNombreCorto("DIRECTV");
        servdirectv.setWeb("http://www.directv.com.ve/");
        servdirectv.getTiposPersona().add(tpProveedor);
        dataPersona(servdirectv, a);
        s.save(servdirectv);

        PersonaJuridica servnetuno = new PersonaJuridica();
        servnetuno.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30108335, 0));
        servnetuno.setNombreLargo("NETUNO, C.A");
        servnetuno.setNombreCorto("NETUNO");
        servnetuno.setWeb("https://www.netuno.net/");
        servnetuno.getTiposPersona().add(tpProveedor);
        dataPersona(servnetuno, a);
        s.save(servnetuno);

        //*************************
        //Persona Juridica Lineas Aereas  ASERCA AIRLINES, C.A.
        PersonaJuridica laserca = new PersonaJuridica();
        laserca.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7503559, 3));
        laserca.setNombreLargo("ASERCA AIRLINES, C.A.");
        laserca.setNombreCorto("ASERCA");
        laserca.setWeb("http://www.asercaairlines.com/");
        laserca.getTiposPersona().add(tpProveedor);
        dataPersona(laserca, a);
        s.save(laserca);

        PersonaJuridica lconviasa = new PersonaJuridica();
        lconviasa.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20007774, 3));
        lconviasa.setNombreLargo("CONSORCIO VENEZOLANO DE INDUSTRIAS AERONAUTICAS Y SERVICIOS AEREOS, S.A.");
        lconviasa.setNombreCorto("CONVIASA");
        lconviasa.setWeb("http://www.conviasa.aero/");
        lconviasa.getTiposPersona().add(tpProveedor);
        dataPersona(lconviasa, a);
        s.save(lconviasa);

//   // pendiente revisar rif seniat
//        PersonaJuridica lrutaca = new PersonaJuridica();
//        lrutaca.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 88));
//        lrutaca.setNombreLargo("RUTACA");
//        lrutaca.setNombreCorto("RUTACA");
//        lrutaca.setWeb("http://www.rutaca.com.ve/");
//        lrutaca.getTiposPersona().add(tpProveedor);
//        dataPersona(lrutaca, a);
//        s.save(lrutaca);

        //*************************
        //Persona Juridica LINEA TRANSPORTE (EXP. LOS LLANOS - OCCIDENTE - MERIDA) ETC.

//        //*************************
//        //Persona Gobierno Seguro Social
//   // pendiente revisar rif seniat
//        PersonaJuridica gobivss = new PersonaJuridica();
//        gobivss.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 88));
//        gobivss.setNombreLargo("INSTITUTO VENEZOLANO DE LOS SEGUROS SOCIALES");
//        gobivss.setNombreCorto("IVSS");
//        gobivss.setWeb("http://www.ivss.gov.ve/");
//        gobivss.getTiposPersona().add(tpProveedor);
//        dataPersona(gobivss, a);
//        s.save(gobivss);

        //*************************
        //Persona Gobierno BANAVIV

        //*************************
        //Persona Juridica Taxis

        //*************************
        //Persona Juridica Farmacias

        //*************************
        //Persona Juridica Clinicas

        //*************************
        //Persona Juridica Talleres

        //*************************
        //Persona Juridica Encomiendas
        //
        PersonaJuridica provmrw = new PersonaJuridica();
        provmrw.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 274758, 7));
        provmrw.setNombreLargo("MENSAJEROS RADIO WORLDWIDE C.A");
        provmrw.setNombreCorto("MRW");
        provmrw.setWeb("http://www.mrw.com.ve/");
        provmrw.getTiposPersona().add(tpProveedor);
        dataPersona(provmrw, a);
        s.save(provmrw);

        PersonaJuridica provzoom = new PersonaJuridica();
        provzoom.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 102174, 4));
        provzoom.setNombreLargo("ZOOM INTERNATIONAL SERVICES C A");
        provzoom.setNombreCorto("ZOOM");
        provzoom.setWeb("http://www.grupozoom.com/");
        provzoom.getTiposPersona().add(tpProveedor);
        dataPersona(provzoom, a);
        s.save(provzoom);

        PersonaJuridica provipostel = new PersonaJuridica();
        provipostel.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 20000043, 0));
        provipostel.setNombreLargo("INSTITUTO POSTAL TELEGRAFICO DE VENEZUELA ");
        provipostel.setNombreCorto("IPOSTEL");
        provipostel.setWeb("http://www.ipostel.gov.ve/");
        provipostel.getTiposPersona().add(tpProveedor);
        dataPersona(provipostel, a);
        s.save(provipostel);

        //Persona Juridica Seguros
        PersonaJuridica caracas = new PersonaJuridica();
        caracas.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 38923, 3));
        caracas.setNombreLargo("SEGUROS CARACAS DE LIBERTY MUTUAL, C.A.");
        caracas.setNombreCorto("CARACAS");
        caracas.setWeb("http://www.seguroscaracas.com/paginas/");
        caracas.setNumeroGaceta("G.O. N° 21.269 DEL 30/11/43");
        caracas.getTiposPersona().add(tpCompania);
        dataPersona(caracas, a);
        s.save(caracas);

        PersonaJuridica previsora = new PersonaJuridica();
        previsora.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 21376, 3));
        previsora.setNombreLargo("LA PREVISORA, C. DE SEGUROS");
        previsora.setNombreCorto("LA PREVISORA");
        previsora.setWeb("http://www.previsora.com/");
        previsora.setNumeroGaceta("G.O. N° 18.989 DEL 23/03/36 GENERALES Y VIDA");
        previsora.getTiposPersona().add(tpCompania);
        dataPersona(previsora, a);
        s.save(previsora);

        PersonaJuridica seguridad = new PersonaJuridica();
        seguridad.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 21410, 7));
        seguridad.setNombreLargo("MAPFRE LA SEGURIDAD,  C. DE SEGUROS");
        seguridad.setNombreCorto("MAPFRE LA SEGURIDAD");
        seguridad.setWeb("http://www.mapfre.com.ve");
        seguridad.setNumeroGaceta("GENERALES: G.O. N°  21.143 DEL 02/07/43 Y VIDA: G.O. N°  21.918 DEL 26/01/46");
        seguridad.getTiposPersona().add(tpCompania);
        dataPersona(seguridad, a);
        s.save(seguridad);

        PersonaJuridica star = new PersonaJuridica();
        star.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7587, 5));
        star.setNombreLargo("ESTAR SEGUROS, S.A.");
        star.setNombreCorto("ESTAR SEGUROS");
        star.setWeb("http://www.royalsun.com.ve/");
        star.setNumeroGaceta("G.O. N° 22.607 DEL 05/05/48");
        star.getTiposPersona().add(tpCompania);
        dataPersona(star, a);
        s.save(star);

        PersonaJuridica carabobo = new PersonaJuridica();
        carabobo.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 34022, 6));
        carabobo.setNombreLargo("SEGUROS CARABOBO, C.A.");
        carabobo.setNombreCorto("CARABOBO");
        carabobo.setWeb("http://www.seguroscarabobo.net/portal/consultaNoticias.do");
        carabobo.setNumeroGaceta("G.O. N° 24.861 DEL 29/09/55 PROV. 4.451 DEL 29/9/55");
        carabobo.getTiposPersona().add(tpCompania);
        dataPersona(carabobo, a);
        s.save(carabobo);

        PersonaJuridica andes = new PersonaJuridica();
        andes.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7001737, 6));
        andes.setNombreLargo("SEGUROS LOS ANDES, C.A.");
        andes.setNombreCorto("LOS ANDES");
        andes.setWeb("http://www.seguroslosandes.com/");
        andes.setNumeroGaceta("PROV. 30.22 DEL 16/7/56 G.O. N° 25.103 DEL 16/7/56");
        andes.getTiposPersona().add(tpCompania);
        dataPersona(andes, a);
        s.save(andes);

        PersonaJuridica nuevomundo = new PersonaJuridica();
        nuevomundo.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 26840, 1));
        nuevomundo.setNombreLargo("SEGUROS NUEVO MUNDO S.A.");
        nuevomundo.setNombreCorto("NUEVO MUNDO");
        nuevomundo.setWeb("http://www.nuevomundo.com.ve/");
        nuevomundo.setNumeroGaceta("G.O. N° 25.208 DEL 17/11/56");
        nuevomundo.getTiposPersona().add(tpCompania);
        dataPersona(nuevomundo, a);
        s.save(nuevomundo);

        PersonaJuridica laoccidental = new PersonaJuridica();
        laoccidental.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7001130, 0));
        laoccidental.setNombreLargo("LA OCCIDENTAL, C.A. DE SEGUROS");
        laoccidental.setNombreCorto("LA OCCIDENTAL");
        laoccidental.setWeb("http://www.laoccidental.com/main/index.php");
        laoccidental.setNumeroGaceta("PROV N° 3.051 DEL 19/07/57 G. O. N° 25.411 DEL 20/07/57");
        laoccidental.getTiposPersona().add(tpCompania);
        dataPersona(laoccidental, a);
        s.save(laoccidental);

        PersonaJuridica catatumbo = new PersonaJuridica();
        catatumbo.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7001736, 8));
        catatumbo.setNombreLargo("SEGUROS CATATUMBO C.A.");
        catatumbo.setNombreCorto("CATATUMBO");
        catatumbo.setWeb("http://www.catatumbo.com/");
        catatumbo.setNumeroGaceta("25.415 DEL 26/07/57");
        catatumbo.getTiposPersona().add(tpCompania);
        dataPersona(catatumbo, a);
        s.save(catatumbo);

        PersonaJuridica sgmercantil = new PersonaJuridica();
        sgmercantil.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 90180, 5));
        sgmercantil.setNombreLargo("SEGUROS MERCANTIL, C.A.");
        sgmercantil.setNombreCorto("MERCANTIL");
        sgmercantil.setWeb("http://www.segurosmercantil.com/00_home/home.htm");
        sgmercantil.setNumeroGaceta("G.O. N° 30.297 DEL 07/01/74");
        sgmercantil.getTiposPersona().add(tpCompania);
        dataPersona(sgmercantil, a);
        s.save(sgmercantil);

        PersonaJuridica segaltamira = new PersonaJuridica();
        segaltamira.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30052236, 9));
        segaltamira.setNombreLargo("SEGUROS ALTAMIRA C.A.");
        segaltamira.setNombreCorto("ALTAMIRA");
        segaltamira.setWeb("http://www.segurosaltamira.com/");
        segaltamira.setNumeroGaceta("PROV. 693 DEL 19-1-93 G.O. N° 35.138 DEL 25/01/93");
        segaltamira.getTiposPersona().add(tpCompania);
        dataPersona(segaltamira, a);
        s.save(segaltamira);

        PersonaJuridica segbanesco = new PersonaJuridica();
        segbanesco.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30083118, 3));
        segbanesco.setNombreLargo("BANESCO SEGUROS C.A.");
        segbanesco.setNombreCorto("BANESCO");
        segbanesco.setWeb("http://www.banescoseguros.com.ve/");
        segbanesco.setNumeroGaceta("G.O. N° 35.199 DEL 28/04/93");
        segbanesco.getTiposPersona().add(tpCompania);
        dataPersona(segbanesco, a);
        s.save(segbanesco);

        PersonaJuridica constiotucion = new PersonaJuridica();
        constiotucion.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 9028623, 3));
        constiotucion.setNombreLargo("SEGUROS CONSTITUCION, C.A. ");
        constiotucion.setNombreCorto("CONSTITUCION");
        constiotucion.setWeb("http://www.segurosconstitucion.com/");
        constiotucion.setNumeroGaceta("13.732");
        constiotucion.getTiposPersona().add(tpCompania);
        dataPersona(constiotucion, a);
        s.save(constiotucion);

        PersonaJuridica qualitas2 = new PersonaJuridica();
        qualitas2.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30668450, 6));
        qualitas2.setNombreLargo("SEGUROS QUALITAS C.A. ");
        qualitas2.setNombreCorto("QUALITAS");
        qualitas2.setWeb("http://www.seguros-qualitas.com/");
        qualitas2.setNumeroGaceta("");
        qualitas2.getTiposPersona().add(tpCompania);
        dataPersona(qualitas2, a);
        s.save(qualitas2);

        PersonaJuridica multinacional = new PersonaJuridica();
        multinacional.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 9013400, 0));
        multinacional.setNombreLargo("MULTINACIONAL DE SEGUROS, C.A. ");
        multinacional.setNombreCorto("MULTINACIONAL");
        multinacional.setWeb("http://www.multinacional.com.ve/");
        multinacional.setNumeroGaceta("");
        multinacional.getTiposPersona().add(tpCompania);
        dataPersona(multinacional, a);
        s.save(multinacional);

        defaultData.poliza.setCompania(caracas);

        //Persona Juridica Financiadoras
        PersonaJuridica fsegucar = new PersonaJuridica();
        fsegucar.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30306933, 9));
        fsegucar.setNombreLargo("INVERSORA SEGUCAR CA");
        fsegucar.setNombreCorto("SEGUCAR");
        fsegucar.getTiposPersona().add(tpFinanciadora);
        dataPersona(fsegucar, a);
        s.save(fsegucar);

        PersonaJuridica fmultinacional8 = new PersonaJuridica();
        fmultinacional8.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30155913, 4));
        fmultinacional8.setNombreLargo("INVERSORA MULTINACIONAL 8 CA");
        fmultinacional8.setNombreCorto("MULTINACIONAL 8");
        fmultinacional8.getTiposPersona().add(tpFinanciadora);
        dataPersona(fmultinacional8, a);
        s.save(fmultinacional8);

        PersonaJuridica fseguridad = new PersonaJuridica();
        fseguridad.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 62684, 7));
        fseguridad.setNombreLargo("INVERSORA SEGURIDAD CA");
        fseguridad.setNombreCorto("INV. SEGURIDAD");
        fseguridad.getTiposPersona().add(tpFinanciadora);
        dataPersona(fseguridad, a);
        s.save(fseguridad);

        PersonaJuridica fcatatumbo = new PersonaJuridica();
        fcatatumbo.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7006969, 4));
        fcatatumbo.setNombreLargo("INVERSORA CATATUMBO CA");
        fcatatumbo.setNombreCorto("INV. CATATUMBO");
        fcatatumbo.getTiposPersona().add(tpFinanciadora);
        dataPersona(fcatatumbo, a);
        s.save(fcatatumbo);

        PersonaJuridica faliafin = new PersonaJuridica();
        faliafin.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30330285, 8));
        faliafin.setNombreLargo("INVERSORA ALIAFIN, C.A.");
        faliafin.setNombreCorto("INV. ALIAFIN");
        faliafin.getTiposPersona().add(tpFinanciadora);
        dataPersona(faliafin, a);
        s.save(faliafin);

        PersonaJuridica fbanesco = new PersonaJuridica();
        fbanesco.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30112630, 0));
        fbanesco.setNombreLargo("BANESCO FINANCIADORA DE PRIMAS CA");
        fbanesco.setNombreCorto("INV. BANESCO");
        fbanesco.getTiposPersona().add(tpFinanciadora);
        dataPersona(fbanesco, a);
        s.save(fbanesco);

        PersonaJuridica funiver = new PersonaJuridica();
        funiver.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30819810, 2));
        funiver.setNombreLargo("INVERSIONES UNIVER C.A.");
        funiver.setNombreCorto("INV. UNIVER C.A.");
        funiver.getTiposPersona().add(tpFinanciadora);
        dataPersona(funiver, a);
        s.save(funiver);

        PersonaJuridica fpriviprima = new PersonaJuridica();
        fpriviprima.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 31413429, 9));
        fpriviprima.setNombreLargo("INVERSORA PRIVIPRIMA, C.A.");
        fpriviprima.setNombreCorto("INV. PREVIPRIMA");
        fpriviprima.getTiposPersona().add(tpFinanciadora);
        dataPersona(fpriviprima, a);
        s.save(fpriviprima);

        PersonaJuridica fsancristobal2004 = new PersonaJuridica();
        fsancristobal2004.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 31192225, 3));
        fsancristobal2004.setNombreLargo("INVERSORA SAN CRISTOBAL 2004 C.A.");
        fsancristobal2004.setNombreCorto("INV. SAN CRITOBAL 2004");
        fsancristobal2004.getTiposPersona().add(tpFinanciadora);
        dataPersona(fsancristobal2004, a);
        s.save(fsancristobal2004);

        PersonaJuridica fsegucons = new PersonaJuridica();
        fsegucons.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 31430838, 6));
        fsegucons.setNombreLargo("INVERSORA SEGUCONS C.A");
        fsegucons.setNombreCorto("INV. SEGUCONS");
        fsegucons.getTiposPersona().add(tpFinanciadora);
        dataPersona(fsegucons, a);
        s.save(fsegucons);

        PersonaJuridica finverpyme = new PersonaJuridica();
        finverpyme.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7008710, 2));
        finverpyme.setNombreLargo("INVERPYME, C.A.");
        finverpyme.setNombreCorto("INV. INVERPYME");
        finverpyme.getTiposPersona().add(tpFinanciadora);
        dataPersona(finverpyme, a);
        s.save(finverpyme);

        PersonaJuridica fmercantil = new PersonaJuridica();
        fmercantil.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 87825, 0));
        fmercantil.setNombreLargo("MERCANTIL FINANCIADORA DE PRIMAS, C.A.");
        fmercantil.setNombreCorto("INV. MERCANTIL");
        fmercantil.getTiposPersona().add(tpFinanciadora);
        dataPersona(fmercantil, a);
        s.save(fmercantil);

        PersonaJuridica fprimaban = new PersonaJuridica();
        fprimaban.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30484137, 0));
        fprimaban.setNombreLargo("C.A INVERSORA PRIMABAN");
        fprimaban.setNombreCorto("INV. PRIMABAN");
        fprimaban.getTiposPersona().add(tpFinanciadora);
        dataPersona(fprimaban, a);
        s.save(fprimaban);

        PersonaJuridica fmedicredit = new PersonaJuridica();
        fmedicredit.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 31057765, 0));
        fmedicredit.setNombreLargo("MEDICREDIT, C.A.");
        fmedicredit.setNombreCorto("INV. MEDICREDIT");
        fmedicredit.getTiposPersona().add(tpFinanciadora);
        dataPersona(fmedicredit, a);
        s.save(fmedicredit);



    }

// <editor-fold defaultstate="collapsed" desc="marcas y modelos">
    private void marcaModeloVehiculo(AuditoriaBasica a) {
        ArrayList<MarcaVehiculo> list = new ArrayList<MarcaVehiculo>(0);
        //Default
        MarcaVehiculo marcaDesconocida = new MarcaVehiculo("Desconocido", a);
        ModeloVehiculo marcaModelo = new ModeloVehiculo("Desconocido", marcaDesconocida, a);
        marcaDesconocida.getModelos().add(marcaModelo);
        list.add(marcaDesconocida);
        MarcaVehiculo CHEVROLET = new MarcaVehiculo("CHEVROLET", a);
        MarcaVehiculo RENAULT = new MarcaVehiculo("RENAULT", a);
        MarcaVehiculo TOYOTA = new MarcaVehiculo("TOYOTA", a);
        MarcaVehiculo VOLKSWAGEN = new MarcaVehiculo("VOLKSWAGEN", a);
        MarcaVehiculo FIAT = new MarcaVehiculo("FIAT", a);
        MarcaVehiculo DAEWOO = new MarcaVehiculo("DAEWOO", a);
        MarcaVehiculo DODGE = new MarcaVehiculo("DODGE", a);
        MarcaVehiculo BUICK = new MarcaVehiculo("BUICK", a);
        MarcaVehiculo FORD = new MarcaVehiculo("FORD", a);
        MarcaVehiculo LADA = new MarcaVehiculo("LADA", a);
        MarcaVehiculo HILLMAN = new MarcaVehiculo("HILLMAN", a);
        MarcaVehiculo RAMBLER = new MarcaVehiculo("RAMBLER", a);
        MarcaVehiculo ISUZU = new MarcaVehiculo("ISUZU", a);
        MarcaVehiculo MERCURY = new MarcaVehiculo("MERCURY", a);
        MarcaVehiculo MERCEDES_BENZ = new MarcaVehiculo("MERCEDES_BENZ", a);
        MarcaVehiculo CHRYSLER = new MarcaVehiculo("CHRYSLER", a);
        MarcaVehiculo NISSAN = new MarcaVehiculo("NISSAN", a);
        MarcaVehiculo FABRICACION_NACIONAL = new MarcaVehiculo("FABRICACION_NACIONAL", a);
        MarcaVehiculo JEEP = new MarcaVehiculo("JEEP", a);
        MarcaVehiculo INTERNATIONAL = new MarcaVehiculo("INTERNATIONAL", a);
        MarcaVehiculo G_M_C = new MarcaVehiculo("G.M.C", a);
        MarcaVehiculo MACK = new MarcaVehiculo("MACK", a);
        MarcaVehiculo MAZDA = new MarcaVehiculo("MAZDA", a);
        MarcaVehiculo GURI = new MarcaVehiculo("GURI", a);
        MarcaVehiculo HONDA = new MarcaVehiculo("HONDA", a);
        MarcaVehiculo HYUNDAI = new MarcaVehiculo("HYUNDAI", a);
        MarcaVehiculo KIA = new MarcaVehiculo("KIA", a);
        MarcaVehiculo YAMAHA = new MarcaVehiculo("YAMAHA", a);
        MarcaVehiculo PEGASO = new MarcaVehiculo("PEGASO", a);
        MarcaVehiculo MITSUBISHI = new MarcaVehiculo("MITSUBISHI", a);
        MarcaVehiculo SEART = new MarcaVehiculo("SEART", a);
        MarcaVehiculo SEAT = new MarcaVehiculo("SEAT", a);
        MarcaVehiculo CARIBE = new MarcaVehiculo("CARIBE", a);
        MarcaVehiculo GENERICO = new MarcaVehiculo("GENERICO", a);
        MarcaVehiculo PEUGEOT = new MarcaVehiculo("PEUGEOT", a);
        MarcaVehiculo ENCAVA = new MarcaVehiculo("ENCAVA", a);
        MarcaVehiculo DAIHATSU = new MarcaVehiculo("DAIHATSU", a);
        MarcaVehiculo FABRICACION_EXTRANJERA = new MarcaVehiculo("FABRICACION_EXTRANJERA", a);
        MarcaVehiculo PLYMOUTH = new MarcaVehiculo("PLYMOUTH", a);
        MarcaVehiculo ALL_AMERICA = new MarcaVehiculo("ALL_AMERICA", a);
        MarcaVehiculo VOLVO = new MarcaVehiculo("VOLVO", a);
        MarcaVehiculo BMW = new MarcaVehiculo("BMW", a);
        MarcaVehiculo MARCOPOLO = new MarcaVehiculo("MARCOPOLO", a);
        MarcaVehiculo ZORZI = new MarcaVehiculo("ZORZI", a);
        MarcaVehiculo IRIZAR = new MarcaVehiculo("IRIZAR", a);
        MarcaVehiculo BUSSCAR = new MarcaVehiculo("BUSSCAR", a);
        MarcaVehiculo SCANIA = new MarcaVehiculo("SCANIA", a);
        MarcaVehiculo OH1420_51 = new MarcaVehiculo("OH1420_51", a);
        MarcaVehiculo AUTOGAGO = new MarcaVehiculo("AUTOGAGO", a);
        MarcaVehiculo MONTESA = new MarcaVehiculo("MONTESA", a);
        MarcaVehiculo HARLEY = new MarcaVehiculo("HARLEY", a);
        MarcaVehiculo COMIL = new MarcaVehiculo("COMIL", a);
        MarcaVehiculo PONTIAC = new MarcaVehiculo("PONTIAC", a);
        MarcaVehiculo LML = new MarcaVehiculo("LML", a);
        MarcaVehiculo LEXUS = new MarcaVehiculo("LEXUS", a);
        MarcaVehiculo OLDSMOBILE = new MarcaVehiculo("OLDSMOBILE", a);
        MarcaVehiculo DACIA = new MarcaVehiculo("DACIA", a);
        MarcaVehiculo MANAURE = new MarcaVehiculo("MANAURE", a);
        MarcaVehiculo ROVER = new MarcaVehiculo("ROVER", a);
        MarcaVehiculo REMYVECA = new MarcaVehiculo("REMYVECA", a);
        MarcaVehiculo FREIGHTLINER = new MarcaVehiculo("FREIGHTLINER", a);
        MarcaVehiculo IVECO = new MarcaVehiculo("IVECO", a);
        MarcaVehiculo ESCAPE = new MarcaVehiculo("ESCAPE", a);
        MarcaVehiculo PINE = new MarcaVehiculo("PINE", a);
        MarcaVehiculo MONO = new MarcaVehiculo("MONO", a);
        MarcaVehiculo LAND_ROVER = new MarcaVehiculo("LAND_ROVER", a);
        MarcaVehiculo WABASH = new MarcaVehiculo("WABASH", a);
        MarcaVehiculo INDIANAPOLIS = new MarcaVehiculo("INDIANAPOLIS", a);
        MarcaVehiculo AVA = new MarcaVehiculo("AVA", a);
        MarcaVehiculo SUZUKI = new MarcaVehiculo("SUZUKI", a);
        MarcaVehiculo CHERY = new MarcaVehiculo("CHERY", a);
        MarcaVehiculo ROMBAUCA = new MarcaVehiculo("ROMBAUCA", a);
        MarcaVehiculo BERA = new MarcaVehiculo("BERA", a);
        MarcaVehiculo SUMO = new MarcaVehiculo("SUMO", a);
        MarcaVehiculo UNICO = new MarcaVehiculo("UNICO", a);
        MarcaVehiculo EMPIRE = new MarcaVehiculo("EMPIRE", a);
        MarcaVehiculo AGRALE_NEOBUS = new MarcaVehiculo("AGRALE_NEOBUS", a);
        MarcaVehiculo HYOSUNG = new MarcaVehiculo("HYOSUNG", a);
        MarcaVehiculo KEEWAY = new MarcaVehiculo("KEEWAY", a);
        MarcaVehiculo HORSE_KW_150 = new MarcaVehiculo("HORSE_KW_150", a);
        MarcaVehiculo SKYGO = new MarcaVehiculo("SKYGO", a);
        MarcaVehiculo REMOLQUE = new MarcaVehiculo("REMOLQUE", a);
        MarcaVehiculo ZXMCO = new MarcaVehiculo("ZXMCO", a);
        MarcaVehiculo ZOTYE = new MarcaVehiculo("ZOTYE", a);
        MarcaVehiculo CHANA = new MarcaVehiculo("CHANA", a);
        MarcaVehiculo JIALING = new MarcaVehiculo("JIALING", a);
        MarcaVehiculo DONG_FENG = new MarcaVehiculo("DONG_FENG", a);
        MarcaVehiculo STRICK = new MarcaVehiculo("STRICK", a);
        MarcaVehiculo JAC = new MarcaVehiculo("JAC", a);
        MarcaVehiculo SCOOTER = new MarcaVehiculo("SCOOTER", a);
        MarcaVehiculo WANGYE = new MarcaVehiculo("WANGYE", a);
        MarcaVehiculo YAMASAKI = new MarcaVehiculo("YAMASAKI", a);
        MarcaVehiculo DE_CARO = new MarcaVehiculo("DE_CARO", a);
        MarcaVehiculo QINGQI = new MarcaVehiculo("QINGQI", a);
        MarcaVehiculo YINGANG = new MarcaVehiculo("YINGANG", a);
        MarcaVehiculo TYPHOON = new MarcaVehiculo("TYPHOON", a);
        MarcaVehiculo VENIRAUTO = new MarcaVehiculo("VENIRAUTO", a);


        CHEVROLET.getModelos().add(new ModeloVehiculo("CAVALIER", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEVETTE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("KODIAK", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-10", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-30", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("MALIBU", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("BLAZER", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEVY", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-60", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CENTURY COUPE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("MONZA CLASSIC", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("SILVERADO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("ESTEEM", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEYENNE-1500", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CENTURY LIMITED", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CARIBE442", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("NPR-59L", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("SWIFT", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("GRAND BLAZER", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-350", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CAPRICE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("VITARA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("BUICK CENTURY", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("WAGON R.", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("IMPALA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CORSA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-70", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-31", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CAMARO R.S.", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("BIG-10", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("PANEL", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CABINA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("APACHE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("LUV DBL", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("LUMINA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("SUPER BRIGADIER", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-1500", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-3500", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("SUNFIRE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEYENNE C-3500", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("ASTRA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("MONZA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEVY 500", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-600", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("P-31", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("SUPER CARRY", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-31/CAHUCHA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("NPR", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-70 CHASIS LARGO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("OTRO MODELO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("FE434CIL", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("MONTE CARLO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEVY NOVA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEVELLE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEYENNE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C300", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C-70 CHASIS CORTO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("JAULA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("TRAILBLAZER ", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("STEEM", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("JIMMY", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHEVY II", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("S-10 DURANGO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("GRAND VITARA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("NOVA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("1987", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CORVETTE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("OPTRA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CONDOR III", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("AVEO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("AVALANCHE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("MONTANA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("BEL AIR", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("SPARK", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("EPICA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("TAHOE", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("FVR", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CAPTIVA", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("NKR", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("COLORADO", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CHASIS TU 59L", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("CELEBRITY", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("FSR", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("EXPRESS VAN", CHEVROLET, a));
        CHEVROLET.getModelos().add(new ModeloVehiculo("C8500", CHEVROLET, a));
        RENAULT.getModelos().add(new ModeloVehiculo("11", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("19", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("21", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("CLIO", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("TWINGO", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("R-5", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("ENERGY FREE", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("R-18BK", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("GALA", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("SYMBOL ", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("MEGANE 1.6 AUT", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("SCENIC 1.6", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("12", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("18", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("FUEGO", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("MEGANE SINC", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("MEGANE 1.6 SINC", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("MEGANE II B SINC", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("LOGAN", RENAULT, a));
        RENAULT.getModelos().add(new ModeloVehiculo("KANGOO", RENAULT, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("CELICA", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("COROLLA", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("STARLET", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("DYNA", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("SAMURAY", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("LAND CRUISER", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("YARIS", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("HILUX", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("4X4", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("CAMRY", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("4 RUNNER", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("SKY", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("4500 VX SINC", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("PRADO ", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("TECHO DURO DE LUJO", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("AUTANA", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("4500 VX AUTO", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("COASTER", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("FORTUNER", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("FJ CRUISER", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("PREVIA 2AZ A/T", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("MERU", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("HILUX DC 2 WD 2 TR A/T", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("MINI VAN", TOYOTA, a));
        TOYOTA.getModelos().add(new ModeloVehiculo("HIACE PANEL", TOYOTA, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("ESCARABAJO", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("BRASILIA", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("GOLF", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("SAVEIRO", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("PASSAT ", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("KOMBI", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("BORA 1.8 T SPORLINE", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("FOX", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("VENTO", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("CROSS FOX", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("CRAFTER", VOLKSWAGEN, a));
        VOLKSWAGEN.getModelos().add(new ModeloVehiculo("POLO", VOLKSWAGEN, a));
        FIAT.getModelos().add(new ModeloVehiculo("UNO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("PREMIO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("RITMO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("TIPO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("TUCAN", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("BRAVA", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("REGATA", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("SPAZIO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("TEMPRA", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("PALIO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("MAREA", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("SEGUN EL AÑO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("SUPER MIRAFIORI", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("SIENA 1.3", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("131 S", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("OM-150", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("330.30-HT ", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("SIENA ", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("FIORINO", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("619-N", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("131 ESPECIAL", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("FIT", FIAT, a));
        FIAT.getModelos().add(new ModeloVehiculo("IDEA", FIAT, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("CIELO", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("DAMAS VAN", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("ESPERO", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("MATIZ", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("RACER", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("LANOS", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("NUBIRA", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("TACUMA", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("TICO", DAEWOO, a));
        DAEWOO.getModelos().add(new ModeloVehiculo("SUPER SALOOM", DAEWOO, a));
        DODGE.getModelos().add(new ModeloVehiculo("DART", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("VAN 77", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("D-100", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("B-200", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("D-350", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("GRAN CARAVAN", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("T-2500", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("T-4000", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("D-300", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("RAN-350", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("CORONET", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("INTREPID", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("ASPEN", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("D-200", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("100", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("D600", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("BRISA", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("B-300", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("RAM", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("CARAVAN SE", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("CALIBER", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("DAKOTA", DODGE, a));
        DODGE.getModelos().add(new ModeloVehiculo("RANGER", DODGE, a));
        BUICK.getModelos().add(new ModeloVehiculo("SEGUN A/O", BUICK, a));
        BUICK.getModelos().add(new ModeloVehiculo("ECLIPSE G.S.", BUICK, a));
        BUICK.getModelos().add(new ModeloVehiculo("RCP MASTER", BUICK, a));
        FORD.getModelos().add(new ModeloVehiculo("F-150", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FESTIVA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-100", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("EXPLORER", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("NOTCH BACK", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-750", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FAIRMONT", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("MUSTANG", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-350", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("RANGER S.", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("MAVERICK", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("CORCEL", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("ZEPHYR", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("GRANADA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("COUGAR ", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("BRONCO", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("ESCORT 160", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-600", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("PICK UP", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("LASER", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-7000", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-8000", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FIESTA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FUTURA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FAIRLANE ", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("LTD", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("SIERRA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("DEL REY", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("GURI", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("TRUCK", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("GALAXIE", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("CONTRY SOUIRE", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("F-450", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("TAURUS", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("SPORT WAGON", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("1979", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("973", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FOCUS", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("B-600", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("CABINA/BARANDA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("B-750", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("CONQUISTADOR", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("EXPEDITION AUTO", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("CARGO", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("B350", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("BUS VEN", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("ALKON", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("E350", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("SPORT 2PTAS.", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("HOBBY", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("ECO SPORT", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("1978", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("KA", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("ESCAPE", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("WINDSTAR", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("SPORT TRAC", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FUSION", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("FURGON", FORD, a));
        FORD.getModelos().add(new ModeloVehiculo("MAX A4VN 5PTAS.", FORD, a));
        LADA.getModelos().add(new ModeloVehiculo("21080", LADA, a));
        LADA.getModelos().add(new ModeloVehiculo("SAMARA", LADA, a));
        LADA.getModelos().add(new ModeloVehiculo("21090", LADA, a));
        LADA.getModelos().add(new ModeloVehiculo("21074", LADA, a));
        LADA.getModelos().add(new ModeloVehiculo("21051", LADA, a));
        HILLMAN.getModelos().add(new ModeloVehiculo("ARROW", HILLMAN, a));
        RAMBLER.getModelos().add(new ModeloVehiculo("HORNET", RAMBLER, a));
        ISUZU.getModelos().add(new ModeloVehiculo("CARIBE 442", ISUZU, a));
        ISUZU.getModelos().add(new ModeloVehiculo("TROOPER", ISUZU, a));
        MERCURY.getModelos().add(new ModeloVehiculo("NOTCH-BACK", MERCURY, a));
        MERCURY.getModelos().add(new ModeloVehiculo("TRACER", MERCURY, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("C-180", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("1981", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("C240 TOURING ELEGAN", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("OH1420_51", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("0302", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("AGA PREMIUN", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("K113", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("L192452", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("L1924/24", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("MB", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("L12452", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("280-SAHK", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("LO71237M", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("SPRINTER 310D", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("L1620", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("LN711/37", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("LS", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("SPRINTER", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("ATEGO", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("MERCEDES 050", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("2425/48", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("E500", MERCEDES_BENZ, a));
        MERCEDES_BENZ.getModelos().add(new ModeloVehiculo("UTILITAR", MERCEDES_BENZ, a));
        CHRYSLER.getModelos().add(new ModeloVehiculo("NEON", CHRYSLER, a));
        CHRYSLER.getModelos().add(new ModeloVehiculo("STATUS", CHRYSLER, a));
        CHRYSLER.getModelos().add(new ModeloVehiculo("LE BARON", CHRYSLER, a));
        CHRYSLER.getModelos().add(new ModeloVehiculo("IMPERIAL", CHRYSLER, a));
        CHRYSLER.getModelos().add(new ModeloVehiculo("PT CRUISER TOURING", CHRYSLER, a));
        CHRYSLER.getModelos().add(new ModeloVehiculo("SEBRING", CHRYSLER, a));
        NISSAN.getModelos().add(new ModeloVehiculo("PATHFINDER", NISSAN, a));
        NISSAN.getModelos().add(new ModeloVehiculo("SENTRA", NISSAN, a));
        NISSAN.getModelos().add(new ModeloVehiculo("PATROL", NISSAN, a));
        NISSAN.getModelos().add(new ModeloVehiculo("ALMERA ", NISSAN, a));
        FABRICACION_NACIONAL.getModelos().add(new ModeloVehiculo("ROMANO", FABRICACION_NACIONAL, a));
        JEEP.getModelos().add(new ModeloVehiculo("CJ-7", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("WAGONNER", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("CHEROKEE LAREDO", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("CHEROKEE CLASIC", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("GRAND CHEROKEE", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("CHEROKEE RENEGADE", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("STATION WAGON", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("CHEROKEE LIMITED", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("CJ-5", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("CHEROKKE", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("J-10", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("164WA", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("WRANGLER", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("RENEGADO", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("LIMITED 4 X 2", JEEP, a));
        JEEP.getModelos().add(new ModeloVehiculo("COMMANDER LIMITED", JEEP, a));
        INTERNATIONAL.getModelos().add(new ModeloVehiculo("1750", INTERNATIONAL, a));
        INTERNATIONAL.getModelos().add(new ModeloVehiculo("1850", INTERNATIONAL, a));
        INTERNATIONAL.getModelos().add(new ModeloVehiculo("F5070805", INTERNATIONAL, a));
        INTERNATIONAL.getModelos().add(new ModeloVehiculo("4700 4 X 2", INTERNATIONAL, a));
        INTERNATIONAL.getModelos().add(new ModeloVehiculo("9670", INTERNATIONAL, a));
        MACK.getModelos().add(new ModeloVehiculo("88R686P", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("R688", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("R609TV", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("MID LINER", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("RD6865", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("87R686SXLD", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("R611ST", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("R611SXV", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("600", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("R686T", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("MS 300", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("GRANITE CV713", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("CXN613LDT VISION", MACK, a));
        MACK.getModelos().add(new ModeloVehiculo("B-42", MACK, a));
        MAZDA.getModelos().add(new ModeloVehiculo("323 NE", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("626 NAV", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("B-4000 C.D", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("B2200", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("ALLEGRO", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("323", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("RX- 7", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("B", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("626 LA3", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("B-4000 ", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("626 GLX", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("626 MATSURI", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("6MV 626", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("626 LX SINC", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("B-2500 CABINA SENCILLA", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("323 NBA", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("323 HE5", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("M6X4 MAZDA6", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("M6X4 ", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("BM44 B2600CD", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("DEMIO", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("MX3", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("M3X6 MAZDA3", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("M3C7", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("3", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("M3C8", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("MAZDA 5", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("6I", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("50-2600 DOB.C", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("CX7 9 SPORT", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("MX-5", MAZDA, a));
        MAZDA.getModelos().add(new ModeloVehiculo("MIATA", MAZDA, a));
        GURI.getModelos().add(new ModeloVehiculo("LTS-9000", GURI, a));
        HONDA.getModelos().add(new ModeloVehiculo("CIVIC", HONDA, a));
        HONDA.getModelos().add(new ModeloVehiculo("GL1500", HONDA, a));
        HONDA.getModelos().add(new ModeloVehiculo("ACCORD", HONDA, a));
        HONDA.getModelos().add(new ModeloVehiculo("FIT FULL EQUIPO", HONDA, a));
        HONDA.getModelos().add(new ModeloVehiculo("ODYSSEY EX", HONDA, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("EXCEL", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("ACCENT", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("ELANTRA", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("COUPE TIBURON", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("SONATA", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("H100", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("TAXI 1.5L 4 A/T", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("ATOS", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("TUCSON", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("SANTA FE", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("H 1", HYUNDAI, a));
        HYUNDAI.getModelos().add(new ModeloVehiculo("GETZ", HYUNDAI, a));
        KIA.getModelos().add(new ModeloVehiculo("RIO", KIA, a));
        KIA.getModelos().add(new ModeloVehiculo("SPECTRA", KIA, a));
        KIA.getModelos().add(new ModeloVehiculo("SEPHIA", KIA, a));
        KIA.getModelos().add(new ModeloVehiculo("PREGIO", KIA, a));
        KIA.getModelos().add(new ModeloVehiculo("SPORTAGE", KIA, a));
        YAMAHA.getModelos().add(new ModeloVehiculo("DT", YAMAHA, a));
        YAMAHA.getModelos().add(new ModeloVehiculo("YZF", YAMAHA, a));
        YAMAHA.getModelos().add(new ModeloVehiculo("YB ", YAMAHA, a));
        YAMAHA.getModelos().add(new ModeloVehiculo("RX-100", YAMAHA, a));
        YAMAHA.getModelos().add(new ModeloVehiculo("YD110", YAMAHA, a));
        PEGASO.getModelos().add(new ModeloVehiculo("1217", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("1089LC", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("1080", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("5231", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("5232", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("1995", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("TORONTO", PEGASO, a));
        PEGASO.getModelos().add(new ModeloVehiculo("1223", PEGASO, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("LANCER", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("CANTER", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("MF", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("L300", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("MONTERO", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("FH 217", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("SIGNO", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("EXPO SP", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("L 200", MITSUBISHI, a));
        MITSUBISHI.getModelos().add(new ModeloVehiculo("TAXI 1.3", MITSUBISHI, a));
        SEAT.getModelos().add(new ModeloVehiculo("CORDOBA STELLA", SEAT, a));
        CARIBE.getModelos().add(new ModeloVehiculo("442", CARIBE, a));
        GENERICO.getModelos().add(new ModeloVehiculo("TANQUE", GENERICO, a));
        GENERICO.getModelos().add(new ModeloVehiculo("SEMI REMOLQUE", GENERICO, a));
        PEUGEOT.getModelos().add(new ModeloVehiculo("306", PEUGEOT, a));
        PEUGEOT.getModelos().add(new ModeloVehiculo("XR PREMIUM", PEUGEOT, a));
        PEUGEOT.getModelos().add(new ModeloVehiculo("XS", PEUGEOT, a));
        PEUGEOT.getModelos().add(new ModeloVehiculo("206", PEUGEOT, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("E-NT-610", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("E-610-32", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("E-610-30", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("ISUZU", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("600-28", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("E-3000-63", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("E-3100-63", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("ENT900AR", ENCAVA, a));
        ENCAVA.getModelos().add(new ModeloVehiculo("3100-A", ENCAVA, a));
        DAIHATSU.getModelos().add(new ModeloVehiculo("TERIOS", DAIHATSU, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("KENWORT", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("SCANIA", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("JUM BUSS", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("340", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("GV1150", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("360", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("K1246X2", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("B12R6X2", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("AP578X", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("P12R6X2", FABRICACION_EXTRANJERA, a));
        FABRICACION_EXTRANJERA.getModelos().add(new ModeloVehiculo("STRICK 40", FABRICACION_EXTRANJERA, a));
        PLYMOUTH.getModelos().add(new ModeloVehiculo("VALIANT", PLYMOUTH, a));
        ALL_AMERICA.getModelos().add(new ModeloVehiculo("BLUE BIRD", ALL_AMERICA, a));
        VOLVO.getModelos().add(new ModeloVehiculo("B10M", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("1991", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("JUM BUSS 340", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("1992", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("AUTOGAGO", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("MARCOPOLO", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("B12", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("ANDINO", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("PARADISO 1200", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("PARADISO GV1450", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("B-58", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("VM23 6*2", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("VM 310 4*2", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("NH", VOLVO, a));
        VOLVO.getModelos().add(new ModeloVehiculo("VOLVO B12R/MARCOP", VOLVO, a));
        BMW.getModelos().add(new ModeloVehiculo("528I", BMW, a));
        BMW.getModelos().add(new ModeloVehiculo("328 A4 AUTO", BMW, a));
        BMW.getModelos().add(new ModeloVehiculo("325", BMW, a));
        BMW.getModelos().add(new ModeloVehiculo("318", BMW, a));
        BMW.getModelos().add(new ModeloVehiculo("F650", BMW, a));
        BMW.getModelos().add(new ModeloVehiculo("330", BMW, a));
        MARCOPOLO.getModelos().add(new ModeloVehiculo("PARAISO", MARCOPOLO, a));
        ZORZI.getModelos().add(new ModeloVehiculo("PVT24", ZORZI, a));
        BUSSCAR.getModelos().add(new ModeloVehiculo("JUM BUSS 360", BUSSCAR, a));
        BUSSCAR.getModelos().add(new ModeloVehiculo("SEGÚN AÑO", BUSSCAR, a));
        SCANIA.getModelos().add(new ModeloVehiculo("NIELSON", SCANIA, a));
        SCANIA.getModelos().add(new ModeloVehiculo("K1137", SCANIA, a));
        SCANIA.getModelos().add(new ModeloVehiculo("BUSSCAR", SCANIA, a));
        SCANIA.getModelos().add(new ModeloVehiculo("K124IB6X2360", SCANIA, a));
        SCANIA.getModelos().add(new ModeloVehiculo("K1241136X2NB360", SCANIA, a));
        SCANIA.getModelos().add(new ModeloVehiculo("SCANNIA", SCANIA, a));
        SCANIA.getModelos().add(new ModeloVehiculo("SCANNIA K-124", SCANIA, a));
        MONTESA.getModelos().add(new ModeloVehiculo("OH142051", MONTESA, a));
        HARLEY.getModelos().add(new ModeloVehiculo("DAVIVSON", HARLEY, a));
        COMIL.getModelos().add(new ModeloVehiculo("VERSATILE", COMIL, a));
        PONTIAC.getModelos().add(new ModeloVehiculo("FINEBIRD", PONTIAC, a));
        LEXUS.getModelos().add(new ModeloVehiculo("LS400", LEXUS, a));
        OLDSMOBILE.getModelos().add(new ModeloVehiculo("STAR FINE", OLDSMOBILE, a));
        OLDSMOBILE.getModelos().add(new ModeloVehiculo("CONVERTIBLE", OLDSMOBILE, a));
        MANAURE.getModelos().add(new ModeloVehiculo("IZERCA", MANAURE, a));
        ROVER.getModelos().add(new ModeloVehiculo("MINICORD", ROVER, a));
        REMYVECA.getModelos().add(new ModeloVehiculo("2BVL24-90T", REMYVECA, a));
        FREIGHTLINER.getModelos().add(new ModeloVehiculo("COLUMBIA CL 120", FREIGHTLINER, a));
        FREIGHTLINER.getModelos().add(new ModeloVehiculo("M2 106 6*4", FREIGHTLINER, a));
        IVECO.getModelos().add(new ModeloVehiculo("4012", IVECO, a));
        IVECO.getModelos().add(new ModeloVehiculo("570", IVECO, a));
        IVECO.getModelos().add(new ModeloVehiculo("ML 170", IVECO, a));
        IVECO.getModelos().add(new ModeloVehiculo("60.12", IVECO, a));
        PINE.getModelos().add(new ModeloVehiculo("1PM", PINE, a));
        MONO.getModelos().add(new ModeloVehiculo("INN", MONO, a));
        LAND_ROVER.getModelos().add(new ModeloVehiculo("RANGE ROVER", LAND_ROVER, a));
        INDIANAPOLIS.getModelos().add(new ModeloVehiculo("WY150T-3", INDIANAPOLIS, a));
        INDIANAPOLIS.getModelos().add(new ModeloVehiculo("XY150-10", INDIANAPOLIS, a));
        AVA.getModelos().add(new ModeloVehiculo("150GY TIGRE", AVA, a));
        SUZUKI.getModelos().add(new ModeloVehiculo("CN 125 E SINC", SUZUKI, a));
        SUZUKI.getModelos().add(new ModeloVehiculo("AX-100", SUZUKI, a));
        SUZUKI.getModelos().add(new ModeloVehiculo("XF 650 ENDURO SINC", SUZUKI, a));
        CHERY.getModelos().add(new ModeloVehiculo("TIGGO", CHERY, a));
        ROMBAUCA.getModelos().add(new ModeloVehiculo("RBBT3ER20", ROMBAUCA, a));
        BERA.getModelos().add(new ModeloVehiculo("BR200-2", BERA, a));
        BERA.getModelos().add(new ModeloVehiculo("NEW JAGUAR ", BERA, a));
        BERA.getModelos().add(new ModeloVehiculo("NEW COBRA", BERA, a));
        BERA.getModelos().add(new ModeloVehiculo("NEW DT-200", BERA, a));
        BERA.getModelos().add(new ModeloVehiculo("BR200-4", BERA, a));
        UNICO.getModelos().add(new ModeloVehiculo("THUNDER", UNICO, a));
        EMPIRE.getModelos().add(new ModeloVehiculo("YG150-2B", EMPIRE, a));
        EMPIRE.getModelos().add(new ModeloVehiculo("KW150-F", EMPIRE, a));
        EMPIRE.getModelos().add(new ModeloVehiculo("YG125-2BE", EMPIRE, a));
        AGRALE_NEOBUS.getModelos().add(new ModeloVehiculo("THUNDER+MINIBUS 32", AGRALE_NEOBUS, a));
        HYOSUNG.getModelos().add(new ModeloVehiculo("GT-650", HYOSUNG, a));
        KEEWAY.getModelos().add(new ModeloVehiculo("HORSE KW-150", KEEWAY, a));
        KEEWAY.getModelos().add(new ModeloVehiculo("SPEED 150 CC", KEEWAY, a));
        KEEWAY.getModelos().add(new ModeloVehiculo("SUPERLIGHT 150CC", KEEWAY, a));
        KEEWAY.getModelos().add(new ModeloVehiculo("SUPERSHADOW", KEEWAY, a));
        KEEWAY.getModelos().add(new ModeloVehiculo("MATRIX 150CC", KEEWAY, a));
        KEEWAY.getModelos().add(new ModeloVehiculo("ARSEN-QJ.150", KEEWAY, a));
        SKYGO.getModelos().add(new ModeloVehiculo("SG125-24", SKYGO, a));
        SKYGO.getModelos().add(new ModeloVehiculo("SG250CC", SKYGO, a));
        SKYGO.getModelos().add(new ModeloVehiculo("SG150T-7", SKYGO, a));
        SKYGO.getModelos().add(new ModeloVehiculo("SG200CC", SKYGO, a));
        REMOLQUE.getModelos().add(new ModeloVehiculo("SEMIREMOLQUE VOLTEO 25 M3", REMOLQUE, a));
        ZXMCO.getModelos().add(new ModeloVehiculo("50QT-7", ZXMCO, a));
        ZXMCO.getModelos().add(new ModeloVehiculo("MOTO", ZXMCO, a));
        ZOTYE.getModelos().add(new ModeloVehiculo("NOMADA 1600CC MT 4X2", ZOTYE, a));
        CHANA.getModelos().add(new ModeloVehiculo("SC1022", CHANA, a));
        JIALING.getModelos().add(new ModeloVehiculo("JH150GY-2", JIALING, a));
        JIALING.getModelos().add(new ModeloVehiculo("JH 100", JIALING, a));
        DONG_FENG.getModelos().add(new ModeloVehiculo("DUOLIKA 7.0", DONG_FENG, a));
        JAC.getModelos().add(new ModeloVehiculo("HFC", JAC, a));
        SCOOTER.getModelos().add(new ModeloVehiculo("SP150T-3", SCOOTER, a));
        SCOOTER.getModelos().add(new ModeloVehiculo("BIG MAX 50", SCOOTER, a));
        YAMASAKI.getModelos().add(new ModeloVehiculo("YM150-T", YAMASAKI, a));
        DE_CARO.getModelos().add(new ModeloVehiculo("DC150T-5", DE_CARO, a));
        QINGQI.getModelos().add(new ModeloVehiculo("QM-200", QINGQI, a));
        YINGANG.getModelos().add(new ModeloVehiculo("YG-150-7", YINGANG, a));
        TYPHOON.getModelos().add(new ModeloVehiculo("150CC", TYPHOON, a));
        VENIRAUTO.getModelos().add(new ModeloVehiculo("CENTAURO", VENIRAUTO, a));

        list.add(CHEVROLET);
        list.add(RENAULT);
        list.add(TOYOTA);
        list.add(VOLKSWAGEN);
        list.add(FIAT);
        list.add(DAEWOO);
        list.add(DODGE);
        list.add(BUICK);
        list.add(FORD);
        list.add(LADA);
        list.add(HILLMAN);
        list.add(RAMBLER);
        list.add(ISUZU);
        list.add(MERCURY);
        list.add(MERCEDES_BENZ);
        list.add(CHRYSLER);
        list.add(NISSAN);
        list.add(FABRICACION_NACIONAL);
        list.add(JEEP);
        list.add(INTERNATIONAL);
        list.add(G_M_C);
        list.add(MACK);
        list.add(MAZDA);
        list.add(GURI);
        list.add(HONDA);
        list.add(HYUNDAI);
        list.add(KIA);
        list.add(YAMAHA);
        list.add(PEGASO);
        list.add(MITSUBISHI);
        list.add(SEART);
        list.add(SEAT);
        list.add(CARIBE);
        list.add(GENERICO);
        list.add(PEUGEOT);
        list.add(ENCAVA);
        list.add(DAIHATSU);
        list.add(FABRICACION_EXTRANJERA);
        list.add(PLYMOUTH);
        list.add(ALL_AMERICA);
        list.add(VOLVO);
        list.add(BMW);
        list.add(MARCOPOLO);
        list.add(ZORZI);
        list.add(IRIZAR);
        list.add(BUSSCAR);
        list.add(SCANIA);
        list.add(OH1420_51);
        list.add(AUTOGAGO);
        list.add(MONTESA);
        list.add(HARLEY);
        list.add(COMIL);
        list.add(PONTIAC);
        list.add(LML);
        list.add(LEXUS);
        list.add(OLDSMOBILE);
        list.add(DACIA);
        list.add(MANAURE);
        list.add(ROVER);
        list.add(FABRICACION_EXTRANJERA);
        list.add(REMYVECA);
        list.add(FREIGHTLINER);
        list.add(IVECO);
        list.add(ESCAPE);
        list.add(ESCAPE);
        list.add(PINE);
        list.add(MONO);
        list.add(LAND_ROVER);
        list.add(WABASH);
        list.add(INDIANAPOLIS);
        list.add(AVA);
        list.add(SUZUKI);
        list.add(CHERY);
        list.add(ROMBAUCA);
        list.add(BERA);
        list.add(SUMO);
        list.add(UNICO);
        list.add(EMPIRE);
        list.add(AGRALE_NEOBUS);
        list.add(HYOSUNG);
        list.add(KEEWAY);
        list.add(HORSE_KW_150);
        list.add(SKYGO);
        list.add(REMOLQUE);
        list.add(ZXMCO);
        list.add(ZOTYE);
        list.add(CHANA);
        list.add(JIALING);
        list.add(DONG_FENG);
        list.add(STRICK);
        list.add(JAC);
        list.add(SCOOTER);
        list.add(WANGYE);
        list.add(YAMASAKI);
        list.add(DE_CARO);
        list.add(QINGQI);
        list.add(YINGANG);
        list.add(TYPHOON);
        list.add(VENIRAUTO);

        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                MarcaVehiculo r1 = (MarcaVehiculo) o1;
                MarcaVehiculo r2 = (MarcaVehiculo) o2;
                return r1.getNombre().compareTo(r2.getNombre());
            }
        });
        for (MarcaVehiculo m : list) {

            Collections.sort(new ArrayList(m.getModelos()), new Comparator() {

                public int compare(Object o1, Object o2) {
                    ModeloVehiculo r1 = (ModeloVehiculo) o1;
                    ModeloVehiculo r2 = (ModeloVehiculo) o2;
                    return r1.getNombre().compareTo(r2.getNombre());
                }
            });
        }
        for (MarcaVehiculo o : list) {
            s.save(o);
        }
        defaultData.vehiculo.setMarcaModelo(marcaModelo);
    }// </editor-fold>

    private void colores(AuditoriaBasica a) {
        ArrayList<TipoColor> list = new ArrayList<TipoColor>(0);
        TipoColor color = new TipoColor("DESCONOCIDO", null, true, a);
        list.add(color);
        TipoColor color2 = new TipoColor("BLANCO", Color.WHITE, true, a);
        list.add(color2);
        list.add(new TipoColor("CIAN", Color.CYAN, true, a));
        list.add(new TipoColor("AZUL", Color.BLUE, true, a));
        list.add(new TipoColor("AMARILLO", Color.YELLOW, true, a));
        list.add(new TipoColor("VERDE", Color.GREEN, true, a));
        list.add(new TipoColor("ROSA", Color.PINK, true, a));
        list.add(new TipoColor("ROJO", Color.RED, true, a));
        list.add(new TipoColor("NARANJA", Color.ORANGE, true, a));
        list.add(new TipoColor("MAGNETA", Color.MAGENTA, true, a));
        list.add(new TipoColor("GRIS CLARO", Color.LIGHT_GRAY, true, a));
        list.add(new TipoColor("GRIS", Color.GRAY, true, a));
        list.add(new TipoColor("GRIS OSCURO", Color.DARK_GRAY, true, a));
        list.add(new TipoColor("NEGRO", Color.BLACK, true, a));
        for (TipoColor o : list) {
            s.save(o);
        }
        defaultData.vehiculo.setColor(color);
    }

    public void causasSiniestro(AuditoriaBasica a) {
        ArrayList<CausaSiniestro> list = new ArrayList<CausaSiniestro>(0);

        // Ver a que ramo pertenece  Personas,vehiculos, r.g. o fianzas

        //Default
        CausaSiniestro causaDesconocida = new CausaSiniestro("DESCONOCIDO", a);
        DetalleCausaSiniestro detalleDesconocido = new DetalleCausaSiniestro("DESCONOCIDO", causaDesconocida, a);
        causaDesconocida.getDetalles().add(detalleDesconocido);
        list.add(causaDesconocida);

        // Vehiculos
        CausaSiniestro dayosmali = new CausaSiniestro("DAÑOS MALICIOSOS", a);
        dayosmali.getDetalles().add(new DetalleCausaSiniestro("RAYAS", dayosmali, a));
        dayosmali.getDetalles().add(new DetalleCausaSiniestro("ABOYADURA Y RAYA", dayosmali, a));
        dayosmali.getDetalles().add(new DetalleCausaSiniestro("DAÑO DE ACCESORIO", dayosmali, a));
        list.add(dayosmali);
        CausaSiniestro choque = new CausaSiniestro("CHOQUE", a);
        choque.getDetalles().add(new DetalleCausaSiniestro("CHOQUE PERDIDA TOTAL", choque, a));
        choque.getDetalles().add(new DetalleCausaSiniestro("CHOQUE ENTRE VEHICULOS", choque, a));
        choque.getDetalles().add(new DetalleCausaSiniestro("CHOQUE ESTACIONADO", choque, a));
        choque.getDetalles().add(new DetalleCausaSiniestro("CHOQUE CONTRA OBJETO FIJO", choque, a));
        choque.getDetalles().add(new DetalleCausaSiniestro("CHOQUE CON ANIMAL", choque, a));
        list.add(choque);
        CausaSiniestro robo = new CausaSiniestro("ROBO", a);
        robo.getDetalles().add(new DetalleCausaSiniestro("ROBO HURTO", robo, a));
        robo.getDetalles().add(new DetalleCausaSiniestro("ROBO A MANO ARMADA", robo, a));
        robo.getDetalles().add(new DetalleCausaSiniestro("ROBO ACCESORIOS", robo, a));
        list.add(robo);
        CausaSiniestro incendio = new CausaSiniestro("INCENDIO", a);
        incendio.getDetalles().add(new DetalleCausaSiniestro("INCENDIO PARCIAL", incendio, a));
        incendio.getDetalles().add(new DetalleCausaSiniestro("INCENDIO TOTAL", incendio, a));
        list.add(incendio);
        CausaSiniestro volcam = new CausaSiniestro("VOLCAMIENTO", a);
        volcam.getDetalles().add(new DetalleCausaSiniestro("VOLCAMIENTO PARCIAL", volcam, a));
        volcam.getDetalles().add(new DetalleCausaSiniestro("VOLCAMIENTO TOTAL", volcam, a));
        list.add(volcam);
//        CausaSiniestro fallaveh = new CausaSiniestro("Fallas Vehículo", a);
//        fallaveh.getDetalles().add(new DetalleCausaSiniestro("Falla Mecanica", fallaveh, a));
//        fallaveh.getDetalles().add(new DetalleCausaSiniestro("Falla Electrica", fallaveh, a));
//        list.add(fallaveh);
        // Personas
        CausaSiniestro vida = new CausaSiniestro("VIDA", a);
        vida.getDetalles().add(new DetalleCausaSiniestro("MUERTE NATURAL", vida, a));
        vida.getDetalles().add(new DetalleCausaSiniestro("MUERTE ACCIDENTAL", vida, a));
        list.add(vida);
        CausaSiniestro hcm = new CausaSiniestro("H.C.M.", a);
        hcm.getDetalles().add(new DetalleCausaSiniestro("HOSPITALIZACION", hcm, a));
        hcm.getDetalles().add(new DetalleCausaSiniestro("AMBULATORIO", hcm, a));
        hcm.getDetalles().add(new DetalleCausaSiniestro("CARTA AVAL", hcm, a));
        hcm.getDetalles().add(new DetalleCausaSiniestro("REEMBOLSO", hcm, a));
        list.add(hcm);
        CausaSiniestro ap = new CausaSiniestro("ACCIDENTES PERSONALES", a);
        //ap.getDetalles().add(new DetalleCausaSiniestro("A.P. ?", ap, a));
        list.add(ap);

        for (CausaSiniestro o : list) {
            s.save(o);
        }
    }

    private void calendarioBancario(AuditoriaBasica a) {
        ArrayList<CalendarioBancario> list = new ArrayList<CalendarioBancario>(0);
        list.add(new CalendarioBancario("AÑO NUEVO", "", new Date(110, 0, 1), false, true, false, a));
        list.add(new CalendarioBancario("DIA DE REYES", "", new Date(110, 0, 4), true, false, false, a));
//        list.add(new CalendarioBancario("DIA DE REYES", "", new Date(110, 0, 6), false, true, false, a));
//        list.add(new CalendarioBancario("DIA DEL COMERCIANTE", "SOLO ESTADO FALCON", new Date(110, 0, 2), false, false, true, a));
//        list.add(new CalendarioBancario("DIA DE LA DIVINA PASTORA", "", new Date(110, 0, 14), false, false, true, a));
//        list.add(new CalendarioBancario("DIA DE SAN SEBASTIAN", "Solo Estado Tachira", new Date(110, 0, 20), false, false, true, a));
        list.add(new CalendarioBancario("CARNAVAL", "", new Date(110, 1, 15), false, true, false, a));
        list.add(new CalendarioBancario("CARNAVAL", "", new Date(110, 1, 16), false, true, false, a));
//        list.add(new CalendarioBancario("DIA DE LA BATALLA DE LA VICTORIA", "SOLO ESTADO ARAGUA", new Date(110, 1, 12), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Jose Maria Vargas", "SOLO ESTADO VARGAS", new Date(110, 2, 10), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE SAN JOSE", "", new Date(110, 2, 19), true, false, false, a));
        list.add(new CalendarioBancario("SEMANA SANTA", "", new Date(110, 2, 1), false, true, false, a));
        list.add(new CalendarioBancario("SEMANA SANTA", "", new Date(110, 2, 2), false, true, false, a));
        list.add(new CalendarioBancario("MOVIMIENTO PRECURSOR DE LA INDEPENDENCIA", "", new Date(110, 3, 19), false, true, false, a));
        list.add(new CalendarioBancario("DIA DEL TRABAJADOR", "", new Date(110, 4, 1), false, true, false, a));
//        list.add(new CalendarioBancario("DIA DE LA CRUZ DE MAYO", "SOLO ESTADO MIRANDA", new Date(110, 4, 3), false, false, true, a));
//        list.add(new CalendarioBancario("Ascensión del Señor", "", new Date(110, 4, 13), false, true, false, a));
        list.add(new CalendarioBancario("ASCENSION DEL SEÑOR", "", new Date(110, 4, 17), true, false, false, a));
//        list.add(new CalendarioBancario("Corpus Christie", "", new Date(110, 5, 3), false, true, false, a));
        list.add(new CalendarioBancario("CORPUS CHRISTIE", "", new Date(110, 5, 07), true, false, false, a));
//        list.add(new CalendarioBancario("Día de Jacinto Lara", "Solo Estado Lara", new Date(110, 4, 28), false, false, true, a));
//        list.add(new CalendarioBancario("Día de San Fernardo", "Solo Estado Apure", new Date(110, 4, 30), false, false, true, a));
        list.add(new CalendarioBancario("BATALLA DE CARABOBO", "", new Date(110, 5, 24), false, true, false, a));
        list.add(new CalendarioBancario("DIA DE SAN PEDRO Y SAN PABLO", "", new Date(110, 5, 28), true, false, false, a));
//        list.add(new CalendarioBancario("Día de San Pedro y San Pablo", "", new Date(110, 5, 29), false, true, false, a));
//        list.add(new CalendarioBancario("Día de Barinas ", "Solo Estado Barinas", new Date(110, 5, 30), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE LA INDEPENDECIA", "", new Date(110, 6, 5), false, true, false, a));
//        list.add(new CalendarioBancario("Día de la Virgen del Carmen", "Solo Edo. Zulia", new Date(110, 6, 16), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Santa Maria Magdalena", "Solo Edo. Aragua", new Date(110, 6, 16), false, false, true, a));
        list.add(new CalendarioBancario("NATALICIO DEL LIBERTADOR", "", new Date(110, 6, 24), false, true, false, a));
        list.add(new CalendarioBancario("ASSUNCION DE NUESTRA SEÑORA", "", new Date(110, 7, 15), true, false, false, a));
//        list.add(new CalendarioBancario("Día de la Vigen del Valle", "El Tigre (edo. Anzuategui) y Edo. Nueva Esparta", new Date(110, 8, 8), false, false, true, a));
//        list.add(new CalendarioBancario("Dia de Barquisimeto", "Solo Estado Lara", new Date(110, 8, 14), false, false, true, a));
//        list.add(new CalendarioBancario("Día de la Virgen del Rosario", "Villa Rosario (Edo. Zulia) y Guigue (Edo. Carabobo)", new Date(110, 9, 7), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE LA RESISTENCIA INDIGENA", "", new Date(110, 9, 12), false, true, false, a));
//        list.add(new CalendarioBancario("Día de Rafael Urdaneta", "Solo Edo. Zulia", new Date(110, 9, 24), false, false, true, a));
//        list.add(new CalendarioBancario("Día de San Rafael", "Solo Bejuma (Edo. Carabobo)", new Date(110, 9, 24), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE TODOS LOS SANTOS", "", new Date(110, 10, 1), true, false, false, a));
//        list.add(new CalendarioBancario("Toma de Puerto Cabello", "Solo Estado Carabobo", new Date(110, 0, 8), false, false, true, a));
//        list.add(new CalendarioBancario("Día de la Virgen del Socorro", "Solo Municipios de Valencia ", new Date(110, 10, 13), false, false, true, a));
//        list.add(new CalendarioBancario("Natalicio de Jose Antonio Anzuategui", "Solo Estado Anzuategui", new Date(110, 10, 14), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Nuestra Señora de la Chiquinquira", "Solo Estado Zulia", new Date(110, 10, 18), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE LA INMACULADA CONCEPCION", "", new Date(110, 10, 6), true, false, false, a));
//        list.add(new CalendarioBancario("Día de la Inmaculada Concepción", "", new Date(110, 10, 8), false, true, false, a));
//        list.add(new CalendarioBancario("Día de la Vigen de Guadalupe", "Estado Falcon", new Date(110, 12, 12), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Santa Lucia", "Solo Estado Miranda", new Date(110, 12, 13), false, false, true, a));
        list.add(new CalendarioBancario("NATIVIDAD DEL SEÑOR", "", new Date(110, 12, 25), false, true, false, a));

        for (CalendarioBancario o : list) {
            s.save(o);
        }
    }

    private void vehiculoDefault(AuditoriaBasica a) {
        Vehiculo v = new Vehiculo();
        v.setAyo(1900);
        v.setAuditoria(a);
        v.setClasificacion(defaultData.vehiculo.getClasificacionVehiculo());
        v.setMarcaModelo(defaultData.vehiculo.getMarcaModelo());
        v.setColor(defaultData.vehiculo.getColor());
        v.setPlaca("0000");
        s.save(v);
        defaultData.poliza.setVehiculo(v);
    }

    private void tipoIngresoCaja(AuditoriaBasica a) {
        ArrayList<TipoIngresoCaja> list = new ArrayList<TipoIngresoCaja>(0);
        TipoIngresoCaja polizas = new TipoIngresoCaja("POLIZAS/RECIBOS", a);
        polizas.setTipoMovimientos(getTipoMovimientosPolizas(a));
        list.add(polizas);
        TipoIngresoCaja comisiones = new TipoIngresoCaja("COMISIONES DE COMPAÑIAS", a);
        comisiones.setTipoMovimientos(geTtipoMovimientosComisiones(a));
        list.add(comisiones);
        TipoIngresoCaja facturacion = new TipoIngresoCaja("FACTURACION A COMPAÑIAS", a);
        facturacion.setTipoMovimientos(geTtipoMovimientosFacturacion(a));
        list.add(facturacion);
        TipoIngresoCaja cheques = new TipoIngresoCaja("CHEQUES DEVUELTOS", a);
        cheques.setTipoMovimientos(geTtipoMovimientosChequeDevuelto(a));
        list.add(cheques);
        for (TipoIngresoCaja o : list) {
            s.save(o);
        }
    }

    private HashSet getTipoMovimientosPolizas(AuditoriaBasica a) {
        HashSet<TipoMovimiento> list = new HashSet<TipoMovimiento>(0);
        list.add(new TipoMovimiento("INGRESO PRIMAS", "INGRESO DE PRIMAS", true, true, true, a));
        list.add(new TipoMovimiento("INICIAL CONTRA", "INICIAL DE CONTRATO", true, true, true, a));
        list.add(new TipoMovimiento("CANCELAC. GIRO", "CANCELACION DE GIRO", true, true, true, a));
        list.add(new TipoMovimiento("ADELANTO/INIC.", "ADELANTO INCIAL FINANCIAMIENTO", true, true, true, a));
        list.add(new TipoMovimiento("ADELANTO/GIRO", "ADELANTO MONTO A CUOTA DE GIRO", true, true, true, a));
        list.add(new TipoMovimiento("INTERESES MORA", "INTERESES DE MORA", true, true, true, a));
        list.add(new TipoMovimiento("OTROS INGRESOS", "OTROS INGRESOS", true, true, true, a));
        list.add(new TipoMovimiento("SOBRANTE CAJA", "SOBRANTE CAJA", true, true, true, a));
        list.add(new TipoMovimiento("FALTANTE CAJA", "FALTANTE CAJA", Boolean.FALSE, Boolean.FALSE, true, a));
        list.add(new TipoMovimiento("DCTO.GTS.COBRA", "DESCUENTO GASTOS DE COBRANZA", Boolean.FALSE, Boolean.FALSE, true, a));
        list.add(new TipoMovimiento("DCTO. DEV.PRI.", "DESCUENTO DEVOLUCIONES/PRIMA", Boolean.FALSE, Boolean.FALSE, true, a));
        list.add(new TipoMovimiento("FORMA DE PAGO", "FORMA DE PAGO", Boolean.FALSE, Boolean.FALSE, true, a));
        return list;
    }

    private HashSet geTtipoMovimientosComisiones(AuditoriaBasica a) {
        HashSet<TipoMovimiento> list = new HashSet<TipoMovimiento>(0);
        list.add(new TipoMovimiento("PRIMA/TOT", "PRIMA TOTAL", true, true, Boolean.FALSE, a));
        list.add(new TipoMovimiento("PRIMA/RCB", "PRIMA RECIBO", true, true, Boolean.FALSE, a));
        list.add(new TipoMovimiento("INGRESO COMIS.", "INGRESO DE COMISIONES", true, true, true, a));
        list.add(new TipoMovimiento("COMIS.MANUAL", "COMISION MANUAL", true, true, true, a));
        list.add(new TipoMovimiento("INGR.BONO(1)", "INGRESO BONO(1)", true, true, true, a));
        list.add(new TipoMovimiento("INGR. BONO(2)", "INGRESO BONO(2)", true, true, true, a));
        list.add(new TipoMovimiento("DCTO.COM/BONO", "DCTO. COMISION O BONO", true, true, true, a));
        list.add(new TipoMovimiento("DCTO. I.S.L.R.", "DESCUENTO I.S.L.R.", true, true, true, a));
        list.add(new TipoMovimiento("FOMRA DE PAGO", "FOMRA DE PAGO", Boolean.FALSE, Boolean.FALSE, true, a));
        return list;
    }

    private HashSet geTtipoMovimientosFacturacion(AuditoriaBasica a) {
        HashSet<TipoMovimiento> list = new HashSet<TipoMovimiento>(0);
        list.add(new TipoMovimiento("FACTURAC.CIA.", "FACTURACION A COMPAÑIA", true, true, true, a));
        return list;
    }

    private HashSet geTtipoMovimientosChequeDevuelto(AuditoriaBasica a) {
        HashSet<TipoMovimiento> list = new HashSet<TipoMovimiento>(0);
        list.add(new TipoMovimiento("CHEQUES DEV.", "CHEQUES DEVUELTOS", true, true, true, a));
        return list;
    }

    private void tipoDocumentosPago(AuditoriaBasica a) {
        ArrayList<TipoDocumentoPago> list = new ArrayList<TipoDocumentoPago>(0);
        list.add(new TipoDocumentoPago("EFE", "EFECTIVO", a));
        list.add(new TipoDocumentoPago("CHQ", "CHEQUE", a));
        list.add(new TipoDocumentoPago("TDB", "TARJETA DE DEBITO", a));
        list.add(new TipoDocumentoPago("TDC", "TARJETA DE CREDITO", a));
        list.add(new TipoDocumentoPago("DEP", "DEPOSITO", a));
        list.add(new TipoDocumentoPago("TRF", "TRANSFERENCIA", a));
        //list.add(new TipoDocumentoPago("NCR", "NOTA DE CREDITO", a));
        //list.add(new TipoDocumentoPago("NDB", "NOTA DE DEBITO", a));
        //list.add(new TipoDocumentoPago("PGD", "PAGO DOMICILIADO", a));
        list.add(new TipoDocumentoPago("OTR", "OTRO", a));
        for (TipoDocumentoPago o : list) {
            s.save(o);
        }
    }

    private void tipoTarjetaCredito(AuditoriaBasica a) {
        ArrayList<TipoTarjetaCredito> list = new ArrayList<TipoTarjetaCredito>(0);
        list.add(new TipoTarjetaCredito("MASTER", a));
        list.add(new TipoTarjetaCredito("VISA", a));
        list.add(new TipoTarjetaCredito("DAINERS", a));
        list.add(new TipoTarjetaCredito("MERICAN EXPRESS", a));
        for (TipoTarjetaCredito o : list) {
            s.save(o);
        }
    }

    private void detalleLiquidacion(AuditoriaBasica a) {
        ArrayList<DetalleLiquidacion> list = new ArrayList<DetalleLiquidacion>(0);
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.NEUTRO.toString(), "Prima Total", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.NEUTRO.toString(), "Prima Recibos", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.SUMA.toString(), "Comisiones", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.SUMA.toString(), "Comisiones Manuales", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.SUMA.toString(), "Bono 1", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.SUMA.toString(), "Bono 2", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.RESTA.toString(), "Extorno de Comisiones", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.RESTA.toString(), "Descuento en Comisiones", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.RESTA.toString(), "Descuento en Bonos", a));
        list.add(new DetalleLiquidacion(Dominios.TipoDetalleLiquidacion.RESTA.toString(), "Descuento en ISRL", a));
        for (DetalleLiquidacion o : list) {
            s.save(o);
        }
    }

    private void tipoOrdenDePago(AuditoriaBasica a) {
        ArrayList<TipoOrden> list = new ArrayList<TipoOrden>(0);
        list.add(new TipoOrden("GASTO ADMINISTRATIVO", a));
        list.add(new TipoOrden("HONORARIOS PROFESIONALES", a));
        list.add(new TipoOrden("GASTOS DE REPRESENTACION", a));
        list.add(new TipoOrden("GASTOS DE VIAJE", a));
        list.add(new TipoOrden("GASTOS DE COBRANZA", a));
        list.add(new TipoOrden("GASTOS VARIOS", a));
        list.add(new TipoOrden("GASTOS DE PUBLICIDAD", a));
        list.add(new TipoOrden("GASTOS DE ENCOMIENDAS", a));
        list.add(new TipoOrden("PAGO DE CONDOMINIO", a));
        list.add(new TipoOrden("PAGO ALQUILER", a));
        list.add(new TipoOrden("ADELANTO DE SUELDO", a));
        list.add(new TipoOrden("PAGO DE NOMINA", a));
        list.add(new TipoOrden("PESTACIONES SOCIALES", a));
        list.add(new TipoOrden("PAGO DE UTILIDADES", a));
        list.add(new TipoOrden("PAGO LIQUIDACION DE EMPLEADO", a));
        list.add(new TipoOrden("AHORRO HABITACIONAL", a));
        list.add(new TipoOrden("CESTA TICKET", a));
        list.add(new TipoOrden("COMISIONES A COBRADORES", a));
        list.add(new TipoOrden("TELEFONO", a));
        list.add(new TipoOrden("AGUA", a));
        list.add(new TipoOrden("LUZ", a));
        list.add(new TipoOrden("SEGURO SOCIAL OBLIGATORIO", a));
        list.add(new TipoOrden("IMPUESTO SOBRE LA RENTA (SENIAT)", a));
        list.add(new TipoOrden("MATERIALES DE OFICINA", a));
        list.add(new TipoOrden("PATENTE MUNICIPAL", a));
        list.add(new TipoOrden("REPOSICION DE CAJA CHICA", a));
        list.add(new TipoOrden("TRSALADO DE FONDOS", a));
        for (TipoOrden o : list) {
            s.save(o);
        }
    }

    private void tipoEnriquecimiento(AuditoriaBasica a) {
        ArrayList<TipoEnriquecimiento> list = new ArrayList<TipoEnriquecimiento>(0);
        list.add(new TipoEnriquecimiento("NINGINO DE ESTOS", null, a));
        list.add(new TipoEnriquecimiento("HONORARIOS PROFESIONALES", getTipoEnriqHonorarios(a), a));
        list.add(new TipoEnriquecimiento("COMISIONES", getTipoEnriqComisiones(a), a));
        list.add(new TipoEnriquecimiento("INTERESES", getTipoEnriqIntereses(a), a));
        list.add(new TipoEnriquecimiento("CONTRATOS Y SUB-CONTRATOS", getTipoEnriqContratos(a), a));
        list.add(new TipoEnriquecimiento("ALQUILER DE BIENES MUEBLES E INMUEBLES", getTipoEnriqAlquiler(a), a));
        list.add(new TipoEnriquecimiento("TRASPORTE NACIONAL (FLETES)", getTipoEnriqTransporte(a), a));
        list.add(new TipoEnriquecimiento("ADQUISICON DE FONDOS DE COMERCIO", getTipoEnriqAdqFondos(a), a));
        list.add(new TipoEnriquecimiento("PUBLICIDAD Y PROPAGANDA", getTipoEnriqPropaganda(a), a));
        list.add(new TipoEnriquecimiento("PUBLICIDAD RADIAL PAGADA DIRECCTAMENTE A LA EMPRESA OPERADORA", getTipoEnriqPublicidadRadial(a), a));
        list.add(new TipoEnriquecimiento("POR EL ENRIQUECIMIENTO OBTENIDO EN LA ENAJACION DE ACCIONES", null, a));
        for (TipoEnriquecimiento o : list) {
            s.save(o);
        }
    }

    private HashSet getTipoEnriqHonorarios(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 137.50, 3.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqComisiones(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 137.50, 3.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqIntereses(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 137.50, 3.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqContratos(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 137.50, 3.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqAlquiler(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 45.83, 1.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 2.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqTransporte(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 137.50, 3.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqAdqFondos(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 137.50, 3.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqPropaganda(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 5.0, "JURIDICO", a));
        return list;
    }

    private HashSet getTipoEnriqPublicidadRadial(AuditoriaBasica a) {
        HashSet<TipoEnriquecimientoDetalle> list = new HashSet<TipoEnriquecimientoDetalle>(0);
        list.add(new TipoEnriquecimientoDetalle(4583.99, 45.83, 1.0, "NATURAL", a));
        list.add(new TipoEnriquecimientoDetalle(25.0, 0.0, 1.0, "JURIDICO", a));
        return list;
    }

    private void tipoZona(AuditoriaBasica a) {
        ArrayList<TipoZona> list = new ArrayList<TipoZona>(0);
        TipoZona zona = new TipoZona("NINGUNA!", a);
        list.add(zona);
        list.add(new TipoZona("BARRIO OBRERO 75", a));
        list.add(new TipoZona("LA GUAYANA 75", a));
        list.add(new TipoZona("LA CONCORDIA 80", a));
        for (TipoZona o : list) {
            s.save(o);
        }
        defaultData.recibo.setZona(zona);
    }

    private void valoresEstandares(AuditoriaBasica auditoriaActivo) {
        ValoresEstandares ve = new ValoresEstandares();
        ve.setIva(12.0);
        ve.setUt(55.0);
    }

//    private void tipoXXX(AuditoriaBasica a) {
//        ArrayList<TipoXXx> list = new ArrayList<TipoXXx>(0);
//        list.add(new TipoXXx(, a));
//        for (TipoXXx o : list) {
//            s.save(o);
//        }
//    }
    public static void main(String[] args) {
        try {
            Crear bd = new Crear();
            bd.create();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
