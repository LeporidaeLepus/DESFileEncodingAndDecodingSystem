/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

import java.util.Arrays;

/**
 *
 * @author HP
 */
public class Decryption extends Crypt{
    char[] ciphertext = new char[64];
    char[] plaintext = new char[64];
    
    public Decryption(char[] cipherData,Key key){
        super(cipherData,key);
        this.ciphertext = cipherData;
        
        //初始置换IP
        text = displaceIP(ciphertext);
        //将置换后数组分为左右两个数组
        left = Arrays.copyOfRange(text, 0, 32);      //L16
        right = Arrays.copyOfRange(text, 32, 64);    //R16
        //第i轮置换，将Li，Ri置换为Li-1，Ri-1。i∈[2,16]
        for(int i = 16;i>=2;i--){
           round(i-1);      //i-1为第i轮所需秘钥在子秘钥组subKeys中的标号
        }
        //i = 1
        left = XOR(left,functionF(1-1,right));  //L16 = L15 XOR F(R15,K16),R16 = R15
        //将左右两边数组合为一个
        System.arraycopy(left, 0, text, 0, 32);
        System.arraycopy(right, 0, text, 32, 32);
        //逆初始置换IPR
        plaintext = displaceIPR(text);
        
    }
    
    public char[] getPlaintext(){
        return plaintext;
    }
}
