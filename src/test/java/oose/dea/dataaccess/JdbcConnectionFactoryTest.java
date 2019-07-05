package oose.dea.dataaccess;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class JdbcConnectionFactoryTest {

    public static final String DEZE_CLASS_BESTAAT_NIET = "deze.class.bestaat.Niet";
    public static final String KAN_GEEN_PROPERTIES_LEZEN = "Kan geen properties lezen";
    private static final String USER = "sa";
    private static final String PASS = "";
    private static final String URL = "jdbc:h2";
    private static final String DRIVER = "org.h2.Driver";

    @Mock Logger mockLogger;
    @Mock Properties properties;

    @Test
    public void createWithExistingPropertyFile() {
        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory();
        assertNotNull(jdbcConnectionFactory.create());
    }

    @Test
    public void whenDriverCannotBeFoundAnExceptionIsLogged() throws IllegalAccessException {
        when(properties.getProperty("driver")).thenReturn(DEZE_CLASS_BESTAAT_NIET);
        FieldUtils.writeStaticField(JdbcConnectionFactory.class, "logger", mockLogger, true);

        new JdbcConnectionFactory(properties);

        verify(mockLogger).severe(DEZE_CLASS_BESTAAT_NIET);
    }

    @Test
    public void whenPropertiesCannotBeLoadedAnExceptionIsLogged() throws IOException, IllegalAccessException {
        doThrow(new IOException(KAN_GEEN_PROPERTIES_LEZEN)).when(properties).load(any(InputStream.class));
        FieldUtils.writeStaticField(JdbcConnectionFactory.class, "logger", mockLogger, true);

        new JdbcConnectionFactory(properties);

        verify(mockLogger).severe(KAN_GEEN_PROPERTIES_LEZEN);
    }

    @Test
    public void whenAConnectionCannotBeCreatedASQLExcepionIsLogged() throws IllegalAccessException {
        when(properties.getProperty("user")).thenReturn(USER);
        when(properties.getProperty("password")).thenReturn(PASS);
        when(properties.getProperty("databaseurl")).thenReturn(URL);
        when(properties.getProperty("driver")).thenReturn(DRIVER);
        FieldUtils.writeStaticField(JdbcConnectionFactory.class, "logger", mockLogger, true);

        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(properties);
        jdbcConnectionFactory.create();
        verify(mockLogger).severe(anyString());

    }
}