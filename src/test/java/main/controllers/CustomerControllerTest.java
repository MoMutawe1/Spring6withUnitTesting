package main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.models.CustomerDTO;
import main.services.CustomerService;
import main.services.CustomerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @Test
    void getCustomerById() throws Exception {   // getCustomerById doesn't have any parameter because it's a test class nothing going to be passed here
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0); // we are calling getAllCustomers from the serviceImpl class to get one record to test

        // this will use the primary implementation (real data from DB) of the CustomerService to get a customer record from DB
        // I have saved 3 hardcoded objects in the CustomerServiceImpl class then mapped them to be saved in h2 in memory DB
        // because we are not testing real data, in the first line above we got a hardcoded value from the CustomerServiceImpl class
        given(customerService.getCustomerById(customer.getId())).willReturn(Optional.of(customer));

        // here is the main testing part, I'm testing the first handler of the user request (The controller class - CustomerController)
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(customer.getName())));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {

        // here we are searching for a random UUID number in the database which will be definitely not found
        // the CustomerService.getCustomerById  will search for a specific record in the actual DB
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());  // willReturn(Optional.empty())  expect empty not found value from DB

        // here is the main testing part, testing the controller class.
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAllCustomers() throws Exception {
        // here when we send a request to CustomerService against DB we return hardcoded data from customerServiceImpl
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        // we have only 3 hardcoded values in the CustomerServiceImpl so the expected length is 3
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testCreateCustomer() throws Exception {
        // here we are getting one hardcoded record from CustomerServiceImpl and save it to CustomerDTO object.
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        // the customerService.saveNewCustomer method expecting a DTO object then will convert it to entity object before saving it to DB.
        given(customerService.saveNewCustomer(any(CustomerDTO.class)))
                .willReturn(customerServiceImpl.getAllCustomers().get(0)); // returning any value from our hardcoded list.

        // we are using the builtin function "post" to test the post operation in the controller.
        mockMvc.perform(post(CustomerController.CUSTOMER_PATH).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // we are getting one hardcoded record from the CustomerServiceImpl
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        // updateCustomerById is expecting 2 parameters (UUID customerId, CustomerDTO customer) and will return a Customer DTO Object.
        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(CustomerDTO.builder()
                .build()));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .content(objectMapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(CustomerDTO.class));

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New Name");

        mockMvc.perform(patch( CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(),
                customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getName())
                .isEqualTo(customerMap.get("name"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        given(customerService.deleteCustomerById(any())).willReturn(true);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}
