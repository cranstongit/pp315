document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/user/me')
        .then(response => {
            if (!response.ok) throw new Error("Ошибка загрузки пользователя");
            return response.json();
        })
        .then(user => {
            const tableBody = document.querySelector(".table tbody");

            const roles = user.roles.map(role => role.roleName).join(", ");

            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td>${user.username}</td>
                <td>${roles}</td>
            `;
            tableBody.appendChild(row);
        })
        .catch(error => {
            console.error("Ошибка:", error);
        });
});