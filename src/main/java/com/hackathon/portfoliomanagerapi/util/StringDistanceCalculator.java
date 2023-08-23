package com.hackathon.portfoliomanagerapi.util;

public class StringDistanceCalculator {
    private static final int INSERT_COST = 1;
    private static final int DELETE_COST = 1;
    private static final int TRANSPOSE_COST = 1;
    private static final int REPLACE_COST = 1;

    private static int minOfFour(int a, int b, int c, int d) {
        return Math.min(a, Math.min(b, Math.min(c, d)));
    }

    public int getDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        // Fill d[][] in bottom up manner
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                // If first string is empty, only option is
                // to insert all characters of second string
                if (i == 0) dp[i][j] = j; // Min. operations = j
                    // If second string is empty, only option is
                    // to remove all characters of second string
                else if (j == 0) dp[i][j] = i; // Min. operations = i
                    // If last characters are same, ignore last
                    // char and recur for remaining string
                else if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else {
                    int insert = INSERT_COST + dp[i][j - 1];
                    int remove = DELETE_COST + dp[i - 1][j];
                    int replace = REPLACE_COST + dp[i - 1][j - 1];
                    int transpose = Integer.MAX_VALUE;

                    //trying to transpose(swap adjacent characters)
                    if (i > 1 && j > 1) {
                        if (a.charAt(i - 1) == b.charAt(j - 2) && a.charAt(i - 2) == b.charAt(j - 1)) {
                            transpose = TRANSPOSE_COST + dp[i - 2][j - 2];
                        }
                    }

                    dp[i][j] = minOfFour(insert, remove, replace, transpose);
                }
            }
        }
        return dp[n][m];
    }

    public int getLcs(String a, String b) {
        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];
        for(int i = 1; i <= n; i++)
        {
            for(int j = 1; j <= m; j++)
            {
                if(a.charAt(i -  1) == b.charAt(j - 1)) dp[i][j] = 1 + dp[i - 1][j - 1];
                else    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        return dp[n][m];
    }

    public int getLongestCommonPrefix(String a, String b) {
        int i = 0;
        int j = 0;
        while(i < a.length() && j < b.length() && a.charAt(i) == b.charAt(j)) {
            i ++;
            j ++;
        }
        return i;
    }

    public int getLongestCommonSuffix(String a, String b) {
        int i = a.length() - 1;
        int j = b.length() - 1;
        int count = 0;
        while(i >= 0 && j >= 0 && a.charAt(i) == b.charAt(j)) {
            i --;
            j --;
            count  ++;
        }
        return count;
    }

    public int getScore(String a, String b) {
        return getDistance(a, b);
    }
}
