import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot {
  public static boolean endgame = false;
  private static int[][] piecePositionTable = {
         {20, 20, -10, -20, -30, -30, -30, -30,
                 30, 20, -20, -30, -40, -40, -40, -40,
                 10, 0, -20, -30, -40, -40, -40, -40,
                 0, 0, -20, -40, -50, -50, -50, -50,
                 0, 0, -20, -40, -50, -50, -50, -50,
                 10, 0, -20, -30, -40, -40, -40, -40,
                 30, 20, -20, -30, -40, -40, -40, -40,
                 20, 20, -10, -20, -30, -30, -30, -30
         },
         {-20, -10, -10, -5, -5, -10, -10, -20,
                 -10, 0, 0, 0, 0, 0, 0, -10,
                 -10, 0, 5, 5, 5, 5, 0, -10,
                 -5, 0, 5, 5, 5, 5, 0, -5,
                 -5, 0, 5, 5, 5, 5, 0, -5,
                 -10, 0, 5, 5, 5, 5, 5, -10,
                 -10, 0, 0, 0, 0, 0, 0, -10,
                 -20, -10, -10, -5, 0, -10, -10, -20
          },
         {0, -5, -5, -5, -5, -5, 5, 0,
                 0, 0, 0, 0, 0, 0, 10, 0,
                 0, 0, 0, 0, 0, 0, 10, 0,
                 5, 0, 0, 0, 0, 0, 10, 5,
                 5, 0, 0, 0, 0, 0, 10, 5,
                 0, 0, 0, 0, 0, 0, 10, 0,
                 0, 0, 0, 0, 0, 0, 10, 0,
                 0, -5, -5, -5, -5, -5, 5, 0
          },
         {-20, -10, -10, -10, -10, -10, -10, -20,
                 -10, 0, 0, 5, 0, 10, 5, -10,
                 -10, 0, 5, 5, 10, 10, 0, -10,
                 -10, 0, 10, 10, 10, 10, 0, -10,
                 -10, 0, 10, 10, 10, 10, 0, -10,
                 -10, 0, 5, 5, 10, 10, 0, -10,
                 -10, 0, 0, 5, 0, 10, 5, -10,
                 -20, -10, -10, -10, -10, -10, -10, -20
          },
         {-50, -40, -30, -30, -30, -30, -40, -50,
                 -40, -20, 0, 0, 0, 0, -20, -40,
                 -30, 0, 10, 15, 15, 10, 0, -30,
                 -30, 5, 15, 20, 20, 15, 5, -30,
                 -30, 5, 15, 20, 20, 15, 5, -30,
                 -30, 0, 10, 15, 15, 10, 0, -30,
                 -40, -20, 0, 5, 5, 0, -20, -40,
                 -50, -40, -30, -30, -30, -30, -40, -50
         },
         {0, 5, 5, 0, 5, 10, 50, 100,
                 0, 10, -5, 0, 5, 10, 50, 100,
                 0, 10, -10, 0, 10, 20, 50, 100,
                 0, -20, 0, 20, 25, 30, 50, 100,
                 0, -20, 0, 20, 25, 30, 50, 100,
                 0, 10, -10, 0, 10, 20, 50, 100,
                 0, 10, -5, 0, 5, 10, 50, 100,
                 0, 5, 5, 0, 5, 10, 50, 100
          },
         {-50, -30, -30, -30, -30, -30, -30, -50,
                 -40, -20, -10, -10, -10, -10, -30, -30,
                 -30, -10, 20, 30, 30, 20, 0, -30,
                 -20, 0, 0, 30, 40, 40, 0, -30,
                 -20, 0, 0, 30, 40, 40, 0, -30,
                 -30, -10, 20, 30, 30, 20, 0, -30,
                 -40, -20, -10, -10, -10, -10, -30, -30,
                 -50, -30, -30, -30, -30, -30, -30, -50
         }
  };
  private static int materialCount(int[] board){
    int value = 0;
      for (int j : board) {
          switch (j) {
              case 2:
              case 8:
                  value += 9;
                  break;
              case 3:
              case 9:
                  value += 5;
                  break;
              case 4:
              case 5:
              case 10:
              case 11:
                  value += 3;
                  break;
          }
      }
    return value;
  }
  private static int countMajorPieces(int[] board){
    int amount = 0;
    for (int j : board) {
      switch (j) {
        case 2:
        case 8:
        case 3:
        case 9:
          amount += 1;
        break;
      }
    }
    return amount;
  }
  private static boolean endgameDetection(int[] board){
      return countMajorPieces(board) < 2 && materialCount(board) < 14;
  }
  private static int piecePositionEvaluation(int[] board, int colour){
    int score = 0;
    for (int i = 1; i <= 6; i++) {
      int piece = i - 1;
      if (endgame && i == 1){
        piece = 6;
      }
      for (int j = 0; j < board.length; j++) {
        if (board[j] == i) {
          score += piecePositionTable[piece][j];
        }
      }
    }
    return score;
  }
  private static int evaluateBoardPieceValues(int[] board, int colour) {
    int score = 0;
    int[] pieceValues = {0, 200000, 900, 500, 300, 300, 100, -200000, -900, -500, -300, -300, -100};
    for (int j : board) {
      if (j != 0) {
        int value = pieceValues[Math.abs(j)];
        score += (j > 0 ? value : -value);
      }
    }
    return score;
  }
  public static int evaluateBoard(int[] board, int colour){
    int score = 0;
    score += evaluateBoardPieceValues(board,colour);
    score += piecePositionEvaluation(board,colour);
    return score;
  }
  public static List<int[]> allMoves(int[] input, int colour){
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
    if (!endgame){
      if (endgameDetection(input)){
        endgame = true;
      }
    }
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