package com.kta.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;
	
	public StudentDbUtil(DataSource myDbSource) {
		this.dataSource=myDbSource;
	}
	
	public List<Student> getStudent()throws Exception{
		
		ArrayList<Student> Students=new ArrayList<>();
		
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
				
		try {
			con=dataSource.getConnection();//get connection
			String sql="select*from student order by last_name";
			st=con.createStatement();//create statement
			rs=st.executeQuery(sql);//execute statement
			
			while(rs.next()) {
				int id=rs.getInt("id");
				String fname=rs.getString("first_name");
				String lname=rs.getString("last_name");
				String email=rs.getString("email");
				
				Student newStd=new Student(id,fname,lname,email);
				
				Students.add(newStd);
			}
			
			return Students;			
		}finally {
			close(con,st,rs);
		}		
	}

	private void close(Connection con, Statement st, ResultSet rs) {

		try {
			if(rs!=null) {
				rs.close();
			}
			if(st!=null) {
				st.close();
			}
			if(con!=null) {
				con.close();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void addStudent(Student std) throws Exception{
		
		Connection mycon=null;
		PreparedStatement myst=null;
		try {
			mycon=dataSource.getConnection();
			String query="insert into student (first_name,last_name,email) values(?,?,?);";
			myst=mycon.prepareStatement(query);
			myst.setString(1, std.getFirstName());
			myst.setString(2, std.getLastName());
			myst.setString(3, std.getEmail());
			myst.execute();
		}
		finally {
			close(mycon,myst,null);
		}
	}

	public Student getStudent(String studentId) throws Exception{
		
		int sId;
		Student theStudent=null;
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			sId=Integer.parseInt(studentId);//convert id string to int
			con=dataSource.getConnection();//get connection
			String sql="select*from student where id=?";//create sql to get selected student
			pst=con.prepareStatement(sql);//prepared statement
			pst.setInt(1, sId);//set param
			rs=pst.executeQuery();//execute query
			
			if(rs.next()) {
				String fname=rs.getString("first_name");
				String lname=rs.getString("last_name");
				String email=rs.getString("email");
				
				theStudent=new Student(sId,fname,lname,email);
			}
			else {
				throw new Exception("Could not find the student id"+sId);
			}
			return theStudent;
			
		}finally {
			close(con,pst,rs);
		}
	}

	public void updateStudent(Student thStudent) throws Exception{
		Connection con=null;
		PreparedStatement pst=null;
		try {
			con=dataSource.getConnection();
			String sql = "update student set first_name=?, last_name=?, email=? where id=?";
			
			pst=con.prepareStatement(sql);
			pst.setString(1, thStudent.getFirstName());
			pst.setString(2, thStudent.getLastName());
			pst.setString(3, thStudent.getEmail());
			pst.setInt(4, thStudent.getId());
			pst.execute();
			
		}
		finally {
			close(con,pst,null);
		}
	}

	public void deleteStudent(String studentId) throws Exception {

		Connection con=null;
		PreparedStatement pst=null;
		int studentid;
		try {
			studentid=Integer.valueOf(studentId);
			con=dataSource.getConnection();//get connection
			String query="delete from student where id=?";
			pst=con.prepareStatement(query);
			pst.setInt(1, studentid);
			pst.execute();
			
		}
		finally {
			close(con,pst,null);
		}		
	}
	
}
