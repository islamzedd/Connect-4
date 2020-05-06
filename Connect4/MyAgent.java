import java.util.Random;
import java.util.ArrayList;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        int x = iCanWin(0); // gets the result of icanwin function
        int y = theyCanWin(0); //gets the result of they can win function
        if(x != -1) //checks to see if there is a winning move and performs it
        {
            moveOnColumn(x);
        }
        else if(y != -1) // checks to see if there is a move to block the other agent and performs it
        {
            moveOnColumn(y);
        }
        else //tries to avoid making a move that will make the oponnent win the next turn and makes a move thst makes this agent wins next turn if it exists
        {
            int theyWin = theyCanWin(1); //gets the result of they can win function
            int iWin = iCanWin(1); // gets the result of i can win function(the parameter modifies the function behaviour)
            if(theyWin != -1) //if there is a move that makes them win
            {
              int random = randomMove();//a random move is generated
              while(random == theyWin)//and tested to see if it is not the losing move
              {
                  random = randomMove();
              }
              if(iWin != -1 && theyWin != iWin)//sees if even a better move can be done making sure that it won't help the other agent
              {
                  random = iWin; // if so set random to it
              }
              moveOnColumn(random); // perform the not so random move
            }
            else if(iWin != -1 && theyWin != iWin)//if there wasnot amove that will help the agent in the first place and there is a move that help me but not help both of us
            {
                moveOnColumn(iWin);//perform it
            }
            else
            {
                moveOnColumn(randomMove());//and if all fails just go random
            }
            
        }
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin(int x)//the x is to get the slot that if i put in will enable me to win the next turn
    {
        for(int i=0;i<myGame.getColumnCount();i++)//iterates over each column
        {
            Connect4Column column = myGame.getColumn(i);//gets the column
            if(!(column.getIsFull())) //makes sure it is not already full
            {
               for(int j=0;j<column.getRowCount()-2;j++) //go over all the slots but not the last two
               {
                   Connect4Slot slot1 = column.getSlot(j);//get the slot
                   if(slot1.getIsFilled())//if it is filled
                   {
                      Connect4Slot slot2 = column.getSlot(j+1);//gets the slot after it
                      Connect4Slot slot3 = column.getSlot(j+2);//and the one after that
                      if(iAmRed)//if my color is red
                      {
                          if(slot1.getIsRed() && slot2.getIsRed() && slot3.getIsRed())//checks to see if they are all red
                          {
                              return i; //the column index
                          }    
                      }
                      else
                      {
                          if(!(slot1.getIsRed() || slot2.getIsRed() || slot3.getIsRed()))//checks to see if they are all yellow
                          {
                              return i;//the column index
                          }   
                      }
                   }
               }
            }
        }//end of the first test to see if i can win with columns and starting rows test
        for(int i=0;i<myGame.getRowCount();i++)//iterate over each row
        {
            for(int j=0;j<myGame.getColumnCount()-3;j++)//iterate over each column but not the last three
            {
                ArrayList<Connect4Slot> slots = new ArrayList<Connect4Slot>(); //putting them all in an arraylist
                slots.add(myGame.getColumn(j).getSlot(i));
                slots.add(myGame.getColumn(j+1).getSlot(i));
                slots.add(myGame.getColumn(j+2).getSlot(i));
                slots.add(myGame.getColumn(j+3).getSlot(i));
                int counter = 0; // to count the empty slots
                int index = -1; // to know the index of the empty slot
                for(int k=0;k<slots.size();k++) //a simple loop to check for empty slots
                {
                    if(!(slots.get(k).getIsFilled()))
                    {
                        counter++;
                        index = k;
                    }
                }
                if(counter == 1)//if there is only one empty slot it should remove it from the array
                {
                    slots.remove(index);
                }    
                if(iAmRed && counter == 1)//if iam red and there is one empty slot
                {
                    if(slots.get(0).getIsRed() && slots.get(1).getIsRed() && slots.get(2).getIsRed() && (getLowestEmptyIndex(myGame.getColumn(index+j))==i+x))//check if all the reamaining four are red and that if i put a token it will complete the row
                    {
                        return index+j;//return the index
                    }
                }
                else if(counter == 1)//if iam yellow and only one empty slot
                {
                    if((!(slots.get(0).getIsRed() || slots.get(1).getIsRed() || slots.get(2).getIsRed()))&& (getLowestEmptyIndex(myGame.getColumn(index+j))==i+x))//same with red but it's yellow this time
                    {
                        return index+j;//return the index
                    }
                }
                }
            }//if it faild i will test the diagonals starting with the diagonal going like (/)
        for(int i=0;i<myGame.getRowCount()-3;i++) //iterate over each row but not the last three
        {
            for(int j=3;j<myGame.getColumnCount();j++)//iterate over each column but not the first three
            {
                ArrayList<Connect4Slot> slots = new ArrayList<Connect4Slot>();//get an array list of four slots forming a diagonal
                slots.add(myGame.getColumn(j).getSlot(i));
                slots.add(myGame.getColumn(j-1).getSlot(i+1));
                slots.add(myGame.getColumn(j-2).getSlot(i+2));
                slots.add(myGame.getColumn(j-3).getSlot(i+3));
                int counter =0;//checking to see if there is one empty slot like i did with rows
                int index=-1;
                for(int k=0;k<slots.size();k++)//same what i did with rows
                {
                    if(!(slots.get(k).getIsFilled()))
                    {
                        counter++;
                        index = k;
                    }
                }
                if(counter == 1)//same what i did with rows
                {
                    slots.remove(index);
                }
                if(iAmRed && counter == 1)//if iam red and there is one empty dlot
                {
                    if(slots.get(0).getIsRed() && slots.get(1).getIsRed() && slots.get(2).getIsRed() && (getLowestEmptyIndex(myGame.getColumn(j-index))==i+index+x))//check if it's all red and that the slot will complete the chain
                    {
                        return j-index;//index of the column
                    }
                }
                else if(counter == 1)
                {
                    if((!(slots.get(0).getIsRed() || slots.get(1).getIsRed() || slots.get(2).getIsRed()))&& (getLowestEmptyIndex(myGame.getColumn(j-index))==i+index+x))
                    {
                        return j-index;//same as above but with yellow
                    }
                }
            }
        }//if there is no winning move try the other diagonal(\)
        for(int i=0;i<myGame.getRowCount()-3;i++)//go over the rows but not last three
        {
            for(int j=0;j<myGame.getColumnCount()-3;j++)//go over the columns but not last three
            {
                ArrayList<Connect4Slot> slots = new ArrayList<Connect4Slot>();//same as the diagonal befors
                slots.add(myGame.getColumn(j).getSlot(i));
                slots.add(myGame.getColumn(j+1).getSlot(i+1));
                slots.add(myGame.getColumn(j+2).getSlot(i+2));
                slots.add(myGame.getColumn(j+3).getSlot(i+3));
                int counter =0;
                int index=-1;
                for(int k=0;k<slots.size();k++)//same as the diagonal befors
                {
                    if(!(slots.get(k).getIsFilled()))
                    {
                        counter++;
                        index = k;
                    }
                }
                if(counter == 1)//same as the diagonal before
                {
                    slots.remove(index);
                }
                if(iAmRed && counter == 1)//same as the diagonal before
                {
                    if(slots.get(0).getIsRed() && slots.get(1).getIsRed() && slots.get(2).getIsRed() && (getLowestEmptyIndex(myGame.getColumn(j+index))==i+index+x))
                    {
                        return j+index;
                    }
                }
                else if(counter == 1)//same as the diagonal before
                {
                    if((!(slots.get(0).getIsRed() || slots.get(1).getIsRed() || slots.get(2).getIsRed()))&& (getLowestEmptyIndex(myGame.getColumn(j+index))==i+index+x))
                    {
                        return j+index;
                    }
                }
            }
        }
        return -1;//if all fails return -1 meaning ican't win
    }
    
    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin(int x)//same what is above but changing the checking for the color to be reversed and the x is to see if there is amove that will make the agent win at the nect turn
    {
        for(int i=0;i<myGame.getColumnCount();i++)
        {
            Connect4Column column = myGame.getColumn(i);
            if(!(column.getIsFull()))
            {
               for(int j=0;j<column.getRowCount()-2;j++)
               {
                   Connect4Slot slot1 = column.getSlot(j);
                   if(slot1.getIsFilled())
                   {
                      Connect4Slot slot2 = column.getSlot(j+1);
                      Connect4Slot slot3 = column.getSlot(j+2);
                      if(!(iAmRed))//just added a not here that's it
                      {
                          if(slot1.getIsRed() && slot2.getIsRed() && slot3.getIsRed())
                          {
                              return i;
                          }    
                      }
                      else
                      {
                          if(!(slot1.getIsRed() || slot2.getIsRed() || slot3.getIsRed()))
                          {
                              return i;
                          }   
                      }
                   }
               }
            }
        }    
        for(int i=0;i<myGame.getRowCount();i++)
        {
            for(int j=0;j<myGame.getColumnCount()-3;j++)
            {
                ArrayList<Connect4Slot> slots = new ArrayList<Connect4Slot>();
                slots.add(myGame.getColumn(j).getSlot(i));
                slots.add(myGame.getColumn(j+1).getSlot(i));
                slots.add(myGame.getColumn(j+2).getSlot(i));
                slots.add(myGame.getColumn(j+3).getSlot(i));
                int counter = 0;
                int index = -1;
                for(int k=0;k<slots.size();k++)
                {
                    if(!(slots.get(k).getIsFilled()))
                    {
                        counter++;
                        index = k;
                    }
                }
                if(counter == 1)
                {
                    slots.remove(index);
                }    
                if((!(iAmRed)) && counter == 1)
                {
                    if(slots.get(0).getIsRed() && slots.get(1).getIsRed() && slots.get(2).getIsRed() && (getLowestEmptyIndex(myGame.getColumn(index+j))==i+x))
                    {
                        return index+j;
                    }
                }
                else if(counter == 1)
                {
                    if((!(slots.get(0).getIsRed() || slots.get(1).getIsRed() || slots.get(2).getIsRed()))&& (getLowestEmptyIndex(myGame.getColumn(index+j))==i+x))
                    {
                        return index+j;
                    }
                }
                }
            }
        for(int i=0;i<myGame.getRowCount()-3;i++)
        {
            for(int j=3;j<myGame.getColumnCount();j++)
            {
                ArrayList<Connect4Slot> slots = new ArrayList<Connect4Slot>();
                slots.add(myGame.getColumn(j).getSlot(i));
                slots.add(myGame.getColumn(j-1).getSlot(i+1));
                slots.add(myGame.getColumn(j-2).getSlot(i+2));
                slots.add(myGame.getColumn(j-3).getSlot(i+3));
                int counter =0;
                int index=-1;
                for(int k=0;k<slots.size();k++)
                {
                    if(!(slots.get(k).getIsFilled()))
                    {
                        counter++;
                        index = k;
                    }
                }
                if(counter == 1)
                {
                    slots.remove(index);
                }
                if((!(iAmRed)) && counter == 1)
                {
                    if(slots.get(0).getIsRed() && slots.get(1).getIsRed() && slots.get(2).getIsRed() && (getLowestEmptyIndex(myGame.getColumn(j-index))==i+index+x))
                    {
                        return j-index;
                    }
                }
                else if(counter == 1)
                {
                    if((!(slots.get(0).getIsRed() || slots.get(1).getIsRed() || slots.get(2).getIsRed()))&& (getLowestEmptyIndex(myGame.getColumn(j-index))==i+index+x))
                    {
                        return j-index;
                    }
                }
            }
        }
        for(int i=0;i<myGame.getRowCount()-3;i++)
        {
            for(int j=0;j<myGame.getColumnCount()-3;j++)
            {
                ArrayList<Connect4Slot> slots = new ArrayList<Connect4Slot>();
                slots.add(myGame.getColumn(j).getSlot(i));
                slots.add(myGame.getColumn(j+1).getSlot(i+1));
                slots.add(myGame.getColumn(j+2).getSlot(i+2));
                slots.add(myGame.getColumn(j+3).getSlot(i+3));
                int counter =0;
                int index=-1;
                for(int k=0;k<slots.size();k++)
                {
                    if(!(slots.get(k).getIsFilled()))
                    {
                        counter++;
                        index = k;
                    }
                }
                if(counter == 1)
                {
                    slots.remove(index);
                }
                if((!(iAmRed)) && counter == 1)
                {
                    if(slots.get(0).getIsRed() && slots.get(1).getIsRed() && slots.get(2).getIsRed() && (getLowestEmptyIndex(myGame.getColumn(j+index))==i+index+x))
                    {
                        return j+index;
                    }
                }
                else if(counter == 1)
                {
                    if((!(slots.get(0).getIsRed() || slots.get(1).getIsRed() || slots.get(2).getIsRed()))&& (getLowestEmptyIndex(myGame.getColumn(j+index))==i+index+x))
                    {
                        return j+index;
                    }
                }
            }
        }
        return -1;
    }
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
}
