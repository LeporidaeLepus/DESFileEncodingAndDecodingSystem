/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;

/**
 *
 * @author HP
 */
public class DESData extends Transform{
    String filenameIn;
    private String filenameOut;
    String textIn = ""; //输入文件中的内容
    String textInBase64 = "";   //textIn进行Base64编码/解码处理
    char[] textInBase64Char;    //textInBase64转为char[]
    private final int numOfGroup;     //输入的文字一共有多少组64位,明文和密文长度相同
    private final char[][] textInByChar; // 存储分组后的char形式二进制
    char[][] textOutByChar;       //存储输出文本的char形式二进制
    String textOut = "";        //输文件中的内容
    String textOutBase64 = "";  //textOut进行Base64编码/解码处理
    String action;      //当前选择的加密/解密动作

    
    
    
    public DESData(String name,String action){
        this.filenameIn = name;
        this.action = action;
        
        //读取输入的文件
        try {
            getFileIn();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DESData.class.getName()).log(Level.SEVERE, null, ex);
        }
        //若进行加密操作，则为明文进行Base64编码
        if(action.equals("Encryption")){
            try {
                textInBase64 = Base64En(textIn);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DESData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//若进行解密操作，则为密文进行base64解码
        else if(action.equals("Decryption")){
            try {
                textInBase64 = Base64De(textIn);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DESData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //补全
        complement();
        
        //8*8=64，每8个byte构成一组64位bits
        numOfGroup = textInBase64Char.length/8;     //计算分组数
        textInByChar = new char[numOfGroup][64];      //初始化textInByChar
        textOutByChar = new char[numOfGroup][64];     //初始化textOutByChar
        
        //将输入的数据以64位为一组进行分组
        divideIntoGroup();
        
        setFilenameOut();
    }
    
    //读取输入的文件存入textIn
    private void getFileIn() throws FileNotFoundException{
        String s;
        File fileIn = new File(filenameIn);
        
        FileReader fileInReader = new FileReader(fileIn);
        BufferedReader in = new BufferedReader(fileInReader);
            
        try{
            while((s = in.readLine())!=null){
                //每在文本中读取一行则将读取内容添加至textIn
                textIn = textIn+s;
            }
            
            in.close();
            fileInReader.close();
        }
        catch(IOException e){
            
        }
    }
    

    
    //末尾不足64位则以“^”补全64位
    private void complement() {
        textInBase64Char = textInBase64.toCharArray();

        //判断末尾是否需要补全，若有空缺用“^”补全至无空缺
        //Base64编码后byte长度为⌈n/3⌉*4，n为原字符串长度
        while((textInBase64Char.length%8)==4){      //8*8=64
                textInBase64 = textInBase64+"Xg==";     //Xg==为^的base64编码
                textInBase64Char = textInBase64.toCharArray();
            }

       
    }
    
    //将输入的内容分为若干组64位二进制
    private void divideIntoGroup(){
        int k = 0;
        
        for(int i=0;i<numOfGroup;i++){
            String temp = "";
            String t = "";
            for(int j=0;j<8;j++){           //8*8=64                 
                t = Integer.toBinaryString(textInBase64Char[k]);
                while(t.length()<8) t = "0"+t;
                temp = temp + t;
                k++;
            }
            textInByChar[i] = temp.toCharArray();
        }
    }
    
    public char[][] getTextIn(){
        return textInByChar;
    }
    
    public int getNumOfGroup(){
        return numOfGroup;
    }
    
    //获取char形式二进制的输出文本，并转换为String
    private void setTextOut(char[][] ch) throws UnsupportedEncodingException{
        this.textOutByChar = ch;
        textOutByCharToString();
    }
    
    //将char格式的二进制转换为String，并将分组合并
    private void textOutByCharToString() throws UnsupportedEncodingException{
        for(int i=0;i<numOfGroup;i++){
            int index = 0;
            for(int j = 0;j<64;j=j+8){
                String temp = "";
                //每八个字符构成一个byte
                for(int k = 0;k<8;k++){
                    temp = temp + textOutByChar[i][index];
                    index++;
                }
                textOut += BitToChar(temp);
                String s =""+ BitToChar(temp);
            }
        }
    }
    
    //生成输出文件
    public void setFileOut(char[][] ch) throws UnsupportedEncodingException{
        //生成输出文本
        setTextOut(ch);
        
        if(action.equals("Encryption")){
            textOutBase64 = Base64En(textOut);
        }
        else if(action.equals("Decryption")){
            textOutBase64 = Base64De(textOut);
        }
        
        
        String s;
        File fileOut = new File(filenameOut);
        FileWriter fileOutWriter;
        try {
            fileOutWriter = new FileWriter(fileOut);
            BufferedWriter out = new BufferedWriter(fileOutWriter);
            
            if(textOutBase64.indexOf("^")!=-1){
                //在输出文件中写入补全符号“^”前的输出文本
                s = textOutBase64.substring(0, textOutBase64.indexOf("^"));
            }else{
                s = textOutBase64;
            }
            out.write(s);
            
            out.close();
            fileOutWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(DESData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //在输入的文件名后加Out生成输出文件名
    public void setFilenameOut(){
        StringBuilder strb = new StringBuilder(filenameIn);
        int index = strb.indexOf(".");
        strb.insert(index, "Out");
        filenameOut = strb.toString();
    }
    
    //获取输出文件名
    public String getFilenameOut(){
        return filenameOut;
    }
}
