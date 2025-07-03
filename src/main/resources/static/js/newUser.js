console.log("New user JS loaded")

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("newUserForm");

    form.addEventListener("submit", (event) => {
        event.preventDefault(); // отмена обычной отправки формы

        const selectedRoles = Array.from(document.getElementById("roleIds").selectedOptions)
            .map(option => parseInt(option.value));

        const userDto = {
            firstName: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            email: document.getElementById("email").value,
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            roleIds: selectedRoles
        };

        fetch("/api/admin/newuser", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userDto)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text);
                    });
                }
                alert("Пользователь успешно создан!");
                form.reset();
                // Перезагрузить таблицу всех пользователей (если хочешь)
                // loadAllUsers();
            })
            .catch(error => {
                console.error("Ошибка создания пользователя:", error);
                alert("Ошибка: " + error.message);
            });
    });

    loadRolesForNewUser(); // подгружаем роли в select
});

function loadRolesForNewUser() {
    fetch("/api/admin/roles") // сделай такой endpoint в контроллере
        .then(response => response.json())
        .then(roles => {
            const select = document.getElementById("roleIds");
            roles.forEach(role => {
                const option = document.createElement("option");
                option.value = role.id;
                option.text = role.roleName;
                select.appendChild(option);
            });
        })
        .catch(error => console.error("Ошибка загрузки ролей:", error));
}