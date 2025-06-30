package com.EcoMarket.Producto.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    // Para crear un producto
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            addSelfLink(nuevoProducto);
            EntityModel<Producto> recurso = EntityModel.of(nuevoProducto);
            return new ResponseEntity<>(recurso, HttpStatus.CREATED);
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
                .map(producto -> {
                    addSelfLink(producto);
                    return EntityModel.of(producto);
                })
                .collect(Collectors.toList());

        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(ProductoController.class).buscarTodosLosProductos());
        CollectionModel<EntityModel<Producto>> recurso = CollectionModel.of(productoRecursos, linkBuilder.withSelfRel());

        return new ResponseEntity<>(recurso, HttpStatus.OK);
    }

    // Para obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> buscarProductoPorId(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoService.buscarProductoPorId(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            addSelfLinkAndCollectionLink(producto);
            EntityModel<Producto> recurso = EntityModel.of(producto);
            return new ResponseEntity<>(recurso, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Para actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            if (productoActualizado != null) {
                addSelfLinkAndCollectionLink(productoActualizado);
                EntityModel<Producto> recurso = EntityModel.of(productoActualizado);
                return new ResponseEntity<>(recurso, HttpStatus.OK);
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

    private void addSelfLink(Producto producto) {
        producto.add(linkTo(methodOn(ProductoController.class).buscarProductoPorId(producto.getId())).withSelfRel());
    }

    private void addSelfLinkAndCollectionLink(Producto producto) {
        addSelfLink(producto);
        producto.add(linkTo(methodOn(ProductoController.class).buscarTodosLosProductos()).withRel("todos-los-productos"));
    }

}