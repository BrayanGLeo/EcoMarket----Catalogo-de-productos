package com.EcoMarket.Producto.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EcoMarket.Producto.model.Producto;
import com.EcoMarket.Producto.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@WebMvcTest(ProductoController.class)
@RequestMapping("/api/productos")
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    // Para crear un producto
    @Test
    @PostMapping
    public ResponseEntity<Producto> testpostProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = ProductoService.crearProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Para obtener todos los productos
    @Test
    @GetMapping
    public ResponseEntity<List<Producto>> testbuscarTodosLosProductos() {
        List<Producto> productos = productoService.buscarTodos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // Para obtener un producto por ID
    @Test
    @GetMapping("/{id}")
    public ResponseEntity<Producto> testbuscarProductoPorId(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);
        if (productoOpt.isPresent()) {
            return new ResponseEntity<>(productoOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Para actualizar un producto
    @Test
    @PutMapping("/{id}") 
    public ResponseEntity<Producto> testactualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            if (productoActualizado != null) {
                return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Para eliminar un producto
    @Test
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> testeliminarProducto(@PathVariable Long id) {
        boolean fueEliminado = productoService.eliminarProducto(id);
        if (fueEliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}