//async function signIn() {
//    const email = document.getElementById("email").value;
//    const password = document.getElementById("password").value;
//
//    const signIn = {
//        email: email,
//        password: password
//    };
//
//
//    const signInJson = JSON.stringify(signIn);
//
//    const response = await fetch(
//            "SignIn",
//            {
//                method: "POST",
//                body: signInJson,
//                header: {
//                    "Content-Type": "application/json"
//                }
//            }
//    );
//
//
//    if (response.ok) {
//        const json = await response.json();
//        console.log(json);
//
//        if (json.status) {
//
//            if (json.message === "1") {
//                window.location = "verify-account.html";
//            } else {
//                window.location = "index.html";
//            }
//
//
//        } else {
//            document.getElementById("message").innerHTML = json.message;
//        }
//    } else {
//        document.getElementById("message").innerHTML = "Sign in failed. Please try again";
//    }
//
//}


async function signIn() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const signIn = {
        email: email,
        password: password
    };

    const signInJson = JSON.stringify(signIn);

    Swal.fire({
        title: 'Signing In...',
        html: 'Please wait while we log you in.',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    try {
        const response = await fetch("SignIn", {
            method: "POST",
            body: signInJson,
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const json = await response.json();
            console.log(json);
            if (json.status) {
                if (json.message === "1") {
                    window.location = "verify-account.html";
                } else {
                    window.location = "index.html";
                }
            } else {
                Swal.close();
                Swal.fire('Sign In Failed', json.message, 'error');
            }
        } else {
            Swal.close();
            Swal.fire('Sign In Failed', 'Something went wrong. Please try again.', 'error');
        }
    } catch (error) {
        console.error("Error:", error);
        Swal.close();
        Swal.fire('Error', 'Network error or server down. Try again.', 'error');
    }
}


async function authenticateUser() {
    const response = await fetch("SignIn");

    if (response.ok) {
        const json = await response.json();

        if (json.message === "1") {
            window.location = "index.html";
        } else {
            document.getElementById("message").innerHTML = json.message;
        }

    } else {
        document.getElementById("message").innerHTML = json.message;
    }
}

