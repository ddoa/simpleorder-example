package oose.dea.services.rest;

import com.sun.net.httpserver.HttpServer;
import org.jboss.resteasy.plugins.server.sun.http.HttpContextBuilder;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResourceFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Tag("integrationtest")
@ExtendWith(MockitoExtension.class)
public abstract class RestEasyIT {
    private Class type;
    private HttpContextBuilder contextBuilder;
    private HttpServer httpServer;

    @Mock
    ResourceFactory resourceFactory;

    public RestEasyIT(Class type) { this.type = type; }

    @BeforeEach
    public void setup() throws IOException {
        setupDependencies();
        when(resourceFactory.getScannableClass()).thenAnswer((Answer<Object>) invocationOnMock -> type);
        when(resourceFactory.createResource(any(HttpRequest.class), any(HttpResponse.class), any(ResteasyProviderFactory.class))).thenReturn(getServiceInstance());
        httpServer = HttpServer.create(new InetSocketAddress(8000), 10);
        contextBuilder = new HttpContextBuilder();
        contextBuilder.getDeployment().getResourceFactories().add(resourceFactory);
        contextBuilder.bind(httpServer);
        httpServer.start();
    }

    protected abstract Object getServiceInstance();

    protected abstract void setupDependencies();


    @AfterEach
    public void tearDown() {
        contextBuilder.cleanup();
        httpServer.stop(0);
    }
}
