package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.List;

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestData;

public class TestDataCharacterScalar { 

   public static List<HashTestData<? extends Object>> list =
      new ArrayList<HashTestData<? extends Object>>();

   static {

      HashTestData<? extends Object> htdCharacterScalar50 = new HashTestData<Character>(
         new Character((char)0x61),
         "760f753576f2955b0074758acb4d5fa6",
         HashAlgorithm.MD5SUM);
      list.add(htdCharacterScalar50);

      HashTestData<? extends Object> htdCharacterScalar51 = new HashTestData<Character>(
         new Character((char)0x61),
         "3106600e0327ca77371f2526df794ed84322585c",
         HashAlgorithm.SHA1SUM);
      list.add(htdCharacterScalar51);

      HashTestData<? extends Object> htdCharacterScalar52 = new HashTestData<Character>(
         new Character((char)0x61),
         "022a6979e6dab7aa5ae4c3e5e45f7e977112a7e63593820dbec1ec738a24f93c",
         HashAlgorithm.SHA256SUM);
      list.add(htdCharacterScalar52);

      HashTestData<? extends Object> htdCharacterScalar53 = new HashTestData<Character>(
         new Character((char)0x61),
         "f5077c4b6e328b21e2f21192776daf9660ceaeaf40a1796b58bd9500b36aff2cb5acd79d2789f891541818315233ff6c",
         HashAlgorithm.SHA384SUM);
      list.add(htdCharacterScalar53);

      HashTestData<? extends Object> htdCharacterScalar54 = new HashTestData<Character>(
         new Character((char)0x61),
         "031ab9ff5962e81139a6900216945fc584ab186aeb1bf3498c661b976a7393af94b6bcc9784f7e8cb75b071de60f9fda06d44ddd561e53e3343857eea2089217",
         HashAlgorithm.SHA512SUM);
      list.add(htdCharacterScalar54);

      HashTestData<? extends Object> htdCharacterScalar55 = new HashTestData<Character>(
         new Character((char)0x42),
         "4d75a03f5a52413121b887ac25ad410f",
         HashAlgorithm.MD5SUM);
      list.add(htdCharacterScalar55);

      HashTestData<? extends Object> htdCharacterScalar56 = new HashTestData<Character>(
         new Character((char)0x42),
         "60a923c61fa5b9d9cbae3dfc1af19dbf52723cbc",
         HashAlgorithm.SHA1SUM);
      list.add(htdCharacterScalar56);

      HashTestData<? extends Object> htdCharacterScalar57 = new HashTestData<Character>(
         new Character((char)0x42),
         "87afe6086fe4571e37657e76281301f189c75ebae1d2eaafb56d578067a1d95e",
         HashAlgorithm.SHA256SUM);
      list.add(htdCharacterScalar57);

      HashTestData<? extends Object> htdCharacterScalar58 = new HashTestData<Character>(
         new Character((char)0x42),
         "a09f464cdc62a6cbf58d9fb7b689e16a3ff9609761defcdf5082059a1f8333166a9b287e545180bc138f374e604413b8",
         HashAlgorithm.SHA384SUM);
      list.add(htdCharacterScalar58);

      HashTestData<? extends Object> htdCharacterScalar59 = new HashTestData<Character>(
         new Character((char)0x42),
         "3bf8ba18e8d0bc73b73a79e69913d4d2fc165e82461d2fd4e0ac4d834cba71ff487ebc4af6e22d677172145368e2ddb1e718da6abea9802f4be8b94788555403",
         HashAlgorithm.SHA512SUM);
      list.add(htdCharacterScalar59);

      HashTestData<? extends Object> htdCharacterScalar60 = new HashTestData<Character>(
         new Character((char)0x46),
         "8e4f8cdcd7dccbc9654c9f6601c43383",
         HashAlgorithm.MD5SUM);
      list.add(htdCharacterScalar60);

      HashTestData<? extends Object> htdCharacterScalar61 = new HashTestData<Character>(
         new Character((char)0x46),
         "beb59e97091dd2f1ada25fea1abf7a70ac3ada78",
         HashAlgorithm.SHA1SUM);
      list.add(htdCharacterScalar61);

      HashTestData<? extends Object> htdCharacterScalar62 = new HashTestData<Character>(
         new Character((char)0x46),
         "c02b4bb4ab96197a9a5a4537d8b87f27c1c8f3f9572d215c505c5f3339ee4399",
         HashAlgorithm.SHA256SUM);
      list.add(htdCharacterScalar62);

      HashTestData<? extends Object> htdCharacterScalar63 = new HashTestData<Character>(
         new Character((char)0x46),
         "40d1c6bb7bed464e594804da93208967363c3f5fb1cc5998899f4de9ae847824622df0425b1c79e524df5ff75cd8fcb0",
         HashAlgorithm.SHA384SUM);
      list.add(htdCharacterScalar63);

      HashTestData<? extends Object> htdCharacterScalar64 = new HashTestData<Character>(
         new Character((char)0x46),
         "b4de823055d00bc12cd78fe1aaf74ee6062195b2629f49a25915a39c64be1900182e73be1a97cd2754ef4f2eb2ba42a8e1e952ea652d49bef5d87b363708dd9f",
         HashAlgorithm.SHA512SUM);
      list.add(htdCharacterScalar64);

      HashTestData<? extends Object> htdCharacterScalar65 = new HashTestData<Character>(
         new Character((char)0x23),
         "319ef1e811eff76cf85e5d8db0e79e55",
         HashAlgorithm.MD5SUM);
      list.add(htdCharacterScalar65);

      HashTestData<? extends Object> htdCharacterScalar66 = new HashTestData<Character>(
         new Character((char)0x23),
         "1b3ed51c2762e1a6e3630cf9017b40b880490ccb",
         HashAlgorithm.SHA1SUM);
      list.add(htdCharacterScalar66);

      HashTestData<? extends Object> htdCharacterScalar67 = new HashTestData<Character>(
         new Character((char)0x23),
         "43799a4d50e0cb19f37f6ce22b5cbcbf6954f13e9bb4604dca4104ae844374a4",
         HashAlgorithm.SHA256SUM);
      list.add(htdCharacterScalar67);

      HashTestData<? extends Object> htdCharacterScalar68 = new HashTestData<Character>(
         new Character((char)0x23),
         "a58240d9c21d9b3605fe679f379328e3bbf73ed7b8292396c189026256444ea6fc9c0993ea9beb482048ac662d22dad3",
         HashAlgorithm.SHA384SUM);
      list.add(htdCharacterScalar68);

      HashTestData<? extends Object> htdCharacterScalar69 = new HashTestData<Character>(
         new Character((char)0x23),
         "c9a6c7647397b538b487cfad8a06db1a42052652e0e9adae490e19965d786ec75b1d26fa3da2fa4c79d544b9f4c6b3759004aa99ac193e1a0a42df320d1ef71c",
         HashAlgorithm.SHA512SUM);
      list.add(htdCharacterScalar69);

      HashTestData<? extends Object> htdCharacterScalar70 = new HashTestData<Character>(
         new Character((char)0x7e),
         "d9597743233abcfa9332c376f0762b29",
         HashAlgorithm.MD5SUM);
      list.add(htdCharacterScalar70);

      HashTestData<? extends Object> htdCharacterScalar71 = new HashTestData<Character>(
         new Character((char)0x7e),
         "410100893e276b8bbb6c917fe8e60309d41bcf3d",
         HashAlgorithm.SHA1SUM);
      list.add(htdCharacterScalar71);

      HashTestData<? extends Object> htdCharacterScalar72 = new HashTestData<Character>(
         new Character((char)0x7e),
         "0d1abbe3b9da7a48d463edb0a844f3a102dcf7fdea35f9c771d885027b31b322",
         HashAlgorithm.SHA256SUM);
      list.add(htdCharacterScalar72);

      HashTestData<? extends Object> htdCharacterScalar73 = new HashTestData<Character>(
         new Character((char)0x7e),
         "2a40ba47817768e0e65b7942a3c1296180e2f6937853a5f74c0e5028aa4c9b9b1ed846cab2451b81400cc1adc2668a9a",
         HashAlgorithm.SHA384SUM);
      list.add(htdCharacterScalar73);

      HashTestData<? extends Object> htdCharacterScalar74 = new HashTestData<Character>(
         new Character((char)0x7e),
         "f08a7590ffa50860c2c045fd005404f5909275991729542df04d9e2d674277530f498540133d7f78d0ca514206ef7a4d4060f8101a2ce173f641cdf746ddb4a2",
         HashAlgorithm.SHA512SUM);
      list.add(htdCharacterScalar74);
   }
}
