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
  const embed = new EmbedBuilder().setTitle('O Hits 0.5').setColor('#0099ff');
  const embed2 = new EmbedBuilder().setTitle('O HR 0.5').setColor('#0099ff');
  const embed3 = new EmbedBuilder().setTitle('O Bases 1.5').setColor('#0099ff');

  jsonData['O Hits 0.5'].forEach((player) => {
    embed.addFields({
      name: player.playerName,
      value: `BP: ${player.bp}  |  FD: ${
        player.fd ? player.fd : '---'
      }  | DK: ${
        player.dk ? player.dk : '---'
      }  |  EV: ${player.expectedValue.toFixed(
        2
      )}%  |  Bet Size: ${player.betSize.toFixed(2)} units`,
    });
  });

  jsonData['O HR 0.5'].forEach((player) => {
    embed2.addFields({
      name: player.playerName,
      value: `BP: ${player.bp}  |  FD: ${
        player.fd ? player.fd : '---'
      }  | DK: ${
        player.dk ? player.dk : '---'
      }  |  EV: ${player.expectedValue.toFixed(
        2
      )}%  |  Bet Size: ${player.betSize.toFixed(2)} units`,
    });
  });

  jsonData['O Bases 1.5'].forEach((player) => {
    embed3.addFields({
      name: player.playerName,
      value: `BP: ${player.bp}  |  FD: ${
        player.fd ? player.fd : '---'
      }  | DK: ${
        player.dk ? player.dk : '---'
      }  |  EV: ${player.expectedValue.toFixed(
        2
      )}%  |  Bet Size: ${player.betSize.toFixed(2)} units`,
    });
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
