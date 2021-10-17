package getir.controller.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddRequest {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;
}
