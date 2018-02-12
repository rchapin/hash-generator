package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.List;

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestData;

public class TestDataLongScalar { 

   public static List<HashTestData<? extends Object>> list =
      new ArrayList<HashTestData<? extends Object>>();

   static {

      HashTestData<? extends Object> htdLongScalar210 = new HashTestData<Long>(
         new Long(-9223372036854775808L),
         "54409ea540dc450d53a86133d867c772",
         HashAlgorithm.MD5SUM);
      list.add(htdLongScalar210);

      HashTestData<? extends Object> htdLongScalar211 = new HashTestData<Long>(
         new Long(-9223372036854775808L),
         "e19f8c6041fd6c4202c739fa01baf61235724b1e",
         HashAlgorithm.SHA1SUM);
      list.add(htdLongScalar211);

      HashTestData<? extends Object> htdLongScalar212 = new HashTestData<Long>(
         new Long(-9223372036854775808L),
         "b1b0bee5378188f5250138bcce25855f2617f9c55b20b9628e13d367c47404a9",
         HashAlgorithm.SHA256SUM);
      list.add(htdLongScalar212);

      HashTestData<? extends Object> htdLongScalar213 = new HashTestData<Long>(
         new Long(-9223372036854775808L),
         "f10c2ce06a3ba0b7944e8bcfb4d3f3ef03ab131a5e5ca3fa2c306ab76e7f5eab3005e3e9d0ad72a26d4360fb7eb11798",
         HashAlgorithm.SHA384SUM);
      list.add(htdLongScalar213);

      HashTestData<? extends Object> htdLongScalar214 = new HashTestData<Long>(
         new Long(-9223372036854775808L),
         "268c793058acb875e8eb1e613aff64ef78487ab8bb95baa2877647a3f7c1a326f8db76c51c19c05fbe428a75ff93edd077089d6f1bac26b1af2764f0f9783149",
         HashAlgorithm.SHA512SUM);
      list.add(htdLongScalar214);

      HashTestData<? extends Object> htdLongScalar215 = new HashTestData<Long>(
         new Long(-36028797018963968L),
         "8c64b2b91eaedcae7dfda9b43ca4f62b",
         HashAlgorithm.MD5SUM);
      list.add(htdLongScalar215);

      HashTestData<? extends Object> htdLongScalar216 = new HashTestData<Long>(
         new Long(-36028797018963968L),
         "101201ec9be0c94da1aabacf9050452d531db7b6",
         HashAlgorithm.SHA1SUM);
      list.add(htdLongScalar216);

      HashTestData<? extends Object> htdLongScalar217 = new HashTestData<Long>(
         new Long(-36028797018963968L),
         "006ce6768bf58dcecb91e0b0957b7e71adbb24e2fafd6e0762fc3b51daae8c65",
         HashAlgorithm.SHA256SUM);
      list.add(htdLongScalar217);

      HashTestData<? extends Object> htdLongScalar218 = new HashTestData<Long>(
         new Long(-36028797018963968L),
         "8963d18e89e6463da925a55e1508cfe020e7d1f6b38ee2331e4ad2795ce4f9a8b48a005d85c820ee3db7f150dbe869c7",
         HashAlgorithm.SHA384SUM);
      list.add(htdLongScalar218);

      HashTestData<? extends Object> htdLongScalar219 = new HashTestData<Long>(
         new Long(-36028797018963968L),
         "46db6a85707d1394778a41b054941701e3cfd93ec390fe7f156565d916981ca1dfe9d3dbbe1c2f380881c96aa1b9f2bbe6b567303c6fa3633ca0431df3ba81a9",
         HashAlgorithm.SHA512SUM);
      list.add(htdLongScalar219);

      HashTestData<? extends Object> htdLongScalar220 = new HashTestData<Long>(
         new Long(0L),
         "7dea362b3fac8e00956a4952a3d4f474",
         HashAlgorithm.MD5SUM);
      list.add(htdLongScalar220);

      HashTestData<? extends Object> htdLongScalar221 = new HashTestData<Long>(
         new Long(0L),
         "05fe405753166f125559e7c9ac558654f107c7e9",
         HashAlgorithm.SHA1SUM);
      list.add(htdLongScalar221);

      HashTestData<? extends Object> htdLongScalar222 = new HashTestData<Long>(
         new Long(0L),
         "af5570f5a1810b7af78caf4bc70a660f0df51e42baf91d4de5b2328de0e83dfc",
         HashAlgorithm.SHA256SUM);
      list.add(htdLongScalar222);

      HashTestData<? extends Object> htdLongScalar223 = new HashTestData<Long>(
         new Long(0L),
         "7c2db09d310ece0b36d50c86e4c3e6641684948cd6fc03262b0d0ed91a6cfbc3cd5affd396c1f85fd0a109b103364b19",
         HashAlgorithm.SHA384SUM);
      list.add(htdLongScalar223);

      HashTestData<? extends Object> htdLongScalar224 = new HashTestData<Long>(
         new Long(0L),
         "1b7409ccf0d5a34d3a77eaabfa9fe27427655be9297127ee9522aa1bf4046d4f945983678169cb1a7348edcac47ef0d9e2c924130e5bcc5f0d94937852c42f1b",
         HashAlgorithm.SHA512SUM);
      list.add(htdLongScalar224);

      HashTestData<? extends Object> htdLongScalar225 = new HashTestData<Long>(
         new Long(2305843009213693952L),
         "beb49121e6b2d95eb429c1641ae31f64",
         HashAlgorithm.MD5SUM);
      list.add(htdLongScalar225);

      HashTestData<? extends Object> htdLongScalar226 = new HashTestData<Long>(
         new Long(2305843009213693952L),
         "0aa60933f90adb707f898d201ff9ec9abf6cb156",
         HashAlgorithm.SHA1SUM);
      list.add(htdLongScalar226);

      HashTestData<? extends Object> htdLongScalar227 = new HashTestData<Long>(
         new Long(2305843009213693952L),
         "9d4ac218fb54041e3a70a8e14db1ea1af9f570f4842b4702ef9323f1ad8f0ec4",
         HashAlgorithm.SHA256SUM);
      list.add(htdLongScalar227);

      HashTestData<? extends Object> htdLongScalar228 = new HashTestData<Long>(
         new Long(2305843009213693952L),
         "222060d2f13d5736702c122971e7aef8b1417c704014fe161a08da9a94bb563c248aeb06b35a48334c82a156997574da",
         HashAlgorithm.SHA384SUM);
      list.add(htdLongScalar228);

      HashTestData<? extends Object> htdLongScalar229 = new HashTestData<Long>(
         new Long(2305843009213693952L),
         "52b0c754e4d2e495544ddfea552df85fb3127fcf6e54f7aacb57e3e6a20be5821d8e707eedb2c22b39b3222ff489d289c9ffb3a85ff8fd6f645a86c8a5a2b1ec",
         HashAlgorithm.SHA512SUM);
      list.add(htdLongScalar229);

      HashTestData<? extends Object> htdLongScalar230 = new HashTestData<Long>(
         new Long(9223372036854775807L),
         "bd5f6598b2d2cd7f130ba3e152116ff7",
         HashAlgorithm.MD5SUM);
      list.add(htdLongScalar230);

      HashTestData<? extends Object> htdLongScalar231 = new HashTestData<Long>(
         new Long(9223372036854775807L),
         "bf7f9f8d6e0a3426aef3f0ce773e69e85821efc7",
         HashAlgorithm.SHA1SUM);
      list.add(htdLongScalar231);

      HashTestData<? extends Object> htdLongScalar232 = new HashTestData<Long>(
         new Long(9223372036854775807L),
         "c624eebf5b9282431fc4e19c3c707a012275e198d3a077dcd36a7b74e4a804ad",
         HashAlgorithm.SHA256SUM);
      list.add(htdLongScalar232);

      HashTestData<? extends Object> htdLongScalar233 = new HashTestData<Long>(
         new Long(9223372036854775807L),
         "ddfe5a9e0659c196434c9d1b8a098cda10c8ce89d4c160374cf781847a5b143d4ab889651516e201cd89f2d5416afbda",
         HashAlgorithm.SHA384SUM);
      list.add(htdLongScalar233);

      HashTestData<? extends Object> htdLongScalar234 = new HashTestData<Long>(
         new Long(9223372036854775807L),
         "f48eaadddca1083f42f340f4ad0ab5bf7e50d9f7b6fec01807e12cef9bb0c014506c6393155095bf4ea4963f409b8db58fb17f0f2c5887824b53aa7cc69dc8b3",
         HashAlgorithm.SHA512SUM);
      list.add(htdLongScalar234);
   }
}
