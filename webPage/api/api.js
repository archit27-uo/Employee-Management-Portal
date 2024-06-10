const headers = new Headers({
    'Authorization': 'Basic ' + localStorage.getItem('authToken'),
    'Content-Type': 'application/json',
    'ngrok-skip-browser-warning': '69420'
});

async function fetchData(url, method) {
    try {
        const response = await fetch(url, { headers: headers, method: method });
        if (!response.ok) throw new Error(await response.text());
        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        showMessage(`Failed due to: ${error.message}`, 'error');
        throw error;
    }
}

module.exports = { fetchData };