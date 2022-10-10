package model;
public class Orders {
    private int id;
    private int idClient;
    private int idProduct;
    private int noItems;

    @Override
    public String toString(){
        return "order id: "+id;
    }

    public int getId() {
        return id;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public int getNoItems() {
        return noItems;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public void setNoItems(int noItems) {
        this.noItems = noItems;
    }
}
