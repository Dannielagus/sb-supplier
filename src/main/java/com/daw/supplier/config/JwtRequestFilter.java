package com.daw.supplier.config;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.daw.supplier.model.DataToken;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private RestTemplate template;
	
	@Value("${user.service.url}")
	private String userServiceUrl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			String jwtToken = requestTokenHeader.substring(7);
			String url = userServiceUrl + "/get-user/" + jwtToken;
			try {
				DataToken dataToken = template.getForObject(url, DataToken.class);

				if (dataToken.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null
						&& !dataToken.isTokenExpired()) {
					UserDetails userDetails = new org.springframework.security.core.userdetails.User(
							dataToken.getUsername(), dataToken.getPassword(), new ArrayList<>());

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(dataToken);
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (Exception ex) {
			}

		}

		chain.doFilter(request, response);
	}

}