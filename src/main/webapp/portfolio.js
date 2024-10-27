

function logout() {
    localStorage.removeItem('isLoggedIn');
    window.location.href = 'login.html';
}

document.addEventListener('DOMContentLoaded', function() {
    fetchPortfolioData(); 
});

function fetchPortfolioData() {
    fetch('PortfolioServlet', {
        method: 'GET',
    })
    .then(response => response.json())
    .then(data => {
		 console.log(data); 
        updatePortfolioDisplay(data.stocks);
        updateAccountValues(data.cashBalance, data.accountValue);
    })
    .catch(error => {
        console.error('Failed to fetch portfolio data:', error);
        alert('Failed to fetch portfolio data.');
    });
}

function updatePortfolioDisplay(portfolioData) {
	
    const portfolioContainer = document.getElementById('portfolioContainer');
    portfolioContainer.innerHTML = ''; 

    portfolioData.forEach(stock => {
		if(stock.quantity > 0){
		const changeIcon = stock.change > 0 ? '<i class="fas fa-caret-up"></i>' : '<i class="fas fa-caret-down"></i>';
        const changeClass = stock.change > 0 ? 'positive' : 'negative';
        portfolioContainer.innerHTML += `
           <div class="stock-item">
                    <h3>${stock.symbol} - ${stock.companyName}</h3>
                    <table>
                        <tr>
                            <td>Quantity:</td>
                            <td>${stock.quantity}</td>
                            <td>Change:</td>
                            <td class="${changeClass}">${changeIcon} ${stock.change.toFixed(2)}</td>
                        </tr>
                        <tr>
                            <td>Avg. Cost / Share:</td>
                            <td>${stock.avgCostPerShare.toFixed(2)}</td>
                            <td>Current Price:</td>
                            <td>${stock.currentPrice.toFixed(2)}</td>
                        </tr>
                        <tr>
                            <td>Total Cost:</td>
                            <td>${stock.totalCost.toFixed(2)}</td>
                            <td>Market Value:</td>
                            <td>${stock.marketValue.toFixed(2)}</td>
                        </tr>
                    </table>
                    <form class="trade-form" data-symbol="${stock.symbol}" data-price="${stock.currentPrice}">
                        <div class="form-group">
                            <label>Quantity:  <input type="number" name="quantity" min="1" required></label>
                            <label><input type="radio" name="tradeType" value="buy" required> Buy</label>
                            <label><input type="radio" name="tradeType" value="sell"> Sell</label>
                            <button type="submit">Submit</button>
                        </div>
                    </form>
                </div>
            `;
            }
    });

    document.querySelectorAll('.trade-form').forEach(form => {
        form.addEventListener('submit', handleTrade);
    });
}

function handleTrade(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const symbol = form.getAttribute('data-symbol');
    const price = form.getAttribute('data-price');
    const quantity = formData.get('quantity');
    const tradeType = formData.get('tradeType');

    fetch('TradeServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
			tradeID:0,
			username:localStorage.getItem("username"),
            ticker: symbol,
            numStock: parseInt(quantity),
            price: parseFloat(price),
            tradeType: tradeType
        })
    })
    .then(response => response.text())
    .then(data => {
        alert(data);
        fetchPortfolioData(); 
    })
    .catch(error => {
        console.error('Error processing trade:', error);
        alert('Error processing trade.');
    });
}

function updateAccountValues(cashBalance, accountValue) {
    const accountInfoContainer = document.getElementById('accountInfoContainer');
    accountInfoContainer.innerHTML = ''; 

    accountInfoContainer.innerHTML += `<h2>My Portfolio</h2>
            	<div>Cash Balance: <span id="cashBalance">${cashBalance.toFixed(2)}</span></div>
            <div>Total Account Value: <span id="totalValue">${accountValue.toFixed(2)}</span></div>`;
    

}

