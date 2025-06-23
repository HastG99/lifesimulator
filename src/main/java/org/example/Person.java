package org.example;

import java.util.Random;

public class Person {

    public static int LAST_ID = 0;
    public static int TOTAL_APP = 0;
    public static int TOTAL_INT = 0;
    public static int TOTAL_SOCIAL = 0;
    public static double TOTAL_MONEY = 0;
    public static int TOTAL_DEAD = 0;

    public final int id;
    public double money;
    public int appear;
    public int social;
    public int skills = 0;
    public int intellect;
    public double salary = 0;
    public int lucky;
    public final boolean girl;
    public final int action;

    public int age = 1;
    public int health = 1;

    public int kids = 0;

    public boolean invincible = false;



    private final Random random = new Random();

    public Person(int age) {
        this.id = LAST_ID++;
        this.money = 0;
        this.appear = random.nextInt(10);
        this.social = random.nextInt(10);
        this.intellect = random.nextInt(10);
        this.lucky = random.nextInt(10);
        this.girl = random.nextBoolean();
        this.action = random(1, 12);
        this.age = age;
        this.health = random(10, 20);

        TOTAL_APP += appear;
        TOTAL_INT += intellect;
        TOTAL_SOCIAL += social;
    }

    public Person(double money, int appear, int social, int intellect, int lucky, int action, boolean girl) {
        this.id = LAST_ID++;
        this.money = money;
        this.appear = appear;
        this.social = social;
        this.intellect = intellect;
        this.lucky = lucky;
        this.girl = girl;
        this.action = action;
        this.health = random(10, 20);

        TOTAL_MONEY += money;
        TOTAL_APP += appear;
        TOTAL_INT += intellect;
        TOTAL_SOCIAL += social;
    }

    public Person() {
        this.id = LAST_ID++;
        this.money = 0;
        this.appear = random.nextInt(10);
        this.social = random.nextInt(10);
        this.intellect = random.nextInt(10);
        this.lucky = random.nextInt(10);
        this.girl = random.nextBoolean();
        this.action = random(1, 12);
        this.age = random(1, 10);
        this.health = random(10, 20);

        TOTAL_APP += appear;
        TOTAL_INT += intellect;
        TOTAL_SOCIAL += social;
    }

    public void die() {
        if(TOTAL_APP < 0)
            System.exit(0);

        if(TOTAL_INT < 0)
            System.exit(0);

        if(TOTAL_SOCIAL < 0)
            System.exit(0);


        TOTAL_APP -= appear;
        TOTAL_INT -= intellect;
        TOTAL_SOCIAL -= social;

        Person person = Main.persons.get(random.nextInt(Main.persons.size()));
        person.money += money;

        Main.persons.remove(this);

        TOTAL_DEAD++;
    }

