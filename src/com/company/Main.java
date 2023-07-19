package com.company;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

public class Main {
    static PrintStream out;
    static SSLSocket client;
    static ServerSocket ss;
    static BufferedReader inn;
    static int min = 0;
    static int max = 10;

    public static void main(String[] args) {
        try {
            server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void server() throws IOException {
        try {
            System.setProperty("javax.net.ssl.keyStore", "/Users/apple/Documents/newkeystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "mypassword");
            // Load server keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("/Users/apple/Documents/newkeystore.jks"), "mypassword".toCharArray());

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("/Users/apple/Documents/newkeystore.jks"), "mypassword".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "mypassword".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);


            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // Set the SSL parameters on the SSL context
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            ServerSocketFactory socketFactory = sslContext.getServerSocketFactory();
            ServerSocket serverSocket = socketFactory.createServerSocket(7100);

            for (;;) {

                client = (SSLSocket) serverSocket.accept();
                SSLParameters sslParams = client.getSSLParameters();
                sslParams.setNeedClientAuth(true);
                client.setSSLParameters(sslParams);

                inn = new BufferedReader(new InputStreamReader(client.getInputStream()));


                char[] buf = new char[10];
                StringBuilder outt = new StringBuilder();
                while (true) {
                    try{
                        int read = inn.read(buf);
                        outt.append(buf, 0, read);
                        if (read < 10)
                            break;
                    }

                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                out = new PrintStream(client.getOutputStream());
                int b=1;
                // Start sending our reply, using the HTTP 1.1 protocol
                String t = "HTTP/1.1 200 Ok\r\n";
                String t1 = "Content-Type: application/json\r\n";
                String t3 = "Connection: Keep-alive\r\n";
                String t2 = "Content-Length: 10\r\n";
                out.write(t.getBytes()); // Version & status code
                out.write(t1.getBytes());
                out.write(t3.getBytes());
                out.write(t2.getBytes());; // The type of data
                out.print("\r\n"); // End of headers


                String payload = "{\"sd\":\"s\"}";

                out.write(payload.getBytes());

                out.print("\r\n");
                out.close(); // Flush and close the output stream
                inn.close(); // Close the input stream
                client.close(); // Close the socket itself
            }
        }
        // If anything goes wrong, print an error message
        catch (Exception e) {

                e.printStackTrace();

        }
    }
}

