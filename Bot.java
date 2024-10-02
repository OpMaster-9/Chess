import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Bot {
  public static void main(String[] args) {
    
    
  }
  private int evaluateBoard(int[] board, int colour) {
    int score = 0;
    List<Integer> figures = new ArrayList<>();
    for (int j : board) {
      if (j != 0) {
        figures.add(j);
      }
    }
    if (colour == 1) {
      for (Integer figure : figures) {
        switch (figure) {
          case 1 -> score += 200;
          case 2 -> score += 9;
          case 3 -> score += 5;
          case 4, 5 -> score += 3;
          case 6 -> score += 1;
          case 7 -> score -= 200;
          case 8 -> score -= 9;
          case 9 -> score -= 5;
          case 10, 11 -> score -= 3;
          case 12 -> score -= 1;
        }
      }
    }else {
      for (Integer figure : figures) {
        switch (figure) {
          case 1 -> score -= 200;
          case 2 -> score -= 9;
          case 3 -> score -= 5;
          case 4, 5 -> score -= 3;
          case 6 -> score -= 1;
          case 7 -> score += 200;
          case 8 -> score += 9;
          case 9 -> score += 5;
          case 10, 11 -> score += 3;
          case 12 -> score += 1;
        }
      }
    }
    return score;
  }
  public List<int[]> allMoves(int[] input, int colour){
    List<int[]> allMoves = new ArrayList<>();
    for (int i = 0; i < 64; i++) {
      if (Main.findColour(input,i) == colour){
        List<Integer> moves = MovementCheck.wohinGehtBittiBitti(input,i,colour);
        if (!moves.isEmpty()) {
          int[] move = new int[1 + moves.size()];
          move[0] = i;
          for (int j = 0; j < moves.size(); j++) {
            move[j + 1] = moves.get(j);
          }
          allMoves.add(move);
        }
      }
    }
    /*for (int[] array : allMoves) {
    System.out.println(Arrays.toString(array));
    }*/
    return allMoves;
  }
  public int[] miniMax2(int[] input, int colour) {
    int bestEval = Integer.MIN_VALUE; // Maximale Bewertung für den Spieler
    int[] bestMove = new int[2]; // Der beste Zug des Spielers
    
    // Erzeuge alle möglichen Züge für den Spieler
    List<int[]> moves1 = allMoves(input, colour);
    
    for (int i = 0; i < moves1.size(); i++) {
      int[] move1 = Arrays.copyOf(moves1.get(i), moves1.get(i).length);
      
      // Simuliere jeden möglichen Zug des Spielers
      for (int j = 0; j < move1.length - 1; j++) {
        int[] tempBoard = Arrays.copyOf(input, input.length);
        Main.moveWithoutCheck(tempBoard, move1[0], move1[j + 1],colour);
        
        // Prüfe, ob der Zug den eigenen König in Schach setzen würde
        if (!Main.check(tempBoard, Main.findKing(tempBoard, colour), colour)) {
          // Nun berechne den besten Zug des Gegners (Minimizer)
          int worstOpponentEval = Integer.MAX_VALUE; // Minimale Bewertung für den Gegner
          int opponentColour;
          if (colour == 1) {
            opponentColour = 0; // Wechsle die Farbe zum Gegner
          }else {
            opponentColour = 1;
          }
          
          // Erzeuge alle möglichen Züge für den Gegner
          List<int[]> opponentMoves = allMoves(tempBoard, opponentColour);
          for (int k = 0; k < opponentMoves.size(); k++) {
            int[] opponentMove = Arrays.copyOf(opponentMoves.get(k), opponentMoves.get(k).length);
            
            // Simuliere jeden möglichen Zug des Gegners
            for (int l = 0; l < opponentMove.length - 1; l++) {
              int[] opponentTempBoard = Arrays.copyOf(tempBoard, tempBoard.length);
              Main.moveWithoutCheck(opponentTempBoard, opponentMove[0], opponentMove[l + 1],opponentColour);
              //System.out.println(Main.zahlZuFeld(opponentMove[0]) + Main.zahlZuFeld(opponentMove[l + 1]));
              //Main.output(opponentTempBoard);
              
              // Prüfe, ob der Zug den gegnerischen König in Schach setzen würde
              if (!Main.check(opponentTempBoard, Main.findKing(opponentTempBoard, opponentColour), opponentColour)) {
                // Bewertung des Boards nach dem Gegenzug
                int opponentEval = evaluateBoard(opponentTempBoard, colour);
                if (opponentEval < worstOpponentEval){
                  worstOpponentEval = opponentEval;
                }
              }
            }
          }
          
          // Maximieren: Der Spieler möchte den Zug wählen, der den besten (maximalen) Wert hat,
          // nachdem der Gegner den schlechtesten möglichen Gegenzug gemacht hat.
          if (worstOpponentEval > bestEval) {
            bestEval = worstOpponentEval;
            bestMove[0] = move1[0]; // Der Startpunkt des besten Zuges
            bestMove[1] = move1[j + 1]; // Der Zielpunkt des besten Zuges
            System.out.println(Main.zahlZuFeld(bestMove[0]) + Main.zahlZuFeld(bestMove[1]) + bestEval);
          }
        }
      }
    }
    return bestMove;
  }
  public int[] miniMax(int[] input, int colour, int depth, boolean isMaximizingPlayer) {
    System.out.println("1");
    // Abbruchbedingung: Wenn die maximale Tiefe erreicht ist, evaluiere das Board
    if (depth == 0) {
      System.out.println("2");
      System.out.println(Arrays.toString(new int[]{evaluateBoard(input, colour)}));
      System.out.println(colour);
      return new int[]{evaluateBoard(input, colour)}; // Bewertung + Dummy-Zug
    }

    System.out.println("3");
    int bestEval;
    System.out.println("4");
    int[] bestMove = new int[2]; // Der beste Zug des Spielers
    System.out.println("5");
    if (isMaximizingPlayer) {
      System.out.println("6");
      bestEval = Integer.MIN_VALUE; // Maximale Bewertung für den Spieler
    } else {
      System.out.println("7");
      bestEval = Integer.MAX_VALUE; // Minimale Bewertung für den Gegner
    }
    
    // Erzeuge alle möglichen Züge für den aktuellen Spieler
    List<int[]> possibleMoves = allMoves(input, colour);
    System.out.println("8");
    if (possibleMoves.isEmpty()) {
      System.out.println("9");
      //System.out.println("Critical Error");
    }
    for (int[] move : possibleMoves) {
      System.out.println("10");
      for (int j = 1; j < move.length; j++) {
        System.out.println("11");
        //System.out.println("Schleife");
        int[] tempBoard = Arrays.copyOf(input, input.length);
        System.out.println("12");
        Main.moveWithoutCheck(tempBoard, move[0], move[j], colour);
        System.out.println("13");
        
        // Prüfe, ob der Zug den eigenen König in Schach setzen würde
        if (!Main.check(tempBoard, Main.findKing(tempBoard, colour), colour)) {
          System.out.println("14");
          int opponentColour = 0;
          System.out.println("15");
          if (colour == 0) {
            System.out.println("16");
            opponentColour = 1;
          } // end of if
          
          // Rekursiver Aufruf mit reduzierter Tiefe und wechselndem Spieler
          System.out.println("16a");
          int[] result = miniMax(tempBoard, opponentColour, depth - 1, !isMaximizingPlayer);
          System.out.println("17");
          int currentEval = result[0];
          System.out.println("18");
          //if (depth > 2) {
          //System.out.println(depth + " " + Main.zahlZuFeld(move[0]) + " " + Main.zahlZuFeld(move[1]) + " " + currentEval + " " + bestEval);
          //}
          // Maximierer (Spieler)
          if (isMaximizingPlayer) {
            System.out.println("19");
            if (currentEval > bestEval) {
              System.out.println("20");
              bestEval = currentEval;
              System.out.println("21");
              bestMove[0] = move[0]; // Startposition des besten Zuges
              System.out.println("22");
              bestMove[1] = move[j]; // Zielposition des besten Zuges
              System.out.println("23");
            }
          }
          // Minimierer (Gegner)
          else {
            System.out.println("24");
            if (currentEval < bestEval) {
              System.out.println("25");
              bestEval = currentEval;
              System.out.println("26");
              bestMove[0] = move[0]; // Startposition des besten Zuges
              System.out.println("27");
              bestMove[1] = move[j]; // Zielposition des besten Zuges
              System.out.println("28");
            }
          }
        }else {
          System.out.println("Schach");
          System.out.println(j + " " + move.length);
        }
      }
    }
    // Rückgabe der besten Bewertung und des besten Zugs
    System.out.println("29");
    return new int[]{bestEval, bestMove[0], bestMove[1]};
  }
  public int[] miniMaxBeta(int[] input, int colour, int depth, boolean isMaximizingPlayer) throws InterruptedException, ExecutionException {
    // Verwende einen Thread-Pool mit einer festen Anzahl an Threads
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    // Erzeuge eine Liste von Aufgaben (Tasks) für jeden möglichen Zug des Spielers
    List<Callable<int[]>> tasks = new ArrayList<>();
    
    // Erzeuge alle möglichen Züge für den aktuellen Spieler
    List<int[]> possibleMoves = allMoves(input, colour);
    
    for (int[] move : possibleMoves) {
      // Für jeden Zug simulieren wir die weiteren Berechnungen in einem separaten Task
      tasks.add(() -> {
        int bestEval;
        if (isMaximizingPlayer) {
          bestEval = Integer.MIN_VALUE;
        } else {
          bestEval = Integer.MAX_VALUE;
        }
        
        int[] bestMove = new int[2];
        
        // Simuliere jeden möglichen Zug des Spielers
        for (int j = 1; j < move.length; j++) {
          int[] tempBoard = Arrays.copyOf(input, input.length);
          Main.moveWithoutCheck(tempBoard, move[0], move[j], colour);
          
          // Prüfe, ob der Zug den eigenen König in Schach setzen würde
          if (!Main.check(tempBoard, Main.findKing(tempBoard, colour), colour)) {
            int opponentColour = (colour == 1) ? 0 : 1;
            
            // Rekursiver Aufruf mit reduzierter Tiefe und wechselndem Spieler
            int[] result = miniMaxBeta(tempBoard, opponentColour, depth - 1, !isMaximizingPlayer);
            int currentEval = result[0];
            
            // Maximierer
            if (isMaximizingPlayer) {
              if (currentEval > bestEval) {
                bestEval = currentEval;
                bestMove[0] = move[0];
                bestMove[1] = move[j];
              }
            }
            // Minimierer
            else {
              if (currentEval < bestEval) {
                bestEval = currentEval;
                bestMove[0] = move[0];
                bestMove[1] = move[j];
              }
            }
          }
        }
        return new int[]{bestEval, bestMove[0], bestMove[1]};
      });
    }
    
    // Starte alle Aufgaben parallel
    List<Future<int[]>> results = executor.invokeAll(tasks);
    
    // Finde den besten Zug basierend auf den Ergebnissen der Threads
    int bestEval = Integer.MIN_VALUE;
    int[] bestMove = new int[2];
    
    for (Future<int[]> result : results) {
      int[] evalResult = result.get(); // Ergebnis des Threads abrufen
      int eval = evalResult[0];
      
      if (eval > bestEval) {
        bestEval = eval;
        bestMove[0] = evalResult[1]; // Startpunkt des besten Zuges
        bestMove[1] = evalResult[2]; // Zielpunkt des besten Zuges
      }
    }
    
    // Beende den Executor-Service
    executor.shutdown();
    
    return new int[]{bestEval, bestMove[0], bestMove[1]};
  }
  public int[] calculateBestMove(int[] input, int colour){
    int bestEval = Integer.MIN_VALUE;
    int[] bestMove = new int[2];
    for (int i = 0; i < 64; i++) {
      if (Main.findColour(input,i) == colour){
        List<Integer> allMoves = MovementCheck.wohinGehtBittiBitti(input,i,colour);
        for (int j = 0; j < allMoves.size(); j++) {
          int[] temp = Arrays.copyOf(input,input.length);
          Main.moveWithoutCheck(temp, i, allMoves.get(j),colour);
          //System.out.println(Main.zahlZuFeld(i) + Main.zahlZuFeld(allMoves.get(j)) + evaluateBoard(temp,colour));
          if (evaluateBoard(temp,colour) > bestEval && !Main.check(temp,Main.findKing(temp,colour),colour)) {
            bestEval = evaluateBoard(temp,colour);
            bestMove[0] = i;
            bestMove[1] = allMoves.get(j);
          }
        }
      }
    }
    return bestMove;
  }
  
}
