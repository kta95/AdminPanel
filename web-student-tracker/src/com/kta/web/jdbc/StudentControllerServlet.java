package com.kta.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private StudentDbUtil studentDbUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		try {
			studentDbUtil=new StudentDbUtil(dataSource);
		}catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			
			String theCommand=request.getParameter("command");
			
			if(theCommand==null) {
				theCommand= "LIST";
			}
			
			switch(theCommand) {
			
			case "LIST": 
				studentList(request,response);
				break;

			case "ADD":
				studentAdd(request,response);
				break;
				
			case "LOAD":
				studentLoad(request,response);
				break;
				
			case "UPDATE":
				studentUpdate(request,response);
				break;
			
			case "DELETE":
				studentDelete(request,response);
				break;
				
			default:
				studentList(request,response);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void studentDelete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String studentId=request.getParameter("studentId");//get student id from delete link of form
		
		studentDbUtil.deleteStudent(studentId);
		
		studentList(request, response);
	}

	private void studentUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int id=Integer.parseInt(request.getParameter("studentId"));
		String fname=request.getParameter("firstName");
		String lname=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		Student theStudent=new Student(id,fname,lname,email);
		
		studentDbUtil.updateStudent(theStudent);
		
		studentList(request, response);

	}

	private void studentLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String studentId=request.getParameter("studentId");//get request id from form 
		
		Student theStudent=studentDbUtil.getStudent(studentId);//get student data by id from database
		
		request.setAttribute("theStudent", theStudent);//place student in the attribute
		
		RequestDispatcher dispatcher=request.getRequestDispatcher("/update-student-form.jsp");//send the data to jsp page
		
		dispatcher.forward(request, response);
	}

	private void studentAdd(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		Student std=new Student(firstName,lastName,email);
		
		studentDbUtil.addStudent(std);
		
		studentList(request, response);
	}

	private void studentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<Student> data= studentDbUtil.getStudent();
		request.setAttribute("Student_List", data);		
		RequestDispatcher dispatcher=request.getRequestDispatcher("/list-student.jsp");		
		dispatcher.forward(request, response);
	}
	

	

}
