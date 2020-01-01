package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class VendorServiceTest {

    public static final long ID = 1L;
    public static final String VENDOR_NAME = "Vendor Name";
    VendorService vendorService;

    @Mock
    VendorRepository vendorRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        vendorService = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
    }

    @Test
    public void getAllVendors() {
        // given
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor(), new Vendor());
        when(vendorRepository.findAll()).thenReturn(vendors);

        // when
        List<VendorDTO> vendorDTOList = vendorService.getAllVendors();

        // then
        assertEquals(4, vendorDTOList.size());
    }

    @Test
    public void getVendorById() {
        // given
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(VENDOR_NAME);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));

        // when
        VendorDTO vendorDTO = vendorService.getVendorById(ID);

        // then
        assertEquals(Long.valueOf(ID), vendorDTO.getId());
        assertEquals(VENDOR_NAME, vendorDTO.getName());
    }

    @Test
    public void createNewVendor() {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(ID);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        // when
        VendorDTO savedVendorDTO = vendorService.createNewVendor(vendorDTO);

        // then
        assertEquals(Long.valueOf(ID), savedVendorDTO.getId());
        assertEquals(VENDOR_NAME, savedVendorDTO.getName());
        assertEquals("/api/v1/vendors/1", savedVendorDTO.getVendor_url());
    }

    @Test
    public void saveVendorByDTO() {
    }

    @Test
    public void patchVendor() {
    }

    @Test
    public void deleteVendorById() {
        vendorRepository.deleteById(ID);
        verify(vendorRepository, times(1)).deleteById(anyLong());
    }
}