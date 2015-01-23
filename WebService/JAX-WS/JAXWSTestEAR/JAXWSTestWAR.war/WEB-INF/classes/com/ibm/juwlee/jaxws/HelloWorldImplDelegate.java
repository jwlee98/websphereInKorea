package com.ibm.juwlee.jaxws;



@javax.jws.WebService (targetNamespace="http://jaxws.juwlee.ibm.com/", serviceName="HelloWorldImplService", portName="HelloWorldImplPort")
public class HelloWorldImplDelegate{

    com.ibm.juwlee.jaxws.HelloWorldImpl _helloWorldImpl = null;

    public String getHelloWorldAsString(String name) {
        return _helloWorldImpl.getHelloWorldAsString(name);
    }

    public HelloWorldImplDelegate() {
        _helloWorldImpl = new com.ibm.juwlee.jaxws.HelloWorldImpl(); }

}