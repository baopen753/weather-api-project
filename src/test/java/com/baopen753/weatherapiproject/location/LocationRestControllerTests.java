package com.baopen753.weatherapiproject.location;

import com.baopen753.weatherapiproject.locationservices.dto.LocationDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.restcontroller.LocationRestController;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LocationRestController.class)
public class LocationRestControllerTests {


    private static final String ENDPOINT = "/api/v1/locations";
    private static final String REQUEST_CONTENT_TYPE = "application/json";


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LocationService locationService;

    @MockBean
    ModelMapper modelMapper;


    @Test
    public void testGetShouldReturn200OK() throws Exception {
        // creat location instance
        Location location = new Location();
        location.setCode("VN_HCM");
        location.setCityName("Hochiminh");
        location.setCountryCode("VN");
        location.setCountryName("Socialist Republic of Vietnam");
        location.setEnabled(true);
        location.setRegionName("Middle");
        location.setTrashed(false);

        // use Mockito to mock Service object to call desired method  --> to simulate a real method in Service object - create test environment
        Mockito.when(locationService.findLocationByCode(location.getCode())).thenReturn(location);


        // use mockMvc to perform HTTP request
        mockMvc.perform(get(ENDPOINT + "/" + location.getCode()).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {

        // create non-exsiting Location instance
        Location testLocation = new Location("VN_HN", "Hanoi", "Myduc", "Socialist Republic of Vietnam", "84", true, false);

        LocationNotFoundException exception = new LocationNotFoundException("Not found location with code: " + testLocation.getCode());

        // use Mockito to create test environment mocking method of Service object
        Mockito.when(locationService.findLocationByCode(testLocation.getCode())).thenThrow(exception);

        // use mockMvc to perform HTTP request
        mockMvc.perform(get(ENDPOINT + "/" + testLocation.getCode()).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testUpdateShouldReturn201OK() throws Exception {
        // test with non-exsiting location
        LocationDto testLocationFromRequest = new LocationDto("VN_NT", "Nhatrang", "Myduc", "Socialist Republic of Vietnam", "84", true, false);

        Location testLocation = new Location("VN_NT", "Nhatrang", "Myduc", "Socialist Republic of Vietnam", "84", true, false);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testLocationFromRequest);

        // use Mockito to create test environment
        Mockito.when(locationService.create(testLocation)).thenReturn(testLocation);

        // use mockMvc to perform HTTP request
        mockMvc.perform(post(ENDPOINT).contentType(REQUEST_CONTENT_TYPE).content(requestBody)).andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void testPostShouldReturn400BadRequest() throws Exception {
        // test with exsiting location
        Location testLocation = null;

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testLocation);

        // use MockMvc to perform HTTP request
        mockMvc.perform(post(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @Disabled
    public void testPostShouldReturn409Conflict() throws Exception {
        // create existing location
        Location testedLocation = new Location("VN_HN", "Hanoi", "Myduc", "Socialist Republic of Vietnam", "84", true, false);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocation);

        LocationExistedException exception = new LocationExistedException("Duplicated location code. Try again !");

        // use Mockito to test environment by mocking LocationService.checkLocationExist()
        Mockito.when(locationService.create(testedLocation)).thenThrow(exception);

        // use MocMvc to perform HTTP Request
        mockMvc.perform(post(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isConflict()).andDo(print());
    }

    @Test
    @Disabled
    public void testUpdateShouldReturn404NotFound() throws Exception {
        // create non-existing location
        Location testedLocation = new Location();
        testedLocation.setCode("ABCDF");        // not found with code 'ABCDF'
        testedLocation.setRegionName("y");
        testedLocation.setCountryCode("y");
        testedLocation.setCountryName("y y of Vietnam");
        testedLocation.setCityName("y");
        testedLocation.setEnabled(true);


        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocation);

        LocationNotFoundException exception = new LocationNotFoundException(testedLocation.getCode());

        // use Mockito to create testing environment by mocking LocationService object
        // Mockito.when(locationService.update(testedLocation)).thenThrow(exception);
        Mockito.doThrow(exception).when(locationService).update(testedLocation);

        // use MockMvc to perform HTTP request
        mockMvc.perform(put(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        // create existing location
        LocationDto testedLocationDto = new LocationDto();
        testedLocationDto.setCode("VN_HN");
        testedLocationDto.setRegionName("South");
        testedLocationDto.setCountryCode("VN");
        testedLocationDto.setCountryName("Socalist Republic of Vietnam");
        testedLocationDto.setCityName("Longan");
        testedLocationDto.setEnabled(true);

        Location testedLocation = new Location();
        testedLocation.setCode("VN_HN");
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socalist Republic of Vietnam");
        testedLocation.setCityName("Longan");
        testedLocation.setEnabled(true);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocationDto);

        // use Mockito to create testing environment
        Mockito.when(locationService.update(testedLocation)).thenReturn(testedLocation);

        // perform http request
        mockMvc.perform(put(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isOk()).andDo(print());
    }


    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {

        LocationDto testedLocation = new LocationDto();
        // create lacked properties location
        // locationId shouldn't be lacked
        testedLocation.setCode("ABCDF");
        //  testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socalist Republic of Vietnam");
        testedLocation.setCityName("Longan");
        testedLocation.setEnabled(true);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocation);


        // perform http request
        mockMvc.perform(put(ENDPOINT).contentType(REQUEST_CONTENT_TYPE).content(requestBody)).andExpect(status().isBadRequest()).andDo(print());
    }


    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        // test with existing code
        String code = "VN_HNVN";

        Mockito.doThrow(LocationNotFoundException.class).when(locationService).delete(code);

        mockMvc.perform(delete(ENDPOINT + "/" + code)).andExpect(status().isNotFound()).andDo(print());
    }


    @Test
    public void testDeleteShouldReturn204NoContent() throws Exception {
        // text with exsiting code
        String code = "VN_HN";

        Mockito.doNothing().when(locationService).delete(code);

        mockMvc.perform(delete(ENDPOINT + "/" + code)).andExpect(status().isNoContent()).andDo(print());
    }


    // test validate request body
    @Test
    public void testValidateRequestBodyLocation() throws Exception   // interpret the request sent from client, validate the request before sending to Service layer
    {
        // create Location object with incompatibe
        LocationDto testedLocation = new LocationDto();
        testedLocation.setCode("VN_LA");
        testedLocation.setRegionName("South");
        testedLocation.setCountryName("Socalist Republic of Vietnam");
        testedLocation.setCityName("Longan");
        testedLocation.setCountryCode("VN");
        testedLocation.setEnabled(true);

        String requestBody = objectMapper.writeValueAsString(testedLocation);

        mockMvc.perform(post(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void testListLocationByPageShouldBeReturn204NoContent() throws Exception {

        Mockito.when(locationService.findAllLocationsByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(Page.empty());

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListLocationByPageShouldBeReturn200OK() throws Exception {
        int pageNum = 2;
        int pageSize = 5;
        String sortField = "code";

        Location location = new Location();
        location.setCode("USA_LA");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryName("United States of America");
        location.setCountryCode("USA");

        Page<Location> page = new PageImpl<>(List.of(location));
        Mockito.when(locationService.findAllLocationsByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(page);


        mockMvc.perform(get(ENDPOINT)
                        .param("pageNum", "1")
                        .param("pageSize", "5")
                        .param("sortField", "code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code", is("USA_LA")))
                .andExpect(jsonPath("$[0].city_name", is("Los Angeles")))
                .andExpect(jsonPath("$[0].region_name", is("California")))
                .andExpect(jsonPath("$[0].country_name", is("United States of America")))
                .andExpect(jsonPath("$[0].country_code", is("USA")))
                .andDo(print());
    }

    @Test
    public void testListLocationByPageShouldReturn400BadRequest() throws Exception {

        int page = 2;
        int size = 2;
        String sort = "code";

        mockMvc.perform(get(ENDPOINT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0]", is("Minimum of size is 4")))
                .andDo(print());
    }

    @Test
    public void testListLocationByPageShouldReturn400BadRequestDueToInValidSortField() throws Exception {
        int page = 2;
        int size = 4;
        String sort = "codeSort";

        mockMvc.perform(get(ENDPOINT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0]", is("Invalid field " + sort + ". Try again !!")))
                        .andDo(print());

    }


}

