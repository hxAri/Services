package org.hxari.payload.response;

import java.util.List;

import org.hxari.model.ProductModel;
import org.hxari.payload.hit.ElasticHit;

public record ProductsResponse<Products>( int length, List<ElasticHit<ProductModel>> products ) {
}
