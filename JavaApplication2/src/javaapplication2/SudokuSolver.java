package javaapplication2;

import java.applet.* ;
import java.awt.* ;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;


public class SudokuSolver extends Applet implements Runnable {
    
    /** The model */
   protected int model[][] ;
    //public int model[][];
    
   /** The view */
   protected Button view[][];
    //public Button view[][];
   
   
   protected Date time;
   
   protected PrintWriter log;
   
   protected void createLog(){
   //PrintWriter log = new PrintWriter("log.txt", "UTF-8");
   
   
   }
   
   
    // Create model form txt file
   protected void createModel(){
   
       System.out.println(java.time.LocalTime.now() + " --> Creating Model");
    
        //This model will be our matrix to stack value matrix[9][9] in sudoku.txt
        //int model[][];x
        
        model = new int[9][9] ;

      // Clear all cells
      for( int row = 0; row < 9; row++ )
         for( int col = 0; col < 9; col++ )
            model[row][col] = 0 ;
    
    // The name of the file to open.
    //Scanner keyboard = new Scanner(System.in);
    //String fileName = keyboard.next();
    String fileName = "/Users/batuhanyalcin/Desktop/yazlab-02/ornek1/sudoku.txt";
    
    // This will reference one line at a time
    String line = null;

    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader = 
            new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = 
            new BufferedReader(fileReader);

            char temp = 0 ;
            System.out.println(java.time.LocalTime.now() + " --> Reading data from txt file and add it to Model");
        //Reading txt file and add it to matrix
        for (int row = 0; row < 9; row++) {
            while((line = bufferedReader.readLine()) != null) {
                for (int col = 0; col < 9; col++) {
                    if(line.charAt(col)=='*'){
                        model[row][col]=0;
                    }else{
                        temp =line.charAt(col);
                        model[row][col]= Character.getNumericValue(temp) ;
                    }      
                    
                System.out.print(model[row][col]);
                }
                System.out.println("");
                row++;
            }   
        }
        
        // Always close files.
        bufferedReader.close();         
    
    }//catchs for any error at the reading file
    catch(FileNotFoundException ex) {
        System.out.println(
            "Unable to open file '" + 
            fileName + "'");                
    }
    catch(IOException ex) {
        System.out.println(
            "Error reading file '" 
            + fileName + "'");                  
        // Or we could just do this: 
        // ex.printStackTrace();
      }
    
    System.out.println(java.time.LocalTime.now() + "--> Model created");
   }
    
   /** Creates an empty view*/ 
   protected void createView()
   {
       

       
      System.out.println(java.time.LocalTime.now() +"--> Flag in createView");
      
      setLayout( new GridLayout(9,9) ) ;

      view = new Button[9][9] ;

      // Create an empty view
      for( int row = 0; row < 9; row++ )
         for( int col = 0; col < 9; col++ )
         {
            view[row][col]  = new Button() ;
            add( view[row][col] ) ;
         }
      
   } //End of the createView
   
   /** Updates the view from the model */
   protected void updateView()
   {
       System.out.println(java.time.LocalTime.now() + "--> Updating View");
      for( int row = 0; row < 9; row++ )
         for( int col = 0; col < 9; col++ )
            if( model[row][col] != 0 )
               view[row][col].setLabel( String.valueOf(model[row][col]) ) ;
            else
               view[row][col].setLabel( "" ) ;
   }

   /** This method is called by the browser when the applet is loaded */
   public void init()
   {
       System.out.println(java.time.LocalTime.now() + "--> Initialization");
      createModel();
      createView();
      updateView();
   }

   /** Checks if num is an acceptable value for the given row */
   protected boolean checkRow( int row, int num )
   {
       System.out.println(java.time.LocalTime.now() + "--> Checking Row");
      for( int col = 0; col < 9; col++ )
         if( model[row][col] == num )
            return false ;

      return true ;
   }

   /** Checks if num is an acceptable value for the given column */
   protected boolean checkCol( int col, int num )
   {
       System.out.println(java.time.LocalTime.now() + "--> Checking Column");
      for( int row = 0; row < 9; row++ )
         if( model[row][col] == num )
            return false ;

      return true ;
   }

   /** Checks if num is an acceptable value for the box around row and col */
   protected boolean checkBox( int row, int col, int num )
   {
       System.out.println(java.time.LocalTime.now() + "--> Checking Box");
      row = (row / 3) * 3 ;
      col = (col / 3) * 3 ;

      for( int r = 0; r < 3; r++ )
         for( int c = 0; c < 3; c++ )
         if( model[row+r][col+c] == num )
            return false ;

      return true ;
   }

   /** This method is called by the browser to start the applet */
   public void start()
   {
       System.out.println(java.time.LocalTime.now() + "-->  Starting");
      // This statement will start the method 'run' to in a new thread
      (new Thread(this)).start() ;
   }

   /** The active part begins here */
   public void run()
   {
       System.out.println(java.time.LocalTime.now() + "--> Running");
      try
      {
         // Let the observers see the initial position
         Thread.sleep( 1000 ) ;

         // Start to solve the puzzle in the left upper corner
         solve( 0, 0 ) ;
      }
      catch( Exception e )
      {
      }
   }

   /** Recursive function to find a valid number for one single cell */
   public void solve( int row, int col ) throws Exception
   {
       System.out.println(java.time.LocalTime.now() +  "--> Solving");
      // Throw an exception to stop the process if the puzzle is solved
      if( row > 8 )
         throw new Exception(java.time.LocalTime.now() + "Solution found" ) ;

      // If the cell is not empty, continue with the next cell
      if( model[row][col] != 0 )
         next( row, col ) ;
      else
      {
         // Find a valid number for the empty cell
         for( int num = 1; num < 10; num++ )
         {
            if( checkRow(row,num) && checkCol(col,num) && checkBox(row,col,num) )
            {
               model[row][col] = num ;
               updateView() ;

               // Let the observer see it
               Thread.sleep( 1000 ) ;

               // Delegate work on the next cell to a recursive call
               next( row, col ) ;
            }
         }

         // No valid number was found, clean up and return to caller
         model[row][col] = 0 ;
         updateView() ;
      }
   }

   /** Calls solve for the next cell */
   public void next( int row, int col ) throws Exception
   {
       System.out.println(java.time.LocalTime.now() + "--> Next Blank");
      if( col < 8 )
         solve( row, col + 1 ) ;
      else
         solve( row + 1, 0 ) ;
   }
    
    /**
     *
     */
    public static void main(){
        
        LocalTime firstTime = java.time.LocalTime.now();
        
        System.out.println(java.time.LocalTime.now() + "--> Main Function in Progress");
        //Creating object called ss to use non-static variables and call non-static functions
        SudokuSolver ss = new SudokuSolver();
        
        ss.init();
   
        LocalTime finishTime = java.time.LocalTime.now();
       //PrintStream println = (finishTime.getHour() - firstTime.getHour()) + "Total time = " ;
    }//End of the main
    
    
    
    
    
    
    
}//End of the SudokuSolver Class
