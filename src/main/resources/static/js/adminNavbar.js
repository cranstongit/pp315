//
//
//
//
//
//
//
//
// function renderAdminNavBar(admin) {
//     const adminNavBarBody = document.getElementById("adminNavBarBody");
//
//     adminNavBarBody.innerHTML = "";
//
//     const adminData = document.createElement("a");
//     const roles = admin.roles.map(r => r.roleName).join(", ");
//
//     adminData.className = "navbar-brand";
//     adminData.href = "#";
//     adminData.innerHTML = `
//         <b>${admin.username}</b>
//         <span> with roles: </span>
//         <span>${roles}</span>
//     `;
//
//     adminNavBarBody.appendChild(adminData);
// }