let selectedBrand = '';
let selectedColor = '';
let selectedSubCategory = '';
let selectedCategory = '';
let selectedSize = '';

async function loadData() {
    const response = await fetch("LoadProductData");
    const json = await response.json();
    console.log(json)

    updateProductView(json.productList);


    const filterGroup = document.getElementById("category-options");
    let currentSelectedButton = null;
    json.categoryList.forEach(category => {
        const button = document.createElement('button');
        button.classList.add('stext-106', 'cl6', 'hov1', 'bor3', 'trans-04', 'm-r-32', 'm-tb-5');
        button.setAttribute('id', `category-${category.id}`);
        button.textContent = category.name;
        catName = category.name;
        button.addEventListener('click', () => {
            if (currentSelectedButton) {
                currentSelectedButton.style.textDecoration = '';  // Remove underline
                currentSelectedButton.style.color = '';  // Remove color change (optional)
            }
            button.style.color = 'black';
            button.style.textDecoration = 'none';
            currentSelectedButton = button;
            loadSubCategories(category.id, json.subCategorylList);
            loadProductsByCategory(category.name);
            selectedCategory = category.name;

        });
        filterGroup.appendChild(button);
    });
//    load categories end


//    function displaySubCategories(subCategories) {
//        const subcategoriesContainer = document.getElementById('subcategories-container');
//        subcategoriesContainer.innerHTML = ''; // Clear any previous subcategories
//
//        // If there are subcategories, create a list and append them to the container
//        if (subCategories.length > 0) {
//            const subCategoryListContainer = document.createElement('ul');
//            subCategories.forEach(subCategory => {
//                const listItem = document.createElement('li');
//                listItem.textContent = subCategory.name;
//                subCategoryListContainer.appendChild(listItem);
//            });
//
//            subcategoriesContainer.appendChild(subCategoryListContainer);
//        } else {
//            subcategoriesContainer.innerHTML = '<p>No subcategories available for this category.</p>';
//        }
//    }

    function loadSubCategories(categoryId, subCategoryList) {
//        console.log(categoryId);
//        console.log(subCategoryList);

        const filteredSubCategories = subCategoryList.filter(subCategory => subCategory.category.id === categoryId);

        displaySubCategories(filteredSubCategories);
    }

    function displaySubCategories(subCategories) {
        const subcategoriesContainer = document.getElementById('sub-category-options');
        const li = document.getElementById("sub-category-id");

        subcategoriesContainer.innerHTML = '';

        if (subCategories.length > 0) {

            subCategories.forEach(subCategory => {
                const listItem = document.createElement("li");
                listItem.classList.add("p-b-6");

                const link = document.createElement("a");
                link.href = "#";
                link.classList.add("filter-link", "stext-106", "trans-04");
                link.textContent = subCategory.name;

                // Handle the click event
                link.onclick = function () {

                    const allLinks = subcategoriesContainer.querySelectorAll('.filter-link');
                    allLinks.forEach(link => link.classList.remove('filter-link-active'));

                    link.classList.add('filter-link-active');

                    if (li) {
                        li.classList.add('chosen');
                    }

                    selectedSubCategory = subCategory.name;
                };

                listItem.appendChild(link);
                subcategoriesContainer.appendChild(listItem);
            });
        } else {
            subcategoriesContainer.innerHTML = '<p>No subcategories available for this category.</p>';
        }
    }


    // Load colors dynamically
    const colorFilterContainer = document.querySelector('#color-options');
    colorFilterContainer.innerHTML = ""; // Clear existing content

    json.colorList.forEach(color => {
        const li = document.createElement('li');
        li.classList.add('p-b-6');

        const colorCircle = document.createElement('span');
        colorCircle.classList.add('fs-15', 'lh-12', 'm-r-6');
        colorCircle.style.color = color.name.toLowerCase();
        colorCircle.innerHTML = `<i class="zmdi zmdi-circle"></i>`;

        const colorLink = document.createElement('a');
        colorLink.href = "#";
        colorLink.classList.add('filter-link', 'stext-106', 'trans-04');
        colorLink.textContent = color.name;
        colorLink.setAttribute('data-id', color.id);

        colorLink.addEventListener('click', (event) => {
            event.preventDefault();

            const allLinks = colorFilterContainer.querySelectorAll('.filter-link');
            allLinks.forEach(link => link.classList.remove('filter-link-active'));

            colorLink.classList.add('filter-link-active');

            li.classList.add('chosen');

            selectedColor = color.name;

        });

        li.appendChild(colorCircle);
        li.appendChild(colorLink);

        colorFilterContainer.appendChild(li);
    });
//load colors end 


//load size start 

    const option = document.getElementById("size-options");
    const li = document.getElementById("size-id");

    option.innerHTML = "";

    json.sizeList.forEach(item => {
        const listItem = document.createElement("li");
        listItem.classList.add("p-b-6");

        // Create the link for each brand
        const link = document.createElement("a");
        link.href = "#";
        link.classList.add("filter-link", "stext-106", "trans-04");
        link.textContent = item.name;
        link.onclick = function () {

            const allLinks = option.querySelectorAll('.filter-link');
            allLinks.forEach(link => link.classList.remove('filter-link-active'));

            link.classList.add('filter-link-active');

            li.classList.add('chosen');


            selectedSize = item.name;
        };

        listItem.appendChild(link);

        option.appendChild(listItem);
    });

//load size end 

}



