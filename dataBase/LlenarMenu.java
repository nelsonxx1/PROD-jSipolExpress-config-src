package dataBase;

import controlador.General;
import java.util.Date;
import modelo.HibernateUtil;
import modelo.entidades.auditoria.AuditoriaBasica;
import modelo.entidades.menu.Item;
import modelo.entidades.menu.MenuByRol;
import modelo.entidades.menu.Rol;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author adrian
 */
public class LlenarMenu {

    public static void llenarItems() {
        Session s = null;

        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();

            Item root = new Item("root", null);


            // <editor-fold defaultstate="collapsed" desc="Personas">
            // Personas
            Item personas = new Item("Personas", null);
            {
                Item presonasGrid = new Item("Personas",
                        "PER", "menu/screenshot1.png", "getPersonas");
                Item presonaNueva = new Item("Nuevo",
                        "PER", "menu/add.png", "getPersonaNueva");
                Item buscar = new Item("Buscar",
                        "PER", "menu/search (2).png", "getBuscarPersona");
                Item historialAsegurado = new Item("Historial Asegurado",
                        "PER", "menu/search (1).png", "getHistorialAsegurado");
                personas.getItems().add(presonaNueva);
                personas.getItems().add(presonasGrid);
                personas.getItems().add(buscar);
                personas.getItems().add(historialAsegurado);
            }
            root.getItems().add(personas);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Vehiculos">
            // Vehículos
            Item vehiculos = new Item("Vehículos", null);
            {
                Item vehiculosNuevo = new Item("Nuevo",
                        "VEH", "menu/add.png", "getVehiculoNuevo");
                vehiculos.getItems().add(vehiculosNuevo);
                Item vehiculosGrid = new Item("Vehículos",
                        "VEH", "menu/carro.png", "getVehiculos");
                Item buscar = new Item("Buscar",
                        "VEH", "menu/search (3).png", "getBuscarVehiculo");
                Item historial = new Item("Historial",
                        "VEH", "menu/search (1).png", "getHistorialVehiculo");
                vehiculos.getItems().add(vehiculosGrid);
                vehiculos.getItems().add(buscar);
                vehiculos.getItems().add(historial);
            }

            root.getItems().add(vehiculos);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Polizas">
            // Pólizas
            Item polizas = new Item("Pólizas", null);
            {
                Item polizaNuevo = new Item("Nuevo",
                        "POL", "menu/add.png", "getPolizaNuevo");
                polizas.getItems().add(polizaNuevo);
                Item polizasGrid = new Item("Polizas",
                        "POL", "menu/poliza.png", "getPolizas");
                polizas.getItems().add(polizasGrid);
                Item buscar = new Item("Buscar Poliza",
                        "REC", "menu/search (3).png", "getBuscarPoliza");
                polizas.getItems().add(buscar);
            }

            root.getItems().add(polizas);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Recibos">
            //Recibos
            Item recibos = new Item("Recibos", null);
            {
                Item reciboNuevo = new Item("Nuevo",
                        "REC", "menu/add.png", "getReciboNuevo");
                recibos.getItems().add(reciboNuevo);
                Item recibosGrid = new Item("Recibos",
                        "REC", "menu/Notepad.png", "getRecibos");
                recibos.getItems().add(recibosGrid);
                Item dist = new Item("Distribucion de Comision",
                        "REC", "menu/money.png", "getTipoDist");
                recibos.getItems().add(dist);
                Item buscar = new Item("Buscar Recibo",
                        "REC", "menu/search (3).png", "getBuscarRecibo");
                recibos.getItems().add(buscar);
//                Item distr = new Item("dist",
//                        "REC", null, "getDist");
//                recibos.getItems().add(distr);
            }

            root.getItems().add(recibos);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Financiamientos">
            //Financiamiento
            Item financiamiento = new Item("Financiamientos", null);
            {
                Item financiamientoNuevo = new Item("Nuevo",
                        "FIN", "menu/add.png", "getFinanciamientoNuevo");
                financiamiento.getItems().add(financiamientoNuevo);
                Item financiamientosGrid = new Item("Financiamientos",
                        "FIN", "menu/financiamiento.png", "getFinanciamientos");
                financiamiento.getItems().add(financiamientosGrid);
                Item buscar = new Item("Buscar Financiamiento",
                        "REC", "menu/search (3).png", "getBuscarFinanciamiento");
                financiamiento.getItems().add(buscar);

            }

            root.getItems().add(financiamiento);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Siniestros">
            // Siniestros
            Item siniestros = new Item("Siniestros", null);
            {
                Item siniestroNuevo = new Item("Nuevo",
                        "SIN", "menu/add.png", "getSiniestroNuevo");
                siniestros.getItems().add(siniestroNuevo);
                Item siniestrosGrid = new Item("Siniestros",
                        "SIN", "menu/thunder.png", "getSiniestrosGrid");
                siniestros.getItems().add(siniestrosGrid);
                Item buscar = new Item("Buscar Siniestro",
                        "REC", "menu/search (3).png", "getBuscarSiniestro");
                siniestros.getItems().add(buscar);
            }

            root.getItems().add(siniestros);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Administracion">
            // Administracion
            Item admistracion = new Item("Administracion (BETA)", null);
            admistracion.getAuditoria().setActivo(Boolean.FALSE);
            {
                Item ingresos = new Item("Ingresos", null);
                {
                    Item facturar = new Item("Facturar Comisiones",
                            "PER", null, "AUN_NO_FUN");
                    Item facturaManual = new Item("Registrar Factura Manual",
                            "PER", "insert2.png", "AUN_NO_FUN");
                    Item todasFacturas = new Item("Todas las Facturas",
                            "PER", null, "AUN_NO_FUN");
                    Item extraordinario = new Item("Ingresos Extraordinarios",
                            "PER", null, "AUN_NO_FUN");
                    Item todosIngresos = new Item("Todos los Ingresos",
                            "PER", null, "AUN_NO_FUN");
                    ingresos.getItems().add(facturar);
                    ingresos.getItems().add(facturaManual);
                    ingresos.getItems().add(todasFacturas);
                    ingresos.getItems().add(extraordinario);
                    ingresos.getItems().add(todosIngresos);
                }
                Item egresos = new Item("Egresos", null);
                {
                    Item pagarComi = new Item("Pagar Comisiones",
                            "PER", null, "AUN_NO_FUN");
                    Item nuevaFactura = new Item("Nueva Factura / CxP",
                            "PER", "insert2.png", "getFacturaCompraNueva");
                    Item todasFacturas = new Item("Todas las Facturas / CxP",
                            "PER", null, "getFacturasCompras");
                    Item extraordinarios = new Item("Egresos Extraordinarios",
                            "PER", null, "getEgresosExtraordinarios");
                    Item todosEgresos = new Item("Todos los Egresos",
                            "PER", null, "getTodosEgresos");
                    egresos.getItems().add(pagarComi);
                    egresos.getItems().add(nuevaFactura);
                    egresos.getItems().add(todasFacturas);
                    egresos.getItems().add(extraordinarios);
                    egresos.getItems().add(todosEgresos);
                }
                admistracion.getItems().add(ingresos);
                admistracion.getItems().add(egresos);
            }

            root.getItems().add(admistracion);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Facturacion">
            //Facturacion
            Item facturacion = new Item("Facturacion", null);
            facturacion.getAuditoria().setActivo(Boolean.FALSE);
            {
                Item nuevaFac = new Item("Nueva Factura",
                        "FACT", "insert2.png", "AUN_NO_FUN");
                facturacion.getItems().add(nuevaFac);
                Item nuevaFacComi = new Item("Nueva Factura de Comisiones",
                        "FACT", "insert2.png", "AUN_NO_FUN");
                facturacion.getItems().add(nuevaFacComi);
                Item facturas = new Item("Facturas",
                        "FACT", null, "AUN_NO_FUN");
                facturacion.getItems().add(facturas);

                Item cuentasXcobrar = new Item("Cuentas Por Cobrar", null);
                {
                    facturacion.getItems().add(cuentasXcobrar);
                    Item nuevaCxC = new Item("Nueva",
                            "FACT", "insert2.png", "AUN_NO_FUN");
                    cuentasXcobrar.getItems().add(nuevaCxC);
                    Item agruparCxC = new Item("Agrupar",
                            "FACT", null, "AUN_NO_FUN");
                    cuentasXcobrar.getItems().add(agruparCxC);
                    Item desglosarCxC = new Item("Desglosar",
                            "FACT", null, "AUN_NO_FUN");
                    cuentasXcobrar.getItems().add(desglosarCxC);
                }
                Item liquidacion = new Item("Liquidaciones de Comision", null);
                {
                    facturacion.getItems().add(liquidacion);
                    Item nuevaLiquidacion = new Item("Nueva",
                            "FACT", "insert2.png", "getNuevaLiquidacion");
                    liquidacion.getItems().add(nuevaLiquidacion);
                    Item liquidaciones = new Item("Liquinaciones",
                            "FACT", null, "getLiquidaciones");
                    liquidacion.getItems().add(liquidaciones);
                }
            }

            root.getItems().add(facturacion);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Orden de Pago">
            // Orden de pago

            Item ordenPago = new Item("Ordenes de Pago y Cheques", null);
            ordenPago.getAuditoria().setActivo(Boolean.FALSE);
            {
                Item nueva = new Item("Nueva",
                        "FACT", "insert2.png", "AUN_NO_FUN");
                ordenPago.getItems().add(nueva);
                Item nuevaComi = new Item("Nueva. Pago de Comisiones",
                        "FACT", "insert2.png", "getOrdenComision");
                ordenPago.getItems().add(nuevaComi);
                Item ordenes = new Item("Ordenes de Pago",
                        "FACT", null, "AUN_NO_FUN");
                ordenPago.getItems().add(ordenes);
                Item cuentasXpagar = new Item("Cuentas por Pagar", null);
                {
                    ordenPago.getItems().add(cuentasXpagar);
                    Item nuevaCxP = new Item("Nueva",
                            "FACT", "insert2.png", "AUN_NO_FUN");
                    cuentasXpagar.getItems().add(nuevaCxP);
                    Item agruparCxP = new Item("Agrupar",
                            "FACT", null, "AUN_NO_FUN");
                    cuentasXpagar.getItems().add(agruparCxP);
                    Item desglosarCxP = new Item("Desglosar",
                            "FACT", null, "AUN_NO_FUN");
                    cuentasXpagar.getItems().add(desglosarCxP);
                }
                Item facturasXpagar = new Item("Facturas por Pagar", null);
                {
                    ordenPago.getItems().add(facturasXpagar);
                    Item nuevaF = new Item("Nueva Factura",
                            "FACT", "insert2.png", "AUN_NO_FUN");
                    facturasXpagar.getItems().add(nuevaF);
                    Item nueva1 = new Item("Nueva Factura de 1 Cuenta por Pagar",
                            "FACT", "insert2.png", "AUN_NO_FUN");
                    facturasXpagar.getItems().add(nueva1);
                    Item nueva2 = new Item("Nueva Factura de Cuentas por Pagar",
                            "FACT", null, "AUN_NO_FUN");
                    facturasXpagar.getItems().add(nueva2);
                    Item facturas = new Item("Facturas",
                            "FACT", null, "AUN_NO_FUN");
                    facturasXpagar.getItems().add(facturas);
                }
            }

            root.getItems().add(ordenPago);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Mensajes de Texto">
            Item sms = new Item("Mensaje de Texto (SMS)", null);
            {
                Item masivoA = new Item("Masivo a:", null);
                sms.getItems().add(masivoA);

                Item smsReciboPend = new Item("Buzon de Entrada",
                        "SMSEntrada", "menu/inbox.png", "getSMSRecibidos");
                sms.getItems().add(smsReciboPend);

                Item smsSolo = new Item("SMS Individual",
                        "SMS", "menu/send_sms.png", "getSMSSolo");
                sms.getItems().add(smsSolo);

                Item smsPreEscrito = new Item("Plantillas",
                        "SMS", "menu/paper.png", "getSMSPreEscritos");
                sms.getItems().add(smsPreEscrito);

                Item smsAutomaticos = new Item("SMS Automaticos",
                        "SMS", "menu/send_sms.png", "getSMSAutomaticos");
                sms.getItems().add(smsAutomaticos);

                Item smsCumple = new Item("Cumpleañeros",
                        "SMS", "menu/cumple.png", "getSMSCumpleayeros");
                masivoA.getItems().add(smsCumple);
                Item smsGiro = new Item("Giros de Financiamiento",
                        "SMS", "menu/financiamiento.png", "getSMSGiros");
                masivoA.getItems().add(smsGiro);
                Item smsDocumentos = new Item("Vencimiento de Documentos anexos",
                        "SMS", "menu/card.png", "getSMSDocumentos");
                masivoA.getItems().add(smsDocumentos);
                Item smsRenovacion = new Item("Vencimiento de Recibos",
                        "SMS", "menu/Notepad.png", "getSMSRecibos");
                masivoA.getItems().add(smsRenovacion);
                Item smstodos = new Item("Todos los contactos",
                        "SMS", "menu/todos.png", "getSMSRTodos");
                masivoA.getItems().add(smstodos);
//                Item smsReciboPend = new Item("Aviso Vcto. Recibos Pendientes",
//                        "SMS", null, "getSMSREciboPend");
//                xxx.getItems().add(smsReciboPend);

                Item smsEnviados = new Item("Buzon de Salida",
                        "SMS", "menu/outbox masa.png", "getSMSEnviados");
                sms.getItems().add(smsEnviados);
                Item masa = new Item("Buzon Salida Masivos",
                        "SMS", "menu/outbox masa.png", "getSMSMasa");
                sms.getItems().add(masa);
            }

            root.getItems().add(sms);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Mantenimiento">
            //Mantenimiento
            Item mantenimiento = new Item("Mantenimiento", null/*"advancedsettings.png"*/);
            {
                Item roles = new Item("Roles", null);
                {
                    Item rol = new Item("Lista de Roles",
                            "REPORT", "menu/screenshot4.png", "getNuevoRol");
                    Item rolOp = new Item("Configurar Roles",
                            "REPORT", "menu/services.png", "getRoles");
                    roles.getItems().add(rol);
                    roles.getItems().add(rolOp);

                }
                mantenimiento.getItems().add(roles);
                //Mantenimiento Personas
                Item manPer = new Item("Personas", null);
                {
                    mantenimiento.getItems().add(manPer);
                    Item actEco = new Item("Tipo Actividad Economica",
                            "MAN", "menu/Briefcase.png", "getTipoActividadEconomica");
                    Item capEco = new Item("Tipo Capacidad Economica",
                            "MAN", "menu/money.png", "getTipoCapacidadEconomica");
                    Item tipPer = new Item("Tipo Persona",
                            "MAN", "menu/employee.png", "getTipoPersona");
                    Item tipTel = new Item("Tipo Telefono",
                            "MAN", "menu/call.png", "getTipoTelefono");
                    Item tipDir = new Item("Tipo Direccion",
                            "MAN", "menu/Home.png", "getTipoDireccion");
                    Item tipCtB = new Item("Tipo Cuenta Bancaria",
                            "MAN", "menu/piggy_bank.png", "getTipoCuentaBancaria");
                    Item codigoArea = new Item("Codigos de Area",
                            "MAN", "menu/Maps.png", "getCodigoArea");
                    manPer.getItems().add(actEco);
                    manPer.getItems().add(capEco);
                    manPer.getItems().add(tipPer);
                    manPer.getItems().add(tipTel);
                    manPer.getItems().add(tipDir);
                    manPer.getItems().add(tipCtB);
                    manPer.getItems().add(codigoArea);
                }
                //mantenimiento Vehículos
                Item manVeh = new Item("Vehículos", null);
                {
                    mantenimiento.getItems().add(manVeh);
                    Item marcasModelos = new Item("Marcas - Modelos",
                            "MAN", "menu/marca.png", "getMarcasModelos");
                    manVeh.getItems().add(marcasModelos);
                    Item tipoColor = new Item("Colores",
                            "MAN", "menu/preferencescolor.png", "getTipoColores");
                    manVeh.getItems().add(tipoColor);
                    Item clasificacionVeh = new Item("Clasificacion",
                            "MAN", "menu/Notes.png", "getClasificacionVehiculo");
                    manVeh.getItems().add(clasificacionVeh);
                }
                // mantenimiento Polizas
                Item manPol = new Item("Pólizas", null);
                {
                    mantenimiento.getItems().add(manPol);
                    Item ramo = new Item("Ramos de Pólizas",
                            "MAN", "menu/poliza.png", "getRamoPoliza");
                    manPol.getItems().add(ramo);
                    Item grupo = new Item("Grupos",
                            "MAN", "menu/grupoPoliza.png", "getGrupoPoliza");
                    manPol.getItems().add(grupo);
                }
                //mantenimiento Recibos
                Item manRec = new Item("Recibos", null);
                {
                    mantenimiento.getItems().add(manRec);
                    Item mantZona = new Item("Zona",
                            "MAN", "menu/Contacts.png", "getTipoZona");
                    manRec.getItems().add(mantZona);
                    Item mantRamCob = new Item("Ramos - Coberturas",
                            "MAN", "menu/grupoPoliza.png", "getRamosCoberturas");
                    manRec.getItems().add(mantRamCob);
                }
                // mantenimiento Siniestros
                Item manSin = new Item("Siniestros", null);
                {
                    mantenimiento.getItems().add(manSin);
                    Item MntoTipoPerdida = new Item("Causas de Siniestro",
                            "MAN", "menu/screenshot3.png", "getCausaSiniestro");
                    manSin.getItems().add(MntoTipoPerdida);
                }
                //Mantenimiento Ordenes de Pago
                Item mantOrdenesPago = new Item("Ordenes de Pago", null);
                mantOrdenesPago.getAuditoria().setActivo(Boolean.FALSE);
                {
                    mantenimiento.getItems().add(mantOrdenesPago);
                    Item tipoOrden = new Item("Tipo de Orden de Pago",
                            "MAN", "menu/Notes.png", "getTipoOrden");
                    mantOrdenesPago.getItems().add(tipoOrden);
                }
                // mantenimiento Sistema
                Item mantSistema = new Item("Sistema", null);
                {
                    mantenimiento.getItems().add(mantSistema);
                    Item usuario = new Item("Usuarios",
                            "MAN", "menu/user.png", "getUsuarios");
                    mantSistema.getItems().add(usuario);
                    Item empresa = new Item("Empresa",
                            "MAN", "menu/company.png", "getEmpresa");
                    mantSistema.getItems().add(empresa);
                    Item configuracion = new Item("Configuracion",
                            "MAN", "menu/advancedsettings.png", "getConfiguracion");
                    mantSistema.getItems().add(configuracion);
                    if (General.usuario.getAdministrador()) {
                        Item licencias = new Item("Licencias",
                                "MAN", "menu/paper.png", "getLicencias");
                        mantSistema.getItems().add(licencias);
                    }
                    Item configLnF = new Item("Config Tiny LnF",
                            "MAN", "menu/mycomputer.png", "getConfigLnF");
                    mantSistema.getItems().add(configLnF);

                }
                Item tipDocAnex = new Item("Documentos Anexos",
                        "MAN", "menu/anexo.png", "getTipoDocAnex");
                mantenimiento.getItems().add(tipDocAnex);

                Item cuentasBanc = new Item("Cuentas Bancarias",
                        "MAN", "menu/piggy_bank.png", "AUN_NO_FUN");
                mantenimiento.getItems().add(cuentasBanc);
                cuentasBanc.getAuditoria().setActivo(Boolean.FALSE);


                Item encabezado = new Item("Encabezados de Reporte",
                        "MAN", "menu/printer.png", "getEncabezado");
//                if (General.usuario.getAdministrador()) {
                    mantenimiento.getItems().add(encabezado);
//                }
            }

            root.getItems().add(mantenimiento);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Comunicados">
            //Comunicados
            Item comunicados = new Item("Comunicados y Solicitudes",
                    "COMU", "menu/email.png", "getComunicadosGrid");

            root.getItems().add(comunicados);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Reportes">
            //Reportes

            Item reportes = new Item("Reportes",
                    "REPORT", "menu/printer1.png", "getReporteH");

            root.getItems().add(reportes);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cambiar Contraseña">
            //Reportes

            Item changePass = new Item("Cambiar Contraseña",
                    "REPORT", "menu/key.png", "changePass");

            root.getItems().add(changePass);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calendario">
            Item calendario = new Item("Calendario",
                    "HELP", "menu/calendar.png", "getCalendario");

            root.getItems().add(calendario);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Soporte Tecnico">
            Item helpCenter = new Item("Soporte Técnico",
                    "HelpCenter", "menu/call-us.png", "getHelpCenter");

            root.getItems().add(helpCenter);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Sincronizar">
            Item sincro = new Item("Sincronizar",
                    "SINC", "menu/update.png", "getSincronizar");
            sincro.getAuditoria().setActivo(Boolean.FALSE);
            root.getItems().add(sincro);
            // </editor-fold>
 
            // <editor-fold defaultstate="collapsed" desc="Salir">
            Item exit = new Item("Salir",
                    "HELP", "menu/exit.png", "getExit");

            root.getItems().add(exit);
            // </editor-fold>

            s.save(root);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }

    }

    public static void updateMenu() {
        printMenuIds(getRootItem(), 0);

        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();
            Item i = (Item) optenerData(Item.class.getName()
                    + " where id=122").get(0);
            Item item = new Item("Nueva Carpeta", null);
            i.getItems().add(1, item);
            s.saveOrUpdate(i);

            java.util.List roles = optenerData(Rol.class.getName());
            for (Object object : roles) {
                Rol rol = (Rol) object;
                fillMenu(rol, item, s);
            }

            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }

    }

    public static void printMenuIds(Item i, int level) {
        System.out.print(i.getId());
        for (int k = 0; k < level; k++) {
            System.out.print(" ");
        }
        System.out.println(i.getNombre());
        for (Item item : i.getItems()) {
            printMenuIds(item, level + 1);
        }
    }

    public static void llenarRoles() {
        Session s = null;
        Date d = new Date();
        AuditoriaBasica audit = (new AuditoriaBasica(d, "defaultData", true));
        AuditoriaBasica audit1 = (new AuditoriaBasica(d, "defaultData", true));
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            // <editor-fold defaultstate="collapsed" desc="Roles">
            tx = s.beginTransaction();
            Rol root = new Rol("Administrador del Sistema");
            {
                root.setAuditoria(audit);
                root.getAuditoria().setActivo(Boolean.FALSE);
                root.getAuditoria().setVisible2(Boolean.FALSE);
                s.save(root);
            }
            Rol secretaria = new Rol("Usuario");
            {
                secretaria.setAuditoria(audit1);
                secretaria.getAuditoria().setActivo(Boolean.TRUE);
                secretaria.getAuditoria().setVisible2(Boolean.TRUE);
                s.save(secretaria);
            }
            tx.commit();
            // </editor-fold>

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }

    }

    public static void llenarMenuByRoles() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            // <editor-fold defaultstate="collapsed" desc="Roles">
            tx = s.beginTransaction();
            java.util.List roles = optenerData(Rol.class.getName());
            Item items = (Item) optenerData(Item.class.getName()
                    + " where nombre='root'").get(0);
            for (Object object : roles) {
                Rol rol = (Rol) object;
                for (Item item : items.getItems()) {
                    fillMenu(rol, item, s);
                }
            }

            tx.commit();
            // </editor-fold>

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }

    }

    static void fillMenu(Rol rol, Item items, Session s) {
        MenuByRol nuevo = new MenuByRol(items.getId(), rol.getId(), items.getAuditoria().getActivo());
        s.save(nuevo);
        for (Item item : items.getItems()) {
            fillMenu(rol, item, s);
        }
    }

    static java.util.List optenerData(String valueObjectClassName) {

        Session s = null;
        java.util.List mensajes = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            {
                String hql = "FROM " + valueObjectClassName;
                Query query = s.createQuery(hql);
                mensajes = query.list();
            }
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return mensajes;
    }

    public static void saveData(Object o) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            tx = s.beginTransaction();
            s.save(o);
            tx.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }

    public static void updateData(Object o) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            tx = s.beginTransaction();
            s.update(o);
            tx.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }

    public static void addMenu(Item menu) {
        Item root = new LlenarMenu().getRootItem();
        root.getItems().add(menu);
        updateData(root);
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            tx = s.beginTransaction();
            java.util.List roles = optenerData(Rol.class.getName());
            for (Object object : roles) {
                Rol rol = (Rol) object;
                fillMenu(rol, menu, s);
                for (Item item : menu.getItems()) {
                    fillMenu(rol, item, s);
                }
            }

            tx.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }

    private static Item getRootItem() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Item items = (Item) optenerData(Item.class.getName()
                    + " where nombre='root'").get(0);
            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
        return null;
    }
}
