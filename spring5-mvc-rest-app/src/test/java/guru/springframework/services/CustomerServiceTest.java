package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.model.CustomerDTO;
import guru.springframework.controllers.v1.CustomerController;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    public static final long ID = 1L;
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void getAllCustomers() {
        // given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        // when
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();

        // then
        assertEquals(3, customerDTOList.size());
    }

    @Test
    public void getCustomerById() {

        // given
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        when(customerRepository.findById(anyLong())).thenReturn(java.util.Optional.of(customer));

        // when
        CustomerDTO customerDTO = customerService.getCustomerById(ID);

        // then
        assertEquals(Long.valueOf(ID), customerDTO.getId());
        assertEquals(FIRST_NAME, customerDTO.getFirstname());
        assertEquals(LAST_NAME, customerDTO.getLastname());
    }

    @Test
    public void createNewCustomer()  {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("John");
        customerDTO.setLastname("Smith");

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstname(customerDTO.getFirstname());
        savedCustomer.setLastname(customerDTO.getLastname());
        savedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // when
        CustomerDTO savedCustomerDTO = customerService.createNewCustomer(customerDTO);

        // then
        assertEquals(Long.valueOf(1L), savedCustomerDTO.getId());
        assertEquals(customerDTO.getFirstname(), savedCustomerDTO.getFirstname());
        assertEquals(customerDTO.getLastname(), savedCustomerDTO.getLastname());
        assertEquals(CustomerController.BASE_URL + "1", savedCustomerDTO.getCustomerUrl());
    }

    @Test
    public void testDeleteCustomerById() {
        Long id = 1L;
        customerRepository.deleteById(id);
        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}