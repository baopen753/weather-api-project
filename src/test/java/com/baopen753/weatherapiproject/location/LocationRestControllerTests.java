package com.baopen753.weatherapiproject.location;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.restcontroller.LocationRestController;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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


        // use Mockito to create test environment mocking method of Service object
        Mockito.when(locationService.findLocationByCode(testLocation.getCode())).thenThrow(LocationNotFoundException.class);

        // use mockMvc to perform HTTP request
        mockMvc.perform(get(ENDPOINT + "/" + testLocation.getCode()).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testUpdateShouldReturn201OK() throws Exception {
        // test with non-exsiting location
        Location testLocation = new Location("VN_NT", "Nhatrang", "Myduc", "Socialist Republic of Vietnam", "84", true, false);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testLocation);

        // use Mockito to create test environment
        Mockito.when(locationService.create(testLocation)).thenReturn(testLocation);

        // use mockMvc to perform HTTP request
        mockMvc.perform(post(ENDPOINT).contentType(REQUEST_CONTENT_TYPE).content(requestBody)).andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void testPostShouldReturn400Badquest() throws Exception {
        // test with exsiting location
        Location testLocation = null;

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testLocation);

        // use MockMvc to perform HTTP request
        mockMvc.perform(post(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testPostShouldReturn409Confict() throws Exception {
        // create existing location
        Location testedLocation = new Location("VN_HN", "Hanoi", "Myduc", "Socialist Republic of Vietnam", "84", true, false);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocation);

        // use Mockito to test environment by mocking LocationService.checkLocationExist()
        Mockito.when(locationService.checkLocationExist(testedLocation.getCode())).thenReturn(testedLocation);

        // use MocMvc to perform HTTP Request
        mockMvc.perform(post(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isConflict()).andDo(print());
    }

    @Test
    @Disabled
    public void testUpdateShouldReturn404NotFound() throws Exception {
        // create non-existing location
        Location testedLocation = new Location();
        testedLocation.setCode("ABCDF");
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socalist Republic of Vietnam");
        testedLocation.setCityName("Longan");
        testedLocation.setEnabled(true);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocation);

        // use Mockito to create testing environment by mocking LocationService object
        Mockito.when(locationService.update(testedLocation)).thenThrow(new LocationNotFoundException("No location found"));

        // use MockMvc to perform HTTP request
        mockMvc.perform(put(ENDPOINT).contentType(REQUEST_CONTENT_TYPE).content(requestBody)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        // create existing location
        Location testedLocation = new Location();
        testedLocation.setCode("VN_HN");
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socalist Republic of Vietnam");
        testedLocation.setCityName("Longan");
        testedLocation.setEnabled(true);

        // serialize POJO to JSON
        String requestBody = objectMapper.writeValueAsString(testedLocation);

        // use Mockito to create testing environment
        Mockito.when(locationService.update(testedLocation)).thenReturn(testedLocation);

        // perform http request
        mockMvc.perform(put(ENDPOINT).content(requestBody).contentType(REQUEST_CONTENT_TYPE)).andExpect(status().isOk()).andDo(print());
    }


    @Test
    @Disabled
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        // create lacked properties location
        Location testedLocation = new Location();
        testedLocation.setRegionName("South");
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

        mockMvc.perform(delete(ENDPOINT+"/"+code))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


}

