/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expert_systems;

/**
 *
 * @author Algreg M. Mata
 */
public class feature {
 private String value;
 private int count;
    
 feature(String val,int cnt){
     this.value = val;
     this.count = cnt;
 }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
 
}
