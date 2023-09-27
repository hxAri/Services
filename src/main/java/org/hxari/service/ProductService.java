package org.hxari.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.hxari.exception.ProductNotFoundException;
import org.hxari.model.ProductModel;
import org.hxari.model.ProductModel.ProductPay;
import org.hxari.payload.ProductPayload;
import org.hxari.payload.hit.ElasticHit;
import org.hxari.payload.request.ProductInfoRequest;
import org.hxari.payload.request.ProductPayRequest;
import org.hxari.payload.request.ProductRequest;
import org.hxari.util.HexUtil;
import org.hxari.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;

@Service
public class ProductService {

	@Autowired
	private ElasticsearchClient client;

	@Value( "${service.elastic.index.shop}" )
	private String index;

	public void create( ProductModel product ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		this.create( HexUtil.encode( SecurityUtil.salt( 6 ) ), product );
	}

	public void create( String id, ProductModel product ) throws IOException {
		if( product.getRelease() == null ) {
			product.setRelease( LocalDateTime.now( ZoneId.of( "Asia/Jakarta" ) ) );
		}
		this.client.create( request -> request.index( this.index ).id( id ).document( product ).refresh( Refresh.True ) ).result();
	}

	public void delete( String id ) throws IOException {
		this.client.delete( delete -> delete.index( this.index ).id( id ) );
	}

	public List<ElasticHit<ProductModel>> getAll() throws IOException {
		SearchResponse<ProductModel> response = this.client.search( search -> search.index( this.index ), ProductModel.class );
		List<ElasticHit<ProductModel>> products = new ArrayList<>();
		for( Hit<ProductModel> hit : response.hits().hits() ) {
			products.add(
				new ElasticHit<>(
					hit.id(),
					hit.index(),
					hit.source()
				)
			);
		}
		return products;
	}

	public ElasticHit<ProductModel> getById( String id ) throws IOException {
		GetResponse<ProductModel> response = this.client.get( get -> get.index( this.index ).id( id ), ProductModel.class );
		if( response.found() ) {
			return new ElasticHit<ProductModel>( 
				response.id(), 
				response.index(), 
				response.source()
			);
		}
		throw new ProductNotFoundException( "Product not found not available" );
	}

	public List<ElasticHit<ProductModel>> getByOwner( String owner ) throws IOException {
		return this.getByStr( "owner", owner );
	}

	public List<ElasticHit<ProductModel>> getByStr( String field, String value ) throws IOException {
		try {
			SearchResponse<ProductModel> response = this.client.search( search -> search
				.index( this.index )
				.query( query -> query 
					.bool( bool -> bool
						.must( must -> must
							.match( match -> match
								.field( field )
								.query( value )
							)
						)
					)
				),
				ProductModel.class 
			);
			HitsMetadata<ProductModel> meta = response.hits();
			List<ElasticHit<ProductModel>> products = new ArrayList<>();
			TotalHits total = meta.total();
			if( total != null ) {
				if( total.value() >= 1 ) {
					for( Hit<ProductModel> hit : meta.hits() ) {
						products.add(
							new ElasticHit<>(
								hit.id(),
								hit.index(),
								hit.source()
							)
						);
					}
				}
			}
			return products;
		}
		catch( IOException e ) {
			throw e;
		}
	}

	public void update( String id, ElasticHit<ProductModel> hit, ProductRequest payload ) throws IOException {
		this.update( id, hit.source(), new ProductPayload( 
			payload.brand(), 
			payload.category(), 
			payload.description(), 
			payload.features(), 
			payload.name(), 
			payload.pay(), 
			payload.price(), 
			payload.quantity(), 
			null
		));
	}

	public void update( String id, ElasticHit<ProductModel> hit, ProductInfoRequest payload ) throws IOException {
		this.update( id, hit.source(), new ProductPayload( payload.brand(), payload.category(), payload.description(), null, payload.name(), null, payload.price(), payload.quantity(), null ) );
	}

	public void update( String id, ElasticHit<ProductModel> hit, ProductPayRequest payload ) throws IOException {
		this.update( id, hit.source(), new ProductPayload( null, null, null, null, null, new ProductPay( payload.method(), payload.currency() ), null, null, null ) );
	}

	public void update( String id, ProductModel product, ProductPayload payload ) throws IOException {
		ProductPay pays = product.getPay();
		if( payload.pay() != null ) {
			if( payload.pay().method() != null && payload.pay().method() != pays.method() ) {
				pays = pays.withMethod( payload.pay().method() );
			}
			if( payload.pay().method() != null && payload.pay().currency() != pays.currency() ) {
				pays = pays.withCurrency( payload.pay().currency() );
			}
			product.setPay( pays );
		}
		if( payload.ratings() != null )
			product.setRatings( payload.ratings() );
		if( payload.brand() != null && payload.brand() != product.getBrand() )
			product.setBrand( payload.brand() );
		if( payload.category() != null && payload.category() != product.getCategory() )
			product.setCategory( payload.category() );
		if( payload.description() != null && payload.description() != product.getDescription() )
			product.setDescription( payload.description() );
		if( payload.features() != null )
			product.setFeatures( payload.features() );
		if( payload.name() != null && payload.name() != product.getName() )
			product.setName( payload.name() );
		if( payload.price() != null && payload.price() != product.getPrice() )
			product.setPrice( payload.price() );
		if( payload.quantity() != null && payload.quantity() != payload.quantity() )
			product.setQuantity( payload.quantity() );
		this.client.update( update -> update.index( this.index ).id( id ).doc( product ), ProductModel.class );
	}

}
