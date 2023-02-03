/**
 * 
 */
package cl.banchile.bchrest.administra.ingreso.operaciones.common.utils;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.monedaextranjeraservice.model.OperacionMonedaExtranjeraModel;
import cl.banchile.framework.lib.uniqueid.SnowflakeUniqueId;
import cl.banchile.ingope.Ingope;
import cl.banchile.ingope.OperacionMonedaExtranjera;
import cl.banchile.ingope.OperacionFondosMutuos;
import cl.banchile.ingope.OperacionAcciones;
import cl.banchile.ingope.PIngresarInstruccions;
import cl.banchile.liquidaciones.java.eapsrv.ejb.OrdenTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
/**
 * Clase con con métodos estáticos
 * de apoyo a la implementación
 */
/**
 * @author Pablo
 *
 */
@Slf4j
@Component
public class Utils {
    private ObjectMapper objectMapper;
    private Ingope ingope;
    private Utils() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ingope = new Ingope();
    }
    /**
     * Transforma una fecha XmlGregorianCalendar en LocalDate
     *
     * @param xmlGregorianCalendar fecha XML del calendario Gregoriano
     * @return LocalDate
     */
    public LocalDate xmlGregorianCalendarToLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return Optional.ofNullable(xmlGregorianCalendar)
                .map(xmlDate -> LocalDate.of(
                                xmlDate.getYear(),
                                xmlDate.getMonth(),
                                xmlDate.getDay()
                        )
                ).orElse(null);
    }
    /**
     * Transforma una fecha XmlGregorianCalendar en LocalDateTime
     *
     * @param xmlGregorianCalendar fecha XML del calendario Gregoriano
     * @return LocalDateTime
     */
    public LocalDateTime xmlGregorianCalendarToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        return Optional.ofNullable(xmlGregorianCalendar)
                .map(xmlDate -> LocalDateTime.of(
                                xmlDate.getYear(),
                                xmlDate.getMonth(),
                                xmlDate.getDay(),
                                xmlDate.getHour(),
                                xmlDate.getMinute(),
                                xmlDate.getSecond()
                        )
                ).orElse(null);
    }
    /**
     * Transforma una fecha LocalDateTime en XmlGregorianCalendar
     *
     * @param from Local DateTime
     * @return fecha XML del calendario Gregoriano
     */
    public XMLGregorianCalendar localDateTimeToXMLGregorianCalendar(LocalDateTime from) {
        GregorianCalendar gc = new GregorianCalendar(from.getYear(), from.getMonthValue(), from.getDayOfMonth(), from.getHour(), from.getMinute(), from.getSecond());
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            log.error("Error al convertir LocalDate a XMLGregorianCalendar", e);
        }
        return null;
    }
    /**
     * Función que convierte el parametro de entrada a numerico
     *
     * @param valorATransformar
     * @return Integer
     * @throws Exception
     */
    public Integer convertStringToInteger(String valorATransformar) {
        Integer value = 0;
        try {
            value = Integer.valueOf(valorATransformar);
        } catch (NumberFormatException e) {
            log.error("Ha ocurrido un error => " + e);
        }
        return value;
    }
    /**
     * Función encargada de retornar un booleano según el parámetro de entrada para el campo indicador18Q
     *
     * @param value
     * @return boolean
     */
    public boolean completarIndicador18Q(String value) {
        try {
            if (("S").equals(value) || ("SI").equals(value)) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e) {
            log.error("Ocurrio un error al completar campo indicador18Q => " + e);
            return false;
        }
    }
    /**
     * Función encargada de formatear la fecha entrante a cadena de texto
     *
     * @param fechaGrogoriana
     * @return String
     */
    public String convertDateGregorianToString(XMLGregorianCalendar fechaGrogoriana) {
        String date = "";
        try {
            String pattern = "yyyy-MM-dd, hh:mm:ss a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            date = simpleDateFormat.format(fechaGrogoriana.toGregorianCalendar().getTime());
        } catch (RuntimeException e) {
            log.error("Ha ocurrido un error =>" + e);
        }
        return date;
    }
    /**
     * Función encargada de validar y generar una fecha valida
     *
     * @param fecha
     * @return
     */
    public XMLGregorianCalendar validaFechas(XMLGregorianCalendar fecha) {
        XMLGregorianCalendar fechaNueva = null;
        if (fecha == null) {
            fechaNueva = localDateTimeToXMLGregorianCalendar(LocalDateTime.now());
        } else {
            if (!fecha.isValid()) {
                fechaNueva = localDateTimeToXMLGregorianCalendar(LocalDateTime.now());
            } else {
                fechaNueva = fecha;
            }
        }
        return fechaNueva;
    }
    /**
     * Función encargada de castear un Objeto a BigDecimal
     *
     * @param objACastear
     * @return BigDecimal
     */
    public BigDecimal castBigDecimal(Object objACastear) {
        BigDecimal value = new BigDecimal(0);
        try {
            if (objACastear instanceof String) {
                if (("").equals(objACastear) || ((String) objACastear).isEmpty()) {
                    objACastear = "0";
                }
                value = BigDecimal.valueOf(Double.parseDouble(objACastear.toString()));
            } else if (objACastear instanceof Integer) {
                Integer integer = Integer.valueOf(objACastear.toString());
                value = BigDecimal.valueOf(integer);
            } else if (objACastear instanceof Float) {
                Float numberFloat = Float.valueOf(objACastear.toString());
                value = BigDecimal.valueOf(numberFloat);
            } else if (objACastear instanceof Double) {
                Double numberDouble = Double.valueOf(objACastear.toString());
                value = BigDecimal.valueOf(numberDouble);
            }
        } catch (NumberFormatException e) {
            log.error("Ocurrio un error en castBigDecimal =>", e);
        }
        return value;
    }
    /**
     * Función encargada de castear un Objeto a Long
     *
     * @param objACastear
     * @return Long
     */
    public Long castLong(Object objACastear) {
        Long value = 0L;
        try {
            if (objACastear instanceof Long) {
                value = Long.valueOf(objACastear.toString());
            } else if (objACastear instanceof String) {
                if (("").equals(objACastear) || ((String) objACastear).isEmpty()) {
                    objACastear = "0";
                }
                value = Long.valueOf(objACastear.toString());
            } else if (objACastear instanceof Integer) {
                Integer integer = Integer.valueOf(objACastear.toString());
                value = Long.valueOf(integer);
            } else if (objACastear instanceof Float) {
                value = Long.valueOf(objACastear.toString());
            } else if (objACastear instanceof Byte) {
                value = Long.valueOf(objACastear.toString());
            }
        } catch (NumberFormatException e) {
            log.error("Ocurrio un error en castLong => ", e);
        }
        return value;
    }
    /**
     * Función encargada de castera un Objeto a Integer
     *
     * @param objACastear
     * @return Integer
     */
    public Integer castInteger(Object objACastear) {
        Integer value = 0;
        try {
            if (objACastear instanceof Long) {
                value = Integer.valueOf(objACastear.toString());
            } else if (objACastear instanceof String) {
                if (("").equals(objACastear) || ((String) objACastear).isEmpty()) {
                    objACastear = "0";
                }
                objACastear = objACastear.toString().split(".");
                value = Integer.valueOf(objACastear.toString());
            } else if (objACastear instanceof Integer) {
                value = Integer.valueOf(objACastear.toString());
            } else if (objACastear instanceof BigDecimal) {
                value = Integer.valueOf(objACastear.toString());
            }
        } catch (NumberFormatException e) {
            log.error("Ocurrio un error en castInteger => ", e);
        }
        return value;
    }
    /**
     * Función encargada de generar un ID único para registrar en RAC
     *
     * @param
     * @return Long
     */
    public Long generarUUII() {
        SnowflakeUniqueId uniqueId = new SnowflakeUniqueId();
        return uniqueId.getIdAsLong();
    }
    /**
     * Función encargada de crear el objeto OrdenTO para InstruccionPagoTO de Fondos Mutuos
     *
     * @param objetoRespuesta
     * @return OrdenTO
     * @throws JsonProcessingException
     */
    public OrdenTO completaOrdenTO(JSONObject objetoRespuesta) throws JsonProcessingException {
        return objectMapper.readValue(objetoRespuesta.toString(), OrdenTO.class);
    }
    /**
     * Función encargada de corregir campos de fecha que vengan con formato XML
     *
     * @param objetoEntrada
     * @return JSONObject
     */
    public JSONObject arreglarJson(JSONObject objetoEntrada) {
        objetoEntrada.put("fechaCapturaOrden", localDateTimeToXMLGregorianCalendar(
                convertStringToZonedDateTime(objetoEntrada, "fechaCapturaOrden").toLocalDateTime()));
        objetoEntrada.put("fechaIngresoOperacion", localDateTimeToXMLGregorianCalendar(
                convertStringToZonedDateTime(objetoEntrada, "fechaIngresoOperacion").toLocalDateTime()));
        objetoEntrada.put("fechaLiquidacionRequerida", localDateTimeToXMLGregorianCalendar(
                convertStringToZonedDateTime(objetoEntrada, "fechaLiquidacionRequerida").toLocalDateTime()));
        return objetoEntrada;
    }
    /**
     * Función encargada de formatear una fecha de tipo String a ZonedDateTime
     *
     * @param ordenLiquidacionTO
     * @param keyValue
     * @return ZonedDateTime
     */
    public ZonedDateTime convertStringToZonedDateTime(JSONObject ordenLiquidacionTO, String keyValue) {
        JSONObject json;
        json = XML.toJSONObject(ordenLiquidacionTO.get(keyValue).toString());
        String fechaString = json.getJSONObject(keyValue).getString("content");
        return ZonedDateTime.parse(fechaString, DateTimeFormatter.ISO_DATE_TIME);
    }
    /**
     * Función encargada de mapear el objeto entrante a la clase Ingope
     *
     * @param jsonObject
     * @return Ingope
     */
    public Ingope mapeoIngope(JSONObject jsonObject) throws JsonProcessingException {
        ingope = new Ingope();
        //instruccions = new PIngresarInstruccions();
        //instruccion = new ArrayList<>();
        camposBase(jsonObject);
        camposMonedaExtranjera(jsonObject);
        camposAcciones(jsonObject);
        camposFondosMutuos(jsonObject);
        crearInstruccionesIngope(jsonObject);
        //ingope = objectMapper.readValue(jsonObject.toString(), Ingope.class);
        //ingope.setPIngresarInstruccions(instruccions);
        log.info("=======================================================================");
        log.info("Resultado mapeo hacia clase Ingope -> " + objectMapper.writeValueAsString(ingope));
        log.info("=======================================================================");
        return ingope;
    }
    private void camposBase(JSONObject jsonObject) throws JsonProcessingException {
        Object idTransaccion = obtieneValor(jsonObject, null, "idProceso");
        Object idProceso = obtieneValor(jsonObject, null, "identificadorTransaccion");
        jsonObject.remove("identificadorTransaccion");
        ingope = objectMapper.readValue( jsonObject.toString(), Ingope.class);
        ingope.setOperacion(castString(obtieneValor(jsonObject, null, "operacionProducto")));
        ingope.setIdentificadorTransaccion(castLong(idTransaccion));
        ingope.setIdProceso(castString(idProceso));
        ingope.setMontoOperacionString(castBigDecimal(obtieneValor(jsonObject, null, "montoOperacion")));
        ingope.setNumeroDeOrden(castString(obtieneValor(jsonObject, null, "numeroOrden")));
        // para casos donde los canales no envíen unidad transadas
        ingope.setUnidadesTransadas(castBigDecimal(obtieneValor(jsonObject, null, "unidadesTransadas")));
        ingope.setCuentaInversion(castByte(obtieneValor(jsonObject, null, "cuentaDeInversion")));
    }
    private void camposMonedaExtranjera(JSONObject jsonObject) throws JsonProcessingException {
        OperacionMonedaExtranjera monedaExtranjera = new OperacionMonedaExtranjera();
        String divisa = "divisa";
        if (isNull(jsonObject, null, divisa)) {
            monedaExtranjera = objectMapper.readValue(jsonObject.get(divisa).toString(), OperacionMonedaExtranjera.class);
            ingope.setOperacionMonedaExtranjera(monedaExtranjera);
        }
    }
    private void camposAcciones(JSONObject jsonObject) throws JsonProcessingException {
        OperacionAcciones acciones = new OperacionAcciones();
        String acc = "acciones";
        if (isNull(jsonObject, null, acc)) {
            acciones = objectMapper.readValue(jsonObject.get(acc).toString(), OperacionAcciones.class);
            acciones.setCantidadOrden(castBigDecimal(obtieneValor(jsonObject, acc, "cantidadOrden")));
            acciones.setComision(castBigDecimal(obtieneValor(jsonObject, acc, "comision")));
            acciones.setNumeroCuentaCorriente(castString(obtieneValor(jsonObject, acc, "numeroCuentaCorriente")));
            acciones.setMontoLimite(castBigDecimal(obtieneValor(jsonObject, acc, "precioLimite")));
            acciones.setMontoOrden(castBigDecimal(obtieneValor(jsonObject, acc, "precioOrden")));
            ingope.setOperacionAcciones(acciones);
        }
    }
    private void camposFondosMutuos(JSONObject jsonObject) throws JsonProcessingException {
        OperacionFondosMutuos fondosMutuos = new OperacionFondosMutuos();
        String ffmm = "fondosMutuos";
        if (isNull(jsonObject, null, ffmm)) {
            fondosMutuos = objectMapper.readValue(jsonObject.get(ffmm).toString(), OperacionFondosMutuos.class);
            fondosMutuos.setIdOrigenFondo(castByte(obtieneValor(jsonObject, ffmm, "idOrigenFondo")));
            fondosMutuos.setCodigoPlanCuenta(castByte(obtieneValor(jsonObject, ffmm, "codigoPlanCuenta")));
            fondosMutuos.setNumeroCuenta(castBigDecimal(obtieneValor(jsonObject, ffmm, "numeroCuenta")));
            fondosMutuos.setIndicador18Q(castString(obtieneValor(jsonObject, ffmm, "indicador18q")));
            ingope.setOperacionFondosMutuos(fondosMutuos);
        }
    }
    /**
     * Función encargada de mapear las instrucciones de cobro y pago para la clase Ingope
     *
     * @param jsonObject
     */
    public void crearInstruccionesIngope(JSONObject jsonObject) throws JsonProcessingException {
        PIngresarInstruccions pIngresarInstruccions = new PIngresarInstruccions();
        List<PIngresarInstruccions.PIngresarInstruccion> instrucciones = new ArrayList<>();
        if (!jsonObject.isNull("listaInstruccion")) {
            JSONArray listaInstruccion = jsonObject.getJSONArray("listaInstruccion");
            for (Object element : listaInstruccion) {
                PIngresarInstruccions.PIngresarInstruccion instruccion = new PIngresarInstruccions.PIngresarInstruccion();
                JSONObject result = (JSONObject) element;
                result.remove("administrarBeneficiarios");
                //result.remove("reInversion");
                result.remove("beneficiario");
                result.remove("aprobarPagoaTerceros");
                if (!result.isNull("cuenta")) {
                    result.put("cuenta", result.getJSONObject("cuenta"));
                }
                if (!result.isNull("reInversion")) {
                    //
                    result.put("datosReinversion", result.getJSONObject("reInversion"));
                    if(!result.getJSONObject("datosReinversion").isNull("fondosMutuos")){
                        result.getJSONObject("datosReinversion").put("operacionFondosMutuos", result.getJSONObject("datosReinversion").getJSONObject("fondosMutuos"));
                        result.getJSONObject("datosReinversion").getJSONObject("operacionFondosMutuos")
                                .put("indicador18Q", result.getJSONObject("datosReinversion").getJSONObject("fondosMutuos").getString("indicador18q"));
                    }
                    // ver luego como quedaría para las otras operaciones
                    /*
                    result.put("operacionFondosMutuos", result.getJSONObject("reInversion").getJSONObject("fondosMutuos"));
                    result.put("operacionMonedaExtranjera", result.getJSONObject("reInversion").getJSONObject("divisa"));
                    result.put("operacionAcciones", result.getJSONObject("reInversion").getJSONObject("acciones"));*/
                }
                instruccion = objectMapper.readValue(result.toString(), PIngresarInstruccions.PIngresarInstruccion.class);
                instruccion.setMonto(castBigDecimal(obtieneValor(result,null,"monto")));
                instruccion.setCodigoModalidad(castBigDecimal(obtieneValor(result,null,"codigoModalidad")));
                instruccion.setBancoOrigen(castBigDecimal(obtieneValor(result,null, "bancoOrigen")).intValue());
                instruccion.setBancoDeposito(castBigDecimal(obtieneValor(result,null,"bancoDeposito")).intValue());
                instruccion.setRutEmpleadoIngreso(castBigDecimal(obtieneValor(result,null,"rutEmpleadoIngreso")).intValue());
                instruccion.setIndicadorAvanzaAutomatico(castString(obtieneValor(result, null, "indicadorAvanzaAutomaticoInstruccion")));
                instruccion.setIndicadorPagoTerceros(castString(obtieneValor(result, null,"aprobarPagoaTerceros")));
                instruccion.setIndicadorReinversion(castString(obtieneValor(result, null, "indicadorReinversion")));
                instruccion.setCanalIngreso(castString(obtieneValor(result, null, "canalIngreso")));
                instruccion.setNumeroCuentaCorriente(castString(obtieneValor(result, null,"numeroCuentaCorriente")));
                /*result.put("numerodelaInstruccion",
                            validaCampos(result,null,"numeroInstruccion","String"));*/
                instrucciones.add(instruccion);
            }
            pIngresarInstruccions.setpIngresarInstruccion(instrucciones);
            ingope.setPIngresarInstruccions(pIngresarInstruccions);
            log.info("=======================================================================");
            log.info("Metodo para mapear instrucciones en clase Ingope");
            log.info("=======================================================================");
            log.info("Instrucciones: " + objectMapper.writeValueAsString(pIngresarInstruccions.getPIngresarInstruccion()));
            log.info("=======================================================================");
        }
    }
    /**
     * Función encargada de castear valores según el tipo de objeto que entra
     *
     * @param jsonObject
     * @param objeto
     * @param llave
     * @param opcion
     */
    public Object validaCampos(JSONObject jsonObject, String objeto, String llave, String opcion) {
        Object value = null;
        try {
            value = obtieneValor(jsonObject, objeto, llave);
            switch (opcion) {
                case "Byte":
                    value = castByte(value);
                    break;
                case "String":
                    value = castString(value);
                    break;
                case "Short":
                    value = castShort(value);
                    break;
                case "Integer":
                    value = castInteger(value);
                    break;
                case "Float":
                    value = castFloat(value);
                    break;
                case "Long":
                    value = castLong(value);
                    break;
                case "BigDecimal":
                    value = castBigDecimal(value);
                    break;
                default:
                    value = 0;
            }
        } catch (Exception ex) {
            log.error("Ocurrio un error en Método validaCampos =>" + ex);
        }
        return value;
    }
    /**
     * Función encargada de obtener el objeto entregado para realizar el casteo correspondiente
     *
     * @param jsonObject
     * @param objeto
     * @param llave
     */
    public Object obtieneValor(JSONObject jsonObject, String objeto, String llave) {
        Object value = null;
        if (isNull(jsonObject, objeto, llave)) {
            if (objeto == null) {
                value = jsonObject.get(llave);
            } else {
                value = jsonObject.getJSONObject(objeto).get(llave);
            }
        }
        return value;
    }
    /**
     * Función encargada de validar campos nulos o vacíos para realizar el casteo correspondiente
     *
     * @param jsonObject
     * @param objeto
     * @param llave
     */
    public Boolean isNull(JSONObject jsonObject, String objeto, String llave) {
        Boolean valid = false;
        if (objeto == null) {
            if (!jsonObject.isNull(llave)) {
                valid = true;
            }
        } else {
            if (!jsonObject.getJSONObject(objeto).isNull(llave)) {
                valid = true;
            }
        }
        return valid;
    }
    /**
     * Función encargada de castera un Objeto a Byte
     *
     * @param objACastear
     * @return Integer
     */
    public Byte castByte(Object objACastear) {
        Byte value = 0;
        try {
            if (objACastear instanceof Byte) {
                value = Byte.valueOf(objACastear.toString());
            } else if (objACastear instanceof String) {
                if (("").equals(objACastear) || ((String) objACastear).isEmpty() || objACastear.equals("0.0")) {
                    objACastear = "0";
                }
                value = Byte.valueOf(objACastear.toString());
            } else if (objACastear instanceof Integer) {
                value = Byte.valueOf(objACastear.toString());
            }
        } catch (NumberFormatException e) {
            log.error("Ocurrio un error en castByte => ", e);
        }
        return value;
    }
    /**
     * Función encargada de castera un Objeto a String
     *
     * @param objACastear
     * @return String
     */
    public String castString(Object objACastear) {
        String value =null;
        try {
            if (objACastear != null && !objACastear.toString().equals("")) {
                value = objACastear.toString();
            }
        } catch (RuntimeException e) {
            log.error("Ocurrio un error en castString => ", e);
        }
        return value;
    }
    /**
     * Función encargada de castera un Objeto a Long
     *
     * @param objACastear
     * @return Short
     */
    public Short castShort(Object objACastear) {
        Short value = 0;
        try {
            if (objACastear instanceof Short) {
                value = Short.valueOf(objACastear.toString());
            } else if (objACastear instanceof String) {
                if (("").equals(objACastear) || ((String) objACastear).isEmpty() || objACastear.equals("0.0")) {
                    objACastear = "0";
                }
                value = Short.valueOf(objACastear.toString());
            } else if (objACastear instanceof Integer) {
                value = Short.valueOf(objACastear.toString());
            }
        } catch (NumberFormatException e) {
            log.error("Ocurrio un error en castShort => ", e);
        }
        return value;
    }
    /**
     * Función encargada de castera un Objeto a Float
     *
     * @param objACastear
     * @return Float
     */
    public Float castFloat(Object objACastear) {
        Float value = 0.0F;
        try {
            if (objACastear != null && !objACastear.equals("")) {
                value = Float.parseFloat(objACastear.toString());
            }
        } catch (NumberFormatException e) {
            log.error("Ocurrio un error en castFloat => ", e);
        }
        return value;
    }
}


