package helper;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.util.List;

public class ItemVentaHeper {
    // CRUD BASICO
    public void guardarItemVenta(ItemVenta iv){
        ServiceFacadeLocator.getInstanceFacadeItemVenta().guardarItemVenta(iv);
    }

    public void modificarItemVenta(ItemVenta ivModificar){
        ServiceFacadeLocator.getInstanceFacadeItemVenta().modificarItemVenta(ivModificar);
    }

    public void eliminarItemventa(ItemVenta ivEliminar){
        ServiceFacadeLocator.getInstanceFacadeItemVenta().eliminarItemVenta(ivEliminar);
    }

    public List<ItemVenta> buscarItemsPorventa(int idVenta){
        return ServiceFacadeLocator.getInstanceFacadeItemVenta().buscarItemsPorVenta(idVenta);
    }
}
