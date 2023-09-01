package org.hxari.response;

public record BodyResponse<Body>( String message, String status, int code, Body data )
{}
