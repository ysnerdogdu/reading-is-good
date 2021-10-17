package getir.service;

import getir.controller.payload.request.CustomerAddRequest;
import getir.controller.payload.response.CustomerDto;
import getir.controller.payload.response.OrderDto;
import getir.model.Customer;
import getir.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements ICustomerService{

    private final ICustomerRepository customerRepository;
    private final IOrderService orderService;

    @Override
    public CustomerDto createNewCustomer(CustomerAddRequest customerAddRequest) {

        String email = customerAddRequest.getEmail();
        if (email == null) {
            log.error("Customer email is not valid email={}", email);
            return null;
        }

        Optional<Customer> customerOptional = customerRepository.findByEmail(email);

        if (customerOptional.isPresent()) {
            log.error("Customer already exists with email={}", email);
            return null;
        }

        Customer customer = Customer.builder()
                .email(email)
                .fistName(customerAddRequest.getFirstName())
                .lastName(customerAddRequest.getLastName())
                .password(customerAddRequest.getPassword())
                .phone(customerAddRequest.getPhone())
                .address(customerAddRequest.getAddress())
                .build();

        customerRepository.save(customer);

        log.info("New customer is created with email={}", email);

        return CustomerDto.fromCustomer(customer);

    }

    @Override
    public List<OrderDto> getCustomerAllOrders(String customerId) {
        return  orderService.getOrdersByCustomerId(customerId);
    }
}
