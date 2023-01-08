/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 *
 * @author HP
 */
public class Key extends Transform{
    String key;         //输入的String类型的秘钥
    byte[] keyBy = new byte[8];    //二进制秘钥
    String keyStr="";      //String格式的二进制秘钥
    char[] keyChar = new char[64];
    char[] key56 = new char[56];    //有效的56位秘钥
    private char[][] subKey = new char[16][48];     //16个48位子秘钥
    //置换选择PC_1
    final int[] PC_1={
        57,49,41,33,25,17,9,
        1,58,50,42,34,26,18,
        10,2,59,51,43,35,27,
        19,11,3,60,52,44,36,
        63,55,47,39,31,23,15,
        7,62,54,46,38,30,22,
        14,6,61,53,45,37,29,
        21,13,5,28,20,12,4
    };
    //每轮位移数
   final int[] MOV={
       1,1,2,2,2,2,2,2,
       1,2,2,2,2,2,2,1
   };
   //置换选择PC_2
   final int[] PC_2={
       14,17,11,24,1,5,
       3,28,15,6,21,10,
       23,19,12,4,26,8,
       16,7,27,20,13,2,
       41,52,31,37,47,55,
       30,40,51,45,33,48,
       44,49,39,56,34,53,
       46,42,50,36,29,32
   };
   
   public Key(String key){
       this.key = key;
       try{
           //将字符串转换为二进制形式
           keyBy = key.getBytes("UTF-8");         
       }
       catch(UnsupportedEncodingException e){
           
       }
       for(int i=0;i<8;i++){
           keyStr = keyStr+byteToBit(keyBy[i]);
       }
       keyChar = keyStr.toCharArray();
       
       generateKey();
   }
   
   //生成秘钥
   public void generateKey(){
       getKeyAfterPC1();
       getSubKeys();
   }
   
   //通过置换选择PC_1得到有效的56位秘钥
   public void getKeyAfterPC1(){
       int index;
       
       for(int i = 0;i<56;i++){
           //获取当前位置PC_1中所示原数组中对应元素
           index = PC_1[i];
           key56[i] = keyChar[index-1];
       }
   }
   
   //生成子秘钥
   public void setSubKeys(){
       int num;     //循环左移位数
       char[] left = new char[28];
       char[] right = new char[28];
       char[] temp = new char[56];
       
       //将有效数组分为两个28位数组
       //length of Arrays.copyOfArange = to - from
       left = Arrays.copyOfRange(key56, 0, 28);
       right = Arrays.copyOfRange(key56, 28, 56);
       
      for(int i = 0;i<16;i++){
          //获取当前轮循环左移位数
          num = MOV[i];
          //循环左移
          left = leftShift(num,left);
          right = leftShift(num,right);
          
          //将左右两边数组合为一个
          System.arraycopy(left, 0, temp, 0, 28);
          System.arraycopy(right, 0, temp, 28, 28);
          
          subKey[i] = getKeyAfterPC2(temp);
       }  
   }
   
   //循环左移，k为左移位数,list为循环左移的数组
   public char[] leftShift(int k,char[] list){
       char[] temp = new char[28];
       
       for(int i = 0;i<28;i++){
           //将原数组中元组的位置减去左移位数再取28的模则为循环左移后的位置
           temp[i] = list[Math.floorMod(i-k, 28)];     //负数时取模与取余的区别
       }
       return temp;
   }
   
   //置换选择PC_2
   public char[] getKeyAfterPC2(char[] list){
       int index;
       char[] temp = new char[48];
       
       for(int i = 0;i<48;i++){
           //获取当前位置PC_2中所示原数组中对应元素
           index = PC_2[i];
           temp[i] = list[index-1];
       }
       
       return temp;
   }
   
   public char[][] getSubKeys(){
       return subKey;
   }
}
