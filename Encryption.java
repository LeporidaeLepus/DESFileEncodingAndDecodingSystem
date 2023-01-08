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
public class Encryption extends Crypt{
    char[] plaintext = new char[64];
    char[] ciphertext = new char[64];
    
    public Encryption(char[] plainData,Key key){
        super(plainData,key);
        this.plaintext = plainData;
        
        //初始置换IP
        text = displaceIP(plaintext);
        //将置换后数组分为左右两个数组
        left = Arrays.copyOfRange(text, 0, 32);      //L0
        right = Arrays.copyOfRange(text, 32, 64);    //R0
        //第i轮置换，将Li-1，Ri-1置换为Li，Ri。i∈[1,15]
        for(int i = 1;i<=15;i++){
           round(i-1);      //i-1为第i轮所需秘钥在子秘钥组subKeys中的标号
        }
        //i = 16
        left = XOR(left,functionF(16-1,right));  //L16 = L15 XOR F(R15,K16),R16 = R15
        //将左右两边数组合为一个
        System.arraycopy(left, 0, text, 0, 32);
        System.arraycopy(right, 0, text, 32, 32);
        //逆初始置换IPR
        ciphertext = displaceIPR(text);
        
    }
    
    public char[] getCiphertext(){
        return ciphertext;
    }
    
}
