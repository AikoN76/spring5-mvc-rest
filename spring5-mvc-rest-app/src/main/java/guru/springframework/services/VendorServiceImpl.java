package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public List<VendorDTO> getAllVendors() {
        return vendorRepository
                .findAll()
                .stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setVendor_url(VendorController.BASE_URL + vendor.getId());
                    return vendorDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public VendorDTO getVendorById(Long id) {
        VendorDTO vendorDTO = vendorRepository.findById(id)
                .map(vendorMapper::vendorToVendorDTO)
                .orElseThrow(ResourceNotFoundException::new);
        vendorDTO.setVendor_url(VendorController.BASE_URL + id);
        return vendorDTO;
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);
        return saveAndReturnDTO(vendor);
    }

    @Override
    public VendorDTO saveVendorByDTO(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);
        vendor.setId(id);
        return saveAndReturnDTO(vendor);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return vendorRepository.findById(id).map(vendor -> {
           if(vendorDTO.getName() != null){
               vendor.setName(vendorDTO.getName());
           }
           VendorDTO returnedVendorDTO = vendorMapper.vendorToVendorDTO(vendorRepository.save(vendor));
           returnedVendorDTO.setVendor_url(VendorController.BASE_URL + id);
           return returnedVendorDTO;
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO saveAndReturnDTO(Vendor vendor) {
        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO returnedVendorDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        returnedVendorDTO.setVendor_url(VendorController.BASE_URL + savedVendor.getId());
        return returnedVendorDTO;
    }
}
