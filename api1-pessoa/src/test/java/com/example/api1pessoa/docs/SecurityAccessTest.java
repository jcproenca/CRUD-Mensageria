package com.example.api1pessoa.docs;

import com.example.api1pessoa.service.FisicaService;
import com.example.api1pessoa.service.JuridicaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        com.example.api1pessoa.controller.FisicaController.class,
        com.example.api1pessoa.controller.JuridicaController.class
})
@Import(com.example.api1pessoa.config.SecurityConfig.class)
class SecurityAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FisicaService fisicaService;

    @MockBean
    private JuridicaService juridicaService;

    @MockBean
    private com.example.api1pessoa.repository.UsuarioRepository usuarioRepository;

    @Test
    @WithMockUser(roles = "USER")
    void user_pode_listar_fisicas() throws Exception {
        when(fisicaService.listarTodos()).thenReturn(List.of());
        mockMvc.perform(get("/api/fisicas"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_nao_pode_criar_fisica() throws Exception {
        mockMvc.perform(post("/api/fisicas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "nome", "Teste", "genero", "M", "idade", 20,
                                "cpf", "12345678901",
                                "endereco", Map.of("rua", "R", "numero", "1",
                                        "bairro", "B", "cidade", "C", "estado", "SP", "cep", "00000-000")
                        ))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_nao_pode_deletar_fisica() throws Exception {
        mockMvc.perform(delete("/api/fisicas/1").with(csrf()))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_pode_deletar_juridica() throws Exception {
        mockMvc.perform(delete("/api/juridicas/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void sem_autenticacao_retorna_401() throws Exception {
        mockMvc.perform(get("/api/fisicas"))
                .andExpect(status().isUnauthorized());
    }
}
