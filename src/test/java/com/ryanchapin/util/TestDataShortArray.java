package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestDataList;

public class TestDataShortArray { 

   public static List<HashTestDataList<? extends Object>> list =
      new ArrayList<HashTestDataList<? extends Object>>();

   static {

      HashTestDataList<? extends Object> htdShortArray235 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)0, (short)647, (short)32767}),
         "6939a58978c8dbf7034957e355f6c6b0",
         HashAlgorithm.MD5SUM);
      list.add(htdShortArray235);

      HashTestDataList<? extends Object> htdShortArray236 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)0, (short)647, (short)32767}),
         "39ad7563129a920ae8f2269dd9643d4ee650dea7",
         HashAlgorithm.SHA1SUM);
      list.add(htdShortArray236);

      HashTestDataList<? extends Object> htdShortArray237 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)0, (short)647, (short)32767}),
         "e7ce5d191f786396d9f6c9da974976c8275f24d0c4b92e0a428c54d13f2363c1",
         HashAlgorithm.SHA256SUM);
      list.add(htdShortArray237);

      HashTestDataList<? extends Object> htdShortArray238 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)0, (short)647, (short)32767}),
         "2f5d6535754e52feb10ebd0deeae3c7a4d3fdd35ff5f46b57f394004920137fb78400b5938044f8276b17d9845ad43a7",
         HashAlgorithm.SHA384SUM);
      list.add(htdShortArray238);

      HashTestDataList<? extends Object> htdShortArray239 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)0, (short)647, (short)32767}),
         "53e69ec1acfe34510795d07fee165f3188f38f54b81fce76e9f63de01f831deaa60bba044c9da1cfc6c0ea9fe1c97b18114120b4eb0922c061ef208064f74a85",
         HashAlgorithm.SHA512SUM);
      list.add(htdShortArray239);

      HashTestDataList<? extends Object> htdShortArray240 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-31909, (short)-31340, (short)-30757, (short)-30175, (short)-29542, (short)-28616, (short)-27969, (short)-27293, (short)-26547, (short)-25963, (short)-25266, (short)-25006, (short)-24232, (short)-23888, (short)-23359, (short)-22581, (short)-21839, (short)-21232, (short)-20407, (short)-20154, (short)-19853, (short)-19515, (short)-18678, (short)-18218, (short)-17262, (short)-16726, (short)-16020, (short)-15238, (short)-14621, (short)-13845, (short)-13479, (short)-12695, (short)-12186, (short)-11190, (short)-10383, (short)-9537, (short)-8930, (short)-8016, (short)-7341, (short)-6874, (short)-5919, (short)-5599, (short)-4755, (short)-4223, (short)-3849, (short)-2892, (short)-2542, (short)-2137, (short)-1374, (short)-455, (short)1, (short)895, (short)1158, (short)2096, (short)2788, (short)3763, (short)4652, (short)5627, (short)6527, (short)6969, (short)7522, (short)8476, (short)8954, (short)9903, (short)10201, (short)10871, (short)11215, (short)11570, (short)11950, (short)12851, (short)13248, (short)14130, (short)15074, (short)15781, (short)16750, (short)17294, (short)17837, (short)18813, (short)19309, (short)19932, (short)20657, (short)21551, (short)22082, (short)22549, (short)22920, (short)23705, (short)24017, (short)24426, (short)24832, (short)25351, (short)25910, (short)26837, (short)27726, (short)28649, (short)29276, (short)30215, (short)30910, (short)31353, (short)32014, (short)32548}),
         "efca13c8f454150aca3d4ffdb046a600",
         HashAlgorithm.MD5SUM);
      list.add(htdShortArray240);

      HashTestDataList<? extends Object> htdShortArray241 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-31909, (short)-31340, (short)-30757, (short)-30175, (short)-29542, (short)-28616, (short)-27969, (short)-27293, (short)-26547, (short)-25963, (short)-25266, (short)-25006, (short)-24232, (short)-23888, (short)-23359, (short)-22581, (short)-21839, (short)-21232, (short)-20407, (short)-20154, (short)-19853, (short)-19515, (short)-18678, (short)-18218, (short)-17262, (short)-16726, (short)-16020, (short)-15238, (short)-14621, (short)-13845, (short)-13479, (short)-12695, (short)-12186, (short)-11190, (short)-10383, (short)-9537, (short)-8930, (short)-8016, (short)-7341, (short)-6874, (short)-5919, (short)-5599, (short)-4755, (short)-4223, (short)-3849, (short)-2892, (short)-2542, (short)-2137, (short)-1374, (short)-455, (short)1, (short)895, (short)1158, (short)2096, (short)2788, (short)3763, (short)4652, (short)5627, (short)6527, (short)6969, (short)7522, (short)8476, (short)8954, (short)9903, (short)10201, (short)10871, (short)11215, (short)11570, (short)11950, (short)12851, (short)13248, (short)14130, (short)15074, (short)15781, (short)16750, (short)17294, (short)17837, (short)18813, (short)19309, (short)19932, (short)20657, (short)21551, (short)22082, (short)22549, (short)22920, (short)23705, (short)24017, (short)24426, (short)24832, (short)25351, (short)25910, (short)26837, (short)27726, (short)28649, (short)29276, (short)30215, (short)30910, (short)31353, (short)32014, (short)32548}),
         "c064e800c6f583cacb78ca534263681aedc64485",
         HashAlgorithm.SHA1SUM);
      list.add(htdShortArray241);

      HashTestDataList<? extends Object> htdShortArray242 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-31909, (short)-31340, (short)-30757, (short)-30175, (short)-29542, (short)-28616, (short)-27969, (short)-27293, (short)-26547, (short)-25963, (short)-25266, (short)-25006, (short)-24232, (short)-23888, (short)-23359, (short)-22581, (short)-21839, (short)-21232, (short)-20407, (short)-20154, (short)-19853, (short)-19515, (short)-18678, (short)-18218, (short)-17262, (short)-16726, (short)-16020, (short)-15238, (short)-14621, (short)-13845, (short)-13479, (short)-12695, (short)-12186, (short)-11190, (short)-10383, (short)-9537, (short)-8930, (short)-8016, (short)-7341, (short)-6874, (short)-5919, (short)-5599, (short)-4755, (short)-4223, (short)-3849, (short)-2892, (short)-2542, (short)-2137, (short)-1374, (short)-455, (short)1, (short)895, (short)1158, (short)2096, (short)2788, (short)3763, (short)4652, (short)5627, (short)6527, (short)6969, (short)7522, (short)8476, (short)8954, (short)9903, (short)10201, (short)10871, (short)11215, (short)11570, (short)11950, (short)12851, (short)13248, (short)14130, (short)15074, (short)15781, (short)16750, (short)17294, (short)17837, (short)18813, (short)19309, (short)19932, (short)20657, (short)21551, (short)22082, (short)22549, (short)22920, (short)23705, (short)24017, (short)24426, (short)24832, (short)25351, (short)25910, (short)26837, (short)27726, (short)28649, (short)29276, (short)30215, (short)30910, (short)31353, (short)32014, (short)32548}),
         "b2f4776056c23585f3a95e3009ed990737d8931875ecfde162547d01b0e5015f",
         HashAlgorithm.SHA256SUM);
      list.add(htdShortArray242);

      HashTestDataList<? extends Object> htdShortArray243 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-31909, (short)-31340, (short)-30757, (short)-30175, (short)-29542, (short)-28616, (short)-27969, (short)-27293, (short)-26547, (short)-25963, (short)-25266, (short)-25006, (short)-24232, (short)-23888, (short)-23359, (short)-22581, (short)-21839, (short)-21232, (short)-20407, (short)-20154, (short)-19853, (short)-19515, (short)-18678, (short)-18218, (short)-17262, (short)-16726, (short)-16020, (short)-15238, (short)-14621, (short)-13845, (short)-13479, (short)-12695, (short)-12186, (short)-11190, (short)-10383, (short)-9537, (short)-8930, (short)-8016, (short)-7341, (short)-6874, (short)-5919, (short)-5599, (short)-4755, (short)-4223, (short)-3849, (short)-2892, (short)-2542, (short)-2137, (short)-1374, (short)-455, (short)1, (short)895, (short)1158, (short)2096, (short)2788, (short)3763, (short)4652, (short)5627, (short)6527, (short)6969, (short)7522, (short)8476, (short)8954, (short)9903, (short)10201, (short)10871, (short)11215, (short)11570, (short)11950, (short)12851, (short)13248, (short)14130, (short)15074, (short)15781, (short)16750, (short)17294, (short)17837, (short)18813, (short)19309, (short)19932, (short)20657, (short)21551, (short)22082, (short)22549, (short)22920, (short)23705, (short)24017, (short)24426, (short)24832, (short)25351, (short)25910, (short)26837, (short)27726, (short)28649, (short)29276, (short)30215, (short)30910, (short)31353, (short)32014, (short)32548}),
         "2b2c48e819092b90ffe3e48a4b3358446c5e37936759804e0de411c735c54a842081e189a516018be87b85680641ff99",
         HashAlgorithm.SHA384SUM);
      list.add(htdShortArray243);

      HashTestDataList<? extends Object> htdShortArray244 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-31909, (short)-31340, (short)-30757, (short)-30175, (short)-29542, (short)-28616, (short)-27969, (short)-27293, (short)-26547, (short)-25963, (short)-25266, (short)-25006, (short)-24232, (short)-23888, (short)-23359, (short)-22581, (short)-21839, (short)-21232, (short)-20407, (short)-20154, (short)-19853, (short)-19515, (short)-18678, (short)-18218, (short)-17262, (short)-16726, (short)-16020, (short)-15238, (short)-14621, (short)-13845, (short)-13479, (short)-12695, (short)-12186, (short)-11190, (short)-10383, (short)-9537, (short)-8930, (short)-8016, (short)-7341, (short)-6874, (short)-5919, (short)-5599, (short)-4755, (short)-4223, (short)-3849, (short)-2892, (short)-2542, (short)-2137, (short)-1374, (short)-455, (short)1, (short)895, (short)1158, (short)2096, (short)2788, (short)3763, (short)4652, (short)5627, (short)6527, (short)6969, (short)7522, (short)8476, (short)8954, (short)9903, (short)10201, (short)10871, (short)11215, (short)11570, (short)11950, (short)12851, (short)13248, (short)14130, (short)15074, (short)15781, (short)16750, (short)17294, (short)17837, (short)18813, (short)19309, (short)19932, (short)20657, (short)21551, (short)22082, (short)22549, (short)22920, (short)23705, (short)24017, (short)24426, (short)24832, (short)25351, (short)25910, (short)26837, (short)27726, (short)28649, (short)29276, (short)30215, (short)30910, (short)31353, (short)32014, (short)32548}),
         "e079d03c2f0f533f0dbe4dbe7e477264469f236f9d193ee4562a6fa6aacfc239da97675fca77b6043d32e1202c0bec67167d0de3f90a88b7d61cf8e543aaf129",
         HashAlgorithm.SHA512SUM);
      list.add(htdShortArray244);

      HashTestDataList<? extends Object> htdShortArray245 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-32225, (short)-31537, (short)-31003, (short)-30472, (short)-29951, (short)-29643, (short)-28786, (short)-27864, (short)-27339, (short)-26469, (short)-26148, (short)-25673, (short)-25063, (short)-24334, (short)-23530, (short)-22606, (short)-22040, (short)-21287, (short)-20615, (short)-19718, (short)-18966, (short)-18348, (short)-17547, (short)-16885, (short)-16205, (short)-15927, (short)-15542, (short)-14920, (short)-14506, (short)-13711, (short)-13099, (short)-12340, (short)-11615, (short)-11271, (short)-10784, (short)-9841, (short)-9105, (short)-8805, (short)-8444, (short)-7623, (short)-6860, (short)-5876, (short)-5256, (short)-4895, (short)-4595, (short)-3673, (short)-3247, (short)-2526, (short)-1753, (short)-1072, (short)-159, (short)657, (short)1615, (short)2416, (short)3035, (short)3678, (short)4050, (short)4826, (short)5150, (short)6139, (short)6669, (short)7205, (short)7483, (short)8349, (short)9258, (short)9806, (short)10211, (short)10815, (short)11157, (short)11865, (short)12481, (short)13305, (short)13862, (short)14509, (short)14905, (short)15559, (short)16548, (short)17447, (short)18219, (short)18736, (short)19542, (short)20539, (short)20986, (short)21900, (short)22814, (short)23354, (short)24293, (short)24798, (short)25419, (short)25792, (short)26483, (short)27096, (short)27554, (short)28279, (short)28581, (short)29270, (short)30200, (short)30854, (short)31185, (short)31994, (short)32387}),
         "2b0b3f29c435d22dda7939bf8dd92c18",
         HashAlgorithm.MD5SUM);
      list.add(htdShortArray245);

      HashTestDataList<? extends Object> htdShortArray246 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-32225, (short)-31537, (short)-31003, (short)-30472, (short)-29951, (short)-29643, (short)-28786, (short)-27864, (short)-27339, (short)-26469, (short)-26148, (short)-25673, (short)-25063, (short)-24334, (short)-23530, (short)-22606, (short)-22040, (short)-21287, (short)-20615, (short)-19718, (short)-18966, (short)-18348, (short)-17547, (short)-16885, (short)-16205, (short)-15927, (short)-15542, (short)-14920, (short)-14506, (short)-13711, (short)-13099, (short)-12340, (short)-11615, (short)-11271, (short)-10784, (short)-9841, (short)-9105, (short)-8805, (short)-8444, (short)-7623, (short)-6860, (short)-5876, (short)-5256, (short)-4895, (short)-4595, (short)-3673, (short)-3247, (short)-2526, (short)-1753, (short)-1072, (short)-159, (short)657, (short)1615, (short)2416, (short)3035, (short)3678, (short)4050, (short)4826, (short)5150, (short)6139, (short)6669, (short)7205, (short)7483, (short)8349, (short)9258, (short)9806, (short)10211, (short)10815, (short)11157, (short)11865, (short)12481, (short)13305, (short)13862, (short)14509, (short)14905, (short)15559, (short)16548, (short)17447, (short)18219, (short)18736, (short)19542, (short)20539, (short)20986, (short)21900, (short)22814, (short)23354, (short)24293, (short)24798, (short)25419, (short)25792, (short)26483, (short)27096, (short)27554, (short)28279, (short)28581, (short)29270, (short)30200, (short)30854, (short)31185, (short)31994, (short)32387}),
         "4f43fca3a68be6d54578f5047cadc80c24067b85",
         HashAlgorithm.SHA1SUM);
      list.add(htdShortArray246);

      HashTestDataList<? extends Object> htdShortArray247 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-32225, (short)-31537, (short)-31003, (short)-30472, (short)-29951, (short)-29643, (short)-28786, (short)-27864, (short)-27339, (short)-26469, (short)-26148, (short)-25673, (short)-25063, (short)-24334, (short)-23530, (short)-22606, (short)-22040, (short)-21287, (short)-20615, (short)-19718, (short)-18966, (short)-18348, (short)-17547, (short)-16885, (short)-16205, (short)-15927, (short)-15542, (short)-14920, (short)-14506, (short)-13711, (short)-13099, (short)-12340, (short)-11615, (short)-11271, (short)-10784, (short)-9841, (short)-9105, (short)-8805, (short)-8444, (short)-7623, (short)-6860, (short)-5876, (short)-5256, (short)-4895, (short)-4595, (short)-3673, (short)-3247, (short)-2526, (short)-1753, (short)-1072, (short)-159, (short)657, (short)1615, (short)2416, (short)3035, (short)3678, (short)4050, (short)4826, (short)5150, (short)6139, (short)6669, (short)7205, (short)7483, (short)8349, (short)9258, (short)9806, (short)10211, (short)10815, (short)11157, (short)11865, (short)12481, (short)13305, (short)13862, (short)14509, (short)14905, (short)15559, (short)16548, (short)17447, (short)18219, (short)18736, (short)19542, (short)20539, (short)20986, (short)21900, (short)22814, (short)23354, (short)24293, (short)24798, (short)25419, (short)25792, (short)26483, (short)27096, (short)27554, (short)28279, (short)28581, (short)29270, (short)30200, (short)30854, (short)31185, (short)31994, (short)32387}),
         "89851296de447627ac82a7545a492468dd6ec877455e7e22be454d06e0176967",
         HashAlgorithm.SHA256SUM);
      list.add(htdShortArray247);

      HashTestDataList<? extends Object> htdShortArray248 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-32225, (short)-31537, (short)-31003, (short)-30472, (short)-29951, (short)-29643, (short)-28786, (short)-27864, (short)-27339, (short)-26469, (short)-26148, (short)-25673, (short)-25063, (short)-24334, (short)-23530, (short)-22606, (short)-22040, (short)-21287, (short)-20615, (short)-19718, (short)-18966, (short)-18348, (short)-17547, (short)-16885, (short)-16205, (short)-15927, (short)-15542, (short)-14920, (short)-14506, (short)-13711, (short)-13099, (short)-12340, (short)-11615, (short)-11271, (short)-10784, (short)-9841, (short)-9105, (short)-8805, (short)-8444, (short)-7623, (short)-6860, (short)-5876, (short)-5256, (short)-4895, (short)-4595, (short)-3673, (short)-3247, (short)-2526, (short)-1753, (short)-1072, (short)-159, (short)657, (short)1615, (short)2416, (short)3035, (short)3678, (short)4050, (short)4826, (short)5150, (short)6139, (short)6669, (short)7205, (short)7483, (short)8349, (short)9258, (short)9806, (short)10211, (short)10815, (short)11157, (short)11865, (short)12481, (short)13305, (short)13862, (short)14509, (short)14905, (short)15559, (short)16548, (short)17447, (short)18219, (short)18736, (short)19542, (short)20539, (short)20986, (short)21900, (short)22814, (short)23354, (short)24293, (short)24798, (short)25419, (short)25792, (short)26483, (short)27096, (short)27554, (short)28279, (short)28581, (short)29270, (short)30200, (short)30854, (short)31185, (short)31994, (short)32387}),
         "c59a2f9bf78363e8dc2da16080be51a779de620e3cefff6af004ddbf71c2a73e9b17902305befe85729015c06675e8b5",
         HashAlgorithm.SHA384SUM);
      list.add(htdShortArray248);

      HashTestDataList<? extends Object> htdShortArray249 = new HashTestDataList<Short>(
         Arrays.asList(new Short[] {(short)-32768, (short)-32225, (short)-31537, (short)-31003, (short)-30472, (short)-29951, (short)-29643, (short)-28786, (short)-27864, (short)-27339, (short)-26469, (short)-26148, (short)-25673, (short)-25063, (short)-24334, (short)-23530, (short)-22606, (short)-22040, (short)-21287, (short)-20615, (short)-19718, (short)-18966, (short)-18348, (short)-17547, (short)-16885, (short)-16205, (short)-15927, (short)-15542, (short)-14920, (short)-14506, (short)-13711, (short)-13099, (short)-12340, (short)-11615, (short)-11271, (short)-10784, (short)-9841, (short)-9105, (short)-8805, (short)-8444, (short)-7623, (short)-6860, (short)-5876, (short)-5256, (short)-4895, (short)-4595, (short)-3673, (short)-3247, (short)-2526, (short)-1753, (short)-1072, (short)-159, (short)657, (short)1615, (short)2416, (short)3035, (short)3678, (short)4050, (short)4826, (short)5150, (short)6139, (short)6669, (short)7205, (short)7483, (short)8349, (short)9258, (short)9806, (short)10211, (short)10815, (short)11157, (short)11865, (short)12481, (short)13305, (short)13862, (short)14509, (short)14905, (short)15559, (short)16548, (short)17447, (short)18219, (short)18736, (short)19542, (short)20539, (short)20986, (short)21900, (short)22814, (short)23354, (short)24293, (short)24798, (short)25419, (short)25792, (short)26483, (short)27096, (short)27554, (short)28279, (short)28581, (short)29270, (short)30200, (short)30854, (short)31185, (short)31994, (short)32387}),
         "896bb661365e233f15bd52095297d505dec89fce83c2bbce5fe5684ca6f98ee5e2ad4e5c456c39490b9e84bfc73ca88c6d40fdd387ed13f8f0e22654143f3629",
         HashAlgorithm.SHA512SUM);
      list.add(htdShortArray249);
   }
}
