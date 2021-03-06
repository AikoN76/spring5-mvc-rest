package guru.springframework.controllers.v1;

import guru.springframework.model.CustomerDTO;
import guru.springframework.services.CustomerService;
import guru.springframework.services.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractRestControllerTest {

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
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
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

        mockMvc.perform(get(CustomerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
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

        mockMvc.perform(get(CustomerController.BASE_URL + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)));
    }


    @Test
    public void createNewCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");
        customerDTO.setLastname("Flintstones");

        CustomerDTO returnedDTO = new CustomerDTO();
        returnedDTO.setId(1L);
        returnedDTO.setFirstname("Fred");
        returnedDTO.setLastname("Flintstones");
        returnedDTO.setCustomerUrl(CustomerController.BASE_URL + "1");

        // when
        when(customerService.createNewCustomer(any())).thenReturn(returnedDTO);

        // then
        mockMvc.perform(post(CustomerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flintstones")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL + "1")));

    }

    @Test
    public void testUpdateCustomer() throws Exception {
        // given
        CustomerDTO updatingCustomerDTO = new CustomerDTO();
        updatingCustomerDTO.setFirstname("Fred");
        updatingCustomerDTO.setLastname("Flintstone");

        CustomerDTO updatedCustomerDTO = new CustomerDTO();
        updatedCustomerDTO.setFirstname("Fred");
        updatedCustomerDTO.setLastname("Flintstone");
        updatedCustomerDTO.setCustomerUrl(CustomerController.BASE_URL + "1");

        // when
        when(customerService.saveCustomerByDTO(anyLong(), any())).thenReturn(updatedCustomerDTO);

        // then
        mockMvc.perform(put(CustomerController.BASE_URL + "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatingCustomerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flintstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL + "1"))
        );
    }

    @Test
    public void testPatchCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");

        CustomerDTO returnedCustomerDTO = new CustomerDTO();
        returnedCustomerDTO.setFirstname(customerDTO.getFirstname());
        returnedCustomerDTO.setLastname("Flintstone");
        returnedCustomerDTO.setCustomerUrl(CustomerController.BASE_URL + "1");

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnedCustomerDTO);

        mockMvc.perform(patch(CustomerController.BASE_URL + "1")
            .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flintstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL + "1"))
        );

    }

    @Test
    public void testDeleteCustomerById() throws Exception {
        mockMvc.perform(delete(CustomerController.BASE_URL + "1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomerById(anyLong());

    }

    @Test
    public void getCustomerNotFoundById() throws Exception {

        when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CustomerController.BASE_URL + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}