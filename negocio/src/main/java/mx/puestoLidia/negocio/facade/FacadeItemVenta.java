package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.ItemVentaId;
import mx.puestoLidia.negocio.delegate.DelegateItemVenta;

import java.util.List;

public class FacadeItemVenta {

    private final DelegateItemVenta delegateItemVenta;

    public FacadeItemVenta() {
        this.delegateItemVenta = new DelegateItemVenta();
    }

    public void guardarItemVenta(ItemVenta itemVentaGuardar) {
        delegateItemVenta.guardarItemVenta(itemVentaGuardar);
    }

    public void modificarItemVenta(ItemVenta itemVentaModificar) {
        delegateItemVenta.modificarItemVenta(itemVentaModificar);
    }

    public void eliminarItemVenta(ItemVenta itemVentaEliminar) {
        delegateItemVenta.eliminarItemVenta(itemVentaEliminar);
    }

    public List<ItemVenta> buscarItemsPorVenta(int idVenta) {
        return delegateItemVenta.buscarItemsPorVenta(idVenta);
    }
}