/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

/**
 *
 * @author HP
 */
public class Mode {
    DESData data;
    int groupNum;
    char[][] input;
    char[][] output;
    
    public Mode(DESData data,Key key,String mode){
        this.data = data;
        groupNum = data.getNumOfGroup();
        input = data.getTextIn();
        output = new char[groupNum][64];
    }
    
    public char[] process(char[] data,Key key,String mode){
        char[] result = new char[64];
        if(mode.equals("Encryption")){
            result = new Encryption(data,key).getCiphertext();
        }
        else if(mode.equals("Decryption")){
            result = new Decryption(data,key).getPlaintext();
        }
        return result;
    }

}
