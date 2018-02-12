package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.List;

import com.ryanchapin.util.HashGenerator.HashAlgorithm;
import com.ryanchapin.util.HashGeneratorTest.HashTestData;

public class TestDataByteScalar { 

   public static List<HashTestData<? extends Object>> list =
      new ArrayList<HashTestData<? extends Object>>();

   static {

      HashTestData<? extends Object> htdByteScalar10 = new HashTestData<Byte>(
         new Byte((byte)-128),
         "8d39dd7eef115ea6975446ef4082951f",
         HashAlgorithm.MD5SUM);
      list.add(htdByteScalar10);

      HashTestData<? extends Object> htdByteScalar11 = new HashTestData<Byte>(
         new Byte((byte)-128),
         "c78ebd3c85a39a596d9f5cfd2b8d240bc1b9c125",
         HashAlgorithm.SHA1SUM);
      list.add(htdByteScalar11);

      HashTestData<? extends Object> htdByteScalar12 = new HashTestData<Byte>(
         new Byte((byte)-128),
         "76be8b528d0075f7aae98d6fa57a6d3c83ae480a8469e668d7b0af968995ac71",
         HashAlgorithm.SHA256SUM);
      list.add(htdByteScalar12);

      HashTestData<? extends Object> htdByteScalar13 = new HashTestData<Byte>(
         new Byte((byte)-128),
         "8db630b3036f40e29aa4c95e3c0156f55a593bf005fc425936d2134a4fc0b855bb4ab4767dd4b0b1dd261cd111598d3d",
         HashAlgorithm.SHA384SUM);
      list.add(htdByteScalar13);

      HashTestData<? extends Object> htdByteScalar14 = new HashTestData<Byte>(
         new Byte((byte)-128),
         "dfe8ef54110b3324d3b889035c95cfb80c92704614bf76f17546ad4f4b08218a630e16da7df34766a975b3bb85b01df9e99a4ec0a1d0ec3de6bed7b7a40b2f10",
         HashAlgorithm.SHA512SUM);
      list.add(htdByteScalar14);

      HashTestData<? extends Object> htdByteScalar15 = new HashTestData<Byte>(
         new Byte((byte)-23),
         "3406877694691ddd1dfb0aca54681407",
         HashAlgorithm.MD5SUM);
      list.add(htdByteScalar15);

      HashTestData<? extends Object> htdByteScalar16 = new HashTestData<Byte>(
         new Byte((byte)-23),
         "1599e9fa41ec68c80230491902786bee889f5bcb",
         HashAlgorithm.SHA1SUM);
      list.add(htdByteScalar16);

      HashTestData<? extends Object> htdByteScalar17 = new HashTestData<Byte>(
         new Byte((byte)-23),
         "de2e331d891ae267a7009cb45b4e8830f170e0c937288ea2731a1941c7a53b0d",
         HashAlgorithm.SHA256SUM);
      list.add(htdByteScalar17);

      HashTestData<? extends Object> htdByteScalar18 = new HashTestData<Byte>(
         new Byte((byte)-23),
         "cf16e9b450d92416b3a86e452a5162e8d6d496a9e126dcead3d60ce8cb9242baeb94b24c73af76c71c00c11a7c0acc03",
         HashAlgorithm.SHA384SUM);
      list.add(htdByteScalar18);

      HashTestData<? extends Object> htdByteScalar19 = new HashTestData<Byte>(
         new Byte((byte)-23),
         "b164716d213ddec0e2dbf8f2c7cc16d673b14c7a5ce46826e10ad36abe97fdeb9a34bb379da49edf29a46f4d2b77883c97786b63d543924dad3873c43a91a1af",
         HashAlgorithm.SHA512SUM);
      list.add(htdByteScalar19);

      HashTestData<? extends Object> htdByteScalar20 = new HashTestData<Byte>(
         new Byte((byte)0),
         "93b885adfe0da089cdf634904fd59f71",
         HashAlgorithm.MD5SUM);
      list.add(htdByteScalar20);

      HashTestData<? extends Object> htdByteScalar21 = new HashTestData<Byte>(
         new Byte((byte)0),
         "5ba93c9db0cff93f52b521d7420e43f6eda2784f",
         HashAlgorithm.SHA1SUM);
      list.add(htdByteScalar21);

      HashTestData<? extends Object> htdByteScalar22 = new HashTestData<Byte>(
         new Byte((byte)0),
         "6e340b9cffb37a989ca544e6bb780a2c78901d3fb33738768511a30617afa01d",
         HashAlgorithm.SHA256SUM);
      list.add(htdByteScalar22);

      HashTestData<? extends Object> htdByteScalar23 = new HashTestData<Byte>(
         new Byte((byte)0),
         "bec021b4f368e3069134e012c2b4307083d3a9bdd206e24e5f0d86e13d6636655933ec2b413465966817a9c208a11717",
         HashAlgorithm.SHA384SUM);
      list.add(htdByteScalar23);

      HashTestData<? extends Object> htdByteScalar24 = new HashTestData<Byte>(
         new Byte((byte)0),
         "b8244d028981d693af7b456af8efa4cad63d282e19ff14942c246e50d9351d22704a802a71c3580b6370de4ceb293c324a8423342557d4e5c38438f0e36910ee",
         HashAlgorithm.SHA512SUM);
      list.add(htdByteScalar24);

      HashTestData<? extends Object> htdByteScalar25 = new HashTestData<Byte>(
         new Byte((byte)87),
         "61e9c06ea9a85a5088a499df6458d276",
         HashAlgorithm.MD5SUM);
      list.add(htdByteScalar25);

      HashTestData<? extends Object> htdByteScalar26 = new HashTestData<Byte>(
         new Byte((byte)87),
         "e2415cb7f63df0c9de23362326ad3c37a9adfc96",
         HashAlgorithm.SHA1SUM);
      list.add(htdByteScalar26);

      HashTestData<? extends Object> htdByteScalar27 = new HashTestData<Byte>(
         new Byte((byte)87),
         "fcb5f40df9be6bae66c1d77a6c15968866a9e6cbd7314ca432b019d17392f6f4",
         HashAlgorithm.SHA256SUM);
      list.add(htdByteScalar27);

      HashTestData<? extends Object> htdByteScalar28 = new HashTestData<Byte>(
         new Byte((byte)87),
         "0b75998ad89118532ac2d0d3561fbd159ec73950df3ec5cd0e358a9a166f0e267f110d029865e736c266eb528d1729d3",
         HashAlgorithm.SHA384SUM);
      list.add(htdByteScalar28);

      HashTestData<? extends Object> htdByteScalar29 = new HashTestData<Byte>(
         new Byte((byte)87),
         "61037724a2fb00a12fa8a53ada233f54ff7fc6ee02048e579b83a127ce79fc03906c35b5307beb18a3cf5021fe031ae6587448d4d60a082d73252fe2e2f4ae7f",
         HashAlgorithm.SHA512SUM);
      list.add(htdByteScalar29);

      HashTestData<? extends Object> htdByteScalar30 = new HashTestData<Byte>(
         new Byte((byte)127),
         "83acb6e67e50e31db6ed341dd2de1595",
         HashAlgorithm.MD5SUM);
      list.add(htdByteScalar30);

      HashTestData<? extends Object> htdByteScalar31 = new HashTestData<Byte>(
         new Byte((byte)127),
         "23833462f55515a900e016db2eb943fb474c19f6",
         HashAlgorithm.SHA1SUM);
      list.add(htdByteScalar31);

      HashTestData<? extends Object> htdByteScalar32 = new HashTestData<Byte>(
         new Byte((byte)127),
         "620bfdaa346b088fb49998d92f19a7eaf6bfc2fb0aee015753966da1028cb731",
         HashAlgorithm.SHA256SUM);
      list.add(htdByteScalar32);

      HashTestData<? extends Object> htdByteScalar33 = new HashTestData<Byte>(
         new Byte((byte)127),
         "23a8a9d42d150a471e8502ee2f4e822cb955e798882d698c5bd5aa01e43137cb566fefb1b06dced14b43c2e49758569d",
         HashAlgorithm.SHA384SUM);
      list.add(htdByteScalar33);

      HashTestData<? extends Object> htdByteScalar34 = new HashTestData<Byte>(
         new Byte((byte)127),
         "75eb69a43e3bbcff322ec624ae7511cf3ad99df84b90d48b2665c70dff548c4857d4446c1eb04535bf54daa96e2cf5c3d5203d1fb43bbf4d40301bab95ac7772",
         HashAlgorithm.SHA512SUM);
      list.add(htdByteScalar34);
   }
}
