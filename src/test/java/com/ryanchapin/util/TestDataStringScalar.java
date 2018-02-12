package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.List;

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestData;

public class TestDataStringScalar { 

   public static List<HashTestData<? extends Object>> list =
      new ArrayList<HashTestData<? extends Object>>();

   static {

      HashTestData<? extends Object> htdStringScalar290 = new HashTestData<String>(
         new String("Here is a String that is human readable.  It is a lot easier to read than the random Strings that are in this test data set, no?"),
         "bde7a553e49ab8b21bb0284813934097",
         HashAlgorithm.MD5SUM);
      list.add(htdStringScalar290);

      HashTestData<? extends Object> htdStringScalar291 = new HashTestData<String>(
         new String("Here is a String that is human readable.  It is a lot easier to read than the random Strings that are in this test data set, no?"),
         "a28b2c3824d27adaa6bee513c2e64cde41299f2e",
         HashAlgorithm.SHA1SUM);
      list.add(htdStringScalar291);

      HashTestData<? extends Object> htdStringScalar292 = new HashTestData<String>(
         new String("Here is a String that is human readable.  It is a lot easier to read than the random Strings that are in this test data set, no?"),
         "fc517e23108da33c97836a1453720a36e3f407f443f519c454322313604c7e71",
         HashAlgorithm.SHA256SUM);
      list.add(htdStringScalar292);

      HashTestData<? extends Object> htdStringScalar293 = new HashTestData<String>(
         new String("Here is a String that is human readable.  It is a lot easier to read than the random Strings that are in this test data set, no?"),
         "ab98b182d1ec418798ad4e56a21966a41f1aa620dd60d8f2a6ad459cffce548371e15e28e2489f9ab6dcf1f71c49bb4e",
         HashAlgorithm.SHA384SUM);
      list.add(htdStringScalar293);

      HashTestData<? extends Object> htdStringScalar294 = new HashTestData<String>(
         new String("Here is a String that is human readable.  It is a lot easier to read than the random Strings that are in this test data set, no?"),
         "7dd8fbb380e9622e6b61eb57aa665d0320eae3f2ad43ae2db9ed8852e7f0c71478dc8baedbbe551dc0d2ac6ab4b11e4d5b05c459f4bcb9f45e191496d2823198",
         HashAlgorithm.SHA512SUM);
      list.add(htdStringScalar294);

      HashTestData<? extends Object> htdStringScalar295 = new HashTestData<String>(
         new String("%[jG8IuFkuz:2>P8OFHs2[#n)w&KrlXzNy:c2bzg#vGuB6(e9sW$wxr3+AmS]>]AZJA5TZs)l5CYy)<qR!4WQ>#IE&f076N:joF(*lT6E1t$Tr%P<3R$:h#N<YpnQnrh"),
         "c98f8e5e7556e0d5ebedf37621a1890a",
         HashAlgorithm.MD5SUM);
      list.add(htdStringScalar295);

      HashTestData<? extends Object> htdStringScalar296 = new HashTestData<String>(
         new String("%[jG8IuFkuz:2>P8OFHs2[#n)w&KrlXzNy:c2bzg#vGuB6(e9sW$wxr3+AmS]>]AZJA5TZs)l5CYy)<qR!4WQ>#IE&f076N:joF(*lT6E1t$Tr%P<3R$:h#N<YpnQnrh"),
         "facc65d7a8f976c713015ee75c54b03395bfc779",
         HashAlgorithm.SHA1SUM);
      list.add(htdStringScalar296);

      HashTestData<? extends Object> htdStringScalar297 = new HashTestData<String>(
         new String("%[jG8IuFkuz:2>P8OFHs2[#n)w&KrlXzNy:c2bzg#vGuB6(e9sW$wxr3+AmS]>]AZJA5TZs)l5CYy)<qR!4WQ>#IE&f076N:joF(*lT6E1t$Tr%P<3R$:h#N<YpnQnrh"),
         "4bfc103bbdb5e30be37bfdd6d96da483c2628d77b5ec29b908a2b31b8df9d182",
         HashAlgorithm.SHA256SUM);
      list.add(htdStringScalar297);

      HashTestData<? extends Object> htdStringScalar298 = new HashTestData<String>(
         new String("%[jG8IuFkuz:2>P8OFHs2[#n)w&KrlXzNy:c2bzg#vGuB6(e9sW$wxr3+AmS]>]AZJA5TZs)l5CYy)<qR!4WQ>#IE&f076N:joF(*lT6E1t$Tr%P<3R$:h#N<YpnQnrh"),
         "3fa214843bce9a3e5c4c3935eb52c3d84e3f9ebe3f3d714ea629900f7172b707a4993e671277033cbb13b77f8abf1eb7",
         HashAlgorithm.SHA384SUM);
      list.add(htdStringScalar298);

      HashTestData<? extends Object> htdStringScalar299 = new HashTestData<String>(
         new String("%[jG8IuFkuz:2>P8OFHs2[#n)w&KrlXzNy:c2bzg#vGuB6(e9sW$wxr3+AmS]>]AZJA5TZs)l5CYy)<qR!4WQ>#IE&f076N:joF(*lT6E1t$Tr%P<3R$:h#N<YpnQnrh"),
         "d99ab54d06d9980119518d8b7e88fb2e35df089bc0693f8006ef7024d50d8dec26d973e0092634f0f365129b3a1fca42c1288596adc172ad225c57fbf1f0658a",
         HashAlgorithm.SHA512SUM);
      list.add(htdStringScalar299);

      HashTestData<? extends Object> htdStringScalar300 = new HashTestData<String>(
         new String("J!^ktjn@^N1_f33>cJ:iBTR2nH7Q0uaSs35^O0n)%V)MKC[5RBpD_aU%A>VPfFjv8xr+o>!f2<(bqnFKxyhQ<N]fAa52pF>6Hm1G5%[h+vHfomJ)qg)GgoO_v9$#&EL2"),
         "5ea3850467fe128706c2dc159316e2b7",
         HashAlgorithm.MD5SUM);
      list.add(htdStringScalar300);

      HashTestData<? extends Object> htdStringScalar301 = new HashTestData<String>(
         new String("J!^ktjn@^N1_f33>cJ:iBTR2nH7Q0uaSs35^O0n)%V)MKC[5RBpD_aU%A>VPfFjv8xr+o>!f2<(bqnFKxyhQ<N]fAa52pF>6Hm1G5%[h+vHfomJ)qg)GgoO_v9$#&EL2"),
         "73e83a22999a0ceaa47a306f358a5cceb8099751",
         HashAlgorithm.SHA1SUM);
      list.add(htdStringScalar301);

      HashTestData<? extends Object> htdStringScalar302 = new HashTestData<String>(
         new String("J!^ktjn@^N1_f33>cJ:iBTR2nH7Q0uaSs35^O0n)%V)MKC[5RBpD_aU%A>VPfFjv8xr+o>!f2<(bqnFKxyhQ<N]fAa52pF>6Hm1G5%[h+vHfomJ)qg)GgoO_v9$#&EL2"),
         "81cde82f5d533a6bb1566ff07c512ed963fc3a369e0621173db411d9c22b049f",
         HashAlgorithm.SHA256SUM);
      list.add(htdStringScalar302);

      HashTestData<? extends Object> htdStringScalar303 = new HashTestData<String>(
         new String("J!^ktjn@^N1_f33>cJ:iBTR2nH7Q0uaSs35^O0n)%V)MKC[5RBpD_aU%A>VPfFjv8xr+o>!f2<(bqnFKxyhQ<N]fAa52pF>6Hm1G5%[h+vHfomJ)qg)GgoO_v9$#&EL2"),
         "ab12e9621f0f22871bb197db434bc4688d965da3db9715978413d3c5627428d951a0829b9b40d9b9f5b537846c61d87c",
         HashAlgorithm.SHA384SUM);
      list.add(htdStringScalar303);

      HashTestData<? extends Object> htdStringScalar304 = new HashTestData<String>(
         new String("J!^ktjn@^N1_f33>cJ:iBTR2nH7Q0uaSs35^O0n)%V)MKC[5RBpD_aU%A>VPfFjv8xr+o>!f2<(bqnFKxyhQ<N]fAa52pF>6Hm1G5%[h+vHfomJ)qg)GgoO_v9$#&EL2"),
         "feedbbd563f3a1122311e5e4978753c4bded49cb0f52e7641c3271e7b33600d6d17c132ac0390e6ab9ce7f6e3da12158dd0a89c106066670642fd0111f121c9c",
         HashAlgorithm.SHA512SUM);
      list.add(htdStringScalar304);

      HashTestData<? extends Object> htdStringScalar305 = new HashTestData<String>(
         new String("V(mzH*!7PoBIgwpft#YX_K[xvo0^Pt33WxVQZVlVu!!JZ!TJ+*h!ePpjt??MPG*mHFpEzKBy:OHBK0DX6jCq%N18sT@X!&Lv$q4E%]>204S$IH[4wXJTYB$jYyfWOG4n"),
         "d90d48cd75e78b04bc5c54534ae813f4",
         HashAlgorithm.MD5SUM);
      list.add(htdStringScalar305);

      HashTestData<? extends Object> htdStringScalar306 = new HashTestData<String>(
         new String("V(mzH*!7PoBIgwpft#YX_K[xvo0^Pt33WxVQZVlVu!!JZ!TJ+*h!ePpjt??MPG*mHFpEzKBy:OHBK0DX6jCq%N18sT@X!&Lv$q4E%]>204S$IH[4wXJTYB$jYyfWOG4n"),
         "8a6af4b0bc5a35764ce4052bb49a20f475993fa0",
         HashAlgorithm.SHA1SUM);
      list.add(htdStringScalar306);

      HashTestData<? extends Object> htdStringScalar307 = new HashTestData<String>(
         new String("V(mzH*!7PoBIgwpft#YX_K[xvo0^Pt33WxVQZVlVu!!JZ!TJ+*h!ePpjt??MPG*mHFpEzKBy:OHBK0DX6jCq%N18sT@X!&Lv$q4E%]>204S$IH[4wXJTYB$jYyfWOG4n"),
         "0b31e7f4d6c0a5df1d49a3c740e1dbdcb08733ff5f6a890708be5bb2987e3961",
         HashAlgorithm.SHA256SUM);
      list.add(htdStringScalar307);

      HashTestData<? extends Object> htdStringScalar308 = new HashTestData<String>(
         new String("V(mzH*!7PoBIgwpft#YX_K[xvo0^Pt33WxVQZVlVu!!JZ!TJ+*h!ePpjt??MPG*mHFpEzKBy:OHBK0DX6jCq%N18sT@X!&Lv$q4E%]>204S$IH[4wXJTYB$jYyfWOG4n"),
         "35a3eb15cd3a111df7c78ad527153a98e08a6a471724db209e64205964e2ae89dea4353393de183135b92f8d4401f65d",
         HashAlgorithm.SHA384SUM);
      list.add(htdStringScalar308);

      HashTestData<? extends Object> htdStringScalar309 = new HashTestData<String>(
         new String("V(mzH*!7PoBIgwpft#YX_K[xvo0^Pt33WxVQZVlVu!!JZ!TJ+*h!ePpjt??MPG*mHFpEzKBy:OHBK0DX6jCq%N18sT@X!&Lv$q4E%]>204S$IH[4wXJTYB$jYyfWOG4n"),
         "a2453fbf0e527261c4db3908c5757d1de46619049720143eb52dc37386d65e96677933a0cfb4c38f66d9cb7f2ce9ea03de2723b86fd08f9e8b06bb6774a19dfe",
         HashAlgorithm.SHA512SUM);
      list.add(htdStringScalar309);

      HashTestData<? extends Object> htdStringScalar310 = new HashTestData<String>(
         new String("UcPj*cFKrY*^AboKOQ1[>3s%_?b$H0^]C_]eSVt:_$G6arXFDabp>KF[e_58#<EJ0mYt)@89$2o^e!zRgl@ewfyY1iY5zelcFXYhzStD9?*cnplpp8_l(L(A@rKB7^am"),
         "6a6b0f178c44ed36401fcb520f84e01b",
         HashAlgorithm.MD5SUM);
      list.add(htdStringScalar310);

      HashTestData<? extends Object> htdStringScalar311 = new HashTestData<String>(
         new String("UcPj*cFKrY*^AboKOQ1[>3s%_?b$H0^]C_]eSVt:_$G6arXFDabp>KF[e_58#<EJ0mYt)@89$2o^e!zRgl@ewfyY1iY5zelcFXYhzStD9?*cnplpp8_l(L(A@rKB7^am"),
         "7304ad2c2d61f8dca57e3cc8deeb11012d5ac022",
         HashAlgorithm.SHA1SUM);
      list.add(htdStringScalar311);

      HashTestData<? extends Object> htdStringScalar312 = new HashTestData<String>(
         new String("UcPj*cFKrY*^AboKOQ1[>3s%_?b$H0^]C_]eSVt:_$G6arXFDabp>KF[e_58#<EJ0mYt)@89$2o^e!zRgl@ewfyY1iY5zelcFXYhzStD9?*cnplpp8_l(L(A@rKB7^am"),
         "4491f528df58c3315d0bc94705c44462282d31ebb7839a5e907885c6ea8f171e",
         HashAlgorithm.SHA256SUM);
      list.add(htdStringScalar312);

      HashTestData<? extends Object> htdStringScalar313 = new HashTestData<String>(
         new String("UcPj*cFKrY*^AboKOQ1[>3s%_?b$H0^]C_]eSVt:_$G6arXFDabp>KF[e_58#<EJ0mYt)@89$2o^e!zRgl@ewfyY1iY5zelcFXYhzStD9?*cnplpp8_l(L(A@rKB7^am"),
         "32b2b4ba6df7edae9fce96745af027230d0674ddfa8dce23b2ef8cda41640bda61b7fb9ae8112b12f9796f66183e7d20",
         HashAlgorithm.SHA384SUM);
      list.add(htdStringScalar313);

      HashTestData<? extends Object> htdStringScalar314 = new HashTestData<String>(
         new String("UcPj*cFKrY*^AboKOQ1[>3s%_?b$H0^]C_]eSVt:_$G6arXFDabp>KF[e_58#<EJ0mYt)@89$2o^e!zRgl@ewfyY1iY5zelcFXYhzStD9?*cnplpp8_l(L(A@rKB7^am"),
         "ebb5f5d89c974cbae4b8119d48644428a445b22d112cdc3f3d6796ea9102f4b6056c15035e6a3bb0dddf0e5ed025a566421963cdfb16f49af4b5aec71a3fcd80",
         HashAlgorithm.SHA512SUM);
      list.add(htdStringScalar314);
   }
}
