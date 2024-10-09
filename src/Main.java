import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {

  //TODO: Botverbessern, Bug fixes:Bot making invalid moves, Draw by Repetition
  public static boolean bot = true;
  public static boolean isRunning = true;
  public static boolean richtig = true;
  public static boolean[] hasMoved = {false, false, false, false, false, false};
  public static int[] letzte = {3, 6, 0, 0, 0, 0, 12, 9,
          5, 6, 0, 0, 0, 0, 12, 11,
          4, 6, 0, 0, 0, 0, 12, 10,
          2, 6, 0, 0, 0, 0, 12, 8,
          1, 6, 0, 0, 0, 0, 12, 7,
          4, 6, 0, 0, 0, 0, 12, 10,
          5, 6, 0, 0, 0, 0, 12, 11,
          3, 6, 0, 0, 0, 0, 12, 9};
  public static int[] brett = {3, 6, 0, 0, 0, 0, 12, 9,
          5, 6, 0, 0, 0, 0, 12, 11,
          4, 6, 0, 0, 0, 0, 12, 10,
          2, 6, 0, 0, 0, 0, 12, 8,
          1, 6, 0, 0, 0, 0, 12, 7,
          4, 6, 0, 0, 0, 0, 12, 10,
          5, 6, 0, 0, 0, 0, 12, 11,
          3, 6, 0, 0, 0, 0, 12, 9
  };
  public static int amZug = 0;
  public static int moveCounter = 0;
  public static int movementRule = 0;
  public static boolean pawnMove = false;

  public static void main(String[] args) {
    writeToFile("log.txt", "Neues Game");
    clearFile("FEN.txt");
    GUI.GUI();
  }

  public static int[] move(int[] input, int pos1, int pos2) {
    if (input[pos1] == 6 || input[pos1] == 12) {
      pawnMove = true;
    }
    int[] anfang = Arrays.copyOf(input, input.length);
    if (input[pos1] != 0) {
      if (MovementCheck.wohinGehtBittiBitti(input, pos1, amZug).contains(pos2)) {
        if ((input[pos1] == 6 && (pos2 - 7) % 8 == 0) || (input[pos1] == 12 && pos2 % 8 == 0)) {
          if (!bot) {
            input[pos2] = promotion(input, pos1);
          } else {
            if (amZug == 0) {
              input[pos2] = 8;
            } else {
              input[pos2] = 2;
            }
          }
          input[pos1] = 0;
        } else {
          try {
            if ((input[pos1] == 6 && (((pos1 - 4) % 8 == 0 && input[pos1 - 7] == 0 && input[pos1 - 8] == 12 && letzte[pos1 - 6] == 12 && brett[pos1 - 6] == 0) || ((pos1 - 4) % 8 == 0 && input[pos1 + 9] == 0 && input[pos1 + 8] == 12 && letzte[pos1 + 10] == 12 && brett[pos1 + 10] == 0))) || (input[pos1] == 12 && (((pos1 - 3) % 8 == 0 && input[pos1 - 9] == 0 && input[pos1 - 8] == 6 && letzte[pos1 - 10] == 6 && input[pos1 + 10] == 0) || ((pos1 - 3) % 8 == 0 && input[pos1 + 7] == 0 && input[pos1 + 8] == 6 && letzte[pos1 + 6] == 6 && input[pos1 + 6] == 0)))) {//AAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
              if (input[pos1] == 6) {
                if ((pos1 - 4) % 8 == 0 && input[pos1 - 7] == 0 && input[pos1 - 8] == 12 && letzte[pos1 - 6] == 12 && brett[pos1 - 6] == 0) {
                  input[pos1 - 8] = 0;
                } else {
                  input[pos1 + 8] = 0;
                }
                input[pos2] = 6;
                input[pos1] = 0;
              } else {
                if ((pos1 - 3) % 8 == 0 && input[pos1 - 9] == 0 && input[pos1 - 8] == 6 && letzte[pos1 - 10] == 6 && input[pos1 + 10] == 0) {
                  System.out.println("test");
                  input[pos1 - 8] = 0;
                } else {
                  input[pos1 + 8] = 0;
                }
                input[pos2] = 12;
                input[pos1] = 0;
              }
            } else {
              if ((pos2 == pos1 + 16 || pos2 == pos1 - 16) && (input[pos1] == 7 || input[pos1] == 1)) {
                if (pos2 == pos1 + 16) {
                  input[pos1 + 8] = input[pos1 + 24];
                  input[pos1 + 24] = 0;
                  input[pos2] = input[pos1];
                  input[pos1] = 0;
                } else {
                  input[pos1 - 8] = input[pos1 - 32];
                  input[pos1 - 32] = 0;
                  input[pos2] = input[pos1];
                  input[pos1] = 0;
                }
              } else {
                input[pos2] = input[pos1];
                input[pos1] = 0;
              }
            }
          } catch (Exception ignored) {
          }
        }
      } else {
        richtig = false;
        System.out.println("Dieser Zug is nicht erlaubt(oder hab ich nicht eingebaut lol)");
        pawnMove = false;
      }
    } else {
      richtig = false;
      System.out.println("In diesem Feld steht keine Figur(skill issue)");
      pawnMove = false;
    }
    if (check(input, findKing(input, amZug), amZug)) {
      richtig = false;
      System.out.println("Du stehst wahrscheinlich im Schach(Würd mir stinken)");
      pawnMove = false;
      return anfang;
    } else {
      if (richtig) {
        if (input[pos2] == 9) {
          if (pos1 == 7) {
            hasMoved[1] = true;
          } else {
            hasMoved[2] = true;
          }
        }
        if (input[pos2] == 3) {
          if (pos1 == 0) {
            hasMoved[4] = true;
          } else {
            hasMoved[5] = true;
          }
        }
        if (input[pos2] == 1) {
          hasMoved[3] = true;
        }
        if (input[pos2] == 7) {
          hasMoved[0] = true;
        }
        System.out.println(Arrays.toString(hasMoved));
        moveCounter++;
        if (pieceAmount(anfang) > pieceAmount(input)) {
          movementRule = 0;
        } else {
          if (pawnMove) {
            movementRule = 0;
            pawnMove = false;
          } else {
            movementRule++;
          }
        }
      }
      return input;
    }
  }

  public static void output(int[] par_brett) {
    int l = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        switch (par_brett[l]) {

          case 0:
            if ((j + i) % 2 == 1) {
              System.out.print("□  ");
            } else {
              System.out.print("■  ");
            }
            break;
          case 1:
            System.out.print("♔ ");
            break;
          case 2:
            System.out.print("♕ ");
            break;
          case 3:
            System.out.print("♖ ");
            break;
          case 4:
            System.out.print("♗ ");
            break;
          case 5:
            System.out.print("♘ ");
            break;
          case 6:
            System.out.print("♙ ");
            break;
          case 7:
            System.out.print("♚ ");
            break;
          case 8:
            System.out.print("♛ ");
            break;
          case 9:
            System.out.print("♜ ");
            break;
          case 10:
            System.out.print("♝ ");
            break;
          case 11:
            System.out.print("♞ ");
            break;
          case 12:
            System.out.print("♟ ");
            break;

        }
        l += 8;
      }
      l -= 63;
      System.out.println(8 - i);
    }
    System.out.println("a  b  c  d  e  f  g  h");
  }

  public static int[] moveWithoutCheck(int[] input, int pos1, int pos2, int colour) {
    if (input[pos1] != 0) {
      if (MovementCheck.wohinGehtBittiBitti(input, pos1, colour).contains(pos2)) {
        if ((input[pos1] == 6 && (pos2 - 7) % 8 == 0) || (input[pos1] == 12 && pos2 % 8 == 0)) {
          input[pos2] = 13;
          input[pos1] = 0;
        } else {
          if ((input[pos1] == 6 && (pos2 == pos1 + 9 || pos2 == pos1 - 7)) || (input[pos1] == 12 && (pos2 == pos1 + 7 || pos2 == pos1 - 9))) {
            input[pos2] = input[pos1];
            if (input[pos1] == 6) {
              input[pos2 - 1] = 0;
            } else {
              input[pos2 + 1] = 0;
            }
            input[pos1] = 0;
          } else {
            input[pos2] = input[pos1];
            input[pos1] = 0;
          }
        }
      }
    }
    return input;
  }

  public static String zahlZuFeld(int input) {
    try {
      char a = "a8a7a6a5a4a3a2a1b8b7b6b5b4b3b2b1c8c7c6c5c4c3c2c1d8d7d6d5d4d3d2d1e8e7e6e5e4e3e2e1f8f7f6f5f4f3f2f1g8g7g6g5g4g3g2g1h8h7h6h5h4h3h2h1".charAt(input * 2);
      char b = "a8a7a6a5a4a3a2a1b8b7b6b5b4b3b2b1c8c7c6c5c4c3c2c1d8d7d6d5d4d3d2d1e8e7e6e5e4e3e2e1f8f7f6f5f4f3f2f1g8g7g6g5g4g3g2g1h8h7h6h5h4h3h2h1".charAt(input * 2 + 1);
      return String.valueOf(a) + String.valueOf(b);
    } catch (Exception ignored) {
      return "kp";
    }

  }

  public static int promotion(int[] input, int pos1) {
    /*
    int output = 0;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Promotion!");
    System.out.println("Was soll es werden? (Q,R,B,K)");
    String piece = br.readLine();
    if(input[pos1] == 6){
    switch (piece){
    case "Q":
    output = 2;
    break;
    case "R":
    output = 3;
    break;
    case "B":
    output = 4;
    break;
    case "K":
    output = 5;
    break;
    }
    }else {
    switch (piece){
    case "Q":
    output = 8;
    break;
    case "R":
    output = 9;
    break;
    case "B":
    output = 10;
    break;
    case "K":
    output = 11;
    break;
    }
    }
    return output;*/
    int output = 0;
    JOptionPane.showMessageDialog(null, "Promotion!", "Schachfiguren-Promotion", JOptionPane.INFORMATION_MESSAGE);
    String piece = (String) JOptionPane.showInputDialog(
            null,
            "Was soll es werden?",
            "Schachfiguren-Promotion",
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Queen", "Turm", "Läufer", "Springer"},
            "Queen"
    );
    if (input[pos1] == 6) {
      output = switch (piece) {
        case "Queen" -> 2;
        case "Turm" -> 3;
        case "Läufer" -> 4;
        case "Springer" -> 5;
        default -> output;
      };
    } else {
      output = switch (piece) {
        case "Queen" -> 8;
        case "Turm" -> 9;
        case "Läufer" -> 10;
        case "Springer" -> 11;
        default -> output;
      };
    }
    return output;
  }

  public static boolean check(int[] input, int pos, int farbe) {
    if (input.length == 64) {
      boolean schach = false;
      if (farbe == 0) { //springer check
        int[] springer = {-17, -15, -10, -6, 6, 10, 15, 17};
        for (int i = 0; i < springer.length; i++) {
          List<Integer> a = new ArrayList(Arrays.asList(-17, -10, 6, 15));
          List<Integer> b = new ArrayList(Arrays.asList(-10, 6));
          List<Integer> c = new ArrayList(Arrays.asList(17, 10, -6, -15));
          List<Integer> d = new ArrayList(Arrays.asList(10, -6));
          if ((pos % 8 == 0 && a.contains(springer[i])) || ((pos - 1) % 8 == 0 && b.contains(springer[i])) || ((pos - 7) % 8 == 0 && c.contains(springer[i])) || ((pos - 6) % 8 == 0 && d.contains(springer[i]))) {
          } else {
            try {
              if (input[pos + springer[i]] == 5) {
                schach = true;
              }
            } catch (Exception ignored) {
            }
          }
        }//läufer check
        //System.out.println("Springer:" + schach);
        if (!schach) {
          boolean wbishop_b = true;
          int wbishop = 0;
          int[] wlimits = {-7, -7, 0, 0};
          int[] wbewegung = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            wbishop_b = true;
            wbishop = 0;
            if ((pos + wbishop + wlimits[i]) % 8 != 0) {
              wbishop = wbishop + wbewegung[i];
            }
            while (wbishop_b) {   //Algorithmus zur Ausrechnung der Felder des Bischofs
              if (pos + wbishop < 0 || pos + wbishop > 63) {
                break;
              }
              try {
                if ((pos + wbishop + wlimits[i]) % 8 == 0 || input[pos + wbishop] != 0) {
                  if (input[pos + wbishop] == 4) {
                    schach = true;
                    break;
                  }
                  wbishop_b = false;
                }
                wbishop = wbishop + wbewegung[i];
              } catch (Exception ignored) {
              }
            }
          }
        }// turm
        //System.out.println("Läufer:" + schach);
        if (!schach) {
          boolean wrook_b = true;
          int wrook = 0;
          int[] wlimits_r = {-7, 0};
          int[] wbewegung_r = {1, -1};
          for (int i = 0; i < 2; i++) {
            wrook_b = true;
            if ((pos + wrook + wlimits_r[i]) % 8 != 0) {
              wrook = wbewegung_r[i];
            } else {
              wrook = 0;
            }
            while (wrook_b) {
              if (pos + wrook < 0 || pos + wrook > 63) {
                break;
              }
              if ((pos + wrook + wlimits_r[i]) % 8 == 0 || input[pos + wrook] != 0) {
                if (input[pos + wrook] == 3) {
                  schach = true;
                }
                wrook_b = false;
              }
              wrook = wrook + wbewegung_r[i];
            }
          }
          wrook = 0;
          try {
            do {
              wrook = wrook + 8;
              if (wrook + pos <= 64 && input[pos + wrook] == 3) {
                schach = true;
              }
            } while (wrook + pos <= 64 && input[pos + wrook] == 0);
          } catch (Exception ignored) {
          }
          wrook = 0;
          try {
            do {
              wrook = wrook - 8;
              if (wrook + pos >= 0 && input[pos + wrook] == 3) {
                schach = true;
              }
            } while (wrook + pos >= 0 && input[pos + wrook] == 0);
          } catch (Exception ignored) {
          }
        }// könig
        //System.out.println("Turm:" + schach);
        if (!schach) {
          int[] wbewegung_k = {-9, -8, -7, -1, 1, 7, 8, 9};
          int[] wlimit_k = {0, 9, -7, 0, -7, 0, 9, -7};
          boolean[] wkein_limit_k = {false, true, false, false, false, false, true, false};
          for (int i = 0; i < 8; i++) {
            if (wkein_limit_k[i]) {
              try {
                if (input[pos + wbewegung_k[i]] == 1) {
                  schach = true;
                }
              } catch (Exception ignored) {
              }
            } else {
              if ((pos + wlimit_k[i]) % 8 != 0) {
                try {
                  if (input[pos + wbewegung_k[i]] == 1) {
                    schach = true;
                  }
                } catch (Exception ignored) {
                }
              }
            }
          }
        }//bauer
        //System.out.println("König:" + schach);
        if (!schach) {
          try {
            if (input[pos - 9] == 6) {
              schach = true;
            }
          } catch (Exception ignored) {
          }
          try {
            if (input[pos + 7] == 6) {
              schach = true;
            }
          } catch (Exception ignored) {
          }
        }//Queen
        //System.out.println("Bauer:" + schach);
        if (!schach) {
          boolean wdame_b = true;
          int wdame = 0;
          int[] wlimits_d = {-7, -7, 0, 0};
          int[] wbewegung_d = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            wdame_b = true;
            wdame = 0;
            if ((pos + wdame + wlimits_d[i]) % 8 != 0) {
              wdame = wdame + wbewegung_d[i];
            }
            while (wdame_b) {   //Algorithmus zur Ausrachnung der Felder des Bischofs
              if (pos + wdame < 0 || pos + wdame > 63) {
                break;
              }
              if ((pos + wdame + wlimits_d[i]) % 8 == 0 || input[pos + wdame] != 0) {
                if (input[pos + wdame] == 2) {
                  schach = true;
                }
                wdame_b = false;
              }
              wdame = wdame + wbewegung_d[i];
            }
          }
          boolean wqueen_b = true;
          int wqueen = 0;
          int[] wlimits_q = {-7, 0};
          int[] wbewegung_q = {1, -1};
          for (int i = 0; i < 2; i++) {
            wqueen_b = true;
            wqueen = 0;
            if ((pos + wqueen + wlimits_q[i]) % 8 != 0) {
              wqueen = wqueen + wbewegung_q[i];
            }
            while (wqueen_b) {
              try {
                if (pos + wqueen < 0 || pos + wqueen > 63) {
                  break;
                }
                if ((pos + wqueen + wlimits_q[i]) % 8 == 0 || input[pos + wqueen] != 0) {
                  if (input[pos + wqueen] == 2) {
                    schach = true;
                  }
                  wqueen_b = false;
                }
                wqueen = wqueen + wbewegung_q[i];
              } catch (Exception ignored) {
              }
            }
          }
          wqueen = 0;
          try {
            do {
              wqueen = wqueen + 8;
              if (input[pos + wqueen] == 2) {
                schach = true;
              }
            } while (wqueen + pos <= 64 && input[pos + wqueen] == 0);
          } catch (Exception ignored) {
          }
          wqueen = 0;
          try {
            do {
              wqueen = wqueen - 8;
              if (input[pos + wqueen] == 2) {
                schach = true;
              }
            } while (wqueen + pos >= 0 && input[pos + wqueen] == 0);
          } catch (Exception ignored) {
          }
        }
        //System.out.println("Queen:" + schach);
      } else {
        int[] springer = {-17, -15, -10, -6, 6, 10, 15, 17};
        for (int i = 0; i < springer.length; i++) {
          List<Integer> a = new ArrayList(Arrays.asList(-17, -10, 6, 15));
          List<Integer> b = new ArrayList(Arrays.asList(-10, 6));
          List<Integer> c = new ArrayList(Arrays.asList(17, 10, -6, -15));
          List<Integer> d = new ArrayList(Arrays.asList(10, -6));
          if ((pos % 8 == 0 && a.contains(springer[i])) || ((pos - 1) % 8 == 0 && b.contains(springer[i])) || ((pos - 7) % 8 == 0 && c.contains(springer[i])) || ((pos - 6) % 8 == 0 && d.contains(springer[i]))) {
          } else {
            try {
              if (input[pos + springer[i]] == 11) {
                schach = true;
              }
            } catch (Exception ignored) {
            }
          }
        }//läufer check
        //System.out.println("Springer:" + schach);
        if (!schach) {
          boolean wbishop_b = true;
          int wbishop = 0;
          int[] wlimits = {-7, -7, 0, 0};
          int[] wbewegung = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            wbishop_b = true;
            wbishop = 0;
            if ((pos + wbishop + wlimits[i]) % 8 != 0) {
              wbishop = wbishop + wbewegung[i];
            }
            while (wbishop_b) {   //Algorithmus zur Ausrechnung der Felder des Bischofs
              if (pos + wbishop < 0 || pos + wbishop > 63) {
                break;
              }
              if ((pos + wbishop + wlimits[i]) % 8 == 0 || input[pos + wbishop] != 0) {
                if (input[pos + wbishop] == 10) {
                  schach = true;
                  break;
                }
                wbishop_b = false;
              }
              wbishop = wbishop + wbewegung[i];
            }
          }
        }// turm
        //System.out.println("Läufer:" + schach);
        if (!schach) {
          boolean wrook_b = true;
          int wrook = 0;
          int[] wlimits_r = {-7, 0};
          int[] wbewegung_r = {1, -1};
          for (int i = 0; i < 2; i++) {
            wrook_b = true;
            if ((pos + wrook + wlimits_r[i]) % 8 != 0) {
              wrook = wbewegung_r[i];
            } else {
              wrook = 0;
            }
            while (wrook_b) {
              if (pos + wrook < 0 || pos + wrook > 63) {
                break;
              }
              if ((pos + wrook + wlimits_r[i]) % 8 == 0 || input[pos + wrook] != 0) {
                if (input[pos + wrook] == 9) {
                  schach = true;
                }
                wrook_b = false;
              }
              wrook = wrook + wbewegung_r[i];
            }
          }
          wrook = 0;
          try {
            do {
              wrook = wrook + 8;
              if (wrook + pos <= 64 && input[pos + wrook] == 9) {
                schach = true;
              }
            } while (wrook + pos <= 64 && input[pos + wrook] == 0);
          } catch (Exception ignored) {
          }
          wrook = 0;
          try {
            do {
              wrook = wrook - 8;
              if (wrook + pos >= 0 && input[pos + wrook] == 9) {
                schach = true;
              }
            } while (wrook + pos >= 0 && input[pos + wrook] == 0);
          } catch (Exception ignored) {
          }
        }// könig
        //System.out.println("Turm:" + schach);
        if (!schach) {
          int[] wbewegung_k = {-9, -8, -7, -1, 1, 7, 8, 9};
          int[] wlimit_k = {0, 9, -7, 0, -7, 0, 9, -7};
          boolean[] wkein_limit_k = {false, true, false, false, false, false, true, false};
          for (int i = 0; i < 8; i++) {
            if (wkein_limit_k[i]) {
              try {
                if (input[pos + wbewegung_k[i]] == 7) {
                  schach = true;
                }
              } catch (Exception ignored) {
              }
            } else {
              if ((pos + wlimit_k[i]) % 8 != 0) {
                try {
                  if (input[pos + wbewegung_k[i]] == 7) {
                    schach = true;
                  }
                } catch (Exception ignored) {
                }
              }
            }
          }
        }//bauer
        //System.out.println("König:" + schach);
        if (!schach) {
          try {
            if (input[pos - 7] == 12) {
              schach = true;
            }
          } catch (Exception ignored) {
          }
          try {
            if (input[pos + 9] == 12) {
              schach = true;
            }
          } catch (Exception ignored) {
          }
        }// Queen
        //System.out.println("Bauer:" + schach);
        if (!schach) {
          boolean wdame_b = true;
          int wdame = 0;
          int[] wlimits_d = {-7, -7, 0, 0};
          int[] wbewegung_d = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            wdame_b = true;
            wdame = 0;
            if ((pos + wdame + wlimits_d[i]) % 8 != 0) {
              wdame = wdame + wbewegung_d[i];
            }
            while (wdame_b) {   //Algorithmus zur Ausrachnung der Felder des Bischofs
              if (pos + wdame < 0 || pos + wdame > 63) {
                break;
              }
              if ((pos + wdame + wlimits_d[i]) % 8 == 0 || input[pos + wdame] != 0) {
                if (input[pos + wdame] == 8) {
                  schach = true;
                }
                wdame_b = false;
              }
              wdame = wdame + wbewegung_d[i];
            }
          }
          boolean wqueen_b = true;
          int wqueen = 0;
          int[] wlimits_q = {-7, 0};
          int[] wbewegung_q = {1, -1};
          for (int i = 0; i < 2; i++) {
            wqueen_b = true;
            wqueen = 0;
            if ((pos + wqueen + wlimits_q[i]) % 8 != 0) {
              wqueen = wqueen + wbewegung_q[i];
            }
            while (wqueen_b) {
              if (pos + wqueen < 0 || pos + wqueen > 63) {
                break;
              }
              if ((pos + wqueen + wlimits_q[i]) % 8 == 0 || input[pos + wqueen] != 0) {
                if (input[pos + wqueen] == 8) {
                  schach = true;
                }
                wqueen_b = false;
              }
              wqueen = wqueen + wbewegung_q[i];
            }
          }
          wqueen = 0;
          try {
            do {
              wqueen = wqueen + 8;
              if (wqueen + pos <= 64 && input[pos + wqueen] == 8) {
                schach = true;
              }
            } while (wqueen + pos <= 64 && input[pos + wqueen] == 0);
          } catch (Exception ignored) {
          }
          wqueen = 0;
          try {
            do {
              wqueen = wqueen - 8;
              if (wqueen + pos >= 0 && input[pos + wqueen] == 8) {
                schach = true;
              }
            } while (wqueen + pos >= 0 && input[pos + wqueen] == 0);
          } catch (Exception ignored) {
          }
        }
        //System.out.println("Queen:" + schach);
      }
      return schach;
    } else {
      System.out.println("Kein Schachfeld!");
      return true;
    }
  }

  public static int findKing(int[] input, int farbe) {
    int pos = -1;
    for (int i = 0; i < 64; i++) {
      if (input[i] == 1 && farbe == 1) {
        pos = i;
        break;
      }
      if (input[i] == 7 && farbe == 0) {
        pos = i;
        break;
      }
    }
    return pos;
  }

  public static boolean mattTest(int[] input, int colour) {
    int[] originalBoard = Arrays.copyOf(input, input.length); // Originale Brettkopie
    boolean isCheckmate = true;
    for (int i = 0; i < 64; i++) {
      if (input[i] != 0 && findColour(input, i) == colour) { // Prüfen, ob eine Figur vorhanden ist
        List<Integer> possibilities = MovementCheck.wohinGehtBittiBitti(input, i, amZug); // Mögliche Züge für diese Figur
        for (int j = 0; j < possibilities.size(); j++) {
          input = Arrays.copyOf(originalBoard, originalBoard.length);
          moveWithoutCheck(input, i, possibilities.get(j), amZug);
          if (!check(input, findKing(input, colour), colour)) {
            isCheckmate = false;
            break;
          }
        }

      }
      if (!isCheckmate) {
        break; // Keine Notwendigkeit, weiter zu prüfen, wenn nicht Matt
      }
    }
    return isCheckmate;
  }

  public static int findColour(int[] input, int pos) {
    return switch (input[pos]) {
      case 1, 2, 3, 4, 5, 6 -> 1;
      case 7, 8, 9, 10, 11, 12 -> 0;
      case 0 -> 3;
      default -> -1;
    };
  }

  public static boolean stalemateTest(int[] input, int pos) {
    int[] boardCopy = Arrays.copyOf(input, input.length);
    int colour = findColour(boardCopy, pos);
    int kingPosition = findKing(boardCopy, colour);
    if (check(boardCopy, kingPosition, colour)) {
      return false;
    }
    for (int i = 0; i < 64; i++) {
      if (!MovementCheck.wohinGehtBittiBitti(boardCopy, i, amZug).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public static String figur(int[] input, int pos) {
    String ende = "Leer";
    switch (input[pos]) {
      case 0:
        if ((pos >= 8 && pos < 16) || (pos >= 24 && pos < 32) || (pos >= 40 && pos < 48) || pos >= 56) {
          pos++;
        }
        if (pos % 2 == 1) {
          ende = " ";
        } else {
          ende = " ";
        }
        break;
      case 1:
        ende = "♚";
        break;
      case 2:
        ende = "♛";
        break;
      case 3:
        ende = "♜";
        break;
      case 4:
        ende = "♝";
        break;
      case 5:
        ende = "♞";
        break;
      case 6:
        ende = "♟";
        break;
      case 7:
        ende = "♔";
        break;
      case 8:
        ende = "♕";
        break;
      case 9:
        ende = "♖";
        break;
      case 10:
        ende = "♗";
        break;
      case 11:
        ende = "♘";
        break;
      case 12:
        ende = "♙";
    }
    return ende;
  }

  public static void writeToFile(String fileName, String content) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(fileName, true);
      writer.write(content + System.lineSeparator());
    } catch (IOException ignored) {
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException ignored) {
      }
    }
  }

  public static String getFEN() {
    StringBuilder fen = new StringBuilder();
    int l = 0;
    for (int i = 0; i < 8; i++) {
      int emptySquares = 0;
      for (int j = 0; j < 8; j++) {
        char piece = switch (brett[l]) {
          case 1 -> 'k';
          case 2 -> 'q';
          case 3 -> 'r';
          case 4 -> 'b';
          case 5 -> 'n';
          case 6 -> 'p';
          case 7 -> 'K';
          case 8 -> 'Q';
          case 9 -> 'R';
          case 10 -> 'B';
          case 11 -> 'N';
          case 12 -> 'P';
          default -> ' ';
        };
        l += 8;
        if (piece == ' ') {
          emptySquares++;
        } else {
          if (emptySquares > 0) {
            fen.append(emptySquares);
            emptySquares = 0;
          }
          fen.append(piece);
        }
      }
      if (emptySquares > 0) {
        fen.append(emptySquares);
      }
      if (i < 7) {
        fen.append('/');
      }
      l -= 63;
    }
    if (amZug == 0) {
      fen.append(" ").append("w");
    } else {
      fen.append(" ").append("b");
    }
    fen.append(" ");
    if (!hasMoved[0] || !hasMoved[3]) {
      if (!hasMoved[0] && !hasMoved[1]) {
        fen.append("Q");
      }
      if (!hasMoved[0] && !hasMoved[2]) {
        fen.append("K");
      }
      if (!hasMoved[3] && !hasMoved[4]) {
        fen.append("q");
      }
      if (!hasMoved[3] && !hasMoved[5]) {
        fen.append("k");
      }
    } else {
      fen.append("-");
    }
    fen.append(" ");
    if (enPassant() != -1) {
      fen.append(zahlZuFeld(enPassant()));
    } else {
      fen.append("-");
    }
    fen.append(" ").append(movementRule);
    fen.append(" ").append(moveCounter / 2);
    return fen.toString();
  }

  public static int enPassant() {
    for (int i = 0; i < 8; i++) {
      if (brett[i * 8 + 3] == 6 && letzte[i * 8 + 1] == 6 && brett[i * 8 + 1] == 0 && letzte[i * 8 + 3] == 0) {
        return i * 8 + 2;
      }
    }
    for (int i = 0; i < 8; i++) {
      if (brett[i * 8 + 4] == 12 && letzte[i * 8 + 6] == 12 && brett[i * 8 + 6] == 0 && letzte[i * 8 + 4] == 0) {
        return i * 8 + 5;
      }
    }
    return -1;
  }

  public static int pieceAmount(int[] input) {
    int amount = 0;
    for (int i = 0; i < 64; i++) {
      if (input[i] != 0) {
        amount++;
      }
    }
    return amount;
  }

  public static void clearFile(String fileName) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(fileName, false);
      writer.write("");
    } catch (IOException ignored) {
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException ignored) {
      }
    }
  }
  public static int countOccurrences(String fileName, String searchString) {
    BufferedReader reader = null;
    int count = 0;  // Counter for occurrences
    try {
      // Initialize BufferedReader to read the file
      reader = new BufferedReader(new FileReader(fileName));
      String line;

      // Read the file line by line
      while ((line = reader.readLine()) != null) {
        // While the line contains the search string, count occurrences
        int index = 0;
        while ((index = line.indexOf(searchString, index)) != -1) {
          count++;  // Increment count when the string is found
          index += searchString.length();  // Move index forward
        }
      }
    } catch (IOException e) {
      // Handle possible IOException
      System.out.println("An error occurred while reading the file.");
      e.printStackTrace();
    } finally {
      // Close the BufferedReader to release resources
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        System.out.println("Failed to close the reader.");
        e.printStackTrace();
      }
    }
    return count;  // Return the total number of occurrences
  }
}