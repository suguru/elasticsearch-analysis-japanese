package jp.ameba.elasticsearch.analysis.japanese.tiny;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Constants and macros for {@link TinyJapaneseTokenizer}.
 */
class TinyJapaneseSegmenterConstants {

  /** contextual state: start of string */
  public static final int PU = 0;
  /** contextual state: non-boundary */
  public static final int PO = 1;
  /** contextual state: boundary */
  public static final int PB = 2;

  /** character category: other */
  public static final int O = 0;
  /** character category: ideographic number */
  public static final int M = 1;
  /** character category: ideographic */
  public static final int H = 2;
  /** character category: hiragana */
  public static final int I = 3;
  /** character category: katakana */
  public static final int K = 4;
  /** character category: alphabetic */
  public static final int A = 5;
  /** character category: numeric */
  public static final int N = 6;

  /** begin marker: sentence start-1 */
  public static final int B1 = 0xE000;
  /** begin marker: sentence start-2 */
  public static final int B2 = 0xE001;
  /** begin marker: sentence start-3 */
  public static final int B3 = 0xE003;
  /** end marker: sentence end */
  public static final int E1 = 0xE004;
  /** end marker: sentence end+1 */
  public static final int E2 = 0xE005;
  /** end marker: sentence end+2 */
  public static final int E3 = 0xE006;

  /** returns character category for ch */
  public static final int charType(int ch) {
    switch(ch) {
      case 0x4E00:
      case 0x4E8C:
      case 0x4E09:
      case 0x56DB:
      case 0x4E94:
      case 0x516D:
      case 0x4E03:
      case 0x516B:
      case 0x4E5D:
      case 0x5341:
      case 0x767E:
      case 0x5343:
      case 0x4E07:
      case 0x5104:
      case 0x5146: return M;
      case 0x3005:
      case 0x3006:
      case 0x30F5:
      case 0x30F6: return H;
      case 0x30FC:
      case 0xFF9E:
      case 0xFF70: return K;

      default:
        if (ch >= 0x4E00 && ch <= 0x9FA0)
          return H;
        else if (ch >= 0x3041 && ch <= 0x3093)
          return I;
        else if (ch >= 0x30A1 && ch <= 0x30F4)
          return K;
        else if (ch >= 0xFF71 && ch <= 0xFF9D)
          return K;
        else if (ch >= 0x0041 && ch <= 0x005A)
          return A;
        else if (ch >= 0x0061 && ch <= 0x007A)
          return A;
        else if (ch >= 0xFF21 && ch <= 0xFF3A)
          return A;
        else if (ch >= 0xFF41 && ch <= 0xFF5A)
          return A;
        else if (ch >= 0x0030 && ch <= 0x0039)
          return N;
        else if (ch >= 0xFF10 && ch <= 0xFF19)
          return N;
        else
          return O;
    }
  }

  /** bias: if the accumulated score is greater than -BIAS its a break */
  public static final int BIAS = -332;

  // for all cost functions below, n is the position of the potential break
  
  /** bigram category cost for (n-2, n-1) */
  public static final int bc1(int i0, int i1) {
    switch(i0 <<3| i1) {
      case O <<3| H: return -1378;
      case I <<3| I: return 2461;
      case H <<3| H: return 6;
      case K <<3| H: return 406;
      default: return 0;
    }
  }

  /** bigram category cost for (n-1, n) */
  public static final int bc2(int i0, int i1) {
    switch(i0 <<3| i1) {
      case A <<3| N: return -878;
      case M <<3| K: return 3334;
      case H <<3| H: return -4070;
      case I <<3| A: return 1327;
      case K <<3| I: return 3831;
      case K <<3| K: return -8741;
      case H <<3| M: return -1711;
      case A <<3| A: return -3267;
      case H <<3| N: return 4012;
      case H <<3| O: return 3761;
      case I <<3| H: return -1184;
      case I <<3| I: return -1332;
      case I <<3| K: return 1721;
      case A <<3| I: return 2744;
      case I <<3| O: return 5492;
      case M <<3| H: return -3132;
      case O <<3| O: return -2920;
      default: return 0;
    }
  }

  /** bigram category cost for (n, n+1) */
  public static final int bc3(int i0, int i1) {
    switch(i0 <<3| i1) {
      case M <<3| K: return 1079;
      case M <<3| M: return 4034;
      case H <<3| H: return 996;
      case H <<3| I: return 626;
      case H <<3| K: return -721;
      case O <<3| A: return -1652;
      case K <<3| K: return 2762;
      case H <<3| N: return -1307;
      case H <<3| O: return -836;
      case I <<3| H: return -301;
      case O <<3| H: return 266;
      default: return 0;
    }
  }

  /** bigram context cost for (n-3, n-2) */
  public static final int bp1(int i0, int i1) {
    switch(i0 <<3| i1) {
      case PB <<3| PB: return 295;
      case PU <<3| PB: return 352;
      case PO <<3| PB: return 304;
      case PO <<3| PO: return -125;
      default: return 0;
    }
  }

  /** bigram context cost for (n-2, n-1) */
  public static final int bp2(int i0, int i1) {
    switch(i0 <<3| i1) {
      case PB <<3| PO: return 60;
      case PO <<3| PO: return -1762;
      default: return 0;
    }
  }

