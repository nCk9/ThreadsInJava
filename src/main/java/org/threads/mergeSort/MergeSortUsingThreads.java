package org.threads.mergeSort;

public class MergeSortUsingThreads {
    //synchronized keyword is not needed here because even if lets say two threads access this method at the same time,
    //then also the data set both would have is different. So any one thread can be assigned first.

    //we need synchronized keyword only when there's some common data gets updated by more than one thread.
    //synchronized keyword avoids the race condition!
    public static void merge(int [] arr, int low, int mid, int high){
        int sz1 = mid - low + 1;
        int sz2 = high - mid;

        int [] aux1 = new int[sz1];
        int [] aux2 = new int[sz2];

        //creating the auxiallary arrays
        for(int i=0; i<sz1; i++){ //considering elements till mid!
            aux1 [i] = arr[i+low];
        }

        for(int j=0; j<sz2; j++){ //considering elements from mid+1 to high
            aux2 [j] = arr[mid+j+1];
        }

        //merging both the sorted auxilliary arrays
        int i=0, j=0, k=low;
        while(i < sz1 || j < sz2){
            while(i < sz1 && j < sz2){
                if(aux1[i] < aux2[j])
                    arr[k++] = aux1[i++];
                else
                    arr[k++] = aux2[j++];
            }
            while(i<sz1)
                arr[k++] = aux1[i++];
            while(j<sz2)
                arr[k++] = aux2[j++];
        }

    }

    public static void mergeSort(int[] arr, int low, int high) throws InterruptedException {
        if(low < high){
            int mid = low + (high-low)/2;
            Runnable leftSplit = new Runnable() {
                @Override
                public void run() {
                    try {
                        mergeSort(arr, low, mid);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
//                    merge(arr, low, mid, high);
                }
            };

            Runnable rightSplit = new Runnable() {
                @Override
                public void run() {
                    try {
                        mergeSort(arr, mid+1, high);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            };
            Thread t1 = new Thread(leftSplit);
            Thread t2 = new Thread(rightSplit);
            t1.start();
            t2.start();

            t1.join();
            t2.join();
            //need to wait for both threads to finish before merging!
            merge(arr, low, mid, high);
        }
    }

    public static void main(String [] args) throws InterruptedException {
        int [] arr  = {37,10,13,456,-17,17,-88,456,10,29387,-3876,8934,30984,-4983, -9,-12,89,90};
        int len = arr.length;
        for (int j : arr) System.out.print(j + " ");

        mergeSort(arr, 0, len-1);
        System.out.println("\nArray post sorting : ");
        for(int i: arr) System.out.print(i + " ");
    }
}
