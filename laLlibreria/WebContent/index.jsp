<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<body>
<div class="search">
	<center><s:form action = "searchBook">
	<s:textfield name="key" label="search:" />
	<s:submit value="search"/>
	</s:form>
	</center>
	<s:if test="results == hola">
	<div class="content">
			<s:iterator value="results" status="userStatus">
			<br
				class="<s:if test="#userStatus.odd == true ">odd</s:if><s:else>even</s:else>">
				<td><s:property value="autor" /></td>
				<td><s:property value="titol" /></td>
				<td><s:property value="descripcio" /></td>
				<td><s:property value="aboutYou" /></td>
				<td><s:property value="mailingList" /></td>
			<br>
		</s:iterator>
	</div>
	</s:if>
		<h2><a href=error.vt>$result.titol</a></h2>

</div>
</body>
</html>