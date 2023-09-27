package org.hxari.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductModel {

	private String brand;
	private String category;
	private String description;
	private Map<String, String> features;
	private String name;
	private String owner;
	private ProductPay pay;
	private Double price;
	private Integer quantity;
	private List<ProductRating> ratings;

	@JsonFormat( pattern="yyyy-MM-dd'T'HH:mm:ss" )
	private LocalDateTime release;

	public ProductModel() {
		this.features = this.features != null ? this.features : new HashMap<>();
		this.ratings = this.ratings != null ? this.ratings : new ArrayList<>();
		this.release = this.release != null ? this.release : LocalDateTime.now( ZoneId.of( "Asia/Jakarta" ) );
	}

	public ProductModel( String brand, String category, String description, Map<String, String> features, String name, String owner, ProductPay pay, Double price, Integer quantity, List<ProductRating> ratings ) {
		this();
		this.brand = brand;
		this.category = category;
		this.description = description;
		this.features = features;
		this.name = name;
		this.owner = owner;
		this.pay = pay;
		this.price = price;
		this.quantity = quantity;
		this.ratings = ratings;
	}

	public String getBrand() {
		return this.brand;
	}

	public String getCategory() {
		return this.category;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<String, String> getFeatures() {
		return this.features;
	}

	public String getName() {
		return this.name;
	}

	public String getOwner() {
		return this.owner;
	}

	public ProductPay getPay() {
		return this.pay;
	}

	public Double getPrice() {
		return this.price;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public List<ProductRating> getRatings() {
		return this.ratings;
	}

	public LocalDateTime getRelease() {
		return this.release;
	}

	public ProductModel setBrand( String brand ) {
		this.brand = brand;
		return this;
	}

	public ProductModel setCategory( String category ) {
		this.category = category;
		return this;
	}

	public ProductModel setDescription( String description ) {
		this.description = description;
		return this;
	}

	public ProductModel setFeatures( Map<String, String> features ) {
		if( features == null ) {
			features = new LinkedHashMap<>();
		}
		this.features = features;
		return this;
	}

	public ProductModel setName( String name ) {
		this.name = name;
		return this;
	}

	public ProductModel setOwner( String owner ) {
		this.owner = owner;
		return this;
	}

	public ProductModel setPay( ProductPay pay ) {
		this.pay = pay;
		return this;
	}

	public ProductModel setPrice( Double price ) {
		this.price = price;
		return this;
	}

	public ProductModel setQuantity( Integer quantity ) {
		this.quantity = quantity;
		return this;
	}

	public ProductModel setRatings( List<ProductRating> ratings ) {
		if( ratings == null ) {
			ratings = new LinkedList<>();
		}
		this.ratings = ratings;
		return this;
	}

	public ProductModel setRelease( LocalDateTime release ) {
		this.release = release;
		return this;
	}

	public record ProductPay( String method, String currency ) {
		public ProductPay withMethod( String method ) {
			return new ProductPay( method, this.currency );
		}
		public ProductPay withCurrency( String currency ) {
			return new ProductPay( this.method, currency );
		}
	}

	public record ProductRating( String description, Double rating, String user ) {
		public ProductRating withDescription( String description ) {
			return new ProductRating( description, this.rating, this.user );
		}
		public ProductRating withRating( Double rating ) {
			return new ProductRating( this.description, rating, this.user );
		}
		public ProductRating withUser( String user ) {
			return new ProductRating( this.description, this.rating, user );
		}
	}

}
