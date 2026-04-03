/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import dal.Adminproductdao;
import java.util.List;
import model.Product;
import model.Category;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
/**
 *
 * @author LENOVO
 */
@WebServlet(name = "Adminproduct", urlPatterns = {"/Adminproduct"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB - File tạm nếu kích thước lớn
        maxFileSize = 1024 * 1024 * 10, // 10MB - Kích thước file tối đa cho phép
        maxRequestSize = 1024 * 1024 * 50 // 50MB - Tổng kích thước form tối đa
)
public class Adminproduct extends HttpServlet {

    private final Adminproductdao dao = new Adminproductdao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        Adminproductdao dao = new Adminproductdao();
        List<Product> list = dao.getallProducts();
        request.setAttribute("data", list);
        if ("update".equals(action)){
            String id = request.getParameter("id");
            Product p = dao.getProduct(id);
            request.setAttribute("productEdit", p);
        }
        else if ("checkId".equals(action)) {
            String id = request.getParameter("id");
            Product p = dao.getProduct(id);
            sendJsonProduct(response, p);
            return;
        }
        else if ("add".equals(action)){
            String id = request.getParameter("id");
            Product p = dao.getProduct(id);
            request.setAttribute("productEdit", p);
        }
        else if ("search".equals(action)) {
            String keyword = request.getParameter("query");
            List<Product> searchResult = dao.searchByName(keyword);
            request.setAttribute("data", searchResult); // ghi đè list mặc định
        }
        else if ("checkCategory".equals(action)) {
        checkCategoryAjax(request, response);
        return; // Dừng lại sau khi xử lý Ajax, không chạy code hiển thị trang bên dưới
        }
        request.getRequestDispatcher("Adminproduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "delete":
                deleteProduct(request, response);
                break;
            case "update":
                updateProduct(request, response);
                break;
            case "add":
                addProduct(request, response);
                break;
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> list = dao.getallProducts();
        request.setAttribute("data", list);
        request.getRequestDispatcher("Adminproduct.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Product p = dao.getProduct(id);
        request.setAttribute("productEdit", p);
        // Bạn có thể dùng chung Adminproduct.jsp hoặc một trang edit riêng
        request.getRequestDispatcher("Adminproduct.jsp").forward(request, response);
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = request.getParameter("id");
        dao.deleteProduct(id);
        response.sendRedirect("Adminproduct"); // Chuyển hướng về trang danh sách (gọi lại doGet)
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String describe = request.getParameter("describe");
            String releaseDate = request.getParameter("releaseDate");
            Part filePart = request.getPart("imageFile"); // "imageFile" là name của input type="file"
            String fileName = filePart.getSubmittedFileName();
            String imagePath;

            if (fileName != null && !fileName.isEmpty()) {
                String realPath = request.getServletContext().getRealPath("/images");
                File dir = new File(realPath);
                if (!dir.exists()) {
                    dir.mkdirs(); 
                }
                filePart.write(realPath + File.separator + fileName);
                imagePath = "images/" + fileName;
            } else {
                imagePath = request.getParameter("image");
            }
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            Category c = new Category();
            c.setId(Integer.parseInt(request.getParameter("category")));
            Product p = new Product(id, name, quantity, price, releaseDate, describe, imagePath, c);
            dao.updateProduct(p);
            response.sendRedirect("Adminproduct");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Adminproduct?error=1");
        }
    }
    
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String describe = request.getParameter("describe");
            String releaseDate = request.getParameter("releaseDate");
            Part filePart = request.getPart("imageFile"); // "imageFile" là name của input type="file"
            String fileName = filePart.getSubmittedFileName();
            String imagePath;

            if (fileName != null && !fileName.isEmpty()) {
                String realPath = request.getServletContext().getRealPath("/images");
                File dir = new File(realPath);
                if (!dir.exists()) {
                    dir.mkdirs(); 
                }
                filePart.write(realPath + File.separator + fileName);
                imagePath = "images/" + fileName;
            } else {
                imagePath = request.getParameter("image");
            }
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            Category c = new Category();
            c.setId(Integer.parseInt(request.getParameter("category")));
            Product p = new Product(id, name, quantity, price, releaseDate, describe, imagePath, c);
            if (dao.checkId(id)==true){
                dao.incrementQuantity(id, quantity);
            }
            else{
                dao.addProduct(p);
            }
            response.sendRedirect("Adminproduct");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Adminproduct?error=1");
        }
    }
    private void sendJsonProduct(HttpServletResponse response, Product p) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json;
        if (p != null && p.getId() != null) {
            json = "{"
                    + "\"exists\": true,"
                    + "\"id\": \"" + p.getId() + "\","
                    + "\"name\": \"" + p.getName() + "\","
                    + "\"quantity\": " + p.getQuantity() + ","
                    + "\"price\": " + p.getPrice() + ","
                    + "\"releaseDate\": \"" + p.getReleaseDate() + "\","
                    + "\"describe\": \"" + p.getDescribe() + "\","
                    + "\"image\": \"" + p.getImage() + "\","
                    + "\"categoryId\": " + (p.getCategory() != null ? p.getCategory().getId() : 0)
                    + "}";
        } else {
            json = "{\"exists\": false}";
        }

        response.getWriter().write(json);
    }
    private void checkCategoryAjax(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idRaw = request.getParameter("catId");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (idRaw != null && !idRaw.trim().isEmpty()) {
                int catId = Integer.parseInt(idRaw);
                Adminproductdao dao = new Adminproductdao();
                Category c = dao.getCategoryById(catId);

                if (c != null) {
                    // Sử dụng JSON format chuẩn
                    out.print("{\"exists\": true, \"name\": \"" + c.getName() + "\"}");
                } else {
                    out.print("{\"exists\": false}");
                }
            } else {
                out.print("{\"exists\": false}");
            }
        } catch (NumberFormatException e) {
            out.print("{\"exists\": false}");
        } finally {
            out.flush();
            out.close();
        }
    }
}


