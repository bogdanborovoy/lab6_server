package commands;

import classes.SpaceMarine;
import helpers.CollectionManager;

/**
 * Класс SaveCommand
 * сохраняет коллекцию в файл
 * @author bogdanborovoy
 */
public class SaveCommand implements Command {
    CollectionManager cm;
    private boolean interactive;
    String[] value;
    SpaceMarine spaceMarine;
    public boolean isInteractive() {
        return interactive;
    }
    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }
    @Override
    public void passSpaceMarine(SpaceMarine spaceMarine) {
        this.spaceMarine = spaceMarine;
    }
    @Override
    public void passValue(String[] value) {

    }

    /**
     * Конструктор класса SaveCommand.
     *
     * @param cm Менеджер коллекции, который будет использоваться для сохранения коллекции в файл
     */
    public SaveCommand(CollectionManager cm) {
        this.cm = cm;
    }

    /**
     * Метод для выполнения команды сохранения коллекции в файл.
     */
    public void execute() {
        cm.save(cm.spaceMarines);
    }

    /**
     * Метод для получения описания команды.
     *
     * @return Описание команды в виде строки
     */
    public String descr() {
        return "save : сохранить коллекцию в файл";
    }
}