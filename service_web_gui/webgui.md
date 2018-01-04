# How to build minimalistic web GUI for your microservice

It seems that minimalism while building web interfaces for microservices is a quite reasonable idea.

We will see how to build a web GUI following  requirements:
* 'mobile first' design in accordance with RWD
* small amount of libraries to download
* small source  code
* easy to learn

Following the API-first development approach, we have built the store microservice as described in the previous article. We will use now this service as our backend (business logic). 

In order to meet the above requirements, we will use.
* Bootstrap component library
* RIOT UI library
* a small dose of JavaScript

Rozwiąznaie które zbudujemy jest naprawdę małe, razem wszystkie pliki zajmują xx kB:
* Bootrstrap
* Riot + Router
* kod webaplikacji

Dodatkową zaletą jest, że oprócz uruchomienia The Strore nie musimy niczego więcej instalować. Wystarczy nam edytor tekstowy i web browser.

## Page view

Przygotujemy uproszczony template strony że stałym menu oraz trzema kartami prezentującym: strona startową,  przeglądarkę produktów oraz formularz dodawania produktu. W tym celu wykorzystam
y grid system  bootstrapa.

KOD

## Application logic

Do zaimplementowania nawigacji wykorzystamy bibliotekę Router. Odpowiada ona za ...

KOD

## Uruchomienie serwisu

Before the next step we need to run our Store Service as described in the [previous  article](https://www.signocom.com/software-development/microservice-with-java-in-a-flash-not-a-problem-at-all/).

Do folderu, w którym utworzyliśmy nasz plik Index.html z szablonem strony, pobieramy kod źródłowe s
erwisu, kompilujemy i uruchamiamy.

We leave the service going to be able to observe effects of our development  on the fly.

KOD

## JavaScript part

## Page Components

Komponenty zawierają kod HTML, CSS i JavaScript

### Page Header

### Page Footer

### Product Browser

### Product View

## The final effect

To be fully functional, our application needs some additional elements such as: modification and removal of products and user authentication. However, this is the subject of another article.

## Summary

We saw how quickly and easily we can build responsive webapplications using Bootstrap and Riot libraries.

Using the microservice as a backend logic causes that both parts of the service: frontend and backend are well separated and can easily be developed independently. Assuming that we maintain the compliance of the API used.

We hope that we have been able to present the usefulness of the "API first" approach and encourage you to try Bootstrap and Riot as a tools for building web applications quickly and efficiently.
