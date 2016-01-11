package com.sankuai.xm.search.tool;

import com.sankuai.xm.search.domain.trietree.Node;
import com.sankuai.xm.search.domain.trietree.TrieTree;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author zhanghao
 * @version 1.0
 * @created 15/12/3
 */
public class PinyinUtil {

    private static final Logger log = LoggerFactory.getLogger(PinyinUtil.class);

    private static volatile PinyinUtil PINYIN_INSTANCE;

    private static final int MAX_PINYIN_SET_SIZE = 30;

    private static final HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();

    private static String[] data1;
    private static int[] data2;
    private static int length = 397;

    private TrieTree tree;

    private Map<String, List<String>> mpyMap;

    private Map<String, List<String>> pyMap;

    private PinyinUtil() {
        initData();
    }

    public static PinyinUtil getInstance() {
        if ( PINYIN_INSTANCE == null ) {
            synchronized ( PinyinUtil.class ) {
                if ( PINYIN_INSTANCE == null ) {
                    PINYIN_INSTANCE = new PinyinUtil();
                }
            }
        }
        return PINYIN_INSTANCE;
    }


    static {
        pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        data1 = new String[length];
        data2 = new int[length];
        data1[0] = "a";
        data2[0] = 20319;
        data1[1] = "ai";
        data2[1] = 20317;
        data1[2] = "an";
        data2[2] = 20304;
        data1[3] = "ang";
        data2[3] = 20295;
        data1[4] = "ang";
        data2[4] = 20295;
        data1[5] = "ao";
        data2[5] = 20292;
        data1[6] = "ba";
        data2[6] = 20283;
        data1[7] = "bai";
        data2[7] = 20265;
        data1[8] = "ban";
        data2[8] = 20257;
        data1[9] = "bang";
        data2[9] = 20242;
        data1[10] = "bao";
        data2[10] = 20230;
        data1[11] = "bei";
        data2[11] = 20051;
        data1[12] = "ben";
        data2[12] = 20036;
        data1[13] = "beng";
        data2[13] = 20032;
        data1[14] = "bi";
        data2[14] = 20026;
        data1[15] = "bian";
        data2[15] = 20002;
        data1[16] = "biao";
        data2[16] = 19990;
        data1[17] = "bie";
        data2[17] = 19986;
        data1[18] = "bin";
        data2[18] = 19982;
        data1[19] = "bing";
        data2[19] = 19976;
        data1[20] = "bo";
        data2[20] = 19805;
        data1[21] = "bu";
        data2[21] = 19784;
        data1[22] = "ca";
        data2[22] = 19775;
        data1[23] = "cai";
        data2[23] = 19774;
        data1[24] = "can";
        data2[24] = 19763;
        data1[25] = "cang";
        data2[25] = 19756;
        data1[26] = "cao";
        data2[26] = 19751;
        data1[27] = "ce";
        data2[27] = 19746;
        data1[28] = "ceng";
        data2[28] = 19741;
        data1[29] = "cha";
        data2[29] = 19739;
        data1[30] = "chai";
        data2[30] = 19728;
        data1[31] = "chan";
        data2[31] = 19725;
        data1[32] = "chang";
        data2[32] = 19715;
        data1[33] = "chao";
        data2[33] = 19540;
        data1[34] = "che";
        data2[34] = 19531;
        data1[35] = "chen";
        data2[35] = 19525;
        data1[36] = "cheng";
        data2[36] = 19515;
        data1[37] = "chi";
        data2[37] = 19500;
        data1[38] = "chong";
        data2[38] = 19484;
        data1[39] = "chou";
        data2[39] = 19479;
        data1[40] = "chu";
        data2[40] = 19467;
        data1[41] = "chuai";
        data2[41] = 19289;
        data1[42] = "chuan";
        data2[42] = 19288;
        data1[43] = "chuang";
        data2[43] = 19281;
        data1[44] = "chui";
        data2[44] = 19275;
        data1[45] = "chun";
        data2[45] = 19270;
        data1[46] = "chuo";
        data2[46] = 19263;
        data1[47] = "ci";
        data2[47] = 19261;
        data1[48] = "cong";
        data2[48] = 19249;
        data1[49] = "cou";
        data2[49] = 19243;
        data1[50] = "cu";
        data2[50] = 19242;
        data1[51] = "cuan";
        data2[51] = 19238;
        data1[52] = "cui";
        data2[52] = 19235;
        data1[53] = "cun";
        data2[53] = 19227;
        data1[54] = "cuo";
        data2[54] = 19224;
        data1[55] = "da";
        data2[55] = 19218;
        data1[56] = "dai";
        data2[56] = 19212;
        data1[57] = "dan";
        data2[57] = 19038;
        data1[58] = "dang";
        data2[58] = 19023;
        data1[59] = "dao";
        data2[59] = 19018;
        data1[60] = "de";
        data2[60] = 19006;
        data1[61] = "deng";
        data2[61] = 19003;
        data1[62] = "di";
        data2[62] = 18996;
        data1[63] = "dian";
        data2[63] = 18977;
        data1[64] = "diao";
        data2[64] = 18961;
        data1[65] = "die";
        data2[65] = 18952;
        data1[66] = "ding";
        data2[66] = 18783;
        data1[67] = "diu";
        data2[67] = 18774;
        data1[68] = "dong";
        data2[68] = 18773;
        data1[69] = "dou";
        data2[69] = 18763;
        data1[70] = "du";
        data2[70] = 18756;
        data1[71] = "duan";
        data2[71] = 18741;
        data1[72] = "dui";
        data2[72] = 18735;
        data1[73] = "dun";
        data2[73] = 18731;
        data1[74] = "duo";
        data2[74] = 18722;
        data1[75] = "e";
        data2[75] = 18710;
        data1[76] = "en";
        data2[76] = 18697;
        data1[77] = "er";
        data2[77] = 18696;
        data1[78] = "fa";
        data2[78] = 18526;
        data1[79] = "fan";
        data2[79] = 18518;
        data1[80] = "fang";
        data2[80] = 18501;
        data1[81] = "fei";
        data2[81] = 18490;
        data1[82] = "fen";
        data2[82] = 18478;
        data1[83] = "feng";
        data2[83] = 18463;
        data1[84] = "fo";
        data2[84] = 18448;
        data1[85] = "fou";
        data2[85] = 18447;
        data1[86] = "fu";
        data2[86] = 18446;
        data1[87] = "ga";
        data2[87] = 18239;
        data1[88] = "gai";
        data2[88] = 18237;
        data1[89] = "gan";
        data2[89] = 18231;
        data1[90] = "gang";
        data2[90] = 18220;
        data1[91] = "gao";
        data2[91] = 18211;
        data1[92] = "ge";
        data2[92] = 18201;
        data1[93] = "gei";
        data2[93] = 18184;
        data1[94] = "gen";
        data2[94] = 18183;
        data1[95] = "geng";
        data2[95] = 18181;
        data1[96] = "gong";
        data2[96] = 18012;
        data1[97] = "gou";
        data2[97] = 17997;
        data1[98] = "gu";
        data2[98] = 17988;
        data1[99] = "gua";
        data2[99] = 17970;
        data1[100] = "guai";
        data2[100] = 17964;
        data1[101] = "guan";
        data2[101] = 17961;
        data1[102] = "guang";
        data2[102] = 17950;
        data1[103] = "gui";
        data2[103] = 17947;
        data1[104] = "gun";
        data2[104] = 17931;
        data1[105] = "guo";
        data2[105] = 17928;
        data1[106] = "ha";
        data2[106] = 17922;
        data1[107] = "hai";
        data2[107] = 17759;
        data1[108] = "han";
        data2[108] = 17752;
        data1[109] = "hang";
        data2[109] = 17733;
        data1[110] = "hao";
        data2[110] = 17730;
        data1[111] = "he";
        data2[111] = 17721;
        data1[112] = "hei";
        data2[112] = 17703;
        data1[113] = "hen";
        data2[113] = 17701;
        data1[114] = "heng";
        data2[114] = 17697;
        data1[115] = "hong";
        data2[115] = 17692;
        data1[116] = "hou";
        data2[116] = 17683;
        data1[117] = "hu";
        data2[117] = 17676;
        data1[118] = "hua";
        data2[118] = 17496;
        data1[119] = "huai";
        data2[119] = 17487;
        data1[120] = "huan";
        data2[120] = 17482;
        data1[121] = "huang";
        data2[121] = 17468;
        data1[122] = "hui";
        data2[122] = 17454;
        data1[123] = "hun";
        data2[123] = 17433;
        data1[124] = "huo";
        data2[124] = 17427;
        data1[125] = "ji";
        data2[125] = 17417;
        data1[126] = "jia";
        data2[126] = 17202;
        data1[127] = "jian";
        data2[127] = 17185;
        data1[128] = "jiang";
        data2[128] = 16983;
        data1[129] = "jiao";
        data2[129] = 16970;
        data1[130] = "jie";
        data2[130] = 16942;
        data1[131] = "jin";
        data2[131] = 16915;
        data1[132] = "jing";
        data2[132] = 16733;
        data1[133] = "jiong";
        data2[133] = 16708;
        data1[134] = "jiu";
        data2[134] = 16706;
        data1[135] = "ju";
        data2[135] = 16689;
        data1[136] = "juan";
        data2[136] = 16664;
        data1[137] = "jue";
        data2[137] = 16657;
        data1[138] = "jun";
        data2[138] = 16647;
        data1[139] = "ka";
        data2[139] = 16474;
        data1[140] = "kai";
        data2[140] = 16470;
        data1[141] = "kan";
        data2[141] = 16465;
        data1[142] = "kang";
        data2[142] = 16459;
        data1[143] = "kao";
        data2[143] = 16452;
        data1[144] = "ke";
        data2[144] = 16448;
        data1[145] = "ken";
        data2[145] = 16433;
        data1[146] = "keng";
        data2[146] = 16429;
        data1[147] = "kong";
        data2[147] = 16427;
        data1[148] = "kou";
        data2[148] = 16423;
        data1[149] = "ku";
        data2[149] = 16419;
        data1[150] = "kua";
        data2[150] = 16412;
        data1[151] = "kuai";
        data2[151] = 16407;
        data1[152] = "kuan";
        data2[152] = 16403;
        data1[153] = "kuang";
        data2[153] = 16401;
        data1[154] = "kui";
        data2[154] = 16393;
        data1[155] = "kun";
        data2[155] = 16220;
        data1[156] = "kuo";
        data2[156] = 16216;
        data1[157] = "la";
        data2[157] = 16212;
        data1[158] = "lai";
        data2[158] = 16205;
        data1[159] = "lan";
        data2[159] = 16202;
        data1[160] = "lang";
        data2[160] = 16187;
        data1[161] = "lao";
        data2[161] = 16180;
        data1[162] = "le";
        data2[162] = 16171;
        data1[163] = "lei";
        data2[163] = 16169;
        data1[164] = "leng";
        data2[164] = 16158;
        data1[165] = "li";
        data2[165] = 16155;
        data1[166] = "lia";
        data2[166] = 15959;
        data1[167] = "lian";
        data2[167] = 15958;
        data1[168] = "liang";
        data2[168] = 15944;
        data1[169] = "liao";
        data2[169] = 15933;
        data1[170] = "lie";
        data2[170] = 15920;
        data1[171] = "lin";
        data2[171] = 15915;
        data1[172] = "ling";
        data2[172] = 15903;
        data1[173] = "liu";
        data2[173] = 15889;
        data1[174] = "long";
        data2[174] = 15878;
        data1[175] = "lou";
        data2[175] = 15707;
        data1[176] = "lu";
        data2[176] = 15701;
        data1[177] = "lv";
        data2[177] = 15681;
        data1[178] = "luan";
        data2[178] = 15667;
        data1[179] = "lue";
        data2[179] = 15661;
        data1[180] = "lun";
        data2[180] = 15659;
        data1[181] = "luo";
        data2[181] = 15652;
        data1[182] = "ma";
        data2[182] = 15640;
        data1[183] = "mai";
        data2[183] = 15631;
        data1[184] = "man";
        data2[184] = 15625;
        data1[185] = "mang";
        data2[185] = 15454;
        data1[186] = "mao";
        data2[186] = 15448;
        data1[187] = "me";
        data2[187] = 15436;
        data1[188] = "mei";
        data2[188] = 15435;
        data1[189] = "men";
        data2[189] = 15419;
        data1[190] = "meng";
        data2[190] = 15416;
        data1[191] = "mi";
        data2[191] = 15408;
        data1[192] = "mian";
        data2[192] = 15394;
        data1[193] = "miao";
        data2[193] = 15385;
        data1[194] = "mie";
        data2[194] = 15377;
        data1[195] = "min";
        data2[195] = 15375;
        data1[196] = "ming";
        data2[196] = 15369;
        data1[197] = "miu";
        data2[197] = 15363;
        data1[198] = "mo";
        data2[198] = 15362;
        data1[199] = "mou";
        data2[199] = 15183;
        data1[200] = "mu";
        data2[200] = 15180;
        data1[201] = "na";
        data2[201] = 15165;
        data1[202] = "nai";
        data2[202] = 15158;
        data1[203] = "nan";
        data2[203] = 15153;
        data1[204] = "nang";
        data2[204] = 15150;
        data1[205] = "nao";
        data2[205] = 15149;
        data1[206] = "ne";
        data2[206] = 15144;
        data1[207] = "nei";
        data2[207] = 15143;
        data1[208] = "nen";
        data2[208] = 15141;
        data1[209] = "neng";
        data2[209] = 15140;
        data1[210] = "ni";
        data2[210] = 15139;
        data1[211] = "nian";
        data2[211] = 15128;
        data1[212] = "niang";
        data2[212] = 15121;
        data1[213] = "niao";
        data2[213] = 15119;
        data1[214] = "nie";
        data2[214] = 15117;
        data1[215] = "nin";
        data2[215] = 15110;
        data1[216] = "ning";
        data2[216] = 15109;
        data1[217] = "niu";
        data2[217] = 14941;
        data1[218] = "nong";
        data2[218] = 14937;
        data1[219] = "nu";
        data2[219] = 14933;
        data1[220] = "nv";
        data2[220] = 14930;
        data1[221] = "nuan";
        data2[221] = 14929;
        data1[222] = "nue";
        data2[222] = 14928;
        data1[223] = "nuo";
        data2[223] = 14926;
        data1[224] = "o";
        data2[224] = 14922;
        data1[225] = "ou";
        data2[225] = 14921;
        data1[226] = "pa";
        data2[226] = 14914;
        data1[227] = "pai";
        data2[227] = 14908;
        data1[228] = "pan";
        data2[228] = 14902;
        data1[229] = "pang";
        data2[229] = 14894;
        data1[230] = "pao";
        data2[230] = 14889;
        data1[231] = "pei";
        data2[231] = 14882;
        data1[232] = "pen";
        data2[232] = 14873;
        data1[233] = "peng";
        data2[233] = 14871;
        data1[234] = "pi";
        data2[234] = 14857;
        data1[235] = "pian";
        data2[235] = 14678;
        data1[236] = "piao";
        data2[236] = 14674;
        data1[237] = "pie";
        data2[237] = 14670;
        data1[238] = "pin";
        data2[238] = 14668;
        data1[239] = "ping";
        data2[239] = 14663;
        data1[240] = "po";
        data2[240] = 14654;
        data1[241] = "pu";
        data2[241] = 14645;
        data1[242] = "qi";
        data2[242] = 14630;
        data1[243] = "qia";
        data2[243] = 14594;
        data1[244] = "qian";
        data2[244] = 14429;
        data1[245] = "qiang";
        data2[245] = 14407;
        data1[246] = "qiao";
        data2[246] = 14399;
        data1[247] = "qie";
        data2[247] = 14384;
        data1[248] = "qin";
        data2[248] = 14379;
        data1[249] = "qing";
        data2[249] = 14368;
        data1[250] = "qiong";
        data2[250] = 14355;
        data1[251] = "qiu";
        data2[251] = 14353;
        data1[252] = "qu";
        data2[252] = 14345;
        data1[253] = "quan";
        data2[253] = 14170;
        data1[254] = "que";
        data2[254] = 14159;
        data1[255] = "qun";
        data2[255] = 14151;
        data1[256] = "ran";
        data2[256] = 14149;
        data1[257] = "rang";
        data2[257] = 14145;
        data1[258] = "rao";
        data2[258] = 14140;
        data1[259] = "re";
        data2[259] = 14137;
        data1[260] = "ren";
        data2[260] = 14135;
        data1[261] = "reng";
        data2[261] = 14125;
        data1[262] = "ri";
        data2[262] = 14123;
        data1[263] = "rong";
        data2[263] = 14122;
        data1[264] = "rou";
        data2[264] = 14112;
        data1[265] = "ru";
        data2[265] = 14109;
        data1[266] = "ruan";
        data2[266] = 14099;
        data1[267] = "rui";
        data2[267] = 14097;
        data1[268] = "run";
        data2[268] = 14094;
        data1[269] = "ruo";
        data2[269] = 14092;
        data1[270] = "sa";
        data2[270] = 14090;
        data1[271] = "sai";
        data2[271] = 14087;
        data1[272] = "san";
        data2[272] = 14083;
        data1[273] = "sang";
        data2[273] = 13917;
        data1[274] = "sao";
        data2[274] = 13914;
        data1[275] = "se";
        data2[275] = 13910;
        data1[276] = "sen";
        data2[276] = 13907;
        data1[277] = "seng";
        data2[277] = 13906;
        data1[278] = "sha";
        data2[278] = 13905;
        data1[279] = "shai";
        data2[279] = 13896;
        data1[280] = "shan";
        data2[280] = 13894;
        data1[281] = "shang";
        data2[281] = 13878;
        data1[282] = "shao";
        data2[282] = 13870;
        data1[283] = "she";
        data2[283] = 13859;
        data1[284] = "shen";
        data2[284] = 13847;
        data1[285] = "sheng";
        data2[285] = 13831;
        data1[286] = "shi";
        data2[286] = 13658;
        data1[287] = "shou";
        data2[287] = 13611;
        data1[288] = "shu";
        data2[288] = 13601;
        data1[289] = "shua";
        data2[289] = 13406;
        data1[290] = "shuai";
        data2[290] = 13404;
        data1[291] = "shuan";
        data2[291] = 13400;
        data1[292] = "shuang";
        data2[292] = 13398;
        data1[293] = "shui";
        data2[293] = 13395;
        data1[294] = "shun";
        data2[294] = 13391;
        data1[295] = "shuo";
        data2[295] = 13387;
        data1[296] = "si";
        data2[296] = 13383;
        data1[297] = "song";
        data2[297] = 13367;
        data1[298] = "sou";
        data2[298] = 13359;
        data1[299] = "su";
        data2[299] = 13356;
        data1[300] = "suan";
        data2[300] = 13343;
        data1[301] = "sui";
        data2[301] = 13340;
        data1[302] = "sun";
        data2[302] = 13329;
        data1[303] = "suo";
        data2[303] = 13326;
        data1[304] = "ta";
        data2[304] = 13318;
        data1[305] = "tai";
        data2[305] = 13147;
        data1[306] = "tan";
        data2[306] = 13138;
        data1[307] = "tang";
        data2[307] = 13120;
        data1[308] = "tao";
        data2[308] = 13107;
        data1[309] = "te";
        data2[309] = 13096;
        data1[310] = "teng";
        data2[310] = 13095;
        data1[311] = "ti";
        data2[311] = 13091;
        data1[312] = "tian";
        data2[312] = 13076;
        data1[313] = "tiao";
        data2[313] = 13068;
        data1[314] = "tie";
        data2[314] = 13063;
        data1[315] = "ting";
        data2[315] = 13060;
        data1[316] = "tong";
        data2[316] = 12888;
        data1[317] = "tou";
        data2[317] = 12875;
        data1[318] = "tu";
        data2[318] = 12871;
        data1[319] = "tuan";
        data2[319] = 12860;
        data1[320] = "tui";
        data2[320] = 12858;
        data1[321] = "tun";
        data2[321] = 12852;
        data1[322] = "tuo";
        data2[322] = 12849;
        data1[323] = "wa";
        data2[323] = 12838;
        data1[324] = "wai";
        data2[324] = 12831;
        data1[325] = "wan";
        data2[325] = 12829;
        data1[326] = "wang";
        data2[326] = 12812;
        data1[327] = "wei";
        data2[327] = 12802;
        data1[328] = "wen";
        data2[328] = 12607;
        data1[329] = "weng";
        data2[329] = 12597;
        data1[330] = "wo";
        data2[330] = 12594;
        data1[331] = "wu";
        data2[331] = 12585;
        data1[332] = "xi";
        data2[332] = 12556;
        data1[333] = "xia";
        data2[333] = 12359;
        data1[334] = "xian";
        data2[334] = 12346;
        data1[335] = "xiang";
        data2[335] = 12320;
        data1[336] = "xiao";
        data2[336] = 12300;
        data1[337] = "xie";
        data2[337] = 12120;
        data1[338] = "xin";
        data2[338] = 12099;
        data1[339] = "xing";
        data2[339] = 12089;
        data1[340] = "xiong";
        data2[340] = 12074;
        data1[341] = "xiu";
        data2[341] = 12067;
        data1[342] = "xu";
        data2[342] = 12058;
        data1[343] = "xuan";
        data2[343] = 12039;
        data1[344] = "xue";
        data2[344] = 11867;
        data1[345] = "xun";
        data2[345] = 11861;
        data1[346] = "ya";
        data2[346] = 11847;
        data1[347] = "yan";
        data2[347] = 11831;
        data1[348] = "yang";
        data2[348] = 11798;
        data1[349] = "yao";
        data2[349] = 11781;
        data1[350] = "ye";
        data2[350] = 11604;
        data1[351] = "yi";
        data2[351] = 11589;
        data1[352] = "yin";
        data2[352] = 11536;
        data1[353] = "ying";
        data2[353] = 11358;
        data1[354] = "yo";
        data2[354] = 11340;
        data1[355] = "yong";
        data2[355] = 11339;
        data1[356] = "you";
        data2[356] = 11324;
        data1[357] = "yu";
        data2[357] = 11303;
        data1[358] = "yuan";
        data2[358] = 11097;
        data1[359] = "yue";
        data2[359] = 11077;
        data1[360] = "yun";
        data2[360] = 11067;
        data1[361] = "za";
        data2[361] = 11055;
        data1[362] = "zai";
        data2[362] = 11052;
        data1[363] = "zan";
        data2[363] = 11045;
        data1[364] = "zang";
        data2[364] = 11041;
        data1[365] = "zao";
        data2[365] = 11038;
        data1[366] = "ze";
        data2[366] = 11024;
        data1[367] = "zei";
        data2[367] = 11020;
        data1[368] = "zen";
        data2[368] = 11019;
        data1[369] = "zeng";
        data2[369] = 11018;
        data1[370] = "zha";
        data2[370] = 11014;
        data1[371] = "zhai";
        data2[371] = 10838;
        data1[372] = "zhan";
        data2[372] = 10832;
        data1[373] = "zhang";
        data2[373] = 10815;
        data1[374] = "zhao";
        data2[374] = 10800;
        data1[375] = "zhe";
        data2[375] = 10790;
        data1[376] = "zhen";
        data2[376] = 10780;
        data1[377] = "zheng";
        data2[377] = 10764;
        data1[378] = "zhi";
        data2[378] = 10587;
        data1[379] = "zhong";
        data2[379] = 10544;
        data1[380] = "zhou";
        data2[380] = 10533;
        data1[381] = "zhu";
        data2[381] = 10519;
        data1[382] = "zhua";
        data2[382] = 10331;
        data1[383] = "zhuai";
        data2[383] = 10329;
        data1[384] = "zhuan";
        data2[384] = 10328;
        data1[385] = "zhuang";
        data2[385] = 10322;
        data1[386] = "zhui";
        data2[386] = 10315;
        data1[387] = "zhun";
        data2[387] = 10309;
        data1[388] = "zhuo";
        data2[388] = 10307;
        data1[389] = "zi";
        data2[389] = 10296;
        data1[390] = "zong";
        data2[390] = 10281;
        data1[391] = "zou";
        data2[391] = 10274;
        data1[392] = "zu";
        data2[392] = 10270;
        data1[393] = "zuan";
        data2[393] = 10262;
        data1[394] = "zui";
        data2[394] = 10260;
        data1[395] = "zun";
        data2[395] = 10256;
        data1[396] = "zuo";
        data2[396] = 10254;

        for ( int i = 0; i < length; i++ ) {
            data2[i] = -data2[i];
        }
    }

