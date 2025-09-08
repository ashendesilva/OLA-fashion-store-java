document.addEventListener("DOMContentLoaded", function () {
    loadProductTable();
    loadDashboardData();
});
//Load Product table with  disable product

async function loadDashboardData() {
    const response = await fetch("AdminDashboard");
    if (response.ok) {
        const json = await response.json();
//        console.log(json)
        document.getElementById("totalUsers").innerText = json.totalUser;
        document.getElementById("totalProduct").innerText = json.totalProduct;
        document.getElementById("totalOrders").innerText = json.totalOrders;


    }
}

async function loadProductTable() {
    const response = await fetch("LoadAllProduct");
    if (response.ok) {
        const products = await response.json();
        if (products.status) {
//            console.log(products);
            if (products.productList && products.productList.length > 0) {
                const tableBody = document.querySelector('#products tbody');
                tableBody.innerHTML = '';

                products.productList.forEach(product => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.title}</td>
                    <td>${product.category.name}</td>
                    <td>${product.brand.name}</td>
                    <td>${product.color.name}</td>
                    <td>${product.price}</td>
                    <td>${product.qty}</td>
                    <td>
                        <label class="toggle-switch">
                            <input type="checkbox" class="status-toggle" data-product-id="${product.id}" ${product.productStatus.name === 'Available' ? 'checked' : ''}>
                            <span class="slider"></span>
                        </label>
                    </td>
                `;
                    tableBody.appendChild(row);
                });

                const toggles = document.querySelectorAll('.status-toggle');
                toggles.forEach(toggle => {
                    toggle.addEventListener('change', async (event) => {
                        const productId = event.target.getAttribute('data-product-id');
                        const newStatus = event.target.checked ? 1 : 2;
                        await updateProductStatus(productId, newStatus);
                    });
                });
            } else {
                console.log('No products found');
            }
        }

    } else {
        console.log(response);
    }
}

// Update product status
async function updateProductStatus(productId, newStatus) {
    // Show the "Updating..." loading animation
    Swal.fire({
        title: 'Updating Product Status...',
        html: 'Please wait while we update the status.',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading(); // Show the loading spinner
        }
    });

    try {
        // Send the request to update the product status
        const response = await fetch(`UpdateProductStatus?productId=${productId}&status=${newStatus}`, {
            method: 'POST',
        });

        const json = await response.json();

        // Close the loading spinner
        Swal.close();

        // Show success or error message based on the response
        if (json.status) {
            await fetch("LoadAllProduct");
            Swal.fire({
                title: 'Success!',
                text: 'Product status updated successfully.',
                icon: 'success',
                confirmButtonText: 'OK'
            }).then(() => {
                // Directly update the product status toggle in the DOM
                const toggle = document.querySelector(`.product-status-toggle[data-product-id="${productId}"]`);
                if (toggle) {
                    toggle.checked = newStatus === 1;  // Update the toggle's state to Active (1) or Inactive (2)
                }
            });
        } else {
            Swal.fire({
                title: 'Error!',
                text: `${json.message}`,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    } catch (error) {
        // Close the loading spinner if there is an error
        Swal.close();

        console.error("Error updating product status:", error);
        Swal.fire({
            title: 'Error!',
            text: 'There was an error while updating the product status.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }
}


//update product status
//async function updateProductStatus(productId, newStatus) {
//
//    const result = await Swal.fire({
//        title: 'Are you sure?',
//        text: "Do you want to update the product status?",
//        icon: 'warning',
//        showCancelButton: true,
//        confirmButtonText: 'Yes, update it!',
//        cancelButtonText: 'Cancel',
//        reverseButtons: true
//    });
//
//    if (result.isConfirmed) {
//        const response = await fetch(`UpdateProductStatus?productId=${productId}&status=${newStatus}`, {
//            method: 'POST',
//        });
//        const json = await response.json();
//
//        if (json.status) {
//            Swal.fire({
//                title: 'Success!',
//                text: 'Product status updated successfully.',
//                icon: 'success',
//                confirmButtonText: 'OK'
//            }).then(() => {
//                // Wait for 2 seconds before reloading the product table
//                setTimeout(() => {
//                    loadProductTable();
//                }, 2000); // 2000ms = 2 seconds delay
//            });
//        } else {
//            Swal.fire({
//                title: 'Error!',
//                text: `${json.message}`,
//                icon: 'error',
//                confirmButtonText: 'OK'
//            }).then(() => {
//                // Wait for 2 seconds before reloading the product table
//                setTimeout(() => {
//                    loadProductTable();
//                }, 2000);
//            });
//        }
//    } else {
//        Swal.fire({
//            title: 'Cancelled',
//            text: 'No changes were made.',
//            icon: 'info',
//            confirmButtonText: 'OK'
//        }).then(() => {
//            // Wait for 2 seconds before reloading the product table
//            setTimeout(() => {
//                loadProductTable();
//            }, 2000);
//        });
//    }
//}


async function addCategory() {
    const categoryName = document.getElementById("categoryName").value;
    
    const category = {
        categoryName: categoryName
    };

    const cat = JSON.stringify(category);

    const response = await fetch(
            "AdminDashboard",
            {
                method: "POST",
                body: cat,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );
    
    if(response.ok){
        const json = await response.json();
        console.log(json)
    }

}

