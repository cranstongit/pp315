console.log("Admin info loaded");

document.addEventListener("DOMContentLoaded", () => {
    loadAdminInfo();
});

function loadAdminInfo() {
    fetch('/api/admin/me')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(renderAdminInfo)
        .catch(error => console.error("Ошибка загрузки администратора:", error));
}

function renderAdminInfo(admin) {
    const adminInfoBody = document.getElementById("adminInfoBody");

    adminInfoBody.innerHTML = "";

    const row = document.createElement("tr");
    const roles = admin.roles.map(r => r.roleName).join(", ");

    row.innerHTML = `
        <td>${admin.id}</td>
        <td>${admin.firstName}</td>
        <td>${admin.lastName}</td>
        <td>${admin.email}</td>
        <td>${admin.username}</td>
        <td>${roles}</td>
    `;

    adminInfoBody.appendChild(row);
}