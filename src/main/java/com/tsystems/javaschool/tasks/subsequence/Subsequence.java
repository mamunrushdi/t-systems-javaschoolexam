package com.tsystems.javaschool.tasks.subsequence;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Subsequence 
{
    /**
     * 
     * @author MD AL MAMUNUR RASHID
     * date: 08/02/2019
     * 
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     *
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) 
    {	
    	//we will handle  illegal argument exception first

		//if list is not created (assigned to null),  
		//in such case we will throw IllegalArguemntException 
    	if( x == null || y == null ) throw new IllegalArgumentException();

    	//if list X is empty, then it will always be subsequence of Y
    	//unless list Y is not null, which is handled in our exception 
    	//handling block
    	if(x.isEmpty()) return true;
    	    	 
    	//list X is larger than list Y, then X can't be subsequence of Y
    	//so it will make X invalid, in such case we will return false
    	if(x.size() > y.size()) return false;
    	
    	//converting the both list to Queue
    	//we are using Queue data structure to avoid iteration
    	//and to remove item flexibly
    	Queue yQueue = new LinkedList<>(y);
    	Queue xQueue = new LinkedList<>(x);
    	/*
    	 * Algothim:
    	 * -first we will remove an item from the X(xQueue), store it 
    	 * to a variable named, xItem
    	 * -then we will keep removing item from Y(yQueue), list untill we found a
    	 *  match for X item, xItem
    	 * -if x item's match is found, then we will update xItem variable with
    	 * by removing next item from the xQueue
    	 * And will keep removing items from Y list untill xItem is found
    	 * 
    	 * if xQueue(x list) is emptied before yQueus (y list),
    	 * then there is a subsequence of X in Y
    	 * if yQueus (y list) is emptied before xQueue(x list), 
    	 * then there is not subsequence of X in Y
    	 * 
    	 */
    	
    	//variable to store items from both lists
    	Object xItem;
    	Object yItem;
    	
    	xItem = xQueue.remove(); //get the first item of the X list
    	
    	while(!yQueue.isEmpty()) //keep removing untill y list is not emtpy
    	{    		
        	yItem = yQueue.remove();        		
    		
        	//xItem matches with yItems
        	if(yItem.equals(xItem)) 
			{
        		//if we could have emptied xQueue at this location, 
        		//then we have found the subsequence of x
				if(xQueue.isEmpty()) return true; 
				
				//after finding a match get next item 
				xItem = xQueue.remove();
			}
    	}//end of while loop
    	
    	return false;
    }//end of the method
}
