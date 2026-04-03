<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>

<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link href="css/admin-style.css" rel="stylesheet">
        <title>Danh sách sản phẩm</title>
    </head>
    <body class="admin-body">
        <div class="container mt-4 mb-5 admin-container">
            <h2 class="page-title">Quản lý danh sách sản phẩm</h2>

            <div class="action-bar mb-4">
                <div class="row gy-3 align-items-center">
                    <div class="col-lg-4 col-md-12">
                        <form action="Adminproduct" method="get" class="d-flex">
                            <input type="hidden" name="action" value="search">
                            <input type="text" name="query" id="searchInput" class="form-control" placeholder="Nhập tên sản phẩm cần tìm...">
                            <button type="submit" class="btn btn-primary ms-2 px-4 shadow-sm">Tìm</button>
                        </form>
                    </div>

                    <div class="col-lg-4 col-md-6 d-flex align-items-center gap-2">
                        <label for="columnSelect" class="mb-0 fw-semibold text-nowrap">Sắp xếp theo:</label>
                        <select id="columnSelect" class="form-select shadow-sm">
                            <option value="0">ID</option>
                            <option value="1">Tên sản phẩm</option>
                            <option value="2">Số lượng</option>
                            <option value="3">Giá</option>
                            <option value="4">Ngày ra mắt</option>
                            <option value="6">Danh mục</option>
                        </select>
                        <button id="toggleSort" class="btn btn-primary shadow-sm text-nowrap">
                            Sắp xếp ↕
                        </button>

                    </div>

                    <div class="col-lg-4 col-md-6 d-flex justify-content-lg-end gap-2">
                        <button class="btn btn-outline-secondary shadow-sm" data-bs-toggle="collapse" data-bs-target="#advancedFilter">
                            ▼ Lọc nâng cao
                        </button>
                        <button class="btn btn-success fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#modalThem">
                            + Thêm sản phẩm
                        </button>
                    </div>
                </div>
            </div>

            <div class="collapse mb-4" id="advancedFilter">
                <div class="range-slider-container shadow-sm">
                    <div class="row">
                        <div class="col-md-6">
                            <label class="fw-bold mb-2">Khoảng giá:</label>
                            <input type="range" id="minRange" min="0" max="1000" value="0" class="form-range">
                            <input type="range" id="maxRange" min="0" max="1000" value="1000" class="form-range mt-2">
                            <div class="mt-3 text-center">
                                Giá trị: <span id="minValue" class="price-display">0</span> - <span id="maxValue" class="price-display">1000</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="table-wrapper">
                <table class="table table-hover table-bordered table-custom mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên sản phẩm</th>
                            <th>SL</th>
                            <th>Giá</th>
                            <th>Ngày ra mắt</th>
                            <th>Mô tả</th>
                            <th>Danh mục</th>
                            <th>Hình ảnh</th>
                            <th width="150">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="productTable">
                        <%
                            List<Product> list = (List<Product>) request.getAttribute("data");
                            if (list != null) {
                                for (Product p : list) {
                        %>
                        <tr>
                            <td class="text-center fw-semibold"><%= p.getId()%></td>
                            <td class="fw-bold text-primary"><%= p.getName()%></td>
                            <td class="text-center"><%= p.getQuantity()%></td>
                            <td class="text-success fw-bold"><%= p.getPrice()%></td>
                            <td><%= p.getReleaseDate()%></td>
                            <td><small class="text-muted"><%= p.getDescribe()%></small></td>
                            <td class="text-center"><span class="badge bg-info text-dark"><%= (p.getCategory() != null) ? p.getCategory().getName() : ""%></span></td>
                            <td class="text-center"><img src="<%= p.getImage()%>" class="img-product" alt="img"></td>
                            <td class="text-center">
                                <div class="d-flex gap-1 justify-content-center">
                                    <a href="Adminproduct?action=update&id=<%= p.getId()%>" class="btn btn-warning btn-sm">Sửa</a>
                                    <form action="Adminproduct" method="POST" style="display:inline;" onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này không?')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="<%= p.getId()%>">
                                        <button type="submit" class="btn btn-danger btn-sm">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>

        <%
            model.Product pEdit = (model.Product) request.getAttribute("productEdit");
            if (pEdit != null) {
        %>
        <div class="modal fade" id="modalSua" tabindex="-1" aria-hidden="true" data-bs-backdrop="static">
            <div class="modal-dialog modal-lg">
                <div class="modal-content modal-content-custom shadow">
                    <div class="modal-header modal-header-edit">
                        <h5 class="modal-title fw-bold">✏️ Cập nhật sản phẩm: <%= pEdit.getId()%></h5>
                        <button type="button" class="btn-close btn-close-white" onclick="window.location.href = 'Adminproduct'"></button>
                    </div>
                    <form action="Adminproduct?action=update" method="POST" enctype="multipart/form-data">
                        <div class="modal-body bg-light">
                            <input type="hidden" name="id" value="<%= pEdit.getId()%>">
                            <input type="hidden" name="image" value="<%= pEdit.getImage()%>">

                            <div class="card p-3 mb-3 shadow-sm border-0">
                                <div class="mb-3">
                                    <label class="fw-bold form-label-custom">Tên sản phẩm:</label>
                                    <input type="text" name="name" class="form-control" value="<%= pEdit.getName()%>" required>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Giá (VNĐ):</label>
                                        <input type="number" step="0.01" name="price" class="form-control" value="<%= pEdit.getPrice()%>" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Số lượng:</label>
                                        <input type="number" name="quantity" class="form-control" value="<%= pEdit.getQuantity()%>" required>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold form-label-custom">Mô tả:</label>
                                    <textarea name="describe" class="form-control" rows="3"><%= pEdit.getDescribe()%></textarea>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Ngày ra mắt:</label>
                                        <input type="date" name="releaseDate" class="form-control" value="<%= pEdit.getReleaseDate()%>">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Mã danh mục (Category ID):</label>
                                        <input type="number" name="category" id="categoryEditInput" 
                                               class="form-control" value="<%= pEdit.getCategory().getId()%>" 
                                               required oninput="checkCategory(this.value, 'Edit')">
                                        <span id="catEditError" class="d-block mt-1"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="card p-3 shadow-sm border-0">
                                <label class="fw-bold form-label-custom mb-3">Ảnh sản phẩm:</label>
                                <div class="text-center mb-3">
                                    <img src="<%= (pEdit.getImage() != null && !pEdit.getImage().isEmpty()) ? pEdit.getImage() : "images/default.jpg"%>" id="preview" class="img-preview shadow-sm border">
                                </div>
                                <div class="input-group input-group-sm mb-2">
                                    <span class="input-group-text bg-secondary text-white">Đường dẫn</span>
                                    <input type="text" id="currentPath" class="form-control bg-white" value="<%= pEdit.getImage()%>" readonly>
                                </div>
                                <input type="file" name="imageFile" id="imageFile" class="form-control" accept="image/*" onchange="updatePreview(this)">
                            </div>
                        </div>
                        <div class="modal-footer bg-white">
                            <a href="Adminproduct" class="btn btn-secondary">Hủy bỏ</a>
                            <button type="submit" class="btn btn-info text-white fw-bold">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            var myModal = new bootstrap.Modal(document.getElementById('modalSua'));
            myModal.show();
        </script>
        <% }%>

        <div class="modal fade" id="modalThem" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content modal-content-custom shadow">
                    <div class="modal-header modal-header-add">
                        <h5 class="modal-title fw-bold">➕ Thêm sản phẩm mới</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="Adminproduct?action=add" method="POST" enctype="multipart/form-data">
                        <div class="modal-body bg-light">
                            <div class="card p-3 mb-3 shadow-sm border-0">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">ID sản phẩm:</label>
                                        <input type="text" name="id" id="productIdInput" class="form-control" 
                                               placeholder="Nhập ID (Ví dụ: ip1)" required
                                               onblur="checkIdDuplicate(this.value)">
                                        <small id="idMessage" class="fw-italic d-block mt-1"></small>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Tên sản phẩm:</label>
                                        <input type="text" name="name" class="form-control" placeholder="Nhập tên sản phẩm..." required>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Giá (VNĐ):</label>
                                        <input type="number" step="0.01" name="price" class="form-control" placeholder="0.00" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Số lượng:</label>
                                        <input type="number" name="quantity" class="form-control" placeholder="0" required>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="fw-bold form-label-custom">Mô tả:</label>
                                    <textarea name="describe" class="form-control" rows="2" placeholder="Nhập mô tả sản phẩm..."></textarea>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Ngày ra mắt:</label>
                                        <input type="date" name="releaseDate" class="form-control">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="fw-bold form-label-custom">Mã danh mục (Category ID):</label>
                                        <input type="number" name="category" id="categoryIdInput" 
                                               class="form-control" placeholder="Nhập ID danh mục" required oninput="checkCategory(this.value, 'Add')">
                                        <small id="categoryMessage" class="d-block mt-1"></small> 
                                    </div>
                                </div>
                            </div>

                            <div class="card p-3 shadow-sm border-0 text-center">
                                <label class="fw-bold form-label-custom d-block text-start mb-3">Ảnh sản phẩm:</label>
                                <img src="images/default.jpg" id="previewAdd" class="img-preview mb-3 shadow-sm border mx-auto">
                                <div class="input-group input-group-sm mb-3">
                                    <span class="input-group-text bg-secondary text-white">Đường dẫn</span>
                                    <input type="text" id="currentPathAdd" name="existingImage" class="form-control bg-white" readonly placeholder="Chưa có ảnh">
                                </div>
                                <input type="file" name="imageFile" class="form-control" accept="image/*" onchange="previewNewImage(this)">
                            </div>
                        </div>
                        <div class="modal-footer bg-white">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                            <button type="submit" class="btn btn-success fw-bold">Xác nhận Thêm</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>


    <script>
        // Preview ảnh cho Modal Sửa
        function updatePreview(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                var fileName = input.files[0].name;
                reader.onload = function(e) {
                    document.getElementById('preview').src = e.target.result;
                    document.getElementById('currentPath').value = "images/" + fileName;
                    document.getElementById('currentPath').classList.add('bg-warning-subtle');
                }
                reader.readAsDataURL(input.files[0]);
            }
        }

        // Preview ảnh cho Modal Thêm
        function previewNewImage(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('previewAdd').src = e.target.result;
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
    <script>
function checkIdDuplicate(id) {
    const message = document.getElementById("idMessage");
    const inputId = document.getElementById("productIdInput");
    // Liệt kê chính xác các name của input trong form
    const allFields = ["name", "price", "releaseDate", "describe", "category", "imageFile"];

    if (id.trim() === "") {
        message.innerHTML = "";
        unlockAndClear(allFields);
        return;
    }
    // Thêm thời gian ngẫu nhiên &t= để tránh bị lưu cache trình duyệt
    fetch("Adminproduct?action=checkId&id=" + id + "&t=" + new Date().getTime())
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                message.innerHTML = "⚠️ ID đã tồn tại. Đang lấy dữ liệu cũ...";
                message.className = "text-warning fw-bold d-block mt-1";
                if(document.getElementsByName("name")[0]) document.getElementsByName("name")[0].value = data.name;
                if(document.getElementsByName("price")[0]) document.getElementsByName("price")[0].value = data.price;
                if(document.getElementsByName("releaseDate")[0]) document.getElementsByName("releaseDate")[0].value = data.releaseDate;
                if(document.getElementsByName("describe")[0]) document.getElementsByName("describe")[0].value = data.describe;
                if(document.getElementsByName("category")[0]) document.getElementsByName("category")[0].value = data.categoryId;
                if(data.image) {
                    document.getElementById("currentPathAdd").value = data.image;
                    document.getElementById("previewAdd").src = data.image;
                }

                allFields.forEach(fieldName => {
                    let element = document.getElementsByName(fieldName)[0];
                    if(element) {
                        element.style.pointerEvents = "none"; 
                        element.style.backgroundColor = "#e9ecef";
                        if (element.type !== "file") {
                            element.readOnly = true;
                        }
                    }
                });
                inputId.classList.add("is-warning");
            } else {
                message.innerHTML = "✅ ID mới hợp lệ.";
                message.className = "text-success d-block mt-1";
                inputId.classList.remove("is-warning");
                inputId.classList.add("is-valid");
                
                unlockAndClear(allFields);
            }
        })
        .catch(err => console.error("Lỗi Ajax:", err));
}

