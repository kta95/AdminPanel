<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<html>
<head><title>Student Tracker App</title>
<link type="text/css" rel="stylesheet" href="css/style.css" />
</head>
<c:set var="students" value="${Student_List}"/>
<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>

	<div id="container">
		<div id="content">
		
		<input type="button" value="Add Student"
		 onclick="window.location.href='add-student-form.jsp';return false;"
		class="add-student-button"/> 
		
			<table>
				<tr>
					<th>FirstName</th>
					<th>LastName</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				<c:forEach var="temp" items="${students}">
				<c:url var="tempLink" value="StudentControllerServlet">
					<c:param name="command" value="LOAD"></c:param>
					<c:param name="studentId" value="${temp.id}"></c:param>
				</c:url>			
				<c:url var="deleteLink" value="StudentControllerServlet">
					<c:param name="command" value="DELETE"></c:param>
					<c:param name="studentId" value="${temp.id}"></c:param>
				</c:url>
				
					<tr>
						<td>${temp.firstName}</td>
						<td>${temp.lastName}</td>
						<td>${temp.email}</td>
						<td><a href="${tempLink}">Update</a> | <a href="${deleteLink}" onclick="if (!(confirm('Are you sure you want to delete this guy?'))) return false"> Delete</a></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>


</html>