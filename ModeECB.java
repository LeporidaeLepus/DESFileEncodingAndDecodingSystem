/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B17040701;

import java.io.UnsupportedEncodingException;


/**
 *
 * @author HP
 */
public class ModeECB extends Mode{
    
    //电子密码本模式
    public ModeECB(DESData data,Key key,String mode) throws UnsupportedEncodingException{
        super(data,key,mode);
        
        for(int i = 0;i<groupNum;i++){
            output[i] = process(input[i],key,mode);
        }
        data.setFileOut(output);    //在ModeECB中进行文件生成是为了确保output是由data生成的
        
    }
    
    
    
    

}
