package connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The class Connection factory
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/ordersmanagement";
    private static final String USER ="root";
    private static final String PASS ="abcABC123!@#";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     *
     * It is a constructor.
     *
     */
    private ConnectionFactory(){

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Create connection
     *
     * @return Connection
     */
    private Connection createConnection() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     *
     * Gets the connection
     *
     * @return the connection
     */
    public static Connection getConnection() {

        return singleInstance.createConnection();
    }


    /**
     *
     * Close
     *
     * @param connection  the connection
     */
    public static void close(Connection connection) {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }


    /**
     *
     * Close
     *
     * @param statement  the statement
     */
    public static void close(Statement statement) {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }


    /**
     *
     * Close
     *
     * @param resultSet  the result set
     */
    public static void close(ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }

}
