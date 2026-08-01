package com.example.api2consumer;

import com.example.api2consumer.listener.PessoaEventListener;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class PessoaEventListenerTest {

    private final PessoaEventListener listener = new PessoaEventListener();

    @Test
    void receberMensagem_criacao_naoDeveLancarExcecao() {
        assertThatCode(() ->
                listener.receberMensagem("Pessoa Física 'João da Silva' (id=1) criada com sucesso"))
                .doesNotThrowAnyException();
    }

    @Test
    void receberMensagem_atualizacao_naoDeveLancarExcecao() {
        assertThatCode(() ->
                listener.receberMensagem("Pessoa Física 'João da Silva' (id=1) atualizada com sucesso"))
                .doesNotThrowAnyException();
    }

    @Test
    void receberMensagem_delecao_naoDeveLancarExcecao() {
        assertThatCode(() ->
                listener.receberMensagem("Pessoa Jurídica 'Empresa Exemplo Ltda' (id=2) deletada com sucesso"))
                .doesNotThrowAnyException();
    }

    @Test
    void receberMensagem_juridica_criacao_naoDeveLancarExcecao() {
        assertThatCode(() ->
                listener.receberMensagem("Pessoa Jurídica 'Empresa Exemplo Ltda' (id=2) criada com sucesso"))
                .doesNotThrowAnyException();
    }
}
