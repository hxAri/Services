package org.hxari.payload;

import java.util.List;
import java.util.Map;

import org.hxari.model.ProductModel.ProductPay;
import org.hxari.model.ProductModel.ProductRating;

public record ProductPayload(
	String brand,
	String category,
	String description,
	Map<String, String> features,
	String name,
	ProductPay pay,
	Double price,
	Integer quantity,
	List<ProductRating> ratings
) {
}
