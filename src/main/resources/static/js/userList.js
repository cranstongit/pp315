console.log("List of users loaded");

document.addEventListener("DOMContentLoaded", () => {
    loadUsers();
});

function loadUsers() {
    fetch('/api/admin/users')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(renderUsersTable)
        .catch(error => console.error("Ошибка загрузки всех пользователей:", error));
}

function renderUsersTable(users) {
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
            <td><button class="btn btn-info btn-sm">Edit</button></td>
            <td><button class="btn btn-danger btn-sm">Delete</button></td>
        `;
        usersTableBody.appendChild(row);
    });
}