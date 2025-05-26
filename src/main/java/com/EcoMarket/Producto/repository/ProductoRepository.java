package com.EcoMarket.Producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EcoMarket.Producto.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
