package main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.models.IceCreamDTO;
import main.services.IceCreamService;
import main.services.IceCreamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IceCreamController.class)
class IceCreamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    IceCreamService iceCreamService;

    IceCreamServiceImpl iceCreamServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<IceCreamDTO> iceCreamArgumentCaptor;

    @BeforeEach
    void setUp(){
        iceCreamServiceImpl = new IceCreamServiceImpl();
    }

    @Test
    void getIceCreamById() throws Exception {

        IceCreamDTO testIceCream = iceCreamServiceImpl.getAllIceCreams().get(0);
        given(iceCreamService.getIceCreamById(any(UUID.class))).willReturn(java.util.Optional.ofNullable(testIceCream));

        mockMvc.perform(get(IceCreamController.ICE_CREAM_PATH_ID, testIceCream.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testIceCream.getId().toString())))
                .andExpect((jsonPath("$.iceCreamName", is(testIceCream.getIceCreamName()))));
    }

    @Test
    void getIceCreamByIdNotFound() throws Exception {
        given(iceCreamService.getIceCreamById(any(UUID.class))).willThrow(NotFoundException.class);
        mockMvc.perform(get(IceCreamController.ICE_CREAM_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getListOfIceCreams() throws Exception {
        given(iceCreamService.getAllIceCreams()).willReturn(iceCreamServiceImpl.getAllIceCreams());

        mockMvc.perform(get(IceCreamController.ICE_CREAM_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testCreateNewIceCream() throws Exception {
        IceCreamDTO iceCream = iceCreamServiceImpl.getAllIceCreams().get(0);
        iceCream.setId(null);
        iceCream.setVersion(null);
        System.out.println(objectMapper.writeValueAsString(iceCream));

        given(iceCreamService.saveNewIceCream(any(IceCreamDTO.class))).willReturn(iceCreamServiceImpl.getAllIceCreams().get(1));
        mockMvc.perform(post(IceCreamController.ICE_CREAM_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(iceCream)))
                        .andExpect(status().isCreated())
                        .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateIceCream() throws Exception {
        IceCreamDTO iceCream = iceCreamServiceImpl.getAllIceCreams().get(0);
        given(iceCreamService.updateIceCreamById(any(), any())).willReturn(Optional.of(iceCream));
        mockMvc.perform(put(IceCreamController.ICE_CREAM_PATH_ID, iceCream.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(iceCream)))
                .andExpect(status().isNoContent());

        verify(iceCreamService).updateIceCreamById(any(UUID.class), any(IceCreamDTO.class));
    }

    @Test
    void testPatchIceCream() throws Exception {
        IceCreamDTO iceCream = iceCreamServiceImpl.getAllIceCreams().get(0);
        Map<String, Object> iceCreamMap = new HashMap<>();
        iceCreamMap.put("IceCreamName", null);

        mockMvc.perform(patch(IceCreamController.ICE_CREAM_PATH_ID, iceCream.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(iceCreamMap)))
                        .andExpect(status().isNoContent());

        verify(iceCreamService).patchIceCreamById(uuidArgumentCaptor.capture(), iceCreamArgumentCaptor.capture());
        assertThat(iceCream.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(iceCreamMap.get("IceCreamName")).isEqualTo(iceCreamArgumentCaptor.getValue().getIceCreamName());
    }

    @Test
    void testDeleteIceCream() throws Exception {
        IceCreamDTO iceCream = iceCreamServiceImpl.getAllIceCreams().get(0);

        given(iceCreamService.deleteIceCreamById(any())).willReturn(true);

        mockMvc.perform(delete(IceCreamController.ICE_CREAM_PATH_ID + iceCream.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(iceCreamService).deleteIceCreamById(uuidArgumentCaptor.capture());
        assertThat(iceCream.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}