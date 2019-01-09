package com.alialaoui.guesstheagechallenge.model;

import com.alialaoui.guesstheagechallenge.R;

public class Game {
    private final static int NUM_LEVELS = 60;

    Level levels[] = new Level[NUM_LEVELS];
    Level thisLevel;

    public Game() {
        levels[0] = new Level(32, R.drawable.level1, 0);
        levels[1] = new Level(21, R.drawable.level2, 1);
        levels[2] = new Level(26, R.drawable.level3, 2);
        levels[3] = new Level(17, R.drawable.level4, 3);
        levels[4] = new Level(18, R.drawable.level5, 4);
        levels[5] = new Level(16, R.drawable.level6, 5);
        levels[6] = new Level(13, R.drawable.level7, 6);
        levels[7] = new Level(20, R.drawable.level8, 7);
        levels[8] = new Level(14, R.drawable.level9, 8);
        levels[9] = new Level(19, R.drawable.level10, 9);
        levels[10] = new Level(19, R.drawable.level11, 10);
        levels[11] = new Level(9, R.drawable.level12, 11);
        levels[12] = new Level(24, R.drawable.level13, 12);
        levels[13] = new Level(15, R.drawable.level14, 13);
        levels[14] = new Level(22, R.drawable.level15, 14);
        levels[15] = new Level(25, R.drawable.level16, 15);
        levels[16] = new Level(11, R.drawable.level17, 16);
        levels[17] = new Level(17, R.drawable.level18, 17);
        levels[18] = new Level(17, R.drawable.level19, 18);
        levels[19] = new Level(20, R.drawable.level20, 19);
        levels[20] = new Level(3, R.drawable.level21, 20);
        levels[21] = new Level(24, R.drawable.level22, 21);
        levels[22] = new Level(25, R.drawable.level23, 22);
        levels[23] = new Level(25, R.drawable.level24, 23);
        levels[24] = new Level(14, R.drawable.level25, 24);
        levels[25] = new Level(20, R.drawable.level26, 25);
        levels[26] = new Level(18, R.drawable.level27, 26);
        levels[27] = new Level(16, R.drawable.level28, 27);
        levels[28] = new Level(17, R.drawable.level29, 28);
        levels[29] = new Level(13, R.drawable.level30, 29);
        levels[30] = new Level(17, R.drawable.level31, 30);
        levels[31] = new Level(21, R.drawable.level32, 31);
        levels[32] = new Level(29, R.drawable.level33, 32);
        levels[33] = new Level(19, R.drawable.level34, 33);
        levels[34] = new Level(27, R.drawable.level35, 34);
        levels[35] = new Level(35, R.drawable.level36, 35);
        levels[36] = new Level(23, R.drawable.level37, 36);
        levels[37] = new Level(43, R.drawable.level38, 37);
        levels[38] = new Level(10, R.drawable.level39, 38);
        levels[39] = new Level(26, R.drawable.level40, 39);
        levels[40] = new Level(17, R.drawable.level41, 40);
        levels[41] = new Level(22, R.drawable.level42, 41);
        levels[42] = new Level(20, R.drawable.level43, 42);
        levels[43] = new Level(18, R.drawable.level44, 43);
        levels[44] = new Level(19, R.drawable.level45, 44);
        levels[45] = new Level(22, R.drawable.level46, 45);
        levels[46] = new Level(28, R.drawable.level47, 46);
        levels[47] = new Level(25, R.drawable.level48, 47);
        levels[48] = new Level(37, R.drawable.level49, 48);
        levels[49] = new Level(18, R.drawable.level50, 49);
        levels[50] = new Level(34, R.drawable.level51, 50);
        levels[51] = new Level(28, R.drawable.level52, 51);
        levels[52] = new Level(20, R.drawable.level53, 52);
        levels[53] = new Level(15, R.drawable.level54, 53);
        levels[54] = new Level(19, R.drawable.level55, 54);
        levels[55] = new Level(32, R.drawable.level56, 55);
        levels[56] = new Level(24, R.drawable.level57, 56);
        levels[57] = new Level(20, R.drawable.level58, 57);
        levels[58] = new Level(12, R.drawable.level59, 58);
        levels[59] = new Level(13, R.drawable.level60, 59);
    }

    public Level getLevel(int numLevel) {
        for(int i = 0; i < NUM_LEVELS; i++) {
            if(levels[i].getNumLevel() == numLevel) {
                return levels[i];
            }
        }
        return null;
    }

    public Level getThisLevel() {
        return thisLevel;
    }

    public void setThisLevel(Level thisLevel) {
        this.thisLevel = thisLevel;
    }
}
