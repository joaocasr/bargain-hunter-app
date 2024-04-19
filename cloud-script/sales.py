import pandas as pd
import firebase_admin
import datetime
import random
import re
from bs4 import BeautifulSoup
from urllib.request import urlopen, Request
from firebase_admin import credentials,firestore
from geopy.geocoders import Nominatim

resp={}
all=[]

req = Request(
    url='https://www.kuantokusta.pt/melhores-precos?discountRange=FROM_50', 
    headers={
        'User-Agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36"
        }
)

def do_work():
    page = urlopen(req).read()
    soup = BeautifulSoup(page, "html.parser")
    items = soup.html.body.find("div",{"id":"__next"}).find("main").find("div",class_=re.compile(r"c-geWlrn-ikZmdmp-css")).find("div",{"class":"c-bWWkpE"}).find("div",{"class":"c-geWlrn"}).find("div",{"class":"c-geSCLP"})
    products = items.find_all("div",{"data-test-id":"product-card"})

    for product in products:
        p = product.find("a",{"class":"c-jafrRC"}).find("div",{"class":"c-hsbTVL"})
        img = p.find("div",class_=re.compile(r"c-gtWDsx-iPJLV-css")).find("span").find_all("img")[2]
        image = img.attrs['src']
        name = img.attrs['alt']
        sale = p.find("div",{"class":"c-jsPNpl"}).find("div",class_=re.compile(r"c-gInMWf-jhTNsr-variant-discountPercentage"))
        desconto = sale.find("span").get_text(strip=True)
        preco = product.find("a",{"class":"c-jafrRC"}).find("span",{"class":"c-irxNwi"}).find("span").get_text(strip=True)
        new = {"nome":name,"imagem":image,"preco":preco,"desconto":desconto}
        print(new)
        all.append(new)

    print("scrapping done.")

    size = len(all)
    ruas = []

    i = 1
    dataset = pd.read_csv('locais.csv')
    geolocator = Nominatim(user_agent="myGetLocapp")
    while(i>=1 and i<size+1):
        try:
            n = random.randrange(1,400)
            nome = dataset.iloc[n]['ruas']
            location = geolocator.geocode(nome+", Braga")
            lat = location.latitude
            lng = location.longitude
            streetname = location.address
            ruas.append({"rua":streetname,"latitude":lat,"longitude":lng})
            i+=1
        except:
            pass

    print("locations generated.")

    for i in range(0,len(all)):
        all[i]["rua"] = ruas[i]["rua"]
        all[i]["latitude"] = ruas[i]["latitude"]
        all[i]["longitude"] = ruas[i]["longitude"]

    resp['sales'] = all

    for r in resp['sales']:
        print(r)

    x = datetime.datetime.now()
    data = str(x).split(" ")[0]
    print(data)

    cred = credentials.Certificate("serviceAccount.json")
    firebase_admin.initialize_app(cred)
    DB = firestore.client()
    if DB is not None:
        doc_ref = DB.collection("allsales").document(data)
        doc_ref.set(resp,merge=True)

    
if __name__ == "__main__":
    do_work()
