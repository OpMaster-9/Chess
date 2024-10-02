import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovementCheck {
  public static boolean[] hasMoved = {false,false,false,false,false,false};
  public static List<Integer> wohinGehtBittiBitti(int[] input, int pos1, int colour){
    List<Integer> wohin = new ArrayList<Integer>();
    int[] springer = {-17,-15,-10,-6,6,10,15,17};
    switch (input[pos1]) {
      
      case 6:
        if (colour == 1) {
          if (input[pos1 + 1] == 0) {
            wohin.add(pos1 + 1);
          }
          if ((pos1 - 1) % 8 == 0 && input[pos1 + 1] == 0 && input[pos1 + 2] == 0) {
            wohin.add(pos1 + 2);
          }
          try {
            if (input[pos1 + 9] > 6) {
              wohin.add(pos1 + 9);
            } else {
              if ((pos1 - 4) % 8 == 0 && input[pos1 + 9] == 0 && input[pos1 + 8] == 12 && Main.letzte[pos1 + 10] == 12 && Main.brett[pos1 + 10] == 0) {
                wohin.add(pos1 + 9);
              }
            }
          } catch (Exception ignored) {
          }
          try {
            if (input[pos1 - 7] != 0 && input[pos1 - 7] > 6) {
              wohin.add(pos1 - 7);
            } else {
              if ((pos1 - 4) % 8 == 0 && input[pos1 - 7] == 0 && input[pos1 - 8] == 12 && Main.letzte[pos1 - 6] == 12 && Main.brett[pos1 - 6] == 0) {
                wohin.add(pos1 - 7);
              }
            }
          } catch (Exception ignored) {
          }
        }
        break;
      case 12:
        if (colour == 0) {
          if (input[pos1 - 1] == 0) {
            wohin.add(pos1 - 1);
          }
          if ((pos1 - 6) % 8 == 0 && input[pos1 - 1] == 0 && input[pos1 - 2] == 0) {
            wohin.add(pos1 - 2);
          }
          try {
            if (input[pos1 + 7] != 0 && input[pos1 + 7] < 7) {
              wohin.add(pos1 + 7);
            } else {
              if ((pos1 - 3) % 8 == 0 && input[pos1 + 7] == 0 && input[pos1 + 8] == 6 && Main.letzte[pos1 + 6] == 6 && input[pos1 + 6] == 0) {
                wohin.add(pos1 + 7);
              }
            }
          } catch (Exception ignored) {
          }
          try {
            if (input[pos1 - 9] != 0 && input[pos1 - 9] < 7) {
              wohin.add(pos1 - 9);
            } else {
              if ((pos1 - 3) % 8 == 0 && input[pos1 - 9] == 0 && input[pos1 - 8] == 6 && Main.letzte[pos1 - 10] == 6 && input[pos1 + 10] == 0){
                wohin.add(pos1 - 9);
              }
            }
          } catch (Exception ignored) {
          }
        }
        break;
      case 5:
        if (colour == 1) {
          for (int i = 0; i < springer.length; i++) {
            try {
              if (input[pos1 + springer[i]] == 0 || input[pos1 + springer[i]] > 6) {
                List<Integer> a = new ArrayList(Arrays.asList(-17, -10, 6, 15));
                List<Integer> b = new ArrayList(Arrays.asList(-10, 6));
                List<Integer> c = new ArrayList(Arrays.asList(17, 10, -6, -15));
                List<Integer> d = new ArrayList(Arrays.asList(10, -6));
                if ((pos1 % 8 == 0 && a.contains(springer[i])) || ((pos1 - 1) % 8 == 0 && b.contains(springer[i])) || ((pos1 - 7) % 8 == 0 && c.contains(springer[i])) || ((pos1 - 6) % 8 == 0 && d.contains(springer[i]))) {
                } else {
                  wohin.add(pos1 + springer[i]);
                }
              }
            } catch (IndexOutOfBoundsException ignored) {
            }
          }
        }
        break;
      case 11:
        if (colour == 0) {
          for (int i = 0; i < springer.length; i++) {
            try {
              if (input[pos1 + springer[i]] < 7) {
                List<Integer> a = new ArrayList(Arrays.asList(-17, -10, 6, 15));
                List<Integer> b = new ArrayList(Arrays.asList(-10, 6));
                List<Integer> c = new ArrayList(Arrays.asList(17, 10, -6, -15));
                List<Integer> d = new ArrayList(Arrays.asList(10, -6));
                if ((pos1 % 8 == 0 && a.contains(springer[i])) || ((pos1 - 1) % 8 == 0 && b.contains(springer[i])) || ((pos1 - 7) % 8 == 0 && c.contains(springer[i])) || ((pos1 - 6) % 8 == 0 && d.contains(springer[i]))) {
                } else {
                  wohin.add(pos1 + springer[i]);
                }
              }
            } catch (IndexOutOfBoundsException ignored) {
            }
          }
        }
        break;
      case 3:
        if (colour == 1) {
          boolean rook_b = true;
          int rook = 0;
          int[] limits_r = {-7, 0};
          int[] bewegung_r = {1, -1};
          for (int i = 0; i < 2; i++) {
            rook_b = true;
            rook = 0;
            while (rook_b) {
              if ((pos1 + rook + limits_r[i]) % 8 != 0) {
                rook = rook + bewegung_r[i];
              }
                if (pos1 + rook < 0 || pos1 + rook > 63) {
                  break;
                }
                if ((pos1 + rook + limits_r[i]) % 8 == 0 || input[pos1 + rook] != 0) {
                  if (input[pos1 + rook] > 6 || input[pos1 + rook] == 0) {
                    wohin.add(pos1 + rook);
                  }
                  rook_b = false;
                } else {
                  wohin.add(pos1 + rook);
                }
            }
          }
          rook = 0;
          try {
            do {
              rook = rook + 8;
              if (rook + pos1 <= 64 && (input[pos1 + rook] > 6 || input[pos1 + rook] == 0)) {
                wohin.add(pos1 + rook);
              }
            } while (rook + pos1 <= 64 && input[pos1 + rook] == 0);
          } catch (Exception ignored) {
          }
          rook = 0;
          try {
            do {
              rook = rook - 8;
              if (rook + pos1 >= 0 && (input[pos1 + rook] > 6 || input[pos1 + rook] == 0)) {
                wohin.add(pos1 + rook);
              }
            } while (rook + pos1 >= 0 && input[pos1 + rook] == 0);
          } catch (Exception ignored) {
          }
        }
        break; //Hahahahah Gelgus
      case 9:
        if (colour == 0) {
          boolean wrook_b = true;
          int wrook = 0;
          int[] wlimits_r = {-7, 0};
          int[] wbewegung_r = {1, -1};
          for (int i = 0; i < 2; i++) {
            wrook_b = true;
            wrook = 0;
            while (wrook_b) {
              if ((pos1 + wrook + wlimits_r[i]) % 8 != 0) {
                wrook = wrook + wbewegung_r[i];
              }
                if (pos1 + wrook < 0 || pos1 + wrook > 63) {
                  break;
                }
                if ((pos1 + wrook + wlimits_r[i]) % 8 == 0 || input[pos1 + wrook] != 0) {
                  if (input[pos1 + wrook] < 7 || input[pos1 + wrook] == 0) {
                    wohin.add(pos1 + wrook);
                  }
                  wrook_b = false;
                } else {
                  wohin.add(pos1 + wrook);
                }
            }
          }
          wrook = 0;
          try {
            do {
              wrook = wrook + 8;
              if (wrook + pos1 <= 64 && input[pos1 + wrook] < 7) {
                wohin.add(pos1 + wrook);
              }
            } while (wrook + pos1 <= 64 && input[pos1 + wrook] == 0);
          } catch (Exception ignored) {
          }
          wrook = 0;
          try {
            do {
              wrook = wrook - 8;
              if (wrook + pos1 >= 0 && input[pos1 + wrook] < 7) {
                wohin.add(pos1 + wrook);
              }
            } while (wrook + pos1 >= 0 && input[pos1 + wrook] == 0);
          } catch (Exception ignored) {
          }
        }
        break;
      case 4:  //TAUBEN SIND NICHT ECHT
        if (colour == 1) {
          boolean bishop_b = true;
          int bishop = 0;
          int[] limits_b = {-7, -7, 0, 0};
          int[] bewegung_b = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            bishop_b = true;
            bishop = 0;
            if ((pos1 + bishop + limits_b[i]) % 8 != 0) {
              bishop = bishop + bewegung_b[i];
            }
            while (bishop_b) {   //Algorithmus zur Ausrachnung der Felder des Bischofs
                if (pos1 + bishop < 0 || pos1 + bishop > 63) {
                  break;
                }
                if ((pos1 + bishop + limits_b[i]) % 8 == 0 || input[pos1 + bishop] != 0) {
                  if (input[pos1 + bishop] > 6 || input[pos1 + bishop] == 0) {
                    wohin.add(pos1 + bishop);
                  }
                  bishop_b = false;
                } else {
                  wohin.add(pos1 + bishop);
                }
                bishop = bishop + bewegung_b[i];
            }
          }
        }
        break;
      case 10:
        if (colour == 0) {
          boolean wbishop_b = true;
          int wbishop = 0;
          int[] wlimits = {-7, -7, 0, 0};
          int[] wbewegung = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            wbishop_b = true;
            wbishop = 0;
            if ((pos1 + wbishop + wlimits[i]) % 8 != 0) {
              wbishop = wbishop + wbewegung[i];
            }
            while (wbishop_b) {   //Algorithmus zur Ausrechnung der Felder des Bischofs
                if (pos1 + wbishop < 0 || pos1 + wbishop > 63) {
                  break;
                }
                if ((pos1 + wbishop + wlimits[i]) % 8 == 0 || input[pos1 + wbishop] != 0) {
                  try {
                    if (input[pos1 + wbishop] < 7 || input[pos1 + wbishop] == 0) {
                      wohin.add(pos1 + wbishop);
                    }
                  } catch (Exception ignored) {
                  }
                  wbishop_b = false;
                } else {
                  wohin.add(pos1 + wbishop);
                }
                wbishop = wbishop + wbewegung[i];
            }
          }
        }
        break;
      case 2:
        if (colour == 1) {
          boolean dame_b = true;
          int dame = 0;
          int[] limits_d = {-7, -7, 0, 0};
          int[] bewegung_d = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            dame_b = true;
            dame = 0;
            if ((pos1 + dame + limits_d[i]) % 8 != 0) {
              dame = dame + bewegung_d[i];
            }
            while (dame_b) {   //Algorithmus zur Ausrachnung der Felder des Bischofs
              if (pos1 + dame < 0 || pos1 + dame > 63) {
                break;
              }
              if ((pos1 + dame + limits_d[i]) % 8 == 0 || input[pos1 + dame] != 0) {
                  if (input[pos1 + dame] > 6 || input[pos1 + dame] == 0) {
                    wohin.add(pos1 + dame);
                  }
                dame_b = false;
              } else {
                wohin.add(pos1 + dame);
              }
              dame = dame + bewegung_d[i];
            }
          }
          boolean queen_b = true;
          int queen = 0;
          int[] limits_q = {-7, 0};
          int[] bewegung_q = {1, -1};
          for (int i = 0; i < 2; i++) {
            queen_b = true;
            queen = 0;
            while (queen_b) {
              if ((pos1 + queen + limits_q[i]) % 8 != 0){
                queen = queen + bewegung_q[i];
              }
                if(pos1 + queen < 0 || pos1 + queen > 63){
                  break;
                }
                if ((pos1 + queen + limits_q[i]) % 8 == 0 || input[pos1 + queen] != 0) {
                  if (input[pos1 + queen] > 6 || input[pos1 + queen] == 0) {
                    wohin.add(pos1 + queen);
                  }
                  queen_b = false;
                } else {
                  wohin.add(pos1 + queen);
                }
            }
          }
          queen = 0;
          try {
            do {
              queen = queen + 8;
              if (queen + pos1 <= 64 && input[pos1 + queen] > 6) {
                wohin.add(pos1 + queen);
              }
            } while (queen + pos1 <= 64 && input[pos1 + queen] == 0);
          } catch (Exception ignored) {
          }
          queen = 0;
          try {
            do {
              queen = queen - 8;
              if (queen + pos1 >= 0 && input[pos1 + queen] > 6) {
                wohin.add(pos1 + queen);
              }
            } while (queen + pos1 >= 0 && input[pos1 + queen] == 0);
          } catch (Exception ignored) {
          }
        }
        break;
      case 8:
        if (colour == 0) {
          boolean wdame_b = true;
          int wdame = 0;
          int[] wlimits_d = {-7, -7, 0, 0};
          int[] wbewegung_d = {9, -7, -9, 7};
          for (int i = 0; i < 4; i++) {
            wdame_b = true;
            wdame = 0;
            if ((pos1 + wdame + wlimits_d[i]) % 8 != 0) {
              wdame = wdame + wbewegung_d[i];
            }
            while (wdame_b) {   //Algorithmus zur Ausrachnung der Felder des Bischofs
              if (pos1 + wdame < 0 || pos1 + wdame > 63) {
                break;
              }
              if ((pos1 + wdame + wlimits_d[i]) % 8 == 0 || input[pos1 + wdame] != 0) {
                  if (input[pos1 + wdame] < 7 || input[pos1 + wdame] == 0) {
                    wohin.add(pos1 + wdame);
                  }
                wdame_b = false;
              } else {
                wohin.add(pos1 + wdame);
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
            while (wqueen_b) {
              if ((pos1 + wqueen + wlimits_q[i]) % 8 != 0) {
                wqueen = wqueen + wbewegung_q[i];
              }
                if (pos1 + wqueen < 0 || pos1 + wqueen > 63) {
                  break;
                }
                if ((pos1 + wqueen + wlimits_q[i]) % 8 == 0 || input[pos1 + wqueen] != 0) {
                  if (input[pos1 + wqueen] < 7 || input[pos1 + wqueen] == 0) {
                    wohin.add(pos1 + wqueen);
                  }
                  wqueen_b = false;
                } else {
                  wohin.add(pos1 + wqueen);
                }
            }
          }
          wqueen = 0;
          try {
            do {
              wqueen = wqueen + 8;
              if (wqueen + pos1 <= 64 && input[pos1 + wqueen] < 7) {
                wohin.add(pos1 + wqueen);
              }
            } while (wqueen + pos1 <= 64 && input[pos1 + wqueen] == 0);
          } catch (Exception ignored) {
          }
          wqueen = 0;
          try {
            do {
              wqueen = wqueen - 8;
              if (wqueen + pos1 >= 0 && input[pos1 + wqueen] < 7) {
                wohin.add(pos1 + wqueen);
              }
            } while (wqueen + pos1 >= 0 && input[pos1 + wqueen] == 0);
          } catch (Exception ignored) {
          }
        }
        break;
      case 1:
        if (colour == 1) {
          int[] bewegung_k = {-9, -8, -7, -1, 1, 7, 8, 9};
          int[] limit_k = {0, 9, -7, 0, -7, 0, 9, -7};
          boolean[] kein_limit_k = {false, true, false, false, false, false, true, false};
          for (int i = 0; i < 8; i++) {
            if (kein_limit_k[i]) {
              try {
                if (input[pos1 + bewegung_k[i]] == 0) {
                  wohin.add(pos1 + bewegung_k[i]);
                } else {
                  if (input[pos1 + bewegung_k[i]] > 6) {
                    wohin.add(pos1 + bewegung_k[i]);
                  }
                }
              }catch (Exception ignored){}
            } else {
              if ((pos1 + limit_k[i]) % 8 != 0) {
                try {
                  if (input[pos1 + bewegung_k[i]] == 0 || input[pos1 + bewegung_k[i]] > 6) {
                    wohin.add(pos1 + bewegung_k[i]);
                  }
                }catch (Exception ignored){}
              }
            }
          }
          try {
            if ((!hasMoved[3] && !hasMoved[4]) && (input[pos1 - 8] == 0 && input[pos1 - 16] == 0 && input[pos1 - 24] == 0) && !(Main.check(input, pos1, colour) && Main.check(input, pos1 - 8, colour) && Main.check(input, pos1 - 16, colour))) { //AAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHH
              if ((Main.check(input, pos1, 0) && Main.check(input, pos1 - 8, 0) && Main.check(input, pos1 - 16, 0))) {
              } else {
                wohin.add(pos1 - 16);
              }
            }
          }catch (Exception ignored){}
          try {
            if ((!hasMoved[3] && !hasMoved[5]) && (input[pos1 + 8] == 0 && input[pos1 + 16] == 0) && !(Main.check(input, pos1, 1) && Main.check(input, pos1 + 8, 1) && Main.check(input, pos1 + 16, 1))) { //GELGUS
              if ((Main.check(input, pos1, 0) && Main.check(input, pos1 + 8, 0) && Main.check(input, pos1 + 16, 0))) {
              } else {
                wohin.add(pos1 + 16);
              }
            }
          }catch (Exception ignored){}
        }
        break;
      case 7:
        if (colour == 0) {
          int[] wbewegung_k = {-9, -8, -7, -1, 1, 7, 8, 9};
          int[] wlimit_k = {0, 9, -7, 0, -7, 0, 9, -7};
          boolean[] wkein_limit_k = {false, true, false, false, false, false, true, false};
          for (int i = 0; i < 8; i++) {
            if (wkein_limit_k[i]) {
              try {
                if (input[pos1 + wbewegung_k[i]] == 0) {
                  wohin.add(pos1 + wbewegung_k[i]);
                } else {
                  if (input[pos1 + wbewegung_k[i]] < 7) {
                    wohin.add(pos1 + wbewegung_k[i]);
                  }
                }
              }catch (Exception ignored){}
            } else {
              if ((pos1 + wlimit_k[i]) % 8 != 0) {
                try {
                  if (input[pos1 + wbewegung_k[i]] == 0 || input[pos1 + wbewegung_k[i]] < 7) {
                    wohin.add(pos1 + wbewegung_k[i]);
                  }
                }catch (Exception ignored){}
              }
            }
          }
          try {
            if ((!hasMoved[0] && !hasMoved[1]) && (input[pos1 - 8] == 0 && input[pos1 - 16] == 0 && input[pos1 - 24] == 0) && !(Main.check(input, pos1, 0) && Main.check(input, pos1 - 8, 0) && Main.check(input, pos1 - 16, 0))) { //AAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHH
              if ((Main.check(input, pos1, 0) && Main.check(input, pos1 - 8, 0) && Main.check(input, pos1 - 16, 0))) {
              } else {
                wohin.add(pos1 - 16);
              }
            }
          }catch (Exception ignored){}
          try {
            if (!hasMoved[0] && !hasMoved[2] && input[pos1 + 8] == 0 && input[pos1 + 16] == 0 && !(Main.check(input, pos1, 0) && Main.check(input, pos1 + 8, 0) && Main.check(input, pos1 + 16, 0))) { //GELGUS
              if ((Main.check(input, pos1, 0) && Main.check(input, pos1 + 8, 0) && Main.check(input, pos1 + 16, 0))) {
              } else {
                wohin.add(pos1 + 16);
              }
            }
          }catch (Exception ignored){}
        }
        break;
    }
    return wohin;
  }
}
