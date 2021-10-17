package getir.controller;

import com.google.gson.Gson;
import getir.controller.payload.request.NewOrderRequest;
import getir.controller.payload.response.OrderDto;
import getir.service.IOrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OrderController.class)
class OrderControllerTest {
    private MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    private IOrderService orderService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach()
    public void setup()
    {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void afterTest()
    {
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void placeOrder() throws Exception {
        when(orderService.createNewOrder(any(NewOrderRequest.class))).thenReturn(any(OrderDto.class));

        this.mockMvc.perform(post("/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new NewOrderRequest())))
                .andExpect(status().isOk());
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
                /*.andExpect(jsonPath("$.data.name", is(createdBook.getName())))
                .andExpect(jsonPath("$.data.writer", is(createdBook.getWriter())))
                .andExpect(jsonPath("$.data.publishYear", is(createdBook.getPublishYear())));*/

        verify(orderService).createNewOrder(any(NewOrderRequest.class));
    }

    @Test
    void getOrder() throws Exception {
        String orderId = "o100L";
        OrderDto orderDto = OrderDto.builder().build();

        when(orderService.getOrderById(orderId)).thenReturn(orderDto);

        this.mockMvc.perform(get("/order/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(orderService).getOrderById(orderId);
    }
}
