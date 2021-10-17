package getir.service;

import getir.controller.payload.response.CustomerMonthlyStaticsDto;

import java.util.List;

public interface IStaticsService {

    List<CustomerMonthlyStaticsDto> getCustomerMonthlyStatics(String customerId);
}
