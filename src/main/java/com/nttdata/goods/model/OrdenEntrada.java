package com.nttdata.goods.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdenEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrdenEntrada;

    @Column(name = "fechaentrada", nullable = false)
    private LocalDate fechaentrada;

    @Column(name = "estado")
    private String estado;

    @Column(name = "idProveedor")
    private Integer idProveedor;


}
