package HeadFirst;

import HeadFirst.behavior.fly.FlyRocket;

public class Test {
    public static void main(String[] args){
        Duck duck = new ModelDuck();
        duck.setFlyBehavior(new FlyRocket());
        duck.performQuack();
        duck.performFly();
        duck.display();
        duck.swim();
    }
}