    /**
     * 将汉字转化成拼音， 中间用指定的分隔符split分隔开来
     * @param str
     * @param split
     * @return
     */
    public String getPinyinWithSplit(String str, String split) {
        if ( str == null ) {
            return "";
        }

        String s = split;
        if ( s == null ) {
            s = " ";
        }

        String x = str.replaceAll("、", "").replaceAll("<","")
                .replaceAll(">", "").replaceAll("\\{", "").replaceAll("\\}", "")
                .replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\(", "")
                .replaceAll("\\)", "").replaceAll("\\.", "").replaceAll("\\*", "");

        String pinyin = "";
        try {
            for (int j = 0; j < x.length(); j++) {
                char word = x.charAt(j);
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word, pinyinFormat);
                if (pinyinArray != null) {
                    pinyin += pinyinArray[0] + s;
                } else {
                    pinyin += (word + s).toLowerCase();
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            log.warn("Pinyin Format Error", ex);
        }
        return pinyin.trim();
    }

    /**
     * 过滤掉拼音中的特殊字符
     * @param str
     * @return
     */
    public String pinyinFilter(String str) {
        String x = str.replaceAll("、", "").replaceAll("<","")
                .replaceAll(">", "").replaceAll("\\{", "").replaceAll("\\}", "")
                .replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\(", "")
                .replaceAll("\\)", "").replaceAll("\\.", "").replaceAll("\\*", "");

        return x;
    }

    /**
     * 将汉字切换成拼音
     * 不推荐使用，方法待优化 ---- 可以使用pinyin4j 工具
     * @param c
     * @return
     * @throws Exception
     */
    public String getPinyin(char c) {
        try {
            byte[] data = (c + "").getBytes("GBK");
            int code = 0;
            for (int i = 0; i < data.length; i++) {
                code *= 256;
                if (data[i] < 0) {
                    code += data[i] + 256;
                } else {
                    code += data[i];
                }
            }
            code -= 65536;
            if (code >= -20319 && code <= -10247) {
                for (int i = length - 1; i >= 0; i--) {
                    if (code >= data2[i]) {
                        return data1[i];
                    }
                }
            }

        } catch ( Exception e ) {
            log.error("Transfer chinese to Pinyin error, reason = {}.", StackTraceUtil.getStackTrace(e));
        }
        return c + "";
    }

    /**
     * 判断字符串是否是一个正确的拼音
     * @param spell
     * @return
     */
    public boolean isPinyin(String spell) {
        Node node = tree.getRoot();
        char[] letters = spell.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] < 'a' || letters[i] > 'z') {
                return false;
            } else if (node.getChildrens().containsKey(letters[i] + "")) {
                node = node.getChildrens().get(letters[i] + "");
            } else {
                return false;
            }
        }

        if ( node.isEnd() ) {
            return true;
        }
        return false;
    }

