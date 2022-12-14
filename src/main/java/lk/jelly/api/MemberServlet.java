package lk.jelly.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lk.jelly.db.ConnectionPool;
import lk.jelly.dto.MemberDTO;
import lk.jelly.util.HttpServlet2;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "MemberServlet", value = "/members/*")
public class MemberServlet extends HttpServlet2 {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getPathInfo() == null || request.getPathInfo().equals("/")){
            String query = request.getParameter("q");
            String size = request.getParameter("size");
            String page = request.getParameter("page");

            if (query != null && size != null && page!= null){
                if (!size.matches("\\d+") || !page.matches("\\d+")){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or size");
                }else {
                    searchPaginatedMembers(query, Integer.parseInt(size), Integer.parseInt(page), response);
                }
            } else if (query != null) {
                searchMembers(query, response);
            } else if (size != null && page != null) {
                if (!size.matches("\\d+") || !page.matches("\\d+")){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or size");
                }else {
                    loadPaginatedAllMembers(Integer.parseInt(size), Integer.parseInt(page), response);
                }
            }else {
                loadAllMembers(response);
            }
        }else {
            Matcher matcher = Pattern.compile("^/([A-Fa-f0-9]{8}(-[A-Fa-f0-9]{4}){3}-[A-Fa-f0-9]{12})$").matcher(request.getPathInfo());
            if (matcher.matches()){
                getMemberDetails(request.getPathInfo(), response);
            }else {
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Expected valid UUID");
            }
        }
    }

    private void loadAllMembers(HttpServletResponse response) throws IOException {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep9_lms", "root", "Dasun@1996")) {
                ConnectionPool pool = (ConnectionPool) getServletContext().getAttribute("pool");
                Connection connection = pool.getConnection();


                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM member");

                ArrayList<MemberDTO> members = new ArrayList<>();

                while(rst.next()){
                    String id = rst.getString("id");
                    String name = rst.getString("name");
                    String address = rst.getString("address");
                    String contact = rst.getString("contact");
                    MemberDTO dto = new MemberDTO(id, name, address, contact);
                    members.add(dto);
                }

                pool.releaseConnection(connection);

                Jsonb jsonb = JsonbBuilder.create();
                response.setContentType("application/json");
//                String json = jsonb.toJson(members);
//                response.getWriter().println(json);
                jsonb.toJson(members, response.getWriter());


//                StringBuilder sb = new StringBuilder();
//                sb.append("[");
//                while (rst.next()) {
//                    String id = rst.getString("id");
//                    String name = rst.getString("name");
//                    String address = rst.getString("address");
//                    String contact = rst.getString("contact");
////                    response.getWriter().printf("<h1>ID: %s, Name: %s, Address: %s, Contact: %s</h1>", id, name, address, contact);
//
//                    String jsonObj = "{\n" +
//                            "  \"id\": \""+id+"\",\n" +
//                            "  \"name\": \""+name+"\",\n" +
//                            "  \"address\": \""+address+"\",\n" +
//                            "  \"contact\": \""+contact+"\"\n" +
//                            "}";
//                    sb.append(jsonObj).append(",");
//                }
//                sb.deleteCharAt(sb.length() - 1);
//                sb.append("]");
//                response.setContentType("application/json");
//                response.getWriter().println(sb);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load members");
        }
    }

    private void loadPaginatedAllMembers(int size, int page, HttpServletResponse response) throws IOException {
        response.getWriter().printf("WS : Load all paginated members %d %d", size, page);
    }

    private void  searchMembers (String query , HttpServletResponse response) throws IOException {
        response.getWriter().printf("WS : Search members, query : %s", query);
    }

    private void searchPaginatedMembers(String query, int size, int page, HttpServletResponse response) throws IOException {
        response.getWriter().printf("WS : Search paginated members : %s, %d %d", query, size, page);
    }

    private void getMemberDetails(String memberId, HttpServletResponse response) throws IOException {
        response.getWriter().printf("WS : Get member details : %s", memberId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("MemberServlet.doDelete()");
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("MemberServlet.doPatch()");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("MemberServlet.doPost()");
    }
}