function unlockAndClear(fields) {
    fields.forEach(fieldName => {
        let element = document.getElementsByName(fieldName)[0];
        if(element) {
            // Mở lại toàn bộ quyền tương tác
            element.style.pointerEvents = "auto";
            element.style.backgroundColor = "";
            element.readOnly = false;
            element.disabled = false; // Đảm bảo gỡ bỏ disabled nếu lỡ bị dính
            element.value = ""; // Xóa trắng để nhập mới
        }
    });
    // Trả ảnh về mặc định
    if(document.getElementById("previewAdd")) {
        document.getElementById("previewAdd").src = "images/default.jpg";
    }
}
    </script>
    <script>
const toggleBtn = document.getElementById('toggleSort');
const table = document.querySelector('table.table-bordered');
const tbody = table.querySelector('tbody');
const columnSelect = document.getElementById('columnSelect');
let sortOrder = 'asc';
let lastColIndex = -1;
toggleBtn.addEventListener('click', function() {
    const rows = Array.from(tbody.querySelectorAll('tr'));
    const colIndex = parseInt(columnSelect.value);

    // Nếu đổi cột mới → reset sortOrder về 'asc'
    if (colIndex !== lastColIndex) {
        sortOrder = 'asc';
        lastColIndex = colIndex;
    }
    // Lưu trạng thái hiện tại để hiển thị nút
    const currentOrder = sortOrder;
    // Sort bảng
    rows.sort((a, b) => {
        let valA = a.children[colIndex].textContent.trim();
        let valB = b.children[colIndex].textContent.trim();
        if (!isNaN(valA) && !isNaN(valB)) {
            valA = parseFloat(valA);
            valB = parseFloat(valB);
        } else {
            valA = valA.toLowerCase();
            valB = valB.toLowerCase();
        }
        return currentOrder === 'asc' ? (valA > valB ? 1 : -1) : (valA > valB ? -1 : 1);
    });
    tbody.innerHTML = '';
    rows.forEach(row => tbody.appendChild(row));

    if (currentOrder === 'asc') {
        toggleBtn.textContent = 'Sắp xếp ↕';
        toggleBtn.classList.remove('btn-success');
        toggleBtn.classList.add('btn-primary');
    } else {
        toggleBtn.textContent = 'Sắp xếp ↕';
        toggleBtn.classList.remove('btn-success');
        toggleBtn.classList.add('btn-primary');
    }

    // Toggle sortOrder cho lần click tiếp theo
    sortOrder = currentOrder === 'asc' ? 'desc' : 'asc';
});

    </script>
    <script>
