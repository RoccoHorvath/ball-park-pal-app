from bs4 import BeautifulSoup
import requests
import json
import os

os.chdir(os.path.dirname(os.path.abspath(__file__)))

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36',
}
batter_markets = {
    10: "Home Runs",
    13: "Hits",
    14: "Bases",
}

pitcher_markets = {
    20: "Strikeouts",
}

prop_url = 'https://www.ballparkpal.com/PlayerProps.php?BetSide=1&BetMarket='

def convertToInt(text):
    if text == '':
        return 0
    try:
        num = int(text)
        return num
    except ValueError:
        print(f"Error converting {text} to int")
        return None


def getTRs(market):
    page = requests.get(f'{prop_url}{market}',headers=headers)
    soup = BeautifulSoup(page.content, 'html.parser')
    return soup.find_all('tr')


def createProp(tr):
    tds = tr.find_all('td')
    
    if len(tds) < 6:
        return None, None
    bp = convertToInt(tds[6].text)
    fd = convertToInt(tds[7].text)
    dk = convertToInt(tds[8].text)
    if bp:
        prop = {'bp': bp}
        if fd and bp < fd:
            prop['fd'] = fd

        if dk and bp < dk:
            prop['dk'] = dk
        
        if 'fd' in prop or 'dk' in prop:
            playerName = tds[1].text
            prop['playerName'] = playerName
            prop['betName'] = tds[5].text
            return prop, playerName

    return None, None     

    
batterProps = {}
for market in batter_markets.keys():
    trs = getTRs(market)
    betName = batter_markets.get(market)
    batterProps[betName] = []

    for tr in trs:
        prop, playerName = createProp(tr)
        if prop:
            if market == 13 and prop["betName"] == "1.5":
                continue
            batterProps[betName].append(prop)

with open("batterProps.json", 'w') as fp:
    json.dump(batterProps, fp)
    

pitcherProps = {'pitchers': {}}
for market in pitcher_markets.keys():
    trs = getTRs(market)
    betName = pitcher_markets.get(market)
    for tr in trs:
        prop, playerName = createProp(tr)
        if prop:
            if playerName not in pitcherProps['pitchers']:
                pitcherProps['pitchers'][playerName] = [] 
            pitcherProps['pitchers'][playerName].append(prop)

with open("pitcherProps.json", 'w') as fp:
    json.dump(pitcherProps, fp)
