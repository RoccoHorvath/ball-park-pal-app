# Ballpark Pal Discord Bot

The Ballpark Pal Discord bot provides a quick and easy way to get all the favorable bets as indicated by Ballparkpal.com

## Installation

Installation guide coming soon.

## How it works

There are three core technologies that allow the bot to work. First, a python script scrapes https://ballparkpal.com/PlayerProps.php and creates a JSON file with the bets deemed favorable. Next, a Java Spring Boot app creates REST endpoints for batter and pitcher prop bets and calculates the expected value and recommended bet size for each bet. Finally, the Discord bot itself is written in JavaScript using Discord.js.

When a user in Discord calls a slash command such as '/batterprops', the discord bot requests data from the /api/batterProps end point, creates embeds to neatly display the data, and posts it in the channel.

## Usage
