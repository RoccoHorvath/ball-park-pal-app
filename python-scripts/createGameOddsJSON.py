import requests
import json
import os

API_KEY = os.environ.get("THE_ODDS_API_KEY")

SPORT = 'baseball_mlb' 

REGIONS = 'us'

MARKETS = 'h2h' 

ODDS_FORMAT = 'decimal'

DATE_FORMAT = 'iso'

def createOddsJSON():
    if API_KEY == None:
        return
    odds_response = requests.get(f'https://api.the-odds-api.com/v4/sports/{SPORT}/odds', params={
        'api_key': API_KEY,
        'regions': REGIONS,
        'markets': MARKETS,
        'oddsFormat': ODDS_FORMAT,
        'dateFormat': DATE_FORMAT,
    })

    if odds_response.status_code != 200:
        print(f'Failed to get odds: status_code {odds_response.status_code}, response body {odds_response.text}')
        return

    else:
        # Check the usage quota
        print('Remaining requests', odds_response.headers['x-requests-remaining'])
        print('Used requests', odds_response.headers['x-requests-used'])
        return odds_response.json()

odds_json = createOddsJSON()

if odds_json:
    with open("gameOdds.json", 'w') as fp:
        json.dump(odds_json, fp)