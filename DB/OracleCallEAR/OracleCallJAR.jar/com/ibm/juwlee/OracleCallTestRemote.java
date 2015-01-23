package com.ibm.juwlee;
import javax.ejb.Remote;

@Remote
public interface OracleCallTestRemote {

	public void insertDB();
}
