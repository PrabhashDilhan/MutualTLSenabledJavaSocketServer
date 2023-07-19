# MutualTLSenabledJavaSocketServer

Create the keystore that includes the private key by executing the following command:

```keytool -genkey -alias newcert -keyalg RSA -keysize 2048 -keystore newkeystore.jks -dname "CN=<testdomain.org>, OU=Home,O=Home,L=SL,S=WS,C=LK" -storepass mypassword -keypass mypassword ```

Then you can export the server certificate to client-side trust store and client-side certificate to the above key store(we have used the same key store as the trust store) as a trusted cert.

