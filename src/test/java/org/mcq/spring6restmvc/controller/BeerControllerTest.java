package org.mcq.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mcq.spring6restmvc.model.BeerDTO;
import org.mcq.spring6restmvc.service.BeerService;
import org.mcq.spring6restmvc.service.BeerServiceImpl;
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

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testGetBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.name", is(testBeer.getName())));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testCreateNewBeer() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        testBeer.setId(null);
        testBeer.setVersion(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(testBeer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.deleteBeerById(any())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("name", "New Name");

        given(beerService.patchBeerById(any(), any())).willReturn(Optional.of(testBeer));

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("name")).isEqualTo(beerArgumentCaptor.getValue().getName());
    }

    @Test
    void testCreateBeerNullName() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBeerBlankName() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        testBeer.setName("");

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(testBeer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBeer)))
                .andExpect(status().isBadRequest());
    }
}
