package org.Java;

import jdk.internal.util.xml.impl.Pair;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class DataStructures {

    public static void main(String[] args){
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1,2,3));
        List<Integer> lst = new ArrayList<>(Arrays.asList(2,34));
        System.out.println(arr);
        System.out.println(lst);

        for(int i=0; i<2; i++)
            arr.add(45+i+1);
        System.out.println(arr);

        Queue<Integer> qu = new LinkedList<Integer>(arr);
//        qu.add(78);
        System.out.println(qu.peek());


        Queue<Integer> pq = new PriorityQueue<>(arr); //min heap priority queue by default
        System.out.println(pq.peek());

        Queue<Integer> maxPq = new PriorityQueue<>(10, Collections.reverseOrder());

        Map<String, ArrayList<String>> bankRepository  = new HashMap<String, ArrayList<String>>(){{
                put("123337672", new ArrayList<>(Arrays.asList("4344238763478369","Nitya", "80000")));
                put("789438333", new ArrayList<>(Arrays.asList("6785041304724789","AC", "800000")));
                put("981234764", new ArrayList<>(Arrays.asList("2894044676954917","ANIL", "1000000")));
            }};
        System.out.println(bankRepository);

        for(String key: bankRepository.keySet()){
            System.out.println(key + "-> " + bankRepository.get(key) + "\n");
        }

//        ArrayList<Pair<Integer, String>> arrPr = new ArrayList<>();

    }
}
