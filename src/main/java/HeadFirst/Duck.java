package HeadFirst;

import HeadFirst.behavior.fly.FlyBehavior;
import HeadFirst.behavior.quack.QuackBehavior;

public abstract class Duck {
    //为行为接口声明两个引用变量
    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    public  abstract void display();

    //委托给行为类
    public void performFly(){
        flyBehavior.fly();
    }
    public void performQuack(){
        quackBehavior.quack();
    }

    public void swim(){
        System.out.println("All duck can float.");
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void setQuackBehavior(QuackBehavior quackBehavior) {
        this.quackBehavior = quackBehavior;
    }
}
