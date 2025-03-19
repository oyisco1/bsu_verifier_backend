package com.BS_Verifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BsVerifierApplication {


	public static void main(String[] args) {

	    int rows = 9;
	    for(int i =1; i<=rows;  i++){
	        for(int j = rows-i; j>0; j--){
                System.out.println("");
            }
	        for(int k =1; k <=i; k++){
                System.out.println(k + " ");
            }
            System.out.println();
        }
	}

}
