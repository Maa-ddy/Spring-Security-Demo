package maaddy.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import maaddy.util.JWTprovider;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired private JWTprovider jwtProvider;
	@Autowired private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String jwt = request.getHeader("Authorization");
		//System.out.println(jwt);
		if (jwt != null && jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7);
			String username = this.jwtProvider.extractUsername(jwt);
			if (username != null
					&& SecurityContextHolder.getContext().getAuthentication() == null) {
				
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (this.jwtProvider.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
							userDetails, 
							null,
							userDetails.getAuthorities()
					);
					System.out.println("valid");
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
