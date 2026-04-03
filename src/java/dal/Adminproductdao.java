/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Category;
import model.Product;
import dal.DBContext;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
public class Adminproductdao extends DBContext{
     public List<Product> getallProducts() {
         List<Product> ds= new ArrayList<>();
        try {
            String sql = "SELECT * FROM Products";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setQuantity(rs.getInt("quantity"));
                p.setPrice(rs.getDouble("price"));
                p.setReleaseDate(rs.getString("releaseDate"));
                p.setDescribe(rs.getString("describe"));
                p.setImage(rs.getString("image"));

                int cid = rs.getInt("cid");
                Category c = getCategoryById(cid);
                p.setCategory(c);

                ds.add(p); // Quan trọng: Phải add vào list
            }
             // Đừng quên return ds ở cuối hàm
        } catch (Exception e) {
            e.printStackTrace();
        }
         return ds;
    }
    
     public void deleteProduct(String id) {
        String sql = "DELETE FROM Products WHERE id = ?"; 
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id.trim());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi tại deleteProduct: " + e.getMessage());
            e.printStackTrace();
        }
    }
     
    public boolean checkId(String id) {
        String sql = "SELECT id FROM Products WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true; // Tìm thấy bản ghi -> ID đã tồn tại
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại checkId: " + e.getMessage());
        }
        return false; // Không tìm thấy -> ID an toàn để dùng
    }
    public Product getProduct(String id){
        Product p = new Product();
        String sql = "SELECT * FROM Products where id=?";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setQuantity(rs.getInt("quantity"));
                p.setPrice(rs.getDouble("price"));
                p.setReleaseDate(rs.getString("releaseDate"));
                p.setDescribe(rs.getString("describe"));
                p.setImage(rs.getString("image"));

                int cid = rs.getInt("cid");
                Category c = getCategoryById(cid);
                p.setCategory(c);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return p;
    }
   
    public void updateProduct(Product p) {
        // Chỉnh lại tên bảng là Products (có s) và cột cid
        String sql = "UPDATE Products SET name = ?, quantity = ?, price = ?, "
                + "releaseDate = ?, [describe] = ?, image = ?, cid = ? "
                + "WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, p.getName());
            st.setInt(2, p.getQuantity());
            st.setDouble(3, p.getPrice());

            // Nếu releaseDate trong Model là String, SQL Server sẽ tự hiểu nếu đúng định dạng yyyy-mm-dd
            st.setString(4, p.getReleaseDate());

            st.setString(5, p.getDescribe());
            st.setString(6, p.getImage());

            // cid (Category ID)
            if (p.getCategory() != null) {
                st.setInt(7, p.getCategory().getId());
            } else {
                st.setNull(7, java.sql.Types.INTEGER);
            }

            st.setString(8, p.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void incrementQuantity(String id, int increment) {
        String sql = "UPDATE Products SET quantity = quantity + ? WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, increment); // số lượng muốn tăng
            st.setString(2, id);     // id của product
            st.executeUpdate();
            System.out.println("Đã tăng số lượng product " + id + " thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product p) {
        String sql = "INSERT INTO Products (id, name, quantity, price, releaseDate, [describe], image, cid) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, p.getId());
            st.setString(2, p.getName());
            st.setInt(3, p.getQuantity());
            st.setDouble(4, p.getPrice());
            st.setString(5, p.getReleaseDate());
            st.setString(6, p.getDescribe());
            st.setString(7, p.getImage());
            if (p.getCategory() != null) {
                st.setInt(8, p.getCategory().getId());
            } else {
                st.setNull(8, java.sql.Types.INTEGER);
            }

            st.executeUpdate();
            System.out.println("Thêm product " + p.getId() + " thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Product> searchByName(String keyword) {
        List<Product> list = new ArrayList<>();
        // Tránh null để không bị lỗi SQL
        if (keyword == null) {
            keyword = "";
        }

        String sql = "SELECT * FROM Products WHERE name LIKE ?  or id like ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getString("id"));
                    p.setName(rs.getString("name"));
                    p.setQuantity(rs.getInt("quantity"));
                    p.setPrice(rs.getDouble("price"));
                    p.setReleaseDate(rs.getString("releaseDate"));
                    p.setDescribe(rs.getString("describe"));
                    p.setImage(rs.getString("image"));

                    // Lấy Category đầy đủ từ database
                    int cid = rs.getInt("cid");
                    Category c = getCategoryById(cid); // Hàm này phải trả Category object
                    p.setCategory(c);

                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // in stack trace để debug
        }

        return list;
    }

    public Category getCategoryById(int id) {
        String sql = "select * from Categories where id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Category c = new Category(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("describe"));
                return c;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    public boolean checkCategoryExist(int categoryId) {
        String sql = "SELECT id FROM dbo.Categories WHERE id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true; // Tìm thấy ID, hợp lệ
            }
        } catch (SQLException e) {
            System.out.println("Check Category Error: " + e.getMessage());
        }
        return false; // Không tìm thấy
    }
}
