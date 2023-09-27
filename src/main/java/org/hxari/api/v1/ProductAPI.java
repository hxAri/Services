package org.hxari.api.v1;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import org.hxari.model.ProductModel;
import org.hxari.model.UserModel;
import org.hxari.payload.hit.ElasticHit;
import org.hxari.payload.request.ProductInfoRequest;
import org.hxari.payload.request.ProductRequest;
import org.hxari.payload.response.BodyResponse;
import org.hxari.payload.response.ProductResponse;
import org.hxari.payload.response.ProductsResponse;
import org.hxari.service.JwtService;
import org.hxari.service.ProductService;
import org.hxari.util.HexUtil;
import org.hxari.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping( path="/api/v1/shop" )
public class ProductAPI {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ProductService productService;

	@RequestMapping( method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<ProductsResponse<ElasticHit<ProductModel>>>> index( HttpServletRequest request ) throws IOException {
		List<ElasticHit<ProductModel>> products = this.productService.getByOwner( this.self( request ).id() );
		return new ResponseEntity<>(
			new BodyResponse<>( "success", "ok", 200,
				new ProductsResponse<>(
					products.size(),
					products
				)
			),
			HttpStatus.OK
		);
	}

	@RequestMapping( method=RequestMethod.POST )
	public ResponseEntity<BodyResponse<ElasticHit<ProductModel>>> create( HttpServletRequest request, @RequestBody( required=true ) ProductRequest body ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		ElasticHit<UserModel> hit = this.self( request );
		ProductModel product = new ProductModel();
		String id = HexUtil.encode( SecurityUtil.salt( 6 ) );
		this.productService.create( id, product
			.setBrand( body.brand() )
			.setCategory( body.category() )
			.setDescription( body.description() )
			.setFeatures( body.features() )
			.setName( body.name() )
			.setOwner( hit.id() )
			.setPay( body.pay() )
			.setPrice( body.price() )
			.setQuantity( body.quantity() )
		);
		return new ResponseEntity<>(
			new BodyResponse<>( "success", "created", 201,
				this.productService.getById( id )
			),
			HttpStatus.CREATED
		);
	}

	@RequestMapping( path="/{id}", method=RequestMethod.GET )
	public ResponseEntity<BodyResponse<ProductResponse<ElasticHit<ProductModel>>>> get( @PathVariable String id ) throws IOException {
		return new ResponseEntity<>(
			new BodyResponse<>( "success", "ok", 200,
				new ProductResponse<>(
					this.productService.getById( id )
				)
			),
			HttpStatus.OK
		);
	}

	@RequestMapping( path="/{id}", method=RequestMethod.DELETE )
	public ResponseEntity<BodyResponse<Void>> delete( @PathVariable String id ) throws IOException {
		this.productService.delete( id );
		return new ResponseEntity<>(
			new BodyResponse<>( 
				"deleted", 
				"ok", 
				200, 
				null 
			),
			HttpStatus.OK
		);
	}

	@RequestMapping( path="/{id}", method=RequestMethod.PUT )
	public ResponseEntity<BodyResponse<ProductResponse<ElasticHit<ProductModel>>>> update( @PathVariable String id, @RequestBody( required=true ) ProductInfoRequest body ) throws IOException {
		ElasticHit<ProductModel> hit = this.productService.getById( id );
		this.productService.update( id, hit, body );
		return new ResponseEntity<>(
			new BodyResponse<>( "success", "ok", 200,
				new ProductResponse<>( hit )
			),
			HttpStatus.OK
		);
	}

	final private ElasticHit<UserModel> self( HttpServletRequest request ) throws IOException {
		return this.jwtService.getUser( request.getHeader( HttpHeaders.AUTHORIZATION ) );
	}

}
