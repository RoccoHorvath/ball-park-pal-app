const { EmbedBuilder } = require('discord.js');

function createBatterTable(jsonData, prop, game) {
  const embed = new EmbedBuilder().setTitle(game ?? prop).setColor('#0099ff');
  count = 0;
  if (jsonData[prop].length == 0) return;

  jsonData[prop].forEach((player) => {
    if (count == 15) return;
    let booksStr = '';
    for (let book in player.books) {
      booksStr += `${book}: ${player.books[book]} | `;
    }
    embed.addFields({
      name: player.playerName + ' ' + player.team,
      value: `O ${player.line} | BP: ${
        player.bp
      } | EV: ${player.expectedValue.toFixed(2)}%\n${booksStr.slice(0, -3)}`,
    });
    count++;
  });

  return embed;
}

module.exports = createBatterTable;
