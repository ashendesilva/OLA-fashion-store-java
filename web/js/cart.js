async function loadCartItems() {
    const response = await fetch("LoadCartItems");

    if (response.ok) {
        const json = await response.json();
//        console.log(json);
        if (json.status && Array.isArray(json.cartItems)) {
            const container = document.getElementById("cart-items");
            container.innerHTML = "";

            let total = 0;
            let totalQty = 0;


            json.cartItems.forEach(item => {


                let productSubTotal = item.product.price * item.qty;
                total += productSubTotal;
                totalQty += item.qty;

                const product = item.product;

                const itemHTML = `
                    <div class="cart-item mb-3" style="border: 1px solid #ddd; padding: 15px; border-radius: 10px;" data-price="${product.price}">
                        <div class="row align-items-center">
                            <div class="col-md-2 col-sm-3">
        <img src="product-images/${product.id}/image3.png" 
             alt="${product.title}" class="product-image w-100" style="object-fit: contain; height: 200px;">
    </div>
                            <div class="col-md-4 col-sm-5">
                                <h4 class="product-name" style="font-size: 20px;">${product.title}</h4>
                                <div class="product-details">
                                    <span class="badge bg-dark me-2" style="font-size: 16px;">Size: ${product.size.name}</span>
                                    <span class="badge bg-secondary" style="font-size: 16px;">Color: ${product.color.name}</span>
                                </div>
                            </div>
                            <div class="col-md-3 col-sm-2">
                                <div class="quantity-controls">
                                    <button class="quantity-btn" onclick="changeQty(${item.id}, ${item.qty - 1})">
    <i class="fas fa-minus"></i>
</button>
                                    <input type="number" class="quantity-input" value="${item.qty}" min="1" max="${product.qty}" 
       onchange="changeQty(${item.id}, this.value)">
                                    <button class="quantity-btn" onclick="changeQty(${item.id}, ${item.qty + 1})">
    <i class="fas fa-plus"></i>
</button>
                                </div>
                            </div>
                            <div class="col-md-2 col-sm-1">
                                <div class="price">$${(product.price).toFixed(2)}</div>
                            </div>
                            <div class="col-md-1 col-sm-1">
                                <button class="remove-btn" onclick="deleteCart(${item.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                `;

                container.insertAdjacentHTML("beforeend", itemHTML);
            });

            document.getElementById("total").innerHTML = total.toFixed(2);
            document.getElementById("qty").innerHTML = totalQty;




//            recalculateTotal();
        } else {
            document.getElementById("cart-items").innerHTML = "<p class='text-muted'>Your cart is empty.</p>";
        }
    } else {
        console.error("Failed to load cart items.");
    }
}

async function changeQty(cartId){
    
}

async function deleteCart(cartID) {
    Swal.fire({
        title: 'Are you sure?',
        text: 'This item will be removed from your cart.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch(`DeleteCart?cartId=${cartID}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    const json = await response.json();

                    if (json.status) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Deleted!',
                            text: json.message || 'Item has been removed.',
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.reload();
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error!',
                            text: json.message || 'Failed to remove the item.'
                        });
                    }
                } else {
                    throw new Error('Network response was not ok');
                }
            } catch (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'Something went wrong. Please try again.'
                });
                console.error(error);
            }
        }
    });
}


//
//async function changeQty(cartId, newQuantity) {
//    console.log(cartId , newQuantity);
//    try {
//        const response = await fetch('UpdateCartItemQty', {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/json',
//            },
//            body: JSON.stringify({ 
//                cartItemId: cartId,
//                newQty: newQuantity
//            })
//        });
//
//        if (response.ok) {
//            const result = await response.json();
//            console.log(result);
//            if (result.success) {
//                // Refresh cart or update totals
//                loadCartItems();
//                // Or just update the subtotal if you prefer:
//                // recalculateTotal();
//            } else {
//                alert(result.message || 'Failed to update quantity');
//                loadCartItems(); // Reload to reset to correct values
//            }
//        } else {
//            throw new Error('Network response was not ok');
//        }
//    } catch (error) {
//        console.error('Error updating quantity:', error);
//        alert('Error updating quantity. Please try again.');
//        loadCartItems(); // Reload to reset to correct values
//    }
//}

async function changeQty(cartId, newQuantity) {
//    console.log(cartId, newQuantity);
    try {
        const response = await fetch('UpdateCartItemQty', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                cartItemId: cartId,
                newQty: newQuantity
            })
        });

        if (response.ok) {
            const result = await response.json();
//            console.log(result);

            if (result.status) {
                Swal.fire({
                    icon: 'success',
                    title: 'Updated!',
                    text: result.message || 'Quantity updated successfully.',
                    timer: 2000,
                    showConfirmButton: false
                });
                loadCartItems();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: result.message || 'Failed to update quantity'
                }).then(() => {
                    loadCartItems();
                });
            }
        } else {
            throw new Error('Network response was not ok');
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: 'Error updating quantity. Please try again.'
        }).then(() => {
            loadCartItems();
        });
    }
}


