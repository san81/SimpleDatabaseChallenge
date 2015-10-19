
package com.san.thumbtrack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StartDbCmd {

   
    public static void main(String args[]) throws FileNotFoundException{
        DbStore dbStore=new DbStore();
        Scanner scanner;
        if(args.length>0){
            File f = new File(args[0]);
            scanner = new Scanner(f);
            scanner.useDelimiter("\n");
            System.out.println(args[0]);
            while(scanner.hasNext()){
                String cmd=scanner.next();
                execCmd(cmd, dbStore);
            }
            scanner.close();
        }else {
            scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
            while(true){
                String cmd=scanner.next();
                execCmd(cmd, dbStore);
            }
        }
        
    }
    
    public static void execCmd(String cmdStr, DbStore dbStore) {
        String keyArg=null;
        Integer valueArg=null;
        String[] cmdArgs = cmdStr.split(" ");
        Command cmd=null;
        try{
            cmd = Command.valueOf(cmdArgs[0]);
            if(cmdArgs.length!=cmd.getArgsCount()+1) {
                System.out.println("Not enough arguments");
                return;
            }
        }catch(IllegalArgumentException iae) {
            System.out.println("Invalid command "+cmdArgs[0]);
            return;
        }
        switch(cmd){
            case BEGIN:
                dbStore.beginTx();
                break;
            case COMMIT:
                String cmRes=dbStore.commitTx();
                if(!"".equals(cmRes)){
                    System.out.println(cmRes);
                }
                break;
            case END:
                System.exit(0);
                break;
            case GET:
                keyArg = cmdArgs[1];
                System.out.println(dbStore.getGivenKeyValue(keyArg));
                break;
            case UNSET:
                keyArg = cmdArgs[1];
                dbStore.unset(keyArg);
                break;    
            case ROLLBACK:
                String res=dbStore.rollbackTx();
                if(!"".equals(res)){
                    System.out.println(res);
                }
                break;
            case SET:
                keyArg = cmdArgs[1];
               try {
                    valueArg = Integer.parseInt(cmdArgs[2].trim());
                }catch(InputMismatchException ime) {
                    System.out.println("Invalid value to set");
                    break;
                }
                dbStore.setKeyValue(keyArg, valueArg);
                break;
            case NUMEQUALTO:
                try {
                    valueArg = Integer.parseInt(cmdArgs[1].trim());
                }catch(InputMismatchException ime) {
                    System.out.println("Invalid value to set");
                    break;
                }
                System.out.println(dbStore.numEqualTo(valueArg));
                break;
            default:
                break;
            
        }
    }
    
   
}
