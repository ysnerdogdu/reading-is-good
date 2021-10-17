package getir.service;

import getir.controller.payload.request.CustomerAddRequest;
import getir.controller.payload.response.CustomerDto;
import getir.controller.payload.response.OrderDto;
import getir.model.Customer;
import getir.repository.ICustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private ICustomerRepository customerRepository;

    @Mock
    private IOrderService orderService;

    @Test
    void createNewCustomer() {

        String email = "test@gmail.com";
        String firstName = "Test First";
        String lastName = "Test Last";
        String phone = "+905*********";
        String address = "Test Address";

        CustomerAddRequest request = new CustomerAddRequest(email, null, firstName, lastName, phone, address);

        Customer customer = Customer.builder()
                .email(email)
                .fistName(firstName)
                .lastName(lastName)
                .phone(phone)
                .address(address)
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        CustomerDto newCustomer = customerService.createNewCustomer(request);

        assertNotNull(newCustomer);
        assertEquals(request.getEmail(), newCustomer.getEmail());
        assertEquals(request.getAddress(), newCustomer.getAddress());
        assertEquals(request.getFirstName(), newCustomer.getFirstName());

    }

    @Test
    void getCustomerAllOrders() {

        String customerId = "c1001L";
        String status = "Completed";
        LocalDateTime localDateTime = LocalDateTime.of(2021, 10, 11, 13, 30);

        OrderDto orderDto = OrderDto.builder()
                .customerId(customerId)
                .createdAt(localDateTime)
                .status(status)
                .build();
        List<OrderDto> orders = Collections.singletonList(orderDto);

        when(orderService.getOrdersByCustomerId(customerId)).thenReturn(orders);

        List<OrderDto> customerAllOrders = customerService.getCustomerAllOrders(customerId);

        assertNotNull(customerAllOrders);
        assertEquals(orders.size(), customerAllOrders.size());
        assertEquals(orders.get(0).getStatus(), customerAllOrders.get(0).getStatus());
    }
}
