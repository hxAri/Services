package org.hxari.payload.request;

public record ProductInfoRequest(
	String brand,
	String category,
	String description,
	String name,
	Double price,
	Integer quantity
) {
}
