package oose.dea.services.rest;

import oose.dea.dataaccess.Item;
import oose.dea.dataaccess.ItemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Tag("integrationtest")
@ExtendWith(MockitoExtension.class)
public class ItemRestServiceCDIIT {
    @Produces
    @Mock
    ItemDAO itemDAO;

    @InjectMocks
    ItemRestService itemRestService;

    @BeforeEach
    public void setup()
    {
        ArrayList<Item> items = new ArrayList<>() {{
            add(new Item("frik", "Vette hap", "Frikandel"));
        }};
        when(itemDAO.list()).thenReturn(items);
    }

    @Test
    public void findAll() {
        List<Item> items = itemRestService.findAll();
        assertEquals(1, items.size());
    }
}