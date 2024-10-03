import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Bot {
  public static void main(String[] args) {


  }
  private static int evaluateBoard(int[] board, int colour) {
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
    return allMoves;
  }
  public int[] miniMax(int[] input, int colour, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
    if (depth == 0) {
      return new int[]{evaluateBoard(input, colour)};
    }
    int bestEval;
    int[] bestMove = {-1, -1};
    if (isMaximizingPlayer) {
      bestEval = Integer.MIN_VALUE;
    } else {
      bestEval = Integer.MAX_VALUE;
    }
    List<int[]> possibleMoves = allMoves(input, colour);
    if (possibleMoves.isEmpty()) {
      System.out.println("Allmoves empty error");
      return new int[]{bestEval, -1, -1};
    }
    for (int[] move : possibleMoves) {
      for (int j = 1; j < move.length; j++) {
        int[] tempBoard = Arrays.copyOf(input, input.length);
        Main.moveWithoutCheck(tempBoard, move[0], move[j], colour);
        if (!Main.check(tempBoard, Main.findKing(tempBoard, colour), colour)) {
          int opponentColour = (colour == 0) ? 1 : 0;
          int[] result = miniMax(tempBoard, opponentColour, depth - 1, !isMaximizingPlayer, alpha, beta);
          int currentEval = result[0];
          if (isMaximizingPlayer) {
            if (currentEval > bestEval) {
              bestEval = currentEval;
              bestMove[0] = move[0];
              bestMove[1] = move[j];
            }
            alpha = Math.max(alpha, bestEval);
            if (beta <= alpha) {
              break;
            }
          } else {
            if (currentEval < bestEval) {
              bestEval = currentEval;
              bestMove[0] = move[0];
              bestMove[1] = move[j];
            }
            beta = Math.min(beta, bestEval);
            if (beta <= alpha) {
              break;
            }
          }
        }
      }
      if (beta <= alpha) {
        break;
      }
    }
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
