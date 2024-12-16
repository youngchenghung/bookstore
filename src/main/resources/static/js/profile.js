document.addEventListener("DOMContentLoaded", function () {
    const pathParts = window.location.pathname.split('/');
    const memberId = pathParts[1];

    // 獲取會員資料
    fetch(`/${memberId}/userInfo`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("username").textContent = data.memberName;
            document.getElementById("userid").textContent = data.memberId;
            document.getElementById("email").textContent = data.email;
            document.getElementById("member-name").textContent = data.memberName;
        })
        .catch(error => console.error("發生錯誤:", error));

    // 獲取使用者權限
    fetch(`/${memberId}/privilege`)
        .then(response => response.json())
        .then(data => {
            const privilegeContainer = document.getElementById("privilege-info");
            privilegeContainer.innerHTML = ""; // 清空容器内容

            if (Array.isArray(data) && data.length > 0) {
                data.forEach(role => {
                    const roleElement = document.createElement("div");
                    roleElement.classList.add("role-item");

                    const roleIdElement = document.createElement("p");
                    roleIdElement.textContent = `權限Id: ${role.roleId}`;
                    roleElement.appendChild(roleIdElement);

                    const roleNameElement = document.createElement("p");
                    roleNameElement.textContent = `權限名稱: ${role.roleName}`;
                    roleElement.appendChild(roleNameElement);

                    privilegeContainer.appendChild(roleElement);
                });
            } else {
                privilegeContainer.textContent = "無權限資訊";
            }
        })
        .catch(error => console.error("獲取使用者權限錯誤:", error));

    // 更新 Email
    document.getElementById("update-email-btn").addEventListener("click", function () {
        const newEmail = document.getElementById("new-email").value;
        
        if (!newEmail) {
            alert("請輸入新的電子郵件。");
            return;
        }
        
        fetch(`/${memberId}/updateEmail`, {
            method: "PUT",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ newEmail })
        })
        .then(response => {
            if (response.ok) {
                alert("電子郵件更新成功！");
                document.getElementById("email").textContent = newEmail;
            } else {
                alert("電子郵件更新失敗。");
            }
        })
        .catch(error => console.error("發生錯誤:", error));
    });
});
