package businessLogic;
import dataAccess.ClientDAO;
import dataAccess.OrdersDAO;
import dataAccess.ProductDAO;
import model.Client;
import model.Orders;
import model.Product;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;

import static javax.swing.JOptionPane.showMessageDialog;


/**
 * The class Orders BLL
 */
public class OrdersBLL {
    private OrdersDAO ordersDAO;


    /**
     *
     * It is a constructor.
     *
     */
    public OrdersBLL() {

        ordersDAO = new OrdersDAO();
    }

    /**
     *
     * Find order by identifier
     *
     * @param id  the id
     * @return Orders
     */
    public Orders findOrderById(int id) {

        Orders order = ordersDAO.findById(id);
        if (order == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return order;
    }

    /**
     *
     * Add order
     *
     * @param client  the client
     * @param product  the product
     * @param number  the number
     */
    public void addOrder(Client client, Product product, int number) {

        ProductBLL productBLL = new ProductBLL();
        List < Orders > ordersList = ordersDAO.findAll();
        if (number > product.getStock())
            showMessageDialog(null, new StringBuilder().append("Not enough items in stock. We only have ").append(product.getStock()).append(" units available").toString());
        else {
            productBLL.editStock(number,product);
            Orders order = new Orders();
            order.setId(ordersList.size()+1);
            order.setNoItems(number);
            order.setIdClient(client.getId());
            order.setIdProduct(product.getId());
            ordersDAO.insert(order);
            showMessageDialog(null, "Order placed successfully");
        }
    }

    /**
     *
     * Add order GUI
     *
     */
    public void addOrderGUI() {

        ClientDAO clientDAO = new ClientDAO();
        List < Client > clientList = clientDAO.findAll();
        ProductDAO productDAO = new ProductDAO();
        List < Product > productList = productDAO.findAll();
        JComboBox client = new JComboBox(clientList.toArray());
        JComboBox product = new JComboBox(productList.toArray());
        JTextField noItems = new JTextField();
        Object[] message = {
                "Client:", client,
                "Product:", product,
                "No. of items:", noItems
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Place new order", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (!noItems.getText().isBlank()) {
                ProductBLL productBLL = new ProductBLL();
                Product product1 = new Product();
                String x = product.getSelectedItem().toString();
                product1 = productBLL.findProductById(Integer.parseInt(String.valueOf(x.charAt(0))));
                ClientBLL clientBLL = new ClientBLL();
                new Client();
                Client client1;
                x = client.getSelectedItem().toString();
                client1 = clientBLL.findClientById(Integer.parseInt(String.valueOf(x.charAt(0))));
                OrdersBLL ordersBLL = new OrdersBLL();
                ordersBLL.addOrder(client1, product1, Integer.parseInt(noItems.getText()));
            } else {
                showMessageDialog(null, "Please complete all fields!");
            }
        }
    }

    /**
     *
     * Delete order GUI
     *
     */
    public void deleteOrderGUI() {

        List < Orders > ordersList = ordersDAO.findAll();
        JComboBox ordersBox = new JComboBox(ordersList.toArray());
        Object[] message = {
                "Choose which client you want to remove:",
                ordersBox
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Delete order", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ClientBLL clientBLL = new ClientBLL();
            String x = ordersBox.getSelectedItem().toString();
            Orders orders = this.findOrderById(Integer.parseInt(String.valueOf(x.charAt(10))));
            ordersDAO.delete(orders.getId());
        }
    }
}
