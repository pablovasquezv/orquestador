package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine;

import org.tempuri.CreateCases;
import org.tempuri.CreateCasesResponse;

/**
 * @author Pablo
 *
 */

public interface EjecutaBizagiWorkflowengine {
    CreateCasesResponse.CreateCasesResult createCases(CreateCases.CasesInfo casesInfo);
}