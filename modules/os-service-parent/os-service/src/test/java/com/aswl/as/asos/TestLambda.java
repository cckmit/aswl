package com.aswl.as.asos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.asos.common.utils.IbrsResponseBean;
import com.aswl.as.asos.common.utils.OsHttpUtil;

import java.util.*;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestLambda {

    public static void main(String[] args) {

        try{

            TestLambda test=new TestLambda();

            // 类型声明
            MathOperation addition = (a,b)->a + b;

            MathOperation subtraction=(a,b)->a-b;

            System.out.println(test.operate(5,10,addition));
            System.out.println(test.operate(5,10,subtraction));

            List<TestPerson> list= Arrays.asList(new TestPerson(3,"张三",10),new TestPerson(4,"李四",60),new TestPerson(5,"王五",80));

            System.out.println("最小值结果为"+list.stream().min((p1,p2)->p1.getAge()-p2.getAge()).get().getAge());

            System.out.println("最大值结果为"+list.stream().max((p1,p2)->p1.getAge()-p2.getAge()).get().getAge());

            System.out.println("平均值为"+list.stream().mapToInt((p)->p.getAge()).average().getAsDouble());

            System.out.println("最小值为"+list.stream().mapToInt(p->p.getAge()).min().getAsInt());

            List<Integer> intList=Arrays.asList(7,3,199,100);
            intList.sort((v1,v2)->v1-v2);
            System.out.println("intList="+intList);

            System.out.println("数字数组平均值为="+intList.stream().mapToInt(v -> v).average().getAsDouble());

            list.forEach(p->{
                p.setLevel(p.getAge()>70?"A":p.getAge()<30?"C":"B");
                System.out.println("id="+p.getId()+"level="+p.getLevel());
            });

            list.stream().map(p->p.getName()).forEach(System.out::println);

            System.out.println(list.stream().mapToInt(p->p.getAge()).min().getAsInt());


            System.out.println("------------ 下面是自定义Consumer ----------------");
            Consumer<TestPerson> c=p->System.out.println(p.getName());
            list.forEach(c);
            System.out.println("------------ 下面是自定义Function ----------------");
            Function<TestPerson,Integer> f=p->p.getAge();
            list.stream().map(f).forEach(System.out::println);
            System.out.println("------------ 下面是自定义Predicate ----------------");
            Predicate<TestPerson> pre=p->p.getAge()<70;
            list.stream().filter(pre).forEach(c);
            List<TestPerson> testPersonList=list.stream().filter(pre).collect(Collectors.toList());
            System.out.println("------------ 下面是第二个Predicate -------------");
            pre=pre.and(p->p.getName().indexOf("张")>-1);
            list.stream().filter(pre).forEach(c);

            System.out.println("------------ 下面是约简操作 ------------------");

            System.out.println(list.stream().mapToInt(p->p.getAge()).reduce((a,b)->a+b).getAsInt());
            System.out.println(list.stream().mapToInt(p->p.getAge()).reduce(200,(a,b)->a+b));
            System.out.println(list.stream().mapToInt(p->p.getAge()).reduce(200,Integer::sum));

            System.out.println("------------ 下面是自定义操作 ------------------");

            System.out.println(list.stream().mapToInt(p->p.getAge()).toArray());
            System.out.println(list.stream().map(p->p.getAge()).collect(Collectors.toList()));

            Runnable runnable;

            DoubleAdder d;

//            System.out.println(Base64.getEncoder().encode(Base64.getDecoder().decode("12")));

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    interface MathOperation {
        int operation(int a, int b);
    }

    private int operate(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }





}
