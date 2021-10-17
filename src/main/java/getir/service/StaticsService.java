package getir.service;

import getir.controller.payload.response.CustomerMonthlyStaticsDto;
import getir.controller.payload.response.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaticsService  implements IStaticsService {

    private final ICustomerService customerService;

    @Override
    public List<CustomerMonthlyStaticsDto> getCustomerMonthlyStatics(String customerId) {

        List<OrderDto> customerOrders = customerService.getCustomerAllOrders(customerId);

        Map<Month, List<OrderDto>> result = customerOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreatedAt().getMonth()));

        return result.entrySet().stream()
                .map(entry -> createCustomerMonthlyStatics(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private CustomerMonthlyStaticsDto createCustomerMonthlyStatics(Month month, List<OrderDto> orderList) {
        int totalBookCount = 0;
        double totalPurchasedAmount = 0;

        for (OrderDto order : orderList) {

            totalBookCount += order.getTotalBooks();
            totalPurchasedAmount += order.getTotalPrice();
        }

        return new CustomerMonthlyStaticsDto(month.toString(), orderList.size(), totalBookCount, totalPurchasedAmount);
    }
}
