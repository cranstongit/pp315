console.log("edit User loaded")

document.addEventListener("DOMContentLoaded", () => {
    loadRolesForEdit();
    document.getElementById("editUserForm").addEventListener("submit", handleEditSubmit);
});

let allRoles = [];

function loadRolesForEdit() {
    fetch("/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            allRoles = data;
        })
        .catch(err => console.error("Ошибка загрузки ролей:", err));
}

window.openEditModal = function (user) {
    const modal = $("#editUserModal");

    document.getElementById("editId").value = user.id;
    document.getElementById("editFirstName").value = user.firstName;
    document.getElementById("editLastName").value = user.lastName;
    document.getElementById("editEmail").value = user.email;
    document.getElementById("editUsername").value = user.username;
    document.getElementById("editPassword").value = user.password;

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

    modal.modal("show");
};

function openEditModal(user) {
    const modalContainer = document.getElementById("modalContainer");
    modalContainer.innerHTML = generateEditModalHTML(user); // вставили модалку
    $('#editUserModal').modal('show'); // показали модалку

    // Теперь, когда форма есть — вешаем обработчик
    document.getElementById("editUserForm").addEventListener("submit", handleEditSubmit);
}

function handleEditSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const userId = form.editId.value;
    const formData = {
        id: userId,
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
            if (!res.ok) throw new Error("Ошибка при обновлении");
            return res.json();
        })
        .then(() => {
            $("#editUserModal").modal("hide");
            window.loadUsers();
        })
        .catch(err => console.error("Ошибка:", err));
}