package com.EcoMarket.Producto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    // Para crear un producto
    @PostMapping
    public ResponseEntity<Producto> postProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Para obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> buscarTodosLosProductos() {
        List<Producto> productos = productoService.buscarTodos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // Para obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarProductoPorId(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);
        if (productoOpt.isPresent()) {
            return new ResponseEntity<>(productoOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Para actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        boolean fueEliminado = productoService.eliminarProducto(id);
        if (fueEliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}