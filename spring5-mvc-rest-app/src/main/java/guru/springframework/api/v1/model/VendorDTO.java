package guru.springframework.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VendorDTO {

    private Long id;
    @ApiModelProperty(value = "This is the name of the vendor", required = true)
    private String name;
    private String vendor_url;

}
