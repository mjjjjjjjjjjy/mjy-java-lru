package com.lru;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Mo Jianyue
 * @Description
 * @Date: 2022/7/28 4:13 PM
 * @Modified By
 */

public class LinkLru<K, V> {

    private class Entry<K,V>{
        Entry next;
        Entry pre;
        K key;
        V value;

        public Entry(){}
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    // 虚拟的头部和尾部节点，永远存在
    private Entry<K,V> head, tail;

    private int capacity;
    private int size;
    private Map<K,Entry<K,V>> map;

    public LinkLru(int capacity) {
        this.capacity = capacity;
        head = new Entry<>();
        tail = new Entry<>();
        head.next = tail;
        tail.pre = head;
        map = new HashMap<>(capacity);
    }

    public void put(K k,V v){
        Entry<K, V> kvEntry = map.get(k);
        if (kvEntry != null){
            // 缓存是存在的，移动到尾部
            //修改值
            kvEntry.value = v;
            if (kvEntry != tail.pre){
                moveToTail(kvEntry);
            }
        } else {
            // 新增了
            if (size == capacity){
                // 移除头部
                Entry<K, V> next = head.next;
                remove(next);
                K key = next.key;
                map.remove(key);
                size --;
            }
            Entry<K, V> newEntry = new Entry<>(k, v);
            addToTail(newEntry);
            map.put(k, newEntry);
            size++;
        }
        ToString();
    }


    public V get(K k){
        Entry<K,V> v = map.get(k);
        if (null == v){
            return null;
        }
        moveToTail(v);
        return v.value;
    }

    private void moveToTail(Entry<K, V> v) {
        remove(v);
        addToTail(v);
    }

    private void addToTail(Entry<K, V> v) {
        if (v != null){
            // 从最后一个非虚拟点开始连起来
            Entry pre = tail.pre;
            pre.next = v;
            v.pre = pre;
            v.next = tail;
            tail.pre = v;
        }
    }

    private void remove(Entry<K, V> v) {
        if (v != null){
            Entry pre = v.pre;
            Entry next = v.next;
            // 将前后连起来
            pre.next = next;
            next.pre = pre;
        }
    }

    public void ToString(){
        Entry<K,V> entry = head;
        while (true){
            entry = entry.next;
            if (entry == tail){
                break;
            }
            V value = entry.value;
            System.out.print(value+"   ");
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        LinkLru<Integer, Integer> linkLru = new LinkLru<>(3);
        linkLru.put(1,1);
        linkLru.put(2,2);
        linkLru.put(3,3);
        linkLru.put(1,1);
        linkLru.put(4,4);
        System.out.println(linkLru.get(4));

    }

}
