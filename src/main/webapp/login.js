document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    // Prepare the request data
    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);
    

    // Perform the AJAX request
    fetch('LoginServlet', {
        method: 'POST',
        body: formData,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'  
        }
    })
    .then(response => response.text())
    .then(data => {
        console.log('Success:', data);
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('username',username);
        console.log("using username instead : ", username);
        
        window.location.href = 'index.html'; 
    })
    .catch(error => console.error('Error:', error));
});

document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const email = document.getElementById('signupEmail').value;
    const username = document.getElementById('signupUsername').value;
    const password = document.getElementById('signupPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if(password !== confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    // Prepare the request data
    const formData = new URLSearchParams();
    formData.append('email', email);
    formData.append('username', username);
    formData.append('password', password);

    // Perform the AJAX request
    fetch('RegisterServlet', {
        method: 'POST',
        body: formData,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded' 
        }
    })
    .then(response => response.text())
    .then(data => {
        console.log('Success:', data);
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('username',username);
        window.location.href = 'index.html';
    })
    .catch(error => console.error('Error:', error));
});
