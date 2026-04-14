import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import java.util.List;

public class testDAO {
    public static void main(String[] args){
        System.out.println("--- TESTING PRODUCTOS ---");
        List<Producto> productos = ServiceLocator.getInstanceProductoDAO().findAll();

        if (productos.isEmpty()){
            System.out.println("--- NO HAY PRODUCTOS REGISTRADOS ---");
        } else {
            for(Producto p : productos){
                System.out.println("PRODUCTO: " + p.getNombre() +
                        "\n Código: " + p.getIdProducto() +
                        "\n Precio: " + p.getPrecio() +
                        "\n Cantidad: " + p.getCantidad() +
                        "\n Umbral: " + p.getUmbral());
            }
        }
    }
}
