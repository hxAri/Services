package org.hxari.payload.response;

import org.springframework.security.core.userdetails.UserDetails;

public record AuthResponse<User/**, Token */>( UserDetails user/**, String token */ ) {}
