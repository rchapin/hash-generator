package com.ryanchapin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ryanchapin.util.HashGenerator;
import com.ryanchapin.util.HashGenerator.DataType;

public class ClientTest implements Runnable {

   private List<Thread> threads;

   /**
    * Map of results, keyed by data type which stores a Map of results
    * keyed by hashed value and a count of each value.
    */
   private Map<DataType, Map<String, Integer>> resultMap;

   /**
    * The number of threads that we be created for each type.
    */
   private int numThreadsPerType;

   /**
    * The number of times that each Runnable will loop through the
    * data set for a given type.
    */
   private int numIterPerThread;

   /** -- Accessor/Mutators ------------------------------------------------ */

   public synchronized void putResult(
         DataType type, Map<String, Integer> inputMap)
   {
      // For each element in the map, update the counts in the result map
      // for each data type
      Map<String, Integer> destinationMap = resultMap.get(type);

      String hashKey   = null;
      Integer inputVal = null;
      for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
         hashKey  = entry.getKey();
         inputVal = entry.getValue();

         // Get the current value in the destinationMap
         int currentVal = 0;
         if (destinationMap.containsKey(hashKey)) {
            currentVal = destinationMap.get(hashKey).intValue();
         }
         Integer updatedVal = currentVal + inputVal;

         // Update the value in the resultMap
         destinationMap.put(hashKey, updatedVal);
      }
   }

   public Map<DataType, Map<String, Integer>> getResultMap() {
      return resultMap;
   }

   /** -- Constructor ------------------------------------------------------ */

   public ClientTest(int numThreadsPerType, int numIterPerThread) {
      this.numThreadsPerType = numThreadsPerType;
      this.numIterPerThread  = numIterPerThread;
      threads = new ArrayList<>();
      resultMap = new HashMap<>();

      // Initialize the map with a complete set of result lists
      for (DataType type : DataType.values()) {
         resultMap.put(type, new HashMap<String, Integer>());
      }
   }

   /** -- Member Methods --------------------------------------------------- */

   public void init() {
      for (HashGenerator.DataType type : HashGenerator.DataType.values()) {
         for (int i = 0; i < numThreadsPerType; i++) {
            ClientRunnable r = new ClientRunnable(this, type, numIterPerThread);
            Thread t = new Thread(r);
            threads.add(t);
         }
      }
   }

   @Override
   public void run() {
      for (Thread t : threads) {
         t.start();
      }
      for (Thread t : threads) {
         try {
            t.join();
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
}
