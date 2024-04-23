import statsapi
import json

teams = {
108: 'LAA',
109: 'ARI',
110: 'BAL',
111: 'BOS',
112: 'CHC',
113: 'CIN',
114: 'CLE',
115: 'COL',
116: 'DET',
117: 'HOU',
118: 'KC',
119: 'LAD',
120: 'WAS',
121: 'NYM',
133: 'OAK',
134: 'PIT',
135: 'SD',
136: 'SEA',
137: 'SF',
138: 'STL',
139: 'TB',
140: 'TEX',
141: 'TOR',
142: 'MIN',
143: 'PHI',
144: 'ATL',
145: 'CHW',
146: 'MIA',
147: 'NYY',
158: 'MIL',
}


schedule = statsapi.schedule()
games = [] 
gamesSet = set()
for game in schedule:
    homeTeam = teams.get(game.get('home_id'))
    awayTeam = teams.get(game.get('away_id'))
    gameStr = f'{awayTeam} vs {homeTeam}'
    
    if gameStr in gamesSet: continue
    gamesSet.add(gameStr)

    games.append({
        'name': gameStr,
        'value': gameStr,
        'time': game.get('game_datetime')
    })


with open('Games.json',"w") as f:
    json.dump(sorted(games, key=lambda x: x['time']),f)
