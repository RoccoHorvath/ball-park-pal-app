const { SlashCommandBuilder } = require('discord.js');
const fs = require('fs');
const path = require('path');
const fetchData = require('../../utils/fetchData');
const createPitcherTable = require('../../utils/createPitcherTable');

module.exports = {
  data: new SlashCommandBuilder()
    .setName('pitcherprops')
    .setDescription('Returns favorable pitcher props from Ballpark Pal')
    .addStringOption((option) =>
      option
        .setName('prop')
        .setDescription('Prop bet you want odds for')
        .setChoices(
          { name: 'Strikeouts', value: 'Strikeouts' },
          { name: 'Hits Allowed', value: 'Hits Allowed' }
        )
    )
    .addStringOption((option) =>
      option
        .setName('game')
        .setDescription('Game you want to see bets for')
        .setAutocomplete(true)
    )
    .addStringOption((option) =>
      option
        .setName('book')
        .setDescription('Sportsbook to get bets from')
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
    const prop = interaction.options.getString('prop');
    const game = interaction.options.getString('game');
    const book = interaction.options.getString('book');
    const jsonData = await fetchData(prop, game, book, 'pitcherProps');
    if (!jsonData)
      interaction.reply({ content: 'Failed to fetch data from the API.' });
    embeds = [];
    for (let propKey in jsonData) {
      const embed = createPitcherTable(jsonData[propKey], propKey);
      if (embed) embeds.push(embed);
    }

    let reply = '';
    if (prop == null) reply = 'Here are the latest favorable pitcher props';
    else reply = `Here are the latest favorable ${prop} prop bets`;

    if (game) reply += ` for the ${game} game`;
    if (book) reply += ` on ${book}`;

    if (embeds.length > 0) {
      interaction.reply({ content: reply });
      interaction.channel.send({ embeds: embeds });
    } else {
      interaction.reply({
        content: `No favorable bets found with options:\n* Prop: ${
          prop ?? 'all'
        }\n* Game: ${game ?? 'all'}\n* Book: ${book ?? 'all'}`,
      });
    }
  },
  autocomplete: ({ interaction, client, handler }) => {
    const gamesPath = path.join(
      __dirname,
      '../../../../python-scripts/Games.json'
    );
    const games = JSON.parse(fs.readFileSync(gamesPath, 'utf8'));

    const focusedGameOption = interaction.options
      .getString('game')
      .toLowerCase();

    const filteredChoices = games.filter((game) =>
      game.name.toLowerCase().includes(focusedGameOption)
    );
    const results = filteredChoices.map((game) => {
      return {
        name: game.name,
        value: game.value,
      };
    });
    interaction.respond(results);
  },
};
