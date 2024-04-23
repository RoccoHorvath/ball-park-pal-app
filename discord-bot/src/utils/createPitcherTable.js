const { EmbedBuilder } = require('discord.js');

function createPitcherTable(jsonData, prop, game) {
  const embed = new EmbedBuilder().setTitle(game ?? prop).setColor('#0099ff');

  if (Object.keys(jsonData).length==0) return;
  let fieldCount = 0;

  for (const pitcher in jsonData) {
    if (fieldCount === 20) break;
    const values = [];

    for (const player of jsonData[pitcher]) {
      let booksStr = '';
      for (const book in player.books) {
        booksStr += `${book}: ${player.books[book]} | `;
      }
      values.push(
        `**O ${player.line} | BP: ${
          player.bp
        } | EV: ${player.expectedValue.toFixed(2)}%**\n${booksStr.slice(0, -3)}`
      );

      if (values.length > 1) break;
    }

    embed.addFields({
      name: pitcher,
      value: values.join('\n'),
    });

    fieldCount++;
  }

  return embed;
}

module.exports = createPitcherTable;
