package com.EcoMarket.Producto.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Service;

import com.EcoMarket.Producto.model.Producto;
import com.EcoMarket.Producto.repository.ProductoRepository;

@Service
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public Producto testcrearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Test
    public Producto testactualizarProducto(Long id, Producto productoDetalles) {
        if (productoRepository.existsById(id)) {
            productoDetalles.setId(id);
            return productoRepository.save(productoDetalles);
        }
        return null;
    }

    @Test
    public boolean testeliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Test
    public List<Producto> testbuscarTodos() {
        return productoRepository.findAll();
    }
    
    @Test
    public Optional<Producto> testbuscarPorId(Long id) {
        return productoRepository.findById(id);
    }
}
