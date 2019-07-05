package oose.dea.services.local;

import oose.dea.dataaccess.Item;
import oose.dea.dataaccess.ItemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocalItemServiceTest {
    @Mock
    ItemDAO itemDAO;

    @InjectMocks
    LocalItemService localItemService;

    private ArrayList<Item> items;

    @BeforeEach
    public void setUp() {
        items = new ArrayList<>() {{
            add(new Item("frik", "Vette hap", "Frikandel"));
        }};
        when(itemDAO.list()).thenReturn(items);
    }

    @Test
    public void findAll() {
        assertEquals(localItemService.findAll(), items);
    }

}