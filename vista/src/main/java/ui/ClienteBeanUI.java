package ui;


import mx.puestoLidia.entity.Cliente;
import helper.ClienteHelper;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ClienteBeanUI - Bean JSF @ViewScoped
 *
 * Responsabilidades:
 * - Gestionar el formulario de clientes en la vista
 * - Validar datos ingresados
 * - Delegar operaciones a ClienteHelper
 * - Comunicarse con el usuario via FacesMessages
 */
@Named("clienteBean")
@ViewScoped
public class ClienteBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;

    // ============ INYECCIÓN ============
    private ClienteHelper clienteHelper;

    // ============ ATRIBUTOS DEL FORMULARIO ============
    private Integer idCliente;
    private String nombre;
    private String telefono;
    private BigDecimal adeudo;

    // ============ ATRIBUTOS DE CONTROL ============
    private List<Cliente> listaClientes;
    private Cliente clienteSeleccionado;
    private boolean esEdicion = false;
    private String textoBusqueda;

    // ============ CONSTRUCTOR ============
    public ClienteBeanUI() {
        this.clienteHelper = new ClienteHelper();
    }

    // ============ VALIDACIONES ============

    /**
     * Valida que campos requeridos no estén vacíos
     */
    public boolean validarCamposVacios() {
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("Validación", "El nombre es requerido");
            return false;
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            mostrarError("Validación", "El teléfono es requerido");
            return false;
        }
        return true;
    }

    /**
     * Valida formato y reglas de negocio
     */
    public boolean validarDatos() {
        // Nombre
        if (nombre.length() > 50) {
            mostrarError("Validación", "El nombre no debe exceder 50 caracteres");
            return false;
        }


        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$")) {
            mostrarError("Validación", "El nombre solo puede contener letras y espacios (sin números)");
            return false;
        }

        // Teléfono
        if (!telefono.matches("^[0-9]*$")) {
            mostrarError("Validación", "El teléfono solo debe contener números");
            return false;
        }

        if (telefono.length() > 10) {
            mostrarError("Validación", "El teléfono no debe exceder 10 dígitos");
            return false;
        }

        // Teléfono único
        Cliente clienteExistente = clienteHelper.buscarClientePorTelefono(telefono);
        if (clienteExistente != null && !esEdicion) {
            mostrarError("Validación", "El teléfono ya está registrado");
            return false;
        }

        return true;
    }

    // ============ OPERACIONES CRUD ============

    /**
     * Registra nuevo cliente
     */
    public void registrarCliente() {
        if (!validarCamposVacios()) return;
        if (!validarDatos()) return;

        try {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(nombre.trim());
            nuevoCliente.setTelefono(telefono.trim());
            nuevoCliente.setAdeudo(BigDecimal.ZERO);

            clienteHelper.guardarCliente(nuevoCliente);
            mostrarMensaje("Éxito", "Cliente registrado correctamente");
            limpiarDatos();
            cargarTodosClientes();
        } catch (Exception e) {
            mostrarError("Error", "Error al registrar: " + e.getMessage());
        }
    }

    /**
     * Prepara cliente para edición
     */
    public void prepararModificacion(Integer id) {
        try {
            clienteSeleccionado = clienteHelper.buscarClientePorID(id);
            idCliente = clienteSeleccionado.getId();
            nombre = clienteSeleccionado.getNombre();
            telefono = clienteSeleccionado.getTelefono();
            adeudo = clienteSeleccionado.getAdeudo();
            esEdicion = true;
        } catch (Exception e) {
            mostrarError("Error", "Error al cargar: " + e.getMessage());
        }
    }

    /**
     * Modifica cliente
     */
    public void modificarCliente() {
        if (!validarCamposVacios()) return;
        if (!validarDatos()) return;

        try {
            clienteSeleccionado.setNombre(nombre.trim());
            clienteSeleccionado.setTelefono(telefono.trim());

            // ¡Esta es la línea que falta para que el adeudo se actualice!
            clienteSeleccionado.setAdeudo(this.adeudo);

            clienteHelper.modificarCliente(clienteSeleccionado);
            mostrarMensaje("Éxito", "Cliente modificado correctamente");
            limpiarDatos();
            esEdicion = false;
            cargarTodosClientes();
        } catch (Exception e) {
            mostrarError("Error", "Error al modificar: " + e.getMessage());
        }
    }

    /**
     * Prepara cliente para eliminación
     */
    public void prepararEliminacion(Integer id) {
        try {
            clienteSeleccionado = clienteHelper.buscarClientePorID(id);
            idCliente = clienteSeleccionado.getId();
            nombre = clienteSeleccionado.getNombre();
            telefono = clienteSeleccionado.getTelefono();
        } catch (Exception e) {
            mostrarError("Error", "Error al cargar: " + e.getMessage());
        }
    }

    /**
     * Elimina cliente
     */
    public void eliminarCliente() {
        try {
            clienteHelper.eliminarCliente(clienteSeleccionado);
            mostrarMensaje("Éxito", "Cliente eliminado correctamente");
            limpiarDatos();
            cargarTodosClientes();
        } catch (Exception e) {
            mostrarError("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    /**
     * Busca cliente por ID, nombre o teléfono.
     * Reglas:
     * - Si la entrada son 10 dígitos: buscar por teléfono
     * - Si la entrada es numérica y no tiene 10 dígitos: buscar por ID
     * - Si contiene letras: buscar por nombre (LIKE %filtro%)
     */
    public void buscarCliente() {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            mostrarMensaje("Info", "Ingrese ID, nombre o teléfono para buscar, o use 'Mostrar Todos' para ver todo.");
            return;
        }

        String criterio = textoBusqueda.trim();
        try {
            // Si son exactamente 10 dígitos, tratar como teléfono
            if (criterio.matches("^\\d{10}$")) {
                Cliente c = clienteHelper.buscarClientePorTelefono(criterio);
                if (c != null) {
                    if (listaClientes == null) listaClientes = new ArrayList<>();
                    listaClientes.clear();
                    listaClientes.add(c);
                } else {
                    mostrarMensaje("Info", "No se encontraron clientes con ese teléfono");
                }
                return;
            }

            // Si es numérico pero no 10 dígitos, intentar ID
            if (criterio.matches("^\\d+$")) {
                try {
                    Integer id = Integer.parseInt(criterio);
                    Cliente cliente = clienteHelper.buscarClientePorID(id);
                    if (cliente != null) {
                        if (listaClientes == null) listaClientes = new ArrayList<>();
                        listaClientes.clear();
                        listaClientes.add(cliente);
                    } else {
                        mostrarMensaje("Info", "No se encontró cliente con ese ID");
                    }
                    return;
                } catch (NumberFormatException nfe) {
                    mostrarError("Error", "ID inválido: " + nfe.getMessage());
                    return;
                }
            }

            // Sino, buscar por nombre
            listaClientes = clienteHelper.buscarClientesPorNombre(criterio);
            if (listaClientes == null || listaClientes.isEmpty()) {
                mostrarMensaje("Info", "No se encontraron clientes con ese nombre");
            }
        } catch (Exception e) {
            mostrarError("Error", "Error al buscar: " + e.getMessage());
        }
    }

    // ============ CARGA DE DATOS ============

    public void cargarTodosClientes() {
        try {
            listaClientes = clienteHelper.obtenerTodosClientes();
        } catch (Exception e) {
            mostrarError("Error", "Error al cargar: " + e.getMessage());
        }
    }

    public void cargarClientesConAdeudo() {
        try {
            listaClientes = clienteHelper.obtenerClientesConAdeudo();
        } catch (Exception e) {
            mostrarError("Error", "Error al cargar: " + e.getMessage());
        }
    }

    // ============ MÉTODOS AUXILIARES ============

    public void limpiarDatos() {
        idCliente = null;
        nombre = "";
        telefono = "";
        adeudo = null;
        clienteSeleccionado = null;
        textoBusqueda = "";
    }

    private void mostrarError(String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje));
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje));
    }

    // ============ GETTERS Y SETTERS ============
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public BigDecimal getAdeudo() { return adeudo; }
    public void setAdeudo(BigDecimal adeudo) { this.adeudo = adeudo; }

    public List<Cliente> getListaClientes() { return listaClientes; }
    public void setListaClientes(List<Cliente> listaClientes) { this.listaClientes = listaClientes; }

    public Cliente getClienteSeleccionado() { return clienteSeleccionado; }
    public void setClienteSeleccionado(Cliente clienteSeleccionado) { this.clienteSeleccionado = clienteSeleccionado; }

    public boolean isEsEdicion() { return esEdicion; }
    public void setEsEdicion(boolean esEdicion) { this.esEdicion = esEdicion; }

    public String getTextoBusqueda() { return textoBusqueda; }
    public void setTextoBusqueda(String textoBusqueda) { this.textoBusqueda = textoBusqueda; }
}