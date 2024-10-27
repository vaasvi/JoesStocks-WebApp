const finnhubApiKey = 'co0d7n9r01qg06er6730co0d7n9r01qg06er673g'; // Replace with your actual API key

//updates nav bar depending on whether logged in or not
document.addEventListener('DOMContentLoaded', () => {
	 console.log('Document loaded, updating navbar based on login state.');
  const navBar = document.getElementById('navBar');
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';

  let linksHtml = '<a href="index.html">Home / Search</a>';
  if (isLoggedIn) {
    linksHtml += `
      <a href="portfolio.html">Portfolio</a>
      <a href="index.html" onclick="logout()">Logout</a>
    `;
  } else {
    linksHtml += '<a href="login.html">Login / Sign Up</a>';
  }
  navBar.innerHTML = linksHtml;
});

// This function will be called when the search button is clicked
function searchStock() {
  console.log('Search button clicked');
  var symbol = document.getElementById('stockTicker').value.trim();
  if (!symbol) {
      alert('Please enter a stock symbol.');
      return;
  }

  const apiKey = 'co0d7n9r01qg06er6730co0d7n9r01qg06er673g'; // Replace with your actual API key
  const quoteUrl = `https://finnhub.io/api/v1/quote?symbol=${encodeURIComponent(symbol)}&token=${apiKey}`;
  const profileUrl = `https://finnhub.io/api/v1/stock/profile2?symbol=${encodeURIComponent(symbol)}&token=${apiKey}`;

  Promise.all([
      fetch(quoteUrl).then(response => response.json()),
      fetch(profileUrl).then(response => response.json())
  ]).then(([quoteData, profileData]) => {
      updateStockDisplay(symbol, quoteData, profileData);
  }).catch(error => {
      console.error('Failed to fetch stock data:', error);
      alert('Failed to fetch stock data.');
  });
}

function updateStockDisplay(symbol,quoteData,companyProfileData){
  
  var searchContainer = document.querySelector('.search-container'); 
  var stockInfoDiv = document.getElementById('stockInfoDiv');

  console.log('inside updateStockDisplay'); 
 
  searchContainer.style.display = 'none';
  mainTitle.style.display = 'none';
   const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';

    if (isLoggedIn) {
  
    stockInfoDiv.innerHTML = `
    <div class="stock-loggedin" >
        <div class="stock-info-left">
            <h2>${companyProfileData.ticker}</h2>
            <p>${companyProfileData.name}</p>
            <p>${companyProfileData.exchange}</p>
            <div class="quantity-label">
            <label for="quantity">Quantity:</label>
            <input type="number" id="quantity" name="quantity" >
            </div>
            <button onclick="buyStock('${companyProfileData.ticker}')">Buy</button>
        </div >
        <div class="stock-info-right ${quoteData.d > 0 ? 'positive' : 'negative'}">
            <h1>${quoteData.c}</h1>
            <span class="${quoteData.d > 0 ? 'up' : 'down'}">
            <i class="fas fa-caret-${quoteData.d > 0 ? 'up' : 'down'}"></i>
                    ${quoteData.d} (${quoteData.dp}%)
                </span>  
            <h4>${getCurrentDateTime()}</h4>  
        </div>
      </div>
      <p>Market is ${isMarketOpen?'Open':'Closed'} </p>
    `;
  }

   else {
     stockInfoDiv.innerHTML = `
     <div class=stock-info>
     <h3>${companyProfileData.ticker}</h3>
     <h3>${companyProfileData.name}</h3>
     <h3>${companyProfileData.exchange}</h3>
     </div>
 `;

  }
  // Populate the stock info container with data
  stockInfoDiv.innerHTML += `
      <div class="summary">
      <h3>Summary</h3>
          <p>High Price: ${quoteData.h}</p>
          <p>Low Price: ${quoteData.l}</p>
          <p>Open Price: ${quoteData.o}</p>
          <p>Close Price: ${quoteData.pc}</p>
      </div>
      <div class="company-info">
          <h3>Company Information</h3>
          <p>IPO Date: ${companyProfileData.ipo}</p>
          <p>Market Cap (SM): ${companyProfileData.marketCapitalization}</p>
          <p>Share Outstanding: ${companyProfileData.shareOutstanding}</p>
          <p>Website: <a href="${companyProfileData.weburl}" target="_blank">${companyProfileData.weburl}</a></p>
          <p>Phone: ${companyProfileData.phone}</p>
      </div>
  `;
  // Show the stock info container
  stockInfoDiv.style.display = 'block';

}
function buyStock(ticker) {
    
    const quantityInput = document.getElementById('quantity');
    const quantity = parseInt(quantityInput.value);

    // Check if the quantity is a positive integer
    if (!quantity || quantity <= 0) {
        alert('FAILED: Purchase not possible.');
        return;
    }

    const apiKey = 'co0d7n9r01qg06er6730co0d7n9r01qg06er673g';
    const quoteUrl = `https://finnhub.io/api/v1/quote?symbol=${encodeURIComponent(ticker)}&token=${apiKey}`;

    // Fetch the stock quote data
    fetch(quoteUrl).then(response => response.json()).then(quoteData => {
        // Check if quoteData contains the current price
        if (!quoteData.c || quoteData.c <= 0) {
            alert('FAILED: Purchase not possible.');
            return;
        }

        // Constructing the trade details object using the fetched price
        const tradeDetails = {
			tradeId:0 ,
           // userId: localStorage.getItem('userId'), 
           username:localStorage.getItem('username'),
            ticker: ticker,
            numStock: quantity,
            price: quoteData.c,
            tradeType: "BUY"
        };
        console.log("Ticker:", ticker);
        console.log("User ID from LocalStorage:", localStorage.getItem('userId'));
        console.log("Username from LocalStorage:", localStorage.getItem('username'));
        // Perform the AJAX request to the TradeServlet
        fetch('TradeServlet', {
            method: 'POST',
            body: JSON.stringify(tradeDetails),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.text())
        .then(data => {
            console.log('Success:', data);
            alert(data); 
            
        })
        .catch(error => {
            console.error('Error:', error);
            alert('There was an error processing your trade.');
        });
    }).catch(error => {
        console.error('Failed to fetch stock data:', error);
        alert('Failed to fetch stock data.');
    });
}



// Call this function to handle user logout
function logout() {
  localStorage.setItem('isLoggedIn', 'false');
  localStorage.removeItem('userId');
  //updateNavbar();
  // Perform additional cleanup if necessary
  window.location.href = 'index.html';
}

//CHATGPTPrompt: function to get current time in MM-DD-YYY and time stamp using Date() function in Javascript
function getCurrentDateTime() {
  const now = new Date();
  const date = now.toLocaleDateString('en-US', { month: '2-digit', day: '2-digit', year: 'numeric' });
  const time = now.toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' });
  return `${date} ${time}`;
}

//CHATGPTPrompt: function to check whether market is open or closed using Date() function in Javascript
function isMarketOpen() {
  let now = new Date();  // Assumes PDT
  let hour = now.getHours();
  let minute = now.getMinutes();

  const marketOpenMinute = 30;
  const marketCloseHour = 13;  // 1:00 PM is 13:00 in 24-hour time

  // Check if current time is within the market hours
  if ((hour > marketOpenHour || (hour === marketOpenHour && minute >= marketOpenMinute)) &&
      (hour < marketCloseHour)) {
      return true; // Market is open
  }
  return false; // Market is closed
}

