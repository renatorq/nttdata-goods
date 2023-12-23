package com.nttdata.goods.kafka;

import com.nttdata.goods.dto.DetalleOrdenEntradaDTO;
import com.nttdata.goods.service.ArticuloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    ArticuloService articuloService;

    @KafkaListener(topics = "${spring.kafka.consumer.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(DetalleOrdenEntradaDTO data) throws Exception {
        LOGGER.info(String.format("Json message recieved -> %s", data.toString()));
       articuloService.actualizarStockArticulo(data);
    }
}
