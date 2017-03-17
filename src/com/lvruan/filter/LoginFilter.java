package com.lvruan.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.lvruan.po.UserInfo;


public class LoginFilter implements Filter{

	public FilterConfig config;

	public void destroy() {
		// TODO Auto-generated method stub
		config = null;
	}

	public static boolean isContains(String container, String[] regx) {
		boolean result = false;
		for (int i = 0; i < regx.length; i++) {
			if (container.indexOf(regx[i]) != -1) {
				return true;
			}
		}
		return result;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest hrequest = (HttpServletRequest) request;
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
		
		String logonStrings = config.getInitParameter("logonStrings"); // ��¼��½ҳ��
		String includeStrings = config.getInitParameter("includeStrings"); // ������Դ��׺����
		String redirectPath = hrequest.getContextPath() + config.getInitParameter("redirectPath");// û�е�½ת��ҳ��
		String disabletestfilter = config.getInitParameter("disabletestfilter");// �������Ƿ���Ч
		
		
		
		if (disabletestfilter.toUpperCase().equals("Y")) { // ������Ч
			chain.doFilter(request, response);
			return;
		}
		String[] logonList = logonStrings.split(";");
		String[] includeList = includeStrings.split(";");

		if (!this.isContains(hrequest.getRequestURI(), includeList)) {// ֻ��ָ�����˲�����׺���й���
			chain.doFilter(request, response);
			return;
		}

		if (this.isContains(hrequest.getRequestURI(), logonList)) {// �Ե�¼ҳ�治���й���
			chain.doFilter(request, response);
			return;
		}

		UserInfo user = (UserInfo) hrequest.getSession().getAttribute("user");// �ж��û��Ƿ��¼
		if (user == null) {
			wrapper.sendRedirect(redirectPath);
			return;
		} else {
			chain.doFilter(request, response);
			return;
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		  config = filterConfig;
	}

}