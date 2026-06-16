package helper;

import mx.puestoLidia.entity.Cliente;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;
import java.io.Serializable;
import java.util.List;

/**
 * ClienteHelper
 *
 * Capa intermedia entre ClienteBeanUI (presentación) y FacadeCliente (negocio)
 * Simplifica el código en el Bean delegando a la Facade
 *
 * Patrón: Helper/Delegate
 * - Implementa Serializable para que el Bean sea serializable
 * - Todos sus métodos delegan a ServiceFacadeLocator
 */
public class ClienteHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Guarda un nuevo cliente
     */
    public void guardarCliente(Cliente cliente) {
        ServiceFacadeLocator.getInstanceFacadeCliente().guardarCliente(cliente);
    }

    /**
     * Busca cliente por ID
     */
    public Cliente buscarClientePorID(Integer id) {
        return ServiceFacadeLocator.getInstanceFacadeCliente().buscarClientePorID(id);
    }

    /**
     * Busca cliente por teléfono (para validar unicidad)
     */
    public Cliente buscarClientePorTelefono(String telefono) {
        return ServiceFacadeLocator.getInstanceFacadeCliente().buscarClientePorTelefono(telefono);
    }

    /**
     * Modifica cliente existente
     */
    public void modificarCliente(Cliente cliente) {
        ServiceFacadeLocator.getInstanceFacadeCliente().modificarCliente(cliente);
    }

    /**
     * Obtiene todos los clientes
     */
    public List<Cliente> obtenerTodosClientes() {
        return ServiceFacadeLocator.getInstanceFacadeCliente().obtenerTodosClientes();
    }

    /**
     * Obtiene clientes con adeudo
     */
    public List<Cliente> obtenerClientesConAdeudo() {
        return ServiceFacadeLocator.getInstanceFacadeCliente().obtenerClientesConAdeudo();
    }

    public List<Cliente> buscarClientesPorNombre(String filtro) {
        return ServiceFacadeLocator.getInstanceFacadeCliente().buscarClientesPorNombre(filtro);
    }

    /**
     * Elimina un cliente
     */
    public void eliminarCliente(Cliente cliente) {
        ServiceFacadeLocator.getInstanceFacadeCliente().eliminarCliente(cliente);
    }

    /**
     * Actualiza adeudo de cliente
     */
    public void actualizarAdeudoCliente(Cliente cliente) {
        ServiceFacadeLocator.getInstanceFacadeCliente().actualizarAdeudoCliente(cliente);
    }
}