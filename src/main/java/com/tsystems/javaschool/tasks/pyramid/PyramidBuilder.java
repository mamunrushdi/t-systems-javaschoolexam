package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

 
public class PyramidBuilder 
{
    /**
     * @author MD AL MAMUNUR RASHID
     * Date: 09/02/2019
     * 
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {

    	//if inputNumbers list contains null arguemnt throw exception
    	if(inputNumbers.contains(null)) throw new CannotBuildPyramidException();
    	
    	//compute how many row we will have for this pyramid 
		int row = getRowNumber(inputNumbers);
		
		/*
		 * if given list does not contain required number of integer numbers 
		 * (items)to build the pyramid which will be indicated by row number,
		 * we will throw exception, 
		 * if the inputNumbers list does not contains required number of items, 
		 * then it will return -1
		 */
		if(row == -1) throw new CannotBuildPyramidException();
		
		/*
		 * for a pyramid there will be 2*row - 1 number of column(s)
		 * As a pyramid follows the bellow pattern
		 *
		 *  row | col
		 *  ----------
		 *   3  | 5
		 *   4  | 7
		 *   5  | 9
		 *   6  | 11
		 *  
		 */  
		int col = 2 * row - 1;
		
		/*
		 * initialize a 2-D array to build the pyramid
		 * this will be our return 2D array 
		 */
		int[][] pyramid = new int[row][col];

		//sort the inputNumbers in ascending order to start building the pyramid from the smallest item
		Collections.sort(inputNumbers);
		
		//we will use Queue data structure to remove items easily and to avoid iteration
		Queue<Integer> queue = new LinkedList<>(inputNumbers);
		
		//Pyramid building starts here
		//top point of our pyramid will be the middle of the column 
		//after building the top of the pyramid,
		//we will spread down by shifting 1 position to both sides (right and left) of the top point
		int startPosition = (pyramid[0].length) / 2;
		
		for(int i = 0; i < pyramid.length; i++)
		{
			//for each row we will have a new start position to put the elements
 			int start = startPosition;
 			
 			/*
 			 * each row will have i+1 number of elements (number) in it,
 			 * that's why we will loop for each row i times,
 			 *  as i index start at 0 (i == 0), so we will not assign j = i + 1
 			 *  
 			 */
			for(int j = 0; j <= i; j++)
			{
				//removing smallest item from the queue and building the pyramid
				pyramid[i][start] = queue.remove();
 				//shifting forward 2 postions
				start += 2;
			}
			//after each iteration, startPoint will be shifted left by one position
			startPosition --;
		}
		return pyramid;
    }//end of the method 

    /**
     * 
     * this is a helper method to compute the row number 
     * using the given input integer list

	 * @param given list of integers to build pyramid
	 * 
	 * A pyramid can be build only if the total number of items of the input list 
	 * follows this, 3 6 10 15 21 ...sequence 
	 * 3 6 10 15 21 ... this sequence of number, we will call pyramid sequence
	 * having the size of items we can get the row number of the pyramid
	 * by computing using this eqaution,  n(n+1)/2, where the n is the size of the list
	 * we will calcualte the root, which will be the row number, of n(n+1)/2 
	 * using the formula, 
	 * root = (-b + (sqrt(b^2 - 4ac))/2a (for standard equation ax^2+bx+c), 
	 * in our case, for n(n+1)/2 = n^2+n-2, it will be  = (-1 + sqrth(1 + 8n))/2, 
	 * reorganizing this we will get
	 * ((sqrt(1 + 8n) -1) / 2;
     * 
     * @return row of the pyramid
     */
    private static int getRowNumber(List<Integer> input)
	{ 	
    	/*
    	 * a list of items will build successfully a pyramid only if the list size follow 
    	 * the pyramid sequence a list will fulfill the requirment if it's size return a positive integer
    	 * 
    	 */
    	
		int	listSize = input.size();
		double result = (Math.sqrt(1+ 8 * listSize) - 1)/2;
		if(result == Math.ceil(result))
			
		return (int)result;
		
		return -1;
		
	}//end of the method
}
