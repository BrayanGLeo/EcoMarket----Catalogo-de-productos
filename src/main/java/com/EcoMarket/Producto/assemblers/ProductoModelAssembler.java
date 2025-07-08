package com.EcoMarket.Producto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.EcoMarket.Producto.controller.ProductoController;
import com.EcoMarket.Producto.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        EntityModel<Producto> productoModel = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).buscarProductoPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).buscarTodosLosProductos()).withRel("productos"));

        return productoModel;
    }
}