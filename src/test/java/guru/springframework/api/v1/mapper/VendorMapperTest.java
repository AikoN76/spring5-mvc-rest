package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.domain.Vendor;
import org.junit.Test;

import static org.junit.Assert.*;

public class VendorMapperTest {

    public static final long ID = 1L;
    public static final String VENDOR_NAME = "Vendor Name";
    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    public void vendorToVendorDTO() {
        // given
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(VENDOR_NAME);

        // when
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

        // then
        assertEquals(Long.valueOf(ID), vendorDTO.getId());
        assertEquals(VENDOR_NAME, vendorDTO.getName());
    }

    @Test
    public void vendorDTOToVendor() {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setId(ID);
        vendorDTO.setName(VENDOR_NAME);

        // when
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

        // then
        assertEquals(Long.valueOf(ID), vendor.getId());
        assertEquals(VENDOR_NAME, vendor.getName());
    }
}