package HeadFirst;

import HeadFirst.behavior.fly.FlyNoWay;
import HeadFirst.behavior.quack.QuackSqueak;

public class ModelDuck extends Duck {
    public ModelDuck(){
        flyBehavior = new FlyNoWay();
        quackBehavior = new QuackSqueak();
    }

    public void display(){
        System.out.println("I'm a modal duck");
    }
}
