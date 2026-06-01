package helper;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.delegate.DelegateVenta;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaHelper {

    public Producto buscarProducto(String textoBusqueda) {
        List<Producto> resultados = ServiceLocator.getInstanceProductoDAO().findByOneParameter(textoBusqueda, "idProducto");
        if (resultados != null && !resultados.isEmpty()) return resultados.get(0);

        List<Producto> resultadosNombre = ServiceLocator.getInstanceProductoDAO().findByOneParameter(textoBusqueda, "nombre");
        if (resultadosNombre != null && !resultadosNombre.isEmpty()) return resultadosNombre.get(0);

        return null;
    }

    public void procesarVenta(List<ItemVenta> listaCompra, BigDecimal total, BigDecimal monto, BigDecimal cambio, boolean esCredito) throws Exception {
        DelegateVenta delegate = new DelegateVenta();
        delegate.registrarAltaVenta(listaCompra, total, monto, cambio, esCredito);
    }

    // Método para buscar por prefijo de ID (Ej. "11" para refrescos)
    public List<Producto> buscarProductosPorPrefijo(String prefijo) {
        List<Producto> todos = ServiceLocator.getInstanceProductoDAO().findAll();
        List<Producto> filtrados = new ArrayList<>();

        for (Producto p : todos) {
            if (p.getIdProducto().startsWith(prefijo)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }
}