document.addEventListener("DOMContentLoaded", function() {
    const minRange = document.getElementById('minRange');
    const maxRange = document.getElementById('maxRange');
    const minValue = document.getElementById('minValue');
    const maxValue = document.getElementById('maxValue');
    const tbody = document.querySelector('#productTable');
    // Lấy tất cả giá từ bảng
    const rows = Array.from(tbody.querySelectorAll('tr'));
    const prices = rows.map(row => parseFloat(row.children[3].textContent.trim())).filter(p => !isNaN(p));
    const tableMin = Math.min(...prices);
    const tableMax = Math.max(...prices);
    // Cập nhật range input
    minRange.min = tableMin;
    minRange.max = tableMax;
    maxRange.min = tableMin;
    maxRange.max = tableMax;
    // Khởi tạo giá trị ngay khi load
    minRange.value = tableMin;
    maxRange.value = tableMax;
    minValue.textContent = tableMin;
    maxValue.textContent = tableMax;
    function filterTable() {
        let min = parseFloat(minRange.value);
        let max = parseFloat(maxRange.value);

        // Đồng bộ 2 thanh
        if (min > max) {
            min = max;
            minRange.value = min;
        }
        if (max < min) {
            max = min;
            maxRange.value = max;
        }
        minValue.textContent = min;
        maxValue.textContent = max;
        // Lọc bảng
        rows.forEach(row => {
            const price = parseFloat(row.children[3].textContent.trim());
            if (price >= min && price <= max) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }
    minRange.addEventListener('input', filterTable);
    maxRange.addEventListener('input', filterTable);
    filterTable();
});

    </script>
    <script>
function checkCategory(catId, type) {
    // Xác định các phần tử dựa trên 'type' (Add hoặc Edit)
    const suffix = (type === 'Edit') ? 'Edit' : '';
    const messageId = (type === 'Edit') ? "catEditError" : "categoryMessage";
    const inputId = (type === 'Edit') ? "categoryEditInput" : "categoryIdInput";
    const modalId = (type === 'Edit') ? "#modalSua" : "#modalThem";

    const message = document.getElementById(messageId);
    const input = document.getElementById(inputId);
    const submitBtn = document.querySelector(modalId + " button[type='submit']");

    if (!catId || catId.trim() === "") {
        if(message) message.innerHTML = "";
        input.classList.remove("is-invalid", "is-valid");
        return;
    }

    fetch("Adminproduct?action=checkCategory&catId=" + catId)
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                message.innerHTML = " ✅ " + data.name;
                message.className = "text-success fw-bold";
                input.classList.replace("is-invalid", "is-valid") || input.classList.add("is-valid");
                if(submitBtn) submitBtn.disabled = false;
            } else {
                message.innerHTML = " ❌ Không tồn tại!";
                message.className = "text-danger fw-bold";
                input.classList.replace("is-valid", "is-invalid") || input.classList.add("is-invalid");
                if(submitBtn) submitBtn.disabled = true;
            }
        })
        .catch(err => console.error("Lỗi Ajax:", err));
}
</script>
</body>
</html>