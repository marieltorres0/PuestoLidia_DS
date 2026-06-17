package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.Cliente;
import mx.puestoLidia.persistence.integration.ServiceLocator;
import java.util.List;

/**
 * DelegateCliente - Contiene lógica de negocio específica de Cliente
 */
public class DelegateCliente {

    public void guardarCliente(Cliente cliente) {
        if (cliente.getAdeudo() == null) {
            cliente.setAdeudo(java.math.BigDecimal.ZERO);
        }
        ServiceLocator.getInstanceClienteDAO().guardarCliente(cliente);
    }

    public Cliente buscarClientePorID(Integer idBuscar) {
        return ServiceLocator.getInstanceClienteDAO().buscarClientePorID(idBuscar);
    }

    public Cliente buscarClientePorTelefono(String telefono) {
        return ServiceLocator.getInstanceClienteDAO().buscarClientePorTelefono(telefono);
    }

    public void modificarCliente(Cliente clienteModificado) {
        ServiceLocator.getInstanceClienteDAO().modificarCliente(clienteModificado);
    }

    public List<Cliente> obtenerTodosClientes() {
        return ServiceLocator.getInstanceClienteDAO().obtenerTodosClientes();
    }

    public List<Cliente> obtenerClientesConAdeudo() {
        return ServiceLocator.getInstanceClienteDAO().obtenerClientesConAdeudo();
    }

    public List<Cliente> buscarClientesPorNombre(String filtro) {
        return ServiceLocator.getInstanceClienteDAO().buscarClientesPorNombre(filtro);
    }

    public void eliminarCliente(Cliente clienteEliminar) {
        ServiceLocator.getInstanceClienteDAO().eliminarCliente(clienteEliminar);
    }

    public void actualizarAdeudoCliente(Cliente cliente) {
        ServiceLocator.getInstanceClienteDAO().update(cliente);
    }
}