package oose.dea.dataaccess;

import oose.dea.exceptions.NoImplementException;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Default
public class ItemJdbcDAO implements ItemDAO {
    private static Logger logger = Logger.getLogger(ItemJdbcDAO.class.getName());

    private static final String INSERT_INTO_ITEMS = "INSERT INTO items VALUES (NULL, ? , ? , ?)";
    private static final String SELECT_FROM_ITEMS = "SELECT * FROM items";

    private JdbcConnectionFactory jdbcConnectionFactory;

    @Inject
    public ItemJdbcDAO(JdbcConnectionFactory jdbcConnectionFactory) {
        this.jdbcConnectionFactory = jdbcConnectionFactory;
    }

    @Override
    public void add(Item entity) {
        try(Connection connection = jdbcConnectionFactory.create()) {
            insertItemsIntoDatabase(entity, connection);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    private void insertItemsIntoDatabase(Item entity, Connection connection) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_ITEMS)) {
            preparedStatement.setString(1, entity.getSku());
            preparedStatement.setString(2, entity.getCategory());
            preparedStatement.setString(3, entity.getTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void update(Item updatedEntity) {
        throw new NoImplementException();
    }

    @Override
    public void remove(Item entity) {
        throw new NoImplementException();
    }

    @Override
    public List<Item> list() {
        List<Item> items = new ArrayList<>();
        try (Connection connection = jdbcConnectionFactory.create()) {
            addItemsToList(items, connection);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return items;
    }

    private void addItemsToList(List<Item> items, Connection connection) {
        try (ResultSet rs = connection.prepareStatement(SELECT_FROM_ITEMS).executeQuery()) {
            while (rs.next()) {
                items.add(new Item(rs.getString("sku"), rs.getString("category"), rs.getString("title")));
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public Item find(int id) {
        throw new NoImplementException();
    }

}