  /** bigram category (n-2, n-1) with context (n-2) cost */
  public static final int bq1(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case PB <<6| H <<3| M: return 1521;
      case PO <<6| H <<3| I: return 451;
      case PO <<6| K <<3| H: return -1020;
      case PO <<6| K <<3| K: return 904;
      case PB <<6| I <<3| I: return -1158;
      case PB <<6| O <<3| H: return -91;
      case PO <<6| I <<3| H: return -296;
      case PB <<6| I <<3| M: return 886;
      case PB <<6| O <<3| O: return -2597;
      case PB <<6| M <<3| H: return 1208;
      case PO <<6| K <<3| A: return 1851;
      case PO <<6| O <<3| O: return 2965;
      case PB <<6| H <<3| H: return 1150;
      case PB <<6| N <<3| H: return 449;
      default: return 0;
    }
  }

  /** bigram category (n-1, n) with context (n-2) cost */
  public static final int bq2(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case PB <<6| K <<3| K: return -1720;
      case PO <<6| H <<3| H: return -1139;
      case PB <<6| H <<3| M: return 466;
      case PB <<6| K <<3| O: return 864;
      case PB <<6| I <<3| H: return -919;
      case PO <<6| H <<3| M: return -181;
      case PO <<6| I <<3| H: return 153;
      case PU <<6| H <<3| I: return -1146;
      case PB <<6| H <<3| H: return 118;
      case PB <<6| H <<3| I: return -1159;
      default: return 0;
    }
  }

  /** bigram category (n-2, n-1) with context (n-1) cost */
  public static final int bq3(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case PO <<6| H <<3| H: return 2174;
      case PO <<6| K <<3| H: return 1798;
      case PO <<6| K <<3| I: return -793;
      case PB <<6| N <<3| N: return 998;
      case PB <<6| I <<3| I: return -299;
      case PO <<6| H <<3| M: return 439;
      case PB <<6| O <<3| H: return 775;
      case PO <<6| K <<3| O: return -2242;
      case PO <<6| I <<3| I: return 280;
      case PB <<6| M <<3| H: return 937;
      case PO <<6| M <<3| H: return -2402;
      case PO <<6| O <<3| O: return 11699;
      case PB <<6| M <<3| M: return 8335;
      case PB <<6| H <<3| H: return -792;
      case PB <<6| H <<3| I: return 2664;
      case PB <<6| K <<3| I: return 419;
      default: return 0;
    }
  }

  /** bigram category (n-1, n) with context (n-1) cost */
  public static final int bq4(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case PB <<6| K <<3| K: return -1806;
      case PO <<6| H <<3| H: return 266;
      case PO <<6| H <<3| K: return -2036;
      case PB <<6| I <<3| H: return 3761;
      case PB <<6| I <<3| I: return -4654;
      case PB <<6| I <<3| K: return 1348;
      case PO <<6| N <<3| N: return -973;
      case PB <<6| O <<3| O: return -12396;
      case PO <<6| A <<3| H: return 926;
      case PB <<6| M <<3| I: return -3385;
      case PB <<6| H <<3| H: return -3895;
      default: return 0;
    }
  }

  /** bigram character cost for (n-2, n-1) */
  public static final int bw1(int i0, int i1) {
    switch(i0 <<16| i1) {
      case 0x5f15 <<16| 0x304d: return (i0 == 0x5f15 && i1 == 0x304d) ? -1336 : 0;
      case 0x304b <<16| 0x3089: return (i0 == 0x304b && i1 == 0x3089) ? 3472 : 0;
      case 0x3044 <<16| 0x3046: return (i0 == 0x3044 && i1 == 0x3046) ? 1743 : 0;
      case 0x3092 <<16| 0x898b: return (i0 == 0x3092 && i1 == 0x898b) ? 731 : 0;
      case 0x5e73 <<16| 0x65b9: return (i0 == 0x5e73 && i1 == 0x65b9) ? -2314 : 0;
      case 0xe000 <<16| 0x540c: return (i0 == 0xe000 && i1 == 0x540c) ? 542 : 0;
      case 0x3066 <<16| 0x3044: return (i0 == 0x3066 && i1 == 0x3044) ? 805 : 0;
      case 0x305f <<16| 0x3061: return (i0 == 0x305f && i1 == 0x3061) ? 1122 : 0;
      case 0x5927 <<16| 0x962a: return (i0 == 0x5927 && i1 == 0x962a) ? 1497 : 0;
      case 0x307e <<16| 0x305b: return (i0 == 0x307e && i1 == 0x305b) ? 2448 : 0;
      case 0x53d6 <<16| 0x308a: return (i0 == 0x53d6 && i1 == 0x308a) ? -2784 : 0;
      case 0x306b <<16| 0x306f: return (i0 == 0x306b && i1 == 0x306f) ? 1498 : 0;
      case 0x3066 <<16| 0x304d: return (i0 == 0x3066 && i1 == 0x304d) ? 1249 : 0;
      case 0x3059 <<16| 0x3067: return (i0 == 0x3059 && i1 == 0x3067) ? -3399 : 0;
      case 0x6bce <<16| 0x65e5: return (i0 == 0x6bce && i1 == 0x65e5) ? -2113 : 0;
      case 0x3069 <<16| 0x3053: return (i0 == 0x3069 && i1 == 0x3053) ? 3887 : 0;
      case 0x306a <<16| 0x3093: return (i0 == 0x306a && i1 == 0x3093) ? -1113 : 0;
      case 0x3055 <<16| 0x3089: return (i0 == 0x3055 && i1 == 0x3089) ? -4143 : 0;
      case 0x3053 <<16| 0x3068: return (i0 == 0x3053 && i1 == 0x3068) ? 2083 : 0;
      case 0x307e <<16| 0x3067: return (i0 == 0x307e && i1 == 0x3067) ? 1711 : 0;
      case 0x306e <<16| 0x4e2d: return (i0 == 0x306e && i1 == 0x4e2d) ? 741 : 0;
      case 0x305d <<16| 0x3053: return (i0 == 0x305d && i1 == 0x3053) ? 1977 : 0;
      case 0x3044 <<16| 0x3063: return (i0 == 0x3044 && i1 == 0x3063) ? -2055 : 0;
      case 0x304c <<16| 0x3089: return (i0 == 0x304c && i1 == 0x3089) ? 600 : 0;
      case 0x3068 <<16| 0x307f: return (i0 == 0x3068 && i1 == 0x307f) ? 1922 : 0;
      case 0x3055 <<16| 0x3093: return (i0 == 0x3055 && i1 == 0x3093) ? 4573 : 0;
      case 0x306b <<16| 0x3082: return (i0 == 0x306b && i1 == 0x3082) ? 1671 : 0;
      case 0x3063 <<16| 0x305f: return (i0 == 0x3063 && i1 == 0x305f) ? 3463 : 0;
      case 0x306a <<16| 0x3044: return (i0 == 0x306a && i1 == 0x3044) ? 5713 : 0;
      case 0x300d <<16| 0x3068: return (i0 == 0x300d && i1 == 0x3068) ? 1682 : 0;
      case 0x3064 <<16| 0x3044: return (i0 == 0x3064 && i1 == 0x3044) ? -802 : 0;
      case 0x305f <<16| 0x3081: return (i0 == 0x305f && i1 == 0x3081) ? 601 : 0;
      case 0x3057 <<16| 0x305f: return (i0 == 0x3057 && i1 == 0x305f) ? 2641 : 0;
      case 0x3046 <<16| 0x3093: return (i0 == 0x3046 && i1 == 0x3093) ? 665 : 0;
      case 0x672c <<16| 0x5f53: return (i0 == 0x672c && i1 == 0x5f53) ? -2423 : 0;
      case 0x3067 <<16| 0x304d: return (i0 == 0x3067 && i1 == 0x304d) ? 1127 : 0;
      case 0x3001 <<16| 0x3068: return (i0 == 0x3001 && i1 == 0x3068) ? 660 : 0;
      case 0x3084 <<16| 0x3080: return (i0 == 0x3084 && i1 == 0x3080) ? -1947 : 0;
      case 0x3088 <<16| 0x3063: return (i0 == 0x3088 && i1 == 0x3063) ? -2565 : 0;
      case 0x307e <<16| 0x307e: return (i0 == 0x307e && i1 == 0x307e) ? 2600 : 0;
      case 0x3057 <<16| 0x3066: return (i0 == 0x3057 && i1 == 0x3066) ? 1104 : 0;
      case 0x3001 <<16| 0x540c: return (i0 == 0x3001 && i1 == 0x540c) ? 727 : 0;
      case 0x306b <<16| 0x5bfe: return (i0 == 0x306b && i1 == 0x5bfe) ? -912 : 0;
      case 0x4ea1 <<16| 0x304f: return (i0 == 0x4ea1 && i1 == 0x304f) ? -1886 : 0;
      case 0xff63 <<16| 0x3068: return (i0 == 0xff63 && i1 == 0x3068) ? 1682 : 0;
      case 0x3067 <<16| 0x3059: return (i0 == 0x3067 && i1 == 0x3059) ? 3445 : 0;
      case 0x5927 <<16| 0x304d: return (i0 == 0x5927 && i1 == 0x304d) ? -2604 : 0;
      case 0xe000 <<16| 0x3042: return (i0 == 0xe000 && i1 == 0x3042) ? 1404 : 0;
      case 0x3092 <<16| 0x3057: return (i0 == 0x3092 && i1 == 0x3057) ? 1860 : 0;
      case 0x3042 <<16| 0x3063: return (i0 == 0x3042 && i1 == 0x3063) ? 1505 : 0;
      case 0x307e <<16| 0x308b: return (i0 == 0x307e && i1 == 0x308b) ? -2155 : 0;
      case 0x4eac <<16| 0x90fd: return (i0 == 0x4eac && i1 == 0x90fd) ? 2558 : 0;
      case 0x3053 <<16| 0x3093: return (i0 == 0x3053 && i1 == 0x3093) ? -1262 : 0;
      case 0x306a <<16| 0x3063: return (i0 == 0x306a && i1 == 0x3063) ? 3015 : 0;
      case 0x3068 <<16| 0x3044: return (i0 == 0x3068 && i1 == 0x3044) ? -4915 : 0;
      case 0x3044 <<16| 0x308b: return (i0 == 0x3044 && i1 == 0x308b) ? 672 : 0;
      case 0x002c <<16| 0x3068: return (i0 == 0x002c && i1 == 0x3068) ? 660 : 0;
      case 0x308c <<16| 0x305f: return (i0 == 0x308c && i1 == 0x305f) ? 2369 : 0;
      case 0x306a <<16| 0x3069: return (i0 == 0x306a && i1 == 0x3069) ? 7379 : 0;
      case 0x002c <<16| 0x540c: return (i0 == 0x002c && i1 == 0x540c) ? 727 : 0;
      case 0x306e <<16| 0x4e00: return (i0 == 0x306e && i1 == 0x4e00) ? -501 : 0;
      case 0x76ee <<16| 0x6307: return (i0 == 0x76ee && i1 == 0x6307) ? -724 : 0;
      case 0x3046 <<16| 0x3057: return (i0 == 0x3046 && i1 == 0x3057) ? -4817 : 0;
      case 0x308c <<16| 0x3067: return (i0 == 0x308c && i1 == 0x3067) ? -913 : 0;
      case 0x3067 <<16| 0x306f: return (i0 == 0x3067 && i1 == 0x306f) ? 844 : 0;
      case 0x305d <<16| 0x308c: return (i0 == 0x305d && i1 == 0x308c) ? -871 : 0;
      case 0x3053 <<16| 0x3046: return (i0 == 0x3053 && i1 == 0x3046) ? -790 : 0;
      case 0x306b <<16| 0x3057: return (i0 == 0x306b && i1 == 0x3057) ? 2468 : 0;
      case 0x65e5 <<16| 0x672c: return (i0 == 0x65e5 && i1 == 0x672c) ? -195 : 0;
      default: return 0;
    }
  }

  /** bigram character cost for (n-1, n) */
  public static final int bw2(int i0, int i1) {
    switch(i0 <<16| i1) {
      case 0x2015 <<16| 0x2015: return (i0 == 0x2015 && i1 == 0x2015) ? -5730 : 0;
      case 0x308c <<16| 0x3070: return (i0 == 0x308c && i1 == 0x3070) ? 4114 : 0;
      case 0x3068 <<16| 0x3053: return (i0 == 0x3068 && i1 == 0x3053) ? -1746 : 0;
      case 0x306b <<16| 0x5bfe: return (i0 == 0x306b && i1 == 0x5bfe) ? -14943 : 0;
      case 0x0031 <<16| 0x0031: return (i0 == 0x0031 && i1 == 0x0031) ? -669 : 0;
      case 0x3093 <<16| 0x3060: return (i0 == 0x3093 && i1 == 0x3060) ? 728 : 0;
      case 0x306f <<16| 0x3044: return (i0 == 0x306f && i1 == 0x3044) ? 1073 : 0;
      case 0x304f <<16| 0x306a: return (i0 == 0x304f && i1 == 0x306a) ? -1597 : 0;
      case 0x4e00 <<16| 0x90e8: return (i0 == 0x4e00 && i1 == 0x90e8) ? -1051 : 0;
      case 0x59d4 <<16| 0x54e1: return (i0 == 0x59d4 && i1 == 0x54e1) ? -1250 : 0;
      case 0x306e <<16| 0x3067: return (i0 == 0x306e && i1 == 0x3067) ? -7059 : 0;
      case 0x3067 <<16| 0x3082: return (i0 == 0x3067 && i1 == 0x3082) ? -4203 : 0;
      case 0x3044 <<16| 0x3046: return (i0 == 0x3044 && i1 == 0x3046) ? -1609 : 0;
      case 0x306e <<16| 0x306b: return (i0 == 0x306e && i1 == 0x306b) ? -6041 : 0;
      case 0x306f <<16| 0x304c: return (i0 == 0x306f && i1 == 0x304c) ? -1033 : 0;
      case 0x3093 <<16| 0x306a: return (i0 == 0x3093 && i1 == 0x306a) ? -4115 : 0;
      case 0x65b0 <<16| 0x805e: return (i0 == 0x65b0 && i1 == 0x805e) ? -4066 : 0;
      case 0x3068 <<16| 0x3068: return (i0 == 0x3068 && i1 == 0x3068) ? -2279 : 0;
      case 0x306e <<16| 0x306e: return (i0 == 0x306e && i1 == 0x306e) ? -6125 : 0;
      case 0x4f1a <<16| 0x793e: return (i0 == 0x4f1a && i1 == 0x793e) ? -1116 : 0;
      case 0x540c <<16| 0x515a: return (i0 == 0x540c && i1 == 0x515a) ? 970 : 0;
      case 0x3068 <<16| 0x306e: return (i0 == 0x3068 && i1 == 0x306e) ? 720 : 0;
      case 0x3082 <<16| 0x3044: return (i0 == 0x3082 && i1 == 0x3044) ? 2230 : 0;
      case 0x3081 <<16| 0x3066: return (i0 == 0x3081 && i1 == 0x3066) ? -3153 : 0;
      case 0x3057 <<16| 0x3044: return (i0 == 0x3057 && i1 == 0x3044) ? -1819 : 0;
      case 0x306f <<16| 0x305a: return (i0 == 0x306f && i1 == 0x305a) ? -2532 : 0;
      case 0x4e00 <<16| 0x65b9: return (i0 == 0x4e00 && i1 == 0x65b9) ? -1375 : 0;
      case 0x3092 <<16| 0x901a: return (i0 == 0x3092 && i1 == 0x901a) ? -11877 : 0;
      case 0x5c11 <<16| 0x306a: return (i0 == 0x5c11 && i1 == 0x306a) ? -1050 : 0;
      case 0x3057 <<16| 0x304b: return (i0 == 0x3057 && i1 == 0x304b) ? -545 : 0;
      case 0x4e0a <<16| 0x304c: return (i0 == 0x4e0a && i1 == 0x304c) ? -4479 : 0;
      case 0x3055 <<16| 0x308c: return (i0 == 0x3055 && i1 == 0x308c) ? 13168 : 0;
      case 0x3068 <<16| 0x307f: return (i0 == 0x3068 && i1 == 0x307f) ? 5168 : 0;
      case 0x2212 <<16| 0x2212: return (i0 == 0x2212 && i1 == 0x2212) ? -13175 : 0;
      case 0x3068 <<16| 0x3082: return (i0 == 0x3068 && i1 == 0x3082) ? -3941 : 0;
      case 0x306a <<16| 0x3044: return (i0 == 0x306a && i1 == 0x3044) ? -2488 : 0;
      case 0x672c <<16| 0x4eba: return (i0 == 0x672c && i1 == 0x4eba) ? -2697 : 0;
      case 0x3063 <<16| 0x305f: return (i0 == 0x3063 && i1 == 0x305f) ? 4589 : 0;
      case 0x3055 <<16| 0x3093: return (i0 == 0x3055 && i1 == 0x3093) ? -3977 : 0;
      case 0x306b <<16| 0x95a2: return (i0 == 0x306b && i1 == 0x95a2) ? -11388 : 0;
      case 0x306a <<16| 0x304c: return (i0 == 0x306a && i1 == 0x304c) ? -1313 : 0;
      case 0x3063 <<16| 0x3066: return (i0 == 0x3063 && i1 == 0x3066) ? 1647 : 0;
      case 0x3063 <<16| 0x3068: return (i0 == 0x3063 && i1 == 0x3068) ? -2094 : 0;
      case 0x624b <<16| 0x6a29: return (i0 == 0x624b && i1 == 0x6a29) ? -1982 : 0;
      case 0x3057 <<16| 0x305f: return (i0 == 0x3057 && i1 == 0x305f) ? 5078 : 0;
      case 0x304b <<16| 0x3057: return (i0 == 0x304b && i1 == 0x3057) ? -1350 : 0;
      case 0x3089 <<16| 0x304b: return (i0 == 0x3089 && i1 == 0x304b) ? -944 : 0;
      case 0x66dc <<16| 0x65e5: return (i0 == 0x66dc && i1 == 0x65e5) ? -601 : 0;
      case 0x5e74 <<16| 0x5ea6: return (i0 == 0x5e74 && i1 == 0x5ea6) ? -8669 : 0;
      case 0x3057 <<16| 0x3066: return (i0 == 0x3057 && i1 == 0x3066) ? 972 : 0;
      case 0x305d <<16| 0x306e: return (i0 == 0x305d && i1 == 0x306e) ? -3744 : 0;
      case 0x3057 <<16| 0x306a: return (i0 == 0x3057 && i1 == 0x306a) ? 939 : 0;
      case 0x3082 <<16| 0x306e: return (i0 == 0x3082 && i1 == 0x306e) ? -10713 : 0;
      case 0x4e00 <<16| 0x4eba: return (i0 == 0x4e00 && i1 == 0x4eba) ? 602 : 0;
      case 0x6771 <<16| 0x4eac: return (i0 == 0x6771 && i1 == 0x4eac) ? -1543 : 0;
      case 0x304c <<16| 0x3044: return (i0 == 0x304c && i1 == 0x3044) ? 853 : 0;
      case 0x3089 <<16| 0x3057: return (i0 == 0x3089 && i1 == 0x3057) ? -1611 : 0;
      case 0x7c73 <<16| 0x56fd: return (i0 == 0x7c73 && i1 == 0x56fd) ? -4268 : 0;
      case 0x4e00 <<16| 0x65e5: return (i0 == 0x4e00 && i1 == 0x65e5) ? 970 : 0;
      case 0x306a <<16| 0x3069: return (i0 == 0x306a && i1 == 0x3069) ? -6509 : 0;
      case 0x306b <<16| 0x304a: return (i0 == 0x306b && i1 == 0x304a) ? -1615 : 0;
      case 0x3046 <<16| 0x304b: return (i0 == 0x3046 && i1 == 0x304b) ? 2490 : 0;
      case 0x65e5 <<16| 0x7c73: return (i0 == 0x65e5 && i1 == 0x7c73) ? 3372 : 0;
      case 0x305f <<16| 0x3044: return (i0 == 0x305f && i1 == 0x3044) ? -1253 : 0;
      case 0x306a <<16| 0x306e: return (i0 == 0x306a && i1 == 0x306e) ? 2614 : 0;
      case 0x3089 <<16| 0x306b: return (i0 == 0x3089 && i1 == 0x306b) ? -1897 : 0;
      case 0x5927 <<16| 0x962a: return (i0 == 0x5927 && i1 == 0x962a) ? -2471 : 0;
      case 0x306b <<16| 0x3057: return (i0 == 0x306b && i1 == 0x3057) ? 2748 : 0;
      case 0x5e9c <<16| 0x770c: return (i0 == 0x5e9c && i1 == 0x770c) ? -2363 : 0;
      case 0x304b <<16| 0x3082: return (i0 == 0x304b && i1 == 0x3082) ? -602 : 0;
      case 0x308a <<16| 0x3057: return (i0 == 0x308a && i1 == 0x3057) ? 651 : 0;
      case 0x793e <<16| 0x4f1a: return (i0 == 0x793e && i1 == 0x4f1a) ? -1276 : 0;
      case 0x304b <<16| 0x3089: return (i0 == 0x304b && i1 == 0x3089) ? -7194 : 0;
      case 0x307e <<16| 0x3057: return (i0 == 0x307e && i1 == 0x3057) ? -1316 : 0;
      case 0x304b <<16| 0x308c: return (i0 == 0x304b && i1 == 0x308c) ? 4612 : 0;
      case 0x3070 <<16| 0x308c: return (i0 == 0x3070 && i1 == 0x308c) ? 1813 : 0;
      case 0x3066 <<16| 0x3044: return (i0 == 0x3066 && i1 == 0x3044) ? 6144 : 0;
      case 0x305f <<16| 0x305f: return (i0 == 0x305f && i1 == 0x305f) ? -662 : 0;
      case 0x306b <<16| 0x306a: return (i0 == 0x306b && i1 == 0x306a) ? 2454 : 0;
      case 0x305f <<16| 0x3060: return (i0 == 0x305f && i1 == 0x3060) ? -3857 : 0;
      case 0x305f <<16| 0x3061: return (i0 == 0x305f && i1 == 0x3061) ? -786 : 0;
      case 0x7b2c <<16| 0x306b: return (i0 == 0x7b2c && i1 == 0x306b) ? -1612 : 0;
      case 0x308f <<16| 0x308c: return (i0 == 0x308f && i1 == 0x308c) ? 7901 : 0;
      case 0x3066 <<16| 0x304d: return (i0 == 0x3066 && i1 == 0x304d) ? 3640 : 0;
      case 0x305f <<16| 0x3068: return (i0 == 0x305f && i1 == 0x3068) ? 1224 : 0;
      case 0x3066 <<16| 0x304f: return (i0 == 0x3066 && i1 == 0x304f) ? 2551 : 0;
      case 0x306a <<16| 0x3093: return (i0 == 0x306a && i1 == 0x3093) ? 3099 : 0;
      case 0x540c <<16| 0x65e5: return (i0 == 0x540c && i1 == 0x65e5) ? -913 : 0;
      case 0x002e <<16| 0x002e: return (i0 == 0x002e && i1 == 0x002e) ? -11822 : 0;
      case 0x307e <<16| 0x3067: return (i0 == 0x307e && i1 == 0x3067) ? -6621 : 0;
      case 0x304d <<16| 0x305f: return (i0 == 0x304d && i1 == 0x305f) ? 1941 : 0;
      case 0x305f <<16| 0x306f: return (i0 == 0x305f && i1 == 0x306f) ? -939 : 0;
      case 0x3053 <<16| 0x3068: return (i0 == 0x3053 && i1 == 0x3068) ? -8392 : 0;
      case 0x7136 <<16| 0x3068: return (i0 == 0x7136 && i1 == 0x3068) ? -1384 : 0;
      case 0x3053 <<16| 0x306e: return (i0 == 0x3053 && i1 == 0x306e) ? -4193 : 0;
      case 0x304c <<16| 0x3089: return (i0 == 0x304c && i1 == 0x3089) ? -3198 : 0;
      case 0x308a <<16| 0x307e: return (i0 == 0x308a && i1 == 0x307e) ? 1620 : 0;
      case 0x3067 <<16| 0x3044: return (i0 == 0x3067 && i1 == 0x3044) ? 2666 : 0;
      case 0x306b <<16| 0x3088: return (i0 == 0x306b && i1 == 0x3088) ? -7236 : 0;
      case 0xff11 <<16| 0xff11: return (i0 == 0xff11 && i1 == 0xff11) ? -669 : 0;
      case 0x3067 <<16| 0x304d: return (i0 == 0x3067 && i1 == 0x304d) ? -1528 : 0;
      case 0x306b <<16| 0x5f93: return (i0 == 0x306b && i1 == 0x5f93) ? -4688 : 0;
      case 0x3066 <<16| 0x306f: return (i0 == 0x3066 && i1 == 0x306f) ? -3110 : 0;
      case 0x7acb <<16| 0x3066: return (i0 == 0x7acb && i1 == 0x3066) ? -990 : 0;
      case 0x3067 <<16| 0x3057: return (i0 == 0x3067 && i1 == 0x3057) ? -3828 : 0;
      case 0x3067 <<16| 0x3059: return (i0 == 0x3067 && i1 == 0x3059) ? -4761 : 0;
      case 0x307e <<16| 0x308c: return (i0 == 0x307e && i1 == 0x308c) ? 5409 : 0;
      case 0x308c <<16| 0x305f: return (i0 == 0x308c && i1 == 0x305f) ? 4270 : 0;
      case 0x3066 <<16| 0x3082: return (i0 == 0x3066 && i1 == 0x3082) ? -3065 : 0;
      case 0x3068 <<16| 0x3044: return (i0 == 0x3068 && i1 == 0x3044) ? 1890 : 0;
      case 0x5206 <<16| 0x306e: return (i0 == 0x5206 && i1 == 0x306e) ? -7758 : 0;
      case 0x306e <<16| 0x304b: return (i0 == 0x306e && i1 == 0x304b) ? 2093 : 0;
      case 0x308d <<16| 0x3046: return (i0 == 0x308d && i1 == 0x3046) ? 6067 : 0;
      case 0x51fa <<16| 0x3066: return (i0 == 0x51fa && i1 == 0x3066) ? 2163 : 0;
      case 0x65e5 <<16| 0x672c: return (i0 == 0x65e5 && i1 == 0x672c) ? -7068 : 0;
      case 0x308c <<16| 0x3066: return (i0 == 0x308c && i1 == 0x3066) ? 849 : 0;
      case 0x5e74 <<16| 0x9593: return (i0 == 0x5e74 && i1 == 0x9593) ? -1626 : 0;
      case 0x65e5 <<16| 0x65b0: return (i0 == 0x65e5 && i1 == 0x65b0) ? -722 : 0;
      case 0x671d <<16| 0x9bae: return (i0 == 0x671d && i1 == 0x9bae) ? -2355 : 0;
      case 0x3055 <<16| 0x305b: return (i0 == 0x3055 && i1 == 0x305b) ? 4533 : 0;
      default: return 0;
    }
  }

  /** bigram character cost for (n, n+1) */
  public static final int bw3(int i0, int i1) {
    switch(i0 <<16| i1) {
      case 0x3067 <<16| 0x306b: return (i0 == 0x3067 && i1 == 0x306b) ? -1482 : 0;
      case 0xff82 <<16| 0x5e02: return (i0 == 0xff82 && i1 == 0x5e02) ? 965 : 0;
      case 0x308b <<16| 0x308b: return (i0 == 0x308b && i1 == 0x308b) ? 3818 : 0;
      case 0x3067 <<16| 0x306f: return (i0 == 0x3067 && i1 == 0x306f) ? 2295 : 0;
      case 0x308c <<16| 0x3070: return (i0 == 0x308c && i1 == 0x3070) ? -3246 : 0;
      case 0x65e5 <<16| 0x3001: return (i0 == 0x65e5 && i1 == 0x3001) ? 974 : 0;
      case 0x305f <<16| 0x002e: return (i0 == 0x305f && i1 == 0x002e) ? 8875 : 0;
      case 0x3068 <<16| 0x3057: return (i0 == 0x3068 && i1 == 0x3057) ? 2266 : 0;
      case 0x304c <<16| 0x3001: return (i0 == 0x304c && i1 == 0x3001) ? 1816 : 0;
      case 0x3059 <<16| 0x002e: return (i0 == 0x3059 && i1 == 0x002e) ? -1310 : 0;
      case 0x3093 <<16| 0x3060: return (i0 == 0x3093 && i1 == 0x3060) ? 606 : 0;
      case 0x306b <<16| 0x3001: return (i0 == 0x306b && i1 == 0x3001) ? -1021 : 0;
      case 0x3044 <<16| 0x3044: return (i0 == 0x3044 && i1 == 0x3044) ? 5308 : 0;
      case 0x3093 <<16| 0x3067: return (i0 == 0x3093 && i1 == 0x3067) ? 798 : 0;
      case 0x3069 <<16| 0x3046: return (i0 == 0x3069 && i1 == 0x3046) ? 4664 : 0;
      case 0x3044 <<16| 0x3048: return (i0 == 0x3044 && i1 == 0x3048) ? 2079 : 0;
      case 0x65b0 <<16| 0x805e: return (i0 == 0x65b0 && i1 == 0x805e) ? -5055 : 0;
      case 0x305f <<16| 0x3002: return (i0 == 0x305f && i1 == 0x3002) ? 8875 : 0;
      case 0x3042 <<16| 0x308a: return (i0 == 0x3042 && i1 == 0x308a) ? 719 : 0;
      case 0x3042 <<16| 0x308b: return (i0 == 0x3042 && i1 == 0x308b) ? 3846 : 0;
      case 0x3044 <<16| 0x304f: return (i0 == 0x3044 && i1 == 0x304f) ? 3029 : 0;
      case 0x308c <<16| 0x308b: return (i0 == 0x308c && i1 == 0x308b) ? 1091 : 0;
      case 0x3068 <<16| 0x306e: return (i0 == 0x3068 && i1 == 0x306e) ? 541 : 0;
      case 0x3059 <<16| 0x3002: return (i0 == 0x3059 && i1 == 0x3002) ? -1310 : 0;
      case 0x305d <<16| 0x3046: return (i0 == 0x305d && i1 == 0x3046) ? 428 : 0;
      case 0x3057 <<16| 0x3044: return (i0 == 0x3057 && i1 == 0x3044) ? -3714 : 0;
      case 0x3060 <<16| 0x002e: return (i0 == 0x3060 && i1 == 0x002e) ? 4098 : 0;
      case 0x305a <<16| 0x002c: return (i0 == 0x305a && i1 == 0x002c) ? 3426 : 0;
      case 0x30ab <<16| 0x6708: return (i0 == 0x30ab && i1 == 0x6708) ? 990 : 0;
      case 0x3044 <<16| 0x305f: return (i0 == 0x3044 && i1 == 0x305f) ? 2056 : 0;
      case 0x3044 <<16| 0x3063: return (i0 == 0x3044 && i1 == 0x3063) ? 1883 : 0;
      case 0x5927 <<16| 0x4f1a: return (i0 == 0x5927 && i1 == 0x4f1a) ? 2217 : 0;
      case 0x3068 <<16| 0x3082: return (i0 == 0x3068 && i1 == 0x3082) ? -3543 : 0;
      case 0x3055 <<16| 0x3092: return (i0 == 0x3055 && i1 == 0x3092) ? 976 : 0;
      case 0x306a <<16| 0x3044: return (i0 == 0x306a && i1 == 0x3044) ? 1796 : 0;
      case 0x3063 <<16| 0x305f: return (i0 == 0x3063 && i1 == 0x305f) ? -4748 : 0;
      case 0x3060 <<16| 0x3002: return (i0 == 0x3060 && i1 == 0x3002) ? 4098 : 0;
      case 0x304b <<16| 0x3051: return (i0 == 0x304b && i1 == 0x3051) ? -743 : 0;
      case 0x3063 <<16| 0x3066: return (i0 == 0x3063 && i1 == 0x3066) ? 300 : 0;
      case 0x305a <<16| 0x3001: return (i0 == 0x305a && i1 == 0x3001) ? 3426 : 0;
      case 0x3057 <<16| 0x305f: return (i0 == 0x3057 && i1 == 0x305f) ? 3562 : 0;
      case 0x306a <<16| 0x304f: return (i0 == 0x306a && i1 == 0x304f) ? -903 : 0;
      case 0x308c <<16| 0x002c: return (i0 == 0x308c && i1 == 0x002c) ? 854 : 0;
      case 0x3057 <<16| 0x3066: return (i0 == 0x3057 && i1 == 0x3066) ? 1449 : 0;
      case 0x3057 <<16| 0x306a: return (i0 == 0x3057 && i1 == 0x306a) ? 2608 : 0;
      case 0x304b <<16| 0x3063: return (i0 == 0x304b && i1 == 0x3063) ? -4098 : 0;
      case 0x3089 <<16| 0x3057: return (i0 == 0x3089 && i1 == 0x3057) ? 1479 : 0;
      case 0x3051 <<16| 0x3069: return (i0 == 0x3051 && i1 == 0x3069) ? 1374 : 0;
      case 0xff76 <<16| 0x6708: return (i0 == 0xff76 && i1 == 0x6708) ? 990 : 0;
      case 0x308c <<16| 0x3001: return (i0 == 0x308c && i1 == 0x3001) ? 854 : 0;
      case 0x304b <<16| 0x306b: return (i0 == 0x304b && i1 == 0x306b) ? -669 : 0;
      case 0x304c <<16| 0x304d: return (i0 == 0x304c && i1 == 0x304d) ? -4855 : 0;
      case 0x306e <<16| 0x002c: return (i0 == 0x306e && i1 == 0x002c) ? -724 : 0;
      case 0x306a <<16| 0x3069: return (i0 == 0x306a && i1 == 0x3069) ? 2135 : 0;
      case 0x304c <<16| 0x3051: return (i0 == 0x304c && i1 == 0x3051) ? -1127 : 0;
      case 0x3044 <<16| 0x308b: return (i0 == 0x3044 && i1 == 0x308b) ? 5600 : 0;
      case 0x305f <<16| 0x3044: return (i0 == 0x305f && i1 == 0x3044) ? -594 : 0;
      case 0x3057 <<16| 0x307e: return (i0 == 0x3057 && i1 == 0x307e) ? 1200 : 0;
      case 0x3044 <<16| 0x308f: return (i0 == 0x3044 && i1 == 0x308f) ? 1527 : 0;
      case 0x4f1a <<16| 0x8b70: return (i0 == 0x4f1a && i1 == 0x8b70) ? 860 : 0;
      case 0x306b <<16| 0x3057: return (i0 == 0x306b && i1 == 0x3057) ? 1771 : 0;
      case 0x304c <<16| 0x3063: return (i0 == 0x304c && i1 == 0x3063) ? -913 : 0;
      case 0x306e <<16| 0x3001: return (i0 == 0x306e && i1 == 0x3001) ? -724 : 0;
      case 0x3046 <<16| 0x3061: return (i0 == 0x3046 && i1 == 0x3061) ? 1117 : 0;
      case 0x793e <<16| 0x4f1a: return (i0 == 0x793e && i1 == 0x4f1a) ? 2024 : 0;
      case 0x304b <<16| 0x3089: return (i0 == 0x304b && i1 == 0x3089) ? 6520 : 0;
      case 0x304b <<16| 0x308a: return (i0 == 0x304b && i1 == 0x308a) ? -2670 : 0;
      case 0x3046 <<16| 0x3068: return (i0 == 0x3046 && i1 == 0x3068) ? 4798 : 0;
      case 0x306e <<16| 0x5b50: return (i0 == 0x306e && i1 == 0x5b50) ? -1000 : 0;
      case 0x307e <<16| 0x3057: return (i0 == 0x307e && i1 == 0x3057) ? 1113 : 0;
      case 0x3066 <<16| 0x3044: return (i0 == 0x3066 && i1 == 0x3044) ? 6240 : 0;
      case 0x306f <<16| 0x002c: return (i0 == 0x306f && i1 == 0x002c) ? 1337 : 0;
      case 0x307e <<16| 0x3059: return (i0 == 0x307e && i1 == 0x3059) ? 6943 : 0;
      case 0x306b <<16| 0x306a: return (i0 == 0x306b && i1 == 0x306a) ? 1906 : 0;
      case 0x3044 <<16| 0x002e: return (i0 == 0x3044 && i1 == 0x002e) ? -1185 : 0;
      case 0x3066 <<16| 0x304a: return (i0 == 0x3066 && i1 == 0x304a) ? 855 : 0;
      case 0x308f <<16| 0x308c: return (i0 == 0x308f && i1 == 0x308c) ? -605 : 0;
      case 0x306b <<16| 0x306f: return (i0 == 0x306b && i1 == 0x306f) ? 2644 : 0;
      case 0x307e <<16| 0x3063: return (i0 == 0x307e && i1 == 0x3063) ? -1549 : 0;
      case 0x3089 <<16| 0x308c: return (i0 == 0x3089 && i1 == 0x308c) ? 6820 : 0;
      case 0x307e <<16| 0x3067: return (i0 == 0x307e && i1 == 0x3067) ? 6154 : 0;
      case 0x305f <<16| 0x306e: return (i0 == 0x305f && i1 == 0x306e) ? 812 : 0;
      case 0x304d <<16| 0x305f: return (i0 == 0x304d && i1 == 0x305f) ? 1645 : 0;
      case 0x3057 <<16| 0x002c: return (i0 == 0x3057 && i1 == 0x002c) ? 1557 : 0;
      case 0x3053 <<16| 0x3068: return (i0 == 0x3053 && i1 == 0x3068) ? 7397 : 0;
      case 0x306f <<16| 0x3001: return (i0 == 0x306f && i1 == 0x3001) ? 1337 : 0;
      case 0x3079 <<16| 0x304d: return (i0 == 0x3079 && i1 == 0x304d) ? 2181 : 0;
      case 0x3053 <<16| 0x306e: return (i0 == 0x3053 && i1 == 0x306e) ? 1542 : 0;
      case 0x3044 <<16| 0x3002: return (i0 == 0x3044 && i1 == 0x3002) ? -1185 : 0;
      case 0x304c <<16| 0x3089: return (i0 == 0x304c && i1 == 0x3089) ? -4977 : 0;
      case 0x304c <<16| 0x308a: return (i0 == 0x304c && i1 == 0x308a) ? -2064 : 0;
      case 0x304b <<16| 0x002e: return (i0 == 0x304b && i1 == 0x002e) ? 2857 : 0;
      case 0x3060 <<16| 0x3063: return (i0 == 0x3060 && i1 == 0x3063) ? 1004 : 0;
      case 0x3057 <<16| 0x3001: return (i0 == 0x3057 && i1 == 0x3001) ? 1557 : 0;
      case 0x305f <<16| 0x308a: return (i0 == 0x305f && i1 == 0x308a) ? -1183 : 0;
      case 0x305f <<16| 0x308b: return (i0 == 0x305f && i1 == 0x308b) ? -853 : 0;
      case 0x3055 <<16| 0x3044: return (i0 == 0x3055 && i1 == 0x3044) ? -714 : 0;
      case 0x59cb <<16| 0x3081: return (i0 == 0x59cb && i1 == 0x3081) ? 1681 : 0;
      case 0x305a <<16| 0x306b: return (i0 == 0x305a && i1 == 0x306b) ? 841 : 0;
      case 0x3059 <<16| 0x308b: return (i0 == 0x3059 && i1 == 0x308b) ? 6521 : 0;
      case 0x3067 <<16| 0x3059: return (i0 == 0x3067 && i1 == 0x3059) ? 1437 : 0;
      case 0x304b <<16| 0x3002: return (i0 == 0x304b && i1 == 0x3002) ? 2857 : 0;
      case 0x307e <<16| 0x308c: return (i0 == 0x307e && i1 == 0x308c) ? -793 : 0;
      case 0x65e5 <<16| 0x002c: return (i0 == 0x65e5 && i1 == 0x002c) ? 974 : 0;
      case 0x3053 <<16| 0x308d: return (i0 == 0x3053 && i1 == 0x308d) ? -2757 : 0;
      case 0x3042 <<16| 0x305f: return (i0 == 0x3042 && i1 == 0x305f) ? -2194 : 0;
      case 0x308c <<16| 0x305f: return (i0 == 0x308c && i1 == 0x305f) ? 1850 : 0;
      case 0x3048 <<16| 0x3068: return (i0 == 0x3048 && i1 == 0x3068) ? 1454 : 0;
      case 0x304c <<16| 0x002c: return (i0 == 0x304c && i1 == 0x002c) ? 1816 : 0;
      case 0x3066 <<16| 0x3082: return (i0 == 0x3066 && i1 == 0x3082) ? 302 : 0;
      case 0x3068 <<16| 0x3046: return (i0 == 0x3068 && i1 == 0x3046) ? -1387 : 0;
      case 0x308c <<16| 0x3066: return (i0 == 0x308c && i1 == 0x3066) ? 1375 : 0;
      case 0x5165 <<16| 0x308a: return (i0 == 0x5165 && i1 == 0x308a) ? 1232 : 0;
      case 0x306b <<16| 0x002c: return (i0 == 0x306b && i1 == 0x002c) ? -1021 : 0;
      default: return 0;
    }
  }

  /** trigram category cost for (n-3, n-2, n-1) */
  public static final int tc1(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case H <<6| O <<3| M: return -331;
      case M <<6| M <<3| H: return 187;
      case A <<6| A <<3| A: return 1093;
      case I <<6| H <<3| I: return 1169;
      case O <<6| O <<3| I: return -1832;
      case H <<6| H <<3| H: return 1029;
      case I <<6| O <<3| H: return -142;
      case H <<6| H <<3| M: return 580;
      case I <<6| O <<3| I: return -1015;
      case H <<6| I <<3| I: return 998;
      case H <<6| O <<3| H: return -390;
      case I <<6| O <<3| M: return 467;
      default: return 0;
    }
  }

  /** trigram category cost for (n-2, n-1, n) */
  public static final int tc2(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case I <<6| H <<3| I: return -1965;
      case O <<6| I <<3| I: return -2649;
      case H <<6| M <<3| M: return -1154;
      case K <<6| K <<3| H: return 703;
      case H <<6| H <<3| O: return 2088;
      case H <<6| I <<3| I: return -1023;
      default: return 0;
    }
  }

  /** trigram category cost for (n-1, n, n+1) */
  public static final int tc3(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case H <<6| H <<3| H: return 346;
      case H <<6| H <<3| I: return -341;
      case K <<6| O <<3| K: return -1009;
      case I <<6| O <<3| I: return -542;
      case I <<6| I <<3| H: return -825;
      case A <<6| A <<3| A: return -294;
      case K <<6| K <<3| A: return 491;
      case I <<6| I <<3| M: return -1035;
      case M <<6| H <<3| H: return -2694;
      case O <<6| H <<3| O: return -3393;
      case K <<6| K <<3| H: return -1217;
      case K <<6| H <<3| H: return -1216;
      case M <<6| H <<3| M: return -457;
      case M <<6| H <<3| O: return 123;
      case I <<6| H <<3| H: return 128;
      case I <<6| H <<3| I: return -3041;
      case M <<6| M <<3| H: return -471;
      case I <<6| H <<3| O: return -1935;
      case H <<6| O <<3| H: return -1486;
      case N <<6| N <<3| H: return -1689;
      case H <<6| I <<3| I: return -1088;
      case H <<6| I <<3| K: return 731;
      case N <<6| N <<3| O: return 662;
      default: return 0;
    }
  }

  /** trigram category cost for (n, n+1, n+2) */
  public static final int tc4(int i0, int i1, int i2) {
    switch(i0 <<6| i1 <<3| i2) {
      case M <<6| O <<3| M: return 841;
      case H <<6| H <<3| H: return -203;
      case H <<6| H <<3| I: return 1344;
      case H <<6| H <<3| K: return 365;
      case H <<6| H <<3| M: return -122;
      case I <<6| I <<3| H: return 321;
      case H <<6| H <<3| N: return 182;
      case I <<6| I <<3| I: return 1497;
      case H <<6| H <<3| O: return 669;
      case K <<6| K <<3| A: return 3386;
      case I <<6| O <<3| O: return 54;
      case M <<6| H <<3| H: return -405;
      case I <<6| I <<3| O: return 656;
      case M <<6| H <<3| I: return 201;
      case K <<6| K <<3| K: return 3065;
      case I <<6| H <<3| H: return 695;
      case M <<6| M <<3| H: return -241;
      case I <<6| H <<3| O: return -2324;
      case H <<6| O <<3| H: return 446;
      case M <<6| M <<3| M: return 661;
      case H <<6| I <<3| H: return 804;
      case H <<6| I <<3| I: return 679;
      case K <<6| A <<3| K: return 4845;
      default: return 0;
    }
  }

  /** trigram category (n-3, n-2, n-1) with context (n-2) cost */
  public static final int tq1(int i0, int i1, int i2, int i3) {
    switch(i0 <<9| i1 <<6| i2 <<3| i3) {
      case PB <<9| H <<6| I <<3| H: return -132;
      case PO <<9| H <<6| H <<3| H: return 281;
      case PB <<9| O <<6| H <<3| H: return 225;
      case PO <<9| I <<6| I <<3| H: return -68;
      case PB <<9| N <<6| H <<3| H: return -744;
      case PO <<9| H <<6| I <<3| H: return 249;
      case PB <<9| I <<6| H <<3| H: return 60;
      case PB <<9| H <<6| H <<3| H: return -227;
      case PB <<9| H <<6| H <<3| I: return 316;
      case PO <<9| A <<6| K <<3| K: return 482;
      case PB <<9| I <<6| I <<3| I: return 1595;
      case PB <<9| O <<6| O <<3| O: return -908;
      case PO <<9| I <<6| H <<3| I: return 200;
      default: return 0;
    }
  }

  /** trigram category (n-2, n-1, n) with context (n-2) cost */
  public static final int tq2(int i0, int i1, int i2, int i3) {
    switch(i0 <<9| i1 <<6| i2 <<3| i3) {
      case PB <<9| I <<6| H <<3| H: return -1401;
      case PB <<9| K <<6| A <<3| K: return -543;
      case PB <<9| O <<6| O <<3| O: return -5591;
      case PB <<9| I <<6| I <<3| I: return -1033;
      default: return 0;
    }
  }

  /** trigram category (n-3, n-2, n-1) with context (n-1) cost */
  public static final int tq3(int i0, int i1, int i2, int i3) {
    switch(i0 <<9| i1 <<6| i2 <<3| i3) {
      case PB <<9| H <<6| I <<3| H: return 222;
      case PB <<9| H <<6| I <<3| I: return -504;
      case PO <<9| H <<6| I <<3| I: return 997;
      case PO <<9| K <<6| A <<3| K: return 2792;
      case PO <<9| O <<6| I <<3| I: return -685;
      case PB <<9| H <<6| H <<3| H: return 478;
      case PO <<9| H <<6| H <<3| H: return 346;
      case PO <<9| H <<6| H <<3| I: return 1729;
      case PB <<9| H <<6| H <<3| M: return -1073;
      case PB <<9| I <<6| I <<3| H: return -116;
      case PB <<9| I <<6| I <<3| I: return -105;
      case PO <<9| I <<6| I <<3| H: return 1344;
      case PO <<9| O <<6| H <<3| H: return 110;
      case PO <<9| K <<6| K <<3| A: return 679;
      case PB <<9| M <<6| H <<3| I: return -863;
      case PB <<9| M <<6| H <<3| M: return -464;
      case PO <<9| H <<6| M <<3| H: return 481;
      case PO <<9| K <<6| H <<3| H: return 587;
      case PB <<9| O <<6| M <<3| H: return 620;
      case PO <<9| I <<6| H <<3| H: return 623;
      default: return 0;
    }
  }

  /** trigram category (n-2, n-1, n) with context (n-1) cost */
  public static final int tq4(int i0, int i1, int i2, int i3) {
    switch(i0 <<9| i1 <<6| i2 <<3| i3) {
      case PB <<9| H <<6| I <<3| I: return -966;
      case PO <<9| H <<6| H <<3| H: return -294;
      case PO <<9| H <<6| H <<3| I: return 2446;
      case PO <<9| K <<6| A <<3| K: return -8156;
      case PO <<9| I <<6| I <<3| H: return 626;
      case PO <<9| I <<6| I <<3| I: return -4007;
      case PO <<9| H <<6| H <<3| O: return 480;
      case PO <<9| H <<6| I <<3| H: return -1573;
      case PB <<9| H <<6| H <<3| H: return -721;
      case PO <<9| A <<6| K <<3| K: return 180;
      case PB <<9| I <<6| I <<3| H: return -607;
      case PB <<9| I <<6| I <<3| I: return -2181;
      case PO <<9| A <<6| A <<3| A: return -2763;
      case PO <<9| I <<6| H <<3| H: return 1935;
      case PB <<9| H <<6| H <<3| M: return -3604;
      case PO <<9| I <<6| H <<3| I: return -493;
      default: return 0;
    }
  }

  /** trigram character cost for (n-3, n-2, n-1) */
  public static final int tw1(int i0, int i1, int i2) {
    switch(i0 ^ i1 ^ i2) {
      case 0x6771 ^ 0x4eac ^ 0x90fd: return (i0 == 0x6771 && i1 == 0x4eac && i2 == 0x90fd) ? 2026 : 0;
      case 0x306b ^ 0x3064 ^ 0x3044: return (i0 == 0x306b && i1 == 0x3064 && i2 == 0x3044) ? -4681 : 0;
      default: return 0;
    }
  }

  /** trigram character cost for (n-2, n-1, n) */
  public static final int tw2(int i0, int i1, int i2) {
    switch(i0 ^ i1 ^ i2) {
      case 0x3060 ^ 0x3063 ^ 0x3066: return (i0 == 0x3060 && i1 == 0x3063 && i2 == 0x3066) ? -1049 : 0;
      case 0x3057 ^ 0x3087 ^ 0x3046: return (i0 == 0x3057 && i1 == 0x3087 && i2 == 0x3046) ? 3873 : 0;
      case 0x3068 ^ 0x3057 ^ 0x3066: return (i0 == 0x3068 && i1 == 0x3057 && i2 == 0x3066) ? -4657 : 0;
      case 0x3042 ^ 0x308b ^ 0x7a0b: return (i0 == 0x3042 && i1 == 0x308b && i2 == 0x7a0b) ? -2049 : 0;
      case 0x5927 ^ 0x304d ^ 0x306a: return (i0 == 0x5927 && i1 == 0x304d && i2 == 0x306a) ? -1255 : 0;
      case 0x305d ^ 0x306e ^ 0x5f8c: return (i0 == 0x305d && i1 == 0x306e && i2 == 0x5f8c) ? -4430 : 0;
      case 0x3068 ^ 0x3082 ^ 0x306b: return (i0 == 0x3068 && i1 == 0x3082 && i2 == 0x306b) ? -4517 : 0;
      case 0x3053 ^ 0x308d ^ 0x304c: return (i0 == 0x3053 && i1 == 0x308d && i2 == 0x304c) ? -2434 : 0;
      case 0x5bfe ^ 0x3057 ^ 0x3066: return (i0 == 0x5bfe && i1 == 0x3057 && i2 == 0x3066) ? -2721 : 0;
      case 0x3082 ^ 0x306e ^ 0x3067: return (i0 == 0x3082 && i1 == 0x306e && i2 == 0x3067) ? 1882 : 0;
      case 0x793e ^ 0x4f1a ^ 0x515a: return (i0 == 0x793e && i1 == 0x4f1a && i2 == 0x515a) ? -3216 : 0;
      case 0x3066 ^ 0x3044 ^ 0x305f: return (i0 == 0x3066 && i1 == 0x3044 && i2 == 0x305f) ? 1833 : 0;
      case 0x4e00 ^ 0x6c17 ^ 0x306b: return (i0 == 0x4e00 && i1 == 0x6c17 && i2 == 0x306b) ? -792 : 0;
      case 0x3044 ^ 0x3063 ^ 0x305f: return (i0 == 0x3044 && i1 == 0x3063 && i2 == 0x305f) ? -1256 : 0;
      case 0x521d ^ 0x3081 ^ 0x3066: return (i0 == 0x521d && i1 == 0x3081 && i2 == 0x3066) ? -1512 : 0;
      case 0x540c ^ 0x6642 ^ 0x306b: return (i0 == 0x540c && i1 == 0x6642 && i2 == 0x306b) ? -8097 : 0;
      default: return 0;
    }
  }

  /** trigram character cost for (n-1, n, n+1) */
  public static final int tw3(int i0, int i1, int i2) {
    switch(i0 ^ i1 ^ i2) {
      case 0x306e ^ 0x3067 ^ 0x3001: return (i0 == 0x306e && i1 == 0x3067 && i2 == 0x3001) ? -727 : 0;
      case 0x3068 ^ 0x3057 ^ 0x3066: return (i0 == 0x3068 && i1 == 0x3057 && i2 == 0x3066) ? -4314 : 0;
      case 0x306e ^ 0x3082 ^ 0x306e: return (i0 == 0x306e && i1 == 0x3082 && i2 == 0x306e) ? -600 : 0;
      case 0x306b ^ 0x3068 ^ 0x3063: return (i0 == 0x306b && i1 == 0x3068 && i2 == 0x3063) ? -5989 : 0;
      case 0x3044 ^ 0x305f ^ 0x3060: return (i0 == 0x3044 && i1 == 0x305f && i2 == 0x3060) ? -1734 : 0;
      case 0x306b ^ 0x3064 ^ 0x3044: return (i0 == 0x306b && i1 == 0x3064 && i2 == 0x3044) ? -5483 : 0;
      case 0x3057 ^ 0x3066 ^ 0x3044: return (i0 == 0x3057 && i1 == 0x3066 && i2 == 0x3044) ? 1314 : 0;
      case 0x306e ^ 0x3067 ^ 0x002c: return (i0 == 0x306e && i1 == 0x3067 && i2 == 0x002c) ? -727 : 0;
      case 0x5341 ^ 0x4e8c ^ 0x6708: return (i0 == 0x5341 && i1 == 0x4e8c && i2 == 0x6708) ? -2287 : 0;
      case 0x308c ^ 0x304b ^ 0x3089: return (i0 == 0x308c && i1 == 0x304b && i2 == 0x3089) ? -3752 : 0;
      case 0x306b ^ 0x5f53 ^ 0x305f: return (i0 == 0x306b && i1 == 0x5f53 && i2 == 0x305f) ? -6247 : 0;
      default: return 0;
    }
  }

  /** trigram character cost for (n, n+1, n+2) */
  public static final int tw4(int i0, int i1, int i2) {
    switch(i0 ^ i1 ^ i2) {
      case 0x304b ^ 0x3089 ^ 0x306a: return (i0 == 0x304b && i1 == 0x3089 && i2 == 0x306a) ? -2348 : 0;
      case 0x307e ^ 0x3057 ^ 0x305f: return (i0 == 0x307e && i1 == 0x3057 && i2 == 0x305f) ? 5543 : 0;
      case 0x3068 ^ 0x3044 ^ 0x3046: return (i0 == 0x3068 && i1 == 0x3044 && i2 == 0x3046) ? 1349 : 0;
      case 0x3044 ^ 0x3046 ^ 0x002e: return (i0 == 0x3044 && i1 == 0x3046 && i2 == 0x002e) ? 8576 : 0;
      case 0x3088 ^ 0x3046 ^ 0x3068: return (i0 == 0x3088 && i1 == 0x3046 && i2 == 0x3068) ? -4258 : 0;
      case 0x3088 ^ 0x308b ^ 0x3068: return (i0 == 0x3088 && i1 == 0x308b && i2 == 0x3068) ? 5865 : 0;
      case 0x305f ^ 0x304c ^ 0x002c: return (i0 == 0x305f && i1 == 0x304c && i2 == 0x002c) ? 1516 : 0;
      case 0x3066 ^ 0x3044 ^ 0x308b: return (i0 == 0x3066 && i1 == 0x3044 && i2 == 0x308b) ? 1538 : 0;
      case 0x3057 ^ 0x3066 ^ 0x3044: return (i0 == 0x3057 && i1 == 0x3066 && i2 == 0x3044) ? 2958 : 0;
      case 0x3044 ^ 0x3046 ^ 0x3002: return (i0 == 0x3044 && i1 == 0x3046 && i2 == 0x3002) ? 8576 : 0;
      case 0x307e ^ 0x305b ^ 0x3093: return (i0 == 0x307e && i1 == 0x305b && i2 == 0x3093) ? 1097 : 0;
      case 0x305f ^ 0x304c ^ 0x3001: return (i0 == 0x305f && i1 == 0x304c && i2 == 0x3001) ? 1516 : 0;
      default: return 0;
    }
  }

  /** unigram category cost for n-3 */
  public static final int uc1(int i0) {
    switch(i0) {
      case M: return 645;
      case O: return -505;
      case K: return 93;
      case A: return 484;
      default: return 0;
    }
  }

  /** unigram category cost for n-2 */
  public static final int uc2(int i0) {
    switch(i0) {
      case M: return 3987;
      case N: return 5775;
      case O: return 646;
      case H: return 1059;
      case I: return 409;
      case A: return 819;
      default: return 0;
    }
  }

  /** unigram category cost for n-1 */
  public static final int uc3(int i0) {
    switch(i0) {
      case A: return -1370;
      case I: return 2311;
      default: return 0;
    }
  }

  /** unigram category cost for n */
  public static final int uc4(int i0) {
    switch(i0) {
      case M: return 3565;
      case N: return 3876;
      case O: return 6646;
      case H: return 1809;
      case I: return -1032;
      case K: return -3450;
      case A: return -2643;
      default: return 0;
    }
  }

  /** unigram category cost for n+1 */
  public static final int uc5(int i0) {
    switch(i0) {
      case M: return 539;
      case O: return -831;
      case H: return 313;
      case I: return -1238;
      case K: return -799;
      default: return 0;
    }
  }

  /** unigram category cost for n+2 */
  public static final int uc6(int i0) {
    switch(i0) {
      case M: return 247;
      case O: return -387;
      case H: return -506;
      case I: return -253;
      case K: return 87;
      default: return 0;
    }
  }

  /** unigram context cost for n-3 */
  public static final int up1(int i0) {
    switch(i0) {
      case PO: return -214;
      default: return 0;
    }
  }

  /** unigram context cost for n-2 */
  public static final int up2(int i0) {
    switch(i0) {
      case PB: return 69;
      case PO: return 935;
      default: return 0;
    }
  }

  /** unigram context cost for n-1 */
  public static final int up3(int i0) {
    switch(i0) {
      case PB: return 189;
      default: return 0;
    }
  }

  /** unigram category (n-3) with context (n-3) cost */
  public static final int uq1(int i0, int i1) {
    switch(i0 <<3| i1) {
      case PB <<3| H: return 21;
      case PB <<3| I: return -12;
      case PB <<3| K: return -99;
      case PB <<3| N: return 142;
      case PB <<3| O: return -56;
      case PO <<3| H: return -95;
      case PO <<3| I: return 477;
      case PO <<3| K: return 410;
      case PO <<3| O: return -2422;
      default: return 0;
    }
  }

  /** unigram category (n-2) with context (n-2) cost */
  public static final int uq2(int i0, int i1) {
    switch(i0 <<3| i1) {
      case PB <<3| H: return 216;
      case PB <<3| I: return 113;
      case PO <<3| K: return 1759;
      default: return 0;
    }
  }

  /** unigram category (n-1) with context (n-1) cost */
  public static final int uq3(int i0, int i1) {
    switch(i0 <<3| i1) {
      case PB <<3| H: return 42;
      case PB <<3| I: return 1913;
      case PB <<3| K: return -7198;
      case PB <<3| M: return 3160;
      case PB <<3| N: return 6427;
      case PB <<3| O: return 14761;
      case PO <<3| I: return -827;
      case PB <<3| A: return -479;
      case PO <<3| N: return -3212;
      default: return 0;
    }
  }

  /** unigram character cost for n-3 */
  public static final int uw1(int i0) {
    switch(i0) {
      case 0x4eac: return -268;
      case 0x3042: return -941;
      case 0x59d4: return 729;
      case 0x3046: return -127;
      case 0x304c: return -553;
      case 0x304d: return 121;
      case 0xff62: return -463;
      case 0x3053: return 505;
      case 0xff65: return -135;
      case 0x5927: return 561;
      case 0x533a: return -912;
      case 0x5e02: return -411;
      case 0x3001: return 156;
      case 0x56fd: return -460;
      case 0x5348: return 871;
      case 0x3067: return -201;
      case 0x3068: return -547;
      case 0x3069: return -123;
      case 0x002c: return 156;
      case 0x306b: return -789;
      case 0x300c: return -463;
      case 0x306e: return -185;
      case 0x306f: return -847;
      case 0x65e5: return -141;
      case 0x751f: return -408;
      case 0x7406: return 361;
      case 0x90fd: return -718;
      case 0x3082: return -466;
      case 0x3084: return -470;
      case 0x3088: return 182;
      case 0x3089: return -292;
      case 0x770c: return -386;
      case 0x308a: return 208;
      case 0x4e3b: return -402;
      case 0x308c: return 169;
      case 0x3092: return -446;
      case 0x3093: return -137;
      case 0x30fb: return -135;
      default: return 0;
    }
  }

  /** unigram character cost for n-2 */
  public static final int uw2(int i0) {
    switch(i0) {
      case 0x63fa: return -1033;
      case 0x5e02: return -813;
      case 0x3082: return -1263;
      case 0x4f1a: return 978;
      case 0x3084: return -402;
      case 0x4fdd: return 362;
      case 0x3088: return 1639;
      case 0x6700: return -630;
      case 0x308a: return -579;
      case 0x521d: return -3025;
      case 0x308b: return -694;
      case 0x308c: return 571;
      case 0x6587: return -1355;
      case 0x7b2c: return 810;
      case 0x5165: return 548;
      case 0x3092: return -2516;
      case 0x3093: return 2095;
      case 0x81ea: return -1353;
      case 0x30a2: return -587;
      case 0x671d: return -1843;
      case 0x002c: return -829;
      case 0x30ab: return 306;
      case 0x30ad: return 568;
      case 0x4e8b: return 492;
      case 0x672c: return -1650;
      case 0x897f: return -744;
      case 0x65b0: return -1682;
      case 0xff62: return -645;
      case 0xff63: return 3145;
      case 0x3001: return -829;
      case 0x898b: return -3874;
      case 0x30c3: return 831;
      case 0xff6f: return 831;
      case 0x5317: return -3414;
      case 0x3007: return 892;
      case 0xff71: return -587;
      case 0x5c0f: return -2009;
      case 0x5b50: return -1519;
      case 0x300c: return -645;
      case 0xff76: return 306;
      case 0x300d: return 3145;
      case 0x76ee: return -1584;
      case 0xff77: return 568;
      case 0x958b: return 1758;
      case 0x76f8: return -242;
      case 0x9593: return -1257;
      case 0x526f: return -1566;
      case 0x5927: return -1769;
      case 0x5b66: return 760;
      case 0x5929: return -865;
      case 0x592a: return -483;
      case 0x7406: return 752;
      case 0x4eba: return -123;
      case 0x533a: return -422;
      case 0x770c: return -1165;
      case 0x65e5: return -1815;
      case 0x7acb: return -763;
      case 0x6b21: return -2378;
      case 0x4e09: return -758;
      case 0x5e74: return -1060;
      case 0x4e0d: return -2150;
      case 0x5f37: return 1067;
      case 0x6771: return -931;
      case 0x8fbc: return 3041;
      case 0x4e16: return -302;
      case 0x3042: return -538;
      case 0x884c: return 838;
      case 0x3044: return 505;
      case 0x3046: return 134;
      case 0x653f: return 1522;
      case 0x304a: return -502;
      case 0x304b: return 1454;
      case 0x304c: return -856;
      case 0x624b: return -1519;
      case 0x304f: return -412;
      case 0x3053: return 1141;
      case 0x4e2d: return -968;
      case 0x3055: return 878;
      case 0x3056: return 540;
      case 0x660e: return -1462;
      case 0x3057: return 1529;
      case 0x767a: return 529;
      case 0x5b9f: return 1023;
      case 0x3059: return -675;
      case 0x7c73: return 509;
      case 0x305b: return 300;
      case 0x305d: return -1011;
      case 0x305f: return 188;
      case 0x3060: return 1837;
      case 0x6c11: return -180;
      case 0x4e3b: return -861;
      case 0x3064: return -949;
      case 0x3066: return -291;
      case 0x679c: return -665;
      case 0x3067: return -268;
      case 0x6c17: return -1740;
      case 0x3068: return -981;
      case 0x3069: return 1273;
      case 0x306a: return 1063;
      case 0x8b70: return 1198;
      case 0x306b: return -1764;
      case 0x306e: return 130;
      case 0x306f: return -409;
      case 0x3072: return -1273;
      case 0x8abf: return 1010;
      case 0x3079: return 1261;
      case 0x307e: return 600;
      default: return 0;
    }
  }

  /** unigram character cost for n-1 */
  public static final int uw3(int i0) {
    switch(i0) {
      case 0x0031: return -800;
      case 0x4f4e: return 811;
      case 0x524d: return 2286;
      case 0x95a2: return -1282;
      case 0x4f55: return 4265;
      case 0x4f5c: return -361;
      case 0x674e: return 3094;
      case 0x6751: return 364;
      case 0x8cbb: return 1777;
      case 0x53e3: return 483;
      case 0x8fbc: return -1504;
      case 0x7acb: return -960;
      case 0x3001: return 4889;
      case 0x5b66: return -1356;
      case 0x7dcf: return 1163;
      case 0x3005: return -2311;
      case 0x526f: return 4437;
      case 0x3007: return 5827;
      case 0x65e5: return 2099;
      case 0x65e7: return 5792;
      case 0x53f3: return 1233;
      case 0x002c: return 4889;
      case 0x300d: return 2670;
      case 0x7dda: return 1255;
      case 0x5e73: return -1804;
      case 0x5e74: return 2416;
      case 0x3013: return -3573;
      case 0x4e00: return -1619;
      case 0x68ee: return 2438;
      case 0x77e5: return -1528;
      case 0x6771: return -805;
      case 0x56fd: return 642;
      case 0x5404: return 3588;
      case 0x4e0b: return -1759;
      case 0x5408: return -241;
      case 0x6d77: return -495;
      case 0x5e83: return -1030;
      case 0x975e: return 2066;
      case 0x540c: return 3906;
      case 0x5b89: return -423;
      case 0x7c73: return 7767;
      case 0x6307: return -3973;
      case 0x4e16: return -2087;
      case 0x529b: return 365;
      case 0x7684: return 7313;
      case 0x80fd: return 725;
      case 0x4e21: return 3815;
      case 0x6c0f: return 2613;
      case 0x6c11: return -1694;
      case 0x5e9c: return 1605;
      case 0x5b9f: return -1008;
      case 0x601d: return -1291;
      case 0x4e2d: return 653;
      case 0x3042: return -2696;
      case 0x3044: return 1006;
      case 0x5ea6: return 1452;
      case 0x3046: return 2342;
      case 0x6027: return 1822;
      case 0x3048: return 1983;
      case 0x304a: return -4864;
      case 0x304b: return -1163;
      case 0x6628: return -661;
      case 0x304c: return 3271;
      case 0x751f: return -273;
      case 0x4e3b: return -758;
      case 0x304f: return 1004;
      case 0x3051: return 388;
      case 0x3052: return 401;
      case 0x5bb6: return 1078;
      case 0x3053: return -3552;
      case 0x3054: return -3116;
      case 0x3055: return -1058;
      case 0x7528: return 914;
      case 0x3057: return -395;
      case 0x5143: return 4858;
      case 0x3059: return 584;
      case 0x901a: return -1136;
      case 0x305b: return 3685;
      case 0x305d: return -5228;
      case 0x7b2c: return 1201;
      case 0x305f: return 842;
      case 0x3061: return -521;
      case 0x3063: return -1444;
      case 0x3064: return -1081;
      case 0x3066: return 6167;
      case 0x6642: return -1248;
      case 0x3067: return 2318;
      case 0x3068: return 1691;
      case 0x753a: return 1215;
      case 0x3069: return -899;
      case 0x306a: return -2788;
      case 0x306b: return 2745;
      case 0x52d5: return -949;
      case 0x306e: return 4056;
      case 0x306f: return 4555;
      case 0x52d9: return -1872;
      case 0x515a: return 3593;
      case 0x3072: return -2171;
      case 0x4fdd: return -2439;
      case 0x79c1: return 4231;
      case 0x3075: return -1798;
      case 0x3078: return 1199;
      case 0x307b: return -5516;
      case 0x307e: return -4384;
      case 0x5168: return 1574;
      case 0x307f: return -120;
      case 0x3081: return 1205;
      case 0x516c: return -3030;
      case 0x3082: return 2323;
      case 0x516d: return 755;
      case 0x3084: return -788;
      case 0x5171: return -1880;
      case 0x3088: return -202;
      case 0x3089: return 727;
      case 0x8eca: return 1835;
      case 0x308a: return 649;
      case 0x308b: return 5905;
      case 0x308c: return 2773;
      case 0x8ecd: return 1375;
      case 0x308f: return -1207;
      case 0x3092: return 6620;
      case 0x91d1: return 2163;
      case 0x3093: return -518;
      case 0x696d: return 484;
      case 0x7269: return 461;
      case 0x5efa: return -2352;
      case 0xff11: return -800;
      case 0x5186: return 5807;
      case 0x4e88: return -1193;
      case 0x4e8c: return 974;
      case 0x30a2: return 551;
      case 0x6c7a: return -1073;
      case 0x518d: return 3095;
      case 0x76f4: return -1835;
      case 0x548c: return -837;
      case 0x578b: return 1389;
      case 0x7279: return -3850;
      case 0x82f1: return 785;
      case 0x5c0f: return -513;
      case 0x5316: return 1327;
      case 0x5c11: return -3102;
      case 0x5317: return -1038;
      case 0x7cfb: return 3066;
      case 0x30b0: return 1319;
      case 0x7701: return 792;
      case 0x5916: return -241;
      case 0x7d04: return 3663;
      case 0x9078: return -681;
      case 0x30b9: return 874;
      case 0x8005: return 6457;
      case 0x770c: return 6293;
      case 0x7a0e: return 401;
      case 0x30c3: return -1350;
      case 0x30c8: return 521;
      case 0x7121: return 979;
      case 0x7d1a: return 1384;
      case 0x4eba: return 2742;
      case 0x533a: return 4646;
      case 0x6238: return -488;
      case 0x5343: return -2309;
      case 0x6838: return 5156;
      case 0x4eca: return 792;
      case 0x5348: return -783;
      case 0x30e0: return 1109;
      case 0x653f: return -2013;
      case 0x4ed6: return 1889;
      case 0x5354: return -1006;
      case 0x30eb: return 1591;
      case 0x30ed: return 2201;
      case 0xff63: return 2670;
      case 0xff65: return -3794;
      case 0x5f53: return -3885;
      case 0x30f3: return 278;
      case 0x54e1: return 4513;
      case 0x4ee5: return -1368;
      case 0xff6f: return -1350;
      case 0x30fb: return -3794;
      case 0x8abf: return -562;
      case 0xff71: return 551;
      case 0x6559: return -1479;
      case 0x5dde: return 1155;
      case 0x6cd5: return 1868;
      case 0x66dc: return -951;
      case 0xff7d: return 874;
      case 0x2212: return -1723;
      case 0x99c5: return 1620;
      case 0x90ce: return 1026;
      case 0xff84: return 521;
      case 0x6570: return 3222;
      case 0xff91: return 1109;
      case 0x5206: return 457;
      case 0x5e02: return 3197;
      case 0x81ea: return -2869;
      case 0x90e1: return 4404;
      case 0xff99: return 1591;
      case 0x6700: return -937;
      case 0x7d71: return -4229;
      case 0xff9b: return 2201;
      case 0xff9d: return 278;
      case 0x90e8: return 1200;
      case 0x6587: return -1489;
      case 0x6708: return 4125;
      case 0x96e8: return 2009;
      case 0x521d: return 2475;
      case 0x5f97: return 1905;
      case 0x9577: return 421;
      case 0x5225: return 1129;
      case 0x96fb: return -1045;
      case 0x671f: return 360;
      case 0x898b: return 1044;
      case 0x5834: return 1219;
      case 0x958b: return -1432;
      case 0x65b0: return 1764;
      case 0x59bb: return 2016;
      case 0x9593: return 1302;
      case 0x8ca1: return -733;
      default: return 0;
    }
  }

  /** unigram character cost for n */
  public static final int uw4(int i0) {
    switch(i0) {
      case 0x822c: return -852;
      case 0x524d: return 1623;
      case 0x4f53: return -1286;
      case 0x5b50: return -4802;
      case 0x4f5c: return 530;
      case 0x56de: return 1500;
      case 0x8fbc: return -3370;
      case 0x7acb: return -2112;
      case 0x3001: return 3930;
      case 0x3002: return 3508;
      case 0x5b66: return -1397;
      case 0x7dcf: return 940;
      case 0x526f: return 3879;
      case 0x3007: return 4999;
      case 0x884c: return -792;
      case 0x65e5: return 1798;
      case 0x6765: return -442;
      case 0x300c: return 1895;
      case 0x002c: return 3930;
      case 0x300d: return 3798;
      case 0x002e: return 3508;
      case 0x7dda: return -994;
      case 0x8fd1: return 929;
      case 0x5e74: return 374;
      case 0x3013: return -5156;
      case 0x5cf6: return -2056;
      case 0x4e00: return -2069;
      case 0x56fd: return -619;
      case 0x8cde: return 730;
      case 0x5e81: return -4556;
      case 0x5408: return -1834;
      case 0x8b66: return -1184;
      case 0x7c73: return 2937;
      case 0x7f72: return 749;
      case 0x5712: return -1200;
      case 0x8b70: return -244;
      case 0x529b: return -302;
      case 0x7684: return 2586;
      case 0x80fd: return -730;
      case 0x7387: return 672;
      case 0x5b9a: return -1057;
      case 0x6c0f: return 5388;
      case 0x6c11: return -2716;
      case 0x6c17: return -910;
      case 0x4e2d: return 2210;
      case 0x3042: return 4752;
      case 0x3044: return -3435;
      case 0x3046: return -640;
      case 0x6027: return 553;
      case 0x3048: return -2514;
      case 0x5730: return 866;
      case 0x304a: return 2405;
      case 0x304b: return 530;
      case 0x304c: return 6006;
      case 0x304d: return -4482;
      case 0x751f: return -1286;
      case 0x304e: return -3821;
      case 0x304f: return -3788;
      case 0x3051: return -4376;
      case 0x7523: return -1101;
      case 0x3052: return -4734;
      case 0x3053: return 2255;
      case 0x3054: return 1979;
      case 0x3055: return 2864;
      case 0x3057: return -843;
      case 0x3058: return -2506;
      case 0x3059: return -731;
      case 0x305a: return 1251;
      case 0x305b: return 181;
      case 0x305d: return 4091;
      case 0x5148: return 601;
      case 0x7530: return -2900;
      case 0x7b2c: return 788;
      case 0x305f: return 5034;
      case 0x3060: return 5408;
      case 0x3061: return -3654;
      case 0x3063: return -5882;
      case 0x3064: return -1659;
      case 0x3066: return 3994;
      case 0x6642: return 1829;
      case 0x3067: return 7410;
      case 0x3068: return 4547;
      case 0x753a: return 1826;
      case 0x306a: return 5433;
      case 0x306b: return 6499;
      case 0x306c: return 1853;
      case 0x52d5: return -740;
      case 0x306d: return 1413;
      case 0x306e: return 7396;
      case 0x9928: return -1984;
      case 0x306f: return 8578;
      case 0x3070: return 1940;
      case 0x52d9: return -2715;
      case 0x515a: return -2006;
      case 0x3072: return 4249;
      case 0x3073: return -4134;
      case 0x3075: return 1345;
      case 0x3078: return 6665;
      case 0x3079: return -744;
      case 0x307b: return 1464;
      case 0x307e: return 1051;
      case 0x307f: return -2082;
      case 0x3080: return -882;
      case 0x3081: return -5046;
      case 0x3082: return 4169;
      case 0x3083: return -2666;
      case 0x3084: return 2795;
      case 0x58eb: return -1413;
      case 0x5171: return -1212;
      case 0x3087: return -1544;
      case 0x3088: return 3351;
      case 0x3089: return -2922;
      case 0x8eca: return -1481;
      case 0x308a: return -9726;
      case 0x2015: return -4841;
      case 0x308b: return -14896;
      case 0x308c: return -2613;
      case 0x8ecd: return 1158;
      case 0x308d: return -4570;
      case 0x308f: return -1783;
      case 0x91ce: return -1100;
      case 0x3092: return 13150;
      case 0x3093: return -2352;
      case 0x696d: return -1043;
      case 0x9053: return -1291;
      case 0x7269: return -735;
      case 0x5bfa: return -809;
      case 0x5185: return 584;
      case 0x5186: return 788;
      case 0x4e88: return 782;
      case 0x76ee: return 922;
      case 0x4e8b: return -190;
      case 0x9ad8: return 2120;
      case 0x548c: return -681;
      case 0x9662: return -2297;
      case 0x4e95: return -1768;
      case 0x30ab: return 2145;
      case 0x5c0f: return 1910;
      case 0x5316: return 776;
      case 0x7cfb: return 786;
      case 0x7403: return -1267;
      case 0x7701: return -3485;
      case 0x6e08: return -543;
      case 0x30b3: return 1789;
      case 0x591a: return 1067;
      case 0x7d04: return 2171;
      case 0x9078: return 2596;
      case 0x8005: return 2145;
      case 0x30bb: return 1287;
      case 0x770c: return 2997;
      case 0x5927: return 571;
      case 0x30c3: return -724;
      case 0x6821: return -360;
      case 0x30c8: return -403;
      case 0x6ca2: return -939;
      case 0x4eba: return 1036;
      case 0x533a: return 4517;
      case 0x652f: return 856;
      case 0x6539: return 787;
      case 0x9996: return 1749;
      case 0x9818: return -1659;
      case 0x969b: return -2604;
      case 0x6240: return -1566;
      case 0x30e1: return -1635;
      case 0x653f: return 2182;
      case 0x5c4b: return -1328;
      case 0x30e9: return -881;
      case 0x8f2a: return -1433;
      case 0x30ea: return -541;
      case 0x5354: return 1013;
      case 0x30eb: return -856;
      case 0xff62: return 1895;
      case 0xff63: return 3798;
      case 0xff65: return -4371;
      case 0x30f3: return -3637;
      case 0x8c37: return -1000;
      case 0x54e1: return -910;
      case 0x4ee5: return 544;
      case 0xff6f: return -724;
      case 0xff70: return -11870;
      case 0x5ddd: return -2667;
      case 0x30fb: return -4371;
      case 0x6559: return 704;
      case 0x30fc: return -11870;
      case 0x7d4c: return 1146;
      case 0xff76: return 2145;
      case 0x5668: return -851;
      case 0xff7a: return 1789;
      case 0xff7e: return 1287;
      case 0x5074: return 4292;
      case 0x5c71: return -1500;
      case 0x90ce: return -4866;
      case 0xff84: return -403;
      case 0x984c: return -792;
      case 0xff92: return -1635;
      case 0x5e02: return 2771;
      case 0xff97: return -881;
      case 0xff98: return -541;
      case 0xff99: return -856;
      case 0x6700: return 845;
      case 0x7d71: return -1169;
      case 0xff9d: return -3637;
      case 0x6587: return 522;
      case 0x5f8c: return 456;
      case 0x7a7a: return -867;
      case 0x6708: return -9066;
      case 0x4f1a: return 950;
      case 0x521d: return 1347;
      case 0x9577: return 357;
      case 0x90fd: return 1192;
      case 0x611f: return 916;
      case 0x96fb: return -878;
      case 0x9280: return -2213;
      case 0x898f: return 792;
      case 0x6728: return -485;
      case 0x5834: return -1410;
      case 0x9593: return -2344;
      case 0x53c2: return 1555;
      case 0x5841: return -2094;
      case 0x65b9: return -856;
      default: return 0;
    }
  }

  /** unigram character cost for n+1 */
  public static final int uw5(int i0) {
    switch(i0) {
      case 0x307f: return 502;
      case 0x5e02: return -2991;
      case 0x0031: return -514;
      case 0x3081: return 865;
      case 0x3083: return 3350;
      case 0x4f1a: return -1153;
      case 0x515a: return -654;
      case 0x3087: return 854;
      case 0x52d9: return 3519;
      case 0x308a: return -208;
      case 0x308b: return 429;
      case 0x308c: return 504;
      case 0x5d50: return -1304;
      case 0x7530: return 240;
      case 0x308f: return 419;
      case 0x90ce: return -368;
      case 0x6708: return -4353;
      case 0x3092: return -1264;
      case 0x3093: return 327;
      case 0x753a: return -3912;
      case 0x984c: return 2368;
      case 0x7d71: return 1955;
      case 0x7a7a: return -813;
      case 0x30a4: return 241;
      case 0x5e2d: return 921;
      case 0x002c: return 465;
      case 0x002e: return -299;
      case 0x9928: return -689;
      case 0x65b0: return -1682;
      case 0xff62: return 363;
      case 0x9577: return 786;
      case 0x3001: return 465;
      case 0x3002: return -299;
      case 0x67fb: return 932;
      case 0xff72: return 241;
      case 0x300c: return 363;
      case 0x4eac: return 722;
      case 0x76f8: return 1319;
      case 0xe005: return -32768;
      case 0x9593: return 1191;
      case 0x005d: return -2762;
      case 0x5927: return -1296;
      case 0x5b66: return -548;
      case 0x7701: return -1052;
      case 0x793e: return -278;
      case 0x533a: return -901;
      case 0x770c: return -4003;
      case 0x30eb: return 451;
      case 0x65e5: return 218;
      case 0x6a5f: return -1508;
      case 0xff99: return 451;
      case 0x8005: return -2233;
      case 0x5e74: return 1763;
      case 0xff9d: return -343;
      case 0x30f3: return -343;
      case 0x9078: return -1018;
      case 0x3042: return 1655;
      case 0x6240: return -814;
      case 0x3044: return 331;
      case 0x3046: return -503;
      case 0x683c: return 1356;
      case 0x3048: return 1199;
      case 0x304a: return 527;
      case 0x304b: return 647;
      case 0x304c: return -421;
      case 0x304d: return 1624;
      case 0x304e: return 1971;
      case 0x304f: return 312;
      case 0x54e1: return 2104;
      case 0x3052: return -983;
      case 0x5b9a: return 1785;
      case 0x4e2d: return -871;
      case 0x3055: return -1537;
      case 0x3057: return -1371;
      case 0x8a9e: return -1073;
      case 0x3059: return -852;
      case 0x6319: return 1618;
      case 0x601d: return 872;
      case 0x8868: return 663;
      case 0x6c0f: return -1347;
      case 0x3060: return -1186;
      case 0x3061: return 1093;
      case 0x7684: return -3149;
      case 0x3063: return 52;
      case 0x3064: return 921;
      case 0x3066: return -18;
      case 0xff11: return -514;
      case 0x3067: return -850;
      case 0x3068: return -127;
      case 0x3069: return 1682;
      case 0x306a: return -787;
      case 0x8b70: return 1219;
      case 0x306b: return -1224;
      case 0x306e: return -635;
      case 0x306f: return -578;
      case 0x7814: return -997;
      case 0x3079: return 1001;
      case 0x544a: return 848;
      default: return 0;
    }
  }

  /** unigram character cost for n+2 */
  public static final int uw6(int i0) {
    switch(i0) {
      case 0x0031: return -270;
      case 0xe004: return 306;
      case 0x3042: return -307;
      case 0x7a7a: return -822;
      case 0x59d4: return 798;
      case 0x3046: return 189;
      case 0x696d: return -697;
      case 0x304b: return 241;
      case 0x304c: return -73;
      case 0x4f1a: return 624;
      case 0x304f: return -121;
      case 0x4e00: return -277;
      case 0x90ce: return 1082;
      case 0x3053: return -200;
      case 0x3058: return 1782;
      case 0x533a: return 1792;
      case 0x3059: return 383;
      case 0x5b66: return -960;
      case 0x5e02: return 887;
      case 0xff11: return -270;
      case 0x305f: return -428;
      case 0x3001: return 227;
      case 0x3002: return 808;
      case 0x3063: return 573;
      case 0x9023: return 463;
      case 0x3066: return -1014;
      case 0x3067: return 101;
      case 0x3068: return -105;
      case 0x002c: return 227;
      case 0x306a: return -253;
      case 0x306b: return -149;
      case 0x5f8c: return 535;
      case 0x002e: return 808;
      case 0x306e: return -417;
      case 0x306f: return -236;
      case 0x798f: return 974;
      case 0x76f8: return 753;
      case 0x4e2d: return 201;
      case 0x5e83: return -695;
      case 0x3082: return -206;
      case 0x793e: return -507;
      case 0x54e1: return -1212;
      case 0xff99: return -673;
      case 0x524d: return 302;
      case 0x4ef6: return -800;
      case 0x308a: return 187;
      case 0x308b: return -135;
      case 0xff9d: return -496;
      case 0x30eb: return -673;
      case 0x3092: return 195;
      case 0x30f3: return -496;
      case 0x8005: return 1811;
      default: return 0;
    }
  }
}
