//function loadSubcategories() {
//    const category = document.getElementById('category').value;
//    const subcategorySelect = document.getElementById('subcategory');
//
//    // Clear existing options
//    subcategorySelect.innerHTML = '<option value="0">Select Subcategory</option>';
//
//    // Add options based on selected category
//    if (category === '1') { // Women's Clothing
//        const options = ['Dresses', 'Tops', 'Pants', 'Skirts', 'Jeans'];
//        addOptions(subcategorySelect, options);
//    } else if (category === '2') { // Men's Clothing
//        const options = ['Shirts', 'T-Shirts', 'Pants', 'Jeans', 'Shorts'];
//        addOptions(subcategorySelect, options);
//    } else if (category === '3') { // Kids' Clothing
//        const options = ['Boys', 'Girls', 'Baby', 'Unisex'];
//        addOptions(subcategorySelect, options);
//    } else if (category === '4') { // Accessories
//        const options = ['Bags', 'Watches', 'Jewelry', 'Hats', 'Belts'];
//        addOptions(subcategorySelect, options);
//    } else if (category === '5') { // Shoes
//        const options = ['Casual', 'Sports', 'Formal', 'Sandals', 'Boots'];
//        addOptions(subcategorySelect, options);
//    }
//}
//
//function addOptions(selectElement, options) {
//    options.forEach(option => {
//        const opt = document.createElement('option');
//        opt.value = option;
//        opt.textContent = option;
//        selectElement.appendChild(opt);
//    });
//}
//
//// Function to save account changes
//function saveChanges() {
//    // Get form values
//    const firstName = document.getElementById('firstName').value;
//    const lastName = document.getElementById('lastName').value;
//    const email = document.getElementById('email').value;
//    const phone = document.getElementById('phone').value;
//
//    // Update profile info
//    document.getElementById('username').textContent = `Hello, ${firstName} ${lastName}`;
//
//    // Show success message
//    showMessage('success', 'Your changes have been saved successfully!');
//}
//
//// Function to save product
//function saveProduct() {
//    const title = document.getElementById('title').value;
//
//    if (!title) {
//        showMessage('error', 'Please enter a product title');
//        return;
//    }
//
//    // Here you would typically send the data to a server
//    showMessage('success', 'Product added successfully!');
//
//    // Reset form
//    document.getElementById('title').value = '';
//    document.getElementById('description').value = '';
//    document.getElementById('price').value = '';
//    document.getElementById('qty').value = '1';
//}
//
//// Function to show messages
//function showMessage(type, text) {
//    const messageDiv = document.createElement('div');
//    messageDiv.className = `message ${type}`;
//    messageDiv.textContent = text;
//
//    // Insert message before the first element in the tab content
//    const tabContent = document.querySelector('.tab-pane.active');
//    tabContent.insertBefore(messageDiv, tabContent.firstChild);
//
//    // Remove message after 5 seconds
//    setTimeout(() => {
//        messageDiv.remove();
//    }, 5000);
//}
//
//// Function to sign out
//function signOut() {
//    // Here you would typically clear session data
//    alert('You have been signed out. Redirecting to home page...');
//    // window.location.href = 'index.html';
//}
//
//// Initialize file upload areas for drag and drop
//document.addEventListener('DOMContentLoaded', function () {
//    const fileUploadAreas = document.querySelectorAll('.file-upload-area');
//
//    fileUploadAreas.forEach(area => {
//        area.addEventListener('dragover', function (e) {
//            e.preventDefault();
//            this.classList.add('drag-over');
//        });
//
//        area.addEventListener('dragleave', function () {
//            this.classList.remove('drag-over');
//        });
//
//        area.addEventListener('drop', function (e) {
//            e.preventDefault();
//            this.classList.remove('drag-over');
//            // Handle dropped files here
//        });
//    });
//});
var cityList;
function loadData() {
    getUserData();
    laodProvince();
    loadUserOrders();
}

async function laodProvince() {

    const response = await fetch("LoadAddressData");
    const json = await response.json();
    if (json.status) {
//        console.log(json)
//        loadprovince
        const provinceSelect = document.getElementById("provinceList");
        cityList = json.citydList;
        json.provinceList.forEach(item => {
            const provinceOption = document.createElement("option");
            provinceOption.value = item.id;
            provinceOption.innerHTML = item.name;
            provinceSelect.appendChild(provinceOption);
        });
    }

}

function loadCity() {
    const provinceId = document.getElementById("provinceList").value;
    const citySelect = document.getElementById("cityList");
    citySelect.length = 1;
    cityList.forEach(item => {
        if (item.province.id == provinceId) {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item.name;
            citySelect.appendChild(option);
        }
    });
}

