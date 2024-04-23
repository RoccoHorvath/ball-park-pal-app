const axios = require('axios');

async function fetchData(prop, team, book,endpoint) {
  try {
    if (team) team = team[2] == ' ' ? team.slice(0, 2) : team.slice(0, 3);
    const queryParams = {
      prop: prop,
      team: team,
      book: book,
    };
    let url = new URL(`http://localhost:8080/api/${endpoint}?`);
    for (let param in queryParams) {
      if (queryParams[param]) {
        url.searchParams.append(param, queryParams[param]);
      }
    }

    console.log(url);

    let response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error('API request failed:', error);
    return null;
  }
}

module.exports = fetchData;
