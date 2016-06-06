# jstabankenrestapi
REST API för JSTA Banken
Här finner du en jar fil som startar ett REST API för en enkel bank.
Filen är tänkt att användas för undervisning av REST API testning med SoapUI.
Du har också källkoden om du vill ändra på API:et

För att starta REST API:et:

1a. Klona repot: git clone https://github.com/saadhashim/jstabankenrestapi.git

1b. Bygg koden (Endast om du har ändrat något): mvn clean install 

2. Kör filen: java -Dport=<portnumber> -jar target/jstabanken-rest-1.0-SNAPSHOT-jar-with-dependencies.jar 

Resultat: Du får ett REST API med addressen: http://localhost:<portnumber>/jstabanken-rest-api

Wadl filen hittas på: http://localhost:<portnumber>/jstabanken-rest-api/application.wadl

Gå till http://jsta.se för kurser i SoapUI och testautomatisering

-------------------

REST API for JSTA Bank
Here you find a jar file that starts a REST API for a simple bank.
The file is amed for learining automation of REST API tests using SoapUI.
You also have the source code in case you want to change something.

To start the REST API:

1a. Clone the repo: git clone https://github.com/saadhashim/jstabankenrestapi.git

1b. Build the code (Only if you change something): mvn clean install 

2. Run the file: java -Dport=<portnumber> -jar target/jstabanken-rest-1.0-SNAPSHOT-jar-with-dependencies.jar 

Result: You will get an API with the address: http://localhost:<portnumber>/jstabanken-rest-api

The Wadl file will be found at: http://localhost:<portnumber>/jstabanken-rest-api/application.wadl

Go to http://jsta.se for cources in SoapUI and test automation
