
package com.san.thumbtrack;

import java.util.HashMap;
import java.util.Map;

/**
 * Storage class for db
 * 
 * @author <a href="santhosh.g@leantaas.com">Santhosh Gandhe</a>
 * @version $Revision: 1.0 $, $Date: Oct 18, 2015
 */
public class DbStore {

    /**
     * This map always holds the state of the keys per Tx 
     * Comment for <code>keyValueMap</code>
     */
    
    private Map<Integer, Map<String, Integer>> transactionStack = new HashMap<Integer,Map<String, Integer>>();
    
    private Map<Integer, Integer> perValueVarCnt = new HashMap<Integer, Integer>();
    
    private Integer txCnt=0;
    
    public void setKeyValue(String key, Integer value){
        if(null==value){
            //request may be to unset a key
            Integer currentValue=getGivenKeyValue(key);
            decreaseValueCount(currentValue);
        }
        else if(perValueVarCnt.containsKey(value)) {
            increaseValueCount(value);
        } else {
            perValueVarCnt.put(value, 1);
        }
        transactionStack.get(txCnt).put(key, value);
    }
    
   public Integer getGivenKeyValue(String key){
       //This may return null, if the key doesn't exist in the store
       for(int i=txCnt;i>0;i--) {
           if(transactionStack.get(i).containsKey(key)){
               return transactionStack.get(i).get(key);
           }
       }
       return null;
   }
   
   public void unset(String key){
       setKeyValue(key, null);
   }
   
   public void beginTx(){
       txCnt++;
       transactionStack.put(txCnt,new HashMap<String,Integer>());
   }
   
   public String commitTx(){
       if(transactionStack.isEmpty()){
           return "NO TRANSACTION";
       }
       if(txCnt>1) {
           Map<String, Integer> currentState = transactionStack.get(txCnt);
           Map<String, Integer> previousState = transactionStack.get(txCnt-1);
           previousState.putAll(currentState);
       }
       transactionStack.remove(txCnt);
       txCnt--;
       return "";
   }
   
   public String rollbackTx() {
       if(transactionStack.isEmpty()) {
           return "NO TRANSACTION";
       }
       //update per value count map
       Map<String, Integer> curTxStateToRollback = transactionStack.get(txCnt);
       for(Map.Entry<String, Integer> keyValEntry:curTxStateToRollback.entrySet()) {
           decreaseValueCount(keyValEntry.getValue());
       }
       //Reset the latest state to the latest backup state from the stack
       transactionStack.remove(txCnt);
       txCnt--;
       return "";
   }
   
   public Integer numEqualTo(Integer value) {
       return perValueVarCnt.get(value);
   }
   
   public void decreaseValueCount(Integer value) {
       Integer currVarCnt=perValueVarCnt.get(value);
       if(null!=currVarCnt) {
           perValueVarCnt.put(value, --currVarCnt);
       }
   }
   
   public void increaseValueCount(Integer value) {
       Integer currVarCnt=perValueVarCnt.get(value);
       perValueVarCnt.put(value, ++currVarCnt);
   }
}

