**You can build with:** 

`./gradlew clean build`

**To start the application you can run. 
`jasypt.encryptor.password` is set for decrypting the password, probably it wouldn't be pushed to git in a real project:**

`./gradlew bootRun --args='--jasypt.encryptor.password=secret'`
