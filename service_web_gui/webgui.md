# How to build minimalistic web GUI for your microservice

Wydaje się,  że minimalizm przy tworzeniu webowego interfejsu dla mikroserwisu jest jak najbardziej uzasadnionym pomysłem.

Pokażemy jak zbudować web GUI spełniające założenia
* działanie dla mobile i desktop
* mały rozmiar plików do download
* mały kod źródłowy
* krótki czas nauki

W poprzednim artykule przygotowaliśmy mikroserwis wykorzystując framework Cricket, ktory teraz wykorzystamy. Natomiast w spełnieniu powyższych założeń pomoże nam zastosowanie

* Bootstrap komponent library
* RIOT UI library

## Page ...

Przygotujemy uproszczony templatów strony że stałym menu oraz trzema kartami prezentującym: strona startową,  przeglądarkę produktów oraz formularz dodawania produktu. W tym celu wykorzystam
y grid system  bootstrapa.

KOD

## Nawigacja

Do zaimplementowania nawigacji wykorzystamy bibliotekę Router. Odpowiada ona za ...

KOD

## Uruchomienie serwisu

Zanim przejdziemy dalej musimy uruchomić nasz Store Service opisany w poprzednim artykule. Do folderu, w którym utworzyliśmy nasz plik Index.html z szablonem strony, pobieramy kod źródłowe s
erwisu, kompilujemy i uruchamiamy. Pozostawiamy działający serwis, żebyśmy mogli na bieżąco śledzić efekt dalszych zmian.

KOD

## Store API client 

## Komponenty strony

### Product View

### Product Form

### Product Browser

## Efekt finalny
Nasza webaplikacja już działa. Do pełnej funkcjonalności brakuje jeszcze kilku elementów, jak np. modyfikowania i usuwania produktów oraz autentykacji i autoryzacji użytkowników. To jednak t
emat na odrębny artykuł.

## Summary

