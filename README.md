server.xml

<Connector executor="tomcatThreadPool"
               port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
                maxHttpHeaderSize="165536"
                maxPostSize="31457280"
               redirectPort="8443" />
               
/etc/apache2/apache2.conf

ProxyRequests Off
<Proxy *>
        Order deny,allow
        Deny from all
        Allow from all
</Proxy>
ProxyPass       /bioflux ajp://localhost:8009/bioflux
ProxyPassReverse    /bioflux ajp://localhost:8009/bioflux

ProxyPass       /xomeq ajp://localhost:8019/xomeq
ProxyPassReverse    /xomeq ajp://localhost:8019/xomeq

ProxyPassReverse    /patho ajp://localhost:8019/patho
ProxyPassReverse    /patho ajp://localhost:8019/patho



<filesMatch "\.(html|htm|js|css)$">
  FileETag None
  <ifModule mod_headers.c>
     Header unset ETag
     Header set Cache-Control "max-age=0, no-cache, no-store, must-revalidate"
     Header set Pragma "no-cache"
     Header set Expires "Wed, 11 Jan 1984 05:00:00 GMT"
  </ifModule>
</filesMatch>

AddType application/octet-stream .bam









https://stackoverflow.com/questions/1730158/how-to-set-the-ajp-packet-size-in-tomcat

<!-- Define a Coyote/JK2 AJP 1.3 Connector on port 8009 -->
<Connector port="8009"
enableLookups="false" redirectPort="8443" debug="0"
protocol="AJP/1.3" />
to

<!-- Define a Coyote/JK2 AJP 1.3 Connector on port 8009 -->
<Connector port="8009"
enableLookups="false" redirectPort="8443" debug="0" packetSize=21000
protocol="AJP/1.3" />


Open /etc/apache2/apache2.conf
and insert under AccessFileName .htaccess:

LimitRequestLine 1000000
LimitRequestFieldSize 1000000

mvn clean package -DskipTests -Dbasepath=patho -Dfinalname=patho -Ddefault.lang=en -Dmongo.database=tdr -Dmysql.pass=xxx

docker run -it --rm -v $PWD/db:/data/db -p 27017:27017 mongo:4.4

  