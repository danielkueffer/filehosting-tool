package com.danielkueffer.filehosting.auth;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.danielkueffer.filehosting.controller.AuthController;


/**
 * Filter checks if LoginController has loginIn property set to true. If it is
 * not set then request is being redirected to the login.xhml page.
 * 
 * @author dkueffer
 * 
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

	@Inject
	AuthController authController;

	/**
	 * Checks if the user is logged in
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		
		if (req.getServletPath().equals("/login.xhtml")) {
			if (authController != null && authController.isLoggedIn()) {
	
				// User is logged in, so just continue request.
				chain.doFilter(request, response);
			} else {
				// User is not logged in, so redirect to index.
				HttpServletResponse res = (HttpServletResponse) response;
				res.sendRedirect(req.getContextPath() + "/login.xhtml");
			}
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
