# Overview

What we're trying to accomplish here is the ability for one tomcat server to host different projects. 
There are a couple of different strategies that we can use to try to make this happen.  While having an Apache
server in front works without any issues by itself.  The problems we face is when we try to fold in authentication
then the process breaks down with regards to context and trying to do the forwarding.  The final solution detailed
here seems to work the best.  The biggest benefit is having the Apache webserver handle the SSL requests as noted below.
The only downfall to this is having to run one tomcat server per installation because 
<br>
<br>


---

| URL | Details |
| --- | ------- |
| http://&lt;HOSTNAME&gt;:8080/myapp/ | In a no configuration option, tomcat it is by default setup to run on port 8080. This is the url if you deploy your application with the context <i>myapp</i> | 
| http://&lt;HOSTNAME&gt;/myapp/ | The next config would be to set the tomcat server from port 8080 to port 80.  Again if the context is <i>myapp</i>.  Follow the "Change Tomcat Port" section below. |
| http://&lt;HOSTNAME&gt; | This is the intended output URL.  There are two(ish) ways to achive this:  <br>&nbsp;&nbsp;1. Configure the tomcat server to do virtual hosting.  <br>&nbsp;&nbsp;2. The other is to place a web server in front of the tomcat server and proxy all requests to the back end. This is where the 'ish' comes into play.  You can either use a nginx server or Apache server - basically anything you want to use in front of Tomcat. |

---

# Spring Boot Setup

In the build section of the pom.xml you need to set the build name to ROOT - this is also in
conjuntion with making sure the build type is of type 'war'.  This makes the context path be '/'
and when the server deploys ROOT.war it deploys it to the '/' context.

```
<build>
    <finalName>ROOT</finalName>
</build>
```

# Tomcat Port Change

You'll need to edit the file ./conf/server.xml and change the following line from the before to the after
####Before
`<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />`
####After
`<Connector port="8081" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />`


# SSL setup
>certbot-auto certonly -d &lt;HOSTNAME&gt;

This will prompt you if you want to install via:
* apache plugin
* temporary webserver
* webroot (file place in your directory structure that is accessable)

It then installs the details in `/etc/letsencrypt/live/<HOSTNAME>`


# Apache Proxy Setup

We do this by using an apache web server in front of tomcat
so that we can use SSL of the Apache webserver instead of having to configure the .jks
files in tomcat.  This makes it simple to get a server started and up and running with SSL
in moments with LetsEncrypt.

---

### Apache Config
```
<VirtualHost *:80>
    ServerName        <HOSTNAME>
    ErrorLog          "/var/log/httpd/<HOSTNAME>/www-error_log"
    CustomLog         "/var/log/httpd/<HOSTNAME>/www-access_log" combined

    ProxyRequests     Off
    ProxyPreserveHost On
    ProxyPass         /  http://localhost:8081/
    ProxyPassReverse  /  http://localhost:8081/
</VirtualHost>

<VirtualHost *:443>
    ServerName        <HOSTNAME>
    ErrorLog          "/var/log/httpd/<HOSTNAME>/ssl-www-error_log"
    CustomLog         "/var/log/httpd/<HOSTNAME>/ssl-www-access_log" combined

    ProxyRequests     Off
    ProxyPreserveHost On
    ProxyPass         /  http://localhost:8081/
    ProxyPassReverse  /  http://localhost:8081/

    SSLEngine on
    SSLCertificateFile      /etc/letsencrypt/live/<HOSTNAME>/cert.pem
    SSLCertificateKeyFile   /etc/letsencrypt/live/<HOSTNAME>/privkey.pem
    SSLCertificateChainFile /etc/letsencrypt/live/<HOSTNAME>/chain.pem
</VirtualHost>
```