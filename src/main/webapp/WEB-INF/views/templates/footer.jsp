<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

	<footer>
		<div>
			<p>
				<a href="<c:url value='/media'/>"><spring:message code="footer.media"/></a> |
				<a href="<c:url value='/support' />"><spring:message code="footer.support"/></a> |
				<a href="<c:url value='/imprint' />"><spring:message code="footer.imprint"/></a> |
				<a href="<c:url value='/privacy' />"><spring:message code="footer.privacy"/></a> |
				<spring:message code="footer.contact"/>: info@radius-schweiz.ch
			</p>
		</div>
		<div>
			<ul id="s-icons">
        		<a href="https://facebook.com/radius.schweiz"><li><img src="img/icon_f.png" class="icon"></li></a>
        		<a href="https://twitter.com/Radius_Schweiz"><li><img src="img/icon_t.png" class="icon"></li></a>
        		<a href="https://github.com/lucaliechti/radius"><li><img src="img/icon_g.png" class="icon"></li></a>
    		</ul>
    	</div>
    </footer>
</body>
</html>
