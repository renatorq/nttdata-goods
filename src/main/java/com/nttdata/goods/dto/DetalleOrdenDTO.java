package com.nttdata.goods.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleOrdenDTO {

    private Integer idOrden;
    private Integer idArticulo;
    private String tipoOrden;
    private String tipoOperacion;
    private Integer cantidad;
    private BigDecimal precioUnitario;

}
