const axios = require('axios');
const AsciiTable = require('ascii-table');
const { EmbedBuilder } = require('discord.js');

async function fetchDataFromAPI() {
  try {
    const response = await axios.get('http://localhost:8080/api/pitcherProps');
    return response.data;
  } catch (error) {
    console.error('API request failed:', error);
    return null;
  }
}

function createTable(jsonData) {
  const embed = new EmbedBuilder()
    .setTitle('Pitcher Props')
    .setColor('#0099ff');

  for (const pitcher in jsonData.pitchers) {
    const pitcherData = jsonData.pitchers[pitcher];
    values = [];
    for (const prop of pitcherData) {
      values.push(
        `${prop.betName}: BP: ${prop.bp}  |  FD: ${
          prop.fd ? prop.fd : '---'
        }  | DK: ${
          prop.dk ? prop.dk : '---'
        }  |  EV: ${prop.expectedValue.toFixed(
          2
        )}%  |  Bet Size: ${prop.betSize.toFixed(2)} units`
      );
    }
    embed.addFields({
      name: pitcher,
      value: values.join('\n'),
    });
  }

  return [embed];
}

module.exports = {
  name: 'pitcherprops',
  description:
    'gets latest favorable pitcher props from https://ballparkpal.com/PlayerProps.php',
  // devOnly: Boolean,
  // testOnly: Boolean,
  // options: Object[],
  // delete: Boolean

  callback: async (client, interaction) => {
    const jsonData = await fetchDataFromAPI();
    if (jsonData) {
      const embed = createTable(jsonData);
      interaction.reply({
        content: 'Here are the latest favorable pitcher props from BallparkPal',
      });
      interaction.channel.send({ embeds: embed });
    } else {
      interaction.reply({ content: 'Failed to fetch data from the API.' });
    }
  },
};
