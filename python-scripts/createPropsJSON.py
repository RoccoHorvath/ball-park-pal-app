from bs4 import BeautifulSoup
import requests
import json
import os

os.chdir(os.path.dirname(os.path.abspath(__file__)))

def convertToInt(text):
    if text == '':
        return 0
    try:
        num = int(text)
        return num
    except ValueError:
        print(f"Error converting {text} to int")
        return None

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36',
}
prop_url = 'https://ballparkpal.com/PlayerProps.php'
page = requests.get(prop_url,headers=headers)
soup = BeautifulSoup(page.content, 'html.parser')
trs = soup.find_all('tr')
batterProps = {}
pitcherProps = {'pitchers': {}}
for tr in trs:
    tds = tr.find_all('td')
    
    if len(tds) < 6:
        continue
    bp = convertToInt(tds[7].text)
    fd = convertToInt(tds[8].text)
    dk = convertToInt(tds[9].text)
    if bp:
        prop = {'bp': bp}
        if fd and bp < fd:
            prop['fd'] = fd

        if dk and bp < dk:
            prop['dk'] = dk
    else:
        continue
        
    if 'fd' in prop or 'dk' in prop:
        playerName = tds[1].text
        betName = tds[6].text

        if betName[0:3] == "O K":
            prop['betName'] = betName
            if playerName in pitcherProps['pitchers']:
                pitcherProps['pitchers'][playerName].append(prop)
            else:
                pitcherProps['pitchers'][playerName] = []
                pitcherProps['pitchers'][playerName].append(prop)
        else:
            if  betName not in batterProps:
                batterProps[betName] = []
            prop['playerName'] = playerName
            batterProps[betName].append(prop)



with open("batterProps.json", 'w') as fp:
    json.dump(batterProps, fp)

with open("pitcherProps.json", 'w') as fp:
    json.dump(pitcherProps, fp)
