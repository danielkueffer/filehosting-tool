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
 * Filter checks if AuthController has loginIn property set to true. If it is
 * not set then request is being redirected to the login.xhml page.
 * 
 * @author dkueffer
 * 
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	@Inject
	AuthController authController;

	/**
	 * Checks if the user is logged in
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getServletPath();

		if ((authController != null && authController.isLoggedIn())
				|| this.excludeFromFilter(path)) {

			// User is logged in, so just continue request.
			chain.doFilter(request, response);
		} else {
			// User is not logged in, so redirect to index.
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect(req.getContextPath() + "/login.xhtml");
		}
	}

	/**
	 * Exclude a path from the filter
	 * 
	 * @param path
	 * @return
	 */
	private boolean excludeFromFilter(String path) {
		if (path.startsWith("/login")
				|| path.startsWith("/javax.faces.resource")) {
			return true;
		}

		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
