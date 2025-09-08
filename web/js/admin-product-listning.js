let subCategorylList;

//Load All Product Datas
async function loadProductData() {
    const response = await fetch("LoadAdminProductData");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.status) {
            loadSelect("productCategory", json.categoryList, "name");
            subCategorylList = json.subCategorylList;
//            loadSelect("model", json.modelList, "name");
            loadSelect("productColor", json.colorList, "name");
            loadSelect("productSize", json.sizeList, "name");
            loadSelect("productBrand", json.brandList, "name");

//          laod Categiry Table
            if (json.categoryList && json.categoryList.length > 0) {
                const tableBody = document.querySelector('#categories tbody');
                tableBody.innerHTML = '';

                json.categoryList.forEach(category => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${category.id}</td>
                    <td>${category.name}</td>
                `;
                    tableBody.appendChild(row);
                });

            } else {
                console.log('No Category found');
            }
//          laod Categiry Table

//          laod Sub Category Table
            if (json.subCategorylList && json.subCategorylList.length > 0) {
                const tableBody = document.querySelector('#subcategories tbody');
                tableBody.innerHTML = '';

                json.subCategorylList.forEach(subCategory => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${subCategory.id}</td>
                    <td>${subCategory.name}</td>
                    <td>${subCategory.category.name}</td>
                `;
                    tableBody.appendChild(row);
                });
            } else {
                console.log('No Sub Category found');
            }
//          laod brand Table

