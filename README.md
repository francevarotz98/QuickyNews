# QuickyNews

<p align="center">
  <a href="https://github.com/francevarotz98/QuickyNews/">
    <img src="ic_launcher.jpeg" alt="Logo" width="250" height="250">
  </a>
  
  <p align="center">
    Browse the latest news close to you 
    <br />


Project made by three students (Giovanni, Pietro e Francesco) of the University of Padua for the course of "Programmazione di Sistemi Embedded 2020".

## Summary

  - [Getting Started](#getting-started)
  - [Usage](#usage)
  
## Getting Started

This app is realized following the Android go guidelines (precisely Build for Billions). 
The aim of this app is to always have news at your fingerprints, even if you are offline, how? Using <a href="https://developer.android.com/topic/libraries/architecture/room">Room.</a>
But what should you have to do to save a news? Just with one click, let's see how.

## Usage 

* Connect your phone to internet

* Click-long on a news you'd like to save and click on yes:

<img class="shadowed" src="howto_save.png" width="297" >

** If the news was saved a toast will appear saying that you can find the news on Saved; in the meanwhile its HTML page is downloading: if the download will be completed, another toast will apper saying:

<img class="shadowed" src="howto_savedHTMLPage.jpg" width="297" >

** ok, now if you remove the connection to internet, you will be able to see something like this :

<img class="shadowed" src="howto_savedHTMLPageOffline.jpg" width="297" >



