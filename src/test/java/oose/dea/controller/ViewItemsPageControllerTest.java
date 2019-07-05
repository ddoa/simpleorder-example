package oose.dea.controller;

import oose.dea.dataaccess.Item;
import oose.dea.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ViewItemsPageControllerTest {
    @Mock
    ItemService itemService;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    private ViewItemsPageController viewPageController;

    private ArrayList<Item> items;

    @BeforeEach
    public void setup()
    {
        items = new ArrayList<>() {{
            add(new Item("frik", "Vette hap", "Frikandel"));
        }};
        when(itemService.findAll()).thenReturn(items);
        when(httpServletRequest.getRequestDispatcher("viewItems.jsp")).thenReturn(requestDispatcher);
    }

    @Test
    public void doGet() throws Exception {
        viewPageController.doGet(httpServletRequest, httpServletResponse);
        verify(httpServletRequest).setAttribute("all", items);
        verify(requestDispatcher).forward(httpServletRequest, httpServletResponse);
    }

}