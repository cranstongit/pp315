console.log("New user JS loaded")

document.addEventListener("DOMContentLoaded", () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const form = document.getElementById("newUserForm");

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        event.stopPropagation();

        const selectedRoles = Array.from(document.getElementById("roleIdsNew").selectedOptions)
            .map(option => parseInt(option.value));

        const userDto = {
            firstName: document.getElementById("firstNameNew").value,
            lastName: document.getElementById("lastNameNew").value,
            email: document.getElementById("emailNew").value,
            username: document.getElementById("usernameNew").value,
            password: document.getElementById("passwordNew").value,
            roleIds: selectedRoles
        };

        fetch("/api/admin/newuser", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken // <--- ВАЖНО!
            },
            credentials: "same-origin",
            body: JSON.stringify(userDto)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                form.reset();
                window.loadUsers();
            })
            .catch(error => {
                console.error("Ошибка создания пользователя:", error);
                alert("Ошибка: " + error.message);
            });
    });

    loadRolesForNewUser();
});

function loadRolesForNewUser() {
    fetch("/api/admin/roles") // сделай такой endpoint в контроллере
        .then(response => response.json())
        .then(roles => {
            const select = document.getElementById("roleIdsNew");
            roles.forEach(role => {
                const option = document.createElement("option");
                option.value = role.id;
                option.text = role.roleName;
                select.appendChild(option);
            });
        })
        .catch(error => console.error("Ошибка загрузки ролей:", error));
}