# Helping Doctors

## Introduction

Helping Doctors is a platform for both doctors as well as patients.

It is mainly targeted for people in remote areas in India who have to travel long distances to consult doctors.

For patients - A simple user friendly android application

For doctors - A web portal

Below are the doctor/1000 patient ratios: 

### • Germany 4.19 (2015)

### • USA 2.57 (2014)

### • India 0.76 (2016)

As you can see in India, the ratio is particularly low.

In today's world where everyone is using a smartphone and where there is an app for anything and everything, we want to provide a simple solution for this problem with the help of Agora RTC.

We have designed and developed a mobile app for the users (patients), using which they are only a click away from consulting a doctor of their choice.

The users can consult with the doctor through an audio or video call.

## Mobile Application (Android)

The android apk is present [here](/Mobile%20App/APK/). I have used my App ID in this APK.

If you want to build the application yourself follow the below steps:

1. Create a developer account at [Agora.io](https://agora.io) and obtain an App ID.

2. Update "app/src/main/resources/values/strings.xml" with your App ID: 

 ```xml
 <string name="agora_app_id">YOUR_APP_ID</string>"
 ```
3. Build the app

## AudioVedioApp (Web Application)

This application is present [here](/AudioVedioApp/).

If you want to build the application yourself follow the below steps:

1. Create a developer account at [Agora.io](https://agora.io) and obtain an App ID.

2. Update "\AudioVedioApp\static\agora.config.js" with your App ID: 

 ```xml
 const AGORA_APP_ID = 'YOUR_APP_ID'
 ```
3. Open the project in visual studi code

4. Execute "npm install" command  in the terminal- this will download the all the neccesary  node modules.

5. Execute "npm start" command in the terminal - this will start the development server to make Audio and Vedio calls.

## WebApp (Web Application)

This application is present [here](/WebApp/).

If you want to build the application yourself follow the below steps:

1. Open the project in visual studio code

2. update "src\static\property.js" with ip and port where AudioVedioApp is running. 

 ```xml
 const ip = "localhost";
 const port = "8080";
 ```

3. Execute "npm install" command  in the terminal- this will download the all the neccessary node modules.

4. Execute "npm start" command in the terminal - this will start the development server where doctors can create and use their account.


## Future Scope

• A mobile application for doctors

• Group audio and video calls. For example - If my parents are staying in another state and I want to consult with a doctor along with     them

• A chat service so that patients have an option to chat with the doctors as well

• Multiple language support in app, so that everyone is able to use it without any hassle
