## Description
This code is android gps traccer version based on traccar, addon feature is registration
and add auto number id generated from server.

This application is alpha, prototype version, maybe still have some buggy codes.

tested under gennymotion in localpc hosted and xampp windows environtment.


## howto create git repository (in local pc)

git init .

git add README.md

git remote add origin https://github.com/iferianto/movingBBandroid.git

git commit -m "first stage, registration to server (emulated on local gennymotion)"

git push -u origin master

##notes:
need to edit src/Konfigurasi.java

public static final String REGISTRATION_URL="http://10.0.3.2/banner/backend/index.php?r=regandroid&key=secret";

it must be set to the ip of gps server

##android version willbe do something like this:

1. registration to server, user will get autoid of application

2. application will act as gps tracker,send position to server

3. there is two button inside this application, start and stop button

4. start will start tracking and send gps on move (periodic time interval)

5. stop will stop tracking and query to server via json webservice

6. calculation is done in server (database trigger level on insert)

7. calculation process in server is just summaries credit point of users

8. result is displayed in application

