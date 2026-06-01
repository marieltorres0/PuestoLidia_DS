package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.negocio.facade.FacadeVenta;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.math.BigDecimal;
import java.util.List;

public class DelegateVenta {

    public void registrarAltaVenta(List<ItemVenta> detalles, BigDecimal total, BigDecimal monto, BigDecimal cambio, boolean esCredito) throws Exception {
        FacadeVenta facade = ServiceFacadeLocator.getInstanceFacadeVenta();
        facade.procesarAltaVenta(detalles, total, monto, cambio, esCredito);
    }
}