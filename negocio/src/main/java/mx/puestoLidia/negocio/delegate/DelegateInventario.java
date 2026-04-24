package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.integration.ServiceLocator;
import java.util.List;


public class DelegateInventario {

    // Obtener todos los productos ordenados
    public List<Producto> obtenerInventarioOrdenado() {
        return ServiceLocator.getInstanceInventarioDAO().obtenerInventarioOrdenado();
    }

    // Buscar productos por coincidencia de nombre
    public List<Producto> buscarPorNombre(String filtro) {
        return ServiceLocator.getInstanceInventarioDAO().buscarPorNombre(filtro);
    }
}