package org.threads.mergeSort;

public class MergeSortImpl {
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

    public static void mergeSort(int[] arr, int low, int high) {
        if(low < high){
            int mid = low + (high-low)/2;
            mergeSort(arr, low, mid);
            mergeSort(arr, mid+1, high);
            merge(arr, low, mid, high);
        }
    }

    public static void main(String [] args) throws InterruptedException {
        int [] arr  = {37,10,13,456,-17,-88,456,10,29387,-3876,8934,30984,-4983};
        int len = arr.length;
        for (int j : arr) System.out.print(j + " ");
        //spawning a thread here is equivalent to not spawning any thread!
        Runnable sorting = new Runnable() {
            @Override
            public void run() {
                mergeSort(arr, 0, len-1);
            }
        };
//        mergeSort(arr, 0, len-1);
        Thread t1 = new Thread(sorting);
        t1.start();
        t1.join();
        System.out.println("\nArray post sorting : ");
        for(int i: arr) System.out.print(i + " ");
    }
}
