document.addEventListener("DOMContentLoaded", function () {
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
    

    // 初始化購物車陣列
    let cart = [];

    // 顯示訂單列表
    function fetchOrderList() {
        fetch(`/${memberId}/order-list`)
            .then(response => response.json())
            .then(data => {
                const orderResults = document.getElementById("order-results");
                orderResults.innerHTML = ''; // 清空內容
    
                data.result.forEach(order => {
                    const orderElement = document.createElement("div");
                    orderElement.className = "item-container";
                    orderElement.innerHTML = `
                        <p>訂單ID: ${order.orderId}</p>
                        <p>會員ID: ${order.memberId}</p>
                        <p>總金額: ${order.totalAmount}</p>
                        <p>建立日期: ${order.createdDate}</p>
                        <p>最後修改日期: ${order.lastModifiedDate}</p>
                        <ul>
                            ${order.orderItemList.map(item => `
                                <li>商品ID: ${item.productId}, 數量: ${item.quantity}, 金額: ${item.amount}</li>
                            `).join('')}
                        </ul>
                    `;
                    orderResults.appendChild(orderElement);
                });
            })
            .catch(error => console.error("無法獲取訂單列表:", error));
    }    
    

    // 初始化載入訂單列表
    fetchOrderList();

    // 更新購物車顯示
    function updateCartDisplay() {
        const cartItems = document.getElementById("cart-items");
        cartItems.innerHTML = '';
    
        cart.forEach((item, index) => {
            const cartItem = document.createElement("div");
            cartItem.className = "item-container";
            cartItem.innerHTML = `
                <span>商品ID: ${item.productId}, 數量: ${item.quantity}</span>
                <span class="remove-btn" onclick="removeFromCart(${index})">刪除</span>
            `;
            cartItems.appendChild(cartItem);
        });
    }
    

    // 從購物車中移除商品
    window.removeFromCart = function (index) {
        cart.splice(index, 1);
        updateCartDisplay();
    };

    // 加入購物車
    document.getElementById("add-to-cart-btn").addEventListener("click", function () {
        const productId = parseInt(document.getElementById("product-id").value);
        const quantity = parseInt(document.getElementById("quantity").value);

        if (isNaN(productId) || isNaN(quantity) || productId <= 0 || quantity <= 0) {
            alert("請輸入有效的商品ID和數量");
            return;
        }

        cart.push({ productId, quantity });
        alert("商品已加入購物車！");
        updateCartDisplay();
    });

    // 建立訂單
    document.getElementById("create-order-btn").addEventListener("click", function () {
        if (cart.length === 0) {
            alert("購物車中無商品");
            return;
        }

        const orderData = { buyItemList: cart };

        fetch(`/${memberId}/create-order`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(orderData)
        })
            .then(response => {
                if (response.ok) {
                    alert("訂單建立成功！");
                    cart.length = 0;
                    updateCartDisplay();
                    fetchOrderList();
                } else {
                    alert("訂單建立失敗");
                }
            })
            .catch(error => {
                console.error("訂單建立過程中發生錯誤:", error);
                alert("訂單建立失敗");
            });
    });
});
