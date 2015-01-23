package com.ibm.juwlee;
import javax.ejb.Remote;

@Remote
public interface DB2CallTestRemote {

    public void insertDB();
}
