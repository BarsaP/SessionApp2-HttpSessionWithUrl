package com.nt.servlet;

/*Date:-05/08/2022
 * Http Session with URL Rewriting
 * ===============================
 * The biggest limitation of HttpSession with cookies is, the session tracking fails if cookies are restricted/ blocked
 * coming to browser s/w because the session id go to browser s/w from web application and comes back to web application from
 * browser in the form of cookies..
 * 
 * =>To overcome the problem, no depend upon cookies for sending and recieving session id .. rather append
 * session id to all the urls that go to broswer s/w from web application aling the response and comes back
 * to web application along with request (This is called URL Rewritting process i.e rewriting the url by 
 * appending the session id)
 * 
 * =>The action url, href of dynamic forms and dynamic hyperlinks generated through servlet, jsp comps go to
 *   browser s/w from web application along with the response and comes back to web application from browser s/w
 *   along with the request..
 *   So we can append session id to these urls by using URL Rewriting concepts.
 *   
 *          res.encodeURL("secondurl");
 *                  |
 *        gives secondurl;jSessionid = 65467FFFAAFGFG    
 *               --------------------------------------
 *                 The Session id of the current Session object will be appended to the given url dynamically..
 *                       
 * => By rewriting the all the url of dynamic forms and hyperlinks with session ids from servlet/jsp components 
 *    we can make the session ids going to browser from web application and coming back to web application from
 *    broswer..This is nothing but session tracking with url rewriting.
 *    
 * => If you are getting ses is null (nullPointerException) then give a space between  
 *    <form action="+res.encodeUrl("secondurl")+" method='post'> secondurl and method.
 * => block the localhost cookies in settings 
 * Advantages of HttpSession with URL Rewriting
 * ============================================
 * => Same as HttpSession with cookies..but the Session tracking will be continued even though
 *    the cookies are restricted coming to browser window
 * Disadvantages
 * =============
 * => same as HttpSession with cookies but
 *    a) appending session id to urls by doing url rewriting is very complex process
 *       because we need to do this work on every url that goes to  
 * Try to read the below documentation    
 * https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletResponse.html      
 * 
 * Encodes the specified URL by including the session ID, or, if encoding is not needed, returns the URL unchanged. The implementation of this method includes the logic to determine whether the session ID needs to be encoded in the URL. For example, if the browser supports cookies, or session tracking is turned off, URL encoding is unnecessary.
For robust session tracking, all URLs emitted by a servlet should be run through this method. Otherwise, URL rewriting cannot be used with browsers which do not support cookies.

If the URL is relative, it is always relative to the current HttpServletRequest.

Parameters:
url - the url to be encoded.
Returns:
the encoded URL if encoding is needed; the unchanged URL otherwise.
Throws:
IllegalArgumentException - if the url is not valid    

Can you tell me which Session tracking should be used in which situation ?
Ans) if website is having huge customer base and the session data is not so sensitive data..
     then go for "http cookies" as the session tracking technique becoz client data will be allocated at client site itself
     eg: flipkart.com, amazon.in, google, facebook data like enquires, purchases and etc..          
   If website is having less customer base and the session data is sensitive data like username and password...then go for  
   "HttpSession with URL rewriting" technique as the Session tracking technique.
    
    eg: Banking Apps, Financial Services Apps, payment apps and etc..
 
 note: if needed we can use multiple Session Tracking techniques in the same web application
       =>use HttpSession with URL rewriting for login activities based Session tracking (sensitive data)
       =>Use Http cookies for shopping cart activities based Session tracking (Insensitive Data) // Any E-commerce app
       
   Can you tell me the realtime use-cases of Servlet(request, session and ServletContext attributes) attributes ?
   
      a) In layered Apps (like MVC Apps) to pass data from one web comp to another web comp of communication go for
           request attributes
      b) For Session Tracking use Session attributes
      
      C) For managing global data of the web application use ServletContext attributes like requests count(session count, user count) and etc...  
          
 * */
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/firsturl")
public class FirstServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//get PrintWriter
		PrintWriter pw=res.getWriter();
		//set response content type
		res.setContentType("text/html");
		
		//read form data(form1/req1 data)
		String name=req.getParameter("pname");
		String fname=req.getParameter("fname");
		String ms=req.getParameter("ms");
		//create HttpSession object
		HttpSession ses=req.getSession();
		
		//add form1/req1 data to Session obj as session attribute values
		ses.setAttribute("name",name);
		ses.setAttribute("fname",fname);
		ses.setAttribute("ms",ms);
		
		//session's idle timeout period
		ses.setMaxInactiveInterval(60);
		
		//generate dynamic form page based on the marital status
		if(ms.equalsIgnoreCase("single")) {
			pw.println("<h1 style='color:red;text-align:center'>Person Registration(Bachelor life)---shadi.com</h1>)");
			pw.println("<form action="+res.encodeUrl("secondurl")+" method='post'>");
			pw.println("<table border='0'bgcolor='cyan'align='center'>");
			pw.println("<tr><td> when do u want to marry?</td>");
			pw.println("<td><input type='text' name='f2t1'></td></tr>");
			pw.println("<tr><td>why do you want to marry?</td>");
			pw.println("<td><input type='text' name='f2t2'></td></tr>");
			pw.println("<tr><td colspan='2'><input type='submit' value='submit'></td></tr>");
			pw.println("</table>");
			pw.println("</form>");
		}
		else {
			pw.println("<h1 style='color:red;text-align:center'>Person Registration(Married life)---shadi.com</h1>)");
			//here secondurl with session id will be their
			pw.println("<form action="+res.encodeUrl("secondurl")+" method='post'>");
			pw.println("<table border='0'bgcolor='cyan'align='center'>");
			pw.println("<tr><td> spouse name::</td>");
			pw.println("<td><input type='text' name='f2t1'></td></tr>");
			pw.println("<tr><td> No.of children::</td>");
			pw.println("<td><input type='text' name='f2t2'></td></tr>");
			pw.println("<tr><td colspan='2'><input type='submit' value='submit'></td></tr>");
			pw.println("</table>");
			pw.println("</form>");
		}//else
		
		pw.println("<br><b>Session id::"+ses.getId()+"</b>");
		//close stream
		pw.close();
	}//doGet(-,-)
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req,res);
	}//doPost(-,-)

}//class
