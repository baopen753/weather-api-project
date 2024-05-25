package com.baopen753.weatherapiproject.location;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.restcontroller.LocationRestController;
import com.baopen753.weatherapiproject.locationservices.service.impl.LocationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    LocationServiceImpl locationService;


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
        mockMvc.perform(get(ENDPOINT+"/"+location.getCode()).contentType(REQUEST_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {

        // create non-exsiting Location instance
        Location testLocation = new Location("VN_HN", "Hanoi", "Myduc", "Socialist Republic of Vietnam", "84", true, false);


        // use Mockito to create test environment mocking method of Service object
        Mockito.when(locationService.findLocationByCode(testLocation.getCode())).thenThrow(LocationNotFoundException.class);

        // use mockMvc to perform HTTP request
        mockMvc.perform(get(ENDPOINT+"/"+testLocation.getCode()).contentType(REQUEST_CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}

