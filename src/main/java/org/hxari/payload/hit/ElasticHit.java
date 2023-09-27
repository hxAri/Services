package org.hxari.payload.hit;

public record ElasticHit<Source>( String id, String index, Source source ) {
}