    /**
     * 切分拼音, 默认最长匹配
     * @param spell
     * @return
     */
    public Map<String, List<String>> splitToPinyin(String spell) {

        Map<String, List<String>> pyMap = new HashMap<>();

        char[] letters = spell.toCharArray();
        /** 完整的拼音列表 */
        List<String> list = new ArrayList<>();
        /** 不完整的拼音列表 */
        List<String> charList = new ArrayList<>();

        String spells = "";

        Node node = tree.getRoot();
        int i = 0;
        while ( i < letters.length ) {
            if ( letters[i] < 'a' || letters[i] > 'z' ) {
                if ( spells != null && !("".equals(spells)) ) {
                    if ( node.isEnd() ) {
                        list.add(spells);
                    } else {
                        charList.add(spells);
                    }
                }
                spells = "" + letters[i];
                node = tree.getRoot();
                list.add(spells);
                spells = "";
                i++;
            } else if (node.getChildrens().containsKey(letters[i] + "")) {
                spells += letters[i];
                node = node.getChildrens().get(letters[i] + "");
                i++;
            } else {
                if (node.isEnd()) {
                    list.add(spells);
                    node = tree.getRoot();
                    spells = "";
                } else {
                    charList.add(spells);
                    node = tree.getRoot();
                    spells = "";
                    i++;
                }
            }
        }

        if ( spells != null && !("".equals(spells)) ) {
            if ( node.isEnd() ) {
                list.add(spells);
            } else {
                charList.add(spells);
            }
        }

        pyMap.put("r", list);
        pyMap.put("l", charList);
        return pyMap;
    }

