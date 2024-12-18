# IOD-L11-Alpha

## Członkowie zespołu: 
- Filip Domanski 151575
- Aleksander Kaminski 155840
- Wojciech Kasprzak 155824
- Piotr Kobusinski 155182

## Opis projektu: 
Dla administratorów budynków, którzy pragną optymalizowac koszty zarządzania budynkami nasza aplikacja Building Info umożliwi pozyskanie informacji o parametrach budynku na poziomie pomieszczen, kondygnacji oraz całych budynków. Aplikacja będzie dostępna poprzez GUI a także jako zdalne API dzięki czemu można ją zintegrowac z istniejącymi narzędziami.

## Struktura danych: 
-  Lokacja to budynek, poziom, lub pomieszczenie
- Budynek może składac się z poziomów a te z pomieszczen
- Każda lokalizacja jest charakteryzowana przez:
    - id – unikalny identyfikator
    - name – opcjonalna nazwa lokalizacji
- Pomieszczenie dodatkowo jest charakteryzowane przez:
   - area = powierzchnia w m^2
   - cube = kubatura pomieszczenia w m^3
   - heating = poziom zużycia energii ogrzewania (float)
   - light – łączna moc oświetlenia
 
## Informacje o projekcie 
[Definition of Done](https://docs.google.com/spreadsheets/d/e/2PACX-1vTn6j3M8pmGEzrsQk8mXse7lVHUdhYWkfxbkQiYI23rBtwM4N3bWw0qtupW-gesfCkcYasnZ-eEXl-F/pubhtml) <br/>
[Zarządzanie rejestrem produktu i sprintu](https://docs.google.com/spreadsheets/d/1KLt9HXL6Aha6orhv00Ywbj6j-ZUO8qhp/edit?usp=sharing&ouid=107019924464445307168&rtpof=true&sd=true)
