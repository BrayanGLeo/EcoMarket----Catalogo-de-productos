package com.EcoMarket.Producto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.EcoMarket.Producto.model.Producto;
import com.EcoMarket.Producto.repository.ProductoRepository;

@SpringBootTest
public class ProductoServiceTest {

    @MockBean
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    private Producto producto;
    private Producto productoDetalles;

    @BeforeEach
    void setUp() {
        producto = new Producto(1L, "Azucar flor", "Bolsa de azucar flor de 1kg", 990.00, 10, "Alimentos");
        productoDetalles = new Producto(1L, "Azucar flor nueva", "Bolsa de azucar flor de 1.2 kg", 1300.00, 80,
                "Alimentos");
    }

    @Test
    void testCrearProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto nuevoProducto = productoService.crearProducto(new Producto());
        assertNotNull(nuevoProducto);
        assertEquals("Azucar flor", nuevoProducto.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoDetalles);

        Producto productoActualizado = productoService.actualizarProducto(1L, productoDetalles);
        assertNotNull(productoActualizado);
        assertEquals(1L, productoActualizado.getId());
        assertEquals("Azucar flor nueva", productoActualizado.getNombre());
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).save(productoDetalles);
    }

    @Test
    void testActualizarProducto_NoExistente() {
        when(productoRepository.existsById(1L)).thenReturn(false);
        Producto resultado = productoService.actualizarProducto(1L, productoDetalles);
        assertNull(resultado);
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void testEliminarProducto() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        boolean resultado = productoService.eliminarProducto(1L);
        assertTrue(resultado);
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProducto_NoExistente() {
        when(productoRepository.existsById(1L)).thenReturn(false);
        boolean resultado = productoService.eliminarProducto(1L);
        assertFalse(resultado);
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, never()).deleteById(1L);
    }

    @Test
    void testBuscarTodosLosProductos() {
        Producto producto2 = new Producto(2L, "Fideos Caracol", "Bolsa de fideos caracoles de 400g", 990.00, 50,
                "Alimentos");
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto, producto2));

        List<Producto> productos = productoService.buscarTodosLosProductos();
        assertNotNull(productos);
        assertEquals(2, productos.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarProductoPorId() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.buscarProductoPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Azucar flor", resultado.get().getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }
}