console.log("List of users loaded");

document.addEventListener("DOMContentLoaded", () => {
    window.loadUsers(); // вызываем через window (но можно и просто loadUsers())
});

// Делаем функции глобально доступными
window.loadUsers = function () {
    fetch('/api/admin/users')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(window.renderUsersTable) // тоже делаем глобальной
        .catch(error => console.error("Ошибка загрузки всех пользователей:", error));
};

window.renderUsersTable = function (users) {
    const usersTableBody = document.getElementById("usersTableBody");
    usersTableBody.innerHTML = "";

    users.forEach(user => {
        const row = document.createElement("tr");
        const roles = user.roles.map(r => r.roleName).join(", ");
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.email}</td>
            <td>${user.username}</td>
            <td>${roles}</td>
            <td><button class="btn btn-info btn-sm" data-user-id="${user.id}">Edit</button></td>
            <td><button class="btn btn-danger btn-sm" data-user-id="${user.id}">Delete</button></td>
        `;

        row.querySelector(".btn-info").addEventListener("click", () => {
            openEditModal(user);
        });

        row.querySelector(".btn-danger").addEventListener("click", () => {
            openDeleteModal(user);
        });

        usersTableBody.appendChild(row);
    });
};