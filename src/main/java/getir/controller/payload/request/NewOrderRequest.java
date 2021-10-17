package getir.controller.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NewOrderRequest {

    private String customerId;

    private List<BookOrder> bookOrders;

    public NewOrderRequest(String customerId, List<BookOrder> bookOrders) {
        this.customerId = customerId;
        this.bookOrders = bookOrders;
    }
}
