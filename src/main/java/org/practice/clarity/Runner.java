package org.practice.clarity;

public class Runner {
    public static void main(String[] args) {

        Exp<Products> exp = new Exp<>(Products.FLIGHTS);
        System.out.println(exp.getElement());

//        Exp<String, Products> exp = new Exp<>("hotels", Products.HOTELS);
//        System.out.println(exp.getElement());
//        Context context = new Context();
//        context.handle(Products.HOTELS);
//
//        Utils utils = new Utils(context);

        //https://chat.openai.com/share/fdfa6088-ebbf-4662-9696-a717e550ff4c


        /*
         * openGrid().getModule().getNeededMethod();
         *
         *      class Grid {
         *             Module module;
         *             Grid(){
         *               this.module = page(Module.class)
         *             }
         *       }
         *
         *        class Module{
         *          public void getNeededMethod(){
         *           }
         *       }
         *
         */
    }
}
