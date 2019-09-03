package oose.dea.dataaccess;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Tag("integrationtest")
@ExtendWith(MockitoExtension.class)
public class ItemJdbcDAOIT {
    public static final String ERROR = "Error";
    private static JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory();
    private Item item = new Item("sku", "cat", "title");

    @BeforeAll
    public static void prepare() throws SQLException {
        Connection connection = jdbcConnectionFactory.create();
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS items (" +
                "  id int(11) auto_increment primary key," +
                "  category varchar(255) DEFAULT NULL," +
                "  sku varchar(255) DEFAULT NULL," +
                "  title varchar(255) DEFAULT NULL" +
                ")";

        statement.executeUpdate(sql);
        statement.close();
        connection.close();
    }

    @Test
    public void whenTableIsEmptyListIsAlsoEmtpy()
    {
        ItemJdbcDAO itemJdbcDAO = new ItemJdbcDAO(jdbcConnectionFactory);
        assertEquals(0,itemJdbcDAO.list().size());
    }

    @Test
    public void afterAddingOneItemTheListHasSizeOne()
    {
        ItemJdbcDAO itemJdbcDAO = new ItemJdbcDAO(jdbcConnectionFactory);
        itemJdbcDAO.add(item);
        assertEquals(1,itemJdbcDAO.list().size());
    }

    @Test
    public void whenSqlExceptionIsThrownDuringAddTheMessageGetsLogged() throws SQLException, IllegalAccessException {
        Logger mockLogger = mock(Logger.class);
        FieldUtils.writeStaticField(ItemJdbcDAO.class, "logger", mockLogger, true);
        ItemJdbcDAO itemJdbcDAO = createItemJdbcDAOWithMockedDependencies();
        itemJdbcDAO.add(item);

        verify(mockLogger).severe(ERROR);
    }

    @Test
    public void whenSqlExceptionIsThrownDuringListTheMessageGetsLogged() throws SQLException, IllegalAccessException {
        Logger mockLogger = mock(Logger.class);
        FieldUtils.writeStaticField(ItemJdbcDAO.class, "logger", mockLogger, true);

        ItemJdbcDAO itemJdbcDAO = createItemJdbcDAOWithMockedDependencies();
        itemJdbcDAO.list();

        verify(mockLogger).severe(ERROR);
    }

    private ItemJdbcDAO createItemJdbcDAOWithMockedDependencies() throws SQLException {
        JdbcConnectionFactory mockConnectionFactory = mock(JdbcConnectionFactory.class);
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException(ERROR));
        when(mockConnectionFactory.create()).thenReturn(mockConnection);
        return new ItemJdbcDAO(mockConnectionFactory);
    }

    @Test
    public void findNotYetImplemented()
    {
        ItemJdbcDAO itemJdbcDAO = new ItemJdbcDAO(jdbcConnectionFactory);
        assertThrows(RuntimeException.class, () -> itemJdbcDAO.find(0));
    }

    @Test
    public void updateNotYetImplemented()
    {
        ItemJdbcDAO itemJdbcDAO = new ItemJdbcDAO(jdbcConnectionFactory);
        assertThrows(RuntimeException.class, () -> itemJdbcDAO.update(null));
    }

    @Test
    public void removeNotYetImplemented()
    {
        ItemJdbcDAO itemJdbcDAO = new ItemJdbcDAO(jdbcConnectionFactory);
        assertThrows(RuntimeException.class, () -> itemJdbcDAO.remove(null));
    }

    @AfterAll
    public static void shutdown() throws IOException {
        // adjust /tmp to c:/temp on Windows systems just like in the database.properties
        FileUtils.deleteDirectory(new File("/tmp/itemjdbcdaoit"));
    }
}