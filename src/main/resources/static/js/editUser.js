console.log("editUser.js loaded");

let allRoles = [];

document.addEventListener("DOMContentLoaded", () => {
    fetch("/api/admin/roles")
        .then(res => res.json())
        .then(data => allRoles = data)
        .catch(err => console.error("Ошибка загрузки ролей:", err));

    document.getElementById("editUserForm").addEventListener("submit", handleEditSubmit);
});

window.openEditModal = function (user) {
    document.getElementById("editId").value = user.id;
    document.getElementById("editFirstName").value = user.firstName;
    document.getElementById("editLastName").value = user.lastName;
    document.getElementById("editEmail").value = user.email;
    document.getElementById("editUsername").value = user.username;
    document.getElementById("editPassword").value = user.password || "";

    const roleSelect = document.getElementById("editRoleIds");
    roleSelect.innerHTML = "";

    allRoles.forEach(role => {
        const option = document.createElement("option");
        option.value = role.id;
        option.text = role.roleName;
        if (user.roles.some(r => r.roleName === role.roleName)) {
            option.selected = true;
        }
        roleSelect.appendChild(option);
    });

    $('#editUserModal').modal('show');
};

function handleEditSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const formData = {
        id: form.editId.value,
        firstName: form.editFirstName.value,
        lastName: form.editLastName.value,
        email: form.editEmail.value,
        username: form.editUsername.value,
        password: form.editPassword.value,
        roleIds: Array.from(form.editRoleIds.selectedOptions).map(option => option.value)
    };

    fetch(`/api/admin/edit`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify(formData)
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(errorMessage => {
                    throw new Error(`Ошибка сервера: ${errorMessage}`);
                });
            }

            if (res.status === 204) return null;  // если 204 — тело пустое

            return res.json(); // если тело есть
        })
        .then(updatedUser => {
            $("#editUserModal").modal("hide");
            window.updateUserInTable(updatedUser);  // заменяем строку таблицы новым пользователем
        })
        .catch(err => {
            console.error("Ошибка при сохранении изменений:", err);
            alert("Ошибка при сохранении пользователя: " + err.message);
        });
}