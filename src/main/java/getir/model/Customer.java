package getir.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String email;

    private String password;

    private String fistName;

    private String lastName;

    private String phone;

    private String address;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