    /**
     * 根据num值拆分拼音
     * @param spell
     * @param num
     * @return
     */
    public Map<String, List<String>> splitToPinyin(String spell, int num) {
        Map<String, List<String>> pyMap = new HashMap<>();

        char[] letters = spell.toCharArray();
        /** 完整的拼音列表 */
        List<String> list = new ArrayList<>();
        /** 不完整的拼音列表 */
        List<String> charList = new ArrayList<>();

        String spells = "";
        Node node = tree.getRoot();

        int i = 0 ;
        while ( i < letters.length ) {
            if (letters[i] < 'a' || letters[i] > 'z') {
                if (spells != null && !("".equals(spells))) {
                    if (node.isEnd()) {
                        list.add(spells);
                    } else {
                        charList.add(spells);
                    }
                }
                spells = "" + letters[i];
                node = tree.getRoot();
                list.add(spells);
                spells = "";
                i++;
            } else if (node.getChildrens().containsKey(letters[i] + "")) {
                spells += letters[i];
                node = node.getChildrens().get(letters[i] + "");
                if ( node.isEnd() && spells.length() >= num ) {
                    list.add(spells);
                    node = tree.getRoot();
                    spells = "";
                }
                i++;
            } else {
                if (node.isEnd()) {
                    list.add(spells);
                    node = tree.getRoot();
                    spells = "";
                } else {
                    charList.add(spells);
                    node = tree.getRoot();
                    spells = "";
                    i++;
                }
            }
        }

        if (spells != null && !("".equals(spells))) {
            if ( node.isEnd() ) {
                list.add(spells);
            } else {
                charList.add(spells);
            }
        }

        pyMap.put("r", list);
        pyMap.put("l", charList);
        return pyMap;
    }

