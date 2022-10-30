package lk.jelly.api;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.jelly.db.ConnectionPool;
import lk.jelly.util.HttpServlet2;

import java.io.IOException;

@WebServlet(name = "TempServlet2", value = "/release")
public class TempServlet2 extends HttpServlet2 {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool pool = (ConnectionPool) getServletContext().getAttribute("pool");
        pool.releaseAllConnections();
    }
}
