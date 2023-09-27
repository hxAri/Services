package org.hxari.payload.response;

import org.hxari.model.ProductModel;
import org.hxari.payload.hit.ElasticHit;

public record ProductResponse<Product>( ElasticHit<ProductModel> product ) {
}
