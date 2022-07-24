/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.common.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * XSS过滤
 *
 * @author admin@gzaswl.net
 */
public class XssFilter implements Filter {

	private String[] excludedUris;

//	private Set<String> excludedUriSet=new HashSet<String>();

	@Override
	public void init(FilterConfig config) throws ServletException {
		excludedUris = config.getInitParameter("asExclude").split(",");
//		excludedUriSet.addAll(Arrays.asList(config.getInitParameter("asExclude").split(",")));
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);

		if (isExcludedUri(xssRequest.getServletPath())){
			chain.doFilter(request, response);
		}else {
			chain.doFilter(xssRequest, response);
		}

//		chain.doFilter(xssRequest, response);
	}

	private boolean isExcludedUri(String uri) {
		if (excludedUris == null || excludedUris.length <= 0) {
			return false;
		}
		for (String ex : excludedUris) {
			uri = uri.trim();
			ex = ex.trim();
			if (uri.toLowerCase().matches(ex.toLowerCase().replace("*",".*")))
				return true;
		}
		return false;

//		return excludedUriSet.contains(uri);
	}

	@Override
	public void destroy() {
	}

}