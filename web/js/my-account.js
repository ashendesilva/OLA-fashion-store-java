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
}

async function laodProvince() {

    const response = await fetch("LoadAddressData");
    const json = await response.json();
    if (json.status) {
        console.log(json)
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
            console.log(json);
            Swal.close();

            if (json.status) {
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
//    console.log(response);
    if (response.ok) {
        const json = await response.json();
//        console.log(json)
        document.getElementById("username").innerHTML = `Hello, ${json.firstName} ${json.lastName}`;
        document.getElementById("since").innerHTML = `OLA Fashion Store Member Since ${json.since}`;
        document.getElementById("firstName").value = json.firstName;
        document.getElementById("lastName").value = json.lastName;
        document.getElementById("email").value = json.email;
        document.getElementById("phone").value = json.mobile;
        document.getElementById("currentPassword").value = json.password;
//        if (json.hasOwnProperty("addressList") && json.addressList !== undefined) {
//            let email;
//            let lineOne;
//            let lineTwo;
//            let city;
//            let postalCode;
//            let cityId;
//            const addressUL = document.getElementById("addressUL");
//            json.addressList.forEach(address => {
//                email = json.firstname;
//                lineOne = address.address_line_1;
//                lineTwo = address.address_line_2;
//                city = address.city.name;
//                postalCode = address.postal_code;
//                cityId = address.province.name;
//                const line = document.createElement("li");
//                line.innerHTML = lineOne + ",<br/>" +
//                        lineTwo + ",<br/>" +
//                        city + "<br/>" +
//                        postalCode;
//                addressUL.appendChild(line);
//            });
//            document.getElementById("addName").innerHTML = `Name: ${json.firstName} ${json.lastName}`;
//            document.getElementById("addEmail").innerHTML = `Email: ${email}`;
//            document.getElementById("contact").innerHTML = `Phone: 011-2215453`;
//
//            document.getElementById("lineOne").value = lineOne;
//            document.getElementById("lineTwo").value = lineTwo;
//            document.getElementById("postalCode").value = postalCode;
//            document.getElementById("citySelect").value = parseInt(cityId);
//        }

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
        `;
                addressUL.appendChild(addressCard);
            });
        }

    }

}