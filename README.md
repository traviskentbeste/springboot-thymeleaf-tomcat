
# Introduction

What we're trying to accomplish here is the abiltiy for one tomcat server to host 
many different projects.  We do this by using an apache web server in front of tomcat
so that it can strip off the context. We want to have the hostname respond
without having to worry about the context.  So requests to http://example.com that
have a context of ***'/example'*** are redirected internally.  This makes it transparent
to the end user that there is a context involved.  There is an added benefit that we can 
have the SSL handled by apache (which is easier to implement and more standard) than with
the .jks files needed for tomcat configuration.

---

# SSL setup
>certbot-auto certonly -d &lt;HOSTNAME&gt;

This will prompt you if you want to install via:
 * apache plugin
 * temporary webserver
 * webroot (file place in your directory structure that is accessable)

It then installs the details in `/etc/letsencrypt/live/<HOSTNAME>`

# Apache Config
```
<VirtualHost *:80>
        ProxyPreserveHost On
        ProxyRequests     Off
        ServerName        <HOSTNAME>
        ProxyPass         / http://localhost:5555/tomcat/
        ProxyPassReverse  / http://localhost:5555/

        ErrorLog  "/var/log/httpd/<HOSTNAME>/www-error_log"
        CustomLog "/var/log/httpd/<HOSTNAME>/www-access_log" combined
</VirtualHost>

<VirtualHost *:443>
        ProxyPreserveHost On
        ProxyRequests     Off
        ServerName        <HOSTNAME>
        ProxyPass         / http://localhost:5555/tomcat/
        ProxyPassReverse  / http://localhost:5555/

        # turn on SSL
        SSLEngine on
        SSLCertificateFile      /etc/letsencrypt/live/<HOSTNAME>/cert.pem
        SSLCertificateKeyFile   /etc/letsencrypt/live/<HOSTNAME>/privkey.pem
        SSLCertificateChainFile /etc/letsencrypt/live/<HOSTNAME>/chain.pem

        ErrorLog  "/var/log/httpd/<HOSTNAME>/ssl-www-error_log"
        CustomLog "/var/log/httpd/<HOSTNAME>/ssl-www-access_log" combined
</VirtualHost>
```

---

# Tomcat config
conf/server.xml
>Notice the port number needs to match above, in this case ***5555***
```
<Connector
        port="5555"
        proxyName="<HOSTNAME>"
        proxyPort="80"
/>
```