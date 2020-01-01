package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.services.ResourceNotFoundException;
import guru.springframework.services.VendorService;
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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest extends AbstractRestControllerTest {

    private final static Long ID_VENDOR_ONE = Long.valueOf(1L);
    private final static Long ID_VENDOR_TWO = Long.valueOf(2L);
    public static final String VENDOR_ONE_NAME = "Vendor One Name";
    public static final String VENDOR_TWO_NAME = "Vendor Two Name";
    private static final String VENDOR_ONE_UPDATED_NAME = "Vendor One Updated Name";

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    public void getAllVendors() throws Exception {
        VendorDTO vendorOneDTO = new VendorDTO();
        vendorOneDTO.setId(ID_VENDOR_ONE);
        vendorOneDTO.setName(VENDOR_ONE_NAME);

        VendorDTO vendorTwoDTO = new VendorDTO();
        vendorTwoDTO.setId(ID_VENDOR_TWO);
        vendorTwoDTO.setName(VENDOR_TWO_NAME);

        List<VendorDTO> vendorDTOList = Arrays.asList(vendorOneDTO, vendorTwoDTO);

        when(vendorService.getAllVendors()).thenReturn(vendorDTOList);

        mockMvc.perform(get(VendorController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", Matchers.hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {
        VendorDTO vendorOneDTO = new VendorDTO();
        vendorOneDTO.setId(ID_VENDOR_ONE);
        vendorOneDTO.setName(VENDOR_ONE_NAME);

        when(vendorService.getVendorById(anyLong())).thenReturn(vendorOneDTO);

        mockMvc.perform(get(VendorController.BASE_URL + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_ONE_NAME)));
    }

    @Test
    public void createNewVendor() throws Exception {
        // given
        VendorDTO vendorOneDTO = new VendorDTO();
        vendorOneDTO.setId(ID_VENDOR_ONE);
        vendorOneDTO.setName(VENDOR_ONE_NAME);

        VendorDTO vendorReturnedDTO = new VendorDTO();
        vendorReturnedDTO.setId(ID_VENDOR_ONE);
        vendorReturnedDTO.setName(VENDOR_ONE_NAME);
        vendorReturnedDTO.setVendor_url(VendorController.BASE_URL + "1");

        // when
        when(vendorService.createNewVendor(vendorOneDTO)).thenReturn(vendorReturnedDTO);

        // then
        mockMvc.perform(post(VendorController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(vendorOneDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_ONE_NAME)))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "1")));
    }

    @Test
    public void updateVendor() throws Exception {
        // given
        VendorDTO updatingVendorOneDTO = new VendorDTO();
        updatingVendorOneDTO.setName(VENDOR_ONE_UPDATED_NAME);

        VendorDTO vendorUpdatedDTO = new VendorDTO();
        vendorUpdatedDTO.setName(VENDOR_ONE_UPDATED_NAME);
        vendorUpdatedDTO.setVendor_url(VendorController.BASE_URL + "1");

        // when
        when(vendorService.saveVendorByDTO(anyLong(), eq(updatingVendorOneDTO))).thenReturn(vendorUpdatedDTO);

        // then
        mockMvc.perform(put(VendorController.BASE_URL + "1")
                .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(updatingVendorOneDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_ONE_UPDATED_NAME)))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "1")));
    }

    @Test
    public void patchVendor() throws Exception {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_ONE_UPDATED_NAME);

        VendorDTO vendorReturnedDTO = new VendorDTO();
        vendorReturnedDTO.setName(VENDOR_ONE_UPDATED_NAME);
        vendorReturnedDTO.setVendor_url(VendorController.BASE_URL + "1");

        // when
        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(vendorReturnedDTO);

        // then
        mockMvc.perform(patch(VendorController.BASE_URL + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_ONE_UPDATED_NAME)))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "1")));
    }

    @Test
    public void deleteVendorById() throws Exception {
        mockMvc.perform(delete(VendorController.BASE_URL + "1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService).deleteVendorById(anyLong());
    }

    @Test
    public void getVendorByIdNotFound() throws Exception {

        when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(VendorController.BASE_URL + "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