    /**
     * 切分拼音 最长匹配和最短匹配可选
     * los = true 最短匹配； los = false 最短匹配
     * @param spell
     * @param los
     * @return
     */
    public Map<String, List<String>> splitToPinyin(String spell, boolean los) {
        if ( !los ) {
            return splitToPinyin(spell);
        }

        Map<String, List<String>> pyMap = new HashMap<>();

        char[] letters = spell.toCharArray();
        /** 完整的拼音列表 */
        List<String> list = new ArrayList<>();
        /** 不完整的拼音列表 */
        List<String> charList = new ArrayList<>();

        String spells = "";
        Node node = tree.getRoot();

        int i = 0 ;
        while ( i < letters.length ) {
            if (letters[i] < 'a' || letters[i] > 'z') {
                if (spells != null && !("".equals(spells))) {
                    if (node.isEnd()) {
                        list.add(spells);
                    } else {
                        charList.add(spells);
                    }
                }
                spells = "" + letters[i];
                node = tree.getRoot();
                list.add(spells);
                spells = "";
                i++;
            } else if (node.getChildrens().containsKey(letters[i] + "")) {
                spells += letters[i];
                node = node.getChildrens().get(letters[i] + "");
                if ( node.isEnd()) {
                    list.add(spells);
                    node = tree.getRoot();
                    spells = "";
                }
                i++;
            } else {
                if (node.isEnd()) {
                    list.add(spells);
                    node = tree.getRoot();
                    spells = "";
                } else {
                    charList.add(spells);
                    node = tree.getRoot();
                    spells = "";
                    i++;
                }
            }
        }

        if (spells != null && !("".equals(spells))) {
            if ( node.isEnd() ) {
                list.add(spells);
            } else {
                charList.add(spells);
            }
        }

        pyMap.put("r", list);
        pyMap.put("l", charList);
        return pyMap;
    }

