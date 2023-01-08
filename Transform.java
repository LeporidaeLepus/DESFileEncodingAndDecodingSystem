/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 *
 * @author HP
 */
public class Transform {
    public Transform(){
        
    }
    
    /**
 * Byte转Bit
 */
    public static String byteToBit(byte b) {
        return "" +(byte)((b >> 7) & 0x1) + 
        (byte)((b >> 6) & 0x1) + 
        (byte)((b >> 5) & 0x1) + 
        (byte)((b >> 4) & 0x1) + 
        (byte)((b >> 3) & 0x1) + 
        (byte)((b >> 2) & 0x1) + 
        (byte)((b >> 1) & 0x1) + 
        (byte)((b >> 0) & 0x1);
    }
    
    //将二进制字符串转换成int数组
    public static int[] BinstrToIntArray(String binStr) {       
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];   
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }


    //将二进制转换成字符
    public static char BitToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;     //<<i=*2^i
        }   
        
        return (char)sum;
   }
    
    //将int转换成指定长度的string形式的二进制
    //num为要转换的int，digits为要保留的位数
    public static String intToBinaryStr(int num, int digits) {
        //1左移digits位后右边补上的digits个0与num进行或运算，与全0或运算得到的是num原值
        int value = 1 << digits | num;      
        String bs = Integer.toBinaryString(value);  //bs = 1+(B)num
        return  bs.substring(1);     
    }

    //Base64 编码
    public String Base64En(String strToEn) throws UnsupportedEncodingException {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = strToEn.getBytes("UTF-8");
        
        return encoder.encodeToString(textByte);
    }
    
    //Base64 解码
    public String Base64De(String strToDe) throws UnsupportedEncodingException{
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(strToDe),"UTF-8");
    }
}
