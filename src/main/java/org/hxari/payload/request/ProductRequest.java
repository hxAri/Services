package org.hxari.payload.request;

import java.util.Map;

import org.hxari.model.ProductModel.ProductPay;

public record ProductRequest(
	String brand,
	String category,
	String description,
	Map<String, String> features,
	String name,
	ProductPay pay,
	Double price,
	Integer quantity
) {
}
