package uz.java.maniac.asaxiy_bot.utils;

import java.util.ArrayList;
import java.util.List;

public class TestInterface <T>{
     public List<T> pageable(List<T> objectList, int page, int size){

         List<T> pageList = new ArrayList<>();
         if (objectList!=null&&page>0&&size>0){
             int listSize=objectList.size();

//             if (listSize<size){
//                 pageList=objectList.subList((page-1)*size,listSize);
//             }

             pageList=objectList.subList((page-1)*size,(page-1)*size+size);
         }
         return pageList;
    }


    public int totalPages(List<T> objectList,int size){
         int pages= (int) Math.ceil((double) objectList.size()/size);
         return pages;
    }


}