async function loadProductsByCategory(categoryName) {
    const data = {
        selectedCategory: categoryName
    };

    const dataJSON = JSON.stringify(data);

    const response = await fetch("LoadProductsByCategory",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });
    console.log(response);
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        updateProductView(json.productList);
    }
}



async function loadBrands() {
    const response = await fetch("LoadProductData");
    const json = await response.json();

    const option = document.getElementById("brand-options");
    const li = document.getElementById("brand-id");

    option.innerHTML = "";

    json.brandList.forEach(item => {
        const listItem = document.createElement("li");
        listItem.classList.add("p-b-6");

        const link = document.createElement("a");
        link.href = "#";
        link.classList.add("filter-link", "stext-106", "trans-04");
        link.textContent = item.name;
        link.onclick = function () {

            const allLinks = option.querySelectorAll('.filter-link');
            allLinks.forEach(link => link.classList.remove('filter-link-active'));

            link.classList.add('filter-link-active');

            li.classList.add('chosen');


            selectedBrand = item.name;
        };

        listItem.appendChild(link);

        option.appendChild(listItem);
    });
}


// Search Product function
async function searchProduct(firstResult) {
//    console.log(`Searching for products from brand: ${selectedBrand}`);
//    console.log(`Searching for products from selectedColor: ${selectedColor}`);
//    console.log(`Searching for products from selectedSubCategory: ${selectedSubCategory}`);
//    console.log(`Searching for products from selectedCategory: ${selectedCategory}`);
//    console.log(`Searching for products from selectedSize: ${selectedSize}`);
//    
    const maxPrice = document.getElementById("max-price").value;
    const minPrice = document.getElementById("min-price").value;
    const sortValue = document.getElementById("st-sort").value;

//    
//    console.log(minPrice , maxPrice);

    const data = {
        firstResult,
        selectedBrand,
        selectedCategory,
        selectedColor,
        selectedSize,
        selectedSubCategory,
        maxPrice,
        minPrice,
        sortValue
    };
//    console.log(data);

    const dataJSON = JSON.stringify(data);

    const response = await fetch("SearchProducts",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });
//    console.log(response);
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            updateProductView(json.productList);


        } else {
            Swal.fire({
            title: 'Oops!',
            text: json.message,
            icon: 'warning',
            confirmButtonText: 'OK'
        })
        }
    } else {
        console.log(response);
    }
}


async function sortProduct() {
    const sortValue = document.getElementById("st-sort").value;
    const data = {
        sortValue
    };
//    console.log(data);

    const dataJSON = JSON.stringify(data);

    const response = await fetch("SortProduct",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });

    if (response.ok) {
        const json = await response.json();

        if (json.status) {
//            console.log(json);
            updateProductView(json.productList);
        } 
    }
}

//
//function updateProductView(productList) {
//    const productListContainer = document.getElementById('product-list');
//    productListContainer.innerHTML = '';
//    
//    if (productList.length === 0) {
//        // Show SweetAlert popup if no products are available
//        Swal.fire({
//            title: 'Oops!',
//            text: 'No products available',
//            icon: 'warning',
//            confirmButtonText: 'OK'
//        })
//        return;
//    }
//    
//    productList.forEach(product => {
//        const productElement = document.createElement('div');
//        productElement.classList.add('col-sm-6', 'col-md-4', 'col-lg-3', 'p-b-35', 'isotope-item');
//
//        productElement.innerHTML = `
//        <div class="block2" id="product-${product.id}">
//            <div class="block2-pic hov-img0">
//                <img src="product-images/${product.id}/image1.png" alt="IMG-PRODUCT" id="product-image-${product.id}">
//                <a href="product-detail.html?id=${product.id}" class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04" id="quick-view-btn-${product.id}">
//                    Quick View
//                </a>
//            </div>
//            <div class="block2-txt flex-w flex-t p-t-14">
//                <div class="block2-txt-child1 flex-col-l">
//                    <a href="product-detail.html?id=${product.id}" class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6" id="product-name-${product.id}">
//                        ${product.title}
//                    </a>
//                    <span class="stext-105 cl3" id="product-price-${product.id}">
//                        $${product.price.toFixed(2)}
//                    </span>
//                </div>
//                <div class="block2-txt-child2 flex-r p-t-3">
//                    <a href="#" class="btn-addwish-b2 dis-block pos-relative js-addwish-b2" id="wishlist-btn-${product.id}">
//                        <img class="icon-heart1 dis-block trans-04" src="images/icons/icon-heart-01.png" alt="ICON" id="wishlist-icon-heart1-${product.id}">
//                        <img class="icon-heart2 dis-block trans-04 ab-t-l" src="images/icons/icon-heart-02.png" alt="ICON" id="wishlist-icon-heart2-${product.id}">
//                    </a>
//                </div>
//            </div>
//        </div>
//    `;
//
//        productListContainer.appendChild(productElement);
//    });
//}

