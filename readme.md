You can build with:

`./gradlew clean build`

To start the application you can run. 
`jasypt.encryptor.password` is set for decrypting the password, probably it wouldn't be pushed to git in a real project:

`./gradlew bootRun --args='--jasypt.encryptor.password=secret'`

Notes:

* I couldn't find any reason to use Streams but, I have long years of experience with java 8.
* This was just  basic demonstration for a simple web application
* More tests can be added, for example expiring token, mocking the report apis
* I mostly get error from the sandbox endpoint so, I didn't rely on the data from services. Probably Data objects I created are not totally correct.