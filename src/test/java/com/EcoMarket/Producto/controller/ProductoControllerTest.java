package com.EcoMarket.Producto.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.EcoMarket.Producto.assemblers.ProductoModelAssembler;
import com.EcoMarket.Producto.model.Producto;
import com.EcoMarket.Producto.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductoController.class)
@Import(ProductoModelAssembler.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;
    private Producto productoActualizado;

    @BeforeEach
    void setUp() {
        producto = new Producto(1L, "Coca Cola 2L", "Coca Cola botella desechable de 2L", 1890.00, 30, "Bebestibles");
        productoActualizado = new Producto(1L, "Coca Cola 2L Retornable", "Coca Cola botella retornable de 2L", 1490.00,
                50, "Bebestibles");
    }

    @Test
    void testCrearProducto() throws Exception {
        when(productoService.crearProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre").value("Coca Cola 2L"))
                .andExpect(jsonPath("$.precio").value(1890.00))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.productos.href").exists());
    }

    @Test
    void testCrearProducto_ConErrorDeServicio() throws Exception {
        when(productoService.crearProducto(any(Producto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testBuscarTodosLosProductos() throws Exception {
        Producto producto2 = new Producto(2L, "Bebida Mas 1.5L", "Bebida Mas botella de 1.5L", 1190.00, 50,
                "Bebestibles");
        List<Producto> listaProductos = Arrays.asList(producto, producto2);
        when(productoService.buscarTodosLosProductos()).thenReturn(listaProductos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre").value("Coca Cola 2L"))
                .andExpect(jsonPath("$._embedded.productoList[1].nombre").value("Bebida Mas 1.5L"))
                .andExpect(jsonPath("$._embedded.productoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testBuscarTodosLosProductos_Vacio() throws Exception {
        when(productoService.buscarTodosLosProductos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testBuscarProductoPorId_Encontrado() throws Exception {
        when(productoService.buscarProductoPorId(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Coca Cola 2L"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testBuscarProductoPorId_NoEncontrado() throws Exception {
        when(productoService.buscarProductoPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarProducto_Exitoso() throws Exception {
        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre").value("Coca Cola 2L Retornable"))
                .andExpect(jsonPath("$.precio").value(1490.00))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testActualizarProducto_NoEncontrado() throws Exception {
        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(null);

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarProducto_ErrorDeServicio() throws Exception {
        when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testEliminarProducto_Exitoso() throws Exception {
        when(productoService.eliminarProducto(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProducto_NoEncontrado() throws Exception {
        when(productoService.eliminarProducto(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNotFound());
    }
}