let currentPage = 1;
const productsPerPage = 8;

function updateProductView(productList) {
    const productListContainer = document.getElementById('product-list');
    productListContainer.innerHTML = '';
    productListContainer.style.cssText = `
    padding: 20px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
`;

    const startIndex = (currentPage - 1) * productsPerPage;
    const endIndex = startIndex + productsPerPage;
    const paginatedProducts = productList.slice(startIndex, endIndex);

    if (paginatedProducts.length === 0) {
        Swal.fire({
            title: 'Oops!',
            text: 'No products available',
            icon: 'warning',
            confirmButtonText: 'OK'
        });
        return;
    }

    paginatedProducts.forEach(product => {
        const productElement = document.createElement('div');
        productElement.classList.add('col-sm-6', 'col-md-4', 'col-lg-3', 'p-b-35', 'isotope-item');

        productElement.innerHTML = `
            <div class="block2" id="product-${product.id}">
                <div class="block2-pic hov-img0">
                    <img src="product-images/${product.id}/image1.png" alt="IMG-PRODUCT" id="product-image-${product.id}">
                    <a href="product-detail.html?id=${product.id}" class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04" id="quick-view-btn-${product.id}">
                        Quick View
                    </a>
                </div>
                <div class="block2-txt flex-w flex-t p-t-14">
                    <div class="block2-txt-child1 flex-col-l">
                        <a href="product-detail.html?id=${product.id}" class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6" id="product-name-${product.id}">
                            ${product.title}
                        </a>
                        <span class="stext-105 cl3" id="product-price-${product.id}">
                            $${product.price.toFixed(2)}
                        </span>
                    </div>
                </div>
            </div>
        `;

        productListContainer.appendChild(productElement);
    });

    renderPagination(productList);
}

function renderPagination(productList) {
    const paginationContainer = document.getElementById('pagination');
    paginationContainer.innerHTML = '';
//    paginationContainer.style.marginTop = '1000px';

    const totalPages = Math.ceil(productList.length / productsPerPage);
    const maxVisibleButtons = 3;

    const prevButton = document.createElement('button');
    prevButton.innerText = '« Prev';
    prevButton.style.cssText = "padding: 10px 15px; border: 1px solid #ddd; background-color: white; margin: 5px; cursor: pointer;";
    prevButton.disabled = currentPage === 1;
    prevButton.style.opacity = currentPage === 1 ? '0.5' : '1';
    prevButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            updateProductView(productList);
        }
    });
    paginationContainer.appendChild(prevButton);

    let startPage = Math.max(1, currentPage - 1);
    let endPage = startPage + maxVisibleButtons - 1;
    if (endPage > totalPages) {
        endPage = totalPages;
        startPage = Math.max(1, endPage - maxVisibleButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        const button = document.createElement('button');
        button.innerText = i;
        button.style.cssText = "padding: 10px 15px; border: 1px solid #ddd; background-color: white; margin: 5px; cursor: pointer;";
        if (i === currentPage) {
            button.style.backgroundColor = '#3b82f6';
            button.style.color = 'white';
        }
        button.addEventListener('click', () => {
            currentPage = i;
            updateProductView(productList);
        });
        paginationContainer.appendChild(button);
    }

    const nextButton = document.createElement('button');
    nextButton.innerText = 'Next »';
    nextButton.style.cssText = "padding: 10px 15px; border: 1px solid #ddd; background-color: white; margin: 5px; cursor: pointer;";
    nextButton.disabled = currentPage === totalPages;
    nextButton.style.opacity = currentPage === totalPages ? '0.5' : '1';
    nextButton.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            updateProductView(productList);
        }
    });
    paginationContainer.appendChild(nextButton);
}








