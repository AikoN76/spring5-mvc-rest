package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.services.CustomerService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    public static final long ID_ONE = 1L;
    public static final String FIRST_NAME_ONE = "First Name One";
    public static final String LAST_NAME_ONE = "Last Name One";
    public static final long ID_TWO = 2L;
    public static final String FIRST_NAME_TWO = "First Name Two";
    public static final String LAST_NAME_TWO = "Last Name Two";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void getAllCustomers() throws Exception {
        CustomerDTO customerOneDTO = new CustomerDTO();
        customerOneDTO.setId(ID_ONE);
        customerOneDTO.setFirstname(FIRST_NAME_ONE);
        customerOneDTO.setLastname(LAST_NAME_ONE);

        CustomerDTO customerTwoDTO = new CustomerDTO();
        customerTwoDTO.setId(ID_TWO);
        customerTwoDTO.setFirstname(FIRST_NAME_TWO);
        customerTwoDTO.setLastname(LAST_NAME_TWO);

        List<CustomerDTO> customerDTOList = Arrays.asList(customerOneDTO, customerTwoDTO);

        when(customerService.getAllCustomers()).thenReturn(customerDTOList);

        mockMvc.perform(get("/api/v1/customers/")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", Matchers.hasSize(2)));
    }

    @Test
    public void getCustomerById() throws Exception {
        CustomerDTO customerOneDTO = new CustomerDTO();
        customerOneDTO.setId(ID_ONE);
        customerOneDTO.setFirstname(FIRST_NAME_ONE);
        customerOneDTO.setLastname(LAST_NAME_ONE);

        when(customerService.getCustomerById(anyLong())).thenReturn(customerOneDTO);

        mockMvc.perform(get("/api/v1/customers/1")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)));
    }
}