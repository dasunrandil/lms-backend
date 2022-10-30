package lk.jelly.api;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.jelly.util.HttpServlet2;

import java.io.IOException;

@WebServlet(name = "IssueNoteServlet", value = "/issue-notes")
public class IssueNoteServlet extends HttpServlet2 {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("IssueNoteServlet.doPost()");
    }
}
