console.log("deleteUser loaded")

function openDeleteModal(user) {
    // Заполняем поля
    document.getElementById("deleteUserId").value = user.id;
    document.getElementById("deleteFirstName").value = user.firstName;
    document.getElementById("deleteLastName").value = user.lastName;
    document.getElementById("deleteEmail").value = user.email;
    document.getElementById("deleteUsername").value = user.username;
    document.getElementById("deleteRoles").value = user.roles.map(r => r.roleName).join(", ");

    $('#deleteUserModal').modal('show'); // Показываем модалку
}

document.getElementById("deleteUserForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const id = document.getElementById("deleteUserId").value;

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/api/admin/delete?id=${id}`, {
        method: "DELETE",
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            if (response.status === 204) {
                $('#deleteUserModal').modal('hide');
                window.loadUsers(); // обновляем таблицу
            } else {
                return response.text().then(text => { throw new Error(text); });
            }
        })
        .catch(error => {
            console.error("Ошибка удаления пользователя:", error);
            alert("Ошибка при удалении пользователя: " + error.message);
        });
});