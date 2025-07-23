async function signUp() {
    const firstname = document.getElementById("fname").value;
    const lastname = document.getElementById("lname").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const mobile = document.getElementById("mobile").value;

    const user = {
        firstname,
        lastname,
        email,
        password,
        mobile
    };

    const popup = Notification();


    try {
        const response = await fetch("SignUp", {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const json = await response.json();
//            console.log("Response:", json);

            if (json.status) {
                window.location.href = "verify-account.html";
            } else {
                document.getElementById("message").innerHTML = json.message;

            }
        } else {
            document.getElementById("message").innerHTML = json.message;
        }


    } catch (error) {
        console.error(error);
    }
}