    /**
     * 根据过滤器切分拼音
     * 过滤器由初始化时设定
     * @param spell
     * @return
     */
    public Map<String, List<String>> splitToPinyinByFilter(String spell) {
        Map<String, List<String>>  map = splitToPinyin(spell);

        List<String> list = map.get("r");
        List<String> resultList = new ArrayList<>();

        for ( String s : list ) {
            resultList.add(s);
            for ( String x : mpyMap.keySet() ) {
                if ( s.equals(x) ) {
                    resultList.remove(s);
                    resultList.addAll(mpyMap.get(x));
                }
            }
        }

        map.put("r", resultList);
        return map;
    }

    public Map<String, List<String>> splitToPinyinBySelfFilter(String spell) {
        Map<String, List<String>>  map = splitToPinyin(spell);

        List<String> list = map.get("r");
        List<String> resultList = new ArrayList<>();

        for ( String s : list ) {
            resultList.add(s);
            for ( String x : pyMap.keySet() ) {
                if ( s.equals(x) ) {
                    resultList.remove(s);
                    resultList.addAll(pyMap.get(x));
                }
            }
        }

        map.put("r", resultList);
        return map;
    }

    private Map<String, List<String>> extractSpecial() {

        Map<String, List<String>> pyMap = new HashMap<>();
        /** 根据最短匹配获取拼音中包含拼音的特殊拼音 */
//        for ( int i = 0; i != length; i ++ ) {
//            Map< String, List<String> > map = splitToPinyin(data1[i], true);
//
//            if (map.get("l").size() == 0 && map.get("r").size() > 1) {
//                System.out.println(map.get("r"));
//            }
//        }

        /** 获取拼音中包含长度为2以上的拼音的特殊拼音 */
        for ( int i = 0; i != length; i ++ ) {
            Map< String, List<String> > map = splitToPinyin(data1[i], 2);

            if (map.get("l").size() == 0 && map.get("r").size() > 1) {
                pyMap.put(data1[i], map.get("r"));
            }
        }

        /** 获取拼音中包含长度为3以上的拼音的特殊拼音 */
//        for ( int i = 0; i != length; i ++ ) {
//            Map< String, List<String> > map = splitToPinyin(data1[i], 3);
//
//            if (map.get("l").size() == 0 && map.get("r").size() > 1) {
//                System.out.println(map.get("r"));
//            }
//        }

//        System.out.println(pyMap);
        return pyMap;
    }

