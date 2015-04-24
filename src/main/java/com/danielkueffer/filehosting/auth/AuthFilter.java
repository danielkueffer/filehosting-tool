package com.danielkueffer.filehosting.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	/**
	 * Checks if the user is logged in
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getServletPath();

		// Get the AuthControler from the session
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		AuthController authController = (session != null) ? (AuthController) session
				.getAttribute("authController") : null;

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
