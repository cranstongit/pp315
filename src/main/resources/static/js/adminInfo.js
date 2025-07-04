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
        .then(admin => {
            renderAdminNavBar(admin);
            renderAdminInfo(admin);
        })
        .then()
        .catch(error => console.error("Ошибка загрузки администратора:", error));
}

function renderAdminNavBar(admin) {
    const adminNavBarBody = document.getElementById("adminNavBarBody");

    if (!adminNavBarBody) {
        console.error("Элемент с ID adminNavBarBody не найден!");
        return;
    }

    adminNavBarBody.innerHTML = "";

    const adminData = document.createElement("a");
    const roles = admin.roles.map(r => r.roleName).join(", ");

    adminData.className = "navbar-brand";
    adminData.href = "#";
    adminData.innerHTML = `
        <b>${admin.username}</b>
        <span> with roles: </span>
        <span>${roles}</span>
    `;

    adminNavBarBody.appendChild(adminData);
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