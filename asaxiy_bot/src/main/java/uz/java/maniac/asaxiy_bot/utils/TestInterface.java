package uz.java.maniac.asaxiy_bot.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestInterface <T>{
     public List<T> pageable(List<T> objectList, int page, int size){

         List<T> pageList = new ArrayList<>();

         int totalPages = totalPages(objectList, size);



         if (totalPages<page||page<=0||size<=0){
//             pageList=objectList.subList(0,size);
             return pageList;
         }

         int listSize=objectList.size();
         if (page==totalPages){
             pageList=objectList.subList((page-1)*size,listSize);
             return pageList;
         }
//             if (listSize<size){
//                 pageList=objectList.subList((page-1)*size,listSize);
//             }

         pageList=objectList.subList((page-1)*size,(page-1)*size+size);
         return pageList;
    }


    public int totalPages(List<T> objectList,int size){
         int pages= (int) Math.ceil((double) objectList.size()/size);
         return pages;
    }





    public String parseString(String str,int index){
        try {
            String[] parts = str.split("-");
            System.out.println(Arrays.toString(parts));
            if (index<0||parts.length<=index) return "";
            return parts[index];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


    public Integer parseInt(String str){
        try {
            String[] parts = str.split("-");
            return Integer.parseInt(parts[1]);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public String parseString(String str){
        try {
            String[] parts = str.split("-");
            return parts[0];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String parseString3th(String str){
        try {
            String[] parts = str.split("-");
            return parts[2];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
