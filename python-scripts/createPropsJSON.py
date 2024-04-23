from bs4 import BeautifulSoup
import requests
import json
import os

os.chdir(os.path.dirname(os.path.abspath(__file__)))
login_url = 'https://www.ballparkpal.com/LogIn.php'
login_data = {
    'email': os.environ["BPPUSER"],
    'password': os.environ["BPPPASS"],
    'login': 'Login'
}

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36',
}

markets = {
    10: "Home Runs",
    13: "Hits",
    14: "Bases",
    15: "Stolen Bases",
    20: "Strikeouts",
    23: "Hits Allowed",
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


def getPageContent(session,market):
    page = session.get(f'{prop_url}{market}')
    return BeautifulSoup(page.content, 'html.parser')


def getBookHeaders(ths):
    bookHeaders = []
    for i in range(7,13):
        bookHeaders.append(ths[i].text)
    return bookHeaders


def createProp(tr,bookHeaders):
    goodBet = False
    tds = tr.find_all('td')
    
    if len(tds) < 6:
        return None
    bp = convertToInt(tds[6].text)
    allBooks = {}
    for i, book in enumerate(bookHeaders):
        allBooks[book] = convertToInt(tds[7+i].text)
    
    if bp:
        prop = {'bp': bp,'books':{}}
        for book, odds in allBooks.items():
            if odds and odds > bp:
                prop['books'][book] = odds
                goodBet = True

        if goodBet:
            team = tds[0].text.strip()
            prop['team'] = team
            prop['playerName'] = tds[1].text
            prop['line'] = tds[5].text
            prop['opponent'] = tds[2].text.strip()
            return prop

    return None     


session = requests.Session()
session.post(login_url, data=login_data)

for market in markets.keys():
    betName = markets.get(market)
    
    soup = getPageContent(session,market)
    trs = soup.find_all('tr')
    bookHeaders = getBookHeaders(soup.find_all('th'))

    props = []
    for tr in trs:
        prop = createProp(tr,bookHeaders)
        if prop:
            if market == 13 and prop["line"] == "1.5":
                continue
            prop['betName'] = betName
            props.append(prop)

    with open(f'{betName}.json', 'w') as fp:
        json.dump(props, fp)