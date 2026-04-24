package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.delegate.DelegateInventario;
import java.util.List;

public class FacadeInventario {

    private final DelegateInventario delegateInventario;

    public FacadeInventario() {
        this.delegateInventario = new DelegateInventario();
    }

    public List<Producto> obtenerInventarioOrdenado() {
        return delegateInventario.obtenerInventarioOrdenado();
    }

    public List<Producto> buscarPorNombre(String filtro) {
        return delegateInventario.buscarPorNombre(filtro);
    }
}