    private void initData() {
        tree = new TrieTree("PinYin");
        try {
            for ( int i = 0 ; i != length; i++ ) {
                tree.insert(data1[i]);
            }
        } catch ( Exception e ) {
            log.error("Init Trie Tree For Pinyin error, reason = {}.", StackTraceUtil.getStackTrace(e));
        }

        mpyMap = extractSpecial();

        initSelfDefPinyin();
    }

    private void initSelfDefPinyin() {
        pyMap = new HashMap<>();

        InputStream input = PinyinUtil.class.getResourceAsStream("/pinyin.properties");

        if (input == null) {
            log.error("Pinyin config file does not exist: " + "/pinyin.properties");
            return;
        }

        Properties py = new Properties();
        try {
            py.load(input);
        } catch ( IOException e) {
            log.error("Failed to load pinyin.properties");
            return;
        }

        Enumeration enums = py.propertyNames();
        while ( enums.hasMoreElements() ) {
            String key = (String)enums.nextElement();
            String value = py.getProperty(key);

            String[] values = value.split(",");
            pyMap.put(key, Arrays.asList(values));
        }
    }

    /**
     * 用于将汉字转化为拼音,支持多音字组合
     * 比如：单于，返回：danyu,chanyu和shanyu
     * 以及支持汉字和拼音混搭
     *
     * @param src 传入汉字和拼音
     *            支持中英文混搭搜索
     * @return 返回所有可能的组合
     */
    public  Set<String> getPinyin(String src) {
        Set<String> pinyinSet = new HashSet<String>();
        try {
            List<List<String>> polyphone = new ArrayList<List<String>>();
            int totalsize = 1;
            for (int i = 0; i < src.length(); i++) {
                Set<String> temp = new HashSet<String>();
                String[] pyarr = PinyinHelper.toHanyuPinyinStringArray(src.charAt(i), pinyinFormat);
                if (pyarr != null) {
                    for (String py : pyarr) {
                        if (py != null) {
                            temp.add(py);
                        }
                    }
                } else {
                    temp.add(src.charAt(i) + "");
                }
                totalsize *= temp.size();
                polyphone.add(new ArrayList<String>(temp));
            }

            for (int i = 0; i < Math.min(totalsize, MAX_PINYIN_SET_SIZE); i++) {
                StringBuilder sb = new StringBuilder();
                int index = i;
                for (List<String> temp : polyphone) {
                    sb.append(temp.get(index % temp.size()));
                    index /= temp.size();
                }
                pinyinSet.add(sb.toString());
            }
        } catch (Exception e) {
            log.warn("get pinyin error, reason = {}.", StackTraceUtil.getStackTrace(e));
        }
        return pinyinSet;
    }


}