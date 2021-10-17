package getir.controller;

import com.google.gson.Gson;
import getir.controller.payload.request.CustomerAddRequest;
import getir.controller.payload.response.CustomerDto;
import getir.controller.payload.response.OrderDto;
import getir.service.ICustomerService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CustomerController.class)
class CustomerControllerTest {
    private static final String EMAIL = "test@gmail.com";
    private static final String FIRST_NAME = "Test";
    private static final String PHONE = "+905********";
    private static final String ADDRESS = "Test Address";

    private MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    private ICustomerService customerService;

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
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void createNewCustomer() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .phone(PHONE)
                .address(ADDRESS)
                .build();

        CustomerAddRequest request = new CustomerAddRequest();

        when(customerService.createNewCustomer(any(CustomerAddRequest.class))).thenReturn(customerDto);

        this.mockMvc.perform(post("/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
                /*.andExpect(jsonPath("$.data.name", is(createdBook.getName())))
                .andExpect(jsonPath("$.data.writer", is(createdBook.getWriter())))
                .andExpect(jsonPath("$.data.publishYear", is(createdBook.getPublishYear())));*/

        verify(customerService).createNewCustomer(any(CustomerAddRequest.class));
    }

    @Test
    void getCustomerOrders() throws Exception {
        String customerId = "c1L";
        OrderDto orderDto = OrderDto.builder().build();
        List<OrderDto> orderDtoList = Collections.singletonList(orderDto);

        when(customerService.getCustomerAllOrders(customerId)).thenReturn(orderDtoList);

        String url = "/customer/create";
        this.mockMvc.perform(get("/customer/{id}/orders", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
                /*.andExpect(jsonPath("$.data.name", is(createdBook.getName())))
                .andExpect(jsonPath("$.data.writer", is(createdBook.getWriter())))
                .andExpect(jsonPath("$.data.publishYear", is(createdBook.getPublishYear())));*/

        verify(customerService).getCustomerAllOrders(customerId);
    }
}
