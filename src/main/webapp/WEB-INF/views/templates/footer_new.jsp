<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

	<footer id="footer">
		<div class="footer-left">
			<p><!-- <a href="#"><spring:message code="footer.media"/></a> | --><a href="<c:url value='/imprint' />"><spring:message code="footer.imprint"/></a> | <a href="<c:url value='/support' />"><spring:message code="footer.support"/></a> | <spring:message code="footer.contact"/>: info@radius-schweiz.ch<!-- | <a href="#"><spring:message code="footer.privacy"/></a>--></p>
		</div>
		<div class="footer-right">
			<ul id="s-icons">
        		<a href="https://www.facebook.com/radius.schweiz"><li><img src="img/icon_f.png" class="icon"></li></a>
        		<a href="https://twitter.com/Radius_Schweiz"><li><img src="img/icon_t.png" class="icon"></li></a>
        		<a href="https://www.instagram.com/radius_ch/"><li><img src="img/icon_i.png" class="icon"></li></a>
    		</ul>
    	</div>
    </footer>
</body>
</html>