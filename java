import java.util.Random;

public class MaxSubsequenceSum {

    public static void main(String[] args) {
        int[] testData = generateRandomArray(100);  //Generate random test data

        //measure time for Exhaustive Algorithm
        long startTime1 = System.nanoTime();  
        int[] result1 = maxSubSum1(testData);  
        long endTime1 = System.nanoTime();  
        long duration1 = endTime1 - startTime1;  

        System.out.println("Exhaustive - Max Sum: " + result1[0]);
        System.out.println("Start Index: " + result1[1]);
        System.out.println("End Index: " + result1[2]);
        System.out.println("CPU Time: " + duration1 + " ns");

        //measure time for Improved Exhaustive Algorithm
        long startTime2 = System.nanoTime();
        int[] result2 = maxSubSum2(testData);  
        long endTime2 = System.nanoTime();  
        long duration2 = endTime2 - startTime2;  

        System.out.println("\nImproved Exhaustive - Max Sum: " + result2[0]);
        System.out.println("Start Index: " + result2[1]);
        System.out.println("End Index: " + result2[2]);
        System.out.println("CPU Time: " + duration2 + " ns");

        //measure time for Divide and Conquer Algorithm
        long startTime3 = System.nanoTime();
        int[] result3 = maxSumRec(testData, 0, testData.length - 1);  
        long endTime3 = System.nanoTime();  
        long duration3 = endTime3 - startTime3;  

        System.out.println("\nDivide and Conquer - Max Sum: " + result3[0]);
        System.out.println("Start Index: " + result3[1]);
        System.out.println("End Index: " + result3[2]);
        System.out.println("CPU Time: " + duration3 + " ns");

        //measure time for Dynamic Algorithm
        long startTime4 = System.nanoTime();
        int[] result4 = dynamicMaxSubSum(testData);  
        long endTime4 = System.nanoTime();  
        long duration4 = endTime4 - startTime4;  

        System.out.println("\nDynamic Algorithm - Max Sum: " + result4[0]);
        System.out.println("Start Index: " + result4[1]);
        System.out.println("End Index: " + result4[2]);
        System.out.println("CPU Time: " + duration4 + " ns");

    }

    //method to generate random test data
    public static int[] generateRandomArray(int size) {
        int[] randomArray = new int[size];
        Random rand = new Random();
        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = rand.nextInt(201) - 100;  //random number between -100 and 100
        }
        return randomArray;
    }

    //Algorithm #1: Exhaustive Example
    public static int[] maxSubSum1(int[] arr) {
        int maxSum = Integer.MIN_VALUE;
        int start = 0, end = 0;
        
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += arr[k];
                }
                if (sum > maxSum) {
                    maxSum = sum;
                    start = i;
                    end = j;
                }
            }
        }
        return new int[]{maxSum, start, end};  //return max sum, start index, and end index
    }

    //Algorithm #2: Improved Exhaustive Example
    public static int[] maxSubSum2(int[] arr) {
        int maxSum = Integer.MIN_VALUE;  // Initialize max sum
        int start = 0, end = 0;  // To store the start and end indices of the max subarray
        
        for (int i = 0; i < arr.length; i++) {
            int sum = 0;  //reset sum for each new starting point

            for (int j = i; j < arr.length; j++) {  // `j` starts from `i`
                sum += arr[j];  //accumulate the sum from arr[i] to arr[j]

                if (sum > maxSum) {
                    maxSum = sum;
                    start = i;
                    end = j;
                }
            }
        }
        return new int[]{maxSum, start, end};  //return max sum, start index, and end index
    }

    // Algorithm #3: Divide and Conquer
    public static int[] maxSumRec(int[] arr, int left, int right) {
        //Base Case (if there's only one element)
        if (left == right) {
            return new int[]{arr[left], left, left};  // max sum, start index, end index
        }

        //find the middle point of the array
        int middle = (left + right) / 2;

        //recursively calculate max sum in the left and right halves
        int[] leftResult = maxSumRec(arr, left, middle);       // Max sum in the left half
        int[] rightResult = maxSumRec(arr, middle + 1, right); // Max sum in the right half

        //find the maximum sum that crosses the middle point
        int[] crossResult = maxCrossingSum(arr, left, middle, right);  // Max crossing sum

        //return the maximum of the three results
        if (leftResult[0] >= rightResult[0] && leftResult[0] >= crossResult[0]) {
            return leftResult;  //left half has the maximum sum
        } else if (rightResult[0] >= leftResult[0] && rightResult[0] >= crossResult[0]) {
            return rightResult;  //right half has the maximum sum
        } else {
            return crossResult;  //crossing subarray has the maximum sum
        }
    }

    //helper function-find the maximum crossing sum
    private static int[] maxCrossingSum(int[] arr, int left, int middle, int right) {
        //find the max sum on the left side of the middle
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        int maxLeft = middle;  //track the index

        for (int i = middle; i >= left; i--) {
            sum += arr[i];
            if (sum > leftSum) {
                leftSum = sum;
                maxLeft = i;
            }
        }

        //find the max sum on the right side of the middle
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        int maxRight = middle + 1;  //track the index

        for (int i = middle + 1; i <= right; i++) {
            sum += arr[i];
            if (sum > rightSum) {
                rightSum = sum;
                maxRight = i;
            }
        }

        //combine the left and right sums
        int totalSum = leftSum + rightSum;
        return new int[]{totalSum, maxLeft, maxRight};  //return the total sum and boundary indices
    }

    //Algorithm #4: Dynamic Algorithm
    public static int[] dynamicMaxSubSum(int[] arr) {
        //initialize variables
        int maxSum = Integer.MIN_VALUE;  //global maximum sum
        int currentSum = 0;              //current subarray sum
        int start = 0, end = 0, tempStart = 0;  //tracking indices
    
        //iterate through the array
        for (int i = 0; i < arr.length; i++) {
            //decide whether to start a new subarray or extend the current subarray
            if (currentSum <= 0) {
                currentSum = arr[i];
                tempStart = i;  //update temporary start index
            } else {
                currentSum += arr[i];  //extend the current subarray
            }
    
            //update the global max sum if the current subarray is better
            if (currentSum > maxSum) {
                maxSum = currentSum;
                start = tempStart;  //update the start index to the tempStart
                end = i;            //update the end index to the current index
            }
        }
    
        //return the result (max sum, start index, end index)
        return new int[]{maxSum, start, end};
    }
    
}
