package lk.jelly.api;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TempServlet", urlPatterns = {"/temp/*", "*.php"})
public class TempServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try(PrintWriter out = response.getWriter()){
            out.println("<style>font-size : 2rem;</style>");
            out.printf("<p>Request : URI %s</p>", request.getRequestURI());
            out.printf("<p>Request : URL %s</p>", request.getRequestURL());
            out.printf("<p>Servlet path: %s</p>", request.getServletPath());
            out.printf("<p>Context path: %s</p>", request.getContextPath());
            out.printf("<p>Path Info: %s</p>", request.getPathInfo());
        }

    }
}
