document.addEventListener("DOMContentLoaded", function () {
    fetch("/nav.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("navbar").innerHTML = data;

            // 設定導覽列連結
            const pathParts = window.location.pathname.split('/');
            const memberId = pathParts[1];
            const memberName = "使用者名稱"; // 替換為您的邏輯或從後端獲取的名稱

            document.getElementById("home-link").href = `http://127.0.0.1:8080/${memberId}/#`;
            document.getElementById("profile-link").href = `http://127.0.0.1:8080/${memberId}/profile`;
            document.getElementById("order-link").href = `http://127.0.0.1:8080/${memberId}/order`;
            document.getElementById("member-name").textContent = memberName;
        })
        .catch(error => {
            console.error("導覽列載入失敗:", error);
        });
});
