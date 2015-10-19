
package com.san.thumbtrack;

public enum Command {
    BEGIN(0), 
    COMMIT(0), 
    GET(1), 
    ROLLBACK(0), 
    SET(2), 
    UNSET(1),
    END(0),
    NUMEQUALTO(1);
    
    private int argsCnt=0;
    
    private Command(int argsCnt) {
        this.argsCnt=argsCnt;
    }
    
    public int getArgsCount(){
        return argsCnt;
    }
}
