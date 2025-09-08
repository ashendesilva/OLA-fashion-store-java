async function laodProductData() {
    const searchParams = new URLSearchParams(window.location.search);
    if (searchParams.has("id")) {
        const productId = searchParams.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);

        if (response.ok) {
            const json = await response.json();
            console.log(json);

            if (json.status) {

                document.getElementById("image1").src = "product-images\\" + json.product.id + "\\image1.png";
                document.getElementById("image2").src = "product-images\\" + json.product.id + "\\image2.png";
                document.getElementById("image3").src = "product-images\\" + json.product.id + "\\image3.png";
                document.getElementById("title").innerHTML = json.product.title;
                document.getElementById("price").innerHTML = json.product.price;
                document.getElementById("pro-description").innerHTML = json.product.description;
                document.getElementById("proSize").innerHTML = json.product.size.name;
                document.getElementById("category").innerHTML = json.product.category.name;
                document.getElementById("color").innerHTML = json.product.color.name;
                document.getElementById("subCategory").innerHTML = json.product.subCategory.name;
                document.getElementById("brand").innerHTML = json.product.brand.name;
                document.getElementById("status").innerHTML = json.product.productStatus.name;
                document.getElementById("qty").innerHTML = json.product.qty;

                //addto cart
                const addToCartMain = document.getElementById("addToCartMain");
                addToCartMain.addEventListener(
                        "click", (e) => {
                    addToCart(json.product.id, document.getElementById("add-to-cart-qty").value);
                    e.preventDefault();
                });
                //addto cart

                //addto wishlist
                const addToWishlist = document.getElementById("addToWishlist1");
                addToWishlist.addEventListener(
                        "click", (e) => {
                    addToWishlist(json.product.id);
                    e.preventDefault();
                });
                //addto wishlist

//                similer product load

                let similer_product_main = document.getElementById("similerProductMain");
                let productHtml = document.getElementById("similerProduct");


                similer_product_main.innerHTML = "";

                json.productList.forEach(item => {
                    let productCloneHtml = productHtml.cloneNode(true);
                    productCloneHtml.querySelector("#similerProductA1").href = "product-detail.html?id=" + item.id;
                    productCloneHtml.querySelector("#similerProductImg").src = "product-images\\" + item.id + "\\image1.png";
                    productCloneHtml.querySelector("#similerProducTtitle").innerHTML = item.title;
                    productCloneHtml.querySelector("#similerProductPrice").innerHTML = item.price;

                    similer_product_main.appendChild(productCloneHtml);
                });


//                similer product load


            }
        }
    }
}

function addToWishlist(productId) {
    console.log(productId);
}
async function addToCart(productId, qty) {
    const response = await fetch("AddToCart?prId=" + productId + "&qty=" + qty);

    if (response.ok) {
        const json = await response.json();
//        console.log(json);

        if (json.status) {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: json.message,
                timer: 1500,
                showConfirmButton: false
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: json.message
            });
        }
    } else {
        Swal.fire({
            icon: 'error',
            title: 'Server Error',
            text: 'Something went wrong while adding the product to cart.'
        });
    }
}





