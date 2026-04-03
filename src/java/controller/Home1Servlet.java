/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Product;

/**
 *
 * @author Admin
 */
@WebServlet(name = "Home1Servlet", urlPatterns = {"/home1"})
public class Home1Servlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Home1Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Home1Servlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] pp = {"Dưới 1 triệu",
            "Từ 1-3 triệu",
            "Từ 3-5 triệu",
            "Từ 5-10 triệu",
            "Trên 10 triệu"};
        boolean[] pb = new boolean[pp.length + 1];
        DAO d = new DAO();
        List<Category> categories = d.getAll();
        boolean[] chid = new boolean[categories.size() + 1];
        List<Product> products = new ArrayList<>();
        String key = request.getParameter("key");
        if (key != null) {
            products = d.searchByKey(key);
        }
        String cid_raw = request.getParameter("cid");
        String[] cidd_raw = request.getParameterValues("cidd");
        String[] price = request.getParameterValues("price");
        int cid = 0;
        int[] cidd = null;
        if (cid_raw != null) {
            cid = Integer.parseInt(cid_raw);
            products = d.getProductsByCid(cid);
            if (cid == 0) {
                chid[0] = true;
            }
        }
        if (cidd_raw != null) {
            cidd = new int[cidd_raw.length];
            for (int i = 0; i < cidd.length; i++) {
                cidd[i] = Integer.parseInt(cidd_raw[i]);
            }
            products = d.searchByCheck(cidd);
        }
        if (price != null) {
            double from = 0, to = 0;
            for (int i = 0; i < price.length; i++) {
                List<Product> temp = new ArrayList<>();
                if (price[i].equals("0")) {
                    from = 0;
                    to = 20000;
                    products = d.getProductsByPrice(from, to);
                    pb[0] = true;
                    break;
                } else {
                    if (price[i].equals("1")) {
                        from = 0;
                        to = 1000;
                        temp = d.getProductsByPrice(from, to);
                        products.addAll(temp);
                        pb[1] = true;
                    }
                    if (price[i].equals("2")) {
                        from = 1000;
                        to = 3000;
                        temp = d.getProductsByPrice(from, to);
                        products.addAll(temp);
                        pb[2] = true;
                    }
                    if (price[i].equals("3")) {
                        from = 3000;
                        to = 5000;
                        temp = d.getProductsByPrice(from, to);
                        products.addAll(temp);
                        pb[3] = true;
                    }
                    if (price[i].equals("4")) {
                        from = 5000;
                        to = 10000;
                        temp = d.getProductsByPrice(from, to);
                        products.addAll(temp);
                        pb[4] = true;
                    }
                    if (price[i].equals("5")) {
                        from = 10000;
                        to = 20000;
                        temp = d.getProductsByPrice(from, to);
                        products.addAll(temp);
                        pb[5] = true;
                    }
                }
            }
        }
        if (price == null) {
            pb[0] = true;
        }
        if (cid_raw == null) {
            chid[0] = true;
        }
        if ((cidd_raw != null) && (cidd[0] != 0)) {
            chid[0] = false;
            for (int i = 1; i < chid.length; i++) {
                if (ischeck(categories.get(i - 1).getId(), cidd)) {
                    chid[i] = true;
                } else {
                    chid[i] = false;
                }
            }
        }

        request.setAttribute("data", categories);
        request.setAttribute("products", products);
        request.setAttribute("pp", pp);
        request.setAttribute("key", key);
        request.setAttribute("pb", pb);
        request.setAttribute("cid", cid);
        request.setAttribute("chid", chid);
        request.getRequestDispatcher("list.jsp").forward(request, response);
    }

    private boolean ischeck(int d, int[] id) {
        if (id == null) {
            return false;
        } else {
            for (int i = 0; i < id.length; i++) {
                if (id[i] == d) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
