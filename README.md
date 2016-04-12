git init .

git add README.md

git remote add origin https://github.com/iferianto/movingBBandroid.git

git commit -m "first stage, registration to server (emulated on local gennymotion)"

git push -u origin master

##notes:
need to edit src/Konfigurasi.java

public static final String REGISTRATION_URL="http://10.0.3.2/banner/backend/index.php?r=regandroid&key=secret";

it must be set to the ip of gps server