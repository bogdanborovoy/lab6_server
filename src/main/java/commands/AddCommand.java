package commands;

import helpers.CollectionManager;
import helpers.Invoker;

import java.util.Scanner;

/**
 * Класс AddCommand
 * добавляет новый элемент в коллекцию
 * @author bogdanborovoy
 */
public class AddCommand implements Command {
    CollectionManager cm;
    Invoker invoker;
    String[] value;

    private boolean interactive = true;
    /**
     * Конструктор класса AddCommand.
     *
     * @param receiver Менеджер коллекции, который будет использоваться для добавления элемента
     * @param
     */
    public AddCommand(CollectionManager receiver) {
        this.cm = receiver;
    }
    public boolean isInteractive() {
        return interactive;
    }
    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    @Override
    public void passValue(String[] value) {
        this.value = value;
    }

    /**
     * Метод для выполнения команды добавления элемента в коллекцию.
     */
    public void execute() {
        Scanner sc = new Scanner(System.in);
        if (interactive) {
            cm.add(sc);
        }
        else{
            cm.add(value);
        }
    }

    /**
     * Метод для получения описания команды.
     *
     * @return Описание команды в виде строки
     */
    public String descr() {
        return "add : добавить новый элемент в коллекцию";
    }
}