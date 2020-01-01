package guru.springframework.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    @ApiModelProperty(value = "This is the first name", required = true)
    private String firstname;
    @ApiModelProperty(required = true )
    private String lastname;
    private String customerUrl;
}
