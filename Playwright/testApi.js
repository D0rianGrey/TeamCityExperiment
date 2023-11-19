async function fetchJson(url) {
    const response = await fetch(url);
    const json = await response.json();
    return json.id;
}

const data = fetchJson('https://jsonplaceholder.typicode.com/todos/1').then(data => {
    console.log(data)
});



