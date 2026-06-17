package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.Cliente;
import mx.puestoLidia.negocio.delegate.DelegateCliente;
import java.util.List;

/**
 * FacadeCliente - Patrón Facade para simplificar acceso a servicios de Cliente
 */
public class FacadeCliente {

    private final DelegateCliente delegateCliente;

    public FacadeCliente() {
        this.delegateCliente = new DelegateCliente();
    }

    public void guardarCliente(Cliente cliente) {
        delegateCliente.guardarCliente(cliente);
    }

    public Cliente buscarClientePorID(Integer idCliente) {
        return delegateCliente.buscarClientePorID(idCliente);
    }

    public Cliente buscarClientePorTelefono(String telefono) {
        return delegateCliente.buscarClientePorTelefono(telefono);
    }

    public void modificarCliente(Cliente cliente) {
        delegateCliente.modificarCliente(cliente);
    }

    public List<Cliente> obtenerTodosClientes() {
        return delegateCliente.obtenerTodosClientes();
    }

    public List<Cliente> obtenerClientesConAdeudo() {
        return delegateCliente.obtenerClientesConAdeudo();
    }

    public List<Cliente> buscarClientesPorNombre(String filtro) {
        return delegateCliente.buscarClientesPorNombre(filtro);
    }

    public void eliminarCliente(Cliente cliente) {
        delegateCliente.eliminarCliente(cliente);
    }

    public void actualizarAdeudoCliente(Cliente cliente) {
        delegateCliente.actualizarAdeudoCliente(cliente);
    }
}