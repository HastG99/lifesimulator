package org.example;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {


    public static int TOTAL_TICKS = 0;

    public static List<Person> persons = new CopyOnWriteArrayList<>();

    public static double treasury = 0;
    public static int delay = 20;

    public static int ed = 1;
    public static int med = 2;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            persons.add(new Person());
        }

        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (true) {
                tick();

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            String command = split[0];

            try {
                if(command.equalsIgnoreCase("kill") && split.length > 1) {
                    int count = Integer.parseInt(split[1]);

                    System.out.println("Killing " + count + " of people");

                    Random random = new Random();

                    for (int i = 0; i < count; i++) {
                        Person person = persons.get(random.nextInt(persons.size()));
                        person.die();
                    }
                }

                else if(command.equalsIgnoreCase("killp") && split.length > 1) {
                    double percent = (double) Integer.parseInt(split[1]) / 100;
                    int count = (int) (persons.size() * percent);

                    System.out.println("Killing " + count + " of people");

                    Random random = new Random();

                    for (int i = 0; i < count; i++) {
                        Person person = persons.get(random.nextInt(persons.size()));
                        person.die();
                    }
                }

                else if(command.equalsIgnoreCase("spawn") && split.length > 1) {
                    int count = Integer.parseInt(split[1]);

                    System.out.println("Spawning " + count + " people");

                    for (int i = 0; i < count; i++) {
                        persons.add(new Person());
                    }
                }

                else if(command.equalsIgnoreCase("spawngod")) {
                    Person person = new Person(
                            Person.TOTAL_MONEY,
                            Person.TOTAL_APP,
                            Person.TOTAL_SOCIAL,
                            Person.TOTAL_INT,
                            9,
                            4,
                            false
                    );

                    person.invincible = true;

                    persons.add(person);

                    System.out.println("GOD SPAWNED: " + person);
                }

                else if(command.equalsIgnoreCase("zero")) {
                    Person.TOTAL_MONEY = 0;
                    Person.TOTAL_INT = 0;
                    Person.TOTAL_SOCIAL = 0;
                    Person.TOTAL_APP = 0;
                    treasury = 0;
                    ed = 1;
                    med = 2;

                    for (Person person : persons) {
                        person.skills = 0;
                        person.salary = 0;
                        person.money = 0;
                        person.health = 20;
                        person.intellect = 0;
                        person.appear = 0;
                        person.social = 0;
                    }

                    System.out.println("ZEROED!");
                }

                else if(command.equalsIgnoreCase("covid")) {
                    for (Person person : persons) {
                        person.health = -10000;
                    }

                    System.out.println("COVID STARTED!");
                }

                else if(command.equalsIgnoreCase("speed") && split.length > 1) {
                    int speed = Integer.parseInt(split[1]);

                    delay = speed;

                    System.out.println("NOW SPEED IS " + speed);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void tick() {
        int i = 0;

        for(Person person : persons) {
            if(TOTAL_TICKS % 40 == 0) {
                double salary = person.salary;
                double fee = salary * 0.2;
                salary -= fee;

                person.money += salary;
                Person.TOTAL_MONEY += salary;
                treasury += fee;

//                System.out.printf("%d [salary]: salary=%f, money=%f, t=%f, fee=%f age=%d\n", person.id, salary, person.money, treasury, fee, person.age);

                person.salary = 0;
            }

            if(TOTAL_TICKS % 80 == 0) {
                person.age++;
                person.health--;
            }

            person.action();

            i++;
        }

        if(TOTAL_TICKS % 160 == 0 && persons.size() > 1) {
            Random random = new Random();

            if(random.nextInt(100) < 10) {
                Person person = persons.get(random.nextInt(persons.size()));

                person.health = 10;
            }

        }

        if(TOTAL_TICKS % 800 == 0) {
            double upCost = treasury / 4;

            System.out.println("\n\n");

            if(GDP() > 100 && treasury > upCost * 2) {
                med++;
                System.out.printf("MEDICINE UPPED: val: %d, cost=%.2f, tr=%.2f\n", med, upCost, treasury);
                treasury -= upCost;

                ed++;
                System.out.printf("EDUCATION UPPED: val: %d, cost=%.2f, tr=%.2f\n", ed, upCost, treasury);
                treasury -= upCost;
            }

            System.out.printf("(DAY:%d STATS) ALIVE: %d, DEAD: %d, BORN: %d, TR: %.2f, GDP: %.2f\n",
                    TOTAL_TICKS / 80,
                    persons.size(),
                    Person.TOTAL_DEAD,
                    Person.LAST_ID,
                    treasury,
                    treasury / persons.size()
            );

            if(!persons.isEmpty()) {
                System.out.printf("(DAY:%d AVG [TOTAL]) money=%.2f [%.2f], appear=%d [%d], social=%d [%d], intellect=%d [%d]\n",
                        TOTAL_TICKS / 80,
                        Person.TOTAL_MONEY / persons.size(),
                        Person.TOTAL_MONEY,
                        Person.TOTAL_APP / persons.size(),
                        Person.TOTAL_APP,
                        Person.TOTAL_SOCIAL / persons.size(),
                        Person.TOTAL_SOCIAL,
                        Person.TOTAL_INT / persons.size(),
                        Person.TOTAL_INT
                );

                Person rich = persons.stream().max(Comparator.comparing(person -> person.money)).get();
                System.out.println("RICHEST PERSON: " + rich);

                Person appear = persons.stream().max(Comparator.comparing(person -> person.appear)).get();
                System.out.println("MOST ATTRACTIVE: " + appear);

                Person social = persons.stream().max(Comparator.comparing(person -> person.social)).get();
                System.out.println("MOST SOCIABLE: " + social);

                Person intelligent = persons.stream().max(Comparator.comparing(person -> person.intellect)).get();
                System.out.println("MOST INTELLIGENT: " + intelligent);

            }
        }

        if(TOTAL_TICKS % 3200 == 0 && TOTAL_TICKS != 0) {
            Random random = new Random();

            if(random.nextInt(100) < 25 && persons.size() > 1) {
                List<Person> people = new ArrayList<>();

                persons.forEach(person -> {
                    if(person.age >= 18)
                        people.add(person);
                });

                Person person = people.get(random.nextInt(people.size()));

                double average = Person.TOTAL_MONEY / Main.persons.size();
                double cost = average * 12;
                double fee = cost * 0.2;

                person.money += (cost - fee);
                Person.TOTAL_MONEY += (cost - fee);

                treasury += fee;

                System.out.println();
                System.out.printf("Person won a lottery %.2f$ (fee: %.2f$, treasury: %.2f$)\n", cost, fee, treasury);
                System.out.println(person);
                System.out.println();
            }
            if(persons.size() < 60 && random.nextInt(100) < 50) {
                if(!persons.isEmpty()) {
                    System.out.println();
                    System.out.println("MIGRANTS EVENT STARTED! SPAWNING 50% PEOPLE");

                    int count = (int) (persons.size() * 0.5);

                    for (int k = 0; k < count; k++)
                        persons.add(new Person());

                    System.out.printf("SPAWNED %d people\n", count);
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println("ALL CITIZENS DEAD! The foreigners are coming!");

                    int count = random.nextInt(50) + 20;

                    for (int k = 0; k < count; k++)
                        persons.add(new Person());

                    System.out.printf("SPAWNED %d people\n", count);
                    System.out.println();
                }
            }

            else if(persons.size() > 100 && random.nextInt(100) < 30) {
                System.out.println();
                System.out.println("EPIDEMIC EVENT STARTED! KILLING 50% PEOPLE");

                int count = (int) (persons.size() * 0.5);

                for (int k = 0; k < count; k++) {
                    Person person = persons.get(random.nextInt(persons.size()));
                    person.die();
                }

                treasury *= 0.55;

                System.out.printf("KILLED %d people\n", count);
                System.out.println();
            }
            else if(persons.size() > 100 && random.nextInt(100) < 50) {
                System.out.println();
                System.out.println("WAR EVENT STARTED! KILLING 50% MANS");

                List<Person> mans = new CopyOnWriteArrayList<>();

                persons.forEach(person -> {
                    if(!person.girl && person.age >= 18)
                        mans.add(person);
                });

                int count = (int) (mans.size() * 0.5);

                for (int k = 0; k < count; k++) {
                    Person person = mans.get(random.nextInt(mans.size()));
                    person.die();

                    mans.remove(person);
                }

                treasury *= 0.55;

                System.out.printf("KILLED %d mans\n", count);
                System.out.println();
            }

            else if(persons.size() > 40 && random.nextInt(100) < 70) {
                List<Person> people = new ArrayList<>();

                persons.forEach(person -> {
                    if(person.age >= 18)
                        people.add(person);
                });

                Person person = people.get(random.nextInt(people.size()));

                double cost = treasury * 0.25;

                person.money += cost;
                Person.TOTAL_MONEY += cost;
                treasury -= cost;

                System.out.println();
                System.out.printf("Person stolen %.2f$ from treasury(%.2f$)\n", cost, treasury);
                System.out.println(person);
                System.out.println();
            }

            else if(persons.isEmpty() || (persons.size() > 40 && random.nextInt(100) < 10)) {
                System.out.println();
                System.out.println("DEFAULT EVENT STARTED! WIPE!");

                treasury = 1;
                ed = 1;
                med = 2;

                Person.TOTAL_MONEY = 0;

                for (Person person : persons) {
                    person.money = 0;
                    person.salary = 0;
                }

                System.out.println();
            }

        }

        TOTAL_TICKS++;
    }

    public static double GDP() {
        if (persons.isEmpty())
            return 0;


        return treasury / persons.size();
    }



}