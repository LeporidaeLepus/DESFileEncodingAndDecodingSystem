/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 *
 * @author HP
 */
public class ModeCBC extends Mode{
    char[] temp = new char[64];     
//    char[] IV = new char[64];       //初始化向量
    
    //分组链接模式
    public ModeCBC(DESData data,Key key,String mode) throws UnsupportedEncodingException{
        super(data,key,mode);
        
        //CBC加密流程
        if(mode.equals("Encryption")){
            temp = input[0];        //temp为链接后被加密部分
            output[0]=process(temp,key,mode);
            for(int i=1;i<groupNum;i++){
                temp = XOR(input[i],output[i-1]);   //第i-1组密文与第i组明文异或  
                output[i]=process(temp,key,mode);   //异或后结果加密得到密文

            }
        }else if(mode.equals("Decryption")){
            output[0] = process(input[0],key,mode);     //第一组的明文为密文直接解密
            for(int i = 1;i<groupNum;i++){
                temp = process(input[i],key,mode);      //temp为密文解密所得
                output[i] = XOR(temp,input[i-1]);       //第i-1组密文与第i组解密后内容异或
            }
        }
        
        data.setFileOut(output);    //在ModeECB中进行文件生成是为了确保output是由data生成的
    }
    
    
    //异或计算
    protected char[] XOR(char[] t1,char[] t2){
        char[] temp = new char[64];
        
        if((t1.length == 64) && (t2.length == 64)){
            for(int i=0;i<64;i++){
                if(t1[i]!= t2[i]){
                    temp[i]='1';
                }
                else{
                    temp[i] = '0';
                }
            }
        }else System.out.println("错误的XOR长度！");
        
        return temp;
    }
}
