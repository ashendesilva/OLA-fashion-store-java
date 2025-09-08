async function forgotPassword() {
    const emailInput = document.getElementById("email");
    const email = emailInput.value.trim();
    const sendCodeBtn = document.getElementById("sendCodeBtn");

    if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        emailInput.focus();
        emailInput.classList.add('is-invalid');
        return;
    }
    emailInput.classList.remove('is-invalid');

    sendCodeBtn.disabled = true;
    sendCodeBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i>Sending...';

    try {
        const response = await fetch("ForgotPassword", {
            method: "POST",
            body: JSON.stringify({email}),
            headers: {"Content-Type": "application/json"}
        });

        const json = await response.json();

        if (json.status) {
            toStep('code');
            startTimer();
            buildCodeInputs();
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: json.message
            });
        }
    } catch (error) {
        console.error("Error:", error);
    } finally {
        // Reset button state
        sendCodeBtn.disabled = false;
        sendCodeBtn.innerHTML = '<i class="fa-solid fa-paper-plane me-2"></i> Send Reset Code';
    }
}

async function verifyCode() {
    const code = document.getElementById("verificationCode").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;


    const verification = {
        verificationCode: code,
        newPassword: newPassword,
        confirmPassword: confirmPassword
    };

    const verificationJson = JSON.stringify(verification);

    const response = await fetch(
            "VerifyResetCode",
            {
                method: "POST",
                body: verificationJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.status) {
            Swal.fire({
                icon: 'success',
                title: 'Password Reset Successful!',
                text: 'You can now log in with your new password.',
                confirmButtonText: 'OK'
            }).then(() => {
                window.location = "index.html";
            });

        } else {
            if (json.message === "Email Not Found") {
                Swal.fire({
                    icon: 'error',
                    title: 'Email Not Found',
                    text: 'Please sign up before resetting your password.',
                    confirmButtonText: 'Sign Up'
                }).then(() => {
                    window.location = "sign-up.html";
                });

            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Verification Failed',
                    text: json.message || 'Something went wrong!',
                    confirmButtonText: 'Try Again'
                });
            }
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