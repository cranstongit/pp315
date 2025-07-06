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
            showElementById("adminNavBarBody");
            renderAdminInfo(admin);
        })
        .then()
        .catch(error => {
            console.warn("Администратор не найден, показываем fallback:", error);
            renderFallbackNavBar();
            showElementById("fallbackNavBar");
        });
}

function renderAdminNavBar(admin) {
    const adminNavBarBody = document.getElementById("adminNavBarBody");

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

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfParam = document.querySelector('meta[name="_csrf_parameter"]').getAttribute('content');

    const logoutForm = document.createElement("form");
    logoutForm.action = "/logout";
    logoutForm.method = "post";
    logoutForm.className = "form-inline";

    logoutForm.innerHTML = `
        <input type="hidden" name="${csrfParam}" value="${csrfToken}" />
        <button type="submit" class="btn btn-link nav-link" style="color: gray; padding: 0;">Logout</button>
    `;

    adminNavBarBody.appendChild(adminData);
    adminNavBarBody.appendChild(logoutForm);
}

function renderFallbackNavBar() {
    const fallbackNavBar = document.getElementById("fallbackNavBar");

    fallbackNavBar.innerHTML = "";

    const brand = document.createElement("a");
    brand.className = "navbar-brand";
    brand.href = "#";
    brand.textContent = "БД не содержит данных администратора";

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfParam = document.querySelector('meta[name="_csrf_parameter"]').getAttribute('content');

    const logoutForm = document.createElement("form");
    logoutForm.action = "/logout";
    logoutForm.method = "post";
    logoutForm.className = "form-inline";
    logoutForm.innerHTML = `
        <input type="hidden" name="${csrfParam}" value="${csrfToken}" />
        <button type="submit" class="btn btn-link nav-link" style="color: gray; padding: 0;">Logout</button>
    `;

    fallbackNavBar.appendChild(brand);
    fallbackNavBar.appendChild(logoutForm);
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

function showElementById(id) {
    const el = document.getElementById(id);
    if (el) el.style.display = "flex"; // для navbar лучше "flex"
}