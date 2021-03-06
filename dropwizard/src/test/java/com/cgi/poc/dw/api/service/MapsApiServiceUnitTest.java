package com.cgi.poc.dw.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.MapApiConfiguration;
import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.api.service.impl.MapsApiServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapsApiServiceUnitTest {

  @Mock
  private Client client;

  @Mock
  private MapApiConfiguration mapApiConfiguration;

  @InjectMocks
  private MapsApiServiceImpl underTest;

  @Before
  public void start() throws IOException {

    when(mapApiConfiguration.getApiURL()).thenReturn("http://googleMapsURL.com");
  }

  @Test(expected = InternalServerErrorException.class)
  public void mapsAPICommunicationFails() {

    //mocking the Jersey Client
    WebTarget mockWebTarget = mock(WebTarget.class);
    when(client.target(anyString())).thenReturn(mockWebTarget);
    when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
    Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
    when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);
    doThrow(new ProcessingException("Processing failed.")).when(mockBuilder).get(String.class);

    underTest.getGeoCoordinatesByZipCode("92105");
    fail("Expected an exception to be thrown");
  }

  @Test
  public void foundGeoCoordinates() throws Exception {

    JsonNode jsonRespone = new ObjectMapper().
        readTree(getClass().getResource("/google_maps_api/success_geocode_response.json"));

    //mocking the Jersey Client
    WebTarget mockWebTarget = mock(WebTarget.class);
    when(client.target(anyString())).thenReturn(mockWebTarget);
    when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
    Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
    when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);
    when(mockBuilder.get(String.class)).thenReturn(jsonRespone.toString());

    GeoCoordinates result = underTest.getGeoCoordinatesByZipCode("92105");
    assertEquals(new Double(38.5824933), result.getLatitude());
    assertEquals(new Double(-121.4941738), result.getLongitude());
  }

  @Test
  public void notFoundGeoCoordinates() throws Exception {

    JsonNode jsonRespone = new ObjectMapper().
        readTree(getClass().getResource("/google_maps_api/not_found_geocode_response.json"));

    //mocking the Jersey Client
    WebTarget mockWebTarget = mock(WebTarget.class);
    when(client.target(anyString())).thenReturn(mockWebTarget);
    when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
    Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
    when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);
    when(mockBuilder.get(String.class)).thenReturn(jsonRespone.toString());

    GeoCoordinates result = underTest.getGeoCoordinatesByZipCode("92105");
    assertEquals(new Double(0.0), result.getLatitude());
    assertEquals(new Double(0.0), result.getLongitude());
  }
}
