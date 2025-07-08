package com.EcoMarket.Producto.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import com.EcoMarket.Producto.assemblers.ProductoModelAssembler;
import com.EcoMarket.Producto.model.Producto;
import com.EcoMarket.Producto.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler productoModelAssembler;

    // Para crear un producto
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            EntityModel<Producto> entityModel = productoModelAssembler.toModel(nuevoProducto);
            return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Para obtener todos los productos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarTodosLosProductos() {
        List<Producto> productos = productoService.buscarTodosLosProductos();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<Producto>> productoRecursos = productos.stream()
                .map(productoModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productoRecursos,
                linkTo(methodOn(ProductoController.class).buscarTodosLosProductos()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    // Para obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> buscarProductoPorId(@PathVariable Long id) {
        return productoService.buscarProductoPorId(id)
                .map(productoModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Para actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id,
            @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            if (productoActualizado != null) {
                EntityModel<Producto> entityModel = productoModelAssembler.toModel(productoActualizado);
                return new ResponseEntity<>(entityModel, HttpStatus.OK);
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