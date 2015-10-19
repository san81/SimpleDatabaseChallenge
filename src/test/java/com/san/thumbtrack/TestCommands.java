
package com.san.thumbtrack;

import org.junit.Assert;
import junit.framework.TestCase;

public class TestCommands extends TestCase {

    public void testSetCommand(){
        DbStore dbStore=new DbStore();
        dbStore.beginTx();
        dbStore.setKeyValue("a", 10);
        Integer valToCheck=dbStore.getGivenKeyValue("a");
        Assert.assertTrue("Set command failed", valToCheck==10);
        dbStore.commitTx();
    }
    
    public void testNumEqualsToCommand() {
        DbStore dbStore=new DbStore();
        dbStore.beginTx();
        dbStore.setKeyValue("a", 10);
        dbStore.setKeyValue("b", 10);
        dbStore.setKeyValue("c", 20);
        Integer valToCheck=dbStore.numEqualTo(10);
        Assert.assertTrue("NUMEQUALSTO command failed", valToCheck==2);
        dbStore.commitTx();
    }
    
    public void testNestedTx(){
        DbStore dbStore=new DbStore();
        dbStore.beginTx();
        dbStore.setKeyValue("a", 10);
        dbStore.beginTx();
        dbStore.setKeyValue("b", 10);
        dbStore.setKeyValue("c", 20);
        dbStore.commitTx();
        Integer valToCheck=dbStore.numEqualTo(10);
        Assert.assertTrue("Nested Transaction command failed", valToCheck==2);
        dbStore.commitTx();
    }
    
    public void testRollbackTx(){
        DbStore dbStore=new DbStore();
        dbStore.beginTx();
        dbStore.setKeyValue("a", 10);
        dbStore.beginTx();
        dbStore.setKeyValue("b", 10);
        dbStore.setKeyValue("c", 20);
        dbStore.rollbackTx();
        Integer valToCheck=dbStore.numEqualTo(10);
        Assert.assertTrue("Rollback command failed", valToCheck==1);
        dbStore.commitTx();
    }
    
    public void testUnsetCmd(){
        DbStore dbStore=new DbStore();
        dbStore.beginTx();
        dbStore.setKeyValue("a", 10);
        dbStore.beginTx();
        dbStore.unset("a");
        Assert.assertTrue("Unset command failed", null==dbStore.getGivenKeyValue("a"));
        dbStore.rollbackTx();
        Assert.assertTrue("Unset command failed", null!=dbStore.getGivenKeyValue("a"));
        dbStore.commitTx();
    }
}
