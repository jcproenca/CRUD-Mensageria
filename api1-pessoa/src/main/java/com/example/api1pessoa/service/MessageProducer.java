package com.example.api1pessoa.service;

import com.example.api1pessoa.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final ObjectProvider<RabbitTemplate> rabbitTemplateProvider;

    public void enviarMensagem(String mensagem) {
        rabbitTemplateProvider.ifAvailable(template ->
                template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, mensagem)
        );
    }
}
