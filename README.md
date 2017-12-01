# Android client

## google connect

You need to generate file `app/google-services.json` (https://developers.google.com/identity/sign-in/android/start-integrating)
1. Go to [Add Google Service page](https://developers.google.com/mobile/add?platform=android&cntapi=signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fsign-in%3Fconfigured%3Dtrue&cntlbl=Continue%20Adding%20Sign-In)
2. Type app name `sea-race`
3. Type package name `fr.sea_race.client.searace`
4. Click on `Choose and configure services`
5. Generate your SHA1
    * debug: `keytool -J-Duser.language=en -exportcert -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore` pass: `android`
    * prod: `keytool -J-Duser.language=en -exportcert -list -v -alias landru -keystore yolo-this-is-my-private-key.jks`
6. Click on `Generate configuration file`
7. Click on `Download google-services.json`