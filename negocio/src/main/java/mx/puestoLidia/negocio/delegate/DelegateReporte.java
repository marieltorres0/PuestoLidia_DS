package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.integration.ServiceLocator;
import java.util.List;

/**
 * @author Eduardo Avitia Castro
 */
public class DelegateReporte {

    public List<Producto> obtenerReporteStockCritico() {
        return ServiceLocator.getInstanceReporteDAO().obtenerReporteStockCritico();
    }
}