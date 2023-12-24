package com.nttdata.goods.repository;

import com.nttdata.goods.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenEntradaRepository extends JpaRepository<Orden,Integer> {
}
