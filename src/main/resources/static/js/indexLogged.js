document.addEventListener("DOMContentLoaded", function () {
    const bookTable = document.getElementById("bookTable");
    const prevPageButton = document.getElementById("prevPageButton");
    const nextPageButton = document.getElementById("nextPageButton");
    let currentPage = 0;
    const pageSize = 10;
    const pathParts = window.location.pathname.split('/');
    const memberId = pathParts[1];

    // 初始化導航列
    fetch(`/${memberId}/userInfo`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`無法獲取會員名稱，狀態碼：${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("member-name").textContent = data.memberName;
        })
        .catch(error => {
            console.error("無法獲取會員名稱:", error);
            logError("會員資訊加載失敗，請稍後再試。");
        });

    // 載入書籍列表
    const loadBooks = function (page = 0) {
        fetch(`/book?limit=${pageSize}&offset=${page * pageSize}`)
            .then(response => response.json())
            .then(data => {
                renderBooks(data.result);
            })
            .catch(error => console.error("無法載入書籍列表:", error));
    };

    // 渲染書籍表格
    const renderBooks = function (books) {
        const table = document.createElement("table");
        table.innerHTML = `
            <thead>
                <tr>
                    <th>ID</th>
                    <th>圖片</th>
                    <th>書名</th>
                    <th>作者</th>
                    <th>分類</th>
                    <th>價格</th>
                    <th>庫存</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                ${books.map(book => `
                    <tr>
                        <td>${book.bookId}</td>
                        <td><img src="${book.imageUrl}" alt="書籍圖片" width="50"></td>
                        <td>${book.bookName}</td>
                        <td>${book.author}</td>
                        <td>${book.bookCategory}</td>
                        <td>${book.price}</td>
                        <td>${book.stock}</td>
                        <td>
                            <button onclick="deleteBook(${book.bookId})">刪除</button>
                        </td>
                    </tr>
                `).join("")}
            </tbody>
        `;
        bookTable.innerHTML = "";
        bookTable.appendChild(table);
    };

    // 新增書籍
    const addBookButton = document.getElementById("addBookButton");
    addBookButton.addEventListener("click", function () {
        const bookName = document.getElementById("addBookName").value;
        const author = document.getElementById("addAuthor").value;
        const category = document.getElementById("addCategory").value;
        const price = document.getElementById("addPrice").value;
        const stock = document.getElementById("addStock").value;
        const bookImage = document.getElementById("addBookImage").files[0]; // 圖片文件
    
        if (!bookName || !author || !category || !price || !stock) {
            alert("所有欄位都為必填！");
            return;
        }
    
        const formData = new FormData();
        formData.append("bookName", bookName);
        formData.append("author", author);
        formData.append("bookCategory", category);
        formData.append("price", price);
        formData.append("stock", stock);
    
        if (bookImage) {
            formData.append("image", bookImage); // 如果有圖片文件則附加
        }
    
        fetch("/book/add", {
            method: "POST",
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("新增書籍失敗");
            }
            return response.json();
        })
        .then(data => {
            alert("書籍新增成功！");
            document.getElementById("addBookForm").reset();
            loadBooks(currentPage); // 新增書籍後重新載入書籍列表
        })
        .catch(error => console.error("新增書籍失敗:", error));
    });

    // 更新書籍
    const updateBookButton = document.getElementById("updateBookButton");
    updateBookButton.addEventListener("click", function () {
        const bookId = document.getElementById("updateBookId").value;
        const formData = new FormData();
        formData.append("bookName", document.getElementById("updateBookName").value);
        formData.append("author", document.getElementById("updateAuthor").value);
        formData.append("bookCategory", document.getElementById("updateCategory").value);
        formData.append("price", document.getElementById("updatePrice").value);
        formData.append("stock", document.getElementById("updateStock").value);
    
        const imageFile = document.getElementById("updateBookImage").files[0];
        if (imageFile) {
            formData.append("image", imageFile);
        }
    
        fetch(`/book/update/${bookId}`, {
            method: "PUT",
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("更新失敗");
            }
            return response.json();
        })
        .then(data => {
            alert("書籍更新成功！");
            loadBooks(currentPage); // 更新書籍後重新載入書籍列表
        })
        .catch(error => console.error("更新書籍失敗:", error));
    });

    // 刪除書籍
    window.deleteBook = function(bookId) {
        console.log("正在刪除書籍 ID:", bookId);
        if (!confirm(`確定要刪除書籍 ID: ${bookId} 嗎？`)) {
            return;
        }
    
        fetch(`/book/delete/${bookId}`, {
            method: "DELETE",
        })
        .then(response => {
            console.log("刪除書籍響應:", response);
            if (!response.ok) {
                throw new Error("刪除書籍失敗");
            }
            return response.json();
        })
        .then(data => {
            console.log("刪除書籍成功後返回資料:", data);
            alert("書籍刪除成功！");
            loadBooks(currentPage);  // 刪除後重新載入書籍列表
        })
        .catch(error => console.error("刪除書籍錯誤:", error));
    };

    // 分頁按鈕
    prevPageButton.addEventListener("click", function () {
        if (currentPage > 0) {
            currentPage--;
            loadBooks(currentPage);
        }
    });

    nextPageButton.addEventListener("click", function () {
        currentPage++;
        loadBooks(currentPage);
    });

    // 初始載入書籍
    loadBooks(currentPage);
});
