package presentation;

import businessLogic.ClientBLL;
import businessLogic.OrdersBLL;
import businessLogic.ProductBLL;
import dataAccess.ClientDAO;
import dataAccess.OrdersDAO;
import dataAccess.ProductDAO;
import model.Client;
import model.Orders;
import model.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

@SuppressWarnings("Annotation")
public class View extends JFrame {

    private JPanel panel1;
    private JLabel welcomeMessage1;
    private JLabel welcomeMessage2;
    private JButton manageClientsButton;
    private JButton manageProductsButton;
    private JButton manageOrdersButton;
    private JTable clientsDataTable;
    private JButton newClientButton;
    private JButton deleteClientButton;
    private JButton editClientButton;
    private JButton showClientsButton;
    private JTabbedPane tabs;
    private JButton addNewProductButton;
    private JButton showProductsButton;
    private JButton deleteProductButton;
    private JButton editProductButton;
    private JButton addNewOrderButton;
    private JButton backButton1;
    private JButton backButton2;
    private JButton backButton3;
    private JButton showOrdersButton;
    private JButton deleteOrderButton;

    public View(){
        tabs.addComponentListener(new ComponentAdapter() {
        });

        manageClientsButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                tabs.setSelectedIndex(1);
            }
        });
        manageProductsButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                tabs.setSelectedIndex(2);
            }
        });
        manageOrdersButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                tabs.setSelectedIndex(3);
            }
        });
        backButton1.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                tabs.setSelectedIndex(0);
            }
        });
        backButton2.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                tabs.setSelectedIndex(0);
            }
        });
        backButton3.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                tabs.setSelectedIndex(0);
            }
        });
        newClientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientBLL clientBLL = new ClientBLL();
                clientBLL.addClientGUI();
            }
        });
        deleteClientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientBLL clientBLL = new ClientBLL();
                clientBLL.deleteClientGUI();
            }
        });

        showClientsButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientDAO clientDAO = new ClientDAO();
                List<Client> clientList = clientDAO.findAll();
                clientsDataTable = new JTable();
                try {
                    clientsDataTable = clientDAO.makeTable(clientList);
                } catch (IntrospectionException | InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                showMessageDialog(null,new JScrollPane(clientsDataTable));
            }
        });

        showProductsButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductDAO productDAO = new ProductDAO();
                List<Product> productList = productDAO.findAll();

                JTable productsDataTable = new JTable();
                try {
                    productsDataTable = productDAO.makeTable(productList);
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                showMessageDialog(null,new JScrollPane(productsDataTable));
            }
        });
        showOrdersButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrdersDAO ordersDAO = new OrdersDAO();
                List<Orders> ordersList = ordersDAO.findAll();
                JTable ordersDataTable = new JTable();
                try {
                    ordersDataTable = ordersDAO.makeTable(ordersList);
                } catch (IntrospectionException | InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                showMessageDialog(null, new JScrollPane(ordersDataTable));
            }
        });
        addNewProductButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.addProductGUI();
            }
        });
        addNewOrderButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrdersBLL ordersBLL = new OrdersBLL();
                ordersBLL.addOrderGUI();
            }
        });
        deleteOrderButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrdersBLL ordersBLL = new OrdersBLL();
                ordersBLL.deleteOrderGUI();
            }
        });
        deleteProductButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.deleteProductGUI();
            }
        });
        editClientButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientBLL clientBLL = new ClientBLL();
                clientBLL.editClientGUI();
            }
        });
        editProductButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductBLL productBLL = new ProductBLL();
                productBLL.updateProductGUI();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Orders Management by Salomeea Muresan");
        frame.setSize(700,500);
        frame.setContentPane(new View().tabs);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700,300);
    }
}