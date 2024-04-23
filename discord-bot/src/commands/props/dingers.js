const { SlashCommandBuilder } = require('discord.js');
const fs = require('fs');
const path = require('path');
const fetchData = require('../../utils/fetchData');
const createBatterTable = require('../../utils/createBatterTable');

module.exports = {
  data: new SlashCommandBuilder()
    .setName('dingers')
    .setDescription('Get the best home run bets for each game')
    .addStringOption((option) =>
      option
        .setName('book')
        .setDescription('Sportsbook to get bets from. Defaults to FanDuel')
        .setChoices(
          { name: 'FanDuel', value: 'FD' },
          { name: 'DraftKings', value: 'DK' },
          { name: 'Ceasers', value: 'CZ' },
          { name: 'ESPN Bets', value: 'ES' },
          { name: 'BetMGM', value: 'MG' },
          { name: 'Bet365', value: 'B3' }
        )
    ),

  run: async ({ interaction, client, handler }) => {
    let embeds = [];
    const book = interaction.options.getString('book');
    const gamesPath = path.join(
      __dirname,
      '../../../../python-scripts/Games.json'
    );
    const games = JSON.parse(fs.readFileSync(gamesPath, 'utf8'));
    for (const game of games) {
      const jsonData = await fetchData(
        'Home Runs',
        game.name,
        book ?? 'FD',
        'batterProps'
      );
      if (!jsonData) continue;

      for (let propKey in jsonData) {
        const embed = createBatterTable(jsonData, propKey, game.name);

        if (embed) embeds.push(embed);
      }
    }

    if (embeds.length > 0) {
      interaction.reply({
        content: `Here are the best home run bets for each game on ${
          book ?? 'FD'
        }`,
      });
      interaction.channel.send({ embeds: embeds.slice(0, 10) });
      if (embeds.length > 10)
        interaction.channel.send({ embeds: embeds.slice(10) });
    } else {
      interaction.reply({
        content: 'No favorable home run bets',
      });
    }
  },
};
