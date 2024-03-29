const axios = require('axios');
const { EmbedBuilder } = require('discord.js');

async function fetchDataFromAPI() {
  try {
    const response = await axios.get('http://localhost:8080/api/batterProps');
    return response.data;
  } catch (error) {
    console.error('API request failed:', error);
    return null;
  }
}

function createTable(jsonData) {
  const embed = new EmbedBuilder().setTitle('Hits').setColor('#0099ff');
  const embed2 = new EmbedBuilder().setTitle('Home Runs').setColor('#0099ff');
  const embed3 = new EmbedBuilder().setTitle('Bases').setColor('#0099ff');
  count = 0
  jsonData['Hits'].forEach((player) => {
    if(count==20){return}
    embed.addFields({
      name: player.playerName,
      value: `O ${player.betName} - BP: ${player.bp}  |  FD: ${
        player.fd ? player.fd : '---'
      }  | DK: ${
        player.dk ? player.dk : '---'
      }  |  EV: ${player.expectedValue.toFixed(
        2
      )}%  |  Bet Size: ${player.betSize.toFixed(2)} units`,
    });
    count++
  });

  count = 0
  jsonData['Home Runs'].forEach((player) => {
    if(count==20){return}
    embed2.addFields({
      name: player.playerName,
      value: `O ${player.betName} - BP: ${player.bp}  |  FD: ${
        player.fd ? player.fd : '---'
      }  | DK: ${
        player.dk ? player.dk : '---'
      }  |  EV: ${player.expectedValue.toFixed(
        2
      )}%  |  Bet Size: ${player.betSize.toFixed(2)} units`,
    });
    count++
  });

  count = 0
  jsonData['Bases'].forEach((player) => {
    if(count==20){return}
    embed3.addFields({
      name: player.playerName,
      value: `O ${player.betName} - BP: ${player.bp}  |  FD: ${
        player.fd ? player.fd : '---'
      }  | DK: ${
        player.dk ? player.dk : '---'
      }  |  EV: ${player.expectedValue.toFixed(
        2
      )}%  |  Bet Size: ${player.betSize.toFixed(2)} units`,
    });
    count++
  });

  return [embed, embed2, embed3];
}

module.exports = {
  name: 'batterprops',
  description:
    'gets latest favorable batter props from https://ballparkpal.com/PlayerProps.php',
  // devOnly: Boolean,
  // testOnly: Boolean,
  // options: Object[],
  // delete: Boolean

  callback: async (client, interaction) => {
    const jsonData = await fetchDataFromAPI();
    if (jsonData) {
      const embed = createTable(jsonData);
      interaction.reply({
        content: 'Here are the latest favorable batter props from BallparkPal',
      });
      interaction.channel.send({ embeds: embed });
    } else {
      interaction.reply({ content: 'Failed to fetch data from the API.' });
    }
  },
};
