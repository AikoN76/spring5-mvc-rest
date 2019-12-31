package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CategoryDTO;
import guru.springframework.services.CategoryService;
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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest {

    public static final long ID_ONE = 1L;
    public static final String NAME_ONE = "Test1";
    public static final long ID_TWO = 2L;
    public static final String NAME_TWO = "Test2";
    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    public void listCategories() throws Exception {
        CategoryDTO categoryOneDTO = new CategoryDTO();
        categoryOneDTO.setId(ID_ONE);
        categoryOneDTO.setName(NAME_ONE);

        CategoryDTO categoryTwoDTO = new CategoryDTO();
        categoryTwoDTO.setId(ID_TWO);
        categoryTwoDTO.setName(NAME_TWO);

        List<CategoryDTO> categoryDTOList = Arrays.asList(categoryOneDTO, categoryTwoDTO);

        when(categoryService.getAllCategories()).thenReturn(categoryDTOList);

        mockMvc.perform(get(CategoryController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", Matchers.hasSize(2)));
    }

    @Test
    public void getByNameCategory() throws Exception {
        CategoryDTO categoryOneDTO = new CategoryDTO();
        categoryOneDTO.setId(ID_ONE);
        categoryOneDTO.setName(NAME_ONE);

        when(categoryService.getCategoryByName(anyString())).thenReturn(categoryOneDTO);

        mockMvc.perform(get(CategoryController.BASE_URL + "Test1").
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME_ONE)));
    }

    @Test
    public void testGetByNameNotFound() throws Exception {
        when(categoryService.getCategoryByName(anyString())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get(CategoryController.BASE_URL + "Foo").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}