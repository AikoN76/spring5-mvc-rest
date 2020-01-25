package guru.springframework.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by jt on 9/24/17.
 */
@Data
public class CategoryDTO {
    private Long id;
    @ApiModelProperty(value = "This is the name of the category", required = true)
    private String name;
}
