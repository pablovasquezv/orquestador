package cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.impl;

import cl.banchile.bchrest.administra.ingreso.operaciones.routes.application.transaccion.external.workflowengine.EjecutaBizagiWorkflowengine;
import org.springframework.beans.factory.annotation.Value;
import org.tempuri.CreateCases;
import org.tempuri.CreateCasesResponse;
import org.tempuri.WorkflowEngineSOA;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Pablo
 *
 */

@Slf4j
@Service
public class OperacionEjecutaBizagiWorkflowengine implements EjecutaBizagiWorkflowengine {

    private WorkflowEngineSOA service;

    public OperacionEjecutaBizagiWorkflowengine(@Value("${service.bizagi.url}") String url) {
        try {
            this.service = new WorkflowEngineSOA(new URL(url));
        } catch (MalformedURLException e) {
            log.error("Ocurrió un Error en constructor de OperacionEjecutaBizagiWorkflowengine" + e);
        }
    }

    @Override
    public CreateCasesResponse.CreateCasesResult createCases(CreateCases.CasesInfo casesInfo) {
        return this.service.getWorkflowEngineSOASoap().createCases(casesInfo);
    }

}