async function addAddress() {
    const lineOne = document.getElementById("lineOne").value;
    const lineTwo = document.getElementById("lineTwo").value;
    const provinceId = document.getElementById("provinceList").value;
    const cityId = document.getElementById("cityList").value;
    const postalCode = document.getElementById("postalCode").value;

    const addressObject = {
        lineOne,
        lineTwo,
        provinceId,
        cityId,
        postalCode
    };

    // Show loading alert
    Swal.fire({
        title: 'Saving Address...',
        html: 'Please wait while we save your address.',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    try {
        const response = await fetch("MyAccount", {
            method: "POST",
            body: JSON.stringify(addressObject),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const json = await response.json();
//            console.log(json);
            Swal.close();

            if (json.status) {
                await fetch("MyAccount");
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: json.message,
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    // Optionally reload or redirect
                    location.reload();
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: json.message
                });
            }
        } else {
            Swal.close();
            Swal.fire({
                icon: 'error',
                title: 'Server Error',
                text: 'Unable to process request. Please try again.'
            });
        }
    } catch (err) {
        console.error("Fetch error:", err);
        Swal.close();
        Swal.fire({
            icon: 'error',
            title: 'Network Error',
            text: 'Something went wrong. Please check your connection.'
        });
    }
}



async function getUserData() {
    const response = await fetch("MyAccount");
    if (response.ok) {
        const json = await response.json();
//        console.log(json);
        document.getElementById("username").innerHTML = `Hello, ${json.firstName} ${json.lastName}`;
        document.getElementById("since").innerHTML = `OLA Fashion Store Member Since ${json.since}`;
        document.getElementById("firstName").value = json.firstName;
        document.getElementById("lastName").value = json.lastName;
        document.getElementById("email").value = json.email;
        document.getElementById("phone").value = json.mobile;
        if (json.hasOwnProperty("addressList") && json.addressList !== undefined) {
            const addressUL = document.getElementById("addressUL");
            addressUL.innerHTML = ""; // Clear existing addresses

            json.addressList.forEach((address, index) => {
                const email = json.email; // or json.firstname, if that's the email
                const firstName = json.firstName;
                const lastName = json.lastName;
                const lineOne = address.address_line_1;
                const lineTwo = address.address_line_2;
                const city = address.city.name;
                const postalCode = address.postal_code;
                const province = address.city.province.name;
                // Create address-card div
                const addressCard = document.createElement("div");
                addressCard.className = "address-card";
                addressCard.className = "address-card p-3 mb-4 shadow-sm border rounded";
                addressCard.innerHTML = `
            <a href="#" class="address-edit"><i class="fas mb-4"></i></a>
            <p id="username">${firstName} ${lastName}</p>
            <p id="address">${lineOne}, ${lineTwo}</p>
            <p id="city">${city}</p>
            <p id="province">${province}</p>
            <p id="mobile">Phone: ${json.mobile}</p>
                <button class="btn btn-danger btn-sm delete-address" 
            data-id="${address.id}" 
            onclick="deleteAddress('${address.id}')">
        Delete
    </button>
        `;
//                deleteAddress(address.id);
                addressUL.appendChild(addressCard);
            });
        }

    }

}

async function deleteAddress(addressID) {
//    console.log(addressID);

    Swal.fire({
        title: "Are you sure?",
        text: "This address will be permanently deleted.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Yes, delete it!"
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch(`DeleteUserAddress?addressID=${addressID}`, {
                    method: 'DELETE'
                });
                const json = await response.json();
//                console.log(json);

                if (json.status) {
                    await fetch("MyAccount")
                    Swal.fire("Deleted!", json.message || "Address deleted successfully.", "success")
                            .then(() => {
                                location.reload();
                            });
                } else {
                    Swal.fire("Error!", json.message || "Failed to delete address.", "error");
                }
            } catch (error) {
                Swal.fire("Error!", "Something went wrong while deleting.", "error");
            }
        }
    });
}



async function updateAccoutDetails() {
    const phone = document.getElementById('phone').value;
    const lastName = document.getElementById('lastName').value;
    const firstName = document.getElementById('firstName').value;
    const email = document.getElementById('email').value;


    const user = {
        phone: phone,
        lastName: lastName,
        firstName: firstName,
        email: email
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "UpdateUserDetails",
            {
                method: "POST",
                body: userJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            Swal.fire({
                icon: 'success',
                title: 'User details update Successful!',
                confirmButtonText: 'OK'
            }).then(() => {
                window.location.reload();
            });

        } else {
            Swal.fire({
                icon: 'error',
                title: 'User Update Failed',
                text: json.message || 'Something went wrong!',
                confirmButtonText: 'Try Again'
            });
        }
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Server Error',
            text: 'Unable to connect to the server. Please try again later.',
            confirmButtonText: 'OK'
        });
    }

}
async function loadUserOrders() {
    const response = await fetch("LoadUserOrders");

    if (response.ok) {
        const json = await response.json();

//        console.log(json);
        const tbody = document.querySelector('#orders table tbody');
        tbody.innerHTML = "";

        json.orders.forEach(order => {
            const date = new Date(order.date).toLocaleDateString("en-GB", {
                day: "2-digit",
                month: "short",
                year: "numeric"
            });

            tbody.innerHTML += `
                    <tr>
                        <td>#ORD-${order.orderId}</td>
                        <td>${date}</td>
                        <td>${order.totalItems}</td>
                        <td>$${order.totalPrice}</td>
                    </tr>
                `;
        });

    }
}
