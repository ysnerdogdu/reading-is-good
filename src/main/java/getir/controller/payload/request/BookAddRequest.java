package getir.controller.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookAddRequest {

    private String name;

    private String writer;

    private Integer publishYear;

    private Integer stock;

    private Double price;
}
