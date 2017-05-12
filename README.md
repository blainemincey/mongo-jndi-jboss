# mongo-jndi-jboss
Example of using MongoDB as a JNDI lookup with JBoss/Wildfly

Steps to run

1.  Install mongo-java-driver as a module
module.xml

<module xmlns="urn:jboss:module:1.3" name="org.mongodb">
   <resources>
       <resource-root path="mongo-java-driver-3.4.2.jar"/>
   </resources>
   <dependencies>
       <module name="javax.api"/>
       <module name="javax.transaction.api"/>
       <module name="javax.servlet.api" optional="true"/>
   </dependencies>
</module>

2.  Add Naming binding to standalone.xml
<subsystem xmlns="urn:jboss:domain:naming:2.0">
            <bindings>
                <object-factory name="java:global/MyMongoClient" module="org.mongodb" class="com.mongodb.client.jndi.MongoClientFactory">
                    <environment>
                        <property name="connectionString" value="mongodb://localhost:27017"/>
                    </environment>
                </object-factory>
            </bindings>
            <remote-naming/>
 </subsystem>

