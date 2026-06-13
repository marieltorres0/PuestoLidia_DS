package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.ItemVentaId;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateItemVenta {
    // CRUD BÁSICO
    public void guardarItemVenta(ItemVenta itemVentaGuardar){
        ServiceLocator.getInstanceItemVentaDAO().guardarItemVenta(itemVentaGuardar);
    }

    public void modificarItemVenta(ItemVenta itemVentaModificar){
        ServiceLocator.getInstanceItemVentaDAO().modificarItemVenta(itemVentaModificar);
    }

    public void eliminarItemVenta(ItemVenta itemVentaEliminar){
        ServiceLocator.getInstanceItemVentaDAO().eliminarItemVenta(itemVentaEliminar);
    }

    // consulta especifica
    public List<ItemVenta> buscarItemsPorVenta(int idVenta){
        return ServiceLocator.getInstanceItemVentaDAO().buscarItemsPorVenta(idVenta);
    }
}
