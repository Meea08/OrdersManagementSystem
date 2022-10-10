package dataAccess;
import connection.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static connection.ConnectionFactory.close;
/**
 * The class Abstract DAO<T>
 *     T can be any Java Model Class that is mapped to the data base, and has the same
 *     name as the table and the same instance variables and data types as the table fields.
 */
public class AbstractDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private Class<T> type;

    /**
     *
     * This is a constructor
     * @return public
     */
    public AbstractDAO(){
        this.type = (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     *
     * @param field the field that has to respect the priority the user wants to search for
     * @return a String representing the select query
     */
    private @NotNull String createSelectQuery(String field){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" WHERE ").append(field).append(" =? ");
        return sb.toString();
    }

    /**
     *
     * @return a String representing the select all query
     */
    private @NotNull String createSelectAllQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append("FROM ");
        sb.append(type.getSimpleName().toLowerCase());
        return sb.toString();
    }

    /**
     *
     * @return List of T objects
     */
    public List<T> findAll() {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
        return null;
    }

    /**
     *
     * @param id the id a Object is searched for
     * @return the Object with id equals to the parameter
     */
    public T findById(int id) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
        return null;
    }

    /**
     *
     * @param resultSet
     * @return list of T objects, created from the resultSet
     */
    private List<T> createObjects(ResultSet resultSet) {

        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (Constructor constructor : ctors) {
            ctor = constructor;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @return a String
     */
    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" VALUES ( ");
        return sb.toString();
    }

    /**
     *
     * Insert
     *
     * @param t  the t
     */
    public void insert(T t) {

        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder query = new StringBuilder(createInsertQuery());
        fields[0].setAccessible(true);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(t);
                if (value.getClass().getSimpleName().equals("String")) {
                    value = "'" + value + "'";
                }
                query.append(value);
                query.append(" , ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query.deleteCharAt(query.length() - 1);
        query.deleteCharAt(query.length() - 1);
        query.append(") ;");
        String queryString = String.valueOf(query);
        Connection connection = null;
        PreparedStatement statementUpdate = null;
        try {
            connection = ConnectionFactory.getConnection();
            statementUpdate = connection.prepareStatement(queryString);
            statementUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(statementUpdate);
            ConnectionFactory.close(connection);
        }
    }
    /**
     *
     * @param id
     * @return
     */
    public String createDeleteQuery(int id){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" WHERE id = ");
        sb.append(id);
        return sb.toString();
    }

    /**
     *
     * Delete
     *
     * @param id  the id
     */
    public void delete(int id){

        StringBuilder query = new StringBuilder(createDeleteQuery(id));
        Connection connection = null;
        PreparedStatement statementUpdate = null;
        try {
            connection = ConnectionFactory.getConnection();
            statementUpdate = connection.prepareStatement(query.toString());
            statementUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(statementUpdate);
            ConnectionFactory.close(connection);
        }
    }
    private String createUpdateQueryBeginning() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(type.getSimpleName().toLowerCase());

        sb.append(" SET ");
        return sb.toString();
    }
    public void update(T t, String field) {
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder query = new StringBuilder(createUpdateQueryBeginning());
        fields[0].setAccessible(true);
        Object idValue = null;
        try {
            idValue = fields[0].get(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int id = (Integer) idValue;
        T updatedField = findById(id);
        if( updatedField != null){
            int i=1;
            while (i<fields.length){
                fields[i].setAccessible(true);
                query.append(fields[i].getName()).append(" = ");

                try {
                    Object value = fields[i].get(t);
                    String typeValue = value.getClass().getSimpleName();
                    if(typeValue.equals("String")){
                        value = "'" + value + "'";
                    }
                    query.append(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                query.append(", ");
                i++;
            }
        }
        query.deleteCharAt(query.length()-1);
        query.deleteCharAt(query.length()-1);


        query.append(" WHERE " + field + " = ");
        query.append(idValue);

        String queryString = String.valueOf(query);
        Connection connection = null;
        PreparedStatement statementUpdate = null;

        try {
            connection = ConnectionFactory.getConnection();
            statementUpdate = connection.prepareStatement(queryString);
            statementUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(statementUpdate);
            ConnectionFactory.close(connection);
        }
    }
    /**
     *
     * @param objects
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public JTable makeTable(List<T> objects) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        if(objects!=null && objects.size()>0){
            String[] columnNames = new String[objects.get(0).getClass().getDeclaredFields().length];
            int i=0;
            for(Field field : objects.get(0).getClass().getDeclaredFields()){
                String fieldName = field.getName();
                columnNames[i]=fieldName;
                i++;
            }
            Object[][] tableData = new Object[objects.size()+1][i];
            int j=0;
            for(T object : objects){
                int k=0;
                for(Field field : object.getClass().getDeclaredFields()){
                    String fieldName = field.getName();
                    PropertyDescriptor propertyDescriptor;
                    propertyDescriptor = new PropertyDescriptor(fieldName,object.getClass());
                    Method method = propertyDescriptor.getReadMethod();
                    tableData[j][k]=method.invoke(object);
                    k++;
                }
                j++;
            }
            return new JTable(tableData,columnNames);
        }
        return new JTable(1,0);
    }
}
