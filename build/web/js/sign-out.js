async function signOut() {
    const result = await Swal.fire({
        title: 'Are you sure?',
        text: 'Do you want to log out?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, log out',
        cancelButtonText: 'Cancel'
    });

    if (result.isConfirmed) {
        // Proceed with logout
        try {
            const response = await fetch("SignOut");
            if (response.ok) {
                const json = await response.json();
                if (json.status) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Logged Out',
                        showConfirmButton: false,
                        timer: 1000
                    }).then(() => {
                        window.location = "sign-in.html";
                    });
                } else {
                    Swal.fire("Oops!", "Something went wrong. Please try again.", "error");
                }
            } else {
                Swal.fire("Error", "Logout request failed.", "error");
            }
        } catch (error) {
            console.error("Logout error:", error);
            Swal.fire("Error", "Something went wrong.", "error");
        }
    }
}