    public void action() {
        int r = random(1, 12);

        if(random(25))
            r = action;

        if(age > 60 && random(0, 1000) <= 5 && !invincible) {
            die();

            return;
        }

        if(age >= 18 && health < 20 && random(0, 100) <= 1 && !invincible) {
            die();

            return;
        }

        if(r == 1 && age <= 60) {
            // workout
            int s = random(0, 4);

            if(age < 30) {
                if(appear < 1000) {
                    appear += s;
                    TOTAL_APP += s;
                }
            }
            else {
                if(s > appear)
                    return;

                TOTAL_APP -= s;
                appear -= s;
            }

//            System.out.printf("%d [train]: appear=%d\n", id, appear);
//            System.out.println(this);
        }

        else if(r == 2 && age <= 60) {
            // study
            int s = random(0, Main.ed);

            if(age < 30) {
                if(intellect < 1000) {
                    intellect += s;
                    TOTAL_INT += s;
                }
            }
            else {
                if(s > intellect)
                    return;

                TOTAL_INT -= s;
                intellect -= s;
            }


//            System.out.printf("%d [train]: appear=%d\n", id, intellect);
//            System.out.println(this);
        }

        else if(r == 3) {
            // social
            int s = random(0, 4);

            if(age <= 30 && social < 1000) {
                social += s;
                TOTAL_SOCIAL += s;
            }

            if(age < 18 || age > 50)
                return;

            int chance = random(1, 5);

            if(average(TOTAL_MONEY, 50)  < money && random(chance))
                reproduce();

            else if(average(TOTAL_APP, 50) < appear && random(chance))
                reproduce();

            else if(average(TOTAL_SOCIAL, 50) < social && random(chance))
                reproduce();

            else if(average(TOTAL_INT, 50) < intellect && random(chance))
                reproduce();

//            System.out.printf("%d [social]: social=%d\n", id, intellect);
//            System.out.println(this);
        }

        else if(r == 4 && age > 12 && age <= 60) {
            // work
            double gdp = Main.GDP() / 4;
            double s = random(1, gdp > 100 ? (int) gdp : 100);

            if(skills > 0)
                s += s * (double) skills / 1000;

            if(lucky > 0)
                s += s * (double) lucky / 1000;

            if(intellect > 0)
                s += s * (double) intellect / 1000;

            salary += s;

            if(age <= 50)
                skills += random(0, 5);
            else {
                int m = random(5, 10);

                if(m < skills)
                    skills -= m;
            }

//            System.out.printf("%d [work] -  %f: sal=%f,  lk=%d, sk=%d, i=%d\n", id, s, salary, lucky, skills, intellect);
//            System.out.println(this);
        }

        else if(r == 5) {
            // shopping

            if(random(3)) {
                double average = TOTAL_MONEY / Main.persons.size();
                double cost = average * 12;

                if(money > cost) {
                    money -= cost;
                    TOTAL_MONEY -= cost;

                    Main.treasury += cost * 0.25;
                }
            }

            int s = random(1, 25);

            double cost = money * ((double) s / 100);

            if(kids > 1)
                cost += cost * kids;

            if(money == 0)
                return;

            if(cost > money)
                cost = money;

            if(age < 30 && appear < 1000) {
                TOTAL_APP += s;
                appear += s;
            }

            money -= cost;
            TOTAL_MONEY -= cost;

            Main.treasury += cost * 0.1;

//            System.out.printf("%d [work] -  %f: sal=%f,  lk=%d, sk=%d, i=%d\n", id, s, salary, lucky, skills, intellect);
//            System.out.println(this);
        }

        else if(r == 6) {
            // health
            int heal = random(0, Main.med);

            if(Main.GDP() < 100)
                return;

            double cost = Main.GDP() / 10;

            if(cost < 1)
                return;

            Main.treasury -= cost;

            if(age <= 60)
                health += heal;

//            System.out.printf("%d [work] -  %f: sal=%f,  lk=%d, sk=%d, i=%d\n", id, s, salary, lucky, skills, intellect);
//            System.out.println(this);
        }

        else {

            if(skills > 0) {
                skills--;
            }

            if(appear > 0) {
                appear--;
                TOTAL_APP--;
            }

            if(intellect > 0) {
                intellect--;
                TOTAL_INT--;
            }

            if(social > 0) {
                social--;
                TOTAL_SOCIAL--;
            }

        }


    }

    public static int average(int total, int percentage) {
        double part = ((double) total / Main.persons.size());
        part += part * ((double) percentage / 100);

        return (int) part;
    }

    public static double average(double total, int percentage) {
        double part = (total / Main.persons.size());
        part += part * ((double) percentage / 100);

        return part;
    }

    public void reproduce() {
        Person partner = Main.persons.get(random.nextInt(Main.persons.size()));

        if(partner.equals(this))
            return;

        if(partner.girl == girl)
            return;

        if(partner.age < 18 || partner.age >= 50)
            return;

        if(Main.persons.size() > 500)
            return;

        Person person = new Person(0);
        person.appear = random(0, (partner.appear / 4 + appear / 4) / 2);
        person.intellect = random(0, (partner.intellect / 4 + intellect / 4) / 2);
        person.social = random(0, (partner.social / 4 + social / 4) / 2);

        TOTAL_SOCIAL += person.social;
        TOTAL_INT += person.intellect;
        TOTAL_APP += person.appear;

        double mCost = money * 0.5;
        double pCost = partner.money * 0.5;

        partner.money -= pCost;
        money -= mCost;

        TOTAL_MONEY -= mCost + pCost;
        Main.treasury += mCost * 0.1 + pCost * 0.1;


/*        System.out.printf("\nNEW PERSON: \nfrom:\n%s;\n%s \nperson: %s\n\n" +
                "" +
                "", this, partner, person);*/



        kids++;
        partner.kids++;

        Main.persons.add(person);
    }

    public int random() {
        return random.nextInt(100);
    }

    public boolean random(int n) {
        return random.nextInt(100) < n;
    }

    public int random(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", money=" + String.format("%.2f", money) +
                ", appear=" + appear +
                ", social=" + social +
                ", skills=" + skills +
                ", intellect=" + intellect +
                ", salary=" + String.format("%.2f", salary) +
                ", lucky=" + lucky +
                ", girl=" + girl +
                ", age=" + age +
                ", action=" + action +
                ", kids=" + kids +
                ", health=" + health +
                '}';
    }
}
