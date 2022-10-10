package businessLogic;
import dataAccess.ClientDAO;
import model.Client;
import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;
import static javax.swing.JOptionPane.showMessageDialog;

public class ClientBLL {
    private final ClientDAO clientDAO;
    private List < Client > clientList;
    public ClientBLL() {
        clientDAO = new ClientDAO();
        clientList = clientDAO.findAll();
    }

    public void addClient(Client client) {
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).getEmail().equals(client.getEmail())) {
                showMessageDialog(null, "This email address already belongs to a client");
                return;
            }
        }
        clientDAO.insert(client);
        showMessageDialog(null, "Client added successfully");
    }
    public void addClientGUI() {
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        Object[] message = {
                "Name:", name,
                "Email address:", email
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Add new client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (!name.getText().isBlank() && !email.getText().isBlank()) {
                Client client = new Client();
                client.setName(name.getText());
                client.setEmail(email.getText());
                client.setId(clientList.get(clientList.size() - 1).getId() + 1);
                ClientBLL clientBLL = new ClientBLL();
                clientBLL.addClient(client);
            } else {
                showMessageDialog(null, "Please complete all fields!");
            }
        }
    }
    public void deleteClientGUI() {
        List < Client > clientList = clientDAO.findAll();
        JComboBox clientsBox = new JComboBox(clientList.toArray());
        Object[] message = {
                "Choose which client you want to remove:",
                clientsBox
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Delete client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ClientBLL clientBLL = new ClientBLL();
            String x = clientsBox.getSelectedItem().toString();
            Client client = clientBLL.findClientById(Integer.parseInt(String.valueOf(x.charAt(0))));
            clientDAO.delete(client.getId());
            showMessageDialog(null,"Client deleted successfully");
        }
    }
    public Client findClientById(int id) {
        Client client = clientDAO.findById(id);
        if (client == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return client;
    }
    public void editClientGUI() {
        List < Client > clientList = clientDAO.findAll();
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JComboBox clientsBox;
        clientsBox = new JComboBox(clientList.toArray());
        Object[] message = {
                "Choose client:", clientsBox,
                "Name:", name,
                "Email address:", email
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Edit client data", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ClientBLL clientBLL = new ClientBLL();
            String x = clientsBox.getSelectedItem().toString();
            Client client = clientBLL.findClientById(Integer.parseInt(String.valueOf(x.charAt(0))));
            clientDAO.delete(client.getId());
            if(name.getText().isBlank() && name.getText().isBlank()){
                showMessageDialog(null,"No changes saved.");
            }
            else{
                if (!name.getText().isBlank())
                    client.setName(name.getText());
                if (!email.getText().isBlank())
                    client.setEmail(email.getText());
                clientDAO.insert(client);
            }
        }
    }
}