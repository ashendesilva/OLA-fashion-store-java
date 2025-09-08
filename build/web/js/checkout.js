let selectedAddressID = null;
let firstName = '';
let lastName = '';
let line_one = '';
let line_two = '';
let postalCode = '';
let city = '';
let province = '';
let totalValue = null;

payhere.onCompleted = function onCompleted(orderId) {
    console.log("Payment completed. OrderID:" + orderId);
};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
};



async function loadCheckOutData() {
//    alert("asdas")
    const response = await fetch("loadCheckOutData");
//    console.log(response);
    if (response.ok) {
        const json = await response.json();
        console.log(json);

        if (json.status) {
            const cartList = json.cartList;
            const addressList = json.userAddressList;
            const container = document.getElementById('existingAddressGroup');
            container.innerHTML = ''; // Clear previous entries
            addressList.forEach((addr, index) => {
                const card = document.createElement('div');
                card.className = 'saved-address-card';
                card.setAttribute('data-address', addr.id);
                card.setAttribute('onclick', `selectAddress(${addr.id})`);
                card.innerHTML = `
                <div class="address-name">${addr.address_line_1}</div>
                <div class="address-details">
                    ${addr.address_line_2 || ''}, ${addr.city?.name || ''}${addr.postal_code ? ', ' + addr.postal_code : ''}
                </div>
            `;
                card.onclick = () => {
                    selectedAddressID = addr.id;
                    line_one = addr.address_line_1;
                    line_two = addr.address_line_2;
                    postalCode = addr.postal_code;
                    city = addr.city.name;
                    province = addr.city.province.name;


//                console.log("Selected Address ID:", selectedAddressID);
                    document.querySelectorAll('.saved-address-card').forEach(c => c.classList.remove('selected'));
                    card.classList.add('selected');
                };

                container.appendChild(card);
            });

            //order item container
            const orderItemsContainer = document.querySelector('.order-items');
            orderItemsContainer.innerHTML = '';

            let subtotal = 0;
            cartList.forEach(item => {
                const itemTotal = item.product.price * item.qty;
                subtotal += itemTotal;

                const itemElement = document.createElement('div');
                itemElement.className = 'order-item';
                itemElement.innerHTML = `
                    <span class="item-name">${item.product.title} (${item.qty})</span>
                    <span class="item-price">$${(itemTotal).toFixed(2)}</span>
                `;
                orderItemsContainer.appendChild(itemElement);
            });

            const shipping = 5.00;
            const shippingElement = document.createElement('div');
            shippingElement.className = 'order-item';
            shippingElement.innerHTML = `
                <span class="item-name">Shipping</span>
                <span class="item-price">$${shipping.toFixed(2)}</span>
            `;
            orderItemsContainer.appendChild(shippingElement);

            let total = subtotal + shipping;
            totalValue = total;
            const totalElement = document.createElement('div');
            totalElement.className = 'order-item';
            totalElement.innerHTML = `
                <span class="item-name">Total</span>
                <span class="item-price">$${total.toFixed(2)}</span>
            `;
            orderItemsContainer.appendChild(totalElement);

        } else {
            if (json.message === "empty-cart") {
                console.log(json.message);
                window.location = "index.html";
            } else {
                console.log(json.message);
            }
        }
    } else {
        console.log(response);
    }
}


async function checkOut() {
    if (totalValue !== null) {


        if (selectedAddressID !== null) {
            let data = {
                selectedAddressID,
                line_one,
                line_two,
                postalCode,
                city,
                province
            };
//        console.log(data);
            let dataJSON = JSON.stringify(data);

            const response = await fetch("CheckOut", {
                method: "POST",
                header: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });

            if (response.ok) {
                const json = await response.json();
                if (json.status) {
                    console.log(json);
                    // PayHere Process
                    payhere.startPayment(json.payhereJson);
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Error",
                        text: json.message || "Something went wrong!",
                        confirmButtonColor: "#d33",
                        confirmButtonText: "OK"
                    });
                }
            }
        } else {
            Swal.fire({
                icon: "warning",
                title: "Please select an address",
                text: "You must choose an address before continuing.",
                confirmButtonColor: "#3085d6",
                confirmButtonText: "OK"
            });
        }

    } else {

        Swal.fire({
            icon: "warning",
            title: "Your Cart Is Empty",
            text: "You must choose an address before continuing.",
            confirmButtonColor: "#3085d6",
            confirmButtonText: "OK"
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = "product.html";
            }
        });
    }

}


