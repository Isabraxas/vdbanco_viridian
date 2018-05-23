package com.vdbanco.viridianDummy.security;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.vdbanco.viridianDummy.security.Constants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {

			UserModel credenciales = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					credenciales.getUserName(), credenciales.getUserPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> grantedAuthorityList = auth
                .getAuthorities();

        List<String> authorities = new ArrayList<String>();

        for (GrantedAuthority grantedAuthority : grantedAuthorityList) {
            authorities.add(grantedAuthority.getAuthority());
        }

        String login = ((User)auth.getPrincipal()).getUsername();
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", authorities);

		String token = Jwts.builder()
				//.setSubject(((User)auth.getPrincipal()).getUsername())
				.setClaims(claims)
                .setIssuedAt(new Date())
                //.setIssuer(ISSUER_INFO)
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
		response.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + " " + token);
	}
}
