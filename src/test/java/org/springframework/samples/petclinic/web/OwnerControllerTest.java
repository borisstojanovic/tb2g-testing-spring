package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void testProcessUpdateOwnerFormValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit",1)
                        .param("firstName", "Jimmy")
                        .param("lastName", "Buffett")
                        .param("address", "123 Duval St")
                        .param("city", "Key West")
                        .param("telephone", "3151231234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void testProcessUpdateOwnerFormNotValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit",1)
                        .param("firstName", "Jimmy")
                        .param("address", "123 Duval St")
                        .param("telephone", "3151231234"))
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner",
                        "lastName", "city"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testNewOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("firstName", "Jimmy")
                        .param("lastName", "Buffett")
                        .param("address", "123 Duval St")
                        .param("city", "Key West")
                        .param("telephone", "3151231234"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("firstName", "Jimmy")
                        .param("lastName", "Buffett")
                        .param("city", "Key West"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner",
                        "address", "telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                        .param("lastName", "DontFindMe"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByNameFoundSingle() throws Exception {
        final String name = "FindMe";
        Owner owner = new Owner();
        owner.setId(1);
        given(clinicService.findOwnerByLastName(name)).willReturn(Lists.newArrayList(owner));

        mockMvc.perform(get("/owners")
                        .param("lastName", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        then(clinicService).should().findOwnerByLastName(anyString());
    }

    @Test
    void testFindByNameFoundMultiple() throws Exception {
        given(clinicService.findOwnerByLastName("")).willReturn(Lists.newArrayList(new Owner(), new Owner()));
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));
        then(clinicService).should().findOwnerByLastName("");
    }

    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}