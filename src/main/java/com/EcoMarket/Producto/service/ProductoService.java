package com.EcoMarket.Producto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EcoMarket.Producto.model.Producto;
import com.EcoMarket.Producto.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        if (productoRepository.existsById(id)) {
            productoDetalles.setId(id);
            return productoRepository.save(productoDetalles);
        }
        return null;
    }

    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Producto> buscarTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarProductoPorId(Long id) {
        return productoRepository.findById(id);
    }
}
