package com.lru;

/**
 * @Author: Mo Jianyue
 * @Description
 * @Date: 2022/7/27 2:24 PM
 * @Modified By
 */
public class ArrayLru<T> implements LRU<T>{
    private T[] array;

    public ArrayLru(int size){
        array = (T[]) new Object[size];
    }


    @Override
    public void add(T o) {
        // 找到旧位置
        int position = array.length-1;
        for (int i = 0; i < array.length; i++) {
            if (o.equals(array[i])){
                position = i;
                break;
            }
        }
        // 以上for循环，可以通过引入一个map记录下标，检查循环的概率 参考 https://segmentfault.com/a/1190000021329931
        moveRight(position);
        array[0] = o;
        print();
    }

    private void moveRight(int position) {
        for (int i = position; i > 0; i--) {
            // 向后挪动 如果position为数组的最后一个，则会被淘汰
            array[i] = array[i-1];
        }
    }

    public void print(){
        for (T t : array) {
            System.out.print(t+",");
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        ArrayLru<Integer> lru = new ArrayLru<Integer>(5);
        lru.add(3);
        lru.add(4);
        lru.add(5);
        lru.add(7);
        lru.add(4);
        lru.add(2);
        lru.add(7);
        lru.add(8);
    }

}
