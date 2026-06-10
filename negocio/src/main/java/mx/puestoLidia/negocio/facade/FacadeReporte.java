package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.delegate.DelegateReporte;
import java.util.List;

/**
 * @author Eduardo Avitia Castro
 */
public class FacadeReporte {

    private DelegateReporte delegateReporte;

    public FacadeReporte() {
        this.delegateReporte = new DelegateReporte();
    }

    public List<Producto> obtenerReporteStockCritico() {
        return delegateReporte.obtenerReporteStockCritico();
    }
}