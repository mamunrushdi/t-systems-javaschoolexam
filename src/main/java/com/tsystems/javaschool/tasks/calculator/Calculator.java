package com.tsystems.javaschool.tasks.calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Calculator 
{
    /**
     * @author MD AL MAMUNUR RASHID
     * 08/02/219
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
	
	public String evaluate(String str)
	{
		 //this variable will store the total sum of the arithmetic calculation
 		double sum = 0.0; 
 		
 		//if there is any white spaces in the string, we will remove them
    	if(str != null) str = str.replaceAll("\\s", "");
    	  	
    	//print each string's validity
 		//if string is invalid, we will return null
    	if(!confirmValidity(str)) return null;

    	 //if string contains parentheses, we will compute the parentheses part first
		if(str.contains("("))	str = computeParenthesesBlocks(str);
    	
 
		//after solvong parentheses part, we will again check the validity
		//as the result will be return as String
		if(!confirmValidity(str)) return null;
   	 
		
		//we will replace all the '-' (minuses) with '+-' by doing this we can split 
		//the whole string by '+' , while preserving the mathematical operational signs,. '*', '/', '-'
		String minuslessStr = str.replaceAll("-", "+-");
        
 		/*
 		 * we will replace all minuses except the minuses which are preceded 
         * or succeeded by multiplication(*) or division (/) sign
		 * so if a minus sign after or before / or * sing was replace by '+-' we will 
		 * correct it by placing minus (-) again
		 * 
		 */
		minuslessStr = correctMisplacedPluses(minuslessStr, str);
		
		/*
		 * we will first split the string by +
		  here we are following divide-and-conquer algorithm 
		- first we will split by +
		- if newly splited string contains * sign, we will split by *
		- if newly splited string contains / sign, we will split by /
		- if * splited string contains, /, then we will also split *part by /
		*
		*/
		String[] byPluses = minuslessStr.split("\\+");
	
		//compution starts here
		for(int i = 0; i < byPluses.length; i++)
		{
			//string contains only digits,  
			if(isValidDigit(byPluses[i]))
			{ 
				/*
				 * if string contains white space, we will not add it
				 * if can happen when first digit of the string minus and thus 
				 * was replaced with '+-', i.e. "-5+6" will be converted "+-5+6"
				 * which in splitting process will generate and empty String
				 * as we are splitting by +
				 * In such case, we will move to the next iteration
				 * 
				 */
				if(byPluses[i].equals("")) continue;
				
				sum = sum  + Double.parseDouble(byPluses[i]); 
 			}//end if block
			
			//string contains multiply (*) sign 
			if(byPluses[i].contains("*")) sum += getMultiplyResult(byPluses[i]);
 
			//string contains only division(*) sign
			if(byPluses[i].contains("/") && !byPluses[i].contains("*"))
				sum += getDivisionResult(byPluses[i]);
			 
		}//end of for loop  
		 //return the result in String format having rounded
		return roundDigit(sum); 
	}
	//helper method to perofrm rounding to 4 significant digits
	// Example: 102.12356 -> 102.1236
	private static String roundDigit(double digit)
	{
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		
		return df.format(digit);
	}
	/**helper method to compute parentheses block  
	 * we will compute one by one parentheses block, start from the inner most block
	 *
	 * @param string with parentheses block
	 */
	private  String computeParenthesesBlocks(String str)
	{ 
		int parenthesese  = getTotalParenthesesBlock(str);
		 
		while (parenthesese > 0)
		{			
			//first we will find the inner most open parenthesis
			int inrMostOpnPthns = str.lastIndexOf('('); 
			
			//now we will find first close parenthesis from innerMostParenthsese
			int inrMostClsPthns = str.indexOf(')', inrMostOpnPthns); 
			
			//now we will get the inner most parentheses block
			String innMstPrtSubStr = str.substring(inrMostOpnPthns, inrMostClsPthns +1); 
			
			//System.out.println("4innMstPrtSubStr: " + innMstPrtSubStr);
			//now we will compute this part

			String result = computeSingleParenthesesBlock(innMstPrtSubStr);
			
			//now replace the result with inner most parentheses block
			if(result == null) str = str.replace(innMstPrtSubStr, "0");
			else str = str.replace(innMstPrtSubStr, result);
		   
			parenthesese  = getTotalParenthesesBlock(str);
		}
		 
		return str;
	}

	//get total number of parentheses block
	private int getTotalParenthesesBlock(String str)
	{
		int counter = 0;
		for(int i = 0; i < str.length(); i++)
			if(str.charAt(i) == '(') counter++;
		return counter;
	}


	//helper method to calculate multplication (*)
	private double getMultiplyResult(String string) 
	{
		double result = 1.0;

		//split the string
		String[] byMultiplies = string.split("\\*");
	 
		for(String multiply : byMultiplies)
		{ 
			//if multiply string contains divsion operator,
			//we will compute them separately
			if(multiply.contains("/")) result *=  getDivisionResult(multiply);
			else result *= Double.parseDouble(multiply);
		}
		
		return result;
	}

	//helper method to calculate division(/)
	private double getDivisionResult(String string) 
	{
		String[] byDivision = string.split("\\/");
		
		//get the first divident
 		double divident = Double.parseDouble(byDivision[0]); ;

		//we will iterate byDivision size - 1 times, 
		//so that in the iteration, we can use i+1 index
		for(int i = 0; i < byDivision.length - 1; i++)
			divident /= Double.parseDouble(byDivision[i + 1]);
		
		return divident;
	} 
	
	//helper method to check if a string contains only valid digits
	/**
	 *  A string is valid digit if it only contains digit or digit with minus sign
	 *  that's why here will only check whether a string contains
	 *  multiplication('*') or division('/') sign, if contains, 
	 *  then this is not a valid digit		
	 *  
	 *  @param string - to check validity
	 */
	private static boolean isValidDigit(String string) 
	{
		if(string.contains("/") || string.contains("*")) return false;
		
		return true;
	}
	
	/**helper method to compute the parentheses part of the string this method, 
	 * after computing the parentheses part, replace the parentheses substring 
	 * in the main string with the result in String format
	 * 
	 * @param s to is the sustrin of string to compute first
	 *   "8 + (3 * 5) - 4" -> "(3 * 5)" will be computed first and computational result 
	 *   will returs in String format
	 * @return "8 + 15 - 4"
	 */
	private String computeSingleParenthesesBlock(String str)
	{
		String result = "";
		
		//find the start index of the open parentheses
		if(str.indexOf("(") != -1)
		{
			int startInd = str.indexOf("(");
			//get end of close parentheses
			int endInd = str.indexOf(")", startInd + 1);
			
			//get arithmetical part between parentheses
			String subStr = str.substring(startInd+1, endInd);
			System.out.println("string in1 paretheses: " + subStr);
			//calculate the subStr and replace it to the original string
			result  = evaluate(subStr);;
		}
		 
		return result; 
	}
	
	//helper method to fix the minus operand when it is adjacent to * or / on both sides
	private String correctMisplacedPluses(String noMinuses, String str) 
	{
       	StringBuilder retString = new StringBuilder(noMinuses);
    	
    	for(int i = 0; i < str.length(); i++)
		{
			if(str.indexOf('-', i) != -1)
			{
				int ind = str.indexOf('-', i);
				if(ind > 0 &&((str.charAt(ind-1) == '*' ||str.charAt(ind-1) == '/')  || 
						(str.charAt(ind+1) == '*' ||str.charAt(ind +1) == '/')))
				{
					retString.delete(ind, ind+1);
					i = i + ind;
				}
			}	
		}
 
		return retString.toString();
	}
	//helper method check the validity of the string
	private boolean confirmValidity(String str) 
	  {
	  	/*
	  	 * string will be an invalid arithmetical expression if 
	  	 * -string is empty, null
	  	 * -string contains characters except 0-9 digits and four mathematical expression (+,-, *,/)
	  	 * -string contains adjacent similar mathematicl expression (++, --, **, //)
	  	 * -string has a zeor (0) divisor
	  	 * - string has enqual open and close parenthesis
	  	 * - if close parenthesis appear before open parenthesis
	  	 */
	  	if(str == null || str.length() == 0) return false;
	  	
	  	if((str.indexOf("..") != -1) ||(str.indexOf("++") != -1)||(str.indexOf("--") != -1)||
					(str.indexOf("**") != -1)||(str.indexOf("//") != -1)) return false;
	
	  	 
	  	//close parenthesis appear before open parenthesis
	  	if(str.indexOf('(') > str.indexOf(')')) return false;
	  	
	  	//check open and close parentheses amount
	  	int openParth = 0;
	  	int closeParth = 0;
	  	
		 
  		// "5/20-20
  		//if divident in divided zero (0)
  		
	  	if(isDividedByZero(str)) 
  		{
   			return false;
  		}
	  	
	  	for(int i = 0; i < str.length(); i++)
	  	{
	  		char ch = str.charAt(i);
	  		if( ch == '(') openParth++;
	  		if(ch == ')') closeParth++;
	  		if(Character.isAlphabetic(ch)) return false;
	  		
	  		if(!Character.isDigit(ch) && ch != '+' && ch != '-' &&  ch != '/' &&  ch != '*' &&  ch != '.'
	  				&& ch != '(' && ch != ')')  return false;
	  	
	    }//end of for loop

	  	if(openParth != closeParth) return false;
  	  
		return true;
	}
	//helper method to check if divident is divided by zero (0)
	// " 10 / 4 -4", "10/50-50	
	private boolean isDividedByZero(String str)
	{
	 	//first we will get the '/' index	
		int ind = str.indexOf('/');
 		//if after / sign, the divisor is zero, return true
		if(ind < str.length() && str.charAt(ind + 1) == '0') return true;
		 
		 
		//if the immediate opearnd of / sign is -, only then we will check this conditions
		
		//now let's get the substring from / to '-'
		String leftString = "";
		//get the next index after '/' index
		ind = ind + 1;
		char ch = str.charAt(ind);

		while(Character.isDigit(ch) && ind < str.length() - 1)
		{
			leftString += ch;
			ind = ind + 1;
			 if(ind < str.length() - 1)
				ch = str.charAt(ind);
		}	
		
		//now we will check if after / sign, the immediate operand is -
		// we will get the minus index
		int minusIndex = ind; 
		if(str.charAt(minusIndex) == '-' && ind < str.length())
		{
			//get the substring between - and immediat next operand
			String rightSubString = "";
			minusIndex += 1;
			ch = str.charAt(minusIndex);

			while(Character.isDigit(ch) && minusIndex < str.length())
			{
				rightSubString += ch;
				minusIndex = minusIndex + 1;
				
				if(minusIndex < str.length() - 1)
				 ch = str.charAt(minusIndex);
			}

			return leftString.equals(rightSubString);
		}
		 
		return false;
	}

}	
	