package com.hepexta.interview.algorithms.sorting;

public class QuickSort {

    private static int traverseCount = 0;

    public static int sort(int arr[]){
        traverseCount = 0;
        sort(arr, 0, arr.length-1);
        return traverseCount;
    }

    public static void sort(int arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
            // Recursively sort elements of the 2 sub-arrays
            sort(arr, begin, partitionIndex-1);
            sort(arr, partitionIndex+1, end);
        }
    }

    private static int partition(int arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin-1);

        for (int j=begin; j<end; j++) {
            traverseCount++;
            if (arr[j] <= pivot) {
                i++;
                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        int swapTemp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = swapTemp;

        return i+1;
    }
}