//          laod Sub Category Table
            if (json.brandList && json.brandList.length > 0) {
                const tableBody = document.querySelector('#brands tbody');
                tableBody.innerHTML = '';
                json.brandList.forEach(brand => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${brand.id}</td>
                    <td>${brand.name}</td>
                `;
                    tableBody.appendChild(row);
                });
            } else {
                console.log('No Sub Category found');
            }
//          laod brand Table



        } else {
            console.log(json);
        }
    } else {
        console.log(response);
    }
}

function loadSelect(selectId, items, property) {
    const select = document.getElementById(selectId);
    items.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        select.appendChild(option);
    });
}

function loadSubCategory() {
    const categoryId = document.getElementById("productCategory").value;
    const subCategorySelect = document.getElementById("productSubCategory");
    subCategorySelect.length = 1;
    subCategorylList.forEach(item => {
        if (item.category.id === parseInt(categoryId)) {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item.name;
            subCategorySelect.appendChild(option);
        }
    });
}


//Add New Product
async function saveProduct() {
    const title = document.getElementById('productTitle').value;
    const description = document.getElementById('productDescription').value;
    const price = document.getElementById('productPrice').value;
    const qty = document.getElementById('productQty').value;
    const category = document.getElementById('productCategory').value;
    const subCategory = document.getElementById('productSubCategory').value;
    const color = document.getElementById('productColor').value;
    const size = document.getElementById('productSize').value;
    const brand = document.getElementById('productBrand').value;

    const image1 = document.getElementById("img1").files[0];
    const image2 = document.getElementById("img2").files[0];
    const image3 = document.getElementById("img3").files[0];

    const form = new FormData();
    form.append("title", title);
    form.append("description", description);
    form.append("price", price);
    form.append("qty", qty);
    form.append("category", category);
    form.append("subCategory", subCategory);
    form.append("color", color);
    form.append("size", size);
    form.append("brand", brand);
    form.append("image1", image1);
    form.append("image2", image2);
    form.append("image3", image3);

    const response = await fetch("AdminSaveProduct", {
        method: "POST",
        body: form
    });


    if (response.ok) {
        const json = await response.json();

        if (json.status) {
            await fetch("LoadAllProduct");
            loadProductTable();
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Product saved successfully!',
                confirmButtonText: 'Ok'
            });

        } else {

            if (json.message === "Please sign in!") {
                Swal.fire({
                    icon: 'warning',
                    title: 'Please sign in!',
                    text: 'You need to sign in to continue.',
                    confirmButtonText: 'Go to Sign In',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location = "sign-in.html";
                    }
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: json.message,
                    confirmButtonText: 'Try Again'
                });
            }
        }
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Request failed!',
            text: json.message,
            confirmButtonText: 'Close'
        });
    }

}
//
//async function loadProductTable() {
//    const response = await fetch("LoadAllProduct");
//
//    if (response.ok) {
//        const products = await response.json();
//        console.log(products)
//        if (products.productList && products.productList.length > 0) {
//            const tableBody = document.querySelector('#products tbody');
//            tableBody.innerHTML = ''; // Clear the table body
//
//            products.productList.forEach(product => {
//                // Create a new row for each produc
//                const row = document.createElement('tr');
//                row.innerHTML = `
//                    <td>${product.id}</td>
//                    <td>${product.title}</td>
//                    <td>${product.category.name}</td>
//                    <td>${product.brand.name}</td>
//                    <td>${product.color.name}</td>
//                    <td>$${product.price}</td>
//                    <td>${product.qty}</td>
//                    <td>
//                        <label class="toggle-switch">
//                            <input type="checkbox" ${product.productStatus.name === 'Available' ? 'checked' : ''}>
//                            <span class="slider"></span>
//                        </label>
//                    </td>
//                `;
//                tableBody.appendChild(row);  // Add the row to the table
//            });
//        } else {
//            console.log('No products found');
//        }
//
//    } else {
//        console.log(response);
//    }
//}



async function loadUserTable() {
    const response = await fetch("UserManagement");

    if (response.ok) {
        const users = await response.json();
        console.log(users);
        if (users.userList && users.userList.length > 0) {
            const tableBody = document.querySelector('#users tbody');
            tableBody.innerHTML = '';

            users.userList.forEach(users1 => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${users1.id}</td>
                    <td>${users1.f_name} ${users1.l_name}</td>
                    <td>${users1.email}</td>
                    <td>${users1.userType.name}</td>
                    <td>${users1.verification}</td>
                    <td>
                        <label class="toggle-switch">
                            <input type="checkbox" class="user-status-toggle" data-user-id="${users1.id}" ${users1.userStatus.name === 'Active' ? 'checked' : ''}> 
                            <span class="slider"></span>
                        </label>
                    </td>
                    <td>
                        <button class="delete-btn btn btn-danger" style="padding: 0.5rem 1rem; font-size: 0.8rem;">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            const toggles = document.querySelectorAll('.user-status-toggle');
            toggles.forEach(toggle => {
                toggle.addEventListener('change', async (event) => {
                    const userId = event.target.getAttribute('data-user-id');
                    const newStatus = event.target.checked ? 1 : 2;
                    await updateUserStatus(userId, newStatus);
                });
            });

            const deleteBtns = document.querySelectorAll('.delete-btn');
            deleteBtns.forEach(deleteBtn => {
                deleteBtn.addEventListener('click', async (event) => {
                    const userId = event.target.closest('tr').querySelector('.user-status-toggle').getAttribute('data-user-id');
                    await deleteUser(userId);
                });
            });

        } else {
            console.log('No users found');
        }

    } else {
        console.log(response);
    }
}

async function printProductTable() {
    const response = await fetch("LoadAdminProductData");
    const json = await response.json();
    let tableHTML = `
        <html>
            <head>
                <title>Product Report</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    h2 { text-align: center; }
                </style>
            </head>
            <body>
                <h2>Product Management Report</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Category</th>
                            <th>Brand</th>
                            <th>Color</th>
                            <th>Price</th>
                            <th>Qty</th>
                            <th>Status</th>

                        </tr>
                    </thead>
                    <tbody>
    `;
    json.productList.forEach(product => {
        tableHTML += `
            <tr>
                <td>${product.id}</td>
                <td>${product.title}</td>
                <td>${product.category.name}</td>
                <td>${product.brand.name}</td>
                <td>${product.color.name}</td>
                <td>${product.price}</td>
                <td>${product.qty}</td>
                <td>${product.productStatus.name}</td>

            </tr>
        `;
    });
    tableHTML += `
                    </tbody>
                </table>
                <script>
                    window.onload = function() {
                        window.print();
                        window.close();
                    };
                </script>
            </body>
        </html>
    `;
    const printWindow = window.open('', '_blank');
    printWindow.document.write(tableHTML);
    printWindow.document.close();
}

async function printUserTable() {
    const response = await fetch("UserManagement");
    const users = await response.json();
    let tableHTML = `
        <html>
            <head>
                <title>User Report</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    h2 { text-align: center; }
                </style>
            </head>
            <body>
                <h2>User Management Report</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>User Type</th>
                            <th>Verification</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
    `;
    users.userList.forEach(user => {
        tableHTML += `
            <tr>
                <td>${user.id}</td>
                <td>${user.f_name} ${user.l_name}</td>
                <td>${user.email}</td>
                <td>${user.userType.name}</td>
                <td>${user.verification}</td>
                <td>${user.userStatus.name}</td>
            </tr>
        `;
    });
    tableHTML += `
                    </tbody>
                </table>
                <script>
                    window.onload = function() {
                        window.print();
                        window.close();
                    };
                </script>
            </body>
        </html>
    `;
    const printWindow = window.open('', '_blank');
    printWindow.document.write(tableHTML);
    printWindow.document.close();
}


async function printCategoryTable() {
    const response = await fetch("LoadAdminProductData");
    const json = await response.json();
    let tableHTML = `
        <html>
            <head>
                <title>Category Report</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    h2 { text-align: center; }
                </style>
            </head>
            <body>
                <h2>Category Management Report</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <tbody>
    `;
    json.categoryList.forEach(cat => {
        tableHTML += `
            <tr>
                <td>${cat.id}</td>
                <td>${cat.name}</td>
            </tr>
        `;
    });
    tableHTML += `
                    </tbody>
                </table>
                <script>
                    window.onload = function() {
                        window.print();
                        window.close();
                    };
                </script>
            </body>
        </html>
    `;
    const printWindow = window.open('', '_blank');
    printWindow.document.write(tableHTML);
    printWindow.document.close();
}

async function printSubCategoryTable() {
    const response = await fetch("LoadAdminProductData");
    const json = await response.json();
    let tableHTML = `
        <html>
            <head>
                <title>Category Report</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    h2 { text-align: center; }
                </style>
            </head>
            <body>
                <h2>Category Management Report</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                                <th>Sub Category Name</th>

                        </tr>
                    </thead>
                    <tbody>
    `;
    json.subCategorylList.forEach(cat => {
        tableHTML += `
            <tr>
                <td>${cat.id}</td>
                <td>${cat.name}</td>
                        <td>${cat.category.name}</td>

            </tr>
        `;
    });
    tableHTML += `
                    </tbody>
                </table>
                <script>
                    window.onload = function() {
                        window.print();
                        window.close();
                    };
                </script>
            </body>
        </html>
    `;
    const printWindow = window.open('', '_blank');
    printWindow.document.write(tableHTML);
    printWindow.document.close();
}


async function updateUserStatus(userId, newStatus) {
//    Swal.fire({
//        title: 'Updating User Status...',
//        html: 'Please wait while we update the status.',
//        allowOutsideClick: false,
//        didOpen: () => {
//            Swal.showLoading();
//        }
//    });

    try {
        const response = await fetch(`UserStatusManagement?userId=${userId}&status=${newStatus}`, {
            method: 'POST',
        });

        const json = await response.json();

        Swal.close();

        if (json.status) {
            await fetch("UserManagement");
            Swal.fire({
                title: 'Success!',
                text: 'User status updated successfully.',
                icon: 'success',
                confirmButtonText: 'OK'
            });
        } else {
            Swal.fire({
                title: 'Error!',
                text: `${json.message}`,
                icon: 'error',
                confirmButtonText: 'OK'
            }).then(() => {
                loadUserTable();
            });
        }
    } catch (error) {
        Swal.close();

        console.error("Error updating user status:", error);
        Swal.fire({
            title: 'Error!',
            text: 'There was an error while updating the user status.',
            icon: 'error',
            confirmButtonText: 'OK'
        }).then(() => {
            loadUserTable();
        });
    }
}



async function deleteUser(userId) {
    const result = await Swal.fire({
        title: 'Are you sure?',
        text: `Do you want to delete User ${userId}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel!',
        reverseButtons: true
    });

    if (result.isConfirmed) {
        try {
            const response = await fetch(`UserManagement?userId=${userId}`, {
                method: 'DELETE',
            });
            const json = await response.json();

            if (json.status) {
                await fetch("UserManagement");
                loadUserTable();
                Swal.fire({
                    title: 'Deleted!',
                    text: `User ${userId} has been deleted successfully.`,
                    icon: 'success',
                    confirmButtonText: 'OK'
                });
            } else {
                await fetch("UserManagement");
                Swal.fire({
                    title: 'Error!',
                    text: json.message || 'Something went wrong!',
                    icon: 'error',
                    confirmButtonText: 'OK'
                }).then(() => {
                    loadUserTable();
                });
            }
        } catch (error) {
            console.error('Error deleting user:', error);
            Swal.fire({
                title: 'Error!',
                text: 'There was an error while deleting the user.',
                icon: 'error',
                confirmButtonText: 'OK'
            }).then(() => {
                loadUserTable();
            });
        }
    }
}










