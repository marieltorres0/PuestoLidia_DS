package helper;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;
import java.io.Serializable;
import java.util.List;

/**
 * @author Eduardo Avitia Castro
 */
public class ReporteHelper implements Serializable {

    public List<Producto> obtenerReporteStockCritico() {
        return ServiceFacadeLocator.getInstanceFacadeReporte().obtenerReporteStockCritico();
    }
}