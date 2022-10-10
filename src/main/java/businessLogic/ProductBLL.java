package businessLogic;

import dataAccess.ProductDAO;
import model.Product;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;


/**
 * The class Product BL L
 */
public class ProductBLL {
    private final ProductDAO productDAO;

    /**
     *
     * It is a constructor.
     *
     */
    public ProductBLL() {

        productDAO = new ProductDAO();
    }


    /**
     *
     * Find product by identifier
     *
     * @param id  the id
     * @return Product
     */
    public Product findProductById(int id) {

        Product product = productDAO.findById(id);
        if (product == null) {
            throw new NoSuchElementException("The product with id = " + id + " was not found!");
        }
        return product;
    }

    /**
     *
     * Delete product GUI
     *
     */
    public void deleteProductGUI() {

        List < Product > ProductList = productDAO.findAll();
        JComboBox ProductBox = new JComboBox(ProductList.toArray());
        Object[] message = {
                "Choose which product you want to remove:", ProductBox
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Delete product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String x = Objects.requireNonNull(ProductBox.getSelectedItem()).toString();
            Product Product = this.findProductById(Integer.parseInt(String.valueOf(x.charAt(0))));
            productDAO.delete(Product.getId());
            showMessageDialog(null,"Product deleted successfully");
        }
    }

    /**
     *
     * Add product GUI
     *
     */
    public void addProductGUI() {

        List < Product > productList = productDAO.findAll();
        JTextField name = new JTextField();
        JTextField stock = new JTextField();
        JTextField price = new JTextField();
        Object[] message = {
                "Name:", name,
                "No. of items in stock:", stock,
                "Price per unit:", price
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Add new product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (!name.getText().isBlank() && !stock.getText().isBlank() && !price.getText().isBlank()) {
                Product product = new Product();
                product.setName(name.getText());
                product.setPrice(Float.parseFloat(price.getText()));
                product.setStock(Integer.parseInt(stock.getText()));
                product.setId(productList.get(productList.size() - 1).getId() + 1);
                productDAO.insert(product);
                showMessageDialog(null, "Product added successfully");
            } else {
                showMessageDialog(null, "Please complete all fields!");
            }
        }
    }

    /**
     *
     * Edit stock
     *
     * @param sold  the sold
     * @param product  the product
     */
    public void editStock(int sold, Product product){

        if(product.getStock()>=sold){
            productDAO.delete(product.getId());
            product.setStock(product.getStock()-sold);
            productDAO.insert(product);
        }
    }

    /**
     *
     * Update product GUI
     *
     */
    public void updateProductGUI(){

        List <Product> productList = productDAO.findAll();
        JComboBox productBox = new JComboBox(productList.toArray());
        JTextField name = new JTextField();
        JTextField stock = new JTextField();
        JTextField price = new JTextField();
        Object[] message = {
                "Choose product:", productBox,
                "Name:", name,
                "No. items in stock:", stock,
                "Price per unit:",price
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Edit product data", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ProductBLL productBLL = new ProductBLL();
            String x = productBox.getSelectedItem().toString();
            Product product = productBLL.findProductById(Integer.parseInt(String.valueOf(x.charAt(0))));
            productDAO.delete(product.getId());
            if(name.getText().isBlank() && stock.getText().isBlank() && price.getText().isBlank()){
                showMessageDialog(null,"No changes saved.");
            }
            else{
                if (!name.getText().isBlank())
                    product.setName(name.getText());
                if (!stock.getText().isBlank())
                    product.setStock(Integer.parseInt(stock.getText()));
                if(!price.getText().isBlank())
                    product.setPrice(Float.parseFloat(price.getText()));
                productDAO.insert(product);
            }
        }